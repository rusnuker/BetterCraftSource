// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.multiplayer;

import java.awt.Color;
import me.amkgre.bettercraft.client.utils.RenderUtils;
import me.amkgre.bettercraft.client.utils.ColorUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import java.io.IOException;
import java.net.UnknownHostException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.client.network.NetHandlerLoginClient;
import java.net.InetAddress;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import net.minecraft.network.NetworkManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.gui.GuiScreen;

public class GuiConnecting extends GuiScreen
{
    private static final AtomicInteger CONNECTION_ID;
    private static final Logger LOGGER;
    private NetworkManager networkManager;
    private boolean cancel;
    private final GuiScreen previousGuiScreen;
    public static boolean ServerIP;
    public static boolean SRV;
    public static boolean Netty;
    public static boolean Resolving;
    public static boolean Connecting;
    public static boolean sendingloginpackets;
    public static boolean waitingforresponse;
    public static boolean verifyingsession;
    public static boolean encrypting;
    public static boolean sucess;
    public static String kickedMessage;
    public static boolean kicked;
    
    static {
        CONNECTION_ID = new AtomicInteger(0);
        LOGGER = LogManager.getLogger();
        GuiConnecting.ServerIP = true;
        GuiConnecting.SRV = true;
        GuiConnecting.Netty = true;
        GuiConnecting.Resolving = true;
        GuiConnecting.Connecting = true;
        GuiConnecting.kickedMessage = "";
        GuiConnecting.kicked = false;
    }
    
    public GuiConnecting(final GuiScreen parent, final Minecraft mcIn, final ServerData serverDataIn) {
        GuiDisconnected.lastlogin = serverDataIn;
        this.mc = mcIn;
        this.previousGuiScreen = parent;
        final ServerAddress serveraddress = ServerAddress.fromString(serverDataIn.serverIP);
        mcIn.loadWorld(null);
        mcIn.setServerData(serverDataIn);
        this.connect(serveraddress.getIP(), serveraddress.getPort());
    }
    
    public GuiConnecting(final GuiScreen parent, final Minecraft mcIn, final String hostName, final int port) {
        this.mc = mcIn;
        this.previousGuiScreen = parent;
        mcIn.loadWorld(null);
        this.connect(hostName, port);
    }
    
    private void connect(final String ip, final int port) {
        GuiConnecting.LOGGER.info("Connecting to {}, {}", ip, port);
        new Thread("Server Connector #" + GuiConnecting.CONNECTION_ID.incrementAndGet()) {
            @Override
            public void run() {
                InetAddress inetaddress = null;
                try {
                    if (GuiConnecting.this.cancel) {
                        return;
                    }
                    GuiConnecting.ServerIP = true;
                    inetaddress = InetAddress.getByName(ip);
                    GuiConnecting.SRV = true;
                    GuiConnecting.access$2(GuiConnecting.this, NetworkManager.createNetworkManagerAndConnect(inetaddress, port, GuiConnecting.this.mc.gameSettings.isUsingNativeTransport()));
                    GuiConnecting.Resolving = true;
                    GuiConnecting.this.networkManager.setNetHandler(new NetHandlerLoginClient(GuiConnecting.this.networkManager, GuiConnecting.this.mc, GuiConnecting.this.previousGuiScreen));
                    GuiConnecting.Netty = true;
                    GuiConnecting.this.networkManager.sendPacket(new C00Handshake(ip, port, EnumConnectionState.LOGIN));
                    GuiConnecting.Connecting = true;
                    GuiConnecting.this.networkManager.sendPacket(new CPacketLoginStart(Minecraft.getSession().getProfile()));
                    GuiConnecting.sendingloginpackets = true;
                }
                catch (final UnknownHostException unknownhostexception) {
                    if (GuiConnecting.this.cancel) {
                        return;
                    }
                    GuiConnecting.LOGGER.error("Couldn't connect to server", unknownhostexception);
                    GuiConnecting.this.mc.displayGuiScreen(new GuiDisconnected(GuiConnecting.this.previousGuiScreen, "connect.failed", new TextComponentTranslation("disconnect.genericReason", new Object[] { "Unknown host" })));
                }
                catch (final Exception exception) {
                    if (GuiConnecting.this.cancel) {
                        return;
                    }
                    GuiConnecting.LOGGER.error("Couldn't connect to server", exception);
                    String s = exception.toString();
                    if (inetaddress != null) {
                        final String s2 = inetaddress + ":" + port;
                        s = s.replaceAll(s2, "");
                    }
                    GuiConnecting.this.mc.displayGuiScreen(new GuiDisconnected(GuiConnecting.this.previousGuiScreen, "connect.failed", new TextComponentTranslation("disconnect.genericReason", new Object[] { s })));
                }
            }
        }.start();
    }
    
    @Override
    public void onGuiClosed() {
        GuiConnecting.ServerIP = true;
        GuiConnecting.SRV = true;
        GuiConnecting.Netty = true;
        GuiConnecting.Resolving = true;
        GuiConnecting.Connecting = true;
        GuiConnecting.sendingloginpackets = false;
        GuiConnecting.waitingforresponse = false;
        GuiConnecting.encrypting = false;
        GuiConnecting.sucess = false;
        GuiConnecting.kicked = false;
        GuiConnecting.kickedMessage = "";
    }
    
    @Override
    public void updateScreen() {
        if (this.networkManager != null) {
            if (this.networkManager.isChannelOpen()) {
                GuiConnecting.waitingforresponse = true;
                this.networkManager.processReceivedPackets();
            }
            else {
                this.networkManager.checkDisconnected();
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiConnecting.width / 2 - 100, GuiConnecting.height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.cancel = true;
            if (this.networkManager != null) {
                this.networkManager.closeChannel(new TextComponentString("Aborted"));
            }
            this.mc.displayGuiScreen(this.previousGuiScreen);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        this.drawDefaultBackground();
        final int var4 = GuiConnecting.height / 4 + 120 + 12;
        Gui.drawRect(GuiConnecting.width / 2 - 100, var4 - 130, GuiConnecting.width / 2 + 100, var4 - 20, -16777216);
        RenderUtils.drawBorderedRect(GuiConnecting.width / 2 - 100, var4 - 130, GuiConnecting.width / 2 + 100, var4 - 20, ColorUtils.rainbowEffect(0L, 1.0f).getRGB(), -16777216);
        String s2 = null;
        switch ((int)(Minecraft.getSystemTime() / 300L % 4L)) {
            default: {
                s2 = "§7_";
                break;
            }
            case 1:
            case 3: {
                s2 = "";
                break;
            }
            case 2: {
                s2 = "§7_";
                break;
            }
        }
        int yPos = var4 - 86;
        Gui.drawString(this.fontRendererObj, "§c" + GuiConnecting.kickedMessage, 5, 5, Color.RED.darker().getRGB());
        if (GuiConnecting.ServerIP) {
            Gui.drawString(this.fontRendererObj, "Conneting to " + Minecraft.getMinecraft().getCurrentServerData().serverIP + "...", GuiConnecting.width / 2 - 95 - 1, var4 - 126, Color.WHITE.getRGB());
            if (GuiConnecting.SRV) {
                Gui.drawString(this.fontRendererObj, "Resolving SRV...", GuiConnecting.width / 2 - 95 - 1, var4 - 116, Color.RED.darker().getRGB());
                if (GuiConnecting.Netty) {
                    Gui.drawString(this.fontRendererObj, "Starting Netty Connection...", GuiConnecting.width / 2 - 95 - 1, var4 - 106, Color.RED.darker().getRGB());
                    if (GuiConnecting.Resolving) {
                        Gui.drawString(this.fontRendererObj, "Resolving IP...", GuiConnecting.width / 2 - 95 - 1, var4 - 96, Color.RED.darker().getRGB());
                        if (GuiConnecting.Connecting) {
                            Gui.drawString(this.fontRendererObj, "Connecting...", GuiConnecting.width / 2 - 95 - 1, var4 - 86, Color.YELLOW.darker().getRGB());
                            if (GuiConnecting.sendingloginpackets) {
                                Gui.drawString(this.fontRendererObj, "Sending Login Packets...", GuiConnecting.width / 2 - 95 - 1, var4 - 76, Color.YELLOW.darker().getRGB());
                                yPos = var4 - 76;
                                if (GuiConnecting.waitingforresponse) {
                                    Gui.drawString(this.fontRendererObj, "Waiting for response...", GuiConnecting.width / 2 - 95 - 1, var4 - 66, Color.YELLOW.darker().getRGB());
                                    yPos = var4 - 66;
                                    if (GuiConnecting.verifyingsession) {
                                        Gui.drawString(this.fontRendererObj, "Verifying Session...", GuiConnecting.width / 2 - 95 - 1, var4 - 56, Color.GREEN.darker().getRGB());
                                        Gui.drawString(this.fontRendererObj, "Encrypting...", GuiConnecting.width / 2 - 95 - 1, var4 - 46, Color.GREEN.darker().getRGB());
                                        yPos = var4 - 46;
                                        if (GuiConnecting.sucess) {
                                            Gui.drawString(this.fontRendererObj, "Success!", GuiConnecting.width / 2 - 95 - 1, var4 - 36, Color.GREEN.darker().getRGB());
                                            yPos = var4 - 36;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Gui.drawString(this.fontRendererObj, s2, GuiConnecting.width / 2 - 95 - 1, yPos + 6, Color.GREEN.darker().getRGB());
                super.drawScreen(mouseX, mouseY, partialTicks);
            }
        }
    }
    
    static /* synthetic */ void access$2(final GuiConnecting guiConnecting, final NetworkManager networkManager) {
        guiConnecting.networkManager = networkManager;
    }
}
