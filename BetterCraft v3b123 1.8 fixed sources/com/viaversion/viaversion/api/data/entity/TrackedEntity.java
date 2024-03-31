// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.data.entity;

import com.viaversion.viaversion.api.minecraft.entities.EntityType;

public interface TrackedEntity
{
    EntityType entityType();
    
    StoredEntityData data();
    
    boolean hasData();
    
    boolean hasSentMetadata();
    
    void sentMetadata(final boolean p0);
}
