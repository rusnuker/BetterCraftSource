// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.world.IBlockAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.properties.PropertyEnum;

public class BlockConcretePowder extends BlockFalling
{
    public static final PropertyEnum<EnumDyeColor> field_192426_a;
    
    static {
        field_192426_a = PropertyEnum.create("color", EnumDyeColor.class);
    }
    
    public BlockConcretePowder() {
        super(Material.SAND);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockConcretePowder.field_192426_a, EnumDyeColor.WHITE));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    public void onEndFalling(final World worldIn, final BlockPos pos, final IBlockState p_176502_3_, final IBlockState p_176502_4_) {
        if (p_176502_4_.getMaterial().isLiquid()) {
            worldIn.setBlockState(pos, Blocks.field_192443_dR.getDefaultState().withProperty(BlockColored.COLOR, (EnumDyeColor)p_176502_3_.getValue((IProperty<V>)BlockConcretePowder.field_192426_a)), 3);
        }
    }
    
    protected boolean func_192425_e(final World p_192425_1_, final BlockPos p_192425_2_, final IBlockState p_192425_3_) {
        boolean flag = false;
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing enumfacing = values[i];
            if (enumfacing != EnumFacing.DOWN) {
                final BlockPos blockpos = p_192425_2_.offset(enumfacing);
                if (p_192425_1_.getBlockState(blockpos).getMaterial() == Material.WATER) {
                    flag = true;
                    break;
                }
            }
        }
        if (flag) {
            p_192425_1_.setBlockState(p_192425_2_, Blocks.field_192443_dR.getDefaultState().withProperty(BlockColored.COLOR, (EnumDyeColor)p_192425_3_.getValue((IProperty<V>)BlockConcretePowder.field_192426_a)), 3);
        }
        return flag;
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos p_189540_5_) {
        if (!this.func_192425_e(worldIn, pos, state)) {
            super.neighborChanged(state, worldIn, pos, blockIn, p_189540_5_);
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!this.func_192425_e(worldIn, pos, state)) {
            super.onBlockAdded(worldIn, pos, state);
        }
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockConcretePowder.field_192426_a).getMetadata();
    }
    
    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> tab) {
        EnumDyeColor[] values;
        for (int length = (values = EnumDyeColor.values()).length, i = 0; i < length; ++i) {
            final EnumDyeColor enumdyecolor = values[i];
            tab.add(new ItemStack(this, 1, enumdyecolor.getMetadata()));
        }
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state, final IBlockAccess p_180659_2_, final BlockPos p_180659_3_) {
        return MapColor.func_193558_a(state.getValue(BlockConcretePowder.field_192426_a));
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockConcretePowder.field_192426_a, EnumDyeColor.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockConcretePowder.field_192426_a).getMetadata();
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockConcretePowder.field_192426_a });
    }
}
