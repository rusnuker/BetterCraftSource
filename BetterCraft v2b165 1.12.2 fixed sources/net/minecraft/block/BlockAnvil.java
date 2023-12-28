// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block;

import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Rotation;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.IInteractionObject;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.properties.PropertyDirection;

public class BlockAnvil extends BlockFalling
{
    public static final PropertyDirection FACING;
    public static final PropertyInteger DAMAGE;
    protected static final AxisAlignedBB X_AXIS_AABB;
    protected static final AxisAlignedBB Z_AXIS_AABB;
    protected static final Logger LOGGER;
    
    static {
        FACING = BlockHorizontal.FACING;
        DAMAGE = PropertyInteger.create("damage", 0, 2);
        X_AXIS_AABB = new AxisAlignedBB(0.0, 0.0, 0.125, 1.0, 1.0, 0.875);
        Z_AXIS_AABB = new AxisAlignedBB(0.125, 0.0, 0.0, 0.875, 1.0, 1.0);
        LOGGER = LogManager.getLogger();
    }
    
    protected BlockAnvil() {
        super(Material.ANVIL);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockAnvil.FACING, EnumFacing.NORTH).withProperty((IProperty<Comparable>)BlockAnvil.DAMAGE, 0));
        this.setLightOpacity(0);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public BlockFaceShape func_193383_a(final IBlockAccess p_193383_1_, final IBlockState p_193383_2_, final BlockPos p_193383_3_, final EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }
    
    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        final EnumFacing enumfacing = placer.getHorizontalFacing().rotateY();
        try {
            return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty((IProperty<Comparable>)BlockAnvil.FACING, enumfacing).withProperty((IProperty<Comparable>)BlockAnvil.DAMAGE, meta >> 2);
        }
        catch (final IllegalArgumentException var11) {
            if (!worldIn.isRemote) {
                BlockAnvil.LOGGER.warn(String.format("Invalid damage property for anvil at %s. Found %d, must be in [0, 1, 2]", pos, meta >> 2));
                if (placer instanceof EntityPlayer) {
                    placer.addChatMessage(new TextComponentTranslation("Invalid damage property. Please pick in [0, 1, 2]", new Object[0]));
                }
            }
            return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, 0, placer).withProperty((IProperty<Comparable>)BlockAnvil.FACING, enumfacing).withProperty((IProperty<Comparable>)BlockAnvil.DAMAGE, 0);
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing heldItem, final float side, final float hitX, final float hitY) {
        if (!worldIn.isRemote) {
            playerIn.displayGui(new Anvil(worldIn, pos));
        }
        return true;
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockAnvil.DAMAGE);
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockAnvil.FACING);
        return (enumfacing.getAxis() == EnumFacing.Axis.X) ? BlockAnvil.X_AXIS_AABB : BlockAnvil.Z_AXIS_AABB;
    }
    
    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> tab) {
        tab.add(new ItemStack(this));
        tab.add(new ItemStack(this, 1, 1));
        tab.add(new ItemStack(this, 1, 2));
    }
    
    @Override
    protected void onStartFalling(final EntityFallingBlock fallingEntity) {
        fallingEntity.setHurtEntities(true);
    }
    
    @Override
    public void onEndFalling(final World worldIn, final BlockPos pos, final IBlockState p_176502_3_, final IBlockState p_176502_4_) {
        worldIn.playEvent(1031, pos, 0);
    }
    
    @Override
    public void func_190974_b(final World p_190974_1_, final BlockPos p_190974_2_) {
        p_190974_1_.playEvent(1029, p_190974_2_, 0);
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return true;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockAnvil.FACING, EnumFacing.getHorizontal(meta & 0x3)).withProperty((IProperty<Comparable>)BlockAnvil.DAMAGE, (meta & 0xF) >> 2);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockAnvil.FACING).getHorizontalIndex();
        i |= state.getValue((IProperty<Integer>)BlockAnvil.DAMAGE) << 2;
        return i;
    }
    
    @Override
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return (state.getBlock() != this) ? state : state.withProperty((IProperty<Comparable>)BlockAnvil.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockAnvil.FACING)));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockAnvil.FACING, BlockAnvil.DAMAGE });
    }
    
    public static class Anvil implements IInteractionObject
    {
        private final World world;
        private final BlockPos position;
        
        public Anvil(final World worldIn, final BlockPos pos) {
            this.world = worldIn;
            this.position = pos;
        }
        
        @Override
        public String getName() {
            return "anvil";
        }
        
        @Override
        public boolean hasCustomName() {
            return false;
        }
        
        @Override
        public ITextComponent getDisplayName() {
            return new TextComponentTranslation(String.valueOf(Blocks.ANVIL.getUnlocalizedName()) + ".name", new Object[0]);
        }
        
        @Override
        public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
            return new ContainerRepair(playerInventory, this.world, this.position, playerIn);
        }
        
        @Override
        public String getGuiID() {
            return "minecraft:anvil";
        }
    }
}
