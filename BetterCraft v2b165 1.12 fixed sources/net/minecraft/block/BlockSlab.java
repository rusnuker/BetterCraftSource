// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyEnum;

public abstract class BlockSlab extends Block
{
    public static final PropertyEnum<EnumBlockHalf> HALF;
    protected static final AxisAlignedBB AABB_BOTTOM_HALF;
    protected static final AxisAlignedBB AABB_TOP_HALF;
    
    static {
        HALF = PropertyEnum.create("half", EnumBlockHalf.class);
        AABB_BOTTOM_HALF = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
        AABB_TOP_HALF = new AxisAlignedBB(0.0, 0.5, 0.0, 1.0, 1.0, 1.0);
    }
    
    public BlockSlab(final Material materialIn) {
        this(materialIn, materialIn.getMaterialMapColor());
    }
    
    public BlockSlab(final Material p_i47249_1_, final MapColor p_i47249_2_) {
        super(p_i47249_1_, p_i47249_2_);
        this.fullBlock = this.isDouble();
        this.setLightOpacity(255);
    }
    
    @Override
    protected boolean canSilkHarvest() {
        return false;
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        if (this.isDouble()) {
            return BlockSlab.FULL_BLOCK_AABB;
        }
        return (state.getValue(BlockSlab.HALF) == EnumBlockHalf.TOP) ? BlockSlab.AABB_TOP_HALF : BlockSlab.AABB_BOTTOM_HALF;
    }
    
    @Override
    public boolean isFullyOpaque(final IBlockState state) {
        return ((BlockSlab)state.getBlock()).isDouble() || state.getValue(BlockSlab.HALF) == EnumBlockHalf.TOP;
    }
    
    @Override
    public BlockFaceShape func_193383_a(final IBlockAccess p_193383_1_, final IBlockState p_193383_2_, final BlockPos p_193383_3_, final EnumFacing p_193383_4_) {
        if (((BlockSlab)p_193383_2_.getBlock()).isDouble()) {
            return BlockFaceShape.SOLID;
        }
        if (p_193383_4_ == EnumFacing.UP && p_193383_2_.getValue(BlockSlab.HALF) == EnumBlockHalf.TOP) {
            return BlockFaceShape.SOLID;
        }
        return (p_193383_4_ == EnumFacing.DOWN && p_193383_2_.getValue(BlockSlab.HALF) == EnumBlockHalf.BOTTOM) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
    
    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return this.isDouble();
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        final IBlockState iblockstate = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(BlockSlab.HALF, EnumBlockHalf.BOTTOM);
        if (this.isDouble()) {
            return iblockstate;
        }
        return (facing != EnumFacing.DOWN && (facing == EnumFacing.UP || hitY <= 0.5)) ? iblockstate : iblockstate.withProperty(BlockSlab.HALF, EnumBlockHalf.TOP);
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return this.isDouble() ? 2 : 1;
    }
    
    @Override
    public boolean isFullCube(final IBlockState state) {
        return this.isDouble();
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        if (this.isDouble()) {
            return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
        if (side != EnumFacing.UP && side != EnumFacing.DOWN && !super.shouldSideBeRendered(blockState, blockAccess, pos, side)) {
            return false;
        }
        final IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        final boolean flag = isHalfSlab(iblockstate) && iblockstate.getValue(BlockSlab.HALF) == EnumBlockHalf.TOP;
        final boolean flag2 = isHalfSlab(blockState) && blockState.getValue(BlockSlab.HALF) == EnumBlockHalf.TOP;
        if (flag2) {
            return side == EnumFacing.DOWN || (side == EnumFacing.UP && super.shouldSideBeRendered(blockState, blockAccess, pos, side)) || !isHalfSlab(iblockstate) || !flag;
        }
        return side == EnumFacing.UP || (side == EnumFacing.DOWN && super.shouldSideBeRendered(blockState, blockAccess, pos, side)) || !isHalfSlab(iblockstate) || flag;
    }
    
    protected static boolean isHalfSlab(final IBlockState state) {
        final Block block = state.getBlock();
        return block == Blocks.STONE_SLAB || block == Blocks.WOODEN_SLAB || block == Blocks.STONE_SLAB2 || block == Blocks.PURPUR_SLAB;
    }
    
    public abstract String getUnlocalizedName(final int p0);
    
    public abstract boolean isDouble();
    
    public abstract IProperty<?> getVariantProperty();
    
    public abstract Comparable<?> getTypeForItem(final ItemStack p0);
    
    public enum EnumBlockHalf implements IStringSerializable
    {
        TOP("TOP", 0, "top"), 
        BOTTOM("BOTTOM", 1, "bottom");
        
        private final String name;
        
        private EnumBlockHalf(final String s, final int n, final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
