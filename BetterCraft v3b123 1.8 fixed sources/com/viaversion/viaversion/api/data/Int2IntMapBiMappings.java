// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.data;

import com.viaversion.viaversion.util.Int2IntBiMap;

public class Int2IntMapBiMappings implements BiMappings
{
    private final Int2IntBiMap mappings;
    private final Int2IntMapBiMappings inverse;
    
    protected Int2IntMapBiMappings(final Int2IntBiMap mappings) {
        this.mappings = mappings;
        this.inverse = new Int2IntMapBiMappings(mappings.inverse(), this);
        mappings.defaultReturnValue(-1);
    }
    
    private Int2IntMapBiMappings(final Int2IntBiMap mappings, final Int2IntMapBiMappings inverse) {
        this.mappings = mappings;
        this.inverse = inverse;
    }
    
    public static Int2IntMapBiMappings of(final Int2IntBiMap mappings) {
        return new Int2IntMapBiMappings(mappings);
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
        return this.mappings.inverse().size();
    }
    
    @Override
    public BiMappings inverse() {
        return this.inverse;
    }
}
