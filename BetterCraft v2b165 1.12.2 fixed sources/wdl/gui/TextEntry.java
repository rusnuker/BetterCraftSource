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
    public void func_192634_a(final int p_192634_1_, final int p_192634_2_, final int p_192634_3_, final int p_192634_4_, final int p_192634_5_, final int p_192634_6_, final int p_192634_7_, final boolean p_192634_8_, final float p_192634_9_) {
        if (p_192634_3_ < 0) {
            return;
        }
        Utils.drawStringWithShadow(this.text, p_192634_2_, p_192634_3_ + 1, this.color);
    }
    
    @Override
    public boolean mousePressed(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
        return false;
    }
    
    @Override
    public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
    }
    
    @Override
    public void func_192633_a(final int p_192633_1_, final int p_192633_2_, final int p_192633_3_, final float p_192633_4_) {
    }
}
