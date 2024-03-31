// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import java.util.concurrent.ExecutorService;
import net.labymod.core.ServerPingerData;
import net.labymod.utils.Consumer;
import net.labymod.core.LabyModCore;
import java.io.IOException;
import net.labymod.addons.teamspeak3.GuiTeamSpeak;
import net.minecraft.client.gui.achievement.GuiStats;
import me.nzxtercode.bettercraft.client.misc.irc.GuiIRC;
import net.labymod.labyconnect.gui.GuiFriendsLayout;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.client.multiplayer.WorldClient;
import java.util.List;
import wdl.WDLHooks;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.labymod.utils.manager.ServerInfoRenderer;

public class GuiIngameMenu extends GuiScreen
{
    private int field_146445_a;
    private int field_146444_f;
    private long lastUpdate;
    private long updateCooldown;
    private ServerInfoRenderer serverInfoRenderer;
    private ServerData serverData;
    
    public GuiIngameMenu() {
        this.lastUpdate = 0L;
        this.updateCooldown = 5000L;
        final String serverIp = Minecraft.getMinecraft().isIntegratedServerRunning() ? "localhost" : Minecraft.getMinecraft().getCurrentServerData().serverIP;
        final String serverName = Minecraft.getMinecraft().isIntegratedServerRunning() ? "localhost" : Minecraft.getMinecraft().getNetHandler().getNetworkManager().getRemoteAddress().toString();
        this.serverData = new ServerData(serverName, serverIp, false);
    }
    
    @Override
    public void initGui() {
        this.field_146445_a = 0;
        this.buttonList.clear();
        final int i = -16;
        final int j = 98;
        this.buttonList.add(new GuiButton(1, GuiIngameMenu.width / 2 - 100, GuiIngameMenu.height / 4 + 120 + i, I18n.format("menu.returnToMenu", new Object[0])));
        if (!this.mc.isIntegratedServerRunning()) {
            this.buttonList.get(0).displayString = I18n.format("menu.disconnect", new Object[0]);
        }
        this.buttonList.add(new GuiButton(4, GuiIngameMenu.width - 80, 5, 75, 20, "LabyMod"));
        this.buttonList.add(new GuiButton(0, GuiIngameMenu.width / 2 - 100, GuiIngameMenu.height / 4 + 96 + i, 98, 20, I18n.format("menu.options", new Object[0])));
        final GuiButton guibutton;
        this.buttonList.add(guibutton = new GuiButton(7, GuiIngameMenu.width / 2 + 2, GuiIngameMenu.height / 4 + 96 + i, 98, 20, I18n.format("menu.shareToLan", new Object[0])));
        this.buttonList.add(new GuiButton(5, GuiIngameMenu.width / 2 - 100, GuiIngameMenu.height / 4 + 24 + i, "Chat"));
        this.buttonList.add(new GuiButton(6, GuiIngameMenu.width / 2 - 100, GuiIngameMenu.height / 4 + 32, I18n.format("gui.stats", new Object[0])));
        this.buttonList.add(new GuiButton(10, GuiIngameMenu.width / 2 - 100, GuiIngameMenu.height / 4 + 72 + i, I18n.format("menu.multiplayer", new Object[0])));
        this.buttonList.add(new GuiButton(11, 5, 5, 75, 20, "TeamSpeak"));
        guibutton.enabled = (this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic());
        WDLHooks.injectWDLButtons(this, this.buttonList);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        WDLHooks.handleWDLButtonClick(this, button);
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            }
            case 1: {
                final boolean flag = this.mc.isIntegratedServerRunning();
                final boolean flag2 = this.mc.isConnectedToRealms();
                button.enabled = false;
                this.mc.theWorld.sendQuittingDisconnectingPacket();
                this.mc.loadWorld(null);
                if (flag) {
                    this.mc.displayGuiScreen(new GuiMainMenu());
                    break;
                }
                if (flag2) {
                    final RealmsBridge realmsbridge = new RealmsBridge();
                    realmsbridge.switchToRealms(new GuiMainMenu());
                    break;
                }
                this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
                break;
            }
            case 4: {
                this.mc.displayGuiScreen(new GuiFriendsLayout(this));
                break;
            }
            case 5: {
                this.mc.displayGuiScreen(new GuiIRC(this));
                break;
            }
            case 6: {
                this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
                break;
            }
            case 10: {
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            }
            case 11: {
                this.mc.displayGuiScreen(new GuiTeamSpeak(this));
                break;
            }
            case 7: {
                this.mc.displayGuiScreen(new GuiShareToLan(this));
                break;
            }
        }
    }
    
    @Override
    public void updateScreen() {
        if (this.lastUpdate + this.updateCooldown < System.currentTimeMillis()) {
            this.lastUpdate = System.currentTimeMillis();
            LabyModCore.getServerPinger().pingServer(null, this.lastUpdate, this.serverData.serverIP, new Consumer<ServerPingerData>() {
                @Override
                public void accept(final ServerPingerData accepted) {
                    if (accepted != null && accepted.getTimePinged() != GuiIngameMenu.this.lastUpdate) {
                        return;
                    }
                    GuiIngameMenu.access$2(GuiIngameMenu.this, new ServerInfoRenderer(GuiIngameMenu.this.serverData.serverIP, accepted));
                }
            });
        }
        super.updateScreen();
        ++this.field_146444_f;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.serverInfoRenderer == null || this.lastUpdate == -1L) {
            return;
        }
        this.serverInfoRenderer.drawEntry(0, GuiIngameMenu.width / 2 - 138, GuiIngameMenu.height / 4 + 24 - 65, 275, 35, 0, 0, false);
    }
    
    static /* synthetic */ void access$2(final GuiIngameMenu guiIngameMenu, final ServerInfoRenderer serverInfoRenderer) {
        guiIngameMenu.serverInfoRenderer = serverInfoRenderer;
    }
}
