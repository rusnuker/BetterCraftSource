// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.data;

import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;

public class Int2IntMapMappings implements Mappings
{
    private final Int2IntMap mappings;
    private final int mappedIds;
    
    protected Int2IntMapMappings(final Int2IntMap mappings, final int mappedIds) {
        this.mappings = mappings;
        this.mappedIds = mappedIds;
        mappings.defaultReturnValue(-1);
    }
    
    public static Int2IntMapMappings of(final Int2IntMap mappings, final int mappedIds) {
        return new Int2IntMapMappings(mappings, mappedIds);
    }
    
    public static Int2IntMapMappings of() {
        return new Int2IntMapMappings(new Int2IntOpenHashMap(), -1);
    }
    
    @Override
    public int getNewId(final int id) {
        return this.mappings.get(id);
    }
    
    @Override
    public void setNewId(final int id, final int mappedId) {
        this.mappings.put(id, mappedId);
    }
    
    @Override
    public int size() {
        return this.mappings.size();
    }
    
    @Override
    public int mappedSize() {
        return this.mappedIds;
    }
    
    @Override
    public Mappings inverse() {
        final Int2IntMap inverse = new Int2IntOpenHashMap();
        inverse.defaultReturnValue(-1);
        for (final Int2IntMap.Entry entry : this.mappings.int2IntEntrySet()) {
            if (entry.getIntValue() != -1) {
                inverse.putIfAbsent(entry.getIntValue(), entry.getIntKey());
            }
        }
        return of(inverse, this.size());
    }
}
