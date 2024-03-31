// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

class TextEntry implements GuiListExtended.IGuiListEntry
{
    private final String text;
    private final int color;
    protected final Minecraft mc;
    
    public TextEntry(final Minecraft mc, final String text) {
        this(mc, text, 1048575);
    }
    
    public TextEntry(final Minecraft mc, final String text, final int color) {
        this.mc = mc;
        this.text = text;
        this.color = color;
    }
    
    @Override
    public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected) {
        if (y < 0) {
            return;
        }
        Utils.drawStringWithShadow(this.text, x, y + 1, this.color);
    }
    
    @Override
    public boolean mousePressed(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
        return false;
    }
    
    @Override
    public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
    }
    
    @Override
    public void setSelected(final int slotIndex, final int p_178011_2_, final int p_178011_3_) {
    }
}
