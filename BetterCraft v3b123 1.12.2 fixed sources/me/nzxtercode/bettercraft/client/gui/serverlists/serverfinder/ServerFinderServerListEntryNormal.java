// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.gui.serverlists.serverfinder;

import net.minecraft.client.gui.GuiScreen;
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
import net.minecraft.client.gui.Gui;
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
import net.minecraft.client.gui.GuiListExtended;

public class ServerFinderServerListEntryNormal implements GuiListExtended.IGuiListEntry
{
    private static final Logger logger;
    private static final ThreadPoolExecutor field_148302_b;
    private static final ResourceLocation UNKNOWN_SERVER;
    private static final ResourceLocation SERVER_SELECTION_BUTTONS;
    private final GuiServerFinderMultiplayer owner;
    private final Minecraft mc;
    private ServerData serverData;
    private ResourceLocation iconLocation;
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
    
    public ServerFinderServerListEntryNormal(final GuiServerFinderMultiplayer owner, final ServerData data) {
        this.owner = owner;
        this.serverData = data;
        this.mc = Minecraft.getMinecraft();
        if (!this.mc.isIntegratedServerRunning()) {
            this.iconLocation = new ResourceLocation("servers/" + data.serverIP + "/icon");
            this.field_148305_h = (DynamicTexture)this.mc.getTextureManager().getTexture(this.iconLocation);
        }
        this.serveradress = ServerAddress.resolveAddress(this.serverData.serverIP);
        this.id = ProtocolVersionUtils.getKnownAs(this.serverData.version);
    }
    
    public void setServerData(final ServerData data) {
        this.serverData = data;
    }
    
    public void ping() {
        this.serverData.field_78841_f = true;
        this.serverData.pingToServer = -2L;
        this.serverData.serverMOTD = "";
        this.serverData.populationInfo = "";
        ServerFinderServerListEntryNormal.field_148302_b.submit(() -> {
            try {
                this.owner.getOldServerPinger().ping(this.serverData);
            }
            catch (final UnknownHostException var2) {
                this.serverData.pingToServer = -1L;
                this.serverData.serverMOTD = EnumChatFormatting.DARK_RED + "Server doesn't exist";
            }
            catch (final Exception var3) {
                this.serverData.pingToServer = -1L;
                this.serverData.serverMOTD = EnumChatFormatting.DARK_RED + "Server is offline";
            }
        });
    }
    
    @Override
    public void drawEntry(final int p_192634_1_, final int p_192634_2_, final int p_192634_3_, final int p_192634_4_, final int p_192634_5_, final int p_192634_6_, final int p_192634_7_, final boolean p_192634_8_) {
        if (!this.serverData.field_78841_f) {
            this.serverData.field_78841_f = true;
            this.serverData.pingToServer = -2L;
            this.serverData.serverMOTD = "";
            this.serverData.populationInfo = "";
            ServerFinderServerListEntryNormal.field_148302_b.submit(() -> {
                try {
                    this.owner.getOldServerPinger().ping(this.serverData);
                }
                catch (final UnknownHostException var2) {
                    this.serverData.pingToServer = -1L;
                    this.serverData.serverMOTD = EnumChatFormatting.DARK_RED + "Server doesn't exist";
                }
                catch (final Exception var3) {
                    this.serverData.pingToServer = -1L;
                    this.serverData.serverMOTD = EnumChatFormatting.DARK_RED + "Server is offline";
                }
                return;
            });
        }
        String s = null;
        try {
            if (this.serverData.pingToServer == -1L) {
                s = "§7IP§8: §cOffline §7Port§8: §cOffline §7Ping§8: §cOffline";
                this.mc.fontRendererObj.drawString(s, p_192634_2_ + p_192634_4_ + 10, p_192634_3_ + 1, -1);
                s = "§7Brand§8: §cOffline §7Version§8: §cOffline §7ID§8: §cOffline §7Online§8: §cOffline";
                this.mc.fontRendererObj.drawString(s, p_192634_2_ + p_192634_4_ + 10, p_192634_3_ + 10, -1);
            }
            else if (this.serverData.pingToServer == -2L) {
                s = "§7IP§8: §aLoading... §7Port§8: §aLoading... §7Ping§8: §aLoading...";
                this.mc.fontRendererObj.drawString(s, p_192634_2_ + p_192634_4_ + 10, p_192634_3_ + 1, -1);
                s = "§7Brand§8: §aLoading... §7Version§8: §aLoading... §7ID§8: §aLoading... §7Online§8: §aLoading...";
                this.mc.fontRendererObj.drawString(s, p_192634_2_ + p_192634_4_ + 10, p_192634_3_ + 10, -1);
            }
            else {
                s = "§7IP§8: §6" + InetAddress.getByName(this.serveradress.getIP()).getHostAddress() + " §7Port§8: §6" + this.serveradress.getPort() + " §7Ping§8: §6" + this.serverData.pingToServer;
                this.mc.fontRendererObj.drawString(s, p_192634_2_ + p_192634_4_ + 10, p_192634_3_ + 1, -1);
                s = "§7Brand§8: §d" + ((this.serverData.gameVersion.split(" ").length > 1) ? (this.serverData.gameVersion.split(" ")[1].startsWith("1.") ? this.serverData.gameVersion.split(" ")[0] : "§dModded") : "§dUnknown") + " §7Version§8: §d" + ((this.serverData.gameVersion.split(" ").length > 1) ? (this.serverData.gameVersion.split(" ")[1].startsWith("1.") ? this.serverData.gameVersion.split(" ")[1] : this.id) : this.id) + " §7ID§8: §d" + this.serverData.version + " §7Online§8: §d" + EnumChatFormatting.getTextWithoutFormattingCodes(this.serverData.populationInfo.split("/")[0]).toString();
                this.mc.fontRendererObj.drawString(s, p_192634_2_ + p_192634_4_ + 10, p_192634_3_ + 10, -1);
            }
        }
        catch (final Exception ex) {}
        final boolean flag = this.serverData.version > 47;
        final boolean flag2 = this.serverData.version < 47;
        final boolean notJoinable = flag || flag2;
        this.mc.fontRendererObj.drawString(this.serverData.serverName, p_192634_2_ + 32 + 3, p_192634_3_ + 1, 16777215);
        final List<String> list = this.mc.fontRendererObj.listFormattedStringToWidth(this.serverData.serverMOTD, p_192634_4_ - 32 - 2);
        for (int i = 0; i < Math.min(list.size(), 2); ++i) {
            this.mc.fontRendererObj.drawString(list.get(i), p_192634_2_ + 32 + 3, p_192634_3_ + 12 + this.mc.fontRendererObj.FONT_HEIGHT * i, 8421504);
        }
        final String s2 = notJoinable ? (EnumChatFormatting.DARK_RED + this.serverData.gameVersion.replace("1.8.x, 1.9.x, 1.10.x, 1.11.x, 1.12.x, 1.13.x, 1.14.x, 1.15.x, 1.16.x, 1.17.x,", "")) : this.serverData.populationInfo;
        final int j = this.mc.fontRendererObj.getStringWidth(s2);
        this.mc.fontRendererObj.drawString(s2, p_192634_2_ + p_192634_4_ - j - 15 - 2, p_192634_3_ + 1, 8421504);
        int k = 0;
        String s3 = null;
        int l;
        String s4;
        if (notJoinable) {
            l = 5;
            s4 = (flag ? "Client out of date!" : "Server out of date!");
            s3 = this.serverData.playerList;
        }
        else if (this.serverData.field_78841_f && this.serverData.pingToServer != -2L) {
            l = ((this.serverData.pingToServer < 0L) ? 5 : ((this.serverData.pingToServer < 150L) ? 0 : ((this.serverData.pingToServer < 300L) ? 1 : ((this.serverData.pingToServer < 600L) ? 2 : ((this.serverData.pingToServer < 1000L) ? 3 : 4)))));
            if (this.serverData.pingToServer < 0L) {
                s4 = "(no connection)";
            }
            else {
                s4 = String.valueOf(this.serverData.pingToServer) + "ms";
                s3 = this.serverData.playerList;
            }
        }
        else {
            k = 1;
            l = (int)(Minecraft.getSystemTime() / 100L + p_192634_1_ * 2 & 0x7L);
            if (l > 4) {
                l = 8 - l;
            }
            s4 = "Pinging...";
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(Gui.icons);
        Gui.drawModalRectWithCustomSizedTexture(p_192634_2_ + p_192634_4_ - 15, p_192634_3_, (float)(k * 10), (float)(176 + l * 8), 10, 8, 256.0f, 256.0f);
        if (this.serverData.getBase64EncodedIconData() != null && !this.serverData.getBase64EncodedIconData().equals(this.field_148299_g)) {
            this.field_148299_g = this.serverData.getBase64EncodedIconData();
            this.prepareServerIcon();
            try {
                this.owner.getServerList().loadServerList();
            }
            catch (final Throwable t) {}
        }
        if (this.field_148305_h != null) {
            this.func_178012_a(p_192634_2_, p_192634_3_, this.iconLocation);
        }
        else {
            this.func_178012_a(p_192634_2_, p_192634_3_, ServerFinderServerListEntryNormal.UNKNOWN_SERVER);
        }
        final int i2 = p_192634_6_ - p_192634_2_;
        final int j2 = p_192634_7_ - p_192634_3_;
        if (i2 >= p_192634_4_ - 15 && i2 <= p_192634_4_ - 5 && j2 >= 0 && j2 <= 8) {
            this.owner.setHoveringText(s4);
        }
        else if (i2 >= p_192634_4_ - j - 15 - 2 && i2 <= p_192634_4_ - 15 - 2 && j2 >= 0 && j2 <= 8) {
            this.owner.setHoveringText(s3);
        }
        if (this.mc.gameSettings.touchscreen || p_192634_8_) {
            this.mc.getTextureManager().bindTexture(ServerFinderServerListEntryNormal.SERVER_SELECTION_BUTTONS);
            Gui.drawRect(p_192634_2_, p_192634_3_, p_192634_2_ + 32, p_192634_3_ + 32, -1601138544);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final int k2 = p_192634_6_ - p_192634_2_;
            final int l2 = p_192634_7_ - p_192634_3_;
            if (this.func_178013_b()) {
                if (k2 < 32 && k2 > 16) {
                    Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 0.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                }
                else {
                    Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 0.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                }
            }
            if (this.owner.func_175392_a(this, p_192634_1_)) {
                if (k2 < 16 && l2 < 16) {
                    Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 96.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                }
                else {
                    Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 96.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                }
            }
            if (this.owner.func_175394_b(this, p_192634_1_)) {
                if (k2 < 16 && l2 > 16) {
                    Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 64.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                }
                else {
                    Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 64.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                }
            }
        }
    }
    
    protected void func_178012_a(final int p_178012_1_, final int p_178012_2_, final ResourceLocation p_178012_3_) {
        this.mc.getTextureManager().bindTexture(p_178012_3_);
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(p_178012_1_, p_178012_2_, 0.0f, 0.0f, 32, 32, 32.0f, 32.0f);
        GlStateManager.disableBlend();
    }
    
    private boolean func_178013_b() {
        return true;
    }
    
    private void prepareServerIcon() {
        if (this.serverData.getBase64EncodedIconData() == null) {
            this.mc.getTextureManager().deleteTexture(this.iconLocation);
            this.field_148305_h = null;
        }
        else {
            final ByteBuf bytebuf = Unpooled.copiedBuffer(this.serverData.getBase64EncodedIconData(), Charsets.UTF_8);
            final ByteBuf bytebuf2 = Base64.decode(bytebuf);
            BufferedImage bufferedimage = null;
            Label_0234: {
                try {
                    bufferedimage = TextureUtil.readBufferedImage(new ByteBufInputStream(bytebuf2));
                    Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                    Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                    break Label_0234;
                }
                catch (final Throwable throwable) {
                    ServerFinderServerListEntryNormal.logger.error("Invalid icon for server " + this.serverData.serverName + " (" + this.serverData.serverIP + ")", throwable);
                    this.serverData.setBase64EncodedIconData(null);
                }
                finally {
                    bytebuf.release();
                    bytebuf2.release();
                }
                return;
            }
            if (this.field_148305_h == null) {
                this.field_148305_h = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
                this.mc.getTextureManager().loadTexture(this.iconLocation, this.field_148305_h);
            }
            bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), this.field_148305_h.getTextureData(), 0, bufferedimage.getWidth());
            this.field_148305_h.updateDynamicTexture();
        }
    }
    
    @Override
    public boolean mousePressed(final int p_192634_1_, final int p_148278_2_, final int p_148278_3_, final int p_148278_4_, final int p_148278_5_, final int p_148278_6_) {
        if (p_148278_5_ <= 32) {
            if (p_148278_5_ < 32 && p_148278_5_ > 16 && this.func_178013_b()) {
                this.owner.selectServer(p_192634_1_);
                this.owner.connectToSelected();
                return true;
            }
            if (p_148278_5_ < 16 && p_148278_6_ < 16 && this.owner.func_175392_a(this, p_192634_1_)) {
                this.owner.func_175391_a(null, p_192634_1_, GuiScreen.isShiftKeyDown());
                return true;
            }
            if (p_148278_5_ < 16 && p_148278_6_ > 16 && this.owner.func_175394_b(this, p_192634_1_)) {
                this.owner.func_175393_b(this, p_192634_1_, GuiScreen.isShiftKeyDown());
                return true;
            }
        }
        this.owner.selectServer(p_192634_1_);
        if (Minecraft.getSystemTime() - this.field_148298_f < 250L) {
            this.owner.connectToSelected();
        }
        this.field_148298_f = Minecraft.getSystemTime();
        return false;
    }
    
    @Override
    public void setSelected(final int p_192633_1_, final int p_192633_2_, final int p_192633_3_) {
    }
    
    @Override
    public void mouseReleased(final int p_192634_1_, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
    }
    
    public ServerData getServerData() {
        return this.serverData;
    }
}
