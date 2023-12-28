// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.api.entities.storage;

public class EntityObjectData extends EntityData
{
    private final boolean isObject;
    private final int objectData;
    
    public EntityObjectData(final int id, final boolean isObject, final int replacementId, final int objectData) {
        super(id, replacementId);
        this.isObject = isObject;
        this.objectData = objectData;
    }
    
    @Override
    public boolean isObjectType() {
        return this.isObject;
    }
    
    @Override
    public int objectData() {
        return this.objectData;
    }
}
