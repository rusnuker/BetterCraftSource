// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.packets;

import com.viaversion.viaversion.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.type.types.version.Types1_13;
import com.viaversion.viaversion.api.type.types.version.Types1_12;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_13Types;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.metadata.MetadataRewriter1_13To1_12_2;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;

public class EntityPackets
{
    public static void register(final Protocol1_13To1_12_2 protocol) {
        final MetadataRewriter1_13To1_12_2 metadataRewriter = protocol.get(MetadataRewriter1_13To1_12_2.class);
        ((AbstractProtocol<ClientboundPackets1_12_1, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_12_1.SPAWN_ENTITY, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> {
                    final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final byte type = wrapper.get((Type<Byte>)Type.BYTE, 0);
                    final Entity1_13Types.EntityType entType = Entity1_13Types.getTypeFromId(type, true);
                    if (entType != null) {
                        wrapper.user().getEntityTracker(Protocol1_13To1_12_2.class).addEntity(entityId, entType);
                        if (entType.is(Entity1_13Types.EntityType.FALLING_BLOCK)) {
                            final int oldId = wrapper.get((Type<Integer>)Type.INT, 0);
                            final int combined = (oldId & 0xFFF) << 4 | (oldId >> 12 & 0xF);
                            wrapper.set(Type.INT, 0, WorldPackets.toNewId(combined));
                        }
                        if (entType.is(Entity1_13Types.EntityType.ITEM_FRAME)) {
                            int data = wrapper.get((Type<Integer>)Type.INT, 0);
                            switch (data) {
                                case 0: {
                                    data = 3;
                                    break;
                                }
                                case 1: {
                                    data = 4;
                                    break;
                                }
                                case 3: {
                                    data = 5;
                                    break;
                                }
                            }
                            wrapper.set(Type.INT, 0, data);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_12_1, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_12_1.SPAWN_MOB, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.SHORT);
                this.map(Types1_12.METADATA_LIST, Types1_13.METADATA_LIST);
                this.handler(metadataRewriter.trackerAndRewriterHandler(Types1_13.METADATA_LIST));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_12_1, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_12_1.SPAWN_PLAYER, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map(Types1_12.METADATA_LIST, Types1_13.METADATA_LIST);
                this.handler(metadataRewriter.trackerAndRewriterHandler(Types1_13.METADATA_LIST, Entity1_13Types.EntityType.PLAYER));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_12_1, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_12_1.JOIN_GAME, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> {
                    final ClientWorld clientChunks = wrapper.user().get(ClientWorld.class);
                    final int dimensionId = wrapper.get((Type<Integer>)Type.INT, 1);
                    clientChunks.setEnvironment(dimensionId);
                    return;
                });
                this.handler(metadataRewriter.playerTrackerHandler());
                this.handler(Protocol1_13To1_12_2.SEND_DECLARE_COMMANDS_AND_TAGS);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_12_1, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_12_1.ENTITY_EFFECT, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(packetWrapper -> {
                    byte flags = packetWrapper.read((Type<Byte>)Type.BYTE);
                    if (Via.getConfig().isNewEffectIndicator()) {
                        flags |= 0x4;
                    }
                    packetWrapper.write(Type.BYTE, flags);
                });
            }
        });
        ((EntityRewriter<ClientboundPackets1_12_1, T>)metadataRewriter).registerRemoveEntities(ClientboundPackets1_12_1.DESTROY_ENTITIES);
        ((EntityRewriter<ClientboundPackets1_12_1, T>)metadataRewriter).registerMetadataRewriter(ClientboundPackets1_12_1.ENTITY_METADATA, Types1_12.METADATA_LIST, Types1_13.METADATA_LIST);
    }
}
