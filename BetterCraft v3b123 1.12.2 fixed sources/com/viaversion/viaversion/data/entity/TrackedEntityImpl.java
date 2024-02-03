// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.data.entity;

import com.viaversion.viaversion.api.data.entity.StoredEntityData;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.data.entity.TrackedEntity;

public final class TrackedEntityImpl implements TrackedEntity
{
    private final EntityType entityType;
    private StoredEntityData data;
    private boolean sentMetadata;
    
    public TrackedEntityImpl(final EntityType entityType) {
        this.entityType = entityType;
    }
    
    @Override
    public EntityType entityType() {
        return this.entityType;
    }
    
    @Override
    public StoredEntityData data() {
        if (this.data == null) {
            this.data = new StoredEntityDataImpl(this.entityType);
        }
        return this.data;
    }
    
    @Override
    public boolean hasData() {
        return this.data != null;
    }
    
    @Override
    public boolean hasSentMetadata() {
        return this.sentMetadata;
    }
    
    @Override
    public void sentMetadata(final boolean sentMetadata) {
        this.sentMetadata = sentMetadata;
    }
}
