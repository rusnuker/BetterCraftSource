// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.data.entity;

import com.google.common.base.Preconditions;
import com.viaversion.viaversion.api.data.entity.StoredEntityData;
import java.util.Collections;
import com.viaversion.viaversion.libs.flare.fastutil.Int2ObjectSyncMap;
import com.viaversion.viaversion.api.data.entity.DimensionData;
import java.util.Map;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.data.entity.TrackedEntity;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.api.data.entity.ClientEntityIdChangeListener;
import com.viaversion.viaversion.api.data.entity.EntityTracker;

public class EntityTrackerBase implements EntityTracker, ClientEntityIdChangeListener
{
    private final Int2ObjectMap<TrackedEntity> entities;
    private final UserConnection connection;
    private final EntityType playerType;
    private int clientEntityId;
    private int currentWorldSectionHeight;
    private int currentMinY;
    private String currentWorld;
    private int biomesSent;
    private Map<String, DimensionData> dimensions;
    
    public EntityTrackerBase(final UserConnection connection, final EntityType playerType) {
        this.entities = (Int2ObjectMap<TrackedEntity>)Int2ObjectSyncMap.hashmap();
        this.clientEntityId = -1;
        this.currentWorldSectionHeight = 16;
        this.biomesSent = -1;
        this.dimensions = Collections.emptyMap();
        this.connection = connection;
        this.playerType = playerType;
    }
    
    @Override
    public UserConnection user() {
        return this.connection;
    }
    
    @Override
    public void addEntity(final int id, final EntityType type) {
        this.entities.put(id, new TrackedEntityImpl(type));
    }
    
    @Override
    public boolean hasEntity(final int id) {
        return this.entities.containsKey(id);
    }
    
    @Override
    public TrackedEntity entity(final int entityId) {
        return this.entities.get(entityId);
    }
    
    @Override
    public EntityType entityType(final int id) {
        final TrackedEntity entity = this.entities.get(id);
        return (entity != null) ? entity.entityType() : null;
    }
    
    @Override
    public StoredEntityData entityData(final int id) {
        final TrackedEntity entity = this.entities.get(id);
        return (entity != null) ? entity.data() : null;
    }
    
    @Override
    public StoredEntityData entityDataIfPresent(final int id) {
        final TrackedEntity entity = this.entities.get(id);
        return (entity != null && entity.hasData()) ? entity.data() : null;
    }
    
    @Override
    public void removeEntity(final int id) {
        this.entities.remove(id);
    }
    
    @Override
    public void clearEntities() {
        this.entities.clear();
    }
    
    @Override
    public int clientEntityId() {
        return this.clientEntityId;
    }
    
    @Override
    public void setClientEntityId(final int clientEntityId) {
        Preconditions.checkNotNull(this.playerType);
        final TrackedEntity oldEntity;
        if (this.clientEntityId != -1 && (oldEntity = this.entities.remove(this.clientEntityId)) != null) {
            this.entities.put(clientEntityId, oldEntity);
        }
        else {
            this.entities.put(clientEntityId, new TrackedEntityImpl(this.playerType));
        }
        this.clientEntityId = clientEntityId;
    }
    
    @Override
    public boolean trackClientEntity() {
        if (this.clientEntityId != -1) {
            this.entities.put(this.clientEntityId, new TrackedEntityImpl(this.playerType));
            return true;
        }
        return false;
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
    
    @Override
    public String currentWorld() {
        return this.currentWorld;
    }
    
    @Override
    public void setCurrentWorld(final String currentWorld) {
        this.currentWorld = currentWorld;
    }
    
    @Override
    public int biomesSent() {
        return this.biomesSent;
    }
    
    @Override
    public void setBiomesSent(final int biomesSent) {
        this.biomesSent = biomesSent;
    }
    
    @Override
    public EntityType playerType() {
        return this.playerType;
    }
    
    @Override
    public DimensionData dimensionData(final String dimension) {
        return this.dimensions.get(dimension);
    }
    
    @Override
    public void setDimensions(final Map<String, DimensionData> dimensions) {
        this.dimensions = dimensions;
    }
}
