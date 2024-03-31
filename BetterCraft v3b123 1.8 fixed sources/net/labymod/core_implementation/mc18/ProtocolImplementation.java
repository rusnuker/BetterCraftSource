// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18;

import net.minecraft.util.MovementInput;
import net.minecraft.client.entity.EntityPlayerSP;
import net.labymod.core.LabyModCore;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.labymod.api.protocol.shadow.ShadowProtocol;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.client.Minecraft;
import net.labymod.utils.GZIPCompression;
import net.labymod.api.protocol.chunk.Extracted;
import net.labymod.api.protocol.chunk.ChunkCachingProtocol;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.labymod.main.LabyMod;
import net.labymod.core.ProtocolAdapter;

public class ProtocolImplementation implements ProtocolAdapter
{
    @Override
    public void onReceiveChunkPacket(final Object packetBuffer, final Object packet) {
        final ChunkCachingProtocol protocol = LabyMod.getInstance().getChunkCachingProtocol();
        if (packet instanceof S21PacketChunkData) {
            final S21PacketChunkData chunkDataPacket = (S21PacketChunkData)packet;
            protocol.onReceive18ChunkData(chunkDataPacket.getChunkX(), chunkDataPacket.getChunkZ(), chunkDataPacket.getExtractedSize(), chunkDataPacket.getExtractedDataBytes());
        }
        if (packet instanceof S26PacketMapChunkBulk) {
            final S26PacketMapChunkBulk chunkDataPacket2 = (S26PacketMapChunkBulk)packet;
            for (int i = chunkDataPacket2.getChunkCount(), k = 0; k < i; ++k) {
                protocol.onReceive18ChunkData(chunkDataPacket2.getChunkX(k), chunkDataPacket2.getChunkZ(k), chunkDataPacket2.getChunkSize(k), chunkDataPacket2.getChunkBytes(k));
            }
        }
    }
    
    @Override
    public void loadChunk(final ChunkCachingProtocol protocol, final Extracted extracted, final int chunkX, final int chunkZ, final boolean flag) {
        final byte[] decompressedData = GZIPCompression.decompress(extracted.data);
        final WorldClient clientWorldController = Minecraft.getMinecraft().theWorld;
        if (flag) {
            if (extracted.dataSize == 0) {
                clientWorldController.doPreChunk(chunkX, chunkZ, false);
                return;
            }
            clientWorldController.doPreChunk(chunkX, chunkZ, true);
        }
        clientWorldController.invalidateBlockReceiveRegion(chunkX << 4, 0, chunkZ << 4, (chunkX << 4) + 15, 256, (chunkZ << 4) + 15);
        final Chunk chunk = clientWorldController.getChunkFromChunkCoords(chunkX, chunkZ);
        chunk.fillChunk(decompressedData, extracted.dataSize, flag);
        clientWorldController.markBlockRangeForRenderUpdate(chunkX << 4, 0, chunkZ << 4, (chunkX << 4) + 15, 256, (chunkZ << 4) + 15);
        if (!flag || !(clientWorldController.provider instanceof WorldProviderSurface)) {
            chunk.resetRelightChecks();
        }
    }
    
    @Override
    public void loadChunkBulk(final ChunkCachingProtocol protocol, final Extracted[] extractedArray, final int[] chunkX, final int[] chunkZ) {
        final WorldClient clientWorldController = Minecraft.getMinecraft().theWorld;
        for (int i = 0; i < extractedArray.length; ++i) {
            final Extracted extracted = extractedArray[i];
            final byte[] decompressedData = GZIPCompression.decompress(extracted.data);
            final int x = chunkX[i];
            final int z = chunkZ[i];
            clientWorldController.doPreChunk(x, z, true);
            clientWorldController.invalidateBlockReceiveRegion(x << 4, 0, z << 4, (x << 4) + 15, 256, (z << 4) + 15);
            final Chunk chunk = clientWorldController.getChunkFromChunkCoords(x, z);
            chunk.fillChunk(decompressedData, extracted.dataSize, true);
            clientWorldController.markBlockRangeForRenderUpdate(x << 4, 0, z << 4, (x << 4) + 15, 256, (z << 4) + 15);
            if (!(clientWorldController.provider instanceof WorldProviderSurface)) {
                chunk.resetRelightChecks();
            }
        }
    }
    
    @Override
    public boolean handleOutgoingPacket(final Object msg, final ShadowProtocol shadowProtocol) {
        if (msg instanceof C03PacketPlayer.C04PacketPlayerPosition) {
            final C03PacketPlayer.C04PacketPlayerPosition packet = (C03PacketPlayer.C04PacketPlayerPosition)msg;
            final PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
            packetBuffer.writeByte(0);
            packetBuffer.writeDouble(packet.getPositionX());
            packetBuffer.writeDouble(packet.getPositionY());
            packetBuffer.writeDouble(packet.getPositionZ());
            packetBuffer.writeBoolean(packet.isOnGround());
            this.addInputs(packetBuffer, shadowProtocol);
            LabyMod.getInstance().getLabyModAPI().sendPluginMessage("SHADOW", packetBuffer);
            return true;
        }
        if (msg instanceof C03PacketPlayer.C05PacketPlayerLook) {
            final C03PacketPlayer.C05PacketPlayerLook packet2 = (C03PacketPlayer.C05PacketPlayerLook)msg;
            final PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
            packetBuffer.writeByte(1);
            packetBuffer.writeFloat(packet2.getYaw());
            packetBuffer.writeFloat(packet2.getPitch());
            packetBuffer.writeBoolean(packet2.isOnGround());
            this.addInputs(packetBuffer, shadowProtocol);
            LabyMod.getInstance().getLabyModAPI().sendPluginMessage("SHADOW", packetBuffer);
            return true;
        }
        if (msg instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
            final C03PacketPlayer.C06PacketPlayerPosLook packet3 = (C03PacketPlayer.C06PacketPlayerPosLook)msg;
            final PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
            packetBuffer.writeByte(2);
            packetBuffer.writeDouble(packet3.getPositionX());
            packetBuffer.writeDouble(packet3.getPositionY());
            packetBuffer.writeDouble(packet3.getPositionZ());
            packetBuffer.writeFloat(packet3.getYaw());
            packetBuffer.writeFloat(packet3.getPitch());
            packetBuffer.writeBoolean(packet3.isOnGround());
            this.addInputs(packetBuffer, shadowProtocol);
            LabyMod.getInstance().getLabyModAPI().sendPluginMessage("SHADOW", packetBuffer);
            return true;
        }
        if (msg instanceof C03PacketPlayer) {
            final C03PacketPlayer packet4 = (C03PacketPlayer)msg;
            final PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
            packetBuffer.writeByte(3);
            packetBuffer.writeBoolean(packet4.isOnGround());
            this.addInputs(packetBuffer, shadowProtocol);
            LabyMod.getInstance().getLabyModAPI().sendPluginMessage("SHADOW", packetBuffer);
        }
        return false;
    }
    
    private void addInputs(final PacketBuffer packetBuffer, final ShadowProtocol shadowProtocol) {
        final EntityPlayerSP player = LabyModCore.getMinecraft().getPlayer();
        final MovementInput movementInput = player.movementInput;
        packetBuffer.writeLong(System.currentTimeMillis());
        packetBuffer.writeDouble(movementInput.moveForward);
        packetBuffer.writeDouble(movementInput.moveStrafe);
        packetBuffer.writeBoolean(movementInput.jump);
        packetBuffer.writeBoolean(movementInput.sneak);
        packetBuffer.writeDouble(player.posX);
        packetBuffer.writeDouble(player.posY);
        packetBuffer.writeDouble(player.posZ);
        packetBuffer.writeFloat(player.rotationYaw);
        packetBuffer.writeFloat(player.rotationPitch);
        packetBuffer.writeBoolean(player.isSprinting());
        packetBuffer.writeInt(shadowProtocol.getPacketCounter().get());
    }
}
