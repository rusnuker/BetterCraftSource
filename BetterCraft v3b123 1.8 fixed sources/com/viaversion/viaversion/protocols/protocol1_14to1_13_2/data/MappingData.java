// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_14to1_13_2.data;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import com.viaversion.viaversion.api.data.MappingDataBase;

public class MappingData extends MappingDataBase
{
    private IntSet motionBlocking;
    private IntSet nonFullBlocks;
    
    public MappingData() {
        super("1.13.2", "1.14");
    }
    
    public void loadExtras(final CompoundTag data) {
        final CompoundTag heightmap = MappingDataLoader.loadNBT("heightmap-1.14.nbt");
        final IntArrayTag motionBlocking = heightmap.get("motionBlocking");
        this.motionBlocking = new IntOpenHashSet(motionBlocking.getValue());
        if (Via.getConfig().isNonFullBlockLightFix()) {
            final IntArrayTag nonFullBlocks = heightmap.get("nonFullBlocks");
            this.nonFullBlocks = new IntOpenHashSet(nonFullBlocks.getValue());
        }
    }
    
    public IntSet getMotionBlocking() {
        return this.motionBlocking;
    }
    
    public IntSet getNonFullBlocks() {
        return this.nonFullBlocks;
    }
}
