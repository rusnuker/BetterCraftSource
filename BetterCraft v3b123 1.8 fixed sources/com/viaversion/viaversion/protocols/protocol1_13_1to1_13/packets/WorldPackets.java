// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13_1to1_13.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.types.Chunk1_13Type;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.rewriter.BlockRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_13_1to1_13.Protocol1_13_1To1_13;

public class WorldPackets
{
    public static void register(final Protocol1_13_1To1_13 protocol) {
        final BlockRewriter<ClientboundPackets1_13> blockRewriter = new BlockRewriter<ClientboundPackets1_13>(protocol, Type.POSITION);
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_13.CHUNK_DATA, wrapper -> {
            final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
            final Chunk chunk = wrapper.passthrough((Type<Chunk>)new Chunk1_13Type(clientWorld));
            chunk.getSections();
            final ChunkSection[] array;
            int j = 0;
            for (int length = array.length; j < length; ++j) {
                final ChunkSection section = array[j];
                if (section != null) {
                    final DataPalette palette = section.palette(PaletteType.BLOCKS);
                    for (int i = 0; i < palette.size(); ++i) {
                        final int mappedBlockStateId = protocol.getMappingData().getNewBlockStateId(palette.idByIndex(i));
                        palette.setIdByIndex(i, mappedBlockStateId);
                    }
                }
            }
            return;
        });
        blockRewriter.registerBlockAction(ClientboundPackets1_13.BLOCK_ACTION);
        blockRewriter.registerBlockChange(ClientboundPackets1_13.BLOCK_CHANGE);
        blockRewriter.registerMultiBlockChange(ClientboundPackets1_13.MULTI_BLOCK_CHANGE);
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_13.EFFECT, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map(Type.POSITION);
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> {
                    final Object val$protocol = protocol;
                    final int id = wrapper.get((Type<Integer>)Type.INT, 0);
                    if (id == 2000) {
                        final int data = wrapper.get((Type<Integer>)Type.INT, 1);
                        switch (data) {
                            case 1: {
                                wrapper.set(Type.INT, 1, 2);
                                break;
                            }
                            case 0:
                            case 3:
                            case 6: {
                                wrapper.set(Type.INT, 1, 4);
                                break;
                            }
                            case 2:
                            case 5:
                            case 8: {
                                wrapper.set(Type.INT, 1, 5);
                                break;
                            }
                            case 7: {
                                wrapper.set(Type.INT, 1, 3);
                                break;
                            }
                            default: {
                                wrapper.set(Type.INT, 1, 0);
                                break;
                            }
                        }
                    }
                    else if (id == 1010) {
                        wrapper.set(Type.INT, 1, protocol.getMappingData().getNewItemId(wrapper.get((Type<Integer>)Type.INT, 1)));
                    }
                    else if (id == 2001) {
                        wrapper.set(Type.INT, 1, protocol.getMappingData().getNewBlockStateId(wrapper.get((Type<Integer>)Type.INT, 1)));
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_13.JOIN_GAME, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> {
                    final ClientWorld clientChunks = wrapper.user().get(ClientWorld.class);
                    final int dimensionId = wrapper.get((Type<Integer>)Type.INT, 1);
                    clientChunks.setEnvironment(dimensionId);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_13.RESPAWN, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> {
                    final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
                    final int dimensionId = wrapper.get((Type<Integer>)Type.INT, 0);
                    clientWorld.setEnvironment(dimensionId);
                });
            }
        });
    }
}
