// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_17to1_16_4.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord1_16_2;
import com.viaversion.viaversion.api.minecraft.BlockChangeRecord;
import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import java.util.BitSet;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.types.Chunk1_17Type;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.types.Chunk1_16_2Type;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ClientboundPackets1_17;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import com.viaversion.viaversion.rewriter.BlockRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.Protocol1_17To1_16_4;

public final class WorldPackets
{
    public static void register(final Protocol1_17To1_16_4 protocol) {
        final BlockRewriter<ClientboundPackets1_16_2> blockRewriter = new BlockRewriter<ClientboundPackets1_16_2>(protocol, Type.POSITION1_14);
        blockRewriter.registerBlockAction(ClientboundPackets1_16_2.BLOCK_ACTION);
        blockRewriter.registerBlockChange(ClientboundPackets1_16_2.BLOCK_CHANGE);
        blockRewriter.registerVarLongMultiBlockChange(ClientboundPackets1_16_2.MULTI_BLOCK_CHANGE);
        blockRewriter.registerAcknowledgePlayerDigging(ClientboundPackets1_16_2.ACKNOWLEDGE_PLAYER_DIGGING);
        ((Protocol<ClientboundPackets1_16_2, ClientboundPackets1_17, SM, SU>)protocol).registerClientbound(ClientboundPackets1_16_2.WORLD_BORDER, null, wrapper -> {
            final int type = wrapper.read((Type<Integer>)Type.VAR_INT);
            ClientboundPacketType packetType = null;
            switch (type) {
                case 0: {
                    packetType = ClientboundPackets1_17.WORLD_BORDER_SIZE;
                    break;
                }
                case 1: {
                    packetType = ClientboundPackets1_17.WORLD_BORDER_LERP_SIZE;
                    break;
                }
                case 2: {
                    packetType = ClientboundPackets1_17.WORLD_BORDER_CENTER;
                    break;
                }
                case 3: {
                    packetType = ClientboundPackets1_17.WORLD_BORDER_INIT;
                    break;
                }
                case 4: {
                    packetType = ClientboundPackets1_17.WORLD_BORDER_WARNING_DELAY;
                    break;
                }
                case 5: {
                    packetType = ClientboundPackets1_17.WORLD_BORDER_WARNING_DISTANCE;
                    break;
                }
                default: {
                    new IllegalArgumentException("Invalid world border type received: " + type);
                    throw;
                }
            }
            wrapper.setPacketType(packetType);
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_16_2, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_16_2.UPDATE_LIGHT, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.handler(wrapper -> {
                    final int skyLightMask = wrapper.read((Type<Integer>)Type.VAR_INT);
                    final int blockLightMask = wrapper.read((Type<Integer>)Type.VAR_INT);
                    wrapper.write(Type.LONG_ARRAY_PRIMITIVE, this.toBitSetLongArray(skyLightMask));
                    wrapper.write(Type.LONG_ARRAY_PRIMITIVE, this.toBitSetLongArray(blockLightMask));
                    wrapper.write(Type.LONG_ARRAY_PRIMITIVE, this.toBitSetLongArray(wrapper.read((Type<Integer>)Type.VAR_INT)));
                    wrapper.write(Type.LONG_ARRAY_PRIMITIVE, this.toBitSetLongArray(wrapper.read((Type<Integer>)Type.VAR_INT)));
                    this.writeLightArrays(wrapper, skyLightMask);
                    this.writeLightArrays(wrapper, blockLightMask);
                });
            }
            
            private void writeLightArrays(final PacketWrapper wrapper, final int bitMask) throws Exception {
                final List<byte[]> light = new ArrayList<byte[]>();
                for (int i = 0; i < 18; ++i) {
                    if (this.isSet(bitMask, i)) {
                        light.add(wrapper.read(Type.BYTE_ARRAY_PRIMITIVE));
                    }
                }
                wrapper.write(Type.VAR_INT, light.size());
                for (final byte[] bytes : light) {
                    wrapper.write(Type.BYTE_ARRAY_PRIMITIVE, bytes);
                }
            }
            
            private long[] toBitSetLongArray(final int bitmask) {
                return new long[] { bitmask };
            }
            
            private boolean isSet(final int mask, final int i) {
                return (mask & 1 << i) != 0x0;
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16_2, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_16_2.CHUNK_DATA, wrapper -> {
            final Chunk chunk = wrapper.read((Type<Chunk>)new Chunk1_16_2Type());
            if (!chunk.isFullChunk()) {
                writeMultiBlockChangePacket(wrapper, chunk);
                wrapper.cancel();
                return;
            }
            else {
                wrapper.write(new Chunk1_17Type(chunk.getSections().length), chunk);
                chunk.setChunkMask(BitSet.valueOf(new long[] { chunk.getBitmask() }));
                for (int s = 0; s < chunk.getSections().length; ++s) {
                    final ChunkSection section = chunk.getSections()[s];
                    if (section != null) {
                        final DataPalette palette = section.palette(PaletteType.BLOCKS);
                        for (int i = 0; i < palette.size(); ++i) {
                            final int mappedBlockStateId = protocol.getMappingData().getNewBlockStateId(palette.idByIndex(i));
                            palette.setIdByIndex(i, mappedBlockStateId);
                        }
                    }
                }
                return;
            }
        });
        blockRewriter.registerEffect(ClientboundPackets1_16_2.EFFECT, 1010, 2001);
    }
    
    private static void writeMultiBlockChangePacket(final PacketWrapper wrapper, final Chunk chunk) throws Exception {
        long chunkPosition = ((long)chunk.getX() & 0x3FFFFFL) << 42;
        chunkPosition |= ((long)chunk.getZ() & 0x3FFFFFL) << 20;
        final ChunkSection[] sections = chunk.getSections();
        for (int chunkY = 0; chunkY < sections.length; ++chunkY) {
            final ChunkSection section = sections[chunkY];
            if (section != null) {
                final PacketWrapper blockChangePacket = wrapper.create(ClientboundPackets1_17.MULTI_BLOCK_CHANGE);
                blockChangePacket.write(Type.LONG, chunkPosition | ((long)chunkY & 0xFFFFFL));
                blockChangePacket.write(Type.BOOLEAN, true);
                final BlockChangeRecord[] blockChangeRecords = new BlockChangeRecord[4096];
                final DataPalette palette = section.palette(PaletteType.BLOCKS);
                int j = 0;
                for (int x = 0; x < 16; ++x) {
                    for (int y = 0; y < 16; ++y) {
                        for (int z = 0; z < 16; ++z) {
                            final int blockStateId = Protocol1_17To1_16_4.MAPPINGS.getNewBlockStateId(palette.idAt(x, y, z));
                            blockChangeRecords[j++] = new BlockChangeRecord1_16_2(x, y, z, blockStateId);
                        }
                    }
                }
                blockChangePacket.write(Type.VAR_LONG_BLOCK_CHANGE_RECORD_ARRAY, blockChangeRecords);
                blockChangePacket.send(Protocol1_17To1_16_4.class);
            }
        }
    }
}
