// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.metadata.MetadataRewriter1_9To1_8;
import com.viaversion.viaversion.api.type.types.version.Types1_8;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import java.util.List;
import com.viaversion.viaversion.api.type.types.version.Types1_9;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.minecraft.metadata.types.MetaType1_9;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ItemRewriter;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.ArrayList;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_10Types;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.protocol.remapper.ValueTransformer;

public class SpawnPackets
{
    public static final ValueTransformer<Integer, Double> toNewDouble;
    
    public static void register(final Protocol1_9To1_8 protocol) {
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_ENTITY, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    wrapper.write(Type.UUID, tracker.getEntityUUID(entityID));
                    return;
                });
                this.map((Type<Object>)Type.BYTE);
                this.handler(wrapper -> {
                    final int entityID2 = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final int typeID = wrapper.get((Type<Byte>)Type.BYTE, 0);
                    final EntityTracker1_9 tracker2 = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    tracker2.addEntity(entityID2, Entity1_10Types.getTypeFromId(typeID, true));
                    tracker2.sendMetadataBuffer(entityID2);
                    return;
                });
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> {
                    final int data = wrapper.get((Type<Integer>)Type.INT, 0);
                    short vX = 0;
                    short vY = 0;
                    short vZ = 0;
                    if (data > 0) {
                        vX = wrapper.read((Type<Short>)Type.SHORT);
                        vY = wrapper.read((Type<Short>)Type.SHORT);
                        vZ = wrapper.read((Type<Short>)Type.SHORT);
                    }
                    wrapper.write(Type.SHORT, vX);
                    wrapper.write(Type.SHORT, vY);
                    wrapper.write(Type.SHORT, vZ);
                    return;
                });
                this.handler(wrapper -> {
                    final int entityID3 = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final int data2 = wrapper.get((Type<Integer>)Type.INT, 0);
                    final int typeID2 = wrapper.get((Type<Byte>)Type.BYTE, 0);
                    if (Entity1_10Types.getTypeFromId(typeID2, true) == Entity1_10Types.EntityType.SPLASH_POTION) {
                        final PacketWrapper metaPacket = wrapper.create(ClientboundPackets1_9.ENTITY_METADATA, wrapper1 -> {
                            wrapper1.write(Type.VAR_INT, entityID);
                            final ArrayList<Metadata> meta = new ArrayList<Metadata>();
                            final Item item = new DataItem(373, (byte)1, (short)data, null);
                            ItemRewriter.toClient(item);
                            final Metadata potion = new Metadata(5, MetaType1_9.Slot, item);
                            meta.add(potion);
                            wrapper1.write(Types1_9.METADATA_LIST, meta);
                            return;
                        });
                        wrapper.send(Protocol1_9To1_8.class);
                        metaPacket.send(Protocol1_9To1_8.class);
                        wrapper.cancel();
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_EXPERIENCE_ORB, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    tracker.addEntity(entityID, Entity1_10Types.EntityType.EXPERIENCE_ORB);
                    tracker.sendMetadataBuffer(entityID);
                    return;
                });
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map((Type<Object>)Type.SHORT);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_GLOBAL_ENTITY, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.BYTE);
                this.handler(wrapper -> {
                    final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    tracker.addEntity(entityID, Entity1_10Types.EntityType.LIGHTNING);
                    tracker.sendMetadataBuffer(entityID);
                    return;
                });
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_MOB, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    wrapper.write(Type.UUID, tracker.getEntityUUID(entityID));
                    return;
                });
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.handler(wrapper -> {
                    final int entityID2 = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final int typeID = wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0);
                    final EntityTracker1_9 tracker2 = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    tracker2.addEntity(entityID2, Entity1_10Types.getTypeFromId(typeID, false));
                    tracker2.sendMetadataBuffer(entityID2);
                    return;
                });
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.SHORT);
                this.map((Type<Object>)Type.SHORT);
                this.map(Types1_8.METADATA_LIST, Types1_9.METADATA_LIST);
                this.handler(wrapper -> {
                    final Object val$protocol = protocol;
                    final List<Metadata> metadataList = wrapper.get(Types1_9.METADATA_LIST, 0);
                    final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final EntityTracker1_9 tracker3 = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    if (tracker3.hasEntity(entityId)) {
                        protocol.get(MetadataRewriter1_9To1_8.class).handleMetadata(entityId, metadataList, wrapper.user());
                    }
                    else {
                        Via.getPlatform().getLogger().warning("Unable to find entity for metadata, entity ID: " + entityId);
                        metadataList.clear();
                    }
                    return;
                });
                this.handler(wrapper -> {
                    final List<Metadata> metadataList2 = wrapper.get(Types1_9.METADATA_LIST, 0);
                    final int entityID3 = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final EntityTracker1_9 tracker4 = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    tracker4.handleMetadata(entityID3, metadataList2);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_PAINTING, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    tracker.addEntity(entityID, Entity1_10Types.EntityType.PAINTING);
                    tracker.sendMetadataBuffer(entityID);
                    return;
                });
                this.handler(wrapper -> {
                    final int entityID2 = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final EntityTracker1_9 tracker2 = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    wrapper.write(Type.UUID, tracker2.getEntityUUID(entityID2));
                    return;
                });
                this.map(Type.STRING);
                this.map(Type.POSITION);
                this.map((Type<Object>)Type.BYTE);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.SPAWN_PLAYER, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.handler(wrapper -> {
                    final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    tracker.addEntity(entityID, Entity1_10Types.EntityType.PLAYER);
                    tracker.sendMetadataBuffer(entityID);
                    return;
                });
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.handler(wrapper -> {
                    final short item = wrapper.read((Type<Short>)Type.SHORT);
                    if (item != 0) {
                        final PacketWrapper packet = PacketWrapper.create(ClientboundPackets1_9.ENTITY_EQUIPMENT, null, wrapper.user());
                        packet.write((Type<Object>)Type.VAR_INT, wrapper.get((Type<T>)Type.VAR_INT, 0));
                        packet.write(Type.VAR_INT, 0);
                        packet.write((Type<DataItem>)Type.ITEM, new DataItem(item, (byte)1, (short)0, null));
                        try {
                            packet.send(Protocol1_9To1_8.class);
                        }
                        catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return;
                });
                this.map(Types1_8.METADATA_LIST, Types1_9.METADATA_LIST);
                this.handler(wrapper -> {
                    final Object val$protocol = protocol;
                    final List<Metadata> metadataList = wrapper.get(Types1_9.METADATA_LIST, 0);
                    final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final EntityTracker1_9 tracker2 = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    if (tracker2.hasEntity(entityId)) {
                        protocol.get(MetadataRewriter1_9To1_8.class).handleMetadata(entityId, metadataList, wrapper.user());
                    }
                    else {
                        Via.getPlatform().getLogger().warning("Unable to find entity for metadata, entity ID: " + entityId);
                        metadataList.clear();
                    }
                    return;
                });
                this.handler(wrapper -> {
                    final List<Metadata> metadataList2 = wrapper.get(Types1_9.METADATA_LIST, 0);
                    final int entityID2 = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final EntityTracker1_9 tracker3 = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    tracker3.handleMetadata(entityID2, metadataList2);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.DESTROY_ENTITIES, new PacketHandlers() {
            public void register() {
                this.map(Type.VAR_INT_ARRAY_PRIMITIVE);
                this.handler(wrapper -> {
                    final int[] entities = wrapper.get(Type.VAR_INT_ARRAY_PRIMITIVE, 0);
                    final EntityTracker tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    final int[] array;
                    int i = 0;
                    for (int length = array.length; i < length; ++i) {
                        final int entity = array[i];
                        tracker.removeEntity(entity);
                    }
                });
            }
        });
    }
    
    static {
        toNewDouble = new ValueTransformer<Integer, Double>(Type.DOUBLE) {
            @Override
            public Double transform(final PacketWrapper wrapper, final Integer inputValue) {
                return inputValue / 32.0;
            }
        };
    }
}
