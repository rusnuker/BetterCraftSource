// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.network.LanServerInfo;
import net.minecraft.client.Minecraft;

public class ServerListEntryLanDetected implements GuiListExtended.IGuiListEntry
{
    private final GuiMultiplayer screen;
    protected final Minecraft mc;
    protected final LanServerInfo serverData;
    private long lastClickTime;
    
    protected ServerListEntryLanDetected(final GuiMultiplayer p_i47141_1_, final LanServerInfo p_i47141_2_) {
        this.screen = p_i47141_1_;
        this.serverData = p_i47141_2_;
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void func_192634_a(final int p_192634_1_, final int p_192634_2_, final int p_192634_3_, final int p_192634_4_, final int p_192634_5_, final int p_192634_6_, final int p_192634_7_, final boolean p_192634_8_, final float p_192634_9_) {
        this.mc.fontRendererObj.drawString(I18n.format("lanServer.title", new Object[0]), p_192634_2_ + 32 + 3, p_192634_3_ + 1, 16777215);
        this.mc.fontRendererObj.drawString(this.serverData.getServerMotd(), p_192634_2_ + 32 + 3, p_192634_3_ + 12, 8421504);
        if (this.mc.gameSettings.hideServerAddress) {
            this.mc.fontRendererObj.drawString(I18n.format("selectServer.hiddenAddress", new Object[0]), p_192634_2_ + 32 + 3, p_192634_3_ + 12 + 11, 3158064);
        }
        else {
            this.mc.fontRendererObj.drawString(this.serverData.getServerIpPort(), p_192634_2_ + 32 + 3, p_192634_3_ + 12 + 11, 3158064);
        }
    }
    
    @Override
    public boolean mousePressed(final int slotIndex, final int mouseX, final int mouseY, final int mouseEvent, final int relativeX, final int relativeY) {
        this.screen.selectServer(slotIndex);
        if (Minecraft.getSystemTime() - this.lastClickTime < 250L) {
            this.screen.connectToSelected();
        }
        this.lastClickTime = Minecraft.getSystemTime();
        return false;
    }
    
    @Override
    public void func_192633_a(final int p_192633_1_, final int p_192633_2_, final int p_192633_3_, final float p_192633_4_) {
    }
    
    @Override
    public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
    }
    
    public LanServerInfo getServerData() {
        return this.serverData;
    }
}
