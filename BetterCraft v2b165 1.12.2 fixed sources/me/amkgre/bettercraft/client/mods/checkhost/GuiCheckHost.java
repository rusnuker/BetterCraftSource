// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.checkhost;

import net.minecraft.client.gui.FontRenderer;
import java.util.Iterator;
import me.amkgre.bettercraft.client.utils.ProtocolVersionUtils;
import me.amkgre.bettercraft.client.utils.GeoUtils;
import java.net.InetAddress;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.util.ResourceLocation;
import java.text.DecimalFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import me.amkgre.bettercraft.client.utils.ColorUtils;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiButton;
import me.amkgre.bettercraft.client.mods.checkhost.results.CheckHostTcpResult;
import me.amkgre.bettercraft.client.mods.checkhost.results.CheckHostHttpResult;
import java.util.Map;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.gui.GuiScreen;

public class GuiCheckHost extends GuiScreen
{
    private ServerData serverData;
    String lastAddress;
    private volatile String addressPort;
    public GuiTextField inputIp;
    public String mode;
    public CheckResult<Map<CheckHostServer, CheckHostHttpResult>> httpResult;
    public CheckResult<Map<CheckHostServer, CheckHostTcpResult>> tcpResult;
    public int time;
    public GuiScreen before;
    
    public GuiCheckHost(final GuiScreen screen) {
        this.lastAddress = "Pinging...";
        this.mode = "";
        this.time = 0;
        this.before = screen;
        this.serverData = new ServerData("", "", false);
    }
    
    @Override
    public void updateScreen() {
        this.inputIp.updateCursorCounter();
        this.buttonList.get(1).displayString = ((this.mode == "tcp") ? "§aTCP" : "§cTCP");
        this.buttonList.get(2).displayString = ((this.mode == "http") ? "§aHTTP" : "§cHTTP");
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1, GuiCheckHost.width - 70, GuiCheckHost.height - 30, 60, 20, "Back"));
        this.buttonList.add(new GuiButton(2, GuiCheckHost.width - 144 + 74, 54, 60, 20, (this.mode == "tcp") ? "§aTCP" : "§cTCP"));
        this.buttonList.add(new GuiButton(3, GuiCheckHost.width - 144 + 10, 54, 60, 20, (this.mode == "http") ? "§aHTTP" : "§cHTTP"));
        (this.inputIp = new GuiTextField(0, this.fontRendererObj, GuiCheckHost.width - 134, 30, 124, 20)).setMaxStringLength(65535);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void actionPerformed(final GuiButton clickedButton) {
        switch (clickedButton.id) {
            case 1: {
                this.mc.displayGuiScreen(this.before);
                break;
            }
            case 2: {
                this.mode = "tcp";
                break;
            }
            case 3: {
                this.mode = "http";
                break;
            }
        }
    }
    
    @Override
    protected void keyTyped(final char par1, final int par2) {
        if (par2 == 28 || par2 == 156) {
            this.actionPerformed(this.buttonList.get(0));
        }
        this.inputIp.textboxKeyTyped(par1, par2);
        this.serverData = new ServerData(this.inputIp.getText(), this.inputIp.getText(), false);
    }
    
    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) throws IOException {
        super.mouseClicked(par1, par2, par3);
        this.inputIp.mouseClicked(par1, par2, par3);
    }
    
    @Override
    public void drawScreen(final int par1, final int par2, final float par3) {
        this.drawDefaultBackground();
        Gui.drawRect(8, 29, 400, 30, ColorUtils.rainbowEffect(0L, 1.0f).getRGB());
        Gui.drawRect(8, 291, 400, 290, ColorUtils.rainbowEffect(0L, 1.0f).getRGB());
        for (int i = 0; i < 26; ++i) {
            Gui.drawRect(10, 30 + i * 10, 400, 30 + (i + 1) * 10, Integer.MIN_VALUE);
            Gui.drawRect(8, 30 + i * 10, 9, 30 + (i + 1) * 10, ColorUtils.rainbowEffect(0L, 1.0f).getRGB());
            Gui.drawRect(399, 30 + i * 10, 400, 30 + (i + 1) * 10, ColorUtils.rainbowEffect(0L, 1.0f).getRGB());
            Gui.drawRect(135, 30 + i * 10, 136, 30 + (i + 1) * 10, ColorUtils.rainbowEffect(0L, 1.0f).getRGB());
            Gui.drawRect(240, 30 + i * 10, 241, 30 + (i + 1) * 10, ColorUtils.rainbowEffect(0L, 1.0f).getRGB());
        }
        this.inputIp.drawTextBox();
        if (this.time <= 550) {
            this.time += 5;
        }
        else {
            this.time = 0;
        }
        if (this.time == 550) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String mode;
                    final String s;
                    switch (s = (mode = GuiCheckHost.this.mode)) {
                        case "tcp": {
                            if (GuiCheckHost.this.inputIp.getText().isEmpty()) {
                                return;
                            }
                            try {
                                GuiCheckHost.this.tcpResult = CheckHostAPI.createTcpRequest(GuiCheckHost.this.inputIp.getText(), 100);
                            }
                            catch (final IOException ex) {}
                            break;
                        }
                        case "http": {
                            if (GuiCheckHost.this.inputIp.getText().isEmpty()) {
                                return;
                            }
                            try {
                                GuiCheckHost.this.httpResult = CheckHostAPI.createHttpRequest(GuiCheckHost.this.inputIp.getText(), 100);
                            }
                            catch (final IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        default:
                            break;
                    }
                }
            }).start();
        }
        int y = 32;
        if (this.mode.isEmpty()) {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§5Waiting...", 10.0f, 18.0f, -1);
        }
        else if (this.mode.equals("http") && this.httpResult != null) {
            for (final CheckHostServer r : this.httpResult.getServers()) {
                final CheckHostHttpResult endResult = (CheckHostHttpResult)this.httpResult.getResult().get(r);
                if (endResult != null) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§5City§8/§5Country", 10.0f, 18.0f, -1);
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§dTime", 150.0f, 18.0f, -1);
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§dCode", 265.0f, 18.0f, -1);
                    final DecimalFormat fm = new DecimalFormat("00.##");
                    final String pingFormat = fm.format(endResult.getPing());
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§d" + r.getCity() + "," + r.getCountryCode(), 10.0f, (float)y, -1);
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§d" + pingFormat + " seconds", 150.0f, (float)y, -1);
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§d" + endResult.getStatus(), 265.0f, (float)y, -1);
                    final String file = "textures/gui/flags/" + r.getCountryCode() + ".png";
                    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(file));
                    Gui.drawModalRectWithCustomSizedTexture(GuiCheckHost.width / 2 - 90, y, 0.0f, 0.0f, 8, 8, 8.0f, 8.0f);
                    y += 10;
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
                        int heigh = 150;
                        final int width = GuiCheckHost.width / 2 + 275;
                        final int adder = 12;
                        int heigh2 = 100;
                        if (version.equalsIgnoreCase("1.12.2") && sd.pingToServer < 0L) {
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
                            this.mc.fontRendererObj.drawStringWithShadow("§5IP: §7" + this.addressPort.replace("null", "").replace("127.0.0.1 25565", "Pinging..."), (float)width, (float)heigh, -1);
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
                            this.mc.fontRendererObj.drawStringWithShadow("§5AS: §7" + GeoUtils.getInstance().getAS().replaceAll("Center ", "").replaceAll("oration", "").replaceAll("Waldecker trading as LUMASERV Systems", "").replaceAll("GmbH", "").replaceAll(". Inc.", "").replaceAll(", LLC", "").replaceAll("Corp.", "").replaceAll("TeleHost", "Tele").replaceAll("e-commerce", "").replaceAll("IT Services & Consulting", "").replaceAll("UG (haftungsbeschrankt)", ""), (float)width, (float)heigh, -1);
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
                            this.mc.fontRendererObj.drawStringWithShadow("§5ISP: §7" + GeoUtils.getInstance().getISP(), (float)width, (float)heigh, -1);
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
                }
            }
        }
        else if (this.mode.equals("tcp") && this.tcpResult != null) {
            for (final CheckHostServer r : this.tcpResult.getServers()) {
                final CheckHostTcpResult endResult2 = (CheckHostTcpResult)this.tcpResult.getResult().get(r);
                if (endResult2 != null) {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§5City§8/§5Country", 10.0f, 18.0f, -1);
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§5Time", 150.0f, 18.0f, -1);
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§5Server", 265.0f, 18.0f, -1);
                    final DecimalFormat fm = new DecimalFormat("00.##");
                    final String pingFormat = fm.format(endResult2.getPing());
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§d" + r.getCity() + "," + r.getCountryCode(), 10.0f, (float)y, -1);
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§d" + pingFormat + " seconds", 150.0f, (float)y, -1);
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§d" + r.getName(), 265.0f, (float)y, -1);
                    final String file = "textures/gui/flags/" + r.getCountryCode() + ".png";
                    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(file));
                    Gui.drawModalRectWithCustomSizedTexture(GuiCheckHost.width / 2 - 90, y, 0.0f, 0.0f, 8, 8, 8.0f, 8.0f);
                    y += 10;
                    try {
                        final ServerData sd = this.serverData;
                        final String version = sd.gameVersion;
                        final String protocolVersion = new StringBuilder().append(sd.version).toString();
                        final String ping = new StringBuilder().append(sd.pingToServer).toString();
                        new Thread(() -> {
                            try {
                                final ServerAddress serveradress2 = ServerAddress.resolveAddress(serverData2.serverIP);
                                final String adress2 = InetAddress.getByName(serveradress2.getIP()).getHostAddress();
                                if (!this.lastAddress.equals(adress2)) {
                                    this.lastAddress = adress2;
                                    new GeoUtils(adress2);
                                    this.addressPort = String.valueOf(InetAddress.getByName(serveradress2.getIP()).getHostAddress()) + " " + serveradress2.getPort();
                                }
                            }
                            catch (final Exception ex2) {}
                            return;
                        }, "PingThread-").start();
                        int heigh = 150;
                        final int width = GuiCheckHost.width / 2 + 275;
                        final int adder = 12;
                        int heigh2 = 100;
                        if (version.equalsIgnoreCase("1.12.2") && sd.pingToServer < 0L) {
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
                            final FontRenderer fontRendererObj2 = this.mc.fontRendererObj;
                            final StringBuilder append2 = new StringBuilder("§5Protocol: §7").append(protocolVersion).append(" -> ");
                            ProtocolVersionUtils.getInstance();
                            fontRendererObj2.drawStringWithShadow(append2.append(ProtocolVersionUtils.getKnownAs(sd.version)).toString(), (float)width, (float)heigh2, -1);
                        }
                        if (sd.pingToServer < 0L) {
                            heigh += adder;
                            this.mc.fontRendererObj.drawStringWithShadow("§5IP: §7Pinging...", (float)width, (float)heigh, -1);
                        }
                        else {
                            heigh += adder;
                            this.mc.fontRendererObj.drawStringWithShadow("§5IP: §7" + this.addressPort.replace("null", "").replace("127.0.0.1 25565", "Pinging..."), (float)width, (float)heigh, -1);
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
                            this.mc.fontRendererObj.drawStringWithShadow("§5AS: §7" + GeoUtils.getInstance().getAS().replaceAll("Center ", "").replaceAll("oration", "").replaceAll("Waldecker trading as LUMASERV Systems", "").replaceAll("GmbH", "").replaceAll(". Inc.", "").replaceAll(", LLC", "").replaceAll("Corp.", "").replaceAll("TeleHost", "Tele").replaceAll("e-commerce", "").replaceAll("IT Services & Consulting", "").replaceAll("UG (haftungsbeschrankt)", ""), (float)width, (float)heigh, -1);
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
                            this.mc.fontRendererObj.drawStringWithShadow("§5ISP: §7" + GeoUtils.getInstance().getISP(), (float)width, (float)heigh, -1);
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
                    catch (final Throwable t2) {}
                }
            }
        }
        this.mc.fontRendererObj.drawString("§7Server IP", GuiCheckHost.width - 100, 20, -1);
        super.drawScreen(par1, par2, par3);
    }
}
