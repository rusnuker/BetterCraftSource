// 
// Decompiled by Procyon v0.6.0
// 

package wdl;

import com.google.common.io.ByteArrayDataInput;
import java.io.UnsupportedEncodingException;
import wdl.api.IWDLMessageType;
import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.network.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import com.google.common.io.ByteStreams;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import java.util.Iterator;
import net.minecraft.world.chunk.Chunk;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import java.util.List;
import com.google.common.collect.Multimap;
import java.util.Map;
import java.util.HashSet;
import org.apache.logging.log4j.Logger;

public class WDLPluginChannels
{
    private static Logger logger;
    private static HashSet<Integer> receivedPackets;
    private static boolean canUseFunctionsUnknownToServer;
    private static boolean canDownloadInGeneral;
    private static int saveRadius;
    private static boolean canCacheChunks;
    private static boolean canSaveEntities;
    private static boolean canSaveTileEntities;
    private static boolean canSaveContainers;
    private static Map<String, Integer> entityRanges;
    private static boolean canRequestPermissions;
    private static String requestMessage;
    private static Map<String, Multimap<String, ChunkRange>> chunkOverrides;
    private static Map<String, String> requests;
    public static final List<String> BOOLEAN_REQUEST_FIELDS;
    public static final List<String> INTEGER_REQUEST_FIELDS;
    private static List<ChunkRange> chunkOverrideRequests;
    
    static {
        WDLPluginChannels.logger = LogManager.getLogger();
        WDLPluginChannels.receivedPackets = new HashSet<Integer>();
        WDLPluginChannels.canUseFunctionsUnknownToServer = true;
        WDLPluginChannels.canDownloadInGeneral = true;
        WDLPluginChannels.saveRadius = -1;
        WDLPluginChannels.canCacheChunks = true;
        WDLPluginChannels.canSaveEntities = true;
        WDLPluginChannels.canSaveTileEntities = true;
        WDLPluginChannels.canSaveContainers = true;
        WDLPluginChannels.entityRanges = new HashMap<String, Integer>();
        WDLPluginChannels.canRequestPermissions = false;
        WDLPluginChannels.requestMessage = "";
        WDLPluginChannels.chunkOverrides = new HashMap<String, Multimap<String, ChunkRange>>();
        WDLPluginChannels.requests = new HashMap<String, String>();
        BOOLEAN_REQUEST_FIELDS = Arrays.asList("downloadInGeneral", "cacheChunks", "saveEntities", "saveTileEntities", "saveContainers", "getEntityRanges");
        INTEGER_REQUEST_FIELDS = Arrays.asList("saveRadius");
        WDLPluginChannels.chunkOverrideRequests = new ArrayList<ChunkRange>();
    }
    
    public static boolean canUseFunctionsUnknownToServer() {
        return !WDLPluginChannels.receivedPackets.contains(0) || WDLPluginChannels.canUseFunctionsUnknownToServer;
    }
    
    public static boolean canDownloadAtAll() {
        return hasChunkOverrides() || canDownloadInGeneral();
    }
    
    public static boolean canDownloadInGeneral() {
        if (WDLPluginChannels.receivedPackets.contains(1)) {
            return WDLPluginChannels.canDownloadInGeneral;
        }
        return canUseFunctionsUnknownToServer();
    }
    
    public static boolean canSaveChunk(final Chunk chunk) {
        if (isChunkOverridden(chunk)) {
            return true;
        }
        if (!canDownloadInGeneral()) {
            return false;
        }
        if (WDLPluginChannels.receivedPackets.contains(1)) {
            if (!WDLPluginChannels.canCacheChunks && WDLPluginChannels.saveRadius >= 0) {
                final int distanceX = chunk.xPosition - WDL.thePlayer.chunkCoordX;
                final int distanceZ = chunk.zPosition - WDL.thePlayer.chunkCoordZ;
                if (Math.abs(distanceX) > WDLPluginChannels.saveRadius || Math.abs(distanceZ) > WDLPluginChannels.saveRadius) {
                    return false;
                }
            }
            return true;
        }
        return canUseFunctionsUnknownToServer();
    }
    
    public static boolean canSaveEntities() {
        if (!canDownloadInGeneral()) {
            return false;
        }
        if (WDLPluginChannels.receivedPackets.contains(1)) {
            return WDLPluginChannels.canSaveEntities;
        }
        return canUseFunctionsUnknownToServer();
    }
    
    public static boolean canSaveEntities(final Chunk chunk) {
        return isChunkOverridden(chunk) || canSaveEntities();
    }
    
    public static boolean canSaveEntities(final int chunkX, final int chunkZ) {
        return isChunkOverridden(chunkX, chunkZ) || canSaveEntities();
    }
    
    public static boolean canSaveTileEntities() {
        if (!canDownloadInGeneral()) {
            return false;
        }
        if (WDLPluginChannels.receivedPackets.contains(1)) {
            return WDLPluginChannels.canSaveTileEntities;
        }
        return canUseFunctionsUnknownToServer();
    }
    
    public static boolean canSaveTileEntities(final Chunk chunk) {
        return isChunkOverridden(chunk) || canSaveTileEntities();
    }
    
    public static boolean canSaveTileEntities(final int chunkX, final int chunkZ) {
        return isChunkOverridden(chunkX, chunkZ) || canSaveTileEntities();
    }
    
    public static boolean canSaveContainers() {
        if (!canDownloadInGeneral()) {
            return false;
        }
        if (!canSaveTileEntities()) {
            return false;
        }
        if (WDLPluginChannels.receivedPackets.contains(1)) {
            return WDLPluginChannels.canSaveContainers;
        }
        return canUseFunctionsUnknownToServer();
    }
    
    public static boolean canSaveContainers(final Chunk chunk) {
        return isChunkOverridden(chunk) || canSaveContainers();
    }
    
    public static boolean canSaveContainers(final int chunkX, final int chunkZ) {
        return isChunkOverridden(chunkX, chunkZ) || canSaveContainers();
    }
    
    public static boolean canSaveMaps() {
        if (!canDownloadInGeneral()) {
            return false;
        }
        if (WDLPluginChannels.receivedPackets.contains(1)) {
            return WDLPluginChannels.canSaveTileEntities;
        }
        return canUseFunctionsUnknownToServer();
    }
    
    public static int getEntityRange(final String entity) {
        if (!canSaveEntities(null)) {
            return -1;
        }
        if (!WDLPluginChannels.receivedPackets.contains(2)) {
            return -1;
        }
        if (WDLPluginChannels.entityRanges.containsKey(entity)) {
            return WDLPluginChannels.entityRanges.get(entity);
        }
        return -1;
    }
    
    public static int getSaveRadius() {
        return WDLPluginChannels.saveRadius;
    }
    
    public static boolean canCacheChunks() {
        return WDLPluginChannels.canCacheChunks;
    }
    
    public static boolean hasServerEntityRange() {
        return WDLPluginChannels.receivedPackets.contains(2) && WDLPluginChannels.entityRanges.size() > 0;
    }
    
    public static Map<String, Integer> getEntityRanges() {
        return new HashMap<String, Integer>(WDLPluginChannels.entityRanges);
    }
    
    public static boolean hasPermissions() {
        return WDLPluginChannels.receivedPackets != null && !WDLPluginChannels.receivedPackets.isEmpty();
    }
    
    public static boolean canRequestPermissions() {
        return WDLPluginChannels.receivedPackets.contains(3) && WDLPluginChannels.canRequestPermissions;
    }
    
    public static String getRequestMessage() {
        if (WDLPluginChannels.receivedPackets.contains(3)) {
            return WDLPluginChannels.requestMessage;
        }
        return null;
    }
    
    public static boolean isChunkOverridden(final Chunk chunk) {
        return chunk != null && isChunkOverridden(chunk.xPosition, chunk.zPosition);
    }
    
    public static boolean isChunkOverridden(final int x, final int z) {
        for (final Multimap<String, ChunkRange> map : WDLPluginChannels.chunkOverrides.values()) {
            for (final ChunkRange range : map.values()) {
                if (x >= range.x1 && x <= range.x2 && z >= range.z1 && z <= range.z2) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean hasChunkOverrides() {
        if (!WDLPluginChannels.receivedPackets.contains(4)) {
            return false;
        }
        if (WDLPluginChannels.chunkOverrides == null || WDLPluginChannels.chunkOverrides.isEmpty()) {
            return false;
        }
        for (final Multimap<String, ChunkRange> m : WDLPluginChannels.chunkOverrides.values()) {
            if (!m.isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    public static Map<String, Multimap<String, ChunkRange>> getChunkOverrides() {
        final Map<String, Multimap<String, ChunkRange>> returned = new HashMap<String, Multimap<String, ChunkRange>>();
        for (final Map.Entry<String, Multimap<String, ChunkRange>> e : WDLPluginChannels.chunkOverrides.entrySet()) {
            final Multimap<String, ChunkRange> map = (Multimap<String, ChunkRange>)ImmutableMultimap.copyOf((Multimap<?, ?>)e.getValue());
            returned.put(e.getKey(), map);
        }
        return (Map<String, Multimap<String, ChunkRange>>)ImmutableMap.copyOf((Map<?, ?>)returned);
    }
    
    public static void addRequest(final String key, final String value) {
        if (!isValidRequest(key, value)) {
            return;
        }
        WDLPluginChannels.requests.put(key, value);
    }
    
    public static Map<String, String> getRequests() {
        return (Map<String, String>)ImmutableMap.copyOf((Map<?, ?>)WDLPluginChannels.requests);
    }
    
    public static boolean isValidRequest(final String key, final String value) {
        if (key == null || value == null) {
            return false;
        }
        if (WDLPluginChannels.BOOLEAN_REQUEST_FIELDS.contains(key)) {
            return value.equals("true") || value.equals("false");
        }
        if (WDLPluginChannels.INTEGER_REQUEST_FIELDS.contains(key)) {
            try {
                Integer.parseInt(value);
                return true;
            }
            catch (final NumberFormatException e) {
                return false;
            }
        }
        return false;
    }
    
    public static List<ChunkRange> getChunkOverrideRequests() {
        return (List<ChunkRange>)ImmutableList.copyOf((Collection<?>)WDLPluginChannels.chunkOverrideRequests);
    }
    
    public static void addChunkOverrideRequest(final ChunkRange range) {
        WDLPluginChannels.chunkOverrideRequests.add(range);
    }
    
    public static void sendRequests() {
        if (WDLPluginChannels.requests.isEmpty() && WDLPluginChannels.chunkOverrideRequests.isEmpty()) {
            return;
        }
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("REQUEST REASON WILL GO HERE");
        output.writeInt(WDLPluginChannels.requests.size());
        for (final Map.Entry<String, String> request : WDLPluginChannels.requests.entrySet()) {
            output.writeUTF(request.getKey());
            output.writeUTF(request.getValue());
        }
        output.writeInt(WDLPluginChannels.chunkOverrideRequests.size());
        for (final ChunkRange range : WDLPluginChannels.chunkOverrideRequests) {
            range.writeToOutput(output);
        }
        final PacketBuffer requestBuffer = new PacketBuffer(Unpooled.buffer());
        requestBuffer.writeBytes(output.toByteArray());
        final C17PacketCustomPayload requestPacket = new C17PacketCustomPayload("WDL|REQUEST", requestBuffer);
        Minecraft.getMinecraft().getNetHandler().addToSendQueue(requestPacket);
    }
    
    static void onWorldLoad() {
        final Minecraft minecraft = Minecraft.getMinecraft();
        WDLPluginChannels.receivedPackets = new HashSet<Integer>();
        WDLPluginChannels.requests = new HashMap<String, String>();
        WDLPluginChannels.chunkOverrideRequests = new ArrayList<ChunkRange>();
        WDLPluginChannels.canUseFunctionsUnknownToServer = true;
        WDLMessages.chatMessageTranslated(WDLMessageTypes.PLUGIN_CHANNEL_MESSAGE, "wdl.messages.permissions.init", new Object[0]);
        final PacketBuffer registerPacketBuffer = new PacketBuffer(Unpooled.buffer());
        registerPacketBuffer.writeBytes(new byte[] { 87, 68, 76, 124, 73, 78, 73, 84, 0, 87, 68, 76, 124, 67, 79, 78, 84, 82, 79, 76, 0, 87, 68, 76, 124, 82, 69, 81, 85, 69, 83, 84, 0 });
        final C17PacketCustomPayload registerPacket = new C17PacketCustomPayload("REGISTER", registerPacketBuffer);
        try {
            final C17PacketCustomPayload initPacket = new C17PacketCustomPayload("WDL|INIT", new PacketBuffer(Unpooled.copiedBuffer("1.8.9a-beta2".getBytes("UTF-8"))));
        }
        catch (final UnsupportedEncodingException e) {
            WDLMessages.chatMessageTranslated(WDLMessageTypes.ERROR, "wdl.messages.generalError.noUTF8", e);
            final C17PacketCustomPayload initPacket = new C17PacketCustomPayload("WDL|INIT", new PacketBuffer(Unpooled.buffer()));
        }
    }
    
    static void onPluginChannelPacket(final String channel, final byte[] bytes) {
    }
    
    public static class ChunkRange
    {
        public final String tag;
        public final int x1;
        public final int z1;
        public final int x2;
        public final int z2;
        
        public ChunkRange(final String tag, final int x1, final int z1, final int x2, final int z2) {
            this.tag = tag;
            if (x1 > x2) {
                this.x1 = x2;
                this.x2 = x1;
            }
            else {
                this.x1 = x1;
                this.x2 = x2;
            }
            if (z1 > z2) {
                this.z1 = z2;
                this.z2 = z1;
            }
            else {
                this.z1 = z1;
                this.z2 = z2;
            }
        }
        
        public static ChunkRange readFromInput(final ByteArrayDataInput input) {
            final String tag = input.readUTF();
            final int x1 = input.readInt();
            final int z1 = input.readInt();
            final int x2 = input.readInt();
            final int z2 = input.readInt();
            return new ChunkRange(tag, x1, z1, x2, z2);
        }
        
        public void writeToOutput(final ByteArrayDataOutput output) {
            output.writeUTF(this.tag);
            output.writeInt(this.x1);
            output.writeInt(this.z1);
            output.writeInt(this.x2);
        }
        
        @Override
        public String toString() {
            return "ChunkRange [tag=" + this.tag + ", x1=" + this.x1 + ", z1=" + this.z1 + ", x2=" + this.x2 + ", z2=" + this.z2 + "]";
        }
    }
}
