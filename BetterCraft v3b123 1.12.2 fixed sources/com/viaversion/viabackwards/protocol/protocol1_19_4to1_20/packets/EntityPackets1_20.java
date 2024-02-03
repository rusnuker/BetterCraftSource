// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_19_4to1_20.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.Iterator;
import com.viaversion.viaversion.util.Key;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.types.version.Types1_19_4;
import com.viaversion.viaversion.api.type.types.version.Types1_20;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_19_4Types;
import com.google.common.collect.Sets;
import java.util.Set;
import com.viaversion.viabackwards.protocol.protocol1_19_4to1_20.Protocol1_19_4To1_20;
import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.ClientboundPackets1_19_4;
import com.viaversion.viabackwards.api.rewriters.EntityRewriter;

public final class EntityPackets1_20 extends EntityRewriter<ClientboundPackets1_19_4, Protocol1_19_4To1_20>
{
    private final Set<String> newTrimPatterns;
    
    public EntityPackets1_20(final Protocol1_19_4To1_20 protocol) {
        super(protocol);
        this.newTrimPatterns = Sets.newHashSet("host_armor_trim_smithing_template", "raiser_armor_trim_smithing_template", "silence_armor_trim_smithing_template", "shaper_armor_trim_smithing_template", "wayfinder_armor_trim_smithing_template");
    }
    
    public void registerPackets() {
        ((EntityRewriter<ClientboundPackets1_19_4, T>)this).registerTrackerWithData1_19(ClientboundPackets1_19_4.SPAWN_ENTITY, Entity1_19_4Types.FALLING_BLOCK);
        ((com.viaversion.viaversion.rewriter.EntityRewriter<ClientboundPackets1_19_4, T>)this).registerMetadataRewriter(ClientboundPackets1_19_4.ENTITY_METADATA, Types1_20.METADATA_LIST, Types1_19_4.METADATA_LIST);
        ((com.viaversion.viaversion.rewriter.EntityRewriter<ClientboundPackets1_19_4, T>)this).registerRemoveEntities(ClientboundPackets1_19_4.REMOVE_ENTITIES);
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
                this.map((Type<Object>)Type.LONG);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map(Type.OPTIONAL_GLOBAL_POSITION);
                this.read(Type.VAR_INT);
                this.handler(EntityPackets1_20.this.dimensionDataHandler());
                this.handler(EntityPackets1_20.this.biomeSizeTracker());
                this.handler(EntityPackets1_20.this.worldDataTrackerHandlerByKey());
                this.handler(wrapper -> {
                    final CompoundTag registry = wrapper.get(Type.NBT, 0);
                    final ListTag values = registry.get("minecraft:trim_pattern").get("value");
                    values.iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final Tag entry = iterator.next();
                        final CompoundTag element = ((CompoundTag)entry).get("element");
                        final StringTag templateItem = element.get("template_item");
                        if (EntityPackets1_20.this.newTrimPatterns.contains(Key.stripMinecraftNamespace(templateItem.getValue()))) {
                            templateItem.setValue("minecraft:spire_armor_trim_smithing_template");
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19_4, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_19_4.RESPAWN, new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.map(Type.STRING);
                this.map((Type<Object>)Type.LONG);
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map((Type<Object>)Type.BOOLEAN);
                this.map((Type<Object>)Type.BYTE);
                this.map(Type.OPTIONAL_GLOBAL_POSITION);
                this.read(Type.VAR_INT);
                this.handler(EntityPackets1_20.this.worldDataTrackerHandlerByKey());
            }
        });
    }
    
    @Override
    protected void registerRewrites() {
        this.filter().handler((event, meta) -> meta.setMetaType(Types1_19_4.META_TYPES.byId(meta.metaType().typeId())));
        this.registerMetaTypeHandler(Types1_19_4.META_TYPES.itemType, Types1_19_4.META_TYPES.blockStateType, Types1_19_4.META_TYPES.optionalBlockStateType, Types1_19_4.META_TYPES.particleType, Types1_19_4.META_TYPES.componentType, Types1_19_4.META_TYPES.optionalComponentType);
        this.filter().filterFamily(Entity1_19_4Types.MINECART_ABSTRACT).index(11).handler((event, meta) -> {
            final int blockState = meta.value();
            meta.setValue(((Protocol1_19_4To1_20)this.protocol).getMappingData().getNewBlockStateId(blockState));
        });
    }
    
    @Override
    public EntityType typeFromId(final int type) {
        return Entity1_19_4Types.getTypeFromId(type);
    }
}
