// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.data;

public class BiMappingsBase implements BiMappings
{
    protected final Mappings mappings;
    private final BiMappingsBase inverse;
    
    protected BiMappingsBase(final Mappings mappings, final Mappings inverse) {
        this.mappings = mappings;
        this.inverse = new BiMappingsBase(inverse, this);
    }
    
    private BiMappingsBase(final Mappings mappings, final BiMappingsBase inverse) {
        this.mappings = mappings;
        this.inverse = inverse;
    }
    
    @Override
    public int getNewId(final int id) {
        return this.mappings.getNewId(id);
    }
    
    @Override
    public void setNewId(final int id, final int mappedId) {
        this.mappings.setNewId(id, mappedId);
        this.inverse.mappings.setNewId(mappedId, id);
    }
    
    @Override
    public int size() {
        return this.mappings.size();
    }
    
    @Override
    public int mappedSize() {
        return this.mappings.mappedSize();
    }
    
    @Override
    public BiMappings inverse() {
        return this.inverse;
    }
}
