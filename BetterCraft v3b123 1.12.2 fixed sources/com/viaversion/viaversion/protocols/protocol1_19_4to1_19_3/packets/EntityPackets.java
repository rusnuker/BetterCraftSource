// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.api.type.types.version.Types1_19_4;
import com.viaversion.viaversion.api.type.types.version.Types1_19_3;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_19_4Types;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ClientboundPackets1_19_4;
import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.storage.PlayerVehicleTracker;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.Protocol1_19_4To1_19_3;
import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.ClientboundPackets1_19_3;
import com.viaversion.viaversion.rewriter.EntityRewriter;

public final class EntityPackets extends EntityRewriter<ClientboundPackets1_19_3, Protocol1_19_4To1_19_3>
{
    public EntityPackets(final Protocol1_19_4To1_19_3 protocol) {
        super(protocol);
    }
    
    public void registerPackets() {
        ((AbstractProtocol<ClientboundPackets1_19_3, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_3.JOIN_GAME, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map(Type.STRING_ARRAY);
                this.map(Type.NBT);
                this.map(Type.STRING);
                this.map(Type.STRING);
                this.handler(EntityPackets.this.dimensionDataHandler());
                this.handler(EntityPackets.this.biomeSizeTracker());
                this.handler(EntityPackets.this.worldDataTrackerHandlerByKey());
                this.handler(EntityPackets.this.playerTrackerHandler());
                this.handler(wrapper -> {
                    final CompoundTag registry = wrapper.get(Type.NBT, 0);
                    final CompoundTag damageTypeRegistry = ((Protocol1_19_4To1_19_3)EntityPackets.this.protocol).getMappingData().damageTypesRegistry();
                    registry.put("minecraft:damage_type", damageTypeRegistry);
                    final CompoundTag biomeRegistry = registry.get("minecraft:worldgen/biome");
                    final ListTag biomes = biomeRegistry.get("value");
                    biomes.iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final Tag biomeTag = iterator.next();
                        final CompoundTag biomeData = ((CompoundTag)biomeTag).get("element");
                        final StringTag precipitation = biomeData.get("precipitation");
                        final byte precipitationByte = (byte)(precipitation.getValue().equals("none") ? 0 : 1);
                        biomeData.put("has_precipitation", new ByteTag(precipitationByte));
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19_3, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_3.PLAYER_POSITION, new PacketHandlers() {
            @Override
            protected void register() {
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    if (wrapper.read((Type<Boolean>)Type.BOOLEAN)) {
                        final PlayerVehicleTracker playerVehicleTracker = wrapper.user().get(PlayerVehicleTracker.class);
                        if (playerVehicleTracker.getVehicleId() != -1) {
                            final PacketWrapper bundleStart = wrapper.create(ClientboundPackets1_19_4.BUNDLE);
                            bundleStart.send(Protocol1_19_4To1_19_3.class);
                            final PacketWrapper setPassengers = wrapper.create(ClientboundPackets1_19_4.SET_PASSENGERS);
                            setPassengers.write(Type.VAR_INT, playerVehicleTracker.getVehicleId());
                            setPassengers.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[0]);
                            setPassengers.send(Protocol1_19_4To1_19_3.class);
                            wrapper.send(Protocol1_19_4To1_19_3.class);
                            wrapper.cancel();
                            final PacketWrapper bundleEnd = wrapper.create(ClientboundPackets1_19_4.BUNDLE);
                            bundleEnd.send(Protocol1_19_4To1_19_3.class);
                            playerVehicleTracker.setVehicleId(-1);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19_3, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_3.SET_PASSENGERS, new PacketHandlers() {
            @Override
            protected void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.VAR_INT_ARRAY_PRIMITIVE);
                this.handler(wrapper -> {
                    final PlayerVehicleTracker playerVehicleTracker = wrapper.user().get(PlayerVehicleTracker.class);
                    final int clientEntityId = wrapper.user().getEntityTracker(Protocol1_19_4To1_19_3.class).clientEntityId();
                    final int vehicleId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    if (playerVehicleTracker.getVehicleId() == vehicleId) {
                        playerVehicleTracker.setVehicleId(-1);
                    }
                    final int[] array;
                    final int[] passengerIds = array = wrapper.get(Type.VAR_INT_ARRAY_PRIMITIVE, 0);
                    final int length = array.length;
                    int i = 0;
                    while (i < length) {
                        final int passengerId = array[i];
                        if (passengerId == clientEntityId) {
                            playerVehicleTracker.setVehicleId(vehicleId);
                            break;
                        }
                        else {
                            ++i;
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19_3, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_3.ENTITY_TELEPORT, new PacketHandlers() {
            @Override
            protected void register() {
                this.handler(wrapper -> {
                    final int entityId = wrapper.read((Type<Integer>)Type.VAR_INT);
                    final int clientEntityId = wrapper.user().getEntityTracker(Protocol1_19_4To1_19_3.class).clientEntityId();
                    if (entityId != clientEntityId) {
                        wrapper.write(Type.VAR_INT, entityId);
                    }
                    else {
                        wrapper.setPacketType(ClientboundPackets1_19_4.PLAYER_POSITION);
                        wrapper.passthrough((Type<Object>)Type.DOUBLE);
                        wrapper.passthrough((Type<Object>)Type.DOUBLE);
                        wrapper.passthrough((Type<Object>)Type.DOUBLE);
                        wrapper.write(Type.FLOAT, wrapper.read((Type<Byte>)Type.BYTE) * 360.0f / 256.0f);
                        wrapper.write(Type.FLOAT, wrapper.read((Type<Byte>)Type.BYTE) * 360.0f / 256.0f);
                        wrapper.read((Type<Object>)Type.BOOLEAN);
                        wrapper.write(Type.BYTE, (Byte)0);
                        wrapper.write(Type.VAR_INT, -1);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19_3, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_3.ENTITY_ANIMATION, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    final short action = wrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                    if (action != 1) {
                        wrapper.write(Type.UNSIGNED_BYTE, action);
                    }
                    else {
                        wrapper.setPacketType(ClientboundPackets1_19_4.HIT_ANIMATION);
                        wrapper.write(Type.FLOAT, 0.0f);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19_3, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_3.RESPAWN, new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.map(Type.STRING);
                this.handler(EntityPackets.this.worldDataTrackerHandlerByKey());
                this.handler(wrapper -> wrapper.user().put(new PlayerVehicleTracker(wrapper.user())));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19_3, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_3.ENTITY_STATUS, wrapper -> {
            final int entityId = wrapper.read((Type<Integer>)Type.INT);
            final byte event = wrapper.read((Type<Byte>)Type.BYTE);
            final int damageType = this.damageTypeFromEntityEvent(event);
            if (damageType != -1) {
                wrapper.setPacketType(ClientboundPackets1_19_4.DAMAGE_EVENT);
                wrapper.write(Type.VAR_INT, entityId);
                wrapper.write(Type.VAR_INT, damageType);
                wrapper.write(Type.VAR_INT, 0);
                wrapper.write(Type.VAR_INT, 0);
                wrapper.write(Type.BOOLEAN, false);
                return;
            }
            else {
                wrapper.write(Type.INT, entityId);
                wrapper.write(Type.BYTE, event);
                return;
            }
        });
        ((EntityRewriter<ClientboundPackets1_19_3, T>)this).registerTrackerWithData1_19(ClientboundPackets1_19_3.SPAWN_ENTITY, Entity1_19_4Types.FALLING_BLOCK);
        ((EntityRewriter<ClientboundPackets1_19_3, T>)this).registerRemoveEntities(ClientboundPackets1_19_3.REMOVE_ENTITIES);
        ((EntityRewriter<ClientboundPackets1_19_3, T>)this).registerMetadataRewriter(ClientboundPackets1_19_3.ENTITY_METADATA, Types1_19_3.METADATA_LIST, Types1_19_4.METADATA_LIST);
    }
    
    private int damageTypeFromEntityEvent(final byte entityEvent) {
        switch (entityEvent) {
            case 33: {
                return 36;
            }
            case 36: {
                return 5;
            }
            case 37: {
                return 27;
            }
            case 57: {
                return 15;
            }
            case 2:
            case 44: {
                return 16;
            }
            default: {
                return -1;
            }
        }
    }
    
    @Override
    protected void registerRewrites() {
        this.filter().handler((event, meta) -> {
            int id = meta.metaType().typeId();
            if (id >= 14) {
                ++id;
            }
            meta.setMetaType(Types1_19_4.META_TYPES.byId(id));
            return;
        });
        this.registerMetaTypeHandler(Types1_19_4.META_TYPES.itemType, Types1_19_4.META_TYPES.blockStateType, Types1_19_4.META_TYPES.optionalBlockStateType, Types1_19_4.META_TYPES.particleType);
        this.filter().filterFamily(Entity1_19_4Types.MINECART_ABSTRACT).index(11).handler((event, meta) -> {
            final int blockState = meta.value();
            meta.setValue(((Protocol1_19_4To1_19_3)this.protocol).getMappingData().getNewBlockStateId(blockState));
            return;
        });
        this.filter().filterFamily(Entity1_19_4Types.BOAT).index(11).handler((event, meta) -> {
            final int boatType = meta.value();
            if (boatType > 4) {
                meta.setValue(boatType + 1);
            }
            return;
        });
        this.filter().filterFamily(Entity1_19_4Types.ABSTRACT_HORSE).removeIndex(18);
    }
    
    @Override
    public void onMappingDataLoaded() {
        this.mapTypes();
    }
    
    @Override
    public EntityType typeFromId(final int type) {
        return Entity1_19_4Types.getTypeFromId(type);
    }
}
