// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.properties.PropertyDirection;

public class BlockBanner extends BlockContainer
{
    public static final PropertyDirection FACING;
    public static final PropertyInteger ROTATION;
    protected static final AxisAlignedBB STANDING_AABB;
    
    static {
        FACING = BlockHorizontal.FACING;
        ROTATION = PropertyInteger.create("rotation", 0, 15);
        STANDING_AABB = new AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 1.0, 0.75);
    }
    
    protected BlockBanner() {
        super(Material.WOOD);
    }
    
    @Override
    public String getLocalizedName() {
        return I18n.translateToLocal("item.banner.white.name");
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        return BlockBanner.NULL_AABB;
    }
    
    @Override
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
        return true;
    }
    
    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public boolean canSpawnInBlock() {
        return true;
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityBanner();
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.BANNER;
    }
    
    private ItemStack getTileDataItemStack(final World worldIn, final BlockPos pos) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        return (tileentity instanceof TileEntityBanner) ? ((TileEntityBanner)tileentity).func_190615_l() : ItemStack.field_190927_a;
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        final ItemStack itemstack = this.getTileDataItemStack(worldIn, pos);
        return itemstack.func_190926_b() ? new ItemStack(Items.BANNER) : itemstack;
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        final ItemStack itemstack = this.getTileDataItemStack(worldIn, pos);
        if (itemstack.func_190926_b()) {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        }
        else {
            Block.spawnAsEntity(worldIn, pos, itemstack);
        }
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return !this.hasInvalidNeighbor(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos);
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer player, final BlockPos pos, final IBlockState state, @Nullable final TileEntity te, final ItemStack stack) {
        if (te instanceof TileEntityBanner) {
            final TileEntityBanner tileentitybanner = (TileEntityBanner)te;
            final ItemStack itemstack = tileentitybanner.func_190615_l();
            Block.spawnAsEntity(worldIn, pos, itemstack);
        }
        else {
            super.harvestBlock(worldIn, player, pos, state, null, stack);
        }
    }
    
    @Override
    public BlockFaceShape func_193383_a(final IBlockAccess p_193383_1_, final IBlockState p_193383_2_, final BlockPos p_193383_3_, final EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }
    
    public static class BlockBannerHanging extends BlockBanner
    {
        protected static final AxisAlignedBB NORTH_AABB;
        protected static final AxisAlignedBB SOUTH_AABB;
        protected static final AxisAlignedBB WEST_AABB;
        protected static final AxisAlignedBB EAST_AABB;
        
        static {
            NORTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.875, 1.0, 0.78125, 1.0);
            SOUTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.78125, 0.125);
            WEST_AABB = new AxisAlignedBB(0.875, 0.0, 0.0, 1.0, 0.78125, 1.0);
            EAST_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.125, 0.78125, 1.0);
        }
        
        public BlockBannerHanging() {
            this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockBannerHanging.FACING, EnumFacing.NORTH));
        }
        
        @Override
        public IBlockState withRotation(final IBlockState state, final Rotation rot) {
            return state.withProperty((IProperty<Comparable>)BlockBannerHanging.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockBannerHanging.FACING)));
        }
        
        @Override
        public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
            return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockBannerHanging.FACING)));
        }
        
        @Override
        public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
            switch (state.getValue((IProperty<EnumFacing>)BlockBannerHanging.FACING)) {
                default: {
                    return BlockBannerHanging.NORTH_AABB;
                }
                case SOUTH: {
                    return BlockBannerHanging.SOUTH_AABB;
                }
                case WEST: {
                    return BlockBannerHanging.WEST_AABB;
                }
                case EAST: {
                    return BlockBannerHanging.EAST_AABB;
                }
            }
        }
        
        @Override
        public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos p_189540_5_) {
            final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockBannerHanging.FACING);
            if (!worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getMaterial().isSolid()) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
            super.neighborChanged(state, worldIn, pos, blockIn, p_189540_5_);
        }
        
        @Override
        public IBlockState getStateFromMeta(final int meta) {
            EnumFacing enumfacing = EnumFacing.getFront(meta);
            if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
                enumfacing = EnumFacing.NORTH;
            }
            return this.getDefaultState().withProperty((IProperty<Comparable>)BlockBannerHanging.FACING, enumfacing);
        }
        
        @Override
        public int getMetaFromState(final IBlockState state) {
            return state.getValue((IProperty<EnumFacing>)BlockBannerHanging.FACING).getIndex();
        }
        
        @Override
        protected BlockStateContainer createBlockState() {
            return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockBannerHanging.FACING });
        }
    }
    
    public static class BlockBannerStanding extends BlockBanner
    {
        public BlockBannerStanding() {
            this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockBannerStanding.ROTATION, 0));
        }
        
        @Override
        public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
            return BlockBannerStanding.STANDING_AABB;
        }
        
        @Override
        public IBlockState withRotation(final IBlockState state, final Rotation rot) {
            return state.withProperty((IProperty<Comparable>)BlockBannerStanding.ROTATION, rot.rotate(state.getValue((IProperty<Integer>)BlockBannerStanding.ROTATION), 16));
        }
        
        @Override
        public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
            return state.withProperty((IProperty<Comparable>)BlockBannerStanding.ROTATION, mirrorIn.mirrorRotation(state.getValue((IProperty<Integer>)BlockBannerStanding.ROTATION), 16));
        }
        
        @Override
        public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos p_189540_5_) {
            if (!worldIn.getBlockState(pos.down()).getMaterial().isSolid()) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
            super.neighborChanged(state, worldIn, pos, blockIn, p_189540_5_);
        }
        
        @Override
        public IBlockState getStateFromMeta(final int meta) {
            return this.getDefaultState().withProperty((IProperty<Comparable>)BlockBannerStanding.ROTATION, meta);
        }
        
        @Override
        public int getMetaFromState(final IBlockState state) {
            return state.getValue((IProperty<Integer>)BlockBannerStanding.ROTATION);
        }
        
        @Override
        protected BlockStateContainer createBlockState() {
            return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockBannerStanding.ROTATION });
        }
    }
}
