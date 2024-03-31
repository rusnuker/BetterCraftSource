// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_19_3to1_19_4.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viabackwards.api.entities.storage.WrappedMetadata;
import com.viaversion.viabackwards.api.entities.storage.EntityData;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.ClientboundPackets1_19_3;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.types.version.Types1_19_4;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_19_4Types;
import com.viaversion.viaversion.api.type.types.version.Types1_19_3;
import com.viaversion.viabackwards.protocol.protocol1_19_3to1_19_4.Protocol1_19_3To1_19_4;
import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ClientboundPackets1_19_4;
import com.viaversion.viabackwards.api.rewriters.EntityRewriter;

public final class EntityPackets1_19_4 extends EntityRewriter<ClientboundPackets1_19_4, Protocol1_19_3To1_19_4>
{
    public EntityPackets1_19_4(final Protocol1_19_3To1_19_4 protocol) {
        super(protocol, Types1_19_3.META_TYPES.optionalComponentType, Types1_19_3.META_TYPES.booleanType);
    }
    
    public void registerPackets() {
        ((EntityRewriter<ClientboundPackets1_19_4, T>)this).registerTrackerWithData1_19(ClientboundPackets1_19_4.SPAWN_ENTITY, Entity1_19_4Types.FALLING_BLOCK);
        ((com.viaversion.viaversion.rewriter.EntityRewriter<ClientboundPackets1_19_4, T>)this).registerRemoveEntities(ClientboundPackets1_19_4.REMOVE_ENTITIES);
        ((com.viaversion.viaversion.rewriter.EntityRewriter<ClientboundPackets1_19_4, T>)this).registerMetadataRewriter(ClientboundPackets1_19_4.ENTITY_METADATA, Types1_19_4.METADATA_LIST, Types1_19_3.METADATA_LIST);
        ((AbstractProtocol<ClientboundPackets1_19_4, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_4.JOIN_GAME, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.INT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map(Type.STRING_ARRAY);
                this.map(Type.NBT);
                this.map(Type.STRING);
                this.map(Type.STRING);
                this.handler(EntityPackets1_19_4.this.dimensionDataHandler());
                this.handler(EntityPackets1_19_4.this.biomeSizeTracker());
                this.handler(EntityPackets1_19_4.this.worldDataTrackerHandlerByKey());
                this.handler(wrapper -> {
                    final CompoundTag registry = wrapper.get(Type.NBT, 0);
                    registry.remove("minecraft:trim_pattern");
                    registry.remove("minecraft:trim_material");
                    registry.remove("minecraft:damage_type");
                    final CompoundTag biomeRegistry = registry.get("minecraft:worldgen/biome");
                    final ListTag biomes = biomeRegistry.get("value");
                    biomes.iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final Tag biomeTag = iterator.next();
                        final CompoundTag biomeData = ((CompoundTag)biomeTag).get("element");
                        final ByteTag hasPrecipitation = biomeData.get("has_precipitation");
                        new StringTag((hasPrecipitation.asByte() == 1) ? "rain" : "none");
                        final StringTag tag;
                        final Object o;
                        final String tagName;
                        ((CompoundTag)o).put(tagName, tag);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19_4, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_4.PLAYER_POSITION, new PacketHandlers() {
            @Override
            protected void register() {
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.FLOAT);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.VAR_INT);
                this.create(Type.BOOLEAN, false);
            }
        });
        this.protocol.registerClientbound(ClientboundPackets1_19_4.DAMAGE_EVENT, ClientboundPackets1_19_3.ENTITY_STATUS, new PacketHandlers() {
            public void register() {
                this.map(Type.VAR_INT, Type.INT);
                this.read(Type.VAR_INT);
                this.read(Type.VAR_INT);
                this.read(Type.VAR_INT);
                this.handler(wrapper -> {
                    if (wrapper.read((Type<Boolean>)Type.BOOLEAN)) {
                        wrapper.read((Type<Object>)Type.DOUBLE);
                        wrapper.read((Type<Object>)Type.DOUBLE);
                        wrapper.read((Type<Object>)Type.DOUBLE);
                    }
                    return;
                });
                this.create(Type.BYTE, (Byte)2);
            }
        });
        this.protocol.registerClientbound(ClientboundPackets1_19_4.HIT_ANIMATION, ClientboundPackets1_19_3.ENTITY_ANIMATION, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.read(Type.FLOAT);
                this.create(Type.UNSIGNED_BYTE, (Short)1);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19_4, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_4.RESPAWN, new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.map(Type.STRING);
                this.handler(EntityPackets1_19_4.this.worldDataTrackerHandlerByKey());
            }
        });
    }
    
    public void registerRewrites() {
        this.filter().handler((event, meta) -> {
            int id = meta.metaType().typeId();
            if (id >= 25) {
                return;
            }
            else {
                if (id >= 15) {
                    --id;
                }
                meta.setMetaType(Types1_19_3.META_TYPES.byId(id));
                return;
            }
        });
        this.registerMetaTypeHandler(Types1_19_3.META_TYPES.itemType, Types1_19_3.META_TYPES.blockStateType, null, Types1_19_3.META_TYPES.particleType, Types1_19_3.META_TYPES.componentType, Types1_19_3.META_TYPES.optionalComponentType);
        this.filter().filterFamily(Entity1_19_4Types.MINECART_ABSTRACT).index(11).handler((event, meta) -> {
            final int blockState = meta.value();
            meta.setValue(((Protocol1_19_3To1_19_4)this.protocol).getMappingData().getNewBlockStateId(blockState));
            return;
        });
        this.filter().filterFamily(Entity1_19_4Types.BOAT).index(11).handler((event, meta) -> {
            final int boatType = meta.value();
            if (boatType > 4) {
                meta.setValue(boatType - 1);
            }
            return;
        });
        this.filter().type(Entity1_19_4Types.TEXT_DISPLAY).index(22).handler((event, meta) -> {
            event.setIndex(2);
            meta.setMetaType(Types1_19_3.META_TYPES.optionalComponentType);
            event.createExtraMeta(new Metadata(3, Types1_19_3.META_TYPES.booleanType, true));
            final JsonElement element = meta.value();
            ((Protocol1_19_3To1_19_4)this.protocol).getTranslatableRewriter().processText(element);
            return;
        });
        this.filter().filterFamily(Entity1_19_4Types.DISPLAY).handler((event, meta) -> {
            if (event.index() > 7) {
                event.cancel();
            }
            return;
        });
        this.filter().type(Entity1_19_4Types.INTERACTION).removeIndex(8);
        this.filter().type(Entity1_19_4Types.INTERACTION).removeIndex(9);
        this.filter().type(Entity1_19_4Types.INTERACTION).removeIndex(10);
        this.filter().type(Entity1_19_4Types.SNIFFER).removeIndex(17);
        this.filter().type(Entity1_19_4Types.SNIFFER).removeIndex(18);
        this.filter().filterFamily(Entity1_19_4Types.ABSTRACT_HORSE).addIndex(18);
    }
    
    @Override
    public void onMappingDataLoaded() {
        this.mapTypes();
        final EntityData.MetaCreator displayMetaCreator = storage -> {
            storage.add(new Metadata(0, Types1_19_3.META_TYPES.byteType, 32));
            storage.add(new Metadata(5, Types1_19_3.META_TYPES.booleanType, true));
            storage.add(new Metadata(15, Types1_19_3.META_TYPES.byteType, 17));
            return;
        };
        this.mapEntityTypeWithData(Entity1_19_4Types.TEXT_DISPLAY, Entity1_19_4Types.ARMOR_STAND).spawnMetadata(displayMetaCreator);
        this.mapEntityTypeWithData(Entity1_19_4Types.ITEM_DISPLAY, Entity1_19_4Types.ARMOR_STAND).spawnMetadata(displayMetaCreator);
        this.mapEntityTypeWithData(Entity1_19_4Types.BLOCK_DISPLAY, Entity1_19_4Types.ARMOR_STAND).spawnMetadata(displayMetaCreator);
        this.mapEntityTypeWithData(Entity1_19_4Types.INTERACTION, Entity1_19_4Types.ARMOR_STAND).spawnMetadata(displayMetaCreator);
        this.mapEntityTypeWithData(Entity1_19_4Types.SNIFFER, Entity1_19_4Types.RAVAGER).jsonName();
    }
    
    @Override
    public EntityType typeFromId(final int type) {
        return Entity1_19_4Types.getTypeFromId(type);
    }
}
