// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util.math;

import net.minecraft.entity.Entity;

public class ChunkPos
{
    public final int chunkXPos;
    public final int chunkZPos;
    private int cachedHashCode;
    
    public ChunkPos(final int x, final int z) {
        this.cachedHashCode = 0;
        this.chunkXPos = x;
        this.chunkZPos = z;
    }
    
    public ChunkPos(final BlockPos pos) {
        this.cachedHashCode = 0;
        this.chunkXPos = pos.getX() >> 4;
        this.chunkZPos = pos.getZ() >> 4;
    }
    
    public static long asLong(final int x, final int z) {
        return ((long)x & 0xFFFFFFFFL) | ((long)z & 0xFFFFFFFFL) << 32;
    }
    
    @Override
    public int hashCode() {
        if (this.cachedHashCode != 0) {
            return this.cachedHashCode;
        }
        final int i = 1664525 * this.chunkXPos + 1013904223;
        final int j = 1664525 * (this.chunkZPos ^ 0xDEADBEEF) + 1013904223;
        return this.cachedHashCode = (i ^ j);
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChunkPos)) {
            return false;
        }
        final ChunkPos chunkpos = (ChunkPos)p_equals_1_;
        return this.chunkXPos == chunkpos.chunkXPos && this.chunkZPos == chunkpos.chunkZPos;
    }
    
    public double getDistanceSq(final Entity entityIn) {
        final double d0 = this.chunkXPos * 16 + 8;
        final double d2 = this.chunkZPos * 16 + 8;
        final double d3 = d0 - entityIn.posX;
        final double d4 = d2 - entityIn.posZ;
        return d3 * d3 + d4 * d4;
    }
    
    public int getXStart() {
        return this.chunkXPos << 4;
    }
    
    public int getZStart() {
        return this.chunkZPos << 4;
    }
    
    public int getXEnd() {
        return (this.chunkXPos << 4) + 15;
    }
    
    public int getZEnd() {
        return (this.chunkZPos << 4) + 15;
    }
    
    public BlockPos getBlock(final int x, final int y, final int z) {
        return new BlockPos((this.chunkXPos << 4) + x, y, (this.chunkZPos << 4) + z);
    }
    
    @Override
    public String toString() {
        return "[" + this.chunkXPos + ", " + this.chunkZPos + "]";
    }
}
