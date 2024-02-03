// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils.manager;

import java.util.Map;
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
import com.google.gson.JsonObject;
import java.util.List;
import com.google.common.base.Objects;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.GuiScreen;
import com.google.gson.JsonElement;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.util.EnumChatFormatting;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.gson.JsonParser;
import java.net.InetAddress;
import net.labymod.main.LabyMod;
import net.labymod.core.LabyModCore;
import net.labymod.main.Source;
import java.util.concurrent.CompletableFuture;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URL;
import me.nzxtercode.bettercraft.client.utils.ProtocolVersionUtils;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.client.multiplayer.ServerAddress;
import net.labymod.utils.ServerData;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.labymod.core.ServerPingerData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ServerInfoRenderer
{
    private static final ResourceLocation UNKNOWN_SERVER;
    private static final ResourceLocation SERVER_SELECTION_BUTTONS;
    private final Minecraft mc;
    private ServerPingerData serverData;
    private ResourceLocation serverIcon;
    private String base64;
    private DynamicTexture dynamicTexture;
    private boolean canReachServer;
    private boolean hidden;
    private ServerData labymodServerData;
    private int index;
    private final ServerAddress serveradress;
    private final String id;
    private final AtomicReference<String> geoData;
    
    static {
        UNKNOWN_SERVER = new ResourceLocation("textures/misc/unknown_server.png");
        SERVER_SELECTION_BUTTONS = new ResourceLocation("textures/gui/server_selection.png");
    }
    
    public ServerInfoRenderer(final String rawIp, final ServerPingerData serverData) {
        this(rawIp, rawIp, serverData);
    }
    
    public ServerInfoRenderer(final String serverName, final String rawIp, final ServerPingerData serverData) {
        this.geoData = new AtomicReference<String>();
        this.canReachServer = false;
        this.hidden = false;
        this.index = 0;
        this.mc = Minecraft.getMinecraft();
        this.init(serverName, rawIp, serverData);
        this.serveradress = ServerAddress.resolveAddress(this.serverData.getIpAddress());
        this.id = ProtocolVersionUtils.getKnownAs(this.serverData.version);
        CompletableFuture.runAsync(() -> {
            try {
                new URL("http://ip-api.com/json/" + this.serveradress.getIP() + "?fields=country,regionName,city,as,reverse,query");
                final URL url;
                final URL servergeodata = url;
                new BufferedReader(new InputStreamReader(servergeodata.openStream()));
                final BufferedReader bufferedReader2;
                final BufferedReader bufferedReader = bufferedReader2;
                this.geoData.set(bufferedReader.readLine());
            }
            catch (final Exception ex) {}
        });
    }
    
    public void init(final String serverName, final String rawIp, ServerPingerData serverData) {
        if (serverData == null) {
            serverData = new ServerPingerData((rawIp == null || rawIp.isEmpty()) ? "localhost" : rawIp, 0L);
            serverData.setPingToServer(-1L);
            serverData.setMotd("§4Can't connect to server.");
            serverData.setVersion(Source.ABOUT_MC_PROTOCOL_VERSION);
            this.canReachServer = false;
        }
        else {
            this.serverIcon = new ResourceLocation("servers/" + serverData.getIpAddress() + "/icon");
            this.dynamicTexture = (DynamicTexture)this.mc.getTextureManager().getTexture(this.serverIcon);
            this.canReachServer = true;
        }
        (this.serverData = serverData).setServerName(serverName);
    }
    
    public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected) {
        final boolean flag = this.isClientOutOfDate();
        final boolean flag2 = this.isServerOutOfDate();
        final boolean flag3 = flag || flag2;
        LabyModCore.getMinecraft().getFontRenderer().drawString(this.serverData.getServerName(), x + 32 + 3, y + 1, 16777215);
        final List<String> list = LabyMod.getInstance().getDrawUtils().listFormattedStringToWidth(this.serverData.getMotd(), listWidth - 32 - 2);
        for (int i = 0; i < Math.min(list.size(), 2); ++i) {
            LabyModCore.getMinecraft().getFontRenderer().drawString(list.get(i), x + 32 + 3, y + 12 + LabyModCore.getMinecraft().getFontRenderer().FONT_HEIGHT * i, 8421504);
        }
        String populationInfo = "§7" + this.serverData.getCurrentPlayers() + "§8/§7" + this.serverData.getMaxPlayers();
        if (!this.canReachServer) {
            populationInfo = "§7???";
        }
        final String s2 = flag3 ? ("§4" + this.serverData.getGameVersion()) : populationInfo;
        final int j = LabyModCore.getMinecraft().getFontRenderer().getStringWidth(s2);
        LabyModCore.getMinecraft().getFontRenderer().drawString(s2, x + listWidth - j - 15 - 2, y + 1, 8421504);
        int k = 0;
        String s3 = null;
        try {
            if (this.serverData.pingToServer == -1L) {
                s3 = "§7IP§8: §cOffline §7Port§8: §cOffline §7Ping§8: §cOffline";
                this.mc.fontRendererObj.drawString(s3, x + listWidth + 10, y + 1, -1);
                s3 = "§7Brand§8: §cOffline §7Version§8: §cOffline §7ID§8: §cOffline §7Online§8: §cOffline";
                this.mc.fontRendererObj.drawString(s3, x + listWidth + 10, y + 10, -1);
            }
            else if (this.serverData.pingToServer == -2L) {
                s3 = "§7IP§8: §aLoading... §7Port§8: §aLoading... §7Ping§8: §aLoading...";
                this.mc.fontRendererObj.drawString(s3, x + listWidth + 10, y + 1, -1);
                s3 = "§7Brand§8: §aLoading... §7Version§8: §aLoading... §7ID§8: §aLoading... §7Online§8: §aLoading...";
                this.mc.fontRendererObj.drawString(s3, x + listWidth + 10, y + 10, -1);
            }
            else {
                s3 = "§7IP§8: §6" + InetAddress.getByName(this.serveradress.getIP()).getHostAddress() + " §7Port§8: §6" + this.serveradress.getPort() + " §7Ping§8: §6" + this.serverData.pingToServer;
                this.mc.fontRendererObj.drawString(s3, x + listWidth + 10, y + 1, -1);
                s3 = "§7Brand§8: §d" + ((this.serverData.gameVersion.split(" ").length > 1) ? (this.serverData.gameVersion.split(" ")[1].startsWith("1.") ? this.serverData.gameVersion.split(" ")[0] : "§dModded") : "§dUnknown") + " §7Version§8: §d" + ((this.serverData.gameVersion.split(" ").length > 1) ? (this.serverData.gameVersion.split(" ")[1].startsWith("1.") ? this.serverData.gameVersion.split(" ")[1] : this.id) : this.id) + " §7ID§8: §d" + this.serverData.version + " §7Online§8: §d" + this.serverData.getCurrentPlayers();
                this.mc.fontRendererObj.drawString(s3, x + listWidth + 10, y + 10, -1);
                final JsonObject json = new JsonParser().parse(this.geoData.get()).getAsJsonObject();
                final AtomicInteger height = new AtomicInteger();
                json.entrySet().forEach(entry -> this.mc.fontRendererObj.drawString(String.format("%s%s: %s%s", EnumChatFormatting.GRAY, StringUtils.capitalize(entry.getKey()), EnumChatFormatting.RED, entry.getValue().getAsString()), n + n2 + 10, GuiScreen.height / 4 + 85 + atomicInteger.getAndAdd(this.mc.fontRendererObj.FONT_HEIGHT) - (jsonObject.entrySet().size() * this.mc.fontRendererObj.FONT_HEIGHT / 2 + this.mc.fontRendererObj.FONT_HEIGHT / 2), -1));
            }
        }
        catch (final Exception ex) {}
        int l;
        String s4;
        if (flag3) {
            l = 5;
            s4 = (flag ? "Client out of date!" : "Server out of date!");
            s3 = this.serverData.getPlayerList();
        }
        else if (this.serverData.getPingToServer() != -2L) {
            if (this.serverData.getPingToServer() < 0L) {
                l = 5;
            }
            else if (this.serverData.getPingToServer() < 150L) {
                l = 0;
            }
            else if (this.serverData.getPingToServer() < 300L) {
                l = 1;
            }
            else if (this.serverData.getPingToServer() < 600L) {
                l = 2;
            }
            else if (this.serverData.getPingToServer() < 1000L) {
                l = 3;
            }
            else {
                l = 4;
            }
            if (this.serverData.getPingToServer() < 0L) {
                s4 = "(no connection)";
            }
            else {
                s4 = String.valueOf(this.serverData.getPingToServer()) + "ms";
                s3 = this.serverData.getPlayerList();
            }
        }
        else {
            k = 1;
            l = (int)(Minecraft.getSystemTime() / 100L + 2L & 0x7L);
            if (l > 4) {
                l = 8 - l;
            }
            s4 = "Pinging...";
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(LabyModCore.getRenderImplementation().getIcons());
        Gui.drawModalRectWithCustomSizedTexture(x + listWidth - 15, y, (float)(k * 10), (float)(176 + l * 8), 10, 8, 256.0f, 256.0f);
        if (this.serverData != null && this.serverData.getBase64EncodedIconData() != null && !Objects.equal(this.serverData.getBase64EncodedIconData(), this.base64)) {
            this.base64 = this.serverData.getBase64EncodedIconData();
            this.prepareServerIcon();
        }
        if (this.dynamicTexture != null) {
            this.drawServerIcon(x, y, this.serverIcon);
        }
        else {
            this.drawServerIcon(x, y, ServerInfoRenderer.UNKNOWN_SERVER);
        }
        final int i2 = mouseX - x;
        final int j2 = mouseY - y;
        if (s3 != null) {
            if (i2 >= listWidth - 15 && i2 <= listWidth - 5 && j2 >= 0 && j2 <= 8 && !s4.isEmpty()) {
                TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, s4.split("\n"));
            }
            else if (i2 >= listWidth - j - 15 - 2 && i2 <= listWidth - 15 - 2 && j2 >= 0 && j2 <= 8 && !s3.isEmpty()) {
                TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, s3.split("\n"));
            }
        }
    }
    
    public boolean isClientOutOfDate() {
        return this.serverData.getVersion() > Source.ABOUT_MC_PROTOCOL_VERSION;
    }
    
    public boolean isServerOutOfDate() {
        return this.serverData.getVersion() < Source.ABOUT_MC_PROTOCOL_VERSION;
    }
    
    public boolean drawJoinServerButton(final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY) {
        if (mouseX > x && mouseX < x + listWidth && mouseY > y && mouseY < y + slotHeight) {
            this.mc.getTextureManager().bindTexture(ServerInfoRenderer.SERVER_SELECTION_BUTTONS);
            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final int k1 = mouseX - x;
            if (k1 < 32 && k1 > 16) {
                Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                return true;
            }
            Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, 32, 32, 256.0f, 256.0f);
        }
        return false;
    }
    
    public boolean drawSaveServerButton(final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY) {
        if (mouseX > x && mouseX < x + listWidth && mouseY > y && mouseY < y + slotHeight) {
            this.mc.getTextureManager().bindTexture(ServerInfoRenderer.SERVER_SELECTION_BUTTONS);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final int k1 = mouseX - x;
            final int l1 = mouseY - y;
            if (k1 < 16 && l1 > 16) {
                Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                return true;
            }
            Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0f, 0.0f, 32, 32, 256.0f, 256.0f);
        }
        return false;
    }
    
    protected void drawServerIcon(final int posX, final int posY, final ResourceLocation resourceLocation) {
        this.mc.getTextureManager().bindTexture(resourceLocation);
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(posX, posY, 0.0f, 0.0f, 32, 32, 32.0f, 32.0f);
        GlStateManager.disableBlend();
    }
    
    private void prepareServerIcon() {
        if (this.serverData.getBase64EncodedIconData() == null) {
            this.mc.getTextureManager().deleteTexture(this.serverIcon);
            this.dynamicTexture = null;
        }
        else {
            final ByteBuf bytebuf = Unpooled.copiedBuffer(this.serverData.getBase64EncodedIconData(), Charsets.UTF_8);
            final ByteBuf bytebuf2 = Base64.decode(bytebuf);
            BufferedImage bufferedimage = null;
            Label_0165: {
                try {
                    bufferedimage = TextureUtil.readBufferedImage(new ByteBufInputStream(bytebuf2));
                    Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                    Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                    break Label_0165;
                }
                catch (final Throwable throwable) {
                    this.serverData.setBase64EncodedIconData(null);
                }
                finally {
                    bytebuf.release();
                    bytebuf2.release();
                }
                return;
            }
            if (this.dynamicTexture == null) {
                this.dynamicTexture = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
                this.mc.getTextureManager().loadTexture(this.serverIcon, this.dynamicTexture);
            }
            bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), this.dynamicTexture.getTextureData(), 0, bufferedimage.getWidth());
            this.dynamicTexture.updateDynamicTexture();
        }
    }
    
    public boolean canReachServer() {
        return this.canReachServer && this.serverData != null && !this.serverData.isPinging();
    }
    
    public ServerPingerData getServerData() {
        return this.serverData;
    }
    
    public ResourceLocation getServerIcon() {
        return this.serverIcon;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }
    
    public ServerData getLabymodServerData() {
        return this.labymodServerData;
    }
    
    public void setLabymodServerData(final ServerData labymodServerData) {
        this.labymodServerData = labymodServerData;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public ServerInfoRenderer setIndex(final int index) {
        this.index = index;
        return this;
    }
}
