// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.Minecraft;

class LinkEntry extends TextEntry
{
    private final String link;
    private final int textWidth;
    private final int linkWidth;
    
    public LinkEntry(final Minecraft mc, final String text, final String link) {
        super(mc, text, 5592575);
        this.link = link;
        this.textWidth = mc.fontRendererObj.getStringWidth(text);
        this.linkWidth = mc.fontRendererObj.getStringWidth(link);
    }
    
    @Override
    public void func_192634_a(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected, final float t) {
        if (y < 0) {
            return;
        }
        super.func_192634_a(slotIndex, x, y, listWidth, slotHeight, mouseX, mouseY, isSelected, t);
        final int relativeX = mouseX - x;
        final int relativeY = mouseY - y;
        if (relativeX >= 0 && relativeX <= this.textWidth && relativeY >= 0 && relativeY <= slotHeight) {
            int drawX = mouseX - 2;
            if (drawX + this.linkWidth + 4 > listWidth + x) {
                drawX = listWidth + x - (4 + this.linkWidth);
            }
            Gui.drawRect(drawX, mouseY - 2, drawX + this.linkWidth + 4, mouseY + this.mc.fontRendererObj.FONT_HEIGHT + 2, Integer.MIN_VALUE);
            Utils.drawStringWithShadow(this.link, drawX + 2, mouseY, 16777215);
        }
    }
    
    @Override
    public boolean mousePressed(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
        if (relativeX >= 0 && relativeX <= this.textWidth) {
            Utils.openLink(this.link);
            return true;
        }
        return false;
    }
}
