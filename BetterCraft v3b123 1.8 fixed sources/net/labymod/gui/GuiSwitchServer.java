// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.client.Minecraft;
import java.util.concurrent.ExecutorService;
import net.labymod.utils.Consumer;
import net.labymod.core.LabyModCore;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiButton;
import net.labymod.main.lang.LanguageManager;
import net.labymod.core.ServerPingerData;
import net.labymod.utils.ModUtils;
import net.labymod.main.LabyMod;
import net.labymod.utils.ModColor;
import net.labymod.gui.elements.CheckBox;
import net.labymod.utils.manager.ServerInfoRenderer;
import net.minecraft.client.gui.GuiScreen;

public class GuiSwitchServer extends GuiScreen
{
    private String title;
    private String address;
    private Result result;
    private boolean connectingScreen;
    private ServerInfoRenderer serverInfoRenderer;
    private long lastUpdate;
    private long updateCooldown;
    private String currentServer;
    private CheckBox checkBox;
    
    public GuiSwitchServer(final String address) {
        this.lastUpdate = 0L;
        this.updateCooldown = 2000L;
        this.address = address;
        this.connectingScreen = true;
    }
    
    public GuiSwitchServer(final String title, final String address, final boolean preview, final Result result) {
        this.lastUpdate = 0L;
        this.updateCooldown = 2000L;
        this.title = ModColor.createColors(title);
        this.address = address;
        this.result = result;
        this.connectingScreen = false;
        this.currentServer = ((LabyMod.getInstance().getCurrentServerData() == null) ? null : ModUtils.getProfileNameByIp(LabyMod.getInstance().getCurrentServerData().getIp()));
        if (preview) {
            this.serverInfoRenderer = new ServerInfoRenderer(address, null);
        }
    }
    
    @Override
    public void initGui() {
        super.initGui();
        if (!this.connectingScreen) {
            this.buttonList.add(new GuiButton(0, GuiSwitchServer.width / 2 + 20, GuiSwitchServer.height / 2 + 20, 100, 20, LanguageManager.translate("server_switch_no")));
            this.buttonList.add(new GuiButton(1, GuiSwitchServer.width / 2 - 120, GuiSwitchServer.height / 2 + 20, 130, 20, LanguageManager.translate("server_switch_yes")));
            this.checkBox = new CheckBox("", CheckBox.EnumCheckBoxValue.DISABLED, null, GuiSwitchServer.width / 2 - 120, GuiSwitchServer.height / 2 + 50, 20, 20);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        if (this.connectingScreen) {
            draw.drawCenteredString(I18n.format("connect.connecting", new Object[0]), GuiSwitchServer.width / 2, GuiSwitchServer.height / 2 - 5);
            draw.drawCenteredString(this.address, GuiSwitchServer.width / 2, GuiSwitchServer.height / 2 + 5);
        }
        else {
            draw.drawCenteredString(this.title, GuiSwitchServer.width / 2, GuiSwitchServer.height / 2 - 55 + ((this.serverInfoRenderer == null) ? 0 : -15));
            final List<String> list = draw.listFormattedStringToWidth(LanguageManager.translate("server_switch_confirm", this.address), 250);
            int i = 0;
            for (final String line : list) {
                draw.drawCenteredString(line, GuiSwitchServer.width / 2, GuiSwitchServer.height / 2 - 40 + i * 10 + ((this.serverInfoRenderer == null) ? 10 : -10));
                ++i;
            }
            if (this.currentServer != null) {
                this.checkBox.drawCheckbox(mouseX, mouseY);
                draw.drawString(String.valueOf(ModColor.cl('a')) + LanguageManager.translate("server_switch_trust", this.currentServer), GuiSwitchServer.width / 2 - 95, GuiSwitchServer.height / 2 + 56);
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (!this.connectingScreen) {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        if (!this.connectingScreen && this.serverInfoRenderer != null && this.lastUpdate + this.updateCooldown < System.currentTimeMillis()) {
            this.lastUpdate = System.currentTimeMillis();
            LabyModCore.getServerPinger().pingServer(null, this.lastUpdate, this.address, new Consumer<ServerPingerData>() {
                @Override
                public void accept(final ServerPingerData accepted) {
                    if (accepted != null && accepted.getTimePinged() != GuiSwitchServer.this.lastUpdate) {
                        return;
                    }
                    if (GuiSwitchServer.this.serverInfoRenderer != null) {
                        GuiSwitchServer.this.serverInfoRenderer.init(GuiSwitchServer.this.address, GuiSwitchServer.this.address, accepted);
                    }
                }
            });
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 0) {
            this.result.notify(this.address, false, false);
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
        if (button.id == 1) {
            final boolean trusted = this.currentServer != null && this.checkBox.getValue() == CheckBox.EnumCheckBoxValue.ENABLED;
            if (trusted) {
                final List<String> list = new ArrayList<String>(Arrays.asList(LabyMod.getSettings().trustedServers));
                final String profileAddress = ModUtils.getProfileNameByIp(this.currentServer);
                if (trusted && !list.contains(profileAddress)) {
                    list.add(profileAddress);
                    final String[] array = new String[list.size()];
                    list.toArray(array);
                    LabyMod.getSettings().trustedServers = array;
                    LabyMod.getMainConfig().save();
                }
            }
            this.result.notify(this.address, true, trusted);
            LabyMod.getInstance().switchServer(this.address, true);
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!this.connectingScreen) {
            this.checkBox.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
    
    public interface Result
    {
        void notify(final String p0, final boolean p1, final boolean p2);
    }
}
