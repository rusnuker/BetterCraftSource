// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.minecraft.chunks;

import java.util.ArrayList;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.List;

public class Chunk1_8 extends BaseChunk
{
    private boolean unloadPacket;
    
    public Chunk1_8(final int x, final int z, final boolean groundUp, final int bitmask, final ChunkSection[] sections, final int[] biomeData, final List<CompoundTag> blockEntities) {
        super(x, z, groundUp, false, bitmask, sections, biomeData, blockEntities);
    }
    
    public Chunk1_8(final int x, final int z) {
        this(x, z, true, 0, new ChunkSection[16], null, new ArrayList<CompoundTag>());
        this.unloadPacket = true;
    }
    
    public boolean hasBiomeData() {
        return this.biomeData != null && this.fullChunk;
    }
    
    public boolean isUnloadPacket() {
        return this.unloadPacket;
    }
}
