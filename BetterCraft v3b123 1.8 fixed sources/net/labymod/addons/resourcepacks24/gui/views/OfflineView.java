// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.gui.views;

import java.io.IOException;
import java.awt.Desktop;
import net.minecraft.client.gui.GuiScreen;
import net.labymod.gui.elements.GuiTextboxPrompt;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.labymod.addons.resourcepacks24.gui.elements.basement.PackElement;
import net.labymod.addons.resourcepacks24.gui.elements.LocalPackElement;
import java.util.Iterator;
import java.util.function.Consumer;
import net.labymod.addons.resourcepacks24.Resourcepacks24;
import net.labymod.addons.resourcepacks24.gui.elements.FolderElement;
import net.labymod.addons.resourcepacks24.gui.GuiResourcepacks24;
import net.labymod.gui.elements.GuiImageButton;
import net.labymod.addons.resourcepacks24.loader.PackRepositoryLoader;

public class OfflineView extends View
{
    private PackRepositoryLoader loader;
    private GuiImageButton buttonCreateFolder;
    private GuiImageButton buttonOpenResourceFolder;
    
    public OfflineView(final GuiResourcepacks24 gui) {
        super(gui, "Your library");
        this.loader = gui.getResourcepacks24().getPackLoader();
        this.buttonCreateFolder = new GuiImageButton(FolderElement.TEXTURE_FOLDER, 0, 127, 85, 127);
        this.buttonOpenResourceFolder = new GuiImageButton(FolderElement.TEXTURE_FOLDER, 170, 0, 85, 127);
    }
    
    @Override
    public void loadRepository(final Resourcepacks24 context) {
        this.loaded = false;
        context.getPackLoader().loadAsync(new Consumer<PackRepositoryLoader.Repository>() {
            @Override
            public void accept(final PackRepositoryLoader.Repository repo) {
                OfflineView.this.gui.repository = repo.clone();
                OfflineView.this.gui.initGui();
                for (final View view : OfflineView.this.gui.getViews()) {
                    view.onLoaded(OfflineView.class);
                    view.onLoaded(SelectedView.class);
                }
            }
        });
    }
    
    @Override
    public void init(final double x, final double y, final double width, final double height, final double margin) {
        super.init(x, y, width, height, margin);
        this.buttonCreateFolder.init(x + 2.0, y - 12.0, 10.0);
        this.buttonOpenResourceFolder.init(x + width - 10.0 - 2.0, y - 12.0, 10.0);
    }
    
    @Override
    public void renderPre(final int mouseX, final int mouseY) {
        super.renderPre(mouseX, mouseY);
        if (!this.loaded) {
            return;
        }
        final double entryWidth = this.width - (this.scrollbar.isHidden() ? 0 : 5);
        final double entryHeight = entryWidth / 7.0;
        double listY = this.y + 3.0 + this.scrollbar.getScrollY();
        final PackRepositoryLoader.Repository repository = this.gui.getRepository();
        for (final FolderElement subFolder : repository.getSubFolders()) {
            int offsetX = 0;
            final int folderIconSize = 10;
            if (subFolder.draw(this.x, listY, entryWidth, 10.0, mouseX, mouseY)) {
                subFolder.drawControls(this.x, listY, this.width, this.height, false, false, false, mouseX, mouseY, this.gui);
                this.shared.hoverElement = subFolder;
            }
            listY += 13.0;
            offsetX += 13;
            if (subFolder.isExpanded()) {
                for (final LocalPackElement element : repository.getRepository()) {
                    if (subFolder.contains(element) && !this.loader.isSelected(element)) {
                        this.drawPack(element, this.x + offsetX, listY, entryWidth - offsetX, entryHeight, mouseX, mouseY);
                        listY += entryHeight + 3.0;
                    }
                }
            }
        }
        for (final LocalPackElement element2 : repository.getRootPacks()) {
            if (!this.loader.isSelected(element2)) {
                this.drawPack(element2, this.x, listY, entryWidth, entryHeight, mouseX, mouseY);
                listY += entryHeight + 3.0;
            }
        }
        this.postRenderCursorY = listY - this.scrollbar.getScrollY();
    }
    
    @Override
    public void renderPost(final int mouseX, final int mouseY) {
        super.renderPost(mouseX, mouseY);
        this.buttonCreateFolder.draw(mouseX, mouseY);
        this.buttonOpenResourceFolder.draw(mouseX, mouseY);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.buttonCreateFolder.isMouseOver(mouseX, mouseY)) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiTextboxPrompt(this.gui, "Folder name", "Create", "Cancel", "", new net.labymod.utils.Consumer<String>() {
                @Override
                public void accept(final String accepted) {
                    if (accepted.isEmpty()) {
                        return;
                    }
                    final File target = new File(OfflineView.this.loader.getDirectory(), accepted);
                    if (!target.exists()) {
                        if (target.mkdir()) {
                            OfflineView.this.gui.getResourcepacks24().getPackLoader().getMinecraftRepositoryHandler().saveResourceList();
                            OfflineView.this.gui.reloadRepositories();
                        }
                        else {
                            OfflineView.this.throwError("Can't create folder " + accepted + "!");
                        }
                    }
                    else {
                        OfflineView.this.throwError("Folder " + accepted + " already exists!");
                    }
                }
            }));
        }
        if (this.buttonOpenResourceFolder.isMouseOver(mouseX, mouseY)) {
            try {
                Desktop.getDesktop().open(Resourcepacks24.getInstance().resourcepacksDir);
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
        this.pendingScrollbarUpdate = true;
    }
}
