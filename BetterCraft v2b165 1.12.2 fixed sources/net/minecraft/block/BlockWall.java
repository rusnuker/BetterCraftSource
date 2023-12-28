// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.EnumFacing;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyBool;

public class BlockWall extends Block
{
    public static final PropertyBool UP;
    public static final PropertyBool NORTH;
    public static final PropertyBool EAST;
    public static final PropertyBool SOUTH;
    public static final PropertyBool WEST;
    public static final PropertyEnum<EnumType> VARIANT;
    protected static final AxisAlignedBB[] AABB_BY_INDEX;
    protected static final AxisAlignedBB[] CLIP_AABB_BY_INDEX;
    
    static {
        UP = PropertyBool.create("up");
        NORTH = PropertyBool.create("north");
        EAST = PropertyBool.create("east");
        SOUTH = PropertyBool.create("south");
        WEST = PropertyBool.create("west");
        VARIANT = PropertyEnum.create("variant", EnumType.class);
        AABB_BY_INDEX = new AxisAlignedBB[] { new AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 1.0, 0.75), new AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 1.0, 1.0), new AxisAlignedBB(0.0, 0.0, 0.25, 0.75, 1.0, 0.75), new AxisAlignedBB(0.0, 0.0, 0.25, 0.75, 1.0, 1.0), new AxisAlignedBB(0.25, 0.0, 0.0, 0.75, 1.0, 0.75), new AxisAlignedBB(0.3125, 0.0, 0.0, 0.6875, 0.875, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 0.75, 1.0, 0.75), new AxisAlignedBB(0.0, 0.0, 0.0, 0.75, 1.0, 1.0), new AxisAlignedBB(0.25, 0.0, 0.25, 1.0, 1.0, 0.75), new AxisAlignedBB(0.25, 0.0, 0.25, 1.0, 1.0, 1.0), new AxisAlignedBB(0.0, 0.0, 0.3125, 1.0, 0.875, 0.6875), new AxisAlignedBB(0.0, 0.0, 0.25, 1.0, 1.0, 1.0), new AxisAlignedBB(0.25, 0.0, 0.0, 1.0, 1.0, 0.75), new AxisAlignedBB(0.25, 0.0, 0.0, 1.0, 1.0, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.75), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0) };
        CLIP_AABB_BY_INDEX = new AxisAlignedBB[] { BlockWall.AABB_BY_INDEX[0].setMaxY(1.5), BlockWall.AABB_BY_INDEX[1].setMaxY(1.5), BlockWall.AABB_BY_INDEX[2].setMaxY(1.5), BlockWall.AABB_BY_INDEX[3].setMaxY(1.5), BlockWall.AABB_BY_INDEX[4].setMaxY(1.5), BlockWall.AABB_BY_INDEX[5].setMaxY(1.5), BlockWall.AABB_BY_INDEX[6].setMaxY(1.5), BlockWall.AABB_BY_INDEX[7].setMaxY(1.5), BlockWall.AABB_BY_INDEX[8].setMaxY(1.5), BlockWall.AABB_BY_INDEX[9].setMaxY(1.5), BlockWall.AABB_BY_INDEX[10].setMaxY(1.5), BlockWall.AABB_BY_INDEX[11].setMaxY(1.5), BlockWall.AABB_BY_INDEX[12].setMaxY(1.5), BlockWall.AABB_BY_INDEX[13].setMaxY(1.5), BlockWall.AABB_BY_INDEX[14].setMaxY(1.5), BlockWall.AABB_BY_INDEX[15].setMaxY(1.5) };
    }
    
    public BlockWall(final Block modelBlock) {
        super(modelBlock.blockMaterial);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockWall.UP, false).withProperty((IProperty<Comparable>)BlockWall.NORTH, false).withProperty((IProperty<Comparable>)BlockWall.EAST, false).withProperty((IProperty<Comparable>)BlockWall.SOUTH, false).withProperty((IProperty<Comparable>)BlockWall.WEST, false).withProperty(BlockWall.VARIANT, EnumType.NORMAL));
        this.setHardness(modelBlock.blockHardness);
        this.setResistance(modelBlock.blockResistance / 3.0f);
        this.setSoundType(modelBlock.blockSoundType);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, final IBlockAccess source, final BlockPos pos) {
        state = this.getActualState(state, source, pos);
        return BlockWall.AABB_BY_INDEX[getAABBIndex(state)];
    }
    
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean p_185477_7_) {
        if (!p_185477_7_) {
            state = this.getActualState(state, worldIn, pos);
        }
        Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockWall.CLIP_AABB_BY_INDEX[getAABBIndex(state)]);
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        blockState = this.getActualState(blockState, worldIn, pos);
        return BlockWall.CLIP_AABB_BY_INDEX[getAABBIndex(blockState)];
    }
    
    private static int getAABBIndex(final IBlockState state) {
        int i = 0;
        if (state.getValue((IProperty<Boolean>)BlockWall.NORTH)) {
            i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
        }
        if (state.getValue((IProperty<Boolean>)BlockWall.EAST)) {
            i |= 1 << EnumFacing.EAST.getHorizontalIndex();
        }
        if (state.getValue((IProperty<Boolean>)BlockWall.SOUTH)) {
            i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
        }
        if (state.getValue((IProperty<Boolean>)BlockWall.WEST)) {
            i |= 1 << EnumFacing.WEST.getHorizontalIndex();
        }
        return i;
    }
    
    @Override
    public String getLocalizedName() {
        return I18n.translateToLocal(String.valueOf(this.getUnlocalizedName()) + "." + EnumType.NORMAL.getUnlocalizedName() + ".name");
    }
    
    @Override
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    private boolean canConnectTo(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing p_176253_3_) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        final BlockFaceShape blockfaceshape = iblockstate.func_193401_d(worldIn, pos, p_176253_3_);
        final boolean flag = blockfaceshape == BlockFaceShape.MIDDLE_POLE_THICK || (blockfaceshape == BlockFaceShape.MIDDLE_POLE && block instanceof BlockFenceGate);
        return (!func_194143_e(block) && blockfaceshape == BlockFaceShape.SOLID) || flag;
    }
    
    protected static boolean func_194143_e(final Block p_194143_0_) {
        return Block.func_193382_c(p_194143_0_) || p_194143_0_ == Blocks.BARRIER || p_194143_0_ == Blocks.MELON_BLOCK || p_194143_0_ == Blocks.PUMPKIN || p_194143_0_ == Blocks.LIT_PUMPKIN;
    }
    
    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> tab) {
        EnumType[] values;
        for (int length = (values = EnumType.values()).length, i = 0; i < length; ++i) {
            final EnumType blockwall$enumtype = values[i];
            tab.add(new ItemStack(this, 1, blockwall$enumtype.getMetadata()));
        }
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockWall.VARIANT).getMetadata();
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return side != EnumFacing.DOWN || super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockWall.VARIANT, EnumType.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockWall.VARIANT).getMetadata();
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        final boolean flag = this.canConnectTo(worldIn, pos.north(), EnumFacing.SOUTH);
        final boolean flag2 = this.canConnectTo(worldIn, pos.east(), EnumFacing.WEST);
        final boolean flag3 = this.canConnectTo(worldIn, pos.south(), EnumFacing.NORTH);
        final boolean flag4 = this.canConnectTo(worldIn, pos.west(), EnumFacing.EAST);
        final boolean flag5 = (flag && !flag2 && flag3 && !flag4) || (!flag && flag2 && !flag3 && flag4);
        return state.withProperty((IProperty<Comparable>)BlockWall.UP, !flag5 || !worldIn.isAirBlock(pos.up())).withProperty((IProperty<Comparable>)BlockWall.NORTH, flag).withProperty((IProperty<Comparable>)BlockWall.EAST, flag2).withProperty((IProperty<Comparable>)BlockWall.SOUTH, flag3).withProperty((IProperty<Comparable>)BlockWall.WEST, flag4);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockWall.UP, BlockWall.NORTH, BlockWall.EAST, BlockWall.WEST, BlockWall.SOUTH, BlockWall.VARIANT });
    }
    
    @Override
    public BlockFaceShape func_193383_a(final IBlockAccess p_193383_1_, final IBlockState p_193383_2_, final BlockPos p_193383_3_, final EnumFacing p_193383_4_) {
        return (p_193383_4_ != EnumFacing.UP && p_193383_4_ != EnumFacing.DOWN) ? BlockFaceShape.MIDDLE_POLE_THICK : BlockFaceShape.CENTER_BIG;
    }
    
    public enum EnumType implements IStringSerializable
    {
        NORMAL("NORMAL", 0, 0, "cobblestone", "normal"), 
        MOSSY("MOSSY", 1, 1, "mossy_cobblestone", "mossy");
        
        private static final EnumType[] META_LOOKUP;
        private final int meta;
        private final String name;
        private final String unlocalizedName;
        
        static {
            META_LOOKUP = new EnumType[values().length];
            EnumType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumType blockwall$enumtype = values[i];
                EnumType.META_LOOKUP[blockwall$enumtype.getMetadata()] = blockwall$enumtype;
            }
        }
        
        private EnumType(final String s, final int n, final int meta, final String name, final String unlocalizedName) {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }
        
        public int getMetadata() {
            return this.meta;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public static EnumType byMetadata(int meta) {
            if (meta < 0 || meta >= EnumType.META_LOOKUP.length) {
                meta = 0;
            }
            return EnumType.META_LOOKUP[meta];
        }
        
        @Override
        public String getName() {
            return this.name;
        }
        
        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }
    }
}
