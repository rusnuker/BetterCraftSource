// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.data.entity;

import com.viaversion.viaversion.api.minecraft.entities.EntityType;

public interface StoredEntityData
{
    EntityType type();
    
    boolean has(final Class<?> p0);
    
     <T> T get(final Class<T> p0);
    
     <T> T remove(final Class<T> p0);
    
    void put(final Object p0);
}
