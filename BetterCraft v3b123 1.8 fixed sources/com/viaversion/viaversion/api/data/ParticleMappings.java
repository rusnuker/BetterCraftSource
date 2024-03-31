// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.data;

import com.viaversion.viaversion.libs.fastutil.ints.IntArrayList;
import java.util.List;
import com.viaversion.viaversion.libs.fastutil.ints.IntList;

public class ParticleMappings extends FullMappingsBase
{
    private final IntList itemParticleIds;
    private final IntList blockParticleIds;
    
    public ParticleMappings(final List<String> unmappedIdentifiers, final List<String> mappedIdentifiers, final Mappings mappings) {
        super(unmappedIdentifiers, mappedIdentifiers, mappings);
        this.itemParticleIds = new IntArrayList(4);
        this.blockParticleIds = new IntArrayList(4);
        this.addBlockParticle("block");
        this.addBlockParticle("falling_dust");
        this.addBlockParticle("block_marker");
        this.addItemParticle("item");
    }
    
    public boolean addItemParticle(final String identifier) {
        final int id = this.id(identifier);
        return id != -1 && this.itemParticleIds.add(id);
    }
    
    public boolean addBlockParticle(final String identifier) {
        final int id = this.id(identifier);
        return id != -1 && this.blockParticleIds.add(id);
    }
    
    public boolean isBlockParticle(final int id) {
        return this.blockParticleIds.contains(id);
    }
    
    public boolean isItemParticle(final int id) {
        return this.itemParticleIds.contains(id);
    }
    
    @Deprecated
    public int getBlockId() {
        return this.id("block");
    }
    
    @Deprecated
    public int getFallingDustId() {
        return this.id("falling_dust");
    }
    
    @Deprecated
    public int getItemId() {
        return this.id("item");
    }
}
