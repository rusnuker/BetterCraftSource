// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;

public class BlockDirt extends Block
{
    public static final PropertyEnum<DirtType> VARIANT;
    public static final PropertyBool SNOWY;
    
    static {
        VARIANT = PropertyEnum.create("variant", DirtType.class);
        SNOWY = PropertyBool.create("snowy");
    }
    
    protected BlockDirt() {
        super(Material.GROUND);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDirt.VARIANT, DirtType.DIRT).withProperty((IProperty<Comparable>)BlockDirt.SNOWY, false));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state, final IBlockAccess p_180659_2_, final BlockPos p_180659_3_) {
        return state.getValue(BlockDirt.VARIANT).getColor();
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        if (state.getValue(BlockDirt.VARIANT) == DirtType.PODZOL) {
            final Block block = worldIn.getBlockState(pos.up()).getBlock();
            state = state.withProperty((IProperty<Comparable>)BlockDirt.SNOWY, block == Blocks.SNOW || block == Blocks.SNOW_LAYER);
        }
        return state;
    }
    
    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> tab) {
        tab.add(new ItemStack(this, 1, DirtType.DIRT.getMetadata()));
        tab.add(new ItemStack(this, 1, DirtType.COARSE_DIRT.getMetadata()));
        tab.add(new ItemStack(this, 1, DirtType.PODZOL.getMetadata()));
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(this, 1, state.getValue(BlockDirt.VARIANT).getMetadata());
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockDirt.VARIANT, DirtType.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockDirt.VARIANT).getMetadata();
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockDirt.VARIANT, BlockDirt.SNOWY });
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        DirtType blockdirt$dirttype = state.getValue(BlockDirt.VARIANT);
        if (blockdirt$dirttype == DirtType.PODZOL) {
            blockdirt$dirttype = DirtType.DIRT;
        }
        return blockdirt$dirttype.getMetadata();
    }
    
    public enum DirtType implements IStringSerializable
    {
        DIRT("DIRT", 0, 0, "dirt", "default", MapColor.DIRT), 
        COARSE_DIRT("COARSE_DIRT", 1, 1, "coarse_dirt", "coarse", MapColor.DIRT), 
        PODZOL("PODZOL", 2, 2, "podzol", MapColor.OBSIDIAN);
        
        private static final DirtType[] METADATA_LOOKUP;
        private final int metadata;
        private final String name;
        private final String unlocalizedName;
        private final MapColor color;
        
        static {
            METADATA_LOOKUP = new DirtType[values().length];
            DirtType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final DirtType blockdirt$dirttype = values[i];
                DirtType.METADATA_LOOKUP[blockdirt$dirttype.getMetadata()] = blockdirt$dirttype;
            }
        }
        
        private DirtType(final String s, final int n, final int metadataIn, final String nameIn, final MapColor color) {
            this(s, n, metadataIn, nameIn, nameIn, color);
        }
        
        private DirtType(final String s, final int n, final int metadataIn, final String nameIn, final String unlocalizedNameIn, final MapColor color) {
            this.metadata = metadataIn;
            this.name = nameIn;
            this.unlocalizedName = unlocalizedNameIn;
            this.color = color;
        }
        
        public int getMetadata() {
            return this.metadata;
        }
        
        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }
        
        public MapColor getColor() {
            return this.color;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public static DirtType byMetadata(int metadata) {
            if (metadata < 0 || metadata >= DirtType.METADATA_LOOKUP.length) {
                metadata = 0;
            }
            return DirtType.METADATA_LOOKUP[metadata];
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
