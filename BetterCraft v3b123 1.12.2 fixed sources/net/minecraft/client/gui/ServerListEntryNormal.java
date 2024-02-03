// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import java.awt.image.BufferedImage;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.texture.ITextureObject;
import org.apache.commons.lang3.Validate;
import java.io.InputStream;
import net.minecraft.client.renderer.texture.TextureUtil;
import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.codec.base64.Base64;
import io.netty.buffer.Unpooled;
import com.google.common.base.Charsets;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import java.net.InetAddress;
import java.net.UnknownHostException;
import net.minecraft.util.EnumChatFormatting;
import me.nzxtercode.bettercraft.client.utils.ProtocolVersionUtils;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.logging.log4j.Logger;

public class ServerListEntryNormal implements GuiListExtended.IGuiListEntry
{
    private static final Logger logger;
    private static final ThreadPoolExecutor field_148302_b;
    private static final ResourceLocation UNKNOWN_SERVER;
    private static final ResourceLocation SERVER_SELECTION_BUTTONS;
    private final GuiMultiplayer owner;
    private final Minecraft mc;
    private ServerData server;
    private ResourceLocation serverIcon;
    private String field_148299_g;
    private DynamicTexture field_148305_h;
    private long field_148298_f;
    private final ServerAddress serveradress;
    private final String id;
    
    static {
        logger = LogManager.getLogger();
        field_148302_b = new ScheduledThreadPoolExecutor(5, new ThreadFactoryBuilder().setNameFormat("Server Pinger #%d").setDaemon(true).build());
        UNKNOWN_SERVER = new ResourceLocation("textures/misc/unknown_server.png");
        SERVER_SELECTION_BUTTONS = new ResourceLocation("textures/gui/server_selection.png");
    }
    
    public ServerListEntryNormal(final GuiMultiplayer owner, final ServerData serverIn) {
        this.owner = owner;
        this.server = serverIn;
        this.mc = Minecraft.getMinecraft();
        if (!this.mc.isIntegratedServerRunning()) {
            this.serverIcon = new ResourceLocation("servers/" + this.server.serverIP + "/icon");
            this.field_148305_h = (DynamicTexture)this.mc.getTextureManager().getTexture(this.serverIcon);
        }
        this.serveradress = ServerAddress.resolveAddress(this.server.serverIP);
        this.id = ProtocolVersionUtils.getKnownAs(this.server.version);
    }
    
    @Override
    public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected) {
        if (!this.server.field_78841_f) {
            this.server.field_78841_f = true;
            this.server.pingToServer = -2L;
            this.server.serverMOTD = "";
            this.server.populationInfo = "";
            ServerListEntryNormal.field_148302_b.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        ServerListEntryNormal.this.owner.getOldServerPinger().ping(ServerListEntryNormal.this.server);
                    }
                    catch (final UnknownHostException var2) {
                        ServerListEntryNormal.this.server.pingToServer = -1L;
                        ServerListEntryNormal.this.server.serverMOTD = EnumChatFormatting.DARK_RED + "Can't resolve hostname";
                    }
                    catch (final Exception var3) {
                        ServerListEntryNormal.this.server.pingToServer = -1L;
                        ServerListEntryNormal.this.server.serverMOTD = EnumChatFormatting.DARK_RED + "Can't connect to server.";
                    }
                }
            });
        }
        String s = null;
        try {
            if (this.server.pingToServer == -1L) {
                s = "§7IP§8: §cOffline §7Port§8: §cOffline §7Ping§8: §cOffline";
                this.mc.fontRendererObj.drawString(s, x + listWidth + 10, y + 1, -1);
                s = "§7Brand§8: §cOffline §7Version§8: §cOffline §7ID§8: §cOffline §7Online§8: §cOffline";
                this.mc.fontRendererObj.drawString(s, x + listWidth + 10, y + 10, -1);
            }
            else if (this.server.pingToServer == -2L) {
                s = "§7IP§8: §aLoading... §7Port§8: §aLoading... §7Ping§8: §aLoading...";
                this.mc.fontRendererObj.drawString(s, x + listWidth + 10, y + 1, -1);
                s = "§7Brand§8: §aLoading... §7Version§8: §aLoading... §7ID§8: §aLoading... §7Online§8: §aLoading...";
                this.mc.fontRendererObj.drawString(s, x + listWidth + 10, y + 10, -1);
            }
            else {
                s = "§7IP§8: §6" + InetAddress.getByName(this.serveradress.getIP()).getHostAddress() + " §7Port§8: §6" + this.serveradress.getPort() + " §7Ping§8: §6" + this.server.pingToServer;
                this.mc.fontRendererObj.drawString(s, x + listWidth + 10, y + 1, -1);
                s = "§7Brand§8: §d" + ((this.server.gameVersion.split(" ").length > 1) ? (this.server.gameVersion.split(" ")[1].startsWith("1.") ? this.server.gameVersion.split(" ")[0] : "§dModded") : "§dUnknown") + " §7Version§8: §d" + ((this.server.gameVersion.split(" ").length > 1) ? (this.server.gameVersion.split(" ")[1].startsWith("1.") ? this.server.gameVersion.split(" ")[1] : this.id) : this.id) + " §7ID§8: §d" + this.server.version + " §7Online§8: §d" + EnumChatFormatting.getTextWithoutFormattingCodes(this.server.populationInfo.split("/")[0]).toString();
                this.mc.fontRendererObj.drawString(s, x + listWidth + 10, y + 10, -1);
            }
        }
        catch (final Exception ex) {}
        final boolean flag = this.server.version > 47;
        final boolean flag2 = this.server.version < 47;
        final boolean flag3 = flag || flag2;
        this.mc.fontRendererObj.drawString(this.server.serverName, x + 32 + 3, y + 1, 16777215);
        final List<String> list = this.mc.fontRendererObj.listFormattedStringToWidth(this.server.serverMOTD, listWidth - 32 - 2);
        for (int i = 0; i < Math.min(list.size(), 2); ++i) {
            this.mc.fontRendererObj.drawString(list.get(i), x + 32 + 3, y + 12 + this.mc.fontRendererObj.FONT_HEIGHT * i, 8421504);
        }
        final String s2 = flag3 ? (EnumChatFormatting.DARK_RED + this.server.gameVersion.replace("1.8.x, 1.9.x, 1.10.x, 1.11.x, 1.12.x, 1.13.x, 1.14.x, 1.15.x, 1.16.x, 1.17.x,", "")) : this.server.populationInfo;
        final int j = this.mc.fontRendererObj.getStringWidth(s2);
        this.mc.fontRendererObj.drawString(s2, x + listWidth - j - 15 - 2, y + 1, 8421504);
        int k = 0;
        int l;
        String s3;
        if (flag3) {
            l = 5;
            s3 = (flag ? "Client out of date!" : "Server out of date!");
            s = this.server.playerList;
        }
        else if (this.server.field_78841_f && this.server.pingToServer != -2L) {
            if (this.server.pingToServer < 0L) {
                l = 5;
            }
            else if (this.server.pingToServer < 150L) {
                l = 0;
            }
            else if (this.server.pingToServer < 300L) {
                l = 1;
            }
            else if (this.server.pingToServer < 600L) {
                l = 2;
            }
            else if (this.server.pingToServer < 1000L) {
                l = 3;
            }
            else {
                l = 4;
            }
            if (this.server.pingToServer < 0L) {
                s3 = "(no connection)";
            }
            else {
                s3 = String.valueOf(this.server.pingToServer) + "ms";
                s = this.server.playerList;
            }
        }
        else {
            k = 1;
            l = (int)(Minecraft.getSystemTime() / 100L + slotIndex * 2 & 0x7L);
            if (l > 4) {
                l = 8 - l;
            }
            s3 = "Pinging...";
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(Gui.icons);
        Gui.drawModalRectWithCustomSizedTexture(x + listWidth - 15, y, (float)(k * 10), (float)(176 + l * 8), 10, 8, 256.0f, 256.0f);
        if (this.server.getBase64EncodedIconData() != null && !this.server.getBase64EncodedIconData().equals(this.field_148299_g)) {
            this.field_148299_g = this.server.getBase64EncodedIconData();
            this.prepareServerIcon();
            this.owner.getServerList().saveServerList();
        }
        if (this.field_148305_h != null) {
            this.drawTextureAt(x, y, this.serverIcon);
        }
        else {
            this.drawTextureAt(x, y, ServerListEntryNormal.UNKNOWN_SERVER);
        }
        final int i2 = mouseX - x;
        final int j2 = mouseY - y;
        if (i2 >= listWidth - 15 && i2 <= listWidth - 5 && j2 >= 0 && j2 <= 8) {
            this.owner.setHoveringText(s3);
        }
        else if (i2 >= listWidth - j - 15 - 2 && i2 <= listWidth - 15 - 2 && j2 >= 0 && j2 <= 8) {
            this.owner.setHoveringText(s);
        }
        if (this.mc.gameSettings.touchscreen || isSelected) {
            this.mc.getTextureManager().bindTexture(ServerListEntryNormal.SERVER_SELECTION_BUTTONS);
            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final int k2 = mouseX - x;
            final int l2 = mouseY - y;
            if (this.func_178013_b()) {
                if (k2 < 32 && k2 > 16) {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                }
                else {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                }
            }
            if (this.owner.func_175392_a(this, slotIndex)) {
                if (k2 < 16 && l2 < 16) {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                }
                else {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                }
            }
            if (this.owner.func_175394_b(this, slotIndex)) {
                if (k2 < 16 && l2 > 16) {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                }
                else {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                }
            }
        }
    }
    
    protected void drawTextureAt(final int p_178012_1_, final int p_178012_2_, final ResourceLocation p_178012_3_) {
        this.mc.getTextureManager().bindTexture(p_178012_3_);
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(p_178012_1_, p_178012_2_, 0.0f, 0.0f, 32, 32, 32.0f, 32.0f);
        GlStateManager.disableBlend();
    }
    
    private boolean func_178013_b() {
        return true;
    }
    
    private void prepareServerIcon() {
        if (this.server.getBase64EncodedIconData() == null) {
            this.mc.getTextureManager().deleteTexture(this.serverIcon);
            this.field_148305_h = null;
        }
        else {
            final ByteBuf bytebuf = Unpooled.copiedBuffer(this.server.getBase64EncodedIconData(), Charsets.UTF_8);
            final ByteBuf bytebuf2 = Base64.decode(bytebuf);
            BufferedImage bufferedimage = null;
            Label_0218: {
                try {
                    bufferedimage = TextureUtil.readBufferedImage(new ByteBufInputStream(bytebuf2));
                    Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                    Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                    break Label_0218;
                }
                catch (final Throwable throwable) {
                    ServerListEntryNormal.logger.error("Invalid icon for server " + this.server.serverName + " (" + this.server.serverIP + ")", throwable);
                    this.server.setBase64EncodedIconData(null);
                }
                finally {
                    bytebuf.release();
                    bytebuf2.release();
                }
                return;
            }
            if (this.field_148305_h == null) {
                this.field_148305_h = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
                this.mc.getTextureManager().loadTexture(this.serverIcon, this.field_148305_h);
            }
            bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), this.field_148305_h.getTextureData(), 0, bufferedimage.getWidth());
            this.field_148305_h.updateDynamicTexture();
        }
    }
    
    @Override
    public boolean mousePressed(final int slotIndex, final int p_148278_2_, final int p_148278_3_, final int p_148278_4_, final int p_148278_5_, final int p_148278_6_) {
        if (p_148278_5_ <= 32) {
            if (p_148278_5_ < 32 && p_148278_5_ > 16 && this.func_178013_b()) {
                this.owner.selectServer(slotIndex);
                this.owner.connectToSelected();
                return true;
            }
            if (p_148278_5_ < 16 && p_148278_6_ < 16 && this.owner.func_175392_a(this, slotIndex)) {
                this.owner.func_175391_a(this, slotIndex, GuiScreen.isShiftKeyDown());
                return true;
            }
            if (p_148278_5_ < 16 && p_148278_6_ > 16 && this.owner.func_175394_b(this, slotIndex)) {
                this.owner.func_175393_b(this, slotIndex, GuiScreen.isShiftKeyDown());
                return true;
            }
        }
        this.owner.selectServer(slotIndex);
        if (Minecraft.getSystemTime() - this.field_148298_f < 250L) {
            this.owner.connectToSelected();
        }
        this.field_148298_f = Minecraft.getSystemTime();
        return false;
    }
    
    @Override
    public void setSelected(final int p_178011_1_, final int p_178011_2_, final int p_178011_3_) {
    }
    
    @Override
    public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
    }
    
    public ServerData getServerData() {
        return this.server;
    }
}
