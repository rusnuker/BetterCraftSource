// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.loader;

import java.beans.ConstructorProperties;
import java.util.Collection;
import java.util.ArrayList;
import java.io.IOException;
import com.google.common.io.Files;
import java.util.List;
import java.util.Collections;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.awt.image.BufferedImage;
import net.minecraft.client.Minecraft;
import net.labymod.utils.ModColor;
import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;
import javax.imageio.ImageIO;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.io.InputStream;
import java.io.FileInputStream;
import net.labymod.addons.resourcepacks24.gui.elements.FolderElement;
import java.util.function.Consumer;
import net.labymod.addons.resourcepacks24.loader.model.PackMeta;
import net.labymod.addons.resourcepacks24.loader.model.MCPack;
import java.io.File;
import net.labymod.addons.resourcepacks24.gui.elements.LocalPackElement;
import com.google.gson.Gson;

public class PackRepositoryLoader
{
    private static final Gson GSON;
    public static final LocalPackElement DEFAULT_MINECRAFT;
    private File directory;
    private Repository repository;
    private MinecraftRepositoryHandler minecraftRepositoryHandler;
    
    static {
        GSON = new Gson();
        DEFAULT_MINECRAFT = new LocalPackElement(new PackMeta(null, new MCPack(1, "The default look of Minecraft"), "Default"));
    }
    
    public PackRepositoryLoader(final File directory) {
        this.repository = new Repository();
        this.minecraftRepositoryHandler = new MinecraftRepositoryHandler(this);
        this.directory = directory;
    }
    
    public void loadAsync() {
        this.loadAsync(null);
    }
    
    public void loadAsync(final Consumer<Repository> consumer) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PackRepositoryLoader.this.load(PackRepositoryLoader.this.directory, false);
                PackRepositoryLoader.this.minecraftRepositoryHandler.loadSelectedPacks();
                if (consumer != null) {
                    consumer.accept(PackRepositoryLoader.this.repository);
                }
            }
        }).start();
    }
    
    private void load(final File directory, final boolean inSubFolder) {
        final File[] list = directory.listFiles();
        if (list == null) {
            return;
        }
        if (inSubFolder) {
            this.repository.getSubFolders().add(new FolderElement(directory));
        }
        else {
            this.repository.clear();
        }
        File[] array;
        for (int length = (array = list).length, i = 0; i < length; ++i) {
            final File file = array[i];
            final String fileName = file.getName();
            try {
                if (file.isDirectory()) {
                    final File fileMcMeta = new File(file, "pack.mcmeta");
                    final File filePng = new File(file, "pack.png");
                    if (fileMcMeta.exists()) {
                        this.loadPack(file, fileName, new FileInputStream(fileMcMeta), filePng.exists() ? new FileInputStream(filePng) : null);
                    }
                    else if (!inSubFolder) {
                        this.load(file, true);
                    }
                }
                else if (fileName.endsWith(".zip")) {
                    final ZipFile zipFile = new ZipFile(file);
                    final ZipEntry mcMetaEntry = zipFile.getEntry("pack.mcmeta");
                    if (mcMetaEntry != null) {
                        final ZipEntry pngEntry = zipFile.getEntry("pack.png");
                        final String name = fileName.substring(0, fileName.lastIndexOf(46));
                        this.loadPack(file, name, zipFile.getInputStream(mcMetaEntry), (pngEntry == null) ? null : zipFile.getInputStream(pngEntry));
                        zipFile.close();
                    }
                }
            }
            catch (final Exception error) {
                error.printStackTrace();
            }
        }
    }
    
    private void loadPack(final File file, final String name, final InputStream mcMetaInput, final InputStream pngInput) throws Exception {
        final BufferedImage bufferedImage = (pngInput == null) ? null : ImageIO.read(pngInput);
        final String json = IOUtils.toString(mcMetaInput, StandardCharsets.UTF_8);
        PackMeta modelPackMeta;
        try {
            modelPackMeta = PackRepositoryLoader.GSON.fromJson(json, PackMeta.class);
        }
        catch (final Exception e) {
            modelPackMeta = new PackMeta(new MCPack(1, String.valueOf(ModColor.cl('c')) + e.getMessage()));
        }
        final PackMeta packMeta = modelPackMeta;
        packMeta.displayName = name;
        packMeta.file = file;
        if (bufferedImage != null) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    packMeta.icon = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("texturepackicon", new DynamicTexture(bufferedImage));
                }
            });
        }
        final LocalPackElement packEntry = new LocalPackElement(packMeta);
        this.repository.getRepository().add(packEntry);
        if (this.directory.equals(file.getParentFile())) {
            this.repository.getRootPacks().add(packEntry);
        }
    }
    
    public boolean selectPack(final LocalPackElement packEntry) {
        return !this.isSelected(packEntry) && this.repository.getSelected().add(packEntry);
    }
    
    public boolean unselectPack(final LocalPackElement packEntry) {
        return this.repository.getSelected().remove(packEntry);
    }
    
    public boolean isSelected(final LocalPackElement packEntry) {
        return this.repository.getSelected().contains(packEntry);
    }
    
    public void swapSelection(final LocalPackElement first, final LocalPackElement second) {
        if (first == second) {
            return;
        }
        final boolean firstSelected = this.isSelected(first);
        final boolean secondSelected = this.isSelected(second);
        if (firstSelected == secondSelected) {
            if (firstSelected) {
                this.unselectPack(first);
                this.unselectPack(second);
            }
            else {
                this.selectPack(first);
                this.selectPack(second);
            }
            return;
        }
        final LocalPackElement currentSelected = firstSelected ? first : second;
        final LocalPackElement currentUnselected = firstSelected ? second : first;
        final int index = this.getSelectedIndexOf(currentSelected);
        this.repository.getSelected().add(index, currentUnselected);
        this.unselectPack(currentSelected);
    }
    
    public void swap(final LocalPackElement packEntry, final int offset) {
        final int target = this.getSelectedIndexOf(packEntry);
        Collections.swap(this.repository.getSelected(), target, target + offset);
    }
    
    public void swap(final LocalPackElement packEntry, final LocalPackElement targetEntry) {
        if (packEntry != targetEntry) {
            Collections.swap(this.repository.getSelected(), this.getSelectedIndexOf(packEntry), this.getSelectedIndexOf(targetEntry));
        }
    }
    
    private int getSelectedIndexOf(final LocalPackElement packEntry) {
        for (int i = 0; i < this.repository.getSelected().size(); ++i) {
            if (this.repository.getSelected().get(i).equals(packEntry)) {
                return i;
            }
        }
        return -1;
    }
    
    public boolean movePack(final LocalPackElement packEntry, final FolderElement folder) {
        if (folder == null) {
            if (packEntry.getPackMeta().file.getParentFile().equals(this.directory)) {
                return true;
            }
        }
        else if (folder.contains(packEntry)) {
            return true;
        }
        this.minecraftRepositoryHandler.unloadUnselectedPacks();
        final File packFile = packEntry.getPackMeta().file;
        final File newPackFile = (folder == null) ? new File(this.directory, packFile.getName()) : new File(folder.getDirectory(), packFile.getName());
        if (!packFile.isDirectory()) {
            try {
                Files.move(packFile, newPackFile);
                packEntry.getPackMeta().file = newPackFile;
                if (folder == null) {
                    this.repository.rootPacks.add(packEntry);
                }
                else {
                    this.repository.rootPacks.remove(packEntry);
                }
                return true;
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    public File getDirectory() {
        return this.directory;
    }
    
    public Repository getRepository() {
        return this.repository;
    }
    
    public MinecraftRepositoryHandler getMinecraftRepositoryHandler() {
        return this.minecraftRepositoryHandler;
    }
    
    public static class Repository
    {
        private List<LocalPackElement> repository;
        private List<FolderElement> subFolders;
        private List<LocalPackElement> rootPacks;
        private List<LocalPackElement> selected;
        
        public void clear() {
            this.repository.clear();
            this.subFolders.clear();
            this.rootPacks.clear();
            this.selected.clear();
        }
        
        public Repository clone() {
            return new Repository(new ArrayList<LocalPackElement>(this.repository), new ArrayList<FolderElement>(this.subFolders), new ArrayList<LocalPackElement>(this.rootPacks), new ArrayList<LocalPackElement>(this.selected));
        }
        
        public List<LocalPackElement> getRepository() {
            return this.repository;
        }
        
        public List<FolderElement> getSubFolders() {
            return this.subFolders;
        }
        
        public List<LocalPackElement> getRootPacks() {
            return this.rootPacks;
        }
        
        public List<LocalPackElement> getSelected() {
            return this.selected;
        }
        
        @ConstructorProperties({ "repository", "subFolders", "rootPacks", "selected" })
        public Repository(final List<LocalPackElement> repository, final List<FolderElement> subFolders, final List<LocalPackElement> rootPacks, final List<LocalPackElement> selected) {
            this.repository = new ArrayList<LocalPackElement>();
            this.subFolders = new ArrayList<FolderElement>();
            this.rootPacks = new ArrayList<LocalPackElement>();
            this.selected = new ArrayList<LocalPackElement>();
            this.repository = repository;
            this.subFolders = subFolders;
            this.rootPacks = rootPacks;
            this.selected = selected;
        }
        
        public Repository() {
            this.repository = new ArrayList<LocalPackElement>();
            this.subFolders = new ArrayList<FolderElement>();
            this.rootPacks = new ArrayList<LocalPackElement>();
            this.selected = new ArrayList<LocalPackElement>();
        }
    }
}
