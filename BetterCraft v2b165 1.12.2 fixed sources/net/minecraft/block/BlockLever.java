// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;

public class BlockLever extends Block
{
    public static final PropertyEnum<EnumOrientation> FACING;
    public static final PropertyBool POWERED;
    protected static final AxisAlignedBB LEVER_NORTH_AABB;
    protected static final AxisAlignedBB LEVER_SOUTH_AABB;
    protected static final AxisAlignedBB LEVER_WEST_AABB;
    protected static final AxisAlignedBB LEVER_EAST_AABB;
    protected static final AxisAlignedBB LEVER_UP_AABB;
    protected static final AxisAlignedBB LEVER_DOWN_AABB;
    
    static {
        FACING = PropertyEnum.create("facing", EnumOrientation.class);
        POWERED = PropertyBool.create("powered");
        LEVER_NORTH_AABB = new AxisAlignedBB(0.3125, 0.20000000298023224, 0.625, 0.6875, 0.800000011920929, 1.0);
        LEVER_SOUTH_AABB = new AxisAlignedBB(0.3125, 0.20000000298023224, 0.0, 0.6875, 0.800000011920929, 0.375);
        LEVER_WEST_AABB = new AxisAlignedBB(0.625, 0.20000000298023224, 0.3125, 1.0, 0.800000011920929, 0.6875);
        LEVER_EAST_AABB = new AxisAlignedBB(0.0, 0.20000000298023224, 0.3125, 0.375, 0.800000011920929, 0.6875);
        LEVER_UP_AABB = new AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 0.6000000238418579, 0.75);
        LEVER_DOWN_AABB = new AxisAlignedBB(0.25, 0.4000000059604645, 0.25, 0.75, 1.0, 0.75);
    }
    
    protected BlockLever() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockLever.FACING, EnumOrientation.NORTH).withProperty((IProperty<Comparable>)BlockLever.POWERED, false));
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        return BlockLever.NULL_AABB;
    }
    
    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        return canAttachTo(worldIn, pos, side);
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing enumfacing = values[i];
            if (canAttachTo(worldIn, pos, enumfacing)) {
                return true;
            }
        }
        return false;
    }
    
    protected static boolean canAttachTo(final World p_181090_0_, final BlockPos p_181090_1_, final EnumFacing p_181090_2_) {
        return BlockButton.canPlaceBlock(p_181090_0_, p_181090_1_, p_181090_2_);
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        final IBlockState iblockstate = this.getDefaultState().withProperty((IProperty<Comparable>)BlockLever.POWERED, false);
        if (canAttachTo(worldIn, pos, facing)) {
            return iblockstate.withProperty(BlockLever.FACING, EnumOrientation.forFacings(facing, placer.getHorizontalFacing()));
        }
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (enumfacing != facing && canAttachTo(worldIn, pos, enumfacing)) {
                return iblockstate.withProperty(BlockLever.FACING, EnumOrientation.forFacings(enumfacing, placer.getHorizontalFacing()));
            }
        }
        if (worldIn.getBlockState(pos.down()).isFullyOpaque()) {
            return iblockstate.withProperty(BlockLever.FACING, EnumOrientation.forFacings(EnumFacing.UP, placer.getHorizontalFacing()));
        }
        return iblockstate;
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos p_189540_5_) {
        if (this.checkCanSurvive(worldIn, pos, state) && !canAttachTo(worldIn, pos, state.getValue(BlockLever.FACING).getFacing())) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }
    
    private boolean checkCanSurvive(final World p_181091_1_, final BlockPos p_181091_2_, final IBlockState p_181091_3_) {
        if (this.canPlaceBlockAt(p_181091_1_, p_181091_2_)) {
            return true;
        }
        this.dropBlockAsItem(p_181091_1_, p_181091_2_, p_181091_3_, 0);
        p_181091_1_.setBlockToAir(p_181091_2_);
        return false;
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        switch (state.getValue(BlockLever.FACING)) {
            default: {
                return BlockLever.LEVER_EAST_AABB;
            }
            case WEST: {
                return BlockLever.LEVER_WEST_AABB;
            }
            case SOUTH: {
                return BlockLever.LEVER_SOUTH_AABB;
            }
            case NORTH: {
                return BlockLever.LEVER_NORTH_AABB;
            }
            case UP_Z:
            case UP_X: {
                return BlockLever.LEVER_UP_AABB;
            }
            case DOWN_X:
            case DOWN_Z: {
                return BlockLever.LEVER_DOWN_AABB;
            }
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing heldItem, final float side, final float hitX, final float hitY) {
        if (worldIn.isRemote) {
            return true;
        }
        state = state.cycleProperty((IProperty<Comparable>)BlockLever.POWERED);
        worldIn.setBlockState(pos, state, 3);
        final float f = state.getValue((IProperty<Boolean>)BlockLever.POWERED) ? 0.6f : 0.5f;
        worldIn.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3f, f);
        worldIn.notifyNeighborsOfStateChange(pos, this, false);
        final EnumFacing enumfacing = state.getValue(BlockLever.FACING).getFacing();
        worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this, false);
        return true;
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (state.getValue((IProperty<Boolean>)BlockLever.POWERED)) {
            worldIn.notifyNeighborsOfStateChange(pos, this, false);
            final EnumFacing enumfacing = state.getValue(BlockLever.FACING).getFacing();
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this, false);
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public int getWeakPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return blockState.getValue((IProperty<Boolean>)BlockLever.POWERED) ? 15 : 0;
    }
    
    @Override
    public int getStrongPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        if (!blockState.getValue((IProperty<Boolean>)BlockLever.POWERED)) {
            return 0;
        }
        return (blockState.getValue(BlockLever.FACING).getFacing() == side) ? 15 : 0;
    }
    
    @Override
    public boolean canProvidePower(final IBlockState state) {
        return true;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.byMetadata(meta & 0x7)).withProperty((IProperty<Comparable>)BlockLever.POWERED, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue(BlockLever.FACING).getMetadata();
        if (state.getValue((IProperty<Boolean>)BlockLever.POWERED)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        Label_0308: {
            switch (rot) {
                case CLOCKWISE_180: {
                    switch (state.getValue(BlockLever.FACING)) {
                        case EAST: {
                            return state.withProperty(BlockLever.FACING, EnumOrientation.WEST);
                        }
                        case WEST: {
                            return state.withProperty(BlockLever.FACING, EnumOrientation.EAST);
                        }
                        case SOUTH: {
                            return state.withProperty(BlockLever.FACING, EnumOrientation.NORTH);
                        }
                        case NORTH: {
                            return state.withProperty(BlockLever.FACING, EnumOrientation.SOUTH);
                        }
                        default: {
                            return state;
                        }
                    }
                    break;
                }
                case COUNTERCLOCKWISE_90: {
                    switch (state.getValue(BlockLever.FACING)) {
                        case EAST: {
                            return state.withProperty(BlockLever.FACING, EnumOrientation.NORTH);
                        }
                        case WEST: {
                            return state.withProperty(BlockLever.FACING, EnumOrientation.SOUTH);
                        }
                        case SOUTH: {
                            return state.withProperty(BlockLever.FACING, EnumOrientation.EAST);
                        }
                        case NORTH: {
                            return state.withProperty(BlockLever.FACING, EnumOrientation.WEST);
                        }
                        case UP_Z: {
                            return state.withProperty(BlockLever.FACING, EnumOrientation.UP_X);
                        }
                        case UP_X: {
                            return state.withProperty(BlockLever.FACING, EnumOrientation.UP_Z);
                        }
                        case DOWN_X: {
                            return state.withProperty(BlockLever.FACING, EnumOrientation.DOWN_Z);
                        }
                        case DOWN_Z: {
                            return state.withProperty(BlockLever.FACING, EnumOrientation.DOWN_X);
                        }
                        default: {
                            break Label_0308;
                        }
                    }
                    break;
                }
                case CLOCKWISE_90: {
                    switch (state.getValue(BlockLever.FACING)) {
                        case EAST: {
                            return state.withProperty(BlockLever.FACING, EnumOrientation.SOUTH);
                        }
                        case WEST: {
                            return state.withProperty(BlockLever.FACING, EnumOrientation.NORTH);
                        }
                        case SOUTH: {
                            return state.withProperty(BlockLever.FACING, EnumOrientation.WEST);
                        }
                        case NORTH: {
                            return state.withProperty(BlockLever.FACING, EnumOrientation.EAST);
                        }
                        case UP_Z: {
                            return state.withProperty(BlockLever.FACING, EnumOrientation.UP_X);
                        }
                        case UP_X: {
                            return state.withProperty(BlockLever.FACING, EnumOrientation.UP_Z);
                        }
                        case DOWN_X: {
                            return state.withProperty(BlockLever.FACING, EnumOrientation.DOWN_Z);
                        }
                        case DOWN_Z: {
                            return state.withProperty(BlockLever.FACING, EnumOrientation.DOWN_X);
                        }
                        default: {
                            break Label_0308;
                        }
                    }
                    break;
                }
            }
        }
        return state;
    }
    
    @Override
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(BlockLever.FACING).getFacing()));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockLever.FACING, BlockLever.POWERED });
    }
    
    @Override
    public BlockFaceShape func_193383_a(final IBlockAccess p_193383_1_, final IBlockState p_193383_2_, final BlockPos p_193383_3_, final EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }
    
    public enum EnumOrientation implements IStringSerializable
    {
        DOWN_X("DOWN_X", 0, 0, "down_x", EnumFacing.DOWN), 
        EAST("EAST", 1, 1, "east", EnumFacing.EAST), 
        WEST("WEST", 2, 2, "west", EnumFacing.WEST), 
        SOUTH("SOUTH", 3, 3, "south", EnumFacing.SOUTH), 
        NORTH("NORTH", 4, 4, "north", EnumFacing.NORTH), 
        UP_Z("UP_Z", 5, 5, "up_z", EnumFacing.UP), 
        UP_X("UP_X", 6, 6, "up_x", EnumFacing.UP), 
        DOWN_Z("DOWN_Z", 7, 7, "down_z", EnumFacing.DOWN);
        
        private static final EnumOrientation[] META_LOOKUP;
        private final int meta;
        private final String name;
        private final EnumFacing facing;
        
        static {
            META_LOOKUP = new EnumOrientation[values().length];
            EnumOrientation[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumOrientation blocklever$enumorientation = values[i];
                EnumOrientation.META_LOOKUP[blocklever$enumorientation.getMetadata()] = blocklever$enumorientation;
            }
        }
        
        private EnumOrientation(final String s, final int n, final int meta, final String name, final EnumFacing facing) {
            this.meta = meta;
            this.name = name;
            this.facing = facing;
        }
        
        public int getMetadata() {
            return this.meta;
        }
        
        public EnumFacing getFacing() {
            return this.facing;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public static EnumOrientation byMetadata(int meta) {
            if (meta < 0 || meta >= EnumOrientation.META_LOOKUP.length) {
                meta = 0;
            }
            return EnumOrientation.META_LOOKUP[meta];
        }
        
        public static EnumOrientation forFacings(final EnumFacing clickedSide, final EnumFacing entityFacing) {
            switch (clickedSide) {
                case DOWN: {
                    switch (entityFacing.getAxis()) {
                        case X: {
                            return EnumOrientation.DOWN_X;
                        }
                        case Z: {
                            return EnumOrientation.DOWN_Z;
                        }
                        default: {
                            throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
                        }
                    }
                    break;
                }
                case UP: {
                    switch (entityFacing.getAxis()) {
                        case X: {
                            return EnumOrientation.UP_X;
                        }
                        case Z: {
                            return EnumOrientation.UP_Z;
                        }
                        default: {
                            throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
                        }
                    }
                    break;
                }
                case NORTH: {
                    return EnumOrientation.NORTH;
                }
                case SOUTH: {
                    return EnumOrientation.SOUTH;
                }
                case WEST: {
                    return EnumOrientation.WEST;
                }
                case EAST: {
                    return EnumOrientation.EAST;
                }
                default: {
                    throw new IllegalArgumentException("Invalid facing: " + clickedSide);
                }
            }
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
