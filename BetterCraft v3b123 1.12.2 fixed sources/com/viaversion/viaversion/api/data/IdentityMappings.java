// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.data;

public class IdentityMappings implements Mappings
{
    private final int size;
    private final int mappedSize;
    
    public IdentityMappings(final int size, final int mappedSize) {
        this.size = size;
        this.mappedSize = mappedSize;
    }
    
    @Override
    public int getNewId(final int id) {
        return (id >= 0 && id < this.size) ? id : -1;
    }
    
    @Override
    public void setNewId(final int id, final int mappedId) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public int mappedSize() {
        return this.mappedSize;
    }
    
    @Override
    public Mappings inverse() {
        return new IdentityMappings(this.mappedSize, this.size);
    }
}
