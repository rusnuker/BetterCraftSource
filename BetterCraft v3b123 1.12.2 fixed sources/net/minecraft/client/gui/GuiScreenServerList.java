// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import java.io.IOException;
import java.util.Objects;
import me.nzxtercode.bettercraft.client.utils.ProxyUtils;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import java.util.concurrent.ExecutorService;
import net.labymod.core.ServerPingerData;
import net.labymod.utils.Consumer;
import net.labymod.core.LabyModCore;
import net.labymod.utils.manager.ServerInfoRenderer;
import net.minecraft.client.multiplayer.ServerData;

public class GuiScreenServerList extends GuiScreen
{
    private final GuiScreen field_146303_a;
    private final ServerData field_146301_f;
    private GuiTextField field_146302_g;
    private long lastUpdate;
    private long updateCooldown;
    private ServerInfoRenderer serverInfoRenderer;
    public static GuiTextField proxy;
    public static boolean isEnabled;
    
    static {
        GuiScreenServerList.isEnabled = false;
    }
    
    public GuiScreenServerList(final GuiScreen p_i1031_1_, final ServerData p_i1031_2_) {
        this.lastUpdate = 0L;
        this.updateCooldown = 5000L;
        this.field_146303_a = p_i1031_1_;
        this.field_146301_f = p_i1031_2_;
    }
    
    @Override
    public void updateScreen() {
        GuiScreenServerList.proxy.updateCursorCounter();
        this.field_146302_g.updateCursorCounter();
        if (!this.field_146302_g.getText().replace(" ", "").isEmpty()) {
            if (this.lastUpdate + this.updateCooldown < System.currentTimeMillis()) {
                this.lastUpdate = System.currentTimeMillis();
                LabyModCore.getServerPinger().pingServer(null, this.lastUpdate, this.field_146302_g.getText(), new Consumer<ServerPingerData>() {
                    @Override
                    public void accept(final ServerPingerData accepted) {
                        if (accepted != null && accepted.getTimePinged() != GuiScreenServerList.this.lastUpdate) {
                            return;
                        }
                        GuiScreenServerList.access$2(GuiScreenServerList.this, new ServerInfoRenderer(GuiScreenServerList.this.field_146302_g.getText(), accepted));
                    }
                });
            }
        }
        else {
            this.serverInfoRenderer = new ServerInfoRenderer(this.field_146302_g.getText(), null);
            this.lastUpdate = -1L;
        }
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiScreenServerList.width / 2 - 100, GuiScreenServerList.height / 4 + 96 + 12, I18n.format("selectServer.select", new Object[0])));
        this.buttonList.add(new GuiButton(1, GuiScreenServerList.width / 2 - 100, GuiScreenServerList.height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
        this.buttonList.add(new GuiButton(2, GuiScreenServerList.width / 2 - 100, GuiScreenServerList.height / 4 + 72 + 12, String.format("%s", GuiScreenServerList.isEnabled ? (String.valueOf(EnumChatFormatting.RED.toString()) + "Disconnect") : (String.valueOf(EnumChatFormatting.GREEN.toString()) + "Connect"))));
        (this.field_146302_g = new GuiTextField(2, this.fontRendererObj, GuiScreenServerList.width / 2 - 100, 116, 200, 20)).setMaxStringLength(128);
        this.field_146302_g.setFocused(true);
        this.field_146302_g.setText(this.mc.gameSettings.lastServer);
        (GuiScreenServerList.proxy = new GuiTextField(3, this.fontRendererObj, GuiScreenServerList.width / 2 - 100, 66, 200, 20)).setMaxStringLength(128);
        GuiScreenServerList.proxy.setText(Objects.nonNull(ProxyUtils.getProxy()) ? ProxyUtils.getProxy().address().toString().replace("/", "") : "127.0.0.1:9050");
        this.buttonList.get(0).enabled = (this.field_146302_g.getText().length() > 0 && this.field_146302_g.getText().split(":").length > 0);
        this.buttonList.get(2).enabled = (GuiScreenServerList.proxy.getText().length() > 0 && GuiScreenServerList.proxy.getText().split(":").length > 0);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.mc.gameSettings.lastServer = this.field_146302_g.getText();
        this.mc.gameSettings.saveOptions();
        LabyModCore.getServerPinger().closePendingConnections();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 1) {
                this.field_146303_a.confirmClicked(false, 0);
            }
            else if (button.id == 0) {
                this.field_146301_f.serverIP = this.field_146302_g.getText();
                this.field_146303_a.confirmClicked(true, 0);
            }
            else if (button.id == 2) {
                ProxyUtils.setProxy(GuiScreenServerList.isEnabled ? null : ProxyUtils.getProxyFromString(GuiScreenServerList.proxy.getText()));
                GuiScreenServerList.isEnabled = !GuiScreenServerList.isEnabled;
                button.displayString = String.format("%s", GuiScreenServerList.isEnabled ? (String.valueOf(EnumChatFormatting.RED.toString()) + "Disconnect") : (String.valueOf(EnumChatFormatting.GREEN.toString()) + "Connect"));
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (this.field_146302_g.textboxKeyTyped(typedChar, keyCode)) {
            this.buttonList.get(0).enabled = (this.field_146302_g.getText().length() > 0 && this.field_146302_g.getText().split(":").length > 0);
        }
        else if (keyCode == 28 || keyCode == 156) {
            this.actionPerformed(this.buttonList.get(0));
        }
        else if (GuiScreenServerList.proxy.textboxKeyTyped(typedChar, keyCode)) {
            this.buttonList.get(2).enabled = (GuiScreenServerList.proxy.getText().length() > 0 && GuiScreenServerList.proxy.getText().split(":").length > 0);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.field_146302_g.mouseClicked(mouseX, mouseY, mouseButton);
        GuiScreenServerList.proxy.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawString(this.fontRendererObj, I18n.format("addServer.enterIp", new Object[0]), GuiScreenServerList.width / 2 - 100, 100, 10526880);
        this.field_146302_g.drawTextBox();
        this.drawString(this.fontRendererObj, "Proxyaddress", GuiScreenServerList.width / 2 - 100, 50, 10526880);
        GuiScreenServerList.proxy.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.serverInfoRenderer == null || this.lastUpdate == -1L) {
            return;
        }
        this.serverInfoRenderer.drawEntry(0, GuiScreenServerList.width / 2 - 138, 10, 275, 35, 0, 0, false);
    }
    
    static /* synthetic */ void access$2(final GuiScreenServerList list, final ServerInfoRenderer serverInfoRenderer) {
        list.serverInfoRenderer = serverInfoRenderer;
    }
}
