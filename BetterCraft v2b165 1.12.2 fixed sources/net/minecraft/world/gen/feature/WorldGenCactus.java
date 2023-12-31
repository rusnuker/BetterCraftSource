// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.gen.feature;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class WorldGenCactus extends WorldGenerator
{
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        for (int i = 0; i < 10; ++i) {
            final BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (worldIn.isAirBlock(blockpos)) {
                for (int j = 1 + rand.nextInt(rand.nextInt(3) + 1), k = 0; k < j; ++k) {
                    if (Blocks.CACTUS.canBlockStay(worldIn, blockpos)) {
                        worldIn.setBlockState(blockpos.up(k), Blocks.CACTUS.getDefaultState(), 2);
                    }
                }
            }
        }
        return true;
    }
}
