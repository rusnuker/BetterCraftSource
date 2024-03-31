// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.IBlockAccess;
import java.util.Random;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockObserver extends BlockDirectional
{
    public static final PropertyBool field_190963_a;
    
    static {
        field_190963_a = PropertyBool.create("powered");
    }
    
    public BlockObserver() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockObserver.FACING, EnumFacing.SOUTH).withProperty((IProperty<Comparable>)BlockObserver.field_190963_a, false));
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockObserver.FACING, BlockObserver.field_190963_a });
    }
    
    @Override
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockObserver.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockObserver.FACING)));
    }
    
    @Override
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockObserver.FACING)));
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (state.getValue((IProperty<Boolean>)BlockObserver.field_190963_a)) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockObserver.field_190963_a, false), 2);
        }
        else {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockObserver.field_190963_a, true), 2);
            worldIn.scheduleUpdate(pos, this, 2);
        }
        this.func_190961_e(worldIn, pos, state);
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos p_189540_5_) {
    }
    
    public void func_190962_b(final IBlockState p_190962_1_, final World p_190962_2_, final BlockPos p_190962_3_, final Block p_190962_4_, final BlockPos p_190962_5_) {
        if (!p_190962_2_.isRemote && p_190962_3_.offset(p_190962_1_.getValue((IProperty<EnumFacing>)BlockObserver.FACING)).equals(p_190962_5_)) {
            this.func_190960_d(p_190962_1_, p_190962_2_, p_190962_3_);
        }
    }
    
    private void func_190960_d(final IBlockState p_190960_1_, final World p_190960_2_, final BlockPos p_190960_3_) {
        if (!p_190960_1_.getValue((IProperty<Boolean>)BlockObserver.field_190963_a) && !p_190960_2_.isUpdateScheduled(p_190960_3_, this)) {
            p_190960_2_.scheduleUpdate(p_190960_3_, this, 2);
        }
    }
    
    protected void func_190961_e(final World p_190961_1_, final BlockPos p_190961_2_, final IBlockState p_190961_3_) {
        final EnumFacing enumfacing = p_190961_3_.getValue((IProperty<EnumFacing>)BlockObserver.FACING);
        final BlockPos blockpos = p_190961_2_.offset(enumfacing.getOpposite());
        p_190961_1_.func_190524_a(blockpos, this, p_190961_2_);
        p_190961_1_.notifyNeighborsOfStateExcept(blockpos, this, enumfacing);
    }
    
    @Override
    public boolean canProvidePower(final IBlockState state) {
        return true;
    }
    
    @Override
    public int getStrongPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return blockState.getWeakPower(blockAccess, pos, side);
    }
    
    @Override
    public int getWeakPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return (blockState.getValue((IProperty<Boolean>)BlockObserver.field_190963_a) && blockState.getValue((IProperty<Comparable>)BlockObserver.FACING) == side) ? 15 : 0;
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote) {
            if (state.getValue((IProperty<Boolean>)BlockObserver.field_190963_a)) {
                this.updateTick(worldIn, pos, state, worldIn.rand);
            }
            this.func_190960_d(state, worldIn, pos);
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (state.getValue((IProperty<Boolean>)BlockObserver.field_190963_a) && worldIn.isUpdateScheduled(pos, this)) {
            this.func_190961_e(worldIn, pos, state.withProperty((IProperty<Comparable>)BlockObserver.field_190963_a, false));
        }
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockObserver.FACING, EnumFacing.func_190914_a(pos, placer).getOpposite());
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockObserver.FACING).getIndex();
        if (state.getValue((IProperty<Boolean>)BlockObserver.field_190963_a)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockObserver.FACING, EnumFacing.getFront(meta & 0x7));
    }
}
