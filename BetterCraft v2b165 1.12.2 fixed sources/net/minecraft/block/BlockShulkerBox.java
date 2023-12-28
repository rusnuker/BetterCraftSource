// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.EnumPushReaction;
import java.util.Iterator;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.util.NonNullList;
import net.minecraft.client.util.ITooltipFlag;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.inventory.IInventory;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyEnum;

public class BlockShulkerBox extends BlockContainer
{
    public static final PropertyEnum<EnumFacing> field_190957_a;
    private final EnumDyeColor field_190958_b;
    
    static {
        field_190957_a = PropertyDirection.create("facing");
    }
    
    public BlockShulkerBox(final EnumDyeColor p_i47248_1_) {
        super(Material.ROCK, MapColor.AIR);
        this.field_190958_b = p_i47248_1_;
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockShulkerBox.field_190957_a, EnumFacing.UP));
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityShulkerBox(this.field_190958_b);
    }
    
    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public boolean causesSuffocation(final IBlockState p_176214_1_) {
        return true;
    }
    
    @Override
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public boolean func_190946_v(final IBlockState p_190946_1_) {
        return true;
    }
    
    @Override
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing heldItem, final float side, final float hitX, final float hitY) {
        if (worldIn.isRemote) {
            return true;
        }
        if (playerIn.isSpectator()) {
            return true;
        }
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityShulkerBox) {
            final EnumFacing enumfacing = state.getValue(BlockShulkerBox.field_190957_a);
            boolean flag;
            if (((TileEntityShulkerBox)tileentity).func_190591_p() == TileEntityShulkerBox.AnimationStatus.CLOSED) {
                final AxisAlignedBB axisalignedbb = BlockShulkerBox.FULL_BLOCK_AABB.addCoord(0.5f * enumfacing.getFrontOffsetX(), 0.5f * enumfacing.getFrontOffsetY(), 0.5f * enumfacing.getFrontOffsetZ()).func_191195_a(enumfacing.getFrontOffsetX(), enumfacing.getFrontOffsetY(), enumfacing.getFrontOffsetZ());
                flag = !worldIn.collidesWithAnyBlock(axisalignedbb.offset(pos.offset(enumfacing)));
            }
            else {
                flag = true;
            }
            if (flag) {
                playerIn.addStat(StatList.field_191272_ae);
                playerIn.displayGUIChest((IInventory)tileentity);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty(BlockShulkerBox.field_190957_a, facing);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockShulkerBox.field_190957_a });
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockShulkerBox.field_190957_a).getIndex();
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        final EnumFacing enumfacing = EnumFacing.getFront(meta);
        return this.getDefaultState().withProperty(BlockShulkerBox.field_190957_a, enumfacing);
    }
    
    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer player) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityShulkerBox) {
            final TileEntityShulkerBox tileentityshulkerbox = (TileEntityShulkerBox)worldIn.getTileEntity(pos);
            tileentityshulkerbox.func_190579_a(player.capabilities.isCreativeMode);
            tileentityshulkerbox.fillWithLoot(player);
        }
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        if (stack.hasDisplayName()) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityShulkerBox) {
                ((TileEntityShulkerBox)tileentity).func_190575_a(stack.getDisplayName());
            }
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityShulkerBox) {
            final TileEntityShulkerBox tileentityshulkerbox = (TileEntityShulkerBox)tileentity;
            if (!tileentityshulkerbox.func_190590_r() && tileentityshulkerbox.func_190582_F()) {
                final ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                nbttagcompound.setTag("BlockEntityTag", ((TileEntityShulkerBox)tileentity).func_190580_f(nbttagcompound2));
                itemstack.setTagCompound(nbttagcompound);
                if (tileentityshulkerbox.hasCustomName()) {
                    itemstack.setStackDisplayName(tileentityshulkerbox.getName());
                    tileentityshulkerbox.func_190575_a("");
                }
                Block.spawnAsEntity(worldIn, pos, itemstack);
            }
            worldIn.updateComparatorOutputLevel(pos, state.getBlock());
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public void func_190948_a(final ItemStack p_190948_1_, @Nullable final World p_190948_2_, final List<String> p_190948_3_, final ITooltipFlag p_190948_4_) {
        super.func_190948_a(p_190948_1_, p_190948_2_, p_190948_3_, p_190948_4_);
        final NBTTagCompound nbttagcompound = p_190948_1_.getTagCompound();
        if (nbttagcompound != null && nbttagcompound.hasKey("BlockEntityTag", 10)) {
            final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("BlockEntityTag");
            if (nbttagcompound2.hasKey("LootTable", 8)) {
                p_190948_3_.add("???????");
            }
            if (nbttagcompound2.hasKey("Items", 9)) {
                final NonNullList<ItemStack> nonnulllist = NonNullList.func_191197_a(27, ItemStack.field_190927_a);
                ItemStackHelper.func_191283_b(nbttagcompound2, nonnulllist);
                int i = 0;
                int j = 0;
                for (final ItemStack itemstack : nonnulllist) {
                    if (!itemstack.func_190926_b()) {
                        ++j;
                        if (i > 4) {
                            continue;
                        }
                        ++i;
                        p_190948_3_.add(String.format("%s x%d", itemstack.getDisplayName(), itemstack.func_190916_E()));
                    }
                }
                if (j - i > 0) {
                    p_190948_3_.add(String.format(TextFormatting.ITALIC + I18n.translateToLocal("container.shulkerBox.more"), j - i));
                }
            }
        }
    }
    
    @Override
    public EnumPushReaction getMobilityFlag(final IBlockState state) {
        return EnumPushReaction.DESTROY;
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        final TileEntity tileentity = source.getTileEntity(pos);
        return (tileentity instanceof TileEntityShulkerBox) ? ((TileEntityShulkerBox)tileentity).func_190584_a(state) : BlockShulkerBox.FULL_BLOCK_AABB;
    }
    
    @Override
    public boolean hasComparatorInputOverride(final IBlockState state) {
        return true;
    }
    
    @Override
    public int getComparatorInputOverride(final IBlockState blockState, final World worldIn, final BlockPos pos) {
        return Container.calcRedstoneFromInventory((IInventory)worldIn.getTileEntity(pos));
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        final ItemStack itemstack = super.getItem(worldIn, pos, state);
        final TileEntityShulkerBox tileentityshulkerbox = (TileEntityShulkerBox)worldIn.getTileEntity(pos);
        final NBTTagCompound nbttagcompound = tileentityshulkerbox.func_190580_f(new NBTTagCompound());
        if (!nbttagcompound.hasNoTags()) {
            itemstack.setTagInfo("BlockEntityTag", nbttagcompound);
        }
        return itemstack;
    }
    
    public static EnumDyeColor func_190955_b(final Item p_190955_0_) {
        return func_190954_c(Block.getBlockFromItem(p_190955_0_));
    }
    
    public static EnumDyeColor func_190954_c(final Block p_190954_0_) {
        return (p_190954_0_ instanceof BlockShulkerBox) ? ((BlockShulkerBox)p_190954_0_).func_190956_e() : EnumDyeColor.PURPLE;
    }
    
    public static Block func_190952_a(final EnumDyeColor p_190952_0_) {
        switch (p_190952_0_) {
            case WHITE: {
                return Blocks.field_190977_dl;
            }
            case ORANGE: {
                return Blocks.field_190978_dm;
            }
            case MAGENTA: {
                return Blocks.field_190979_dn;
            }
            case LIGHT_BLUE: {
                return Blocks.field_190980_do;
            }
            case YELLOW: {
                return Blocks.field_190981_dp;
            }
            case LIME: {
                return Blocks.field_190982_dq;
            }
            case PINK: {
                return Blocks.field_190983_dr;
            }
            case GRAY: {
                return Blocks.field_190984_ds;
            }
            case SILVER: {
                return Blocks.field_190985_dt;
            }
            case CYAN: {
                return Blocks.field_190986_du;
            }
            default: {
                return Blocks.field_190987_dv;
            }
            case BLUE: {
                return Blocks.field_190988_dw;
            }
            case BROWN: {
                return Blocks.field_190989_dx;
            }
            case GREEN: {
                return Blocks.field_190990_dy;
            }
            case RED: {
                return Blocks.field_190991_dz;
            }
            case BLACK: {
                return Blocks.field_190975_dA;
            }
        }
    }
    
    public EnumDyeColor func_190956_e() {
        return this.field_190958_b;
    }
    
    public static ItemStack func_190953_b(final EnumDyeColor p_190953_0_) {
        return new ItemStack(func_190952_a(p_190953_0_));
    }
    
    @Override
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty(BlockShulkerBox.field_190957_a, rot.rotate(state.getValue(BlockShulkerBox.field_190957_a)));
    }
    
    @Override
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(BlockShulkerBox.field_190957_a)));
    }
    
    @Override
    public BlockFaceShape func_193383_a(final IBlockAccess p_193383_1_, IBlockState p_193383_2_, final BlockPos p_193383_3_, final EnumFacing p_193383_4_) {
        p_193383_2_ = this.getActualState(p_193383_2_, p_193383_1_, p_193383_3_);
        final EnumFacing enumfacing = p_193383_2_.getValue(BlockShulkerBox.field_190957_a);
        final TileEntityShulkerBox.AnimationStatus tileentityshulkerbox$animationstatus = ((TileEntityShulkerBox)p_193383_1_.getTileEntity(p_193383_3_)).func_190591_p();
        return (tileentityshulkerbox$animationstatus != TileEntityShulkerBox.AnimationStatus.CLOSED && (tileentityshulkerbox$animationstatus != TileEntityShulkerBox.AnimationStatus.OPENED || (enumfacing != p_193383_4_.getOpposite() && enumfacing != p_193383_4_))) ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
    }
}
