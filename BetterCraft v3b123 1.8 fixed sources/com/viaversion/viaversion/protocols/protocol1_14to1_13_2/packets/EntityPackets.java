// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_14to1_13_2.packets;

import com.viaversion.viaversion.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.LinkedList;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.storage.EntityTracker1_14;
import com.viaversion.viaversion.api.type.types.version.Types1_14;
import com.viaversion.viaversion.api.type.types.version.Types1_13_2;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_14Types;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_13Types;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.metadata.MetadataRewriter1_14To1_13_2;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.Protocol1_14To1_13_2;

public class EntityPackets
{
    public static void register(final Protocol1_14To1_13_2 protocol) {
        final MetadataRewriter1_14To1_13_2 metadataRewriter = protocol.get(MetadataRewriter1_14To1_13_2.class);
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_13.SPAWN_ENTITY, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.BYTE, Type.VAR_INT);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.SHORT);
                this.handler(wrapper -> {
                    final Object val$metadataRewriter = metadataRewriter;
                    final Object val$protocol = protocol;
                    final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final int typeId = wrapper.get((Type<Integer>)Type.VAR_INT, 1);
                    final Entity1_13Types.EntityType type1_13 = Entity1_13Types.getTypeFromId(typeId, true);
                    int typeId2 = metadataRewriter.newEntityId(type1_13.getId());
                    final EntityType type1_14 = Entity1_14Types.getTypeFromId(typeId2);
                    if (type1_14 != null) {
                        final int data = wrapper.get((Type<Integer>)Type.INT, 0);
                        if (type1_14.is(Entity1_14Types.FALLING_BLOCK)) {
                            wrapper.set(Type.INT, 0, protocol.getMappingData().getNewBlockStateId(data));
                        }
                        else if (type1_14.is(Entity1_14Types.MINECART)) {
                            switch (data) {
                                case 1: {
                                    typeId2 = Entity1_14Types.CHEST_MINECART.getId();
                                    break;
                                }
                                case 2: {
                                    typeId2 = Entity1_14Types.FURNACE_MINECART.getId();
                                    break;
                                }
                                case 3: {
                                    typeId2 = Entity1_14Types.TNT_MINECART.getId();
                                    break;
                                }
                                case 4: {
                                    typeId2 = Entity1_14Types.SPAWNER_MINECART.getId();
                                    break;
                                }
                                case 5: {
                                    typeId2 = Entity1_14Types.HOPPER_MINECART.getId();
                                    break;
                                }
                                case 6: {
                                    typeId2 = Entity1_14Types.COMMAND_BLOCK_MINECART.getId();
                                    break;
                                }
                            }
                        }
                        else if ((type1_14.is(Entity1_14Types.ITEM) && data > 0) || type1_14.isOrHasParent(Entity1_14Types.ABSTRACT_ARROW)) {
                            if (type1_14.isOrHasParent(Entity1_14Types.ABSTRACT_ARROW)) {
                                wrapper.set(Type.INT, 0, data - 1);
                            }
                            final PacketWrapper velocity = wrapper.create(69);
                            velocity.write(Type.VAR_INT, entityId);
                            velocity.write((Type<Object>)Type.SHORT, wrapper.get((Type<T>)Type.SHORT, 0));
                            velocity.write((Type<Object>)Type.SHORT, wrapper.get((Type<T>)Type.SHORT, 1));
                            velocity.write((Type<Object>)Type.SHORT, wrapper.get((Type<T>)Type.SHORT, 2));
                            velocity.scheduleSend(Protocol1_14To1_13_2.class);
                        }
                        wrapper.user().getEntityTracker(Protocol1_14To1_13_2.class).addEntity(entityId, type1_14);
                    }
                    wrapper.set(Type.VAR_INT, 1, typeId2);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_13.SPAWN_MOB, new PacketHandlers() {
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
                this.map(Types1_13_2.METADATA_LIST, Types1_14.METADATA_LIST);
                this.handler(metadataRewriter.trackerAndRewriterHandler(Types1_14.METADATA_LIST));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_13.SPAWN_PAINTING, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.POSITION, Type.POSITION1_14);
                this.map((Type<Object>)Type.BYTE);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_13.SPAWN_PLAYER, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map(Types1_13_2.METADATA_LIST, Types1_14.METADATA_LIST);
                this.handler(metadataRewriter.trackerAndRewriterHandler(Types1_14.METADATA_LIST, Entity1_14Types.PLAYER));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_13.ENTITY_ANIMATION, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    final short animation = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE);
                    if (animation == 2) {
                        final EntityTracker1_14 tracker = wrapper.user().getEntityTracker(Protocol1_14To1_13_2.class);
                        final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        tracker.setSleeping(entityId, false);
                        final PacketWrapper metadataPacket = wrapper.create(ClientboundPackets1_14.ENTITY_METADATA);
                        metadataPacket.write(Type.VAR_INT, entityId);
                        final LinkedList<Metadata> metadataList = new LinkedList<Metadata>();
                        if (tracker.clientEntityId() != entityId) {
                            metadataList.add(new Metadata(6, Types1_14.META_TYPES.poseType, MetadataRewriter1_14To1_13_2.recalculatePlayerPose(entityId, tracker)));
                        }
                        metadataList.add(new Metadata(12, Types1_14.META_TYPES.optionalPositionType, null));
                        metadataPacket.write(Types1_14.METADATA_LIST, metadataList);
                        metadataPacket.scheduleSend(Protocol1_14To1_13_2.class);
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
                    return;
                });
                this.handler(metadataRewriter.playerTrackerHandler());
                this.handler(wrapper -> {
                    final Object val$protocol = protocol;
                    final short difficulty = wrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                    final PacketWrapper difficultyPacket = wrapper.create(ClientboundPackets1_14.SERVER_DIFFICULTY);
                    difficultyPacket.write(Type.UNSIGNED_BYTE, difficulty);
                    difficultyPacket.write(Type.BOOLEAN, false);
                    difficultyPacket.scheduleSend(protocol.getClass());
                    wrapper.passthrough((Type<Object>)Type.UNSIGNED_BYTE);
                    wrapper.passthrough(Type.STRING);
                    wrapper.write(Type.VAR_INT, 64);
                    return;
                });
                this.handler(wrapper -> {
                    wrapper.send(Protocol1_14To1_13_2.class);
                    wrapper.cancel();
                    WorldPackets.sendViewDistancePacket(wrapper.user());
                });
            }
        });
        ((Protocol<ClientboundPackets1_13, ClientboundPackets1_14, SM, SU>)protocol).registerClientbound(ClientboundPackets1_13.USE_BED, ClientboundPackets1_14.ENTITY_METADATA, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    final EntityTracker1_14 tracker = wrapper.user().getEntityTracker(Protocol1_14To1_13_2.class);
                    final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    tracker.setSleeping(entityId, true);
                    final Position position = wrapper.read(Type.POSITION);
                    final LinkedList<Metadata> metadataList = new LinkedList<Metadata>();
                    metadataList.add(new Metadata(12, Types1_14.META_TYPES.optionalPositionType, position));
                    if (tracker.clientEntityId() != entityId) {
                        metadataList.add(new Metadata(6, Types1_14.META_TYPES.poseType, MetadataRewriter1_14To1_13_2.recalculatePlayerPose(entityId, tracker)));
                    }
                    wrapper.write(Types1_14.METADATA_LIST, metadataList);
                });
            }
        });
        ((EntityRewriter<ClientboundPackets1_13, T>)metadataRewriter).registerRemoveEntities(ClientboundPackets1_13.DESTROY_ENTITIES);
        ((EntityRewriter<ClientboundPackets1_13, T>)metadataRewriter).registerMetadataRewriter(ClientboundPackets1_13.ENTITY_METADATA, Types1_13_2.METADATA_LIST, Types1_14.METADATA_LIST);
    }
}
