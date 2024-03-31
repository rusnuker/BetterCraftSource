// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import me.amkgre.bettercraft.client.utils.ServerDataFeaturedUtils;
import net.minecraft.client.multiplayer.GuiConnecting;
import com.google.common.collect.Lists;
import com.google.common.base.Splitter;
import me.amkgre.bettercraft.client.utils.ProtocolVersionUtils;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.utils.GeoUtils;
import viaforge.gui.GuiProtocolSelector;
import me.amkgre.bettercraft.client.mods.serverfinder.GuiServerFinder;
import java.util.concurrent.CompletableFuture;
import java.net.UnknownHostException;
import me.amkgre.bettercraft.client.mods.crasher.NullpingCrasher;
import me.amkgre.bettercraft.client.mods.crasher.ic.Instantcrasher;
import java.net.InetAddress;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.network.LanServerInfo;
import java.util.List;
import viaforge.protocols.ProtocolCollection;
import viaforge.ViaForge;
import net.minecraft.client.resources.I18n;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import org.apache.logging.log4j.LogManager;
import me.amkgre.bettercraft.client.utils.TimeHelperUtils;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.multiplayer.ServerData;
import me.amkgre.bettercraft.client.mods.partnerlist.ServerPartnerList;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.ServerPinger;
import org.apache.logging.log4j.Logger;

public class GuiMultiplayer extends GuiScreen
{
    private static final Logger LOGGER;
    private final ServerPinger oldServerPinger;
    private final GuiScreen parentScreen;
    public ServerSelectionList serverListSelector;
    public ServerList savedServerList;
    private ServerPartnerList savedServerListPartner;
    private GuiButton btnEditServer;
    private GuiButton btnSelectServer;
    private GuiButton btnDeleteServer;
    private boolean deletingServer;
    private boolean addingServer;
    private boolean editingServer;
    private boolean directConnect;
    private String hoveringText;
    private ServerData selectedServer;
    private LanServerDetector.LanServerList lanServerList;
    private LanServerDetector.ThreadLanServerFind lanServerDetector;
    private boolean initialized;
    public static boolean isPartnerPage;
    String lastAddress;
    private volatile String addressPort;
    private final TimeHelperUtils timeHelperUtils;
    
    static {
        LOGGER = LogManager.getLogger();
        GuiMultiplayer.isPartnerPage = false;
    }
    
    public GuiMultiplayer(final GuiScreen parentScreen) {
        this.oldServerPinger = new ServerPinger();
        this.lastAddress = "Pinging...";
        this.timeHelperUtils = new TimeHelperUtils();
        this.parentScreen = parentScreen;
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        if (this.initialized) {
            this.serverListSelector.setDimensions(GuiMultiplayer.width, GuiMultiplayer.height, 32, GuiMultiplayer.height - 64);
        }
        else {
            this.initialized = true;
            if (!GuiMultiplayer.isPartnerPage) {
                (this.savedServerList = new ServerList(this.mc)).loadServerList();
            }
            else {
                (this.savedServerListPartner = new ServerPartnerList(this.mc)).loadServerList();
            }
            this.lanServerList = new LanServerDetector.LanServerList();
            try {
                (this.lanServerDetector = new LanServerDetector.ThreadLanServerFind(this.lanServerList)).start();
            }
            catch (final Exception exception) {
                GuiMultiplayer.LOGGER.warn("Unable to start LAN server detection: {}", exception.getMessage());
            }
            this.serverListSelector = new ServerSelectionList(this, this.mc, GuiMultiplayer.width, GuiMultiplayer.height, 32, GuiMultiplayer.height - 64, 36);
            if (!GuiMultiplayer.isPartnerPage) {
                this.serverListSelector.updateOnlineServers(this.savedServerList);
            }
            else {
                this.serverListSelector.updateOnlineServersPartners(this.savedServerListPartner);
            }
        }
        this.createButtons();
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.serverListSelector.handleMouseInput();
    }
    
    public void createButtons() {
        this.btnEditServer = this.addButton(new GuiButton(7, GuiMultiplayer.width / 2 - 154, GuiMultiplayer.height - 28 - 5 + 110 - 105, 70, 20, I18n.format("selectServer.edit", new Object[0])));
        this.btnDeleteServer = this.addButton(new GuiButton(2, GuiMultiplayer.width / 2 - 74, GuiMultiplayer.height - 28 - 5 + 110 - 105, 70, 20, I18n.format("selectServer.delete", new Object[0])));
        this.btnSelectServer = this.addButton(new GuiButton(1, GuiMultiplayer.width / 2 - 154, GuiMultiplayer.height - 52 - 5 + 110 - 105, 70, 20, I18n.format("selectServer.select", new Object[0])));
        this.buttonList.add(new GuiButton(4, GuiMultiplayer.width / 2 - 74, GuiMultiplayer.height - 52 - 5 + 110 - 105, 70, 20, I18n.format("selectServer.direct", new Object[0])));
        final GuiButton addServer;
        this.buttonList.add(addServer = new GuiButton(3, GuiMultiplayer.width / 2 + 4, GuiMultiplayer.height - 52 - 5 + 110 - 105, 70, 20, I18n.format("selectServer.add", new Object[0])));
        final GuiButton refresh;
        this.buttonList.add(refresh = new GuiButton(8, GuiMultiplayer.width / 2 + 4, GuiMultiplayer.height - 28 - 5 + 110 - 105, 70, 20, I18n.format("selectServer.refresh", new Object[0])));
        this.buttonList.add(new GuiButton(10, GuiMultiplayer.width / 2 + 4 + 76, GuiMultiplayer.height - 52 - 5 + 110 - 105, 75, 20, I18n.format("Server Finder", new Object[0])));
        this.buttonList.add(new GuiButton(15, GuiMultiplayer.width / 2 + 4 + 76, GuiMultiplayer.height - 28 - 5 + 110 - 105, 75, 20, I18n.format("gui.cancel", new Object[0])));
        final GuiButton arrowRight;
        this.buttonList.add(arrowRight = new GuiButton(324, GuiMultiplayer.width / 2 + 4 + 76 + 80, GuiMultiplayer.height - 28 - 5 + 110 - 105, 20, 20, ">"));
        final GuiButton arrowLeft;
        this.buttonList.add(arrowLeft = new GuiButton(325, GuiMultiplayer.width / 2 - 154 - 20 - 4, GuiMultiplayer.height - 28 - 5 + 110 - 105, 20, 20, "<"));
        if (!GuiMultiplayer.isPartnerPage) {
            this.buttonList.add(new GuiButton(8000, GuiMultiplayer.width / 2 - 55, 5, 50, 20, "§cCrash 1"));
            this.buttonList.add(new GuiButton(8001, GuiMultiplayer.width / 2 + 10, 5, 50, 20, "§cCrash 2"));
            arrowLeft.visible = false;
            this.buttonList.add(new GuiButton(64, GuiMultiplayer.width - 105, 5, 100, 20, "§4Nullping"));
        }
        else {
            refresh.enabled = false;
            arrowRight.visible = false;
            addServer.enabled = false;
            this.btnEditServer.enabled = false;
        }
        this.buttonList.add(new GuiButton(1337, GuiMultiplayer.isPartnerPage ? (GuiMultiplayer.width / 2 - 50) : 5, 5, 100, 20, "§d" + ProtocolCollection.getProtocolById(ViaForge.getInstance().getVersion()).getName()));
        this.selectServer(this.serverListSelector.getSelected());
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        if (this.lanServerList.getWasUpdated()) {
            final List<LanServerInfo> list = this.lanServerList.getLanServers();
            this.lanServerList.setWasNotUpdated();
            this.serverListSelector.updateNetworkServers(list);
        }
        this.oldServerPinger.pingPendingNetworks();
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        if (this.lanServerDetector != null) {
            this.lanServerDetector.interrupt();
            this.lanServerDetector = null;
        }
        this.oldServerPinger.clearPendingNetworks();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            final GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (this.serverListSelector.getSelected() < 0) ? null : this.serverListSelector.getListEntry(this.serverListSelector.getSelected());
            if (button.id == 2 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                final String s4 = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData().serverName;
                if (s4 != null) {
                    this.deletingServer = true;
                    final String s5 = I18n.format("selectServer.deleteQuestion", new Object[0]);
                    final String s6 = "'" + s4 + "' " + I18n.format("selectServer.deleteWarning", new Object[0]);
                    final String s7 = I18n.format("selectServer.deleteButton", new Object[0]);
                    final String s8 = I18n.format("gui.cancel", new Object[0]);
                    final GuiYesNo guiyesno = new GuiYesNo(this, s5, s6, s7, s8, this.serverListSelector.getSelected());
                    this.mc.displayGuiScreen(guiyesno);
                }
            }
            else if (button.id == 8000) {
                final GuiListExtended.IGuiListEntry listEntry = (this.serverListSelector.getSelected() < 0) ? null : this.serverListSelector.getListEntry(this.serverListSelector.getSelected());
                if (listEntry == null) {
                    return;
                }
                final ServerData serverData = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
                if (serverData == null) {
                    return;
                }
                final ServerAddress serveradress = ServerAddress.resolveAddress(serverData.serverIP);
                final String string = String.valueOf(InetAddress.getByName(serveradress.getIP()).getHostAddress()) + " " + serveradress.getPort();
                this.addressPort = string;
                final String address = string;
                Runtime.getRuntime().exec("BetterCraft/instantcrasher.exe " + address);
            }
            else if (button.id == 8001) {
                final GuiListExtended.IGuiListEntry listEntry = (this.serverListSelector.getSelected() < 0) ? null : this.serverListSelector.getListEntry(this.serverListSelector.getSelected());
                if (listEntry == null) {
                    return;
                }
                final ServerData serverData = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
                if (serverData == null) {
                    return;
                }
                final ServerAddress serveradress = ServerAddress.resolveAddress(serverData.serverIP);
                final String string2 = String.valueOf(InetAddress.getByName(serveradress.getIP()).getHostAddress()) + " " + serveradress.getPort();
                this.addressPort = string2;
                final String address = string2;
                Instantcrasher.crash(InetAddress.getByName(serveradress.getIP()).getHostAddress(), serveradress.getPort(), "https://discord.com/3bqXpRJ", 47);
            }
            else if (button.id == 64) {
                final GuiListExtended.IGuiListEntry listEntry = (this.serverListSelector.getSelected() < 0) ? null : this.serverListSelector.getListEntry(this.serverListSelector.getSelected());
                if (listEntry == null) {
                    return;
                }
                final ServerData serverData = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
                if (serverData == null) {
                    return;
                }
                final ServerAddress serveradress = ServerAddress.resolveAddress(serverData.serverIP);
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
            else if (button.id == 1) {
                this.connectToSelected();
            }
            else if (button.id == 4) {
                this.directConnect = true;
                this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false);
                this.mc.displayGuiScreen(new GuiScreenServerList(this, this.selectedServer));
            }
            else if (button.id == 3) {
                this.addingServer = true;
                this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false);
                this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
            }
            else if (button.id == 7 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                this.editingServer = true;
                final ServerData serverdata = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
                (this.selectedServer = new ServerData(serverdata.serverName, serverdata.serverIP, false)).copyFrom(serverdata);
                this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
            }
            else if (button.id == 10) {
                this.mc.displayGuiScreen(new GuiServerFinder(this));
            }
            else if (button.id == 15) {
                this.mc.displayGuiScreen(this.parentScreen);
            }
            else if (button.id == 8) {
                this.refreshServerList();
            }
            else if (button.id == 324) {
                GuiMultiplayer.isPartnerPage = !GuiMultiplayer.isPartnerPage;
                this.mc.displayGuiScreen(new GuiMultiplayer(this.parentScreen));
                this.refreshServerList();
            }
            else if (button.id == 325) {
                GuiMultiplayer.isPartnerPage = !GuiMultiplayer.isPartnerPage;
                this.mc.displayGuiScreen(new GuiMultiplayer(this.parentScreen));
                this.refreshServerList();
            }
            else if (button.id == 1337) {
                this.mc.displayGuiScreen(new GuiProtocolSelector(this));
            }
        }
    }
    
    private void refreshServerList() {
        if (!GuiMultiplayer.isPartnerPage) {
            this.savedServerList = new ServerList(this.mc);
            this.serverListSelector.updateOnlineServers(this.savedServerList);
        }
        else {
            this.savedServerListPartner = new ServerPartnerList(this.mc);
            this.serverListSelector.updateOnlineServersPartners(this.savedServerListPartner);
        }
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        final GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (this.serverListSelector.getSelected() < 0) ? null : this.serverListSelector.getListEntry(this.serverListSelector.getSelected());
        if (this.deletingServer) {
            this.deletingServer = false;
            if (result && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                this.savedServerList.removeServerData(this.serverListSelector.getSelected());
                this.savedServerList.saveServerList();
                this.serverListSelector.setSelectedSlotIndex(-1);
                this.serverListSelector.updateOnlineServers(this.savedServerList);
            }
            this.mc.displayGuiScreen(this);
        }
        else if (this.directConnect) {
            this.directConnect = false;
            if (result) {
                this.connectToServer(this.selectedServer);
            }
            else {
                this.mc.displayGuiScreen(this);
            }
        }
        else if (this.addingServer) {
            this.addingServer = false;
            if (result) {
                this.savedServerList.addServerData(this.selectedServer);
                this.savedServerList.saveServerList();
                this.serverListSelector.setSelectedSlotIndex(-1);
                this.serverListSelector.updateOnlineServers(this.savedServerList);
            }
            this.mc.displayGuiScreen(this);
        }
        else if (this.editingServer) {
            this.editingServer = false;
            if (result && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                final ServerData serverdata = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
                serverdata.serverName = this.selectedServer.serverName;
                serverdata.serverIP = this.selectedServer.serverIP;
                serverdata.copyFrom(this.selectedServer);
                this.savedServerList.saveServerList();
                this.serverListSelector.updateOnlineServers(this.savedServerList);
            }
            this.mc.displayGuiScreen(this);
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        final int i = this.serverListSelector.getSelected();
        final GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (i < 0) ? null : this.serverListSelector.getListEntry(i);
        if (keyCode == 63) {
            this.refreshServerList();
        }
        else if (i >= 0) {
            if (keyCode == 200) {
                if (isShiftKeyDown()) {
                    if (i > 0 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                        this.savedServerList.swapServers(i, i - 1);
                        this.selectServer(this.serverListSelector.getSelected() - 1);
                        this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                        this.serverListSelector.updateOnlineServers(this.savedServerList);
                    }
                }
                else if (i > 0) {
                    this.selectServer(this.serverListSelector.getSelected() - 1);
                    this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                    if (this.serverListSelector.getListEntry(this.serverListSelector.getSelected()) instanceof ServerListEntryLanScan) {
                        if (this.serverListSelector.getSelected() > 0) {
                            this.selectServer(this.serverListSelector.getSize() - 1);
                            this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                        }
                        else {
                            this.selectServer(-1);
                        }
                    }
                }
                else {
                    this.selectServer(-1);
                }
            }
            else if (keyCode == 208) {
                if (isShiftKeyDown()) {
                    if (i < this.savedServerList.countServers() - 1) {
                        this.savedServerList.swapServers(i, i + 1);
                        this.selectServer(i + 1);
                        this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                        this.serverListSelector.updateOnlineServers(this.savedServerList);
                    }
                }
                else if (i < this.serverListSelector.getSize()) {
                    this.selectServer(this.serverListSelector.getSelected() + 1);
                    this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                    if (this.serverListSelector.getListEntry(this.serverListSelector.getSelected()) instanceof ServerListEntryLanScan) {
                        if (this.serverListSelector.getSelected() < this.serverListSelector.getSize() - 1) {
                            this.selectServer(this.serverListSelector.getSize() + 1);
                            this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                        }
                        else {
                            this.selectServer(-1);
                        }
                    }
                }
                else {
                    this.selectServer(-1);
                }
            }
            else if (keyCode != 28 && keyCode != 156) {
                super.keyTyped(typedChar, keyCode);
            }
            else {
                this.actionPerformed(this.buttonList.get(2));
            }
        }
        else {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        this.hoveringText = null;
        this.drawDefaultBackground();
        this.serverListSelector.drawScreen(mouseX, mouseY, partialTicks);
        try {
            final GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (this.serverListSelector.getSelected() < 0) ? null : this.serverListSelector.getListEntry(this.serverListSelector.getSelected());
            final ServerData sd = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
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
            if (sd.pingToServer < 0L) {
                Gui.drawString(this.fontRendererObj, "§5Remote: §7Pinging...", GuiMultiplayer.width / 4 + 620, Minecraft.getMinecraft().displayHeight / 2 - 50, -1);
            }
            else {
                Gui.drawString(this.fontRendererObj, "§5Remote: §7" + this.addressPort.replace("null", ""), GuiMultiplayer.width / 4 + 575, Minecraft.getMinecraft().displayHeight / 2 - 50, -1);
            }
            if (version.equalsIgnoreCase("1.12.2") && sd.pingToServer < 0L) {
                Gui.drawString(this.fontRendererObj, "§5Brand: §7Pinging...", GuiMultiplayer.width / 4 + 620, Minecraft.getMinecraft().displayHeight / 2 - 40, -1);
            }
            else {
                Gui.drawString(this.fontRendererObj, "§5Brand: §7" + version.replaceAll("1.8.x, 1.9.x, 1.10.x, 1.11.x, 1.12.x, 1.13.x, 1.14.x, 1.15.x, 1.16.x", "1.8.x-1.16.x").replaceAll("1.7.x, ", "").replaceAll("PE-1.8.x, PE-1.9.x, PE-1.10.x, PE-1.11.x, PE-1.12.x, PE-1.13.x, PE-1.14.x, PE-1.15.x, PE-1.16.x", "PE-1.8.x - PE-1.16.x"), GuiMultiplayer.width / 4 + 575, Minecraft.getMinecraft().displayHeight / 2 - 40, -1);
            }
            if (sd.pingToServer < 0L) {
                Gui.drawString(this.fontRendererObj, "§5Protocol: §7Pinging...", GuiMultiplayer.width / 4 + 620, Minecraft.getMinecraft().displayHeight / 2 - 30, -1);
            }
            else {
                final FontRenderer fontRendererObj = this.fontRendererObj;
                final StringBuilder append = new StringBuilder("§5Protocol: §7").append(protocolVersion).append(" -> ");
                ProtocolVersionUtils.getInstance();
                Gui.drawString(fontRendererObj, append.append(ProtocolVersionUtils.getKnownAs(sd.version)).toString(), GuiMultiplayer.width / 4 + 575, Minecraft.getMinecraft().displayHeight / 2 - 30, -1);
            }
            if (sd.pingToServer < 0L) {
                Gui.drawString(this.fontRendererObj, "§5Last Ping: §7Pinging...", GuiMultiplayer.width / 4 + 620, Minecraft.getMinecraft().displayHeight / 2 - 20, -1);
            }
            else {
                Gui.drawString(this.fontRendererObj, "§5Last Ping: §7" + ping + "ms", GuiMultiplayer.width / 4 + 575, Minecraft.getMinecraft().displayHeight / 2 - 20, -1);
            }
            if (sd.pingToServer < 0L) {
                Gui.drawString(this.fontRendererObj, "§5AS: §7Pinging...", GuiMultiplayer.width / 4 - 230, Minecraft.getMinecraft().displayHeight / 2 - 50, -1);
            }
            else {
                Gui.drawString(this.fontRendererObj, "§5AS: §7" + GeoUtils.getInstance().getAS().replaceAll("Center ", "").replaceAll("oration", "").replaceAll("Waldecker trading as LUMASERV Systems", "").replaceAll("GmbH", "").replaceAll(". Inc.", "").replaceAll(", LLC", "").replaceAll("Corp.", "").replaceAll("TeleHost", "Tele").replaceAll("e-commerce", "").replaceAll("IT Services & Consulting", "").replaceAll("UG (haftungsbeschrankt)", ""), GuiMultiplayer.width / 4 - 230, Minecraft.getMinecraft().displayHeight / 2 - 50, -1);
            }
            if (sd.pingToServer < 0L) {
                Gui.drawString(this.fontRendererObj, "§5City: §7Pinging...", GuiMultiplayer.width / 4 - 230, Minecraft.getMinecraft().displayHeight / 2 - 40, -1);
            }
            else {
                Gui.drawString(this.fontRendererObj, "§5City: §7" + GeoUtils.getInstance().getCITY(), GuiMultiplayer.width / 4 - 230, Minecraft.getMinecraft().displayHeight / 2 - 40, -1);
            }
            if (sd.pingToServer < 0L) {
                Gui.drawString(this.fontRendererObj, "§5ORG: §7Pinging...", GuiMultiplayer.width / 4 - 230, Minecraft.getMinecraft().displayHeight / 2 - 30, -1);
            }
            else {
                Gui.drawString(this.fontRendererObj, "§5ORG: §7" + GeoUtils.getInstance().getORG().replaceAll("- Connecting your World!", "").replaceAll("Cloud ", "").replaceAll("- DDoS-Protected Gameservers and more", "").replaceAll("www.", "").replaceAll("Corp.", "").replaceAll("(haftungsbeschraenkt) & Co. KG", "").replaceAll("trading as Gericke KG", ""), GuiMultiplayer.width / 4 - 230, Minecraft.getMinecraft().displayHeight / 2 - 30, -1);
            }
            if (sd.pingToServer < 0L) {
                Gui.drawString(this.fontRendererObj, "§5Country: §7Pinging...", GuiMultiplayer.width / 4 - 230, Minecraft.getMinecraft().displayHeight / 2 - 20, -1);
            }
            else {
                Gui.drawString(this.fontRendererObj, "§5Country: §7" + GeoUtils.getInstance().getCOUNTRY(), GuiMultiplayer.width / 4 - 230, Minecraft.getMinecraft().displayHeight / 2 - 20, -1);
            }
        }
        catch (final Throwable t) {}
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.hoveringText != null) {
            this.drawHoveringText((List<String>)Lists.newArrayList((Iterable<?>)Splitter.on("\n").split(this.hoveringText)), mouseX, mouseY);
        }
    }
    
    public void connectToSelected() {
        final GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (this.serverListSelector.getSelected() < 0) ? null : this.serverListSelector.getListEntry(this.serverListSelector.getSelected());
        if (guilistextended$iguilistentry instanceof ServerListEntryNormal) {
            this.connectToServer(((ServerListEntryNormal)guilistextended$iguilistentry).getServerData());
        }
        else if (guilistextended$iguilistentry instanceof ServerListEntryLanDetected) {
            final LanServerInfo lanserverinfo = ((ServerListEntryLanDetected)guilistextended$iguilistentry).getServerData();
            this.connectToServer(new ServerData(lanserverinfo.getServerMotd(), lanserverinfo.getServerIpPort(), true));
        }
    }
    
    private void connectToServer(final ServerData server) {
        this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, server));
    }
    
    public void selectServer(final int index) {
        this.serverListSelector.setSelectedSlotIndex(index);
        final GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (index < 0) ? null : this.serverListSelector.getListEntry(index);
        this.btnSelectServer.enabled = false;
        if (!GuiMultiplayer.isPartnerPage) {
            this.btnEditServer.enabled = false;
        }
        this.btnDeleteServer.enabled = false;
        if (guilistextended$iguilistentry != null && !(guilistextended$iguilistentry instanceof ServerListEntryLanScan)) {
            this.btnSelectServer.enabled = true;
            if (guilistextended$iguilistentry instanceof ServerListEntryNormal && !GuiMultiplayer.isPartnerPage) {
                this.btnEditServer.enabled = true;
                this.btnDeleteServer.enabled = true;
            }
            if (this.savedServerList.getServerData(index) instanceof ServerDataFeaturedUtils && !GuiMultiplayer.isPartnerPage) {
                this.btnEditServer.enabled = false;
                this.btnDeleteServer.enabled = false;
            }
        }
    }
    
    public ServerPinger getOldServerPinger() {
        return this.oldServerPinger;
    }
    
    public void setHoveringText(final String p_146793_1_) {
        this.hoveringText = p_146793_1_;
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.serverListSelector.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.serverListSelector.mouseReleased(mouseX, mouseY, state);
    }
    
    public ServerList getServerList() {
        return this.savedServerList;
    }
    
    public boolean canMoveUp(final ServerListEntryNormal p_175392_1_, final int p_175392_2_) {
        return !GuiMultiplayer.isPartnerPage && p_175392_2_ > 0;
    }
    
    public boolean canMoveDown(final ServerListEntryNormal p_175394_1_, final int p_175394_2_) {
        return !GuiMultiplayer.isPartnerPage && p_175394_2_ < this.savedServerList.countServers() - 1;
    }
    
    public void moveServerUp(final ServerListEntryNormal p_175391_1_, final int p_175391_2_, final boolean p_175391_3_) {
        if (GuiMultiplayer.isPartnerPage) {
            return;
        }
        final int i = p_175391_3_ ? 0 : (p_175391_2_ - 1);
        this.savedServerList.swapServers(p_175391_2_, i);
        if (this.serverListSelector.getSelected() == p_175391_2_) {
            this.selectServer(i);
        }
        this.serverListSelector.updateOnlineServers(this.savedServerList);
    }
    
    public void moveServerDown(final ServerListEntryNormal p_175393_1_, final int p_175393_2_, final boolean p_175393_3_) {
        if (GuiMultiplayer.isPartnerPage) {
            return;
        }
        final int i = p_175393_3_ ? (this.savedServerList.countServers() - 1) : (p_175393_2_ + 1);
        this.savedServerList.swapServers(p_175393_2_, i);
        if (this.serverListSelector.getSelected() == p_175393_2_) {
            this.selectServer(i);
        }
        this.serverListSelector.updateOnlineServers(this.savedServerList);
    }
}
