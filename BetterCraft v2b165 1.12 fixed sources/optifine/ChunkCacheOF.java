// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.Biome;
import java.util.Arrays;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;

public class ChunkCacheOF implements IBlockAccess
{
    private ChunkCache chunkCache;
    private int posX;
    private int posY;
    private int posZ;
    private int[] combinedLights;
    private IBlockState[] blockStates;
    private static ArrayCache cacheCombinedLights;
    private static ArrayCache cacheBlockStates;
    private static final int ARRAY_SIZE = 8000;
    
    static {
        ChunkCacheOF.cacheCombinedLights = new ArrayCache(Integer.TYPE, 16);
        ChunkCacheOF.cacheBlockStates = new ArrayCache(IBlockState.class, 16);
    }
    
    public ChunkCacheOF(final ChunkCache p_i22_1_, final BlockPos p_i22_2_, final int p_i22_3_) {
        this.chunkCache = p_i22_1_;
        this.posX = p_i22_2_.getX() - p_i22_3_;
        this.posY = p_i22_2_.getY() - p_i22_3_;
        this.posZ = p_i22_2_.getZ() - p_i22_3_;
    }
    
    @Override
    public int getCombinedLight(final BlockPos pos, final int lightValue) {
        if (this.combinedLights == null) {
            int k = this.chunkCache.getCombinedLight(pos, lightValue);
            if (Config.isDynamicLights() && !this.getBlockState(pos).isOpaqueCube()) {
                k = DynamicLights.getCombinedLight(pos, k);
            }
            return k;
        }
        final int i = this.getPositionIndex(pos);
        if (i >= 0 && i < this.combinedLights.length) {
            int j = this.combinedLights[i];
            if (j == -1) {
                j = this.chunkCache.getCombinedLight(pos, lightValue);
                if (Config.isDynamicLights() && !this.getBlockState(pos).isOpaqueCube()) {
                    j = DynamicLights.getCombinedLight(pos, j);
                }
                this.combinedLights[i] = j;
            }
            return j;
        }
        return this.chunkCache.getCombinedLight(pos, lightValue);
    }
    
    @Override
    public IBlockState getBlockState(final BlockPos pos) {
        if (this.blockStates == null) {
            return this.chunkCache.getBlockState(pos);
        }
        final int i = this.getPositionIndex(pos);
        if (i >= 0 && i < this.blockStates.length) {
            IBlockState iblockstate = this.blockStates[i];
            if (iblockstate == null) {
                iblockstate = this.chunkCache.getBlockState(pos);
                this.blockStates[i] = iblockstate;
            }
            return iblockstate;
        }
        return this.chunkCache.getBlockState(pos);
    }
    
    private int getPositionIndex(final BlockPos p_getPositionIndex_1_) {
        final int i = p_getPositionIndex_1_.getX() - this.posX;
        final int j = p_getPositionIndex_1_.getY() - this.posY;
        final int k = p_getPositionIndex_1_.getZ() - this.posZ;
        return i * 400 + k * 20 + j;
    }
    
    public void renderStart() {
        if (this.combinedLights == null) {
            this.combinedLights = (int[])ChunkCacheOF.cacheCombinedLights.allocate(8000);
        }
        Arrays.fill(this.combinedLights, -1);
        if (this.blockStates == null) {
            this.blockStates = (IBlockState[])ChunkCacheOF.cacheBlockStates.allocate(8000);
        }
        Arrays.fill(this.blockStates, null);
    }
    
    public void renderFinish() {
        ChunkCacheOF.cacheCombinedLights.free(this.combinedLights);
        this.combinedLights = null;
        ChunkCacheOF.cacheBlockStates.free(this.blockStates);
        this.blockStates = null;
    }
    
    public boolean isEmpty() {
        return this.chunkCache.extendedLevelsInChunkCache();
    }
    
    @Override
    public Biome getBiome(final BlockPos pos) {
        return this.chunkCache.getBiome(pos);
    }
    
    @Override
    public int getStrongPower(final BlockPos pos, final EnumFacing direction) {
        return this.chunkCache.getStrongPower(pos, direction);
    }
    
    @Override
    public TileEntity getTileEntity(final BlockPos pos) {
        return this.chunkCache.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
    }
    
    public TileEntity getTileEntity(final BlockPos p_getTileEntity_1_, final Chunk.EnumCreateEntityType p_getTileEntity_2_) {
        return this.chunkCache.getTileEntity(p_getTileEntity_1_, p_getTileEntity_2_);
    }
    
    @Override
    public WorldType getWorldType() {
        return this.chunkCache.getWorldType();
    }
    
    @Override
    public boolean isAirBlock(final BlockPos pos) {
        return this.chunkCache.isAirBlock(pos);
    }
    
    public boolean isSideSolid(final BlockPos p_isSideSolid_1_, final EnumFacing p_isSideSolid_2_, final boolean p_isSideSolid_3_) {
        return Reflector.callBoolean(this.chunkCache, Reflector.ForgeChunkCache_isSideSolid, p_isSideSolid_1_, p_isSideSolid_2_, p_isSideSolid_3_);
    }
}
