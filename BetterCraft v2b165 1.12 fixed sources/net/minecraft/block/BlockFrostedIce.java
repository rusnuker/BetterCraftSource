// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block;

import net.minecraft.item.ItemStack;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.EnumFacing;
import java.util.Random;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;

public class BlockFrostedIce extends BlockIce
{
    public static final PropertyInteger AGE;
    
    static {
        AGE = PropertyInteger.create("age", 0, 3);
    }
    
    public BlockFrostedIce() {
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockFrostedIce.AGE, 0));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockFrostedIce.AGE);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockFrostedIce.AGE, MathHelper.clamp(meta, 0, 3));
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if ((rand.nextInt(3) == 0 || this.countNeighbors(worldIn, pos) < 4) && worldIn.getLightFromNeighbors(pos) > 11 - state.getValue((IProperty<Integer>)BlockFrostedIce.AGE) - state.getLightOpacity()) {
            this.slightlyMelt(worldIn, pos, state, rand, true);
        }
        else {
            worldIn.scheduleUpdate(pos, this, MathHelper.getInt(rand, 20, 40));
        }
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos p_189540_5_) {
        if (blockIn == this) {
            final int i = this.countNeighbors(worldIn, pos);
            if (i < 2) {
                this.turnIntoWater(worldIn, pos);
            }
        }
    }
    
    private int countNeighbors(final World p_185680_1_, final BlockPos p_185680_2_) {
        int i = 0;
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, j = 0; j < length; ++j) {
            final EnumFacing enumfacing = values[j];
            if (p_185680_1_.getBlockState(p_185680_2_.offset(enumfacing)).getBlock() == this && ++i >= 4) {
                return i;
            }
        }
        return i;
    }
    
    protected void slightlyMelt(final World p_185681_1_, final BlockPos p_185681_2_, final IBlockState p_185681_3_, final Random p_185681_4_, final boolean p_185681_5_) {
        final int i = p_185681_3_.getValue((IProperty<Integer>)BlockFrostedIce.AGE);
        if (i < 3) {
            p_185681_1_.setBlockState(p_185681_2_, p_185681_3_.withProperty((IProperty<Comparable>)BlockFrostedIce.AGE, i + 1), 2);
            p_185681_1_.scheduleUpdate(p_185681_2_, this, MathHelper.getInt(p_185681_4_, 20, 40));
        }
        else {
            this.turnIntoWater(p_185681_1_, p_185681_2_);
            if (p_185681_5_) {
                EnumFacing[] values;
                for (int length = (values = EnumFacing.values()).length, j = 0; j < length; ++j) {
                    final EnumFacing enumfacing = values[j];
                    final BlockPos blockpos = p_185681_2_.offset(enumfacing);
                    final IBlockState iblockstate = p_185681_1_.getBlockState(blockpos);
                    if (iblockstate.getBlock() == this) {
                        this.slightlyMelt(p_185681_1_, blockpos, iblockstate, p_185681_4_, false);
                    }
                }
            }
        }
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockFrostedIce.AGE });
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return ItemStack.field_190927_a;
    }
}
