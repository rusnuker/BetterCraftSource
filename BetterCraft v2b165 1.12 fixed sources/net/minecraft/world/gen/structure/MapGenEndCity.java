// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.gen.structure;

import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Random;
import net.minecraft.world.gen.ChunkGeneratorEnd;

public class MapGenEndCity extends MapGenStructure
{
    private final int citySpacing = 20;
    private final int minCitySeparation = 11;
    private final ChunkGeneratorEnd endProvider;
    
    public MapGenEndCity(final ChunkGeneratorEnd p_i46665_1_) {
        this.endProvider = p_i46665_1_;
    }
    
    @Override
    public String getStructureName() {
        return "EndCity";
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        final int i = chunkX;
        final int j = chunkZ;
        if (chunkX < 0) {
            chunkX -= 19;
        }
        if (chunkZ < 0) {
            chunkZ -= 19;
        }
        int k = chunkX / 20;
        int l = chunkZ / 20;
        final Random random = this.worldObj.setRandomSeed(k, l, 10387313);
        k *= 20;
        l *= 20;
        k += (random.nextInt(9) + random.nextInt(9)) / 2;
        l += (random.nextInt(9) + random.nextInt(9)) / 2;
        if (i == k && j == l && this.endProvider.isIslandChunk(i, j)) {
            final int i2 = func_191070_b(i, j, this.endProvider);
            return i2 >= 60;
        }
        return false;
    }
    
    @Override
    protected StructureStart getStructureStart(final int chunkX, final int chunkZ) {
        return new Start(this.worldObj, this.endProvider, this.rand, chunkX, chunkZ);
    }
    
    @Override
    public BlockPos getClosestStrongholdPos(final World worldIn, final BlockPos pos, final boolean p_180706_3_) {
        this.worldObj = worldIn;
        return MapGenStructure.func_191069_a(worldIn, this, pos, 20, 11, 10387313, true, 100, p_180706_3_);
    }
    
    private static int func_191070_b(final int p_191070_0_, final int p_191070_1_, final ChunkGeneratorEnd p_191070_2_) {
        final Random random = new Random(p_191070_0_ + p_191070_1_ * 10387313);
        final Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
        final ChunkPrimer chunkprimer = new ChunkPrimer();
        p_191070_2_.setBlocksInChunk(p_191070_0_, p_191070_1_, chunkprimer);
        int i = 5;
        int j = 5;
        if (rotation == Rotation.CLOCKWISE_90) {
            i = -5;
        }
        else if (rotation == Rotation.CLOCKWISE_180) {
            i = -5;
            j = -5;
        }
        else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
            j = -5;
        }
        final int k = chunkprimer.findGroundBlockIdx(7, 7);
        final int l = chunkprimer.findGroundBlockIdx(7, 7 + j);
        final int i2 = chunkprimer.findGroundBlockIdx(7 + i, 7);
        final int j2 = chunkprimer.findGroundBlockIdx(7 + i, 7 + j);
        final int k2 = Math.min(Math.min(k, l), Math.min(i2, j2));
        return k2;
    }
    
    public static class Start extends StructureStart
    {
        private boolean isSizeable;
        
        public Start() {
        }
        
        public Start(final World worldIn, final ChunkGeneratorEnd chunkProvider, final Random random, final int chunkX, final int chunkZ) {
            super(chunkX, chunkZ);
            this.create(worldIn, chunkProvider, random, chunkX, chunkZ);
        }
        
        private void create(final World worldIn, final ChunkGeneratorEnd chunkProvider, final Random rnd, final int chunkX, final int chunkZ) {
            final Random random = new Random(chunkX + chunkZ * 10387313);
            final Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
            final int i = func_191070_b(chunkX, chunkZ, chunkProvider);
            if (i < 60) {
                this.isSizeable = false;
            }
            else {
                final BlockPos blockpos = new BlockPos(chunkX * 16 + 8, i, chunkZ * 16 + 8);
                StructureEndCityPieces.func_191087_a(worldIn.getSaveHandler().getStructureTemplateManager(), blockpos, rotation, this.components, rnd);
                this.updateBoundingBox();
                this.isSizeable = true;
            }
        }
        
        @Override
        public boolean isSizeableStructure() {
            return this.isSizeable;
        }
    }
}
