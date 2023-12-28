// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.data.entity;

import com.google.common.base.Preconditions;
import java.util.concurrent.ConcurrentHashMap;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.data.entity.StoredEntityData;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import java.util.Map;
import com.viaversion.viaversion.api.data.entity.ClientEntityIdChangeListener;
import com.viaversion.viaversion.api.data.entity.EntityTracker;

public class EntityTrackerBase implements EntityTracker, ClientEntityIdChangeListener
{
    private final Map<Integer, EntityType> entityTypes;
    private final Map<Integer, StoredEntityData> entityData;
    private final UserConnection connection;
    private final EntityType playerType;
    private int clientEntityId;
    private int currentWorldSectionHeight;
    private int currentMinY;
    
    public EntityTrackerBase(final UserConnection connection, final EntityType playerType) {
        this(connection, playerType, false);
    }
    
    public EntityTrackerBase(final UserConnection connection, final EntityType playerType, final boolean storesEntityData) {
        this.entityTypes = new ConcurrentHashMap<Integer, EntityType>();
        this.clientEntityId = -1;
        this.currentWorldSectionHeight = 16;
        this.connection = connection;
        this.playerType = playerType;
        this.entityData = (storesEntityData ? new ConcurrentHashMap<Integer, StoredEntityData>() : null);
    }
    
    @Override
    public UserConnection user() {
        return this.connection;
    }
    
    @Override
    public void addEntity(final int id, final EntityType type) {
        this.entityTypes.put(id, type);
    }
    
    @Override
    public boolean hasEntity(final int id) {
        return this.entityTypes.containsKey(id);
    }
    
    @Override
    public EntityType entityType(final int id) {
        return this.entityTypes.get(id);
    }
    
    @Override
    public StoredEntityData entityData(final int id) {
        final EntityType type = this.entityType(id);
        return (type != null) ? this.entityData.computeIfAbsent(id, s -> new StoredEntityImpl(type)) : null;
    }
    
    @Override
    public StoredEntityData entityDataIfPresent(final int id) {
        return this.entityData.get(id);
    }
    
    @Override
    public void removeEntity(final int id) {
        this.entityTypes.remove(id);
        if (this.entityData != null) {
            this.entityData.remove(id);
        }
    }
    
    @Override
    public int clientEntityId() {
        return this.clientEntityId;
    }
    
    @Override
    public void setClientEntityId(final int clientEntityId) {
        Preconditions.checkNotNull(this.playerType);
        this.entityTypes.put(clientEntityId, this.playerType);
        if (this.clientEntityId != -1 && this.entityData != null) {
            final StoredEntityData data = this.entityData.remove(this.clientEntityId);
            if (data != null) {
                this.entityData.put(clientEntityId, data);
            }
        }
        this.clientEntityId = clientEntityId;
    }
    
    @Override
    public int currentWorldSectionHeight() {
        return this.currentWorldSectionHeight;
    }
    
    @Override
    public void setCurrentWorldSectionHeight(final int currentWorldSectionHeight) {
        this.currentWorldSectionHeight = currentWorldSectionHeight;
    }
    
    @Override
    public int currentMinY() {
        return this.currentMinY;
    }
    
    @Override
    public void setCurrentMinY(final int currentMinY) {
        this.currentMinY = currentMinY;
    }
}
