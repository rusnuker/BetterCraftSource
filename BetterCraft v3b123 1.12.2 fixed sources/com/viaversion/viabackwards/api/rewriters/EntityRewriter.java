// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.api.rewriters;

import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.type.types.version.Types1_14;
import com.viaversion.viabackwards.api.BackwardsProtocol;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public abstract class EntityRewriter<C extends ClientboundPacketType, T extends BackwardsProtocol<C, ?, ?, ?>> extends EntityRewriterBase<C, T>
{
    protected EntityRewriter(final T protocol) {
        this(protocol, Types1_14.META_TYPES.optionalComponentType, Types1_14.META_TYPES.booleanType);
    }
    
    protected EntityRewriter(final T protocol, final MetaType displayType, final MetaType displayVisibilityType) {
        super(protocol, displayType, 2, displayVisibilityType, 3);
    }
    
    @Override
    public void registerTrackerWithData(final C packetType, final EntityType fallingBlockType) {
        ((BackwardsProtocol)this.protocol).registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.DOUBLE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.BYTE);
                this.map((Type<Object>)Type.INT);
                this.handler(EntityRewriter.this.getSpawnTrackerWithDataHandler(fallingBlockType));
            }
        });
    }
    
    @Override
    public void registerTrackerWithData1_19(final C packetType, final EntityType fallingBlockType) {
        ((BackwardsProtocol)this.protocol).registerClientbound(packetType, new PacketHandlers() {
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
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(EntityRewriter.this.getSpawnTrackerWithDataHandler1_19(fallingBlockType));
            }
        });
    }
    
    public PacketHandler getSpawnTrackerWithDataHandler(final EntityType fallingBlockType) {
        return wrapper -> {
            final EntityType entityType = this.trackAndMapEntity(wrapper);
            if (entityType == fallingBlockType) {
                final int blockState = wrapper.get((Type<Integer>)Type.INT, 0);
                wrapper.set(Type.INT, 0, ((BackwardsProtocol)this.protocol).getMappingData().getNewBlockStateId(blockState));
            }
        };
    }
    
    public PacketHandler getSpawnTrackerWithDataHandler1_19(final EntityType fallingBlockType) {
        return wrapper -> {
            final EntityType entityType = this.trackAndMapEntity(wrapper);
            if (entityType == fallingBlockType) {
                final int blockState = wrapper.get((Type<Integer>)Type.VAR_INT, 2);
                wrapper.set(Type.VAR_INT, 2, ((BackwardsProtocol)this.protocol).getMappingData().getNewBlockStateId(blockState));
            }
        };
    }
    
    public void registerSpawnTracker(final C packetType) {
        ((BackwardsProtocol)this.protocol).registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> EntityRewriter.this.trackAndMapEntity(wrapper));
            }
        });
    }
    
    public PacketHandler worldTrackerHandlerByKey() {
        return wrapper -> {
            final EntityTracker tracker = this.tracker(wrapper.user());
            final String world = wrapper.get(Type.STRING, 1);
            if (tracker.currentWorld() != null && !tracker.currentWorld().equals(world)) {
                tracker.clearEntities();
                tracker.trackClientEntity();
            }
            tracker.setCurrentWorld(world);
        };
    }
    
    protected EntityType trackAndMapEntity(final PacketWrapper wrapper) throws Exception {
        final int typeId = wrapper.get((Type<Integer>)Type.VAR_INT, 1);
        final EntityType entityType = this.typeFromId(typeId);
        this.tracker(wrapper.user()).addEntity(wrapper.get((Type<Integer>)Type.VAR_INT, 0), entityType);
        final int mappedTypeId = this.newEntityId(entityType.getId());
        if (typeId != mappedTypeId) {
            wrapper.set(Type.VAR_INT, 1, mappedTypeId);
        }
        return entityType;
    }
}
