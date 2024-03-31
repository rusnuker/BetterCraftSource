// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.biome;

public class BiomeVoid extends Biome
{
    public BiomeVoid(final BiomeProperties properties) {
        super(properties);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.theBiomeDecorator = new BiomeVoidDecorator();
    }
    
    @Override
    public boolean ignorePlayerSpawnSuitability() {
        return true;
    }
}
