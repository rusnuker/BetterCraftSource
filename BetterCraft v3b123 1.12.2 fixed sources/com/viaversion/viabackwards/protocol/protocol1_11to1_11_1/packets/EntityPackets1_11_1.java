// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_11to1_11_1.packets;

import com.viaversion.viaversion.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import java.util.function.Function;
import com.viaversion.viaversion.api.type.types.version.Types1_9;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_11Types;
import com.viaversion.viaversion.api.minecraft.entities.ObjectType;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viabackwards.protocol.protocol1_11to1_11_1.Protocol1_11To1_11_1;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import com.viaversion.viabackwards.api.rewriters.LegacyEntityRewriter;

public class EntityPackets1_11_1 extends LegacyEntityRewriter<ClientboundPackets1_9_3, Protocol1_11To1_11_1>
{
    public EntityPackets1_11_1(final Protocol1_11To1_11_1 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((AbstractProtocol<ClientboundPackets1_9_3, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_9_3.SPAWN_ENTITY, new PacketHandlers() {
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
                this.handler(LegacyEntityRewriter.this.getObjectTrackerHandler());
                this.handler(LegacyEntityRewriter.this.getObjectRewriter(id -> Entity1_11Types.ObjectType.findById(id).orElse(null)));
            }
        });
        ((EntityRewriter<ClientboundPackets1_9_3, T>)this).registerTracker(ClientboundPackets1_9_3.SPAWN_EXPERIENCE_ORB, Entity1_11Types.EntityType.EXPERIENCE_ORB);
        ((EntityRewriter<ClientboundPackets1_9_3, T>)this).registerTracker(ClientboundPackets1_9_3.SPAWN_GLOBAL_ENTITY, Entity1_11Types.EntityType.WEATHER);
        ((AbstractProtocol<ClientboundPackets1_9_3, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_9_3.SPAWN_MOB, new PacketHandlers() {
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
                this.map(Types1_9.METADATA_LIST);
                this.handler(EntityRewriterBase.this.getTrackerHandler());
                this.handler(LegacyEntityRewriter.this.getMobSpawnRewriter(Types1_9.METADATA_LIST));
            }
        });
        ((EntityRewriter<ClientboundPackets1_9_3, T>)this).registerTracker(ClientboundPackets1_9_3.SPAWN_PAINTING, Entity1_11Types.EntityType.PAINTING);
        ((LegacyEntityRewriter<ClientboundPackets1_9_3, T>)this).registerJoinGame(ClientboundPackets1_9_3.JOIN_GAME, Entity1_11Types.EntityType.PLAYER);
        ((LegacyEntityRewriter<ClientboundPackets1_9_3, T>)this).registerRespawn(ClientboundPackets1_9_3.RESPAWN);
        ((AbstractProtocol<ClientboundPackets1_9_3, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_9_3.SPAWN_PLAYER, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map(Types1_9.METADATA_LIST);
                this.handler(LegacyEntityRewriter.this.getTrackerAndMetaHandler(Types1_9.METADATA_LIST, Entity1_11Types.EntityType.PLAYER));
            }
        });
        ((EntityRewriter<ClientboundPackets1_9_3, T>)this).registerRemoveEntities(ClientboundPackets1_9_3.DESTROY_ENTITIES);
        ((LegacyEntityRewriter<ClientboundPackets1_9_3, T>)this).registerMetadataRewriter(ClientboundPackets1_9_3.ENTITY_METADATA, Types1_9.METADATA_LIST);
    }
    
    @Override
    protected void registerRewrites() {
        this.filter().type(Entity1_11Types.EntityType.FIREWORK).cancel(7);
        this.filter().type(Entity1_11Types.EntityType.PIG).cancel(14);
    }
    
    @Override
    public EntityType typeFromId(final int typeId) {
        return Entity1_11Types.getTypeFromId(typeId, false);
    }
    
    @Override
    protected EntityType getObjectTypeFromId(final int typeId) {
        return Entity1_11Types.getTypeFromId(typeId, true);
    }
}
