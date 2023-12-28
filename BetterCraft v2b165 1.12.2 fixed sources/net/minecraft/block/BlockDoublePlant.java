// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.NonNullList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.stats.StatList;
import javax.annotation.Nullable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyEnum;

public class BlockDoublePlant extends BlockBush implements IGrowable
{
    public static final PropertyEnum<EnumPlantType> VARIANT;
    public static final PropertyEnum<EnumBlockHalf> HALF;
    public static final PropertyEnum<EnumFacing> FACING;
    
    static {
        VARIANT = PropertyEnum.create("variant", EnumPlantType.class);
        HALF = PropertyEnum.create("half", EnumBlockHalf.class);
        FACING = BlockHorizontal.FACING;
    }
    
    public BlockDoublePlant() {
        super(Material.VINE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDoublePlant.VARIANT, EnumPlantType.SUNFLOWER).withProperty(BlockDoublePlant.HALF, EnumBlockHalf.LOWER).withProperty(BlockDoublePlant.FACING, EnumFacing.NORTH));
        this.setHardness(0.0f);
        this.setSoundType(SoundType.PLANT);
        this.setUnlocalizedName("doublePlant");
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockDoublePlant.FULL_BLOCK_AABB;
    }
    
    private EnumPlantType getType(final IBlockAccess blockAccess, final BlockPos pos, IBlockState state) {
        if (state.getBlock() == this) {
            state = state.getActualState(blockAccess, pos);
            return state.getValue(BlockDoublePlant.VARIANT);
        }
        return EnumPlantType.FERN;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && worldIn.isAirBlock(pos.up());
    }
    
    @Override
    public boolean isReplaceable(final IBlockAccess worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() != this) {
            return true;
        }
        final EnumPlantType blockdoubleplant$enumplanttype = iblockstate.getActualState(worldIn, pos).getValue(BlockDoublePlant.VARIANT);
        return blockdoubleplant$enumplanttype == EnumPlantType.FERN || blockdoubleplant$enumplanttype == EnumPlantType.GRASS;
    }
    
    @Override
    protected void checkAndDropBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!this.canBlockStay(worldIn, pos, state)) {
            final boolean flag = state.getValue(BlockDoublePlant.HALF) == EnumBlockHalf.UPPER;
            final BlockPos blockpos = flag ? pos : pos.up();
            final BlockPos blockpos2 = flag ? pos.down() : pos;
            final Block block = flag ? this : worldIn.getBlockState(blockpos).getBlock();
            final Block block2 = flag ? worldIn.getBlockState(blockpos2).getBlock() : this;
            if (block == this) {
                worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2);
            }
            if (block2 == this) {
                worldIn.setBlockState(blockpos2, Blocks.AIR.getDefaultState(), 3);
                if (!flag) {
                    this.dropBlockAsItem(worldIn, blockpos2, state, 0);
                }
            }
        }
    }
    
    @Override
    public boolean canBlockStay(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (state.getValue(BlockDoublePlant.HALF) == EnumBlockHalf.UPPER) {
            return worldIn.getBlockState(pos.down()).getBlock() == this;
        }
        final IBlockState iblockstate = worldIn.getBlockState(pos.up());
        return iblockstate.getBlock() == this && super.canBlockStay(worldIn, pos, iblockstate);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        if (state.getValue(BlockDoublePlant.HALF) == EnumBlockHalf.UPPER) {
            return Items.field_190931_a;
        }
        final EnumPlantType blockdoubleplant$enumplanttype = state.getValue(BlockDoublePlant.VARIANT);
        if (blockdoubleplant$enumplanttype == EnumPlantType.FERN) {
            return Items.field_190931_a;
        }
        if (blockdoubleplant$enumplanttype == EnumPlantType.GRASS) {
            return (rand.nextInt(8) == 0) ? Items.WHEAT_SEEDS : Items.field_190931_a;
        }
        return super.getItemDropped(state, rand, fortune);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return (state.getValue(BlockDoublePlant.HALF) != EnumBlockHalf.UPPER && state.getValue(BlockDoublePlant.VARIANT) != EnumPlantType.GRASS) ? state.getValue(BlockDoublePlant.VARIANT).getMeta() : 0;
    }
    
    public void placeAt(final World worldIn, final BlockPos lowerPos, final EnumPlantType variant, final int flags) {
        worldIn.setBlockState(lowerPos, this.getDefaultState().withProperty(BlockDoublePlant.HALF, EnumBlockHalf.LOWER).withProperty(BlockDoublePlant.VARIANT, variant), flags);
        worldIn.setBlockState(lowerPos.up(), this.getDefaultState().withProperty(BlockDoublePlant.HALF, EnumBlockHalf.UPPER), flags);
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        worldIn.setBlockState(pos.up(), this.getDefaultState().withProperty(BlockDoublePlant.HALF, EnumBlockHalf.UPPER), 2);
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer player, final BlockPos pos, final IBlockState state, @Nullable final TileEntity te, final ItemStack stack) {
        if (worldIn.isRemote || stack.getItem() != Items.SHEARS || state.getValue(BlockDoublePlant.HALF) != EnumBlockHalf.LOWER || !this.onHarvest(worldIn, pos, state, player)) {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }
    
    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer player) {
        if (state.getValue(BlockDoublePlant.HALF) == EnumBlockHalf.UPPER) {
            if (worldIn.getBlockState(pos.down()).getBlock() == this) {
                if (player.capabilities.isCreativeMode) {
                    worldIn.setBlockToAir(pos.down());
                }
                else {
                    final IBlockState iblockstate = worldIn.getBlockState(pos.down());
                    final EnumPlantType blockdoubleplant$enumplanttype = iblockstate.getValue(BlockDoublePlant.VARIANT);
                    if (blockdoubleplant$enumplanttype != EnumPlantType.FERN && blockdoubleplant$enumplanttype != EnumPlantType.GRASS) {
                        worldIn.destroyBlock(pos.down(), true);
                    }
                    else if (worldIn.isRemote) {
                        worldIn.setBlockToAir(pos.down());
                    }
                    else if (!player.getHeldItemMainhand().func_190926_b() && player.getHeldItemMainhand().getItem() == Items.SHEARS) {
                        this.onHarvest(worldIn, pos, iblockstate, player);
                        worldIn.setBlockToAir(pos.down());
                    }
                    else {
                        worldIn.destroyBlock(pos.down(), true);
                    }
                }
            }
        }
        else if (worldIn.getBlockState(pos.up()).getBlock() == this) {
            worldIn.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 2);
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }
    
    private boolean onHarvest(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer player) {
        final EnumPlantType blockdoubleplant$enumplanttype = state.getValue(BlockDoublePlant.VARIANT);
        if (blockdoubleplant$enumplanttype != EnumPlantType.FERN && blockdoubleplant$enumplanttype != EnumPlantType.GRASS) {
            return false;
        }
        player.addStat(StatList.getBlockStats(this));
        final int i = ((blockdoubleplant$enumplanttype == EnumPlantType.GRASS) ? BlockTallGrass.EnumType.GRASS : BlockTallGrass.EnumType.FERN).getMeta();
        Block.spawnAsEntity(worldIn, pos, new ItemStack(Blocks.TALLGRASS, 2, i));
        return true;
    }
    
    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> tab) {
        EnumPlantType[] values;
        for (int length = (values = EnumPlantType.values()).length, i = 0; i < length; ++i) {
            final EnumPlantType blockdoubleplant$enumplanttype = values[i];
            tab.add(new ItemStack(this, 1, blockdoubleplant$enumplanttype.getMeta()));
        }
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(this, 1, this.getType(worldIn, pos, state).getMeta());
    }
    
    @Override
    public boolean canGrow(final World worldIn, final BlockPos pos, final IBlockState state, final boolean isClient) {
        final EnumPlantType blockdoubleplant$enumplanttype = this.getType(worldIn, pos, state);
        return blockdoubleplant$enumplanttype != EnumPlantType.GRASS && blockdoubleplant$enumplanttype != EnumPlantType.FERN;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        return true;
    }
    
    @Override
    public void grow(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        Block.spawnAsEntity(worldIn, pos, new ItemStack(this, 1, this.getType(worldIn, pos, state).getMeta()));
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return ((meta & 0x8) > 0) ? this.getDefaultState().withProperty(BlockDoublePlant.HALF, EnumBlockHalf.UPPER) : this.getDefaultState().withProperty(BlockDoublePlant.HALF, EnumBlockHalf.LOWER).withProperty(BlockDoublePlant.VARIANT, EnumPlantType.byMetadata(meta & 0x7));
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        if (state.getValue(BlockDoublePlant.HALF) == EnumBlockHalf.UPPER) {
            final IBlockState iblockstate = worldIn.getBlockState(pos.down());
            if (iblockstate.getBlock() == this) {
                state = state.withProperty(BlockDoublePlant.VARIANT, (EnumPlantType)iblockstate.getValue((IProperty<V>)BlockDoublePlant.VARIANT));
            }
        }
        return state;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return (state.getValue(BlockDoublePlant.HALF) == EnumBlockHalf.UPPER) ? (0x8 | state.getValue(BlockDoublePlant.FACING).getHorizontalIndex()) : state.getValue(BlockDoublePlant.VARIANT).getMeta();
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockDoublePlant.HALF, BlockDoublePlant.VARIANT, BlockDoublePlant.FACING });
    }
    
    @Override
    public EnumOffsetType getOffsetType() {
        return EnumOffsetType.XZ;
    }
    
    public enum EnumBlockHalf implements IStringSerializable
    {
        UPPER("UPPER", 0), 
        LOWER("LOWER", 1);
        
        private EnumBlockHalf(final String s, final int n) {
        }
        
        @Override
        public String toString() {
            return this.getName();
        }
        
        @Override
        public String getName() {
            return (this == EnumBlockHalf.UPPER) ? "upper" : "lower";
        }
    }
    
    public enum EnumPlantType implements IStringSerializable
    {
        SUNFLOWER("SUNFLOWER", 0, 0, "sunflower"), 
        SYRINGA("SYRINGA", 1, 1, "syringa"), 
        GRASS("GRASS", 2, 2, "double_grass", "grass"), 
        FERN("FERN", 3, 3, "double_fern", "fern"), 
        ROSE("ROSE", 4, 4, "double_rose", "rose"), 
        PAEONIA("PAEONIA", 5, 5, "paeonia");
        
        private static final EnumPlantType[] META_LOOKUP;
        private final int meta;
        private final String name;
        private final String unlocalizedName;
        
        static {
            META_LOOKUP = new EnumPlantType[values().length];
            EnumPlantType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumPlantType blockdoubleplant$enumplanttype = values[i];
                EnumPlantType.META_LOOKUP[blockdoubleplant$enumplanttype.getMeta()] = blockdoubleplant$enumplanttype;
            }
        }
        
        private EnumPlantType(final String s, final int n, final int meta, final String name) {
            this(s, n, meta, name, name);
        }
        
        private EnumPlantType(final String s, final int n, final int meta, final String name, final String unlocalizedName) {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }
        
        public int getMeta() {
            return this.meta;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public static EnumPlantType byMetadata(int meta) {
            if (meta < 0 || meta >= EnumPlantType.META_LOOKUP.length) {
                meta = 0;
            }
            return EnumPlantType.META_LOOKUP[meta];
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
