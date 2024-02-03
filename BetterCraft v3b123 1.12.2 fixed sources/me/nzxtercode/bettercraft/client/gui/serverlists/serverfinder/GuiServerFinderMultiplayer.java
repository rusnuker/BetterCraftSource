// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.gui.serverlists.serverfinder;

import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.gui.ServerListEntryLanDetected;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.base.Splitter;
import net.minecraft.client.gui.ServerListEntryLanScan;
import net.minecraft.client.gui.GuiListExtended;
import me.nzxtercode.bettercraft.client.gui.serverlists.partner.GuiPartnerMultiplayer;
import net.minecraft.client.gui.GuiMultiplayer;
import net.labymod.labyconnect.gui.GuiFriendsLayout;
import viamcp.gui.GuiProtocolSelector;
import net.minecraft.client.gui.GuiMainMenu;
import java.util.concurrent.Executor;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import net.minecraft.client.gui.GuiScreenServerList;
import net.minecraft.util.EnumChatFormatting;
import viamcp.vialoadingbase.ViaLoadingBase;
import net.minecraft.client.resources.I18n;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import org.apache.logging.log4j.LogManager;
import me.nzxtercode.bettercraft.client.gui.serverlists.partner.PartnerServerList;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.network.OldServerPinger;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.GuiScreen;

public class GuiServerFinderMultiplayer extends GuiScreen implements GuiYesNoCallback
{
    private static final Logger logger;
    private final OldServerPinger oldServerPinger;
    private GuiScreen parentScreen;
    public ServerFinderServerSelectionList serverListSelector;
    public static ServerFinderServerList savedServerList;
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
    public ServerList normalServerList;
    public PartnerServerList partnerServerList;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public GuiServerFinderMultiplayer(final GuiScreen parentScreen) {
        this.oldServerPinger = new OldServerPinger();
        this.parentScreen = parentScreen;
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        if (!this.initialized) {
            this.initialized = true;
            (GuiServerFinderMultiplayer.savedServerList = new ServerFinderServerList(this.mc)).loadServerList();
            (this.normalServerList = new ServerList(this.mc)).loadServerList();
            (this.partnerServerList = new PartnerServerList(this.mc)).loadServerList();
            this.lanServerList = new LanServerDetector.LanServerList();
            try {
                (this.lanServerDetector = new LanServerDetector.ThreadLanServerFind(this.lanServerList)).start();
            }
            catch (final Exception exception) {
                GuiServerFinderMultiplayer.logger.warn("Unable to start LAN server detection: " + exception.getMessage());
            }
            (this.serverListSelector = new ServerFinderServerSelectionList(this, this.mc, GuiServerFinderMultiplayer.width, GuiServerFinderMultiplayer.height, 32, GuiServerFinderMultiplayer.height - 64, 36)).func_148195_a(GuiServerFinderMultiplayer.savedServerList);
        }
        else {
            this.serverListSelector.setDimensions(GuiServerFinderMultiplayer.width, GuiServerFinderMultiplayer.height, 32, GuiServerFinderMultiplayer.height - 64);
        }
        this.createButtons();
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.serverListSelector.handleMouseInput();
    }
    
    public void createButtons() {
        this.buttonList.add(new GuiButton(7, GuiServerFinderMultiplayer.width / 2 + 4 + 50, GuiServerFinderMultiplayer.height - 28, 100, 20, "Clean All"));
        this.buttonList.add(this.btnDeleteServer = new GuiButton(2, GuiServerFinderMultiplayer.width / 2 - 154, GuiServerFinderMultiplayer.height - 28, 100, 20, I18n.format("selectServer.delete", new Object[0])));
        this.buttonList.add(this.btnSelectServer = new GuiButton(1, GuiServerFinderMultiplayer.width / 2 - 154, GuiServerFinderMultiplayer.height - 52, 100, 20, I18n.format("selectServer.select", new Object[0])));
        this.buttonList.add(new GuiButton(3, GuiServerFinderMultiplayer.width / 2 + 4 + 50, GuiServerFinderMultiplayer.height - 52, 100, 20, I18n.format("selectServer.add", new Object[0])));
        this.buttonList.add(new GuiButton(8, GuiServerFinderMultiplayer.width / 2 - 50, GuiServerFinderMultiplayer.height - 28, 100, 20, I18n.format("selectServer.refresh", new Object[0])));
        this.buttonList.add(new GuiButton(0, GuiServerFinderMultiplayer.width / 2 - 50, GuiServerFinderMultiplayer.height - 52, 100, 20, I18n.format("gui.cancel", new Object[0])));
        this.buttonList.add(new GuiButton(10, 5, 5, 75, 20, ViaLoadingBase.getInstance().getTargetVersion().getName()));
        this.buttonList.add(new GuiButton(11, GuiServerFinderMultiplayer.width - 80, 5, 75, 20, "LabyMod"));
        this.buttonList.add(new GuiButton(100, GuiServerFinderMultiplayer.width / 2 - 100, 15, 50, 10, "My " + EnumChatFormatting.RED.toString() + String.valueOf(this.normalServerList.countServers())));
        this.buttonList.add(new GuiButton(101, GuiServerFinderMultiplayer.width / 2 - 25, 15, 50, 10, "Partner " + EnumChatFormatting.RED.toString() + String.valueOf(this.partnerServerList.countServers())));
        this.buttonList.add(new GuiButton(102, GuiServerFinderMultiplayer.width / 2 + 50, 15, 50, 12, "Finder " + EnumChatFormatting.RED.toString() + String.valueOf(GuiServerFinderMultiplayer.savedServerList.countServers())));
        this.selectServer(this.serverListSelector.func_148193_k());
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
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
            final GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (this.serverListSelector.func_148193_k() < 0) ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
            if (button.id == 2 && guilistextended$iguilistentry instanceof ServerFinderServerListEntryNormal) {
                final String s4 = ((ServerFinderServerListEntryNormal)guilistextended$iguilistentry).getServerData().serverName;
                if (s4 != null) {
                    this.deletingServer = true;
                    GuiServerFinderMultiplayer.savedServerList.removeServerData(this.serverListSelector.func_148193_k());
                    GuiServerFinderMultiplayer.savedServerList.saveServerList();
                    this.serverListSelector.setSelectedSlotIndex(-1);
                    this.serverListSelector.func_148195_a(GuiServerFinderMultiplayer.savedServerList);
                }
            }
            else if (button.id == 1) {
                this.connectToSelected();
            }
            else if (button.id == 4) {
                this.directConnect = true;
                this.mc.displayGuiScreen(new GuiScreenServerList(this, this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false)));
            }
            else if (button.id == 3) {
                this.addingServer = true;
                this.mc.displayGuiScreen(new GuiServerFinderAddServer(this));
            }
            else if (button.id == 7) {
                CompletableFuture.runAsync(() -> {
                    for (int i = GuiServerFinderMultiplayer.savedServerList.countServers() - 1; i >= 0; --i) {
                        final ServerData server = GuiServerFinderMultiplayer.savedServerList.getServerData(i);
                        GuiServerFinderMultiplayer.savedServerList.removeServerData(i);
                        GuiServerFinderMultiplayer.savedServerList.saveServerList();
                        this.serverListSelector.setSelectedSlotIndex(-1);
                    }
                    this.refreshServerList();
                }, Executors.newSingleThreadExecutor());
            }
            else if (button.id == 0) {
                this.mc.displayGuiScreen(new GuiMainMenu());
            }
            else if (button.id == 8) {
                this.refreshServerList();
            }
            else if (button.id == 10) {
                this.mc.displayGuiScreen(new GuiProtocolSelector(this));
            }
            else if (button.id == 11) {
                this.mc.displayGuiScreen(new GuiFriendsLayout(this));
            }
            else if (button.id == 100) {
                this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
            }
            else if (button.id == 101) {
                this.mc.displayGuiScreen(new GuiPartnerMultiplayer(new GuiMainMenu()));
            }
        }
    }
    
    private void refreshServerList() {
        this.mc.displayGuiScreen(new GuiServerFinderMultiplayer(this.parentScreen));
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        final GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (this.serverListSelector.func_148193_k() < 0) ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
        if (this.deletingServer) {
            this.deletingServer = false;
            if (result && guilistextended$iguilistentry instanceof ServerFinderServerListEntryNormal) {
                GuiServerFinderMultiplayer.savedServerList.removeServerData(this.serverListSelector.func_148193_k());
                GuiServerFinderMultiplayer.savedServerList.saveServerList();
                this.serverListSelector.setSelectedSlotIndex(-1);
                this.serverListSelector.func_148195_a(GuiServerFinderMultiplayer.savedServerList);
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
                this.serverListSelector.setSelectedSlotIndex(-1);
                this.serverListSelector.func_148195_a(GuiServerFinderMultiplayer.savedServerList);
            }
            this.mc.displayGuiScreen(this);
        }
        else if (this.editingServer) {
            this.editingServer = false;
            if (result && guilistextended$iguilistentry instanceof ServerFinderServerListEntryNormal) {
                final ServerData serverdata = ((ServerFinderServerListEntryNormal)guilistextended$iguilistentry).getServerData();
                serverdata.serverName = this.selectedServer.serverName;
                serverdata.serverIP = this.selectedServer.serverIP;
                serverdata.copyFrom(this.selectedServer);
                this.serverListSelector.func_148195_a(GuiServerFinderMultiplayer.savedServerList);
            }
            this.mc.displayGuiScreen(this);
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        final int i = this.serverListSelector.func_148193_k();
        final GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (i < 0) ? null : this.serverListSelector.getListEntry(i);
        if (keyCode == 63) {
            this.refreshServerList();
        }
        else if (i >= 0) {
            if (keyCode == 200) {
                if (isShiftKeyDown()) {
                    if (i > 0 && guilistextended$iguilistentry instanceof ServerFinderServerListEntryNormal) {
                        this.selectServer(this.serverListSelector.func_148193_k() - 1);
                        this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                        this.serverListSelector.func_148195_a(GuiServerFinderMultiplayer.savedServerList);
                    }
                }
                else if (i > 0) {
                    this.selectServer(this.serverListSelector.func_148193_k() - 1);
                    this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                    if (this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k()) instanceof ServerListEntryLanScan) {
                        if (this.serverListSelector.func_148193_k() > 0) {
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
                    if (i < GuiServerFinderMultiplayer.savedServerList.countServers() - 1) {
                        this.selectServer(i + 1);
                        this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                        this.serverListSelector.func_148195_a(GuiServerFinderMultiplayer.savedServerList);
                    }
                }
                else if (i < this.serverListSelector.getSize()) {
                    this.selectServer(this.serverListSelector.func_148193_k() + 1);
                    this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                    if (this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k()) instanceof ServerListEntryLanScan) {
                        if (this.serverListSelector.func_148193_k() < this.serverListSelector.getSize() - 1) {
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
        this.hoveringText = null;
        this.drawDefaultBackground();
        this.serverListSelector.drawScreen(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.hoveringText != null) {
            this.drawHoveringText((List<String>)Lists.newArrayList((Iterable<?>)Splitter.on("\n").split(this.hoveringText)), mouseX, mouseY);
        }
    }
    
    public void connectToSelected() {
        final GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (this.serverListSelector.func_148193_k() < 0) ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
        if (guilistextended$iguilistentry instanceof ServerFinderServerListEntryNormal) {
            this.connectToServer(((ServerFinderServerListEntryNormal)guilistextended$iguilistentry).getServerData());
        }
        else if (guilistextended$iguilistentry instanceof ServerListEntryLanDetected) {
            final LanServerDetector.LanServer lanserverdetector$lanserver = ((ServerListEntryLanDetected)guilistextended$iguilistentry).getLanServer();
            this.connectToServer(new ServerData(lanserverdetector$lanserver.getServerMotd(), lanserverdetector$lanserver.getServerIpPort(), true));
        }
    }
    
    private void connectToServer(final ServerData server) {
        this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, server));
    }
    
    public void selectServer(final int index) {
        this.serverListSelector.setSelectedSlotIndex(index);
        final GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (index < 0) ? null : this.serverListSelector.getListEntry(index);
        this.btnSelectServer.enabled = false;
        if (guilistextended$iguilistentry != null && !(guilistextended$iguilistentry instanceof ServerListEntryLanScan)) {
            this.btnSelectServer.enabled = true;
            final boolean b = guilistextended$iguilistentry instanceof ServerFinderServerListEntryNormal;
        }
    }
    
    public OldServerPinger getOldServerPinger() {
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
    
    public ServerFinderServerList getServerList() {
        return GuiServerFinderMultiplayer.savedServerList;
    }
    
    public boolean func_175392_a(final ServerFinderServerListEntryNormal p_175392_1_, final int p_175392_2_) {
        return p_175392_2_ > 0;
    }
    
    public boolean func_175394_b(final ServerFinderServerListEntryNormal p_175394_1_, final int p_175394_2_) {
        return p_175394_2_ < GuiServerFinderMultiplayer.savedServerList.countServers() - 1;
    }
    
    public void func_175391_a(final ServerFinderServerListEntryNormal p_175391_1_, final int p_175391_2_, final boolean p_175391_3_) {
        final int i = p_175391_3_ ? 0 : (p_175391_2_ - 1);
        if (this.serverListSelector.func_148193_k() == p_175391_2_) {
            this.selectServer(i);
        }
        this.serverListSelector.func_148195_a(GuiServerFinderMultiplayer.savedServerList);
    }
    
    public void func_175393_b(final ServerFinderServerListEntryNormal p_175393_1_, final int p_175393_2_, final boolean p_175393_3_) {
        final int i = p_175393_3_ ? (GuiServerFinderMultiplayer.savedServerList.countServers() - 1) : (p_175393_2_ + 1);
        if (this.serverListSelector.func_148193_k() == p_175393_2_) {
            this.selectServer(i);
        }
        this.serverListSelector.func_148195_a(GuiServerFinderMultiplayer.savedServerList);
    }
}
