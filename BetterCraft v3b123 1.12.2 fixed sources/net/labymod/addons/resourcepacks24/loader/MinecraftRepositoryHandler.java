// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.loader;

import java.beans.ConstructorProperties;
import net.labymod.addons.resourcepacks24.asm.ResourcepackMethods;
import java.util.Collection;
import java.util.ArrayList;
import java.io.File;
import java.util.List;
import java.util.Iterator;
import net.labymod.addons.resourcepacks24.gui.elements.LocalPackElement;
import net.minecraft.client.resources.ResourcePackRepository;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;

public class MinecraftRepositoryHandler
{
    private PackRepositoryLoader loader;
    
    public void unloadUnselectedPacks() {
        Minecraft.getMinecraft().getResourcePackRepository().getRepositoryEntries().forEach(new Consumer<ResourcePackRepository.Entry>() {
            @Override
            public void accept(final ResourcePackRepository.Entry entry) {
                boolean isSelected = false;
                for (final LocalPackElement packEntry : MinecraftRepositoryHandler.this.loader.getRepository().getSelected()) {
                    if (packEntry.getPackMeta().getFileName().equals(entry.getResourcePackName())) {
                        isSelected = true;
                    }
                }
                if (!isSelected) {
                    entry.closeResourcePack();
                }
            }
        });
    }
    
    public void loadSelectedPacks() {
        final List<String> mcSettingList = Minecraft.getMinecraft().gameSettings.resourcePacks;
        for (final String mcPackName : mcSettingList) {
            for (final LocalPackElement element : this.loader.getRepository().getRepository()) {
                final File file = element.getPackMeta().file;
                String path = element.getPackMeta().getFileName();
                if (!this.loader.getDirectory().equals(file.getParentFile())) {
                    path = String.valueOf(file.getParentFile().getName()) + "/" + path;
                }
                if (path.equals(mcPackName)) {
                    this.loader.getRepository().getSelected().add(element);
                    break;
                }
            }
        }
    }
    
    public void saveResourceList() {
        final List<String> mcSettingList = Minecraft.getMinecraft().gameSettings.resourcePacks;
        mcSettingList.clear();
        for (final LocalPackElement packEntry : this.loader.getRepository().getSelected()) {
            final File file = packEntry.getPackMeta().file;
            if (this.loader.getDirectory().equals(file.getParentFile())) {
                mcSettingList.add(file.getName());
            }
            else {
                mcSettingList.add(String.valueOf(file.getParentFile().getName()) + "/" + file.getName());
            }
        }
        Minecraft.getMinecraft().gameSettings.resourcePacks = new ArrayList<String>(mcSettingList);
        Minecraft.getMinecraft().gameSettings.saveOptions();
    }
    
    public void refreshResources() {
        try {
            final ResourcePackRepository mcRepository = Minecraft.getMinecraft().getResourcePackRepository();
            mcRepository.updateRepositoryEntriesAll();
            final List<ResourcePackRepository.Entry> entries = new ArrayList<ResourcePackRepository.Entry>();
            for (final LocalPackElement element : this.loader.getRepository().getSelected()) {
                if (!this.loader.isSelected(element)) {
                    continue;
                }
                final ResourcePackRepository.Entry target = this.getEntryByPackElement(mcRepository, element);
                if (target == null) {
                    continue;
                }
                entries.add(target);
            }
            mcRepository.setRepositories(entries);
            Minecraft.getMinecraft().refreshResources();
        }
        catch (final Throwable error) {
            error.printStackTrace();
        }
    }
    
    public ResourcePackRepository.Entry getEntryByPackElement(final ResourcePackRepository mcRepository, final LocalPackElement element) {
        for (final ResourcePackRepository.Entry entry : mcRepository.getRepositoryEntriesAll()) {
            final String pathName = ResourcepackMethods.modifyName(element.getPackMeta().getFileName(), element.getPackMeta().file);
            if (pathName.equals(entry.getResourcePackName())) {
                return entry;
            }
        }
        return null;
    }
    
    @ConstructorProperties({ "loader" })
    public MinecraftRepositoryHandler(final PackRepositoryLoader loader) {
        this.loader = loader;
    }
}
