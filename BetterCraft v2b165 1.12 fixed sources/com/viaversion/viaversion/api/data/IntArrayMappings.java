// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.data;

import com.viaversion.viaversion.libs.gson.JsonArray;
import java.util.Arrays;
import com.viaversion.viaversion.libs.gson.JsonObject;

public class IntArrayMappings implements Mappings
{
    protected final int[] oldToNew;
    
    public IntArrayMappings(final int[] oldToNew) {
        this.oldToNew = oldToNew;
    }
    
    public IntArrayMappings(final int size, final JsonObject oldMapping, final JsonObject newMapping, final JsonObject diffMapping) {
        Arrays.fill(this.oldToNew = new int[size], -1);
        MappingDataLoader.mapIdentifiers(this.oldToNew, oldMapping, newMapping, diffMapping);
    }
    
    public IntArrayMappings(final JsonObject oldMapping, final JsonObject newMapping, final JsonObject diffMapping) {
        this(oldMapping.entrySet().size(), oldMapping, newMapping, diffMapping);
    }
    
    public IntArrayMappings(final int size, final JsonObject oldMapping, final JsonObject newMapping) {
        Arrays.fill(this.oldToNew = new int[size], -1);
        MappingDataLoader.mapIdentifiers(this.oldToNew, oldMapping, newMapping);
    }
    
    public IntArrayMappings(final JsonObject oldMapping, final JsonObject newMapping) {
        this(oldMapping.entrySet().size(), oldMapping, newMapping);
    }
    
    public IntArrayMappings(final int size, final JsonArray oldMapping, final JsonArray newMapping, final JsonObject diffMapping, final boolean warnOnMissing) {
        Arrays.fill(this.oldToNew = new int[size], -1);
        MappingDataLoader.mapIdentifiers(this.oldToNew, oldMapping, newMapping, diffMapping, warnOnMissing);
    }
    
    public IntArrayMappings(final int size, final JsonArray oldMapping, final JsonArray newMapping, final boolean warnOnMissing) {
        this(size, oldMapping, newMapping, null, warnOnMissing);
    }
    
    public IntArrayMappings(final JsonArray oldMapping, final JsonArray newMapping, final boolean warnOnMissing) {
        this(oldMapping.size(), oldMapping, newMapping, warnOnMissing);
    }
    
    public IntArrayMappings(final int size, final JsonArray oldMapping, final JsonArray newMapping) {
        this(size, oldMapping, newMapping, true);
    }
    
    public IntArrayMappings(final JsonArray oldMapping, final JsonArray newMapping, final JsonObject diffMapping) {
        this(oldMapping.size(), oldMapping, newMapping, diffMapping, true);
    }
    
    public IntArrayMappings(final JsonArray oldMapping, final JsonArray newMapping) {
        this(oldMapping.size(), oldMapping, newMapping, true);
    }
    
    @Override
    public int getNewId(final int id) {
        return (id >= 0 && id < this.oldToNew.length) ? this.oldToNew[id] : -1;
    }
    
    @Override
    public void setNewId(final int id, final int newId) {
        this.oldToNew[id] = newId;
    }
    
    public int[] getOldToNew() {
        return this.oldToNew;
    }
}
