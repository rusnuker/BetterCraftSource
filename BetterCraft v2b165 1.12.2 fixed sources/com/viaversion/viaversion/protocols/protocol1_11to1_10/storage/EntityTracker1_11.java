// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_11to1_10.storage;

import com.google.common.collect.Sets;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_11Types;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.Set;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;

public class EntityTracker1_11 extends EntityTrackerBase
{
    private final Set<Integer> holograms;
    
    public EntityTracker1_11(final UserConnection user) {
        super(user, Entity1_11Types.EntityType.PLAYER);
        this.holograms = Sets.newConcurrentHashSet();
    }
    
    @Override
    public void removeEntity(final int entityId) {
        super.removeEntity(entityId);
        if (this.isHologram(entityId)) {
            this.removeHologram(entityId);
        }
    }
    
    public void addHologram(final int entId) {
        this.holograms.add(entId);
    }
    
    public boolean isHologram(final int entId) {
        return this.holograms.contains(entId);
    }
    
    public void removeHologram(final int entId) {
        this.holograms.remove(entId);
    }
}
