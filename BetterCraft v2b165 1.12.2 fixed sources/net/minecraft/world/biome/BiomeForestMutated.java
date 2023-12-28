// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.biome;

import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.Random;

public class BiomeForestMutated extends BiomeForest
{
    public BiomeForestMutated(final BiomeProperties properties) {
        super(Type.BIRCH, properties);
    }
    
    @Override
    public WorldGenAbstractTree genBigTreeChance(final Random rand) {
        return rand.nextBoolean() ? BiomeForest.SUPER_BIRCH_TREE : BiomeForest.BIRCH_TREE;
    }
}
