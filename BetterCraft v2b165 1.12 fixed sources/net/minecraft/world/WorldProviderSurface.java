// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world;

public class WorldProviderSurface extends WorldProvider
{
    @Override
    public DimensionType getDimensionType() {
        return DimensionType.OVERWORLD;
    }
    
    @Override
    public boolean canDropChunk(final int x, final int z) {
        return !this.worldObj.isSpawnChunk(x, z);
    }
}
