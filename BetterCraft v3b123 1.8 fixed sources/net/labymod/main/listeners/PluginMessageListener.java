// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.main.listeners;

import io.netty.buffer.ByteBuf;
import com.google.common.base.Charsets;
import io.netty.handler.codec.DecoderException;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.labymod.gui.GuiShaderSelection;
import net.minecraft.util.ResourceLocation;
import net.labymod.core.asm.LabyModCoreMod;
import net.labymod.api.LabyModAddon;
import net.labymod.addon.AddonLoader;
import net.labymod.api.protocol.liquid.FixedLiquidBucketProtocol;
import net.labymod.api.permissions.Permissions;
import net.labymod.main.LabyMod;
import net.labymod.api.permissions.OldPluginMessage;
import net.labymod.support.util.Debug;
import net.minecraft.network.PacketBuffer;
import com.google.gson.JsonElement;
import com.google.common.io.Files;
import com.google.common.hash.Hashing;
import com.google.gson.JsonObject;
import java.io.File;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import net.labymod.api.events.PluginMessageEvent;

public class PluginMessageListener implements PluginMessageEvent
{
    private static final JsonParser jsonParser;
    private long lastMCBrandMessage;
    private JsonArray mods;
    
    static {
        jsonParser = new JsonParser();
    }
    
    public PluginMessageListener() {
        this.lastMCBrandMessage = -1L;
        this.mods = new JsonArray();
        try {
            final File modsFolder = new File("mods");
            if (!modsFolder.exists()) {
                return;
            }
            final File[] list = modsFolder.listFiles();
            if (list != null) {
                File[] array;
                for (int length = (array = list).length, i = 0; i < length; ++i) {
                    final File file = array[i];
                    if (file != null && !file.isDirectory() && file.getName().endsWith(".jar")) {
                        try {
                            final JsonObject entry = new JsonObject();
                            entry.addProperty("hash", "sha256:" + Files.hash(file, Hashing.sha256()).toString());
                            entry.addProperty("name", file.getName());
                            this.mods.add(entry);
                        }
                        catch (final Exception error) {
                            error.printStackTrace();
                        }
                    }
                }
            }
        }
        catch (final Exception error2) {
            error2.printStackTrace();
        }
    }
    
    @Override
    public void receiveMessage(final String channelName, final PacketBuffer packetBuffer) {
        if (!channelName.equals("CCP") && !channelName.equals("LMC")) {
            Debug.log(Debug.EnumDebugMode.PLUGINMESSAGE, "[IN] " + channelName);
        }
        try {
            OldPluginMessage.handlePluginMessage(channelName, packetBuffer);
            boolean updatedPermissions = false;
            if (channelName.equals("LMC")) {
                String messageKey = "unknown";
                String messageContent = "couldn't read the content";
                try {
                    if (packetBuffer.readableBytes() <= 0) {
                        throw new Exception("There is no message key");
                    }
                    messageKey = this.readStringFromBuffer(32767, packetBuffer);
                    if (packetBuffer.readableBytes() <= 0) {
                        throw new Exception("There is no message content");
                    }
                    messageContent = this.readStringFromBuffer(32767, packetBuffer);
                    if (messageKey.equals("PERMISSIONS")) {
                        updatedPermissions = true;
                    }
                    final JsonElement parsedServerMessage = PluginMessageListener.jsonParser.parse(messageContent);
                    LabyMod.getInstance().getEventManager().callServerMessage(messageKey, parsedServerMessage);
                    Debug.log(Debug.EnumDebugMode.PLUGINMESSAGE, "[IN] [LMC] " + messageKey + ": " + messageContent);
                }
                catch (final Exception ex) {
                    Debug.log(Debug.EnumDebugMode.PLUGINMESSAGE, "Failed parsing JSON message \"" + messageContent + "\" (key: " + messageKey + ")");
                    ex.printStackTrace();
                }
            }
            if (channelName.equals("LABYMOD") || updatedPermissions) {
                Permissions.getPermissionNotifyRenderer().checkChangedPermissions();
            }
            final boolean brandMessage = channelName.equals("MC|Brand") && this.lastMCBrandMessage + 1000L < System.currentTimeMillis();
            if (brandMessage) {
                this.lastMCBrandMessage = System.currentTimeMillis();
            }
            if (brandMessage && LabyMod.getSettings().improvedLavaFixedGhostBlocks && LabyMod.getInstance().getCurrentServerData() != null && Permissions.isAllowed(Permissions.Permission.IMPROVED_LAVA)) {
                FixedLiquidBucketProtocol.handleBucketAction(FixedLiquidBucketProtocol.Action.ENABLE, 0, 0, 0);
            }
            if (brandMessage) {
                LabyMod.getInstance().getChunkCachingProtocol().disable(null, true);
                final JsonArray addons = new JsonArray();
                for (final LabyModAddon addonInfo : AddonLoader.getAddons()) {
                    if (addonInfo.about != null) {
                        if (addonInfo.about.uuid == null) {
                            continue;
                        }
                        final JsonObject entry = new JsonObject();
                        entry.addProperty("uuid", addonInfo.about.uuid.toString());
                        entry.addProperty("name", addonInfo.about.name);
                        addons.add(entry);
                    }
                }
                final JsonObject ccp = new JsonObject();
                ccp.addProperty("enabled", LabyMod.getSettings().chunkCaching);
                ccp.addProperty("version", 2);
                final JsonObject shadow = new JsonObject();
                shadow.addProperty("enabled", true);
                shadow.addProperty("version", 1);
                final JsonObject obj = new JsonObject();
                obj.addProperty("version", "3.6.6");
                obj.add("ccp", ccp);
                obj.add("shadow", shadow);
                obj.add("addons", addons);
                if (LabyModCoreMod.isForge()) {
                    obj.add("mods", this.mods);
                }
                LabyMod.getInstance().getLabyModAPI().sendJsonMessageToServer("INFO", obj);
                if (LabyMod.getSettings().loadedShader != null && LabyMod.getSettings().betterShaderSelection) {
                    try {
                        GuiShaderSelection.loadShader(new ResourceLocation(LabyMod.getSettings().loadedShader));
                    }
                    catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (brandMessage) {
                Minecraft.getMinecraft().isSingleplayer();
            }
        }
        catch (final Exception error) {
            error.printStackTrace();
        }
    }
    
    public String readStringFromBuffer(final int maxLength, final PacketBuffer packetBuffer) {
        final int i = this.readVarIntFromBuffer(packetBuffer);
        if (i > maxLength * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + maxLength * 4 + ")");
        }
        if (i < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        }
        final ByteBuf byteBuf = packetBuffer.readBytes(i);
        byte[] bytes = null;
        if (byteBuf.hasArray()) {
            bytes = byteBuf.array();
        }
        else {
            bytes = new byte[byteBuf.readableBytes()];
            byteBuf.getBytes(byteBuf.readerIndex(), bytes);
        }
        final String s = new String(bytes, Charsets.UTF_8);
        if (s.length() > maxLength) {
            throw new DecoderException("The received string length is longer than maximum allowed (" + i + " > " + maxLength + ")");
        }
        return s;
    }
    
    public int readVarIntFromBuffer(final PacketBuffer packetBuffer) {
        int i = 0;
        int j = 0;
        byte b0;
        do {
            b0 = packetBuffer.readByte();
            i |= (b0 & 0x7F) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b0 & 0x80) == 0x80);
        return i;
    }
}
