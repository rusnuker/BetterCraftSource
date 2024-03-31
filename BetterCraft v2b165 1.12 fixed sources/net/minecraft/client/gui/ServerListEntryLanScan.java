// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.Minecraft;

public class ServerListEntryLanScan implements GuiListExtended.IGuiListEntry
{
    private final Minecraft mc;
    
    public ServerListEntryLanScan() {
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void func_192634_a(final int p_192634_1_, final int p_192634_2_, final int p_192634_3_, final int p_192634_4_, final int p_192634_5_, final int p_192634_6_, final int p_192634_7_, final boolean p_192634_8_, final float p_192634_9_) {
        final int i = p_192634_3_ + p_192634_5_ / 2 - this.mc.fontRendererObj.FONT_HEIGHT / 2;
        final FontRenderer fontRendererObj = this.mc.fontRendererObj;
        final String format = I18n.format("lanServer.scanning", new Object[0]);
        final GuiScreen currentScreen = Minecraft.currentScreen;
        fontRendererObj.drawString(format, GuiScreen.width / 2 - this.mc.fontRendererObj.getStringWidth(I18n.format("lanServer.scanning", new Object[0])) / 2, i, 16777215);
        String s = null;
        switch ((int)(Minecraft.getSystemTime() / 300L % 4L)) {
            default: {
                s = "O o o";
                break;
            }
            case 1:
            case 3: {
                s = "o O o";
                break;
            }
            case 2: {
                s = "o o O";
                break;
            }
        }
        final FontRenderer fontRendererObj2 = this.mc.fontRendererObj;
        final String text = s;
        final GuiScreen currentScreen2 = Minecraft.currentScreen;
        fontRendererObj2.drawString(text, GuiScreen.width / 2 - this.mc.fontRendererObj.getStringWidth(s) / 2, i + this.mc.fontRendererObj.FONT_HEIGHT, 8421504);
    }
    
    @Override
    public void func_192633_a(final int p_192633_1_, final int p_192633_2_, final int p_192633_3_, final float p_192633_4_) {
    }
    
    @Override
    public boolean mousePressed(final int slotIndex, final int mouseX, final int mouseY, final int mouseEvent, final int relativeX, final int relativeY) {
        return false;
    }
    
    @Override
    public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
    }
}
