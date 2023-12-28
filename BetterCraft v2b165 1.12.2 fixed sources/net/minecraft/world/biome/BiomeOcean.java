// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.biome;

public class BiomeOcean extends Biome
{
    public BiomeOcean(final BiomeProperties properties) {
        super(properties);
        this.spawnableCreatureList.clear();
    }
    
    @Override
    public TempCategory getTempCategory() {
        return TempCategory.OCEAN;
    }
}
