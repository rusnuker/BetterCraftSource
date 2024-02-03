// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.gui.views;

import net.labymod.addons.resourcepacks24.gui.elements.LocalPackElement;
import java.util.List;
import net.labymod.addons.resourcepacks24.loader.PackRepositoryLoader;
import net.labymod.addons.resourcepacks24.gui.elements.basement.PackElement;
import net.labymod.addons.resourcepacks24.gui.GuiResourcepacks24;

public class SelectedView extends View
{
    public SelectedView(final GuiResourcepacks24 gui) {
        super(gui, "Selected packs");
    }
    
    @Override
    public void renderPre(final int mouseX, final int mouseY) {
        super.renderPre(mouseX, mouseY);
        if (!this.loaded) {
            return;
        }
        final double entryWidth = this.width - (this.scrollbar.isHidden() ? 0 : 5);
        final double entryHeight = 32.0;
        double listY = this.y + 3.0 + this.scrollbar.getScrollY();
        final List<LocalPackElement> entries = this.gui.getRepository().getSelected();
        final int total = entries.size();
        for (int index = total - 1; index >= 0; --index) {
            this.drawPack(entries.get(index), this.x, listY, entryWidth, 32.0, true, total - index - 1, total, mouseX, mouseY);
            listY += 35.0;
        }
        this.drawPack(PackRepositoryLoader.DEFAULT_MINECRAFT, this.x, listY, this.width, 32.0, true, -1, -1, mouseX, mouseY);
        this.postRenderCursorY = listY - this.scrollbar.getScrollY();
    }
}
