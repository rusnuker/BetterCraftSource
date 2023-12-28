// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.achievement.GuiStats;
import me.amkgre.bettercraft.client.mods.altmanager.GuiAltManager;
import me.amkgre.bettercraft.client.gui.GuiTools;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;

public class GuiIngameMenuSinglePlayer extends GuiScreen
{
    private int saveStep;
    private int visibleTime;
    
    @Override
    public void initGui() {
        this.saveStep = 0;
        this.buttonList.clear();
        final int i = -16;
        final int j = 98;
        this.buttonList.add(new GuiButton(1, GuiIngameMenuSinglePlayer.width / 2 - 100, GuiIngameMenuSinglePlayer.height / 4 + 120 - 16, I18n.format("menu.returnToMenu", new Object[0])));
        if (!this.mc.isIntegratedServerRunning()) {
            this.buttonList.get(0).displayString = I18n.format("menu.disconnect", new Object[0]);
        }
        this.buttonList.add(new GuiButton(4, GuiIngameMenuSinglePlayer.width / 2 - 100, GuiIngameMenuSinglePlayer.height / 4 + 24 + i, I18n.format("Serverliste", new Object[0])));
        this.buttonList.add(new GuiButton(69, GuiIngameMenuSinglePlayer.width / 2 + 2, GuiIngameMenuSinglePlayer.height / 4 + 48 - 16, 98, 20, I18n.format("Tools", new Object[0])));
        this.buttonList.add(new GuiButton(0, GuiIngameMenuSinglePlayer.width / 2 - 100, GuiIngameMenuSinglePlayer.height / 4 + 96 - 16, 98, 20, I18n.format("menu.options", new Object[0])));
        final GuiButton guibutton = this.addButton(new GuiButton(7, GuiIngameMenuSinglePlayer.width / 2 - 100, GuiIngameMenuSinglePlayer.height / 4 + 72 + i, I18n.format("menu.shareToLan", new Object[0])));
        guibutton.enabled = (this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic());
        this.buttonList.add(new GuiButton(5, GuiIngameMenuSinglePlayer.width / 2 - 100, GuiIngameMenuSinglePlayer.height / 4 + 48 - 16, 98, 20, I18n.format("Altmanager", new Object[0])));
        this.buttonList.add(new GuiButton(6, GuiIngameMenuSinglePlayer.width / 2 + 2, GuiIngameMenuSinglePlayer.height / 4 + 96 - 16, 98, 20, I18n.format("gui.stats", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            }
            case 1: {
                final boolean flag = this.mc.isIntegratedServerRunning();
                final boolean flag2 = this.mc.isConnectedToRealms();
                button.enabled = false;
                this.mc.world.sendQuittingDisconnectingPacket();
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
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            }
            case 69: {
                this.mc.displayGuiScreen(new GuiTools(this));
                break;
            }
            case 5: {
                this.mc.displayGuiScreen(new GuiAltManager(this));
                break;
            }
            case 6: {
                this.mc.displayGuiScreen(new GuiStats(this, this.mc.player.getStatFileWriter()));
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
        super.updateScreen();
        ++this.visibleTime;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
