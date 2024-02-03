// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_13to1_13_1.packets;

import com.viaversion.viaversion.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.type.types.Particle;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import com.viaversion.viaversion.api.type.types.version.Types1_13;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_13Types;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viabackwards.protocol.protocol1_13to1_13_1.Protocol1_13To1_13_1;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viabackwards.api.rewriters.LegacyEntityRewriter;

public class EntityPackets1_13_1 extends LegacyEntityRewriter<ClientboundPackets1_13, Protocol1_13To1_13_1>
{
    public EntityPackets1_13_1(final Protocol1_13To1_13_1 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.SPAWN_ENTITY, new PacketHandlers() {
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
                    if (entType == null) {
                        ViaBackwards.getPlatform().getLogger().warning("Could not find 1.13 entity type " + type);
                    }
                    else {
                        if (entType.is(Entity1_13Types.EntityType.FALLING_BLOCK)) {
                            final int data = wrapper.get((Type<Integer>)Type.INT, 0);
                            wrapper.set(Type.INT, 0, ((Protocol1_13To1_13_1)EntityPackets1_13_1.this.protocol).getMappingData().getNewBlockStateId(data));
                        }
                        EntityPackets1_13_1.this.tracker(wrapper.user()).addEntity(entityId, entType);
                    }
                });
            }
        });
        ((EntityRewriter<ClientboundPackets1_13, T>)this).registerTracker(ClientboundPackets1_13.SPAWN_EXPERIENCE_ORB, Entity1_13Types.EntityType.EXPERIENCE_ORB);
        ((EntityRewriter<ClientboundPackets1_13, T>)this).registerTracker(ClientboundPackets1_13.SPAWN_GLOBAL_ENTITY, Entity1_13Types.EntityType.LIGHTNING_BOLT);
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.SPAWN_MOB, new PacketHandlers() {
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
                this.map(Types1_13.METADATA_LIST);
                this.handler(EntityRewriterBase.this.getTrackerHandler());
                this.handler(wrapper -> {
                    final List<Metadata> metadata = wrapper.get(Types1_13.METADATA_LIST, 0);
                    EntityPackets1_13_1.this.handleMetadata(wrapper.get((Type<Integer>)Type.VAR_INT, 0), metadata, wrapper.user());
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.SPAWN_PLAYER, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map(Types1_13.METADATA_LIST);
                this.handler(LegacyEntityRewriter.this.getTrackerAndMetaHandler(Types1_13.METADATA_LIST, Entity1_13Types.EntityType.PLAYER));
            }
        });
        ((EntityRewriter<ClientboundPackets1_13, T>)this).registerTracker(ClientboundPackets1_13.SPAWN_PAINTING, Entity1_13Types.EntityType.PAINTING);
        ((LegacyEntityRewriter<ClientboundPackets1_13, T>)this).registerJoinGame(ClientboundPackets1_13.JOIN_GAME, Entity1_13Types.EntityType.PLAYER);
        ((LegacyEntityRewriter<ClientboundPackets1_13, T>)this).registerRespawn(ClientboundPackets1_13.RESPAWN);
        ((EntityRewriter<ClientboundPackets1_13, T>)this).registerRemoveEntities(ClientboundPackets1_13.DESTROY_ENTITIES);
        ((LegacyEntityRewriter<ClientboundPackets1_13, T>)this).registerMetadataRewriter(ClientboundPackets1_13.ENTITY_METADATA, Types1_13.METADATA_LIST);
    }
    
    @Override
    protected void registerRewrites() {
        this.filter().handler((event, meta) -> {
            if (meta.metaType() == Types1_13.META_TYPES.itemType) {
                ((Protocol1_13To1_13_1)this.protocol).getItemRewriter().handleItemToClient((Item)meta.getValue());
            }
            else if (meta.metaType() == Types1_13.META_TYPES.blockStateType) {
                final int data = (int)meta.getValue();
                meta.setValue(((Protocol1_13To1_13_1)this.protocol).getMappingData().getNewBlockStateId(data));
            }
            else if (meta.metaType() == Types1_13.META_TYPES.particleType) {
                this.rewriteParticle((Particle)meta.getValue());
            }
            else if (meta.metaType() == Types1_13.META_TYPES.optionalComponentType || meta.metaType() == Types1_13.META_TYPES.componentType) {
                final JsonElement element = meta.value();
                ((Protocol1_13To1_13_1)this.protocol).translatableRewriter().processText(element);
            }
            return;
        });
        this.filter().filterFamily(Entity1_13Types.EntityType.ABSTRACT_ARROW).cancel(7);
        this.filter().type(Entity1_13Types.EntityType.SPECTRAL_ARROW).index(8).toIndex(7);
        this.filter().type(Entity1_13Types.EntityType.TRIDENT).index(8).toIndex(7);
        this.filter().filterFamily(Entity1_13Types.EntityType.MINECART_ABSTRACT).index(9).handler((event, meta) -> {
            final int data2 = (int)meta.getValue();
            meta.setValue(((Protocol1_13To1_13_1)this.protocol).getMappingData().getNewBlockStateId(data2));
        });
    }
    
    @Override
    public EntityType typeFromId(final int typeId) {
        return Entity1_13Types.getTypeFromId(typeId, false);
    }
    
    @Override
    protected EntityType getObjectTypeFromId(final int typeId) {
        return Entity1_13Types.getTypeFromId(typeId, true);
    }
}
