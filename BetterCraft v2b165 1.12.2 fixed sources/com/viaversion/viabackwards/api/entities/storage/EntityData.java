// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.api.entities.storage;

import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ChatRewriter;

public class EntityData
{
    private final int id;
    private final int replacementId;
    private Object mobName;
    private MetaCreator defaultMeta;
    
    public EntityData(final int id, final int replacementId) {
        this.id = id;
        this.replacementId = replacementId;
    }
    
    public EntityData jsonName(final String name) {
        this.mobName = ChatRewriter.legacyTextToJson(name);
        return this;
    }
    
    public EntityData mobName(final String name) {
        this.mobName = name;
        return this;
    }
    
    public EntityData spawnMetadata(final MetaCreator handler) {
        this.defaultMeta = handler;
        return this;
    }
    
    public boolean hasBaseMeta() {
        return this.defaultMeta != null;
    }
    
    public int typeId() {
        return this.id;
    }
    
    public Object mobName() {
        return this.mobName;
    }
    
    public int replacementId() {
        return this.replacementId;
    }
    
    public MetaCreator defaultMeta() {
        return this.defaultMeta;
    }
    
    public boolean isObjectType() {
        return false;
    }
    
    public int objectData() {
        return -1;
    }
    
    @Override
    public String toString() {
        return "EntityData{id=" + this.id + ", mobName='" + this.mobName + '\'' + ", replacementId=" + this.replacementId + ", defaultMeta=" + this.defaultMeta + '}';
    }
    
    @FunctionalInterface
    public interface MetaCreator
    {
        void createMeta(final WrappedMetadata p0);
    }
}
