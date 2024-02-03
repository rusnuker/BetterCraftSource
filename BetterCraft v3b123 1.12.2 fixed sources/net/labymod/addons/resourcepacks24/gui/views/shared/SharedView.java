// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.gui.views.shared;

import net.labymod.addons.resourcepacks24.gui.views.SelectedView;
import net.labymod.addons.resourcepacks24.gui.elements.FolderElement;
import net.labymod.addons.resourcepacks24.gui.views.OfflineView;
import net.labymod.addons.resourcepacks24.gui.elements.LocalPackElement;
import net.labymod.addons.resourcepacks24.loader.PackRepositoryLoader;
import java.util.Iterator;
import java.util.List;
import net.labymod.utils.ModColor;
import net.labymod.utils.DrawUtils;
import net.labymod.addons.resourcepacks24.gui.elements.basement.PackElement;
import net.labymod.main.LabyMod;
import net.labymod.addons.resourcepacks24.gui.elements.basement.DraggableElement;
import net.labymod.addons.resourcepacks24.gui.views.View;
import net.labymod.addons.resourcepacks24.gui.elements.basement.Element;
import net.labymod.addons.resourcepacks24.gui.GuiResourcepacks24;

public class SharedView
{
    private GuiResourcepacks24 gui;
    public Element hoverElement;
    public View hoverView;
    public DraggableElement.Dragging draggingElement;
    public String lastErrorMessage;
    public boolean changes;
    
    public SharedView(final GuiResourcepacks24 gui) {
        this.hoverElement = null;
        this.hoverView = null;
        this.draggingElement = null;
        this.lastErrorMessage = null;
        this.changes = false;
        this.gui = gui;
    }
    
    public void preRender(final int mouseX, final int mouseY) {
        this.hoverView = null;
        this.hoverElement = null;
    }
    
    public void postRender(final int mouseX, final int mouseY) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        if (this.draggingElement != null && this.draggingElement.getElement() instanceof PackElement && this.draggingElement.isValid()) {
            this.draggingElement.getElement().draw(mouseX - this.draggingElement.getOffsetX(), mouseY - this.draggingElement.getOffsetY(), this.draggingElement.getWidth(), this.draggingElement.getHeight(), mouseX, mouseY);
        }
        if (this.lastErrorMessage != null) {
            final int errorWidth = draw.getWidth() / 3;
            final List<String> list = draw.listFormattedStringToWidth(this.lastErrorMessage, errorWidth);
            int errorY = draw.getHeight() - 15 - list.size() * 10;
            DrawUtils.drawRect(draw.getWidth() / 2.0 - errorWidth / 2.0, errorY - 5, draw.getWidth() / 2.0 + errorWidth / 2.0, errorY + list.size() * 10 + 5, Integer.MIN_VALUE);
            for (final String line : list) {
                draw.drawCenteredString(String.valueOf(ModColor.cl('c')) + line, draw.getWidth() / 2, errorY);
                errorY += 10;
            }
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        this.lastErrorMessage = null;
        if (this.hoverElement != null) {
            this.hoverElement.mouseClicked(mouseX, mouseY, mouseButton, this.gui);
            if (this.hoverElement != PackRepositoryLoader.DEFAULT_MINECRAFT && this.hoverElement instanceof LocalPackElement) {
                this.draggingElement = new DraggableElement.Dragging((DraggableElement)this.hoverElement, mouseX, mouseY);
            }
        }
    }
    
    public void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        if (this.draggingElement != null && !this.draggingElement.getElement().getButtonDelete().isMouseOver(mouseX, mouseY)) {
            this.draggingElement.setValid(true);
        }
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        try {
            final PackRepositoryLoader packLoader = this.gui.getResourcepacks24().getPackLoader();
            if (this.draggingElement != null && this.draggingElement.isValid()) {
                if (this.draggingElement.getElement() instanceof LocalPackElement) {
                    final LocalPackElement draggingElement = (LocalPackElement)this.draggingElement.getElement();
                    final boolean draggingSelected = packLoader.isSelected(draggingElement);
                    if (this.hoverElement == null) {
                        if (this.hoverView != null) {
                            if (this.hoverView instanceof OfflineView) {
                                if (packLoader.unselectPack(draggingElement)) {
                                    this.changes = true;
                                }
                                if (!packLoader.movePack(draggingElement, null)) {
                                    this.lastErrorMessage = "Unnable to move pack because it's still loaded or it's an pack-directory!";
                                }
                            }
                            if (this.hoverView instanceof SelectedView && packLoader.selectPack(draggingElement)) {
                                this.changes = true;
                            }
                            this.gui.initGui();
                        }
                    }
                    else if (this.hoverElement instanceof LocalPackElement) {
                        final LocalPackElement targetElement = (LocalPackElement)this.hoverElement;
                        final boolean targetSelected = packLoader.isSelected(targetElement);
                        if (targetSelected && draggingSelected) {
                            packLoader.swap(draggingElement, targetElement);
                        }
                        else if (targetSelected != draggingSelected) {
                            packLoader.swapSelection(targetElement, draggingElement);
                        }
                        this.changes = true;
                    }
                    else if (this.hoverElement instanceof FolderElement) {
                        final FolderElement folder = (FolderElement)this.hoverElement;
                        if (!packLoader.movePack(draggingElement, folder)) {
                            this.lastErrorMessage = "Unnable to move pack because it's still loaded or it's an pack-directory!";
                        }
                        if (packLoader.unselectPack(draggingElement)) {
                            this.changes = true;
                        }
                    }
                }
                this.gui.initGui();
            }
            else if (this.hoverElement != null && this.hoverElement instanceof LocalPackElement) {
                final LocalPackElement packElement = (LocalPackElement)this.hoverElement;
                packElement.mouseReleased(mouseX, mouseY, state, this.gui);
                this.changes = true;
            }
            this.draggingElement = null;
        }
        catch (final Exception error) {
            error.printStackTrace();
        }
    }
    
    public boolean isInsideView(final int mouseX, final int mouseY) {
        for (final View view : this.gui.getViews()) {
            if (view.isInsideView(mouseX, mouseY)) {
                return true;
            }
        }
        return false;
    }
}
