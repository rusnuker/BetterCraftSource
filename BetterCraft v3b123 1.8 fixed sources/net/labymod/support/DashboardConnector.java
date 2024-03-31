// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.support;

import net.labymod.utils.Consumer;
import net.labymod.utils.DrawUtils;
import net.labymod.labyconnect.LabyConnect;
import net.minecraft.util.Session;
import net.labymod.main.lang.LanguageManager;
import net.labymod.utils.manager.TooltipHelper;
import net.labymod.main.ModTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;

public class DashboardConnector
{
    private long lastRequest;
    private boolean hoverIcon;
    
    public DashboardConnector() {
        this.lastRequest = -1L;
        this.hoverIcon = false;
    }
    
    public void renderIcon(final int x, final int y, final int mouseX, final int mouseY) {
        final Session session = Minecraft.getMinecraft().getSession();
        final LabyConnect labyConnect = LabyMod.getInstance().getLabyConnect();
        if (session == null || session.getProfile() == null || !labyConnect.isOnline() || !labyConnect.getClientConnection().isPinAvailable()) {
            return;
        }
        final int iconSize = 20;
        final int gearSize = 13;
        this.hoverIcon = (mouseX > x - 10 && mouseX < x + 10 && mouseY > y - 10 && mouseY < y + 10);
        final int hoverAnimation = this.hoverIcon ? 1 : 0;
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        if (this.isOnCooldown()) {
            GlStateManager.color(0.5f, 0.5f, 0.5f);
        }
        else {
            GlStateManager.color(1.0f, 1.0f, 1.0f);
        }
        draw.drawPlayerHead(session.getProfile(), x - 10 - hoverAnimation, y - 10 - hoverAnimation, 20 + hoverAnimation * 2);
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.BUTTON_ADVANCED);
        draw.drawTexture(x - hoverAnimation, y - hoverAnimation, 255.0, 255.0, 13 + hoverAnimation * 2, 13 + hoverAnimation * 2);
        if (this.hoverIcon) {
            TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, LanguageManager.translate("open_dashboard_website"));
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.hoverIcon) {
            this.openDashboard();
        }
    }
    
    private boolean isOnCooldown() {
        return System.currentTimeMillis() - this.lastRequest < 5000L;
    }
    
    public void openDashboard() {
        final LabyConnect labyConnect = LabyMod.getInstance().getLabyConnect();
        if (!labyConnect.isOnline() || !labyConnect.getClientConnection().isPinAvailable()) {
            LabyMod.getInstance().openWebpage("http://www.labymod.net/dashboard", false);
        }
        else if (!this.isOnCooldown()) {
            this.lastRequest = System.currentTimeMillis();
            labyConnect.getClientConnection().requestPin(new Consumer<String>() {
                @Override
                public void accept(final String pin) {
                    LabyMod.getInstance().openWebpage(String.format("http://www.labymod.net/key/?id=%s&pin=%s", LabyMod.getInstance().getPlayerUUID(), pin), false);
                }
            });
        }
    }
}
