// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.gui.elements.basement;

import java.util.Iterator;
import net.labymod.addons.resourcepacks24.loader.PackRepositoryLoader;
import net.labymod.addons.resourcepacks24.gui.elements.LocalPackElement;
import net.labymod.addons.resourcepacks24.gui.elements.FolderElement;
import net.labymod.addons.resourcepacks24.gui.GuiResourcepacks24;
import net.labymod.main.ModTextures;
import net.labymod.gui.elements.GuiImageButton;

public abstract class DeletableElement extends Element
{
    protected GuiImageButton buttonDelete;
    protected boolean deleteable;
    
    public DeletableElement(final boolean deleteable) {
        this.deleteable = deleteable;
        (this.buttonDelete = new GuiImageButton(ModTextures.MISC_BLOCKED)).setImageAlpha(0.3f);
    }
    
    protected abstract String getElementName();
    
    public void drawControls(final double x, final double y, final double width, final double height, final boolean isFirstEntry, final boolean isLastEntry, final boolean isSelected, final int mouseX, final int mouseY, final GuiResourcepacks24 gui) {
        if (!this.deleteable) {
            return;
        }
        if (gui.getSharedView().draggingElement == null) {
            this.buttonDelete.draw(x + width - 10.0 - 5.0, y, 10.0, mouseX, mouseY);
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton, final GuiResourcepacks24 gui) {
        super.mouseClicked(mouseX, mouseY, mouseButton, gui);
        if (!this.buttonDelete.isMouseOver(mouseX, mouseY)) {
            return;
        }
        this.delete(gui);
    }
    
    private void delete(final GuiResourcepacks24 gui) {
        final PackRepositoryLoader packLoader = gui.getResourcepacks24().getPackLoader();
        if (this instanceof FolderElement) {
            final FolderElement packFolder = (FolderElement)this;
            for (final LocalPackElement packEntry : gui.getRepository().getRepository()) {
                if (packFolder.contains(packEntry)) {
                    packLoader.movePack(packEntry, null);
                }
            }
            if (packFolder.getDirectory().delete()) {
                gui.getResourcepacks24().getPackLoader().getMinecraftRepositoryHandler().saveResourceList();
                gui.reloadRepositories();
            }
            else {
                gui.getSharedView().lastErrorMessage = "Can't delete folder because something in there is still loaded";
            }
        }
        if (this instanceof LocalPackElement) {
            final LocalPackElement element = (LocalPackElement)this;
            packLoader.getMinecraftRepositoryHandler().unloadUnselectedPacks();
            if (element.getPackMeta().file.delete()) {
                gui.reloadRepositories();
            }
            else {
                gui.getSharedView().lastErrorMessage = "Can't delete pack because it's still loaded";
            }
        }
    }
    
    public GuiImageButton getButtonDelete() {
        return this.buttonDelete;
    }
}
