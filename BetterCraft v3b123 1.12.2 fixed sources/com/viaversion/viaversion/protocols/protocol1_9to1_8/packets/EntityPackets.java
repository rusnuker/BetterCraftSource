// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ServerboundPackets1_9;
import java.util.Iterator;
import java.util.Map;
import com.google.common.collect.ImmutableList;
import java.util.UUID;
import com.viaversion.viaversion.util.Triple;
import java.util.ArrayList;
import com.viaversion.viaversion.util.Pair;
import java.util.HashMap;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.metadata.MetadataRewriter1_9To1_8;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import com.viaversion.viaversion.api.type.types.version.Types1_9;
import com.viaversion.viaversion.api.type.types.version.Types1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ItemRewriter;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.protocol.remapper.ValueTransformer;

public class EntityPackets
{
    public static final ValueTransformer<Byte, Short> toNewShort;
    
    public static void register(final Protocol1_9To1_8 protocol) {
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.ATTACH_ENTITY, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.UNSIGNED_BYTE, (ValueTransformer<Object, Object>)new ValueTransformer<Short, Void>(Type.NOTHING) {
                    @Override
                    public Void transform(final PacketWrapper wrapper, final Short inputValue) throws Exception {
                        final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        if (inputValue == 0) {
                            final int passenger = wrapper.get((Type<Integer>)Type.INT, 0);
                            final int vehicle = wrapper.get((Type<Integer>)Type.INT, 1);
                            wrapper.cancel();
                            final PacketWrapper passengerPacket = wrapper.create(ClientboundPackets1_9.SET_PASSENGERS);
                            if (vehicle == -1) {
                                if (!tracker.getVehicleMap().containsKey(passenger)) {
                                    return null;
                                }
                                passengerPacket.write(Type.VAR_INT, tracker.getVehicleMap().remove(passenger));
                                passengerPacket.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[0]);
                            }
                            else {
                                passengerPacket.write(Type.VAR_INT, vehicle);
                                passengerPacket.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[] { passenger });
                                tracker.getVehicleMap().put(passenger, vehicle);
                            }
                            passengerPacket.send(Protocol1_9To1_8.class);
                        }
                        return null;
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.ENTITY_TELEPORT, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map(Type.INT, SpawnPackets.toNewDouble);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BOOLEAN);
                this.handler(wrapper -> {
                    final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    if (Via.getConfig().isHologramPatch()) {
                        final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        if (tracker.getKnownHolograms().contains(entityID)) {
                            final Double newValue = wrapper.get((Type<Double>)Type.DOUBLE, 1);
                            final Double newValue2 = newValue + Via.getConfig().getHologramYOffset();
                            wrapper.set(Type.DOUBLE, 1, newValue2);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.ENTITY_POSITION_AND_ROTATION, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.BYTE, EntityPackets.toNewShort);
                this.map(Type.BYTE, EntityPackets.toNewShort);
                this.map(Type.BYTE, EntityPackets.toNewShort);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BOOLEAN);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.ENTITY_POSITION, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.BYTE, EntityPackets.toNewShort);
                this.map(Type.BYTE, EntityPackets.toNewShort);
                this.map(Type.BYTE, EntityPackets.toNewShort);
                this.map((Type<Object>)Type.BOOLEAN);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.ENTITY_EQUIPMENT, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.SHORT, (ValueTransformer<Object, Object>)new ValueTransformer<Short, Integer>(Type.VAR_INT) {
                    @Override
                    public Integer transform(final PacketWrapper wrapper, final Short slot) throws Exception {
                        final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final int receiverId = wrapper.user().getEntityTracker(Protocol1_9To1_8.class).clientEntityId();
                        if (entityId == receiverId) {
                            return slot + 2;
                        }
                        return (Integer)((slot > 0) ? (slot + 1) : slot);
                    }
                });
                this.map(Type.ITEM);
                this.handler(wrapper -> {
                    final Item stack = wrapper.get(Type.ITEM, 0);
                    ItemRewriter.toClient(stack);
                    return;
                });
                this.handler(wrapper -> {
                    final EntityTracker1_9 entityTracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final Item stack2 = wrapper.get(Type.ITEM, 0);
                    if (stack2 != null && Protocol1_9To1_8.isSword(stack2.identifier())) {
                        entityTracker.getValidBlocking().add(entityID);
                    }
                    else {
                        entityTracker.getValidBlocking().remove(entityID);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.ENTITY_METADATA, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Types1_8.METADATA_LIST, Types1_9.METADATA_LIST);
                this.handler(wrapper -> {
                    final Object val$protocol = protocol;
                    final List<Metadata> metadataList = wrapper.get(Types1_9.METADATA_LIST, 0);
                    final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    if (tracker.hasEntity(entityId)) {
                        protocol.get(MetadataRewriter1_9To1_8.class).handleMetadata(entityId, metadataList, wrapper.user());
                    }
                    else {
                        tracker.addMetadataToBuffer(entityId, metadataList);
                        wrapper.cancel();
                    }
                    return;
                });
                this.handler(wrapper -> {
                    final List<Metadata> metadataList2 = wrapper.get(Types1_9.METADATA_LIST, 0);
                    final int entityID = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final EntityTracker1_9 tracker2 = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                    tracker2.handleMetadata(entityID, metadataList2);
                    return;
                });
                this.handler(wrapper -> {
                    final List<Metadata> metadataList3 = wrapper.get(Types1_9.METADATA_LIST, 0);
                    if (metadataList3.isEmpty()) {
                        wrapper.cancel();
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.ENTITY_EFFECT, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    final boolean showParticles = wrapper.read((Type<Boolean>)Type.BOOLEAN);
                    final boolean newEffect = Via.getConfig().isNewEffectIndicator();
                    wrapper.write(Type.BYTE, (byte)(showParticles ? (newEffect ? 2 : 1) : 0));
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).cancelClientbound(ClientboundPackets1_8.UPDATE_ENTITY_NBT);
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.COMBAT_EVENT, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    if (wrapper.get((Type<Integer>)Type.VAR_INT, 0) == 2) {
                        wrapper.passthrough((Type<Object>)Type.VAR_INT);
                        wrapper.passthrough((Type<Object>)Type.INT);
                        Protocol1_9To1_8.FIX_JSON.write(wrapper, wrapper.read(Type.STRING));
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.ENTITY_PROPERTIES, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    if (!(!Via.getConfig().isMinimizeCooldown())) {
                        final EntityTracker1_9 tracker = wrapper.user().getEntityTracker(Protocol1_9To1_8.class);
                        if (wrapper.get((Type<Integer>)Type.VAR_INT, 0) == tracker.getProvidedEntityId()) {
                            final int propertiesToRead = wrapper.read((Type<Integer>)Type.INT);
                            final HashMap<String, Pair> properties = new HashMap<String, Pair>(propertiesToRead);
                            for (int i = 0; i < propertiesToRead; ++i) {
                                final String key = wrapper.read(Type.STRING);
                                final Double value = wrapper.read((Type<Double>)Type.DOUBLE);
                                final int modifiersToRead = wrapper.read((Type<Integer>)Type.VAR_INT);
                                final ArrayList<Triple<UUID, Double, Byte>> modifiers = new ArrayList<Triple<UUID, Double, Byte>>(modifiersToRead);
                                for (int j = 0; j < modifiersToRead; ++j) {
                                    modifiers.add(new Triple<UUID, Double, Byte>(wrapper.read(Type.UUID), wrapper.read((Type<Double>)Type.DOUBLE), wrapper.read((Type<Byte>)Type.BYTE)));
                                }
                                properties.put(key, new Pair<Double, List<Triple<UUID, Double, Byte>>>(value, modifiers));
                            }
                            new Pair(15.9, (ImmutableList<Object>)ImmutableList.of(new Triple(UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3"), 0.0, 0), new Triple(UUID.fromString("AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF3"), 0.0, 2), new Triple(UUID.fromString("55FCED67-E92A-486E-9800-B47F202C4386"), 0.0, 2)));
                            final Pair pair;
                            final Object o;
                            final String s;
                            ((Map<String, Pair>)o).put(s, pair);
                            wrapper.write(Type.INT, properties.size());
                            properties.entrySet().iterator();
                            final Iterator iterator;
                            while (iterator.hasNext()) {
                                final Map.Entry<String, Pair<Double, List<Triple<UUID, Double, Byte>>>> entry = iterator.next();
                                wrapper.write(Type.STRING, entry.getKey());
                                wrapper.write((Type<Object>)Type.DOUBLE, ((Pair<T, Y>)entry.getValue()).key());
                                wrapper.write(Type.VAR_INT, ((Pair<X, List>)entry.getValue()).value().size());
                                ((Pair<X, List>)entry.getValue()).value().iterator();
                                final Iterator iterator2;
                                while (iterator2.hasNext()) {
                                    final Triple<UUID, Double, Byte> modifier = iterator2.next();
                                    wrapper.write(Type.UUID, modifier.first());
                                    wrapper.write(Type.DOUBLE, modifier.second());
                                    wrapper.write(Type.BYTE, modifier.third());
                                }
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, CM, SM, SU>)protocol).registerClientbound(ClientboundPackets1_8.ENTITY_ANIMATION, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.handler(wrapper -> {
                    if (wrapper.get((Type<Short>)Type.UNSIGNED_BYTE, 0) == 3) {
                        wrapper.cancel();
                    }
                });
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_9>)protocol).registerServerbound(ServerboundPackets1_9.ENTITY_ACTION, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    final int action = wrapper.get((Type<Integer>)Type.VAR_INT, 1);
                    if (action == 6 || action == 8) {
                        wrapper.cancel();
                    }
                    if (action == 7) {
                        wrapper.set(Type.VAR_INT, 1, 6);
                    }
                });
            }
        });
        ((AbstractProtocol<CU, CM, SM, ServerboundPackets1_9>)protocol).registerServerbound(ServerboundPackets1_9.INTERACT_ENTITY, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    final int type = wrapper.get((Type<Integer>)Type.VAR_INT, 1);
                    if (type == 2) {
                        wrapper.passthrough((Type<Object>)Type.FLOAT);
                        wrapper.passthrough((Type<Object>)Type.FLOAT);
                        wrapper.passthrough((Type<Object>)Type.FLOAT);
                    }
                    if (type == 0 || type == 2) {
                        final int hand = wrapper.read((Type<Integer>)Type.VAR_INT);
                        if (hand == 1) {
                            wrapper.cancel();
                        }
                    }
                });
            }
        });
    }
    
    static {
        toNewShort = new ValueTransformer<Byte, Short>(Type.SHORT) {
            @Override
            public Short transform(final PacketWrapper wrapper, final Byte inputValue) {
                return (short)(inputValue * 128);
            }
        };
    }
}
