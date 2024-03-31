// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.data;

import java.util.Arrays;

public class IntArrayMappings implements Mappings
{
    private final int[] mappings;
    private final int mappedIds;
    
    protected IntArrayMappings(final int[] mappings, final int mappedIds) {
        this.mappings = mappings;
        this.mappedIds = mappedIds;
    }
    
    public static IntArrayMappings of(final int[] mappings, final int mappedIds) {
        return new IntArrayMappings(mappings, mappedIds);
    }
    
    @Deprecated
    public static Builder<IntArrayMappings> builder() {
        return Mappings.builder(IntArrayMappings::new);
    }
    
    @Override
    public int getNewId(final int id) {
        return (id >= 0 && id < this.mappings.length) ? this.mappings[id] : -1;
    }
    
    @Override
    public void setNewId(final int id, final int mappedId) {
        this.mappings[id] = mappedId;
    }
    
    @Override
    public int size() {
        return this.mappings.length;
    }
    
    @Override
    public int mappedSize() {
        return this.mappedIds;
    }
    
    @Override
    public Mappings inverse() {
        final int[] inverse = new int[this.mappedIds];
        Arrays.fill(inverse, -1);
        for (int id = 0; id < this.mappings.length; ++id) {
            final int mappedId = this.mappings[id];
            if (mappedId != -1 && inverse[mappedId] == -1) {
                inverse[mappedId] = id;
            }
        }
        return of(inverse, this.mappings.length);
    }
    
    public int[] raw() {
        return this.mappings;
    }
}
