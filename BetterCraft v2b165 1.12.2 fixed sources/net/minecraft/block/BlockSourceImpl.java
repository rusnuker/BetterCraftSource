// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.dispenser.IBlockSource;

public class BlockSourceImpl implements IBlockSource
{
    private final World worldObj;
    private final BlockPos pos;
    
    public BlockSourceImpl(final World worldIn, final BlockPos posIn) {
        this.worldObj = worldIn;
        this.pos = posIn;
    }
    
    @Override
    public World getWorld() {
        return this.worldObj;
    }
    
    @Override
    public double getX() {
        return this.pos.getX() + 0.5;
    }
    
    @Override
    public double getY() {
        return this.pos.getY() + 0.5;
    }
    
    @Override
    public double getZ() {
        return this.pos.getZ() + 0.5;
    }
    
    @Override
    public BlockPos getBlockPos() {
        return this.pos;
    }
    
    @Override
    public IBlockState getBlockState() {
        return this.worldObj.getBlockState(this.pos);
    }
    
    @Override
    public <T extends TileEntity> T getBlockTileEntity() {
        return (T)this.worldObj.getTileEntity(this.pos);
    }
}
