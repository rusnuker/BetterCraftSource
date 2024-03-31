// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_14to1_14_1.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import com.viaversion.viaversion.api.type.types.version.Types1_14;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_14Types;
import com.viaversion.viabackwards.protocol.protocol1_14to1_14_1.Protocol1_14To1_14_1;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viabackwards.api.rewriters.LegacyEntityRewriter;

public class EntityPackets1_14_1 extends LegacyEntityRewriter<ClientboundPackets1_14, Protocol1_14To1_14_1>
{
    public EntityPackets1_14_1(final Protocol1_14To1_14_1 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((EntityRewriter<ClientboundPackets1_14, T>)this).registerTracker(ClientboundPackets1_14.SPAWN_EXPERIENCE_ORB, Entity1_14Types.EXPERIENCE_ORB);
        ((EntityRewriter<ClientboundPackets1_14, T>)this).registerTracker(ClientboundPackets1_14.SPAWN_GLOBAL_ENTITY, Entity1_14Types.LIGHTNING_BOLT);
        ((EntityRewriter<ClientboundPackets1_14, T>)this).registerTracker(ClientboundPackets1_14.SPAWN_PAINTING, Entity1_14Types.PAINTING);
        ((EntityRewriter<ClientboundPackets1_14, T>)this).registerTracker(ClientboundPackets1_14.SPAWN_PLAYER, Entity1_14Types.PLAYER);
        ((EntityRewriter<ClientboundPackets1_14, T>)this).registerTracker(ClientboundPackets1_14.JOIN_GAME, Entity1_14Types.PLAYER, Type.INT);
        ((EntityRewriter<ClientboundPackets1_14, T>)this).registerRemoveEntities(ClientboundPackets1_14.DESTROY_ENTITIES);
        ((AbstractProtocol<ClientboundPackets1_14, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_14.SPAWN_ENTITY, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(EntityRewriterBase.this.getTrackerHandler());
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_14.SPAWN_MOB, new PacketHandlers() {
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
                this.map(Types1_14.METADATA_LIST);
                this.handler(wrapper -> {
                    final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final int type = wrapper.get((Type<Integer>)Type.VAR_INT, 1);
                    EntityPackets1_14_1.this.tracker(wrapper.user()).addEntity(entityId, Entity1_14Types.getTypeFromId(type));
                    final List<Metadata> metadata = wrapper.get(Types1_14.METADATA_LIST, 0);
                    EntityPackets1_14_1.this.handleMetadata(entityId, metadata, wrapper.user());
                });
            }
        });
        ((LegacyEntityRewriter<ClientboundPackets1_14, T>)this).registerMetadataRewriter(ClientboundPackets1_14.ENTITY_METADATA, Types1_14.METADATA_LIST);
    }
    
    @Override
    protected void registerRewrites() {
        this.filter().type(Entity1_14Types.VILLAGER).cancel(15);
        this.filter().type(Entity1_14Types.VILLAGER).index(16).toIndex(15);
        this.filter().type(Entity1_14Types.WANDERING_TRADER).cancel(15);
    }
    
    @Override
    public EntityType typeFromId(final int typeId) {
        return Entity1_14Types.getTypeFromId(typeId);
    }
}
