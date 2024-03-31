// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.gui.advanced;

import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.network.LanServerInfo;
import net.minecraft.client.gui.ServerListEntryLanDetected;
import net.minecraft.client.gui.ServerListEntryLanScan;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.base.Splitter;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.world.storage.ISaveFormat;
import java.net.URI;
import java.io.IOException;
import java.awt.datatransfer.Clipboard;
import net.minecraft.client.gui.GuiListExtended;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import net.minecraft.client.gui.GuiScreenAddServer;
import net.minecraft.client.gui.GuiScreenServerList;
import me.amkgre.bettercraft.client.gui.GuiTools;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiWorldSelection;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GLContext;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.ServerPinger;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.gui.GuiScreen;

public class AdvancedGuiMainMenu extends GuiScreen
{
    private static final Logger logger;
    private final Object threadLock;
    private String openGLWarning1;
    private String openGLWarning2;
    private String openGLWarningLink;
    public static final String field_96138_a;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    public final ServerPinger serverPinger;
    public AdvancedServerSelectionList serverListSelector;
    private ServerList savedServerList;
    private GuiButton btnEditServer;
    private GuiButton btnSelectServer;
    private GuiButton btnDeleteServer;
    private GuiButton btnCopyIP;
    private boolean deletingServer;
    private boolean addingServer;
    private boolean editingServer;
    private boolean directConnect;
    private String hoveringText;
    private ServerData selectedServer;
    private LanServerDetector.ThreadLanServerFind lanServerDetector;
    private boolean initialized;
    
    static {
        logger = LogManager.getLogger();
        field_96138_a = "Please click " + TextFormatting.UNDERLINE + "here" + TextFormatting.RESET + " for more information.";
    }
    
    public AdvancedGuiMainMenu() {
        this.threadLock = new Object();
        this.openGLWarning1 = "";
        this.serverPinger = new ServerPinger();
        this.openGLWarning2 = AdvancedGuiMainMenu.field_96138_a;
        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
            this.openGLWarning1 = I18n.format("title.oldgl1", new Object[0]);
            this.openGLWarning2 = I18n.format("title.oldgl2", new Object[0]);
            this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        final int j = AdvancedGuiMainMenu.height / 4 + 48;
        this.buttonList.add(new GuiButton(77, AdvancedGuiMainMenu.width / 2 - 450 - 10, j - 48, "AltManager"));
        this.buttonList.add(new GuiButton(857, AdvancedGuiMainMenu.width / 2 - 450 - 10, j - 24, I18n.format("Tools", new Object[0])));
        this.buttonList.add(new GuiButton(2, AdvancedGuiMainMenu.width / 2 - 450 - 10, j, I18n.format("menu.options", new Object[0])));
        this.buttonList.add(new GuiButton(3, AdvancedGuiMainMenu.width / 2 - 450 - 10, j + 24, I18n.format("menu.quit", new Object[0])));
        final Object object = this.threadLock;
        synchronized (object) {
            final int field_92023_s = this.fontRendererObj.getStringWidth(this.openGLWarning1);
            final int field_92024_r = this.fontRendererObj.getStringWidth(this.openGLWarning2);
            final int k = Math.max(field_92023_s, field_92024_r);
            this.field_92022_t = (AdvancedGuiMainMenu.width - k) / 2;
            this.field_92021_u = this.buttonList.get(0).yPosition - 24;
            this.field_92020_v = this.field_92022_t + k;
            this.field_92019_w = this.field_92021_u + 24;
            monitorexit(object);
        }
        this.mc.func_181537_a(false);
        Keyboard.enableRepeatEvents(true);
        if (!this.initialized) {
            this.initialized = true;
            (this.savedServerList = new ServerList(this.mc)).loadServerList();
            (this.serverListSelector = new AdvancedServerSelectionList(this, this.mc, AdvancedGuiMainMenu.width + 290, AdvancedGuiMainMenu.height, 12, AdvancedGuiMainMenu.height, 36)).load(this.savedServerList);
        }
        else {
            this.serverListSelector.setDimensions(AdvancedGuiMainMenu.width + 290, AdvancedGuiMainMenu.height, 12, AdvancedGuiMainMenu.height);
        }
        this.createButtons();
    }
    
    public void createButtons() {
        final int j = AdvancedGuiMainMenu.height / 4 + 48;
        this.buttonList.add(new GuiButton(41, AdvancedGuiMainMenu.width / 2 - 450 - 10, j + 72, I18n.format("selectServer.direct", new Object[0])));
        this.buttonList.add(new GuiButton(31, AdvancedGuiMainMenu.width / 2 - 450 - 10, j + 96, I18n.format("selectServer.add", new Object[0])));
        this.btnCopyIP = new GuiButton(89, AdvancedGuiMainMenu.width / 2 - 450 - 10, j + 120, "Copy IP");
        this.buttonList.add(this.btnCopyIP);
        this.btnDeleteServer = new GuiButton(21, AdvancedGuiMainMenu.width / 2 - 450 - 10, j + 144, I18n.format("selectServer.delete", new Object[0]));
        this.buttonList.add(this.btnDeleteServer);
        this.btnEditServer = new GuiButton(71, AdvancedGuiMainMenu.width / 2 - 450 - 10, j + 168, I18n.format("selectServer.edit", new Object[0]));
        this.buttonList.add(this.btnEditServer);
        this.buttonList.add(new GuiButton(81, AdvancedGuiMainMenu.width / 2 - 450 - 10, j + 192, I18n.format("selectServer.refresh", new Object[0])));
        this.btnSelectServer = new GuiButton(1, -999, -999, 100, 20, I18n.format("selectServer.select", new Object[0]));
        this.buttonList.add(this.btnSelectServer);
        this.selectServer(this.serverListSelector.getSelectedSlotIndex());
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiWorldSelection(this));
        }
        else if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        else if (button.id == 3) {
            this.mc.shutdown();
        }
        else if (button.id == 857) {
            this.mc.displayGuiScreen(new GuiTools(this));
        }
        if (button.enabled) {
            final GuiListExtended.IGuiListEntry iGuiListEntry;
            final GuiListExtended.IGuiListEntry entry = iGuiListEntry = ((this.serverListSelector.getSelectedSlotIndex() < 0) ? null : this.serverListSelector.getListEntry(this.serverListSelector.getSelectedSlotIndex()));
            if (button.id == 21 && entry instanceof AdvancedServerListEntryNormal) {
                final String s4 = ((AdvancedServerListEntryNormal)entry).getServerData().serverName;
                if (s4 != null) {
                    this.savedServerList.removeServerData(this.serverListSelector.getSelectedSlotIndex());
                    this.savedServerList.saveServerList();
                    this.serverListSelector.setSelectedSlotIndex(-1);
                    this.serverListSelector.load(this.savedServerList);
                    this.refreshServerList();
                }
            }
            else if (button.id == 11) {
                this.connectToSelected();
            }
            else if (button.id == 41) {
                this.directConnect = true;
                this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false);
                this.mc.displayGuiScreen(new GuiScreenServerList(this, this.selectedServer));
            }
            else if (button.id == 31) {
                this.addingServer = true;
                this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false);
                this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
            }
            else if (button.id == 71 && entry instanceof AdvancedServerListEntryNormal) {
                this.editingServer = true;
                final ServerData serverdata = ((AdvancedServerListEntryNormal)entry).getServerData();
                (this.selectedServer = new ServerData(serverdata.serverName, serverdata.serverIP, false)).copyFrom(serverdata);
                this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
            }
            else if (button.id == 81) {
                this.refreshServerList();
            }
            else if (button.id == 89 && entry instanceof AdvancedServerListEntryNormal) {
                final String[] split = ((AdvancedServerListEntryNormal)entry).getServerData().serverIP.split(":");
                final String ip = split[0];
                final String port = (split.length == 2) ? split[1] : "25565";
                final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(String.valueOf(ip) + ":" + port), null);
            }
            else {
                final int id = button.id;
            }
        }
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        if (result && id == 12) {
            final ISaveFormat isaveformat = this.mc.getSaveLoader();
            isaveformat.flushCache();
            isaveformat.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        }
        else if (id == 13) {
            if (result) {
                try {
                    final Class<?> oclass = Class.forName("java.awt.Desktop");
                    final Object object = oclass.getMethod("getDesktop", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
                    oclass.getMethod("browse", URI.class).invoke(object, new URI(this.openGLWarningLink));
                }
                catch (final Throwable throwable) {
                    AdvancedGuiMainMenu.logger.error("Couldn't open link", throwable);
                }
            }
            this.mc.displayGuiScreen(this);
        }
        final GuiListExtended.IGuiListEntry iGuiListEntry;
        final GuiListExtended.IGuiListEntry guilistextended$iguilistentry = iGuiListEntry = ((this.serverListSelector.getSelectedSlotIndex() < 0) ? null : this.serverListSelector.getListEntry(this.serverListSelector.getSelectedSlotIndex()));
        if (this.deletingServer) {
            this.deletingServer = false;
            if (result && guilistextended$iguilistentry instanceof AdvancedServerListEntryNormal) {
                this.savedServerList.removeServerData(this.serverListSelector.getSelectedSlotIndex());
                this.savedServerList.saveServerList();
                this.serverListSelector.setSelectedSlotIndex(-1);
                this.serverListSelector.load(this.savedServerList);
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
                this.serverListSelector.load(this.savedServerList);
            }
            this.mc.displayGuiScreen(this);
        }
        else if (this.editingServer) {
            this.editingServer = false;
            if (result && guilistextended$iguilistentry instanceof AdvancedServerListEntryNormal) {
                final ServerData serverdata = ((AdvancedServerListEntryNormal)guilistextended$iguilistentry).getServerData();
                serverdata.serverName = this.selectedServer.serverName;
                serverdata.serverIP = this.selectedServer.serverIP;
                serverdata.copyFrom(this.selectedServer);
                this.savedServerList.saveServerList();
                this.serverListSelector.load(this.savedServerList);
            }
            this.mc.displayGuiScreen(this);
        }
    }
    
    protected void renderBackground() {
        GlStateManager.color(255.0f, 255.0f, 255.0f, 1.0f);
        final ScaledResolution s = new ScaledResolution(this.mc);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("client/mainmenu/background.png"));
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), (float)ScaledResolution.getScaledWidth(), (float)ScaledResolution.getScaledHeight());
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawForm(final double x, final double y, final double x2, final double y2, final double x3, final double y3, final double x4, final double y4, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glDisable(2884);
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(7);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x3, y3);
        GL11.glVertex2d(x4, y4);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    protected void renderLine() {
        drawForm(0.0, 0.0, AdvancedGuiMainMenu.width / 2.5, 0.0, AdvancedGuiMainMenu.width / 5, AdvancedGuiMainMenu.height, 0.0, AdvancedGuiMainMenu.height, -1375731712);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawDefaultBackground();
        this.renderLine();
        this.hoveringText = null;
        this.serverListSelector.drawScreen(mouseX, mouseY, partialTicks);
        final int color = Color.RED.darker().getRGB();
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.hoveringText != null) {
            this.drawHoveringText((List<String>)Lists.newArrayList((Iterable<?>)Splitter.on("\n").split(this.hoveringText)), mouseX, mouseY);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final Object object = this.threadLock;
        synchronized (object) {
            if (this.openGLWarning1.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v && mouseY >= this.field_92021_u && mouseY <= this.field_92019_w) {
                final GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
                guiconfirmopenlink.disableSecurityWarning();
                this.mc.displayGuiScreen(guiconfirmopenlink);
            }
            monitorexit(object);
        }
        this.serverListSelector.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.serverListSelector.handleMouseInput();
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        this.serverPinger.pingPendingNetworks();
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        if (this.lanServerDetector != null) {
            this.lanServerDetector.interrupt();
            this.lanServerDetector = null;
        }
        this.serverPinger.clearPendingNetworks();
    }
    
    private void refreshServerList() {
        this.mc.displayGuiScreen(new AdvancedGuiMainMenu());
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        final int i = this.serverListSelector.getSelectedSlotIndex();
        final GuiListExtended.IGuiListEntry iGuiListEntry;
        final GuiListExtended.IGuiListEntry guilistextended$iguilistentry = iGuiListEntry = ((i < 0) ? null : this.serverListSelector.getListEntry(i));
        if (keyCode == 63) {
            this.refreshServerList();
        }
        else if (i >= 0) {
            if (keyCode == 200) {
                if (isShiftKeyDown()) {
                    if (i > 0 && guilistextended$iguilistentry instanceof AdvancedServerListEntryNormal) {
                        this.savedServerList.swapServers(i, i - 1);
                        this.selectServer(this.serverListSelector.getSelectedSlotIndex() - 1);
                        this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                        this.serverListSelector.load(this.savedServerList);
                    }
                }
                else if (i > 0) {
                    this.selectServer(this.serverListSelector.getSelectedSlotIndex() - 1);
                    this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                    if (this.serverListSelector.getListEntry(this.serverListSelector.getSelectedSlotIndex()) instanceof ServerListEntryLanScan) {
                        if (this.serverListSelector.getSelectedSlotIndex() > 0) {
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
                        this.serverListSelector.load(this.savedServerList);
                    }
                }
                else if (i < this.serverListSelector.getSize()) {
                    this.selectServer(this.serverListSelector.getSelectedSlotIndex() + 1);
                    this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                    if (this.serverListSelector.getListEntry(this.serverListSelector.getSelectedSlotIndex()) instanceof ServerListEntryLanScan) {
                        if (this.serverListSelector.getSelectedSlotIndex() < this.serverListSelector.getSize() - 1) {
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
    
    public void connectToSelected() {
        final GuiListExtended.IGuiListEntry iGuiListEntry;
        final GuiListExtended.IGuiListEntry guilistextended$iguilistentry = iGuiListEntry = ((this.serverListSelector.getSelectedSlotIndex() < 0) ? null : this.serverListSelector.getListEntry(this.serverListSelector.getSelectedSlotIndex()));
        if (guilistextended$iguilistentry instanceof AdvancedServerListEntryNormal) {
            this.connectToServer(((AdvancedServerListEntryNormal)guilistextended$iguilistentry).getServerData());
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
        this.btnEditServer.enabled = false;
        this.btnDeleteServer.enabled = false;
        this.btnCopyIP.enabled = false;
        if (guilistextended$iguilistentry != null && !(guilistextended$iguilistentry instanceof ServerListEntryLanScan)) {
            this.btnSelectServer.enabled = true;
            if (guilistextended$iguilistentry instanceof AdvancedServerListEntryNormal) {
                this.btnEditServer.enabled = true;
                this.btnDeleteServer.enabled = true;
                this.btnCopyIP.enabled = true;
            }
        }
    }
    
    public ServerPinger getServerPinger() {
        return this.serverPinger;
    }
    
    public void setHoveringText(final String p_146793_1_) {
        this.hoveringText = p_146793_1_;
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.serverListSelector.mouseReleased(mouseX, mouseY, state);
    }
    
    public ServerList getServerList() {
        return this.savedServerList;
    }
    
    public boolean func_175392_a(final int p_175392_2_) {
        return p_175392_2_ > 0;
    }
    
    public boolean func_175394_b(final int p_175394_2_) {
        return p_175394_2_ < this.savedServerList.countServers() - 1;
    }
    
    public void func_175391_a(final int p_175391_2_, final boolean p_175391_3_) {
        final int i = p_175391_3_ ? 0 : (p_175391_2_ - 1);
        this.savedServerList.swapServers(p_175391_2_, i);
        if (this.serverListSelector.getSelectedSlotIndex() == p_175391_2_) {
            this.selectServer(i);
        }
        this.serverListSelector.load(this.savedServerList);
    }
    
    public void func_175393_b(final int p_175393_2_, final boolean p_175393_3_) {
        final int i = p_175393_3_ ? (this.savedServerList.countServers() - 1) : (p_175393_2_ + 1);
        this.savedServerList.swapServers(p_175393_2_, i);
        if (this.serverListSelector.getSelectedSlotIndex() == p_175393_2_) {
            this.selectServer(i);
        }
        this.serverListSelector.load(this.savedServerList);
    }
}
