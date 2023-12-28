// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import javax.annotation.Nullable;
import com.google.common.base.Predicate;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;

public class BlockRailPowered extends BlockRailBase
{
    public static final PropertyEnum<EnumRailDirection> SHAPE;
    public static final PropertyBool POWERED;
    
    static {
        SHAPE = PropertyEnum.create("shape", EnumRailDirection.class, new Predicate<EnumRailDirection>() {
            @Override
            public boolean apply(@Nullable final EnumRailDirection p_apply_1_) {
                return p_apply_1_ != EnumRailDirection.NORTH_EAST && p_apply_1_ != EnumRailDirection.NORTH_WEST && p_apply_1_ != EnumRailDirection.SOUTH_EAST && p_apply_1_ != EnumRailDirection.SOUTH_WEST;
            }
        });
        POWERED = PropertyBool.create("powered");
    }
    
    protected BlockRailPowered() {
        super(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockRailPowered.SHAPE, EnumRailDirection.NORTH_SOUTH).withProperty((IProperty<Comparable>)BlockRailPowered.POWERED, false));
    }
    
    protected boolean findPoweredRailSignal(final World worldIn, final BlockPos pos, final IBlockState state, final boolean p_176566_4_, final int p_176566_5_) {
        if (p_176566_5_ >= 8) {
            return false;
        }
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        boolean flag = true;
        EnumRailDirection blockrailbase$enumraildirection = state.getValue(BlockRailPowered.SHAPE);
        switch (blockrailbase$enumraildirection) {
            case NORTH_SOUTH: {
                if (p_176566_4_) {
                    ++k;
                    break;
                }
                --k;
                break;
            }
            case EAST_WEST: {
                if (p_176566_4_) {
                    --i;
                    break;
                }
                ++i;
                break;
            }
            case ASCENDING_EAST: {
                if (p_176566_4_) {
                    --i;
                }
                else {
                    ++i;
                    ++j;
                    flag = false;
                }
                blockrailbase$enumraildirection = EnumRailDirection.EAST_WEST;
                break;
            }
            case ASCENDING_WEST: {
                if (p_176566_4_) {
                    --i;
                    ++j;
                    flag = false;
                }
                else {
                    ++i;
                }
                blockrailbase$enumraildirection = EnumRailDirection.EAST_WEST;
                break;
            }
            case ASCENDING_NORTH: {
                if (p_176566_4_) {
                    ++k;
                }
                else {
                    --k;
                    ++j;
                    flag = false;
                }
                blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
                break;
            }
            case ASCENDING_SOUTH: {
                if (p_176566_4_) {
                    ++k;
                    ++j;
                    flag = false;
                }
                else {
                    --k;
                }
                blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
                break;
            }
        }
        return this.isSameRailWithPower(worldIn, new BlockPos(i, j, k), p_176566_4_, p_176566_5_, blockrailbase$enumraildirection) || (flag && this.isSameRailWithPower(worldIn, new BlockPos(i, j - 1, k), p_176566_4_, p_176566_5_, blockrailbase$enumraildirection));
    }
    
    protected boolean isSameRailWithPower(final World worldIn, final BlockPos pos, final boolean p_176567_3_, final int distance, final EnumRailDirection p_176567_5_) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() != this) {
            return false;
        }
        final EnumRailDirection blockrailbase$enumraildirection = iblockstate.getValue(BlockRailPowered.SHAPE);
        return (p_176567_5_ != EnumRailDirection.EAST_WEST || (blockrailbase$enumraildirection != EnumRailDirection.NORTH_SOUTH && blockrailbase$enumraildirection != EnumRailDirection.ASCENDING_NORTH && blockrailbase$enumraildirection != EnumRailDirection.ASCENDING_SOUTH)) && (p_176567_5_ != EnumRailDirection.NORTH_SOUTH || (blockrailbase$enumraildirection != EnumRailDirection.EAST_WEST && blockrailbase$enumraildirection != EnumRailDirection.ASCENDING_EAST && blockrailbase$enumraildirection != EnumRailDirection.ASCENDING_WEST)) && iblockstate.getValue((IProperty<Boolean>)BlockRailPowered.POWERED) && (worldIn.isBlockPowered(pos) || this.findPoweredRailSignal(worldIn, pos, iblockstate, p_176567_3_, distance + 1));
    }
    
    @Override
    protected void updateState(final IBlockState p_189541_1_, final World p_189541_2_, final BlockPos p_189541_3_, final Block p_189541_4_) {
        final boolean flag = p_189541_1_.getValue((IProperty<Boolean>)BlockRailPowered.POWERED);
        final boolean flag2 = p_189541_2_.isBlockPowered(p_189541_3_) || this.findPoweredRailSignal(p_189541_2_, p_189541_3_, p_189541_1_, true, 0) || this.findPoweredRailSignal(p_189541_2_, p_189541_3_, p_189541_1_, false, 0);
        if (flag2 != flag) {
            p_189541_2_.setBlockState(p_189541_3_, p_189541_1_.withProperty((IProperty<Comparable>)BlockRailPowered.POWERED, flag2), 3);
            p_189541_2_.notifyNeighborsOfStateChange(p_189541_3_.down(), this, false);
            if (p_189541_1_.getValue(BlockRailPowered.SHAPE).isAscending()) {
                p_189541_2_.notifyNeighborsOfStateChange(p_189541_3_.up(), this, false);
            }
        }
    }
    
    @Override
    public IProperty<EnumRailDirection> getShapeProperty() {
        return BlockRailPowered.SHAPE;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockRailPowered.SHAPE, EnumRailDirection.byMetadata(meta & 0x7)).withProperty((IProperty<Comparable>)BlockRailPowered.POWERED, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue(BlockRailPowered.SHAPE).getMetadata();
        if (state.getValue((IProperty<Boolean>)BlockRailPowered.POWERED)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        Label_0406: {
            switch (rot) {
                case CLOCKWISE_180: {
                    switch (state.getValue(BlockRailPowered.SHAPE)) {
                        case ASCENDING_EAST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.ASCENDING_WEST);
                        }
                        case ASCENDING_WEST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.ASCENDING_EAST);
                        }
                        case ASCENDING_NORTH: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.ASCENDING_SOUTH);
                        }
                        case ASCENDING_SOUTH: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.ASCENDING_NORTH);
                        }
                        case SOUTH_EAST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.NORTH_WEST);
                        }
                        case SOUTH_WEST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.NORTH_EAST);
                        }
                        case NORTH_WEST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.SOUTH_EAST);
                        }
                        case NORTH_EAST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.SOUTH_WEST);
                        }
                        default: {
                            break Label_0406;
                        }
                    }
                    break;
                }
                case COUNTERCLOCKWISE_90: {
                    switch (state.getValue(BlockRailPowered.SHAPE)) {
                        case NORTH_SOUTH: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.EAST_WEST);
                        }
                        case EAST_WEST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.NORTH_SOUTH);
                        }
                        case ASCENDING_EAST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.ASCENDING_NORTH);
                        }
                        case ASCENDING_WEST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.ASCENDING_SOUTH);
                        }
                        case ASCENDING_NORTH: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.ASCENDING_WEST);
                        }
                        case ASCENDING_SOUTH: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.ASCENDING_EAST);
                        }
                        case SOUTH_EAST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.NORTH_EAST);
                        }
                        case SOUTH_WEST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.SOUTH_EAST);
                        }
                        case NORTH_WEST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.SOUTH_WEST);
                        }
                        case NORTH_EAST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.NORTH_WEST);
                        }
                        default: {
                            break Label_0406;
                        }
                    }
                    break;
                }
                case CLOCKWISE_90: {
                    switch (state.getValue(BlockRailPowered.SHAPE)) {
                        case NORTH_SOUTH: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.EAST_WEST);
                        }
                        case EAST_WEST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.NORTH_SOUTH);
                        }
                        case ASCENDING_EAST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.ASCENDING_SOUTH);
                        }
                        case ASCENDING_WEST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.ASCENDING_NORTH);
                        }
                        case ASCENDING_NORTH: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.ASCENDING_EAST);
                        }
                        case ASCENDING_SOUTH: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.ASCENDING_WEST);
                        }
                        case SOUTH_EAST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.SOUTH_WEST);
                        }
                        case SOUTH_WEST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.NORTH_WEST);
                        }
                        case NORTH_WEST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.NORTH_EAST);
                        }
                        case NORTH_EAST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.SOUTH_EAST);
                        }
                        default: {
                            break Label_0406;
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
        final EnumRailDirection blockrailbase$enumraildirection = state.getValue(BlockRailPowered.SHAPE);
        Label_0313: {
            switch (mirrorIn) {
                case LEFT_RIGHT: {
                    switch (blockrailbase$enumraildirection) {
                        case ASCENDING_NORTH: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.ASCENDING_SOUTH);
                        }
                        case ASCENDING_SOUTH: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.ASCENDING_NORTH);
                        }
                        case SOUTH_EAST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.NORTH_EAST);
                        }
                        case SOUTH_WEST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.NORTH_WEST);
                        }
                        case NORTH_WEST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.SOUTH_WEST);
                        }
                        case NORTH_EAST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.SOUTH_EAST);
                        }
                        default: {
                            return super.withMirror(state, mirrorIn);
                        }
                    }
                    break;
                }
                case FRONT_BACK: {
                    switch (blockrailbase$enumraildirection) {
                        case ASCENDING_EAST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.ASCENDING_WEST);
                        }
                        case ASCENDING_WEST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.ASCENDING_EAST);
                        }
                        default: {
                            break Label_0313;
                        }
                        case SOUTH_EAST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.SOUTH_WEST);
                        }
                        case SOUTH_WEST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.SOUTH_EAST);
                        }
                        case NORTH_WEST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.NORTH_EAST);
                        }
                        case NORTH_EAST: {
                            return state.withProperty(BlockRailPowered.SHAPE, EnumRailDirection.NORTH_WEST);
                        }
                    }
                    break;
                }
            }
        }
        return super.withMirror(state, mirrorIn);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockRailPowered.SHAPE, BlockRailPowered.POWERED });
    }
}
