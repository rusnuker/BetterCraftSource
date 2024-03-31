// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import me.amkgre.bettercraft.client.utils.ProtocolVersionUtils;
import me.amkgre.bettercraft.client.utils.RenderUtils;
import me.amkgre.bettercraft.client.utils.ColorUtils;
import me.amkgre.bettercraft.client.utils.GeoUtils;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.net.UnknownHostException;
import me.amkgre.bettercraft.client.mods.crasher.NullpingCrasher;
import me.amkgre.bettercraft.client.mods.crasher.ic.Instantcrasher;
import java.net.InetAddress;
import net.minecraft.client.multiplayer.ServerAddress;
import me.amkgre.bettercraft.client.mods.clientchat.GuiClientChat;
import net.minecraft.client.gui.achievement.GuiStats;
import me.amkgre.bettercraft.client.mods.altmanager.GuiAltManager;
import me.amkgre.bettercraft.client.gui.GuiTools;
import me.amkgre.bettercraft.client.gui.GuiMods;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.client.multiplayer.WorldClient;
import java.util.List;
import wdl.WDLHooks;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

public class GuiIngameMenu extends GuiScreen
{
    private int saveStep;
    private int visibleTime;
    private ServerData serverData;
    private ServerListEntryNormal entry;
    String lastAddress;
    private volatile String addressPort;
    
    public GuiIngameMenu() {
        this.lastAddress = "Pinging...";
        final String serverIp = Minecraft.getMinecraft().getConnection().getNetworkManager().getRemoteAddress().toString().split("/")[1];
        final String serverName = Minecraft.getMinecraft().getConnection().getNetworkManager().channel.toString().split(":")[4].replace("./" + serverIp.split(":")[0], "");
        this.serverData = new ServerData(serverName, serverIp, false);
        this.entry = new ServerListEntryNormal(new GuiMultiplayer(null), this.serverData);
    }
    
    @Override
    public void initGui() {
        this.saveStep = 0;
        this.buttonList.clear();
        final int i = -16;
        final int j = 98;
        this.buttonList.add(new GuiButton(1, GuiIngameMenu.width / 2 - 100, GuiIngameMenu.height / 4 + 120 - 16, I18n.format("menu.returnToMenu", new Object[0])));
        if (!this.mc.isIntegratedServerRunning()) {
            this.buttonList.get(0).displayString = I18n.format("menu.disconnect", new Object[0]);
        }
        this.buttonList.add(new GuiButton(8000, 10, 5, 100, 20, "§cCrash 1"));
        this.buttonList.add(new GuiButton(8001, GuiIngameMenu.width - 108, 5, 100, 20, "§cCrash 2"));
        this.buttonList.add(new GuiButton(4, GuiIngameMenu.width / 2 - 100, GuiIngameMenu.height / 4 + 24 + i, I18n.format("Serverliste", new Object[0])));
        this.buttonList.add(new GuiButton(69, GuiIngameMenu.width / 2 + 2, GuiIngameMenu.height / 4 + 48 - 16, 98, 20, I18n.format("Chat", new Object[0])));
        this.buttonList.add(new GuiButton(0, GuiIngameMenu.width / 2 - 100, GuiIngameMenu.height / 4 + 96 - 16, 98, 20, I18n.format("menu.options", new Object[0])));
        this.buttonList.add(new GuiButton(7, GuiIngameMenu.width / 2 - 100, GuiIngameMenu.height / 4 + 72 + i, 98, 20, I18n.format("Mods", new Object[0])));
        this.buttonList.add(new GuiButton(8, GuiIngameMenu.width / 2 + 2, GuiIngameMenu.height / 4 + 72 + i, 98, 20, I18n.format("Tools", new Object[0])));
        this.buttonList.add(new GuiButton(5, GuiIngameMenu.width / 2 - 100, GuiIngameMenu.height / 4 + 48 - 16, 98, 20, I18n.format("Accmanager", new Object[0])));
        this.buttonList.add(new GuiButton(6, GuiIngameMenu.width / 2 + 2, GuiIngameMenu.height / 4 + 96 - 16, 98, 20, I18n.format("gui.stats", new Object[0])));
        WDLHooks.injectWDLButtons(this, this.buttonList);
        this.buttonList.add(new GuiButton(64, GuiIngameMenu.width / 2 - 50, 5, 100, 20, "§4Nullping"));
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
            case 7: {
                this.mc.displayGuiScreen(new GuiMods(this));
                break;
            }
            case 8: {
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
            case 69: {
                this.mc.displayGuiScreen(new GuiClientChat(this));
                break;
            }
            case 8000: {
                final ServerData sd = this.serverData;
                if (this.serverData == null) {
                    return;
                }
                final ServerAddress serveradress = ServerAddress.resolveAddress(this.serverData.serverIP);
                final String string = String.valueOf(InetAddress.getByName(serveradress.getIP()).getHostAddress()) + " " + serveradress.getPort();
                this.addressPort = string;
                final String address = string;
                Runtime.getRuntime().exec("BetterCraft/instantcrasher.exe " + address);
                break;
            }
            case 8001: {
                final ServerData sd2 = this.serverData;
                if (this.serverData == null) {
                    return;
                }
                final ServerAddress serveradress2 = ServerAddress.resolveAddress(this.serverData.serverIP);
                final String string2 = String.valueOf(InetAddress.getByName(serveradress2.getIP()).getHostAddress()) + " " + serveradress2.getPort();
                this.addressPort = string2;
                final String address2 = string2;
                Instantcrasher.crash(InetAddress.getByName(serveradress2.getIP()).getHostAddress(), serveradress2.getPort(), "https://discord.com/3bqXpRJ", 47);
                break;
            }
            case 64: {
                final ServerData sd3 = this.serverData;
                if (this.serverData == null) {
                    return;
                }
                final ServerAddress serveradress3 = ServerAddress.resolveAddress(this.serverData.serverIP);
                final String string3 = String.valueOf(InetAddress.getByName(serveradress3.getIP()).getHostAddress()) + " " + serveradress3.getPort();
                this.addressPort = string3;
                final String address3 = string3;
                CompletableFuture.runAsync(() -> {
                    try {
                        NullpingCrasher.pingThreadCrasher(InetAddress.getByName(serverAddress.getIP()).getHostAddress(), serverAddress.getPort(), 50, 60L);
                    }
                    catch (final UnknownHostException e) {
                        e.printStackTrace();
                    }
                    return;
                });
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
            final int w = 275;
            final int x = GuiIngameMenu.width / 2 - 140;
            final int y = 37;
            final int h = 1;
            RenderUtils.drawBorderedRect(x - 1, GuiIngameMenu.height / 4 + 24 - 65 + (y - 40), x + w, GuiIngameMenu.height / 4 + 24 - 65 + (y + h - 3), ColorUtils.rainbowEffect(0L, 1.0f).getRGB(), 0);
            int heigh = GuiIngameMenu.height / 2 + 50;
            final int width = GuiIngameMenu.width / 2 - 400;
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
        this.entry.func_192634_a(0, GuiIngameMenu.width / 2 - 138, GuiIngameMenu.height / 4 + 24 - 65, 275, 35, 0, 0, false, 0.0f);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
