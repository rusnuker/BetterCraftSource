// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import me.amkgre.bettercraft.client.utils.ProtocolVersionUtils;
import me.amkgre.bettercraft.client.utils.GeoUtils;
import net.minecraft.util.text.TextFormatting;
import me.amkgre.bettercraft.client.utils.RenderUtils;
import me.amkgre.bettercraft.client.utils.ColorUtils;
import java.awt.Color;
import java.io.IOException;
import viaforge.gui.GuiProtocolSelector;
import java.util.concurrent.CompletableFuture;
import java.net.UnknownHostException;
import me.amkgre.bettercraft.client.mods.crasher.NullpingCrasher;
import me.amkgre.bettercraft.client.mods.crasher.ic.Instantcrasher;
import java.net.InetAddress;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import me.amkgre.bettercraft.client.utils.TimeHelperUtils;
import net.minecraft.client.multiplayer.ServerData;

public class GuiScreenServerList extends GuiScreen
{
    private final GuiScreen lastScreen;
    private final ServerData serverData;
    private GuiTextField ipEdit;
    private final ServerListEntryNormal entry;
    private final TimeHelperUtils timeHelperUtils;
    private boolean wasPinged;
    String lastAddress;
    private volatile String addressPort;
    
    public GuiScreenServerList(final GuiScreen p_i1031_1_, final ServerData p_i1031_2_) {
        this.timeHelperUtils = new TimeHelperUtils();
        this.lastAddress = "Pinging...";
        this.lastScreen = p_i1031_1_;
        this.serverData = p_i1031_2_;
        this.entry = new ServerListEntryNormal((GuiMultiplayer)p_i1031_1_, p_i1031_2_);
    }
    
    @Override
    public void updateScreen() {
        this.ipEdit.updateCursorCounter();
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiScreenServerList.width / 2 - 100, GuiScreenServerList.height / 4 + 96 + 12, I18n.format("selectServer.select", new Object[0])));
        this.buttonList.add(new GuiButton(1, GuiScreenServerList.width / 2 - 100, GuiScreenServerList.height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
        (this.ipEdit = new GuiTextField(2, this.fontRendererObj, GuiScreenServerList.width / 2 - 100, 116, 200, 20)).setMaxStringLength(128);
        this.ipEdit.setFocused(true);
        this.ipEdit.setText(this.mc.gameSettings.lastServer);
        this.buttonList.get(0).enabled = (!this.ipEdit.getText().isEmpty() && this.ipEdit.getText().split(":").length > 0);
        this.buttonList.add(new GuiButton(8000, GuiScreenServerList.width / 2 - 55, 5, 50, 20, "§cCrash 1"));
        this.buttonList.add(new GuiButton(8001, GuiScreenServerList.width / 2 + 10, 5, 50, 20, "§cCrash 2"));
        this.buttonList.add(new GuiButton(1337, 8, 5, 100, 20, "§dViaVersion"));
        this.buttonList.add(new GuiButton(64, GuiScreenServerList.width - 105, 5, 100, 20, "§4Nullping"));
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.mc.gameSettings.lastServer = this.ipEdit.getText();
        this.mc.gameSettings.saveOptions();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 1) {
                this.lastScreen.confirmClicked(false, 0);
            }
            else if (button.id == 0) {
                this.serverData.serverIP = this.ipEdit.getText();
                this.lastScreen.confirmClicked(true, 0);
            }
            else if (button.id == 8000) {
                this.serverData.serverIP = this.ipEdit.getText();
                if (this.serverData == null) {
                    return;
                }
                final ServerAddress serveradress = ServerAddress.resolveAddress(this.serverData.serverIP);
                final String string = String.valueOf(InetAddress.getByName(serveradress.getIP()).getHostAddress()) + " " + serveradress.getPort();
                this.addressPort = string;
                final String address = string;
                Runtime.getRuntime().exec("BetterCraft/instantcrasher.exe " + address);
            }
            else if (button.id == 8001) {
                this.serverData.serverIP = this.ipEdit.getText();
                if (this.serverData == null) {
                    return;
                }
                final ServerAddress serveradress = ServerAddress.resolveAddress(this.serverData.serverIP);
                final String string2 = String.valueOf(InetAddress.getByName(serveradress.getIP()).getHostAddress()) + " " + serveradress.getPort();
                this.addressPort = string2;
                final String address = string2;
                Instantcrasher.crash(InetAddress.getByName(serveradress.getIP()).getHostAddress(), serveradress.getPort(), "https://discord.com/3bqXpRJ", 47);
            }
            else if (button.id == 64) {
                this.serverData.serverIP = this.ipEdit.getText();
                if (this.serverData == null) {
                    return;
                }
                final ServerAddress serveradress = ServerAddress.resolveAddress(this.serverData.serverIP);
                final String string3 = String.valueOf(InetAddress.getByName(serveradress.getIP()).getHostAddress()) + " " + serveradress.getPort();
                this.addressPort = string3;
                final String address = string3;
                CompletableFuture.runAsync(() -> {
                    try {
                        NullpingCrasher.pingThreadCrasher(InetAddress.getByName(serverAddress.getIP()).getHostAddress(), serverAddress.getPort(), 50, 60L);
                    }
                    catch (final UnknownHostException e) {
                        e.printStackTrace();
                    }
                });
            }
            else if (button.id == 1337) {
                this.mc.displayGuiScreen(new GuiProtocolSelector(this));
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (this.ipEdit.textboxKeyTyped(typedChar, keyCode)) {
            this.buttonList.get(0).enabled = (!this.ipEdit.getText().isEmpty() && this.ipEdit.getText().split(":").length > 0);
        }
        else if (keyCode == 28 || keyCode == 156) {
            this.actionPerformed(this.buttonList.get(0));
        }
        this.wasPinged = false;
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.ipEdit.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.serverData.serverIP = this.ipEdit.getText();
        this.serverData.serverName = this.ipEdit.getText();
        this.timeHelperUtils.updateTick();
        final long max = 100L;
        final int w = 275;
        final int x = GuiScreenServerList.width / 2 - 140;
        final int y = 86;
        final int h = 1;
        final Color barColor = new Color(255 - Integer.parseInt(String.valueOf(this.timeHelperUtils.getTick())), Integer.parseInt(String.valueOf(this.timeHelperUtils.getTick())) / 2, Integer.parseInt(String.valueOf(this.timeHelperUtils.getTick())) * 2, 255);
        RenderUtils.drawBorderedRect(x - 1, y - 40, x + w, y + h + 1, ColorUtils.rainbowEffect(0L, 1.0f).getRGB(), Integer.MIN_VALUE);
        RenderUtils.drawBorderedRect(x - 1, y - 40, x + w, y + h - 2, ColorUtils.rainbowEffect(0L, 1.0f).getRGB(), 0);
        RenderUtils.drawBorderedRect(x, y - 1, x + w - 1, y + h, -1, 0);
        Gui.drawRect(x, y - 1, x + (int)(this.timeHelperUtils.getTick() / (double)max * w), y + h, barColor.getRGB());
        this.fontRendererObj.drawString(TextFormatting.RED + String.valueOf(this.timeHelperUtils.getTick() / 20L + 1L), GuiScreenServerList.width / 2 + 125, y - 12, 0);
        if (this.timeHelperUtils.hasTimePassedTick(max)) {
            this.entry.setServerData(this.serverData);
            this.entry.ping();
            this.timeHelperUtils.resetTick();
        }
        try {
            final ServerData sd = this.serverData;
            final String version = sd.gameVersion;
            final String protocolVersion = new StringBuilder().append(sd.version).toString();
            final String ping = new StringBuilder().append(sd.pingToServer).toString();
            new Thread(() -> {
                try {
                    final ServerAddress serveradress = ServerAddress.resolveAddress(serverData.serverIP);
                    final String adress = InetAddress.getByName(serveradress.getIP()).getHostAddress();
                    if (!this.lastAddress.equals(adress)) {
                        this.lastAddress = adress;
                        new GeoUtils(adress);
                        this.addressPort = String.valueOf(InetAddress.getByName(serveradress.getIP()).getHostAddress()) + " " + serveradress.getPort();
                    }
                }
                catch (final Exception ex) {}
                return;
            }, "PingThread-").start();
            int heigh = GuiScreenServerList.height / 2 + 50;
            final int width = GuiScreenServerList.width / 2 - 400;
            final int adder = 12;
            int heigh2 = 250;
            RenderUtils.drawBorderedRect(width - 5, heigh - 50, width * 3.25, 450.0, ColorUtils.rainbowEffect(0L, 1.0f).getRGB(), Integer.MIN_VALUE);
            if (version.equalsIgnoreCase("1.12.2") || sd.pingToServer < 0L) {
                heigh2 += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5Brand: §7Pinging...", (float)width, (float)heigh2, -1);
            }
            else {
                heigh2 += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5Brand: §7" + version.replaceAll("1.8.x, 1.9.x, 1.10.x, 1.11.x, 1.12.x, 1.13.x, 1.14.x, 1.15.x, 1.16.x", "1.8.x-1.16.x").replaceAll("1.7.x, ", "").replaceAll("PE-1.8.x, PE-1.9.x, PE-1.10.x, PE-1.11.x, PE-1.12.x, PE-1.13.x, PE-1.14.x, PE-1.15.x, PE-1.16.x", "PE-1.8.x - PE-1.16.x"), (float)width, (float)heigh2, -1);
            }
            if (sd.pingToServer < 0L) {
                heigh2 += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5Protocol: §7Pinging...", (float)width, (float)heigh2, -1);
            }
            else {
                heigh2 += adder;
                final FontRenderer fontRendererObj = this.mc.fontRendererObj;
                final StringBuilder append = new StringBuilder("§5Protocol: §7").append(protocolVersion).append(" -> ");
                ProtocolVersionUtils.getInstance();
                fontRendererObj.drawStringWithShadow(append.append(ProtocolVersionUtils.getKnownAs(sd.version)).toString(), (float)width, (float)heigh2, -1);
            }
            if (sd.pingToServer < 0L) {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5IP: §7Pinging...", (float)width, (float)heigh, -1);
            }
            else {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5IP: §7" + this.addressPort.replace("null", "").replaceAll("127.0.0.1 25565", "Pinging..."), (float)width, (float)heigh, -1);
            }
            if (sd.pingToServer < 0L) {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5ORG: §7Pinging...", (float)width, (float)heigh, -1);
            }
            else {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5ORG: §7" + GeoUtils.getInstance().getORG().replaceAll("- Connecting your World!", "").replaceAll("Cloud ", "").replaceAll("- DDoS-Protected Gameservers and more", "").replaceAll("www.", "").replaceAll("Corp.", "").replaceAll("(haftungsbeschraenkt) & Co. KG", "").replaceAll("trading as Gericke KG", ""), (float)width, (float)heigh, -1);
            }
            if (sd.pingToServer < 0L) {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5AS: §7Pinging...", (float)width, (float)heigh, -1);
            }
            else {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5AS: §7" + GeoUtils.getInstance().getAS().replaceAll("Center ", "").replaceAll("oration", "").replaceAll("Waldecker trading as LUMASERV Systems", "").replaceAll("GmbH", "").replaceAll(". Inc.", "").replaceAll(", LLC", "").replaceAll("Corp.", "").replaceAll("TeleHost", "Tele").replaceAll("e-commerce", "").replaceAll("IT Services & Consulting", "").replaceAll("UG (haftungsbeschrankt)", "").replaceAll("is trading as SYNLINQ", ""), (float)width, (float)heigh, -1);
            }
            if (sd.pingToServer < 0L) {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5Country: §7Pinging,,,", (float)width, (float)heigh, -1);
            }
            else {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5Country: §7" + GeoUtils.getInstance().getCOUNTRY(), (float)width, (float)heigh, -1);
            }
            if (sd.pingToServer < 0L) {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5City: §7Pinging...", (float)width, (float)heigh, -1);
            }
            else {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5City: §7" + GeoUtils.getInstance().getCITY(), (float)width, (float)heigh, -1);
            }
            if (sd.pingToServer < 0L) {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5Region: §7Pinging...", (float)width, (float)heigh, -1);
            }
            else {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5Region: §7" + GeoUtils.getInstance().getREGIONNAME(), (float)width, (float)heigh, -1);
            }
            if (sd.pingToServer < 0L) {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5ISP: §7Pinging...", (float)width, (float)heigh, -1);
            }
            else {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5ISP: §7" + GeoUtils.getInstance().getISP().replaceAll("is trading as \"SYNLINQ\"", ""), (float)width, (float)heigh, -1);
            }
            if (sd.pingToServer < 0L) {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5Timezone: §7Pinging...", (float)width, (float)heigh, -1);
            }
            else {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5Timezone: §7" + GeoUtils.getInstance().getTIMEZONE(), (float)width, (float)heigh, -1);
            }
            if (sd.pingToServer < 0L) {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5CountryCode: §7Pinging...", (float)width, (float)heigh, -1);
            }
            else {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5CountryCode: §7" + GeoUtils.getInstance().getCOUNTRYCODE(), (float)width, (float)heigh, -1);
            }
            if (sd.pingToServer < 0L) {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5Proxy: §7Pinging...", (float)width, (float)heigh, -1);
            }
            else {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5Proxy: §7" + GeoUtils.getInstance().getPROXY(), (float)width, (float)heigh, -1);
            }
            if (sd.pingToServer < 0L) {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5Reverse: §7Pinging...", (float)width, (float)heigh, -1);
            }
            else {
                heigh += adder;
                this.mc.fontRendererObj.drawStringWithShadow("§5Reverse: §7" + GeoUtils.getInstance().getREVERSE(), (float)width, (float)heigh, -1);
            }
        }
        catch (final Throwable t) {}
        this.entry.func_192634_a(0, (int)(GuiScreenServerList.width / 2 - 137.5), 50, 275, 35, 0, 0, false, 0.0f);
        Gui.drawString(this.fontRendererObj, I18n.format("addServer.enterIp", new Object[0]), GuiScreenServerList.width / 2 - 100, 100, 10526880);
        this.ipEdit.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
