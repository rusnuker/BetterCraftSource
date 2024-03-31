// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.nbt.NBTBase;
import net.minecraft.init.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.client.util.ITooltipFlag;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.BannerPattern;
import java.util.List;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;

public class ItemBanner extends ItemBlock
{
    public ItemBanner() {
        super(Blocks.STANDING_BANNER);
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer stack, final World playerIn, BlockPos worldIn, final EnumHand pos, final EnumFacing hand, final float facing, final float hitX, final float hitY) {
        final IBlockState iblockstate = playerIn.getBlockState(worldIn);
        final boolean flag = iblockstate.getBlock().isReplaceable(playerIn, worldIn);
        if (hand == EnumFacing.DOWN || (!iblockstate.getMaterial().isSolid() && !flag) || (flag && hand != EnumFacing.UP)) {
            return EnumActionResult.FAIL;
        }
        worldIn = worldIn.offset(hand);
        final ItemStack itemstack = stack.getHeldItem(pos);
        if (!stack.canPlayerEdit(worldIn, hand, itemstack) || !Blocks.STANDING_BANNER.canPlaceBlockAt(playerIn, worldIn)) {
            return EnumActionResult.FAIL;
        }
        if (playerIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        worldIn = (flag ? worldIn.down() : worldIn);
        if (hand == EnumFacing.UP) {
            final int i = MathHelper.floor((stack.rotationYaw + 180.0f) * 16.0f / 360.0f + 0.5) & 0xF;
            playerIn.setBlockState(worldIn, Blocks.STANDING_BANNER.getDefaultState().withProperty((IProperty<Comparable>)BlockStandingSign.ROTATION, i), 3);
        }
        else {
            playerIn.setBlockState(worldIn, Blocks.WALL_BANNER.getDefaultState().withProperty((IProperty<Comparable>)BlockWallSign.FACING, hand), 3);
        }
        final TileEntity tileentity = playerIn.getTileEntity(worldIn);
        if (tileentity instanceof TileEntityBanner) {
            ((TileEntityBanner)tileentity).setItemValues(itemstack, false);
        }
        if (stack instanceof EntityPlayerMP) {
            CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)stack, worldIn, itemstack);
        }
        itemstack.func_190918_g(1);
        return EnumActionResult.SUCCESS;
    }
    
    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        String s = "item.banner.";
        final EnumDyeColor enumdyecolor = getBaseColor(stack);
        s = String.valueOf(s) + enumdyecolor.getUnlocalizedName() + ".name";
        return I18n.translateToLocal(s);
    }
    
    public static void appendHoverTextFromTileEntityTag(final ItemStack stack, final List<String> p_185054_1_) {
        final NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag");
        if (nbttagcompound != null && nbttagcompound.hasKey("Patterns")) {
            final NBTTagList nbttaglist = nbttagcompound.getTagList("Patterns", 10);
            for (int i = 0; i < nbttaglist.tagCount() && i < 6; ++i) {
                final NBTTagCompound nbttagcompound2 = nbttaglist.getCompoundTagAt(i);
                final EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(nbttagcompound2.getInteger("Color"));
                final BannerPattern bannerpattern = BannerPattern.func_190994_a(nbttagcompound2.getString("Pattern"));
                if (bannerpattern != null) {
                    p_185054_1_.add(I18n.translateToLocal("item.banner." + bannerpattern.func_190997_a() + "." + enumdyecolor.getUnlocalizedName()));
                }
            }
        }
    }
    
    @Override
    public void addInformation(final ItemStack stack, @Nullable final World playerIn, final List<String> tooltip, final ITooltipFlag advanced) {
        appendHoverTextFromTileEntityTag(stack, tooltip);
    }
    
    @Override
    public void getSubItems(final CreativeTabs itemIn, final NonNullList<ItemStack> tab) {
        if (this.func_194125_a(itemIn)) {
            EnumDyeColor[] values;
            for (int length = (values = EnumDyeColor.values()).length, i = 0; i < length; ++i) {
                final EnumDyeColor enumdyecolor = values[i];
                tab.add(func_190910_a(enumdyecolor, null));
            }
        }
    }
    
    public static ItemStack func_190910_a(final EnumDyeColor p_190910_0_, @Nullable final NBTTagList p_190910_1_) {
        final ItemStack itemstack = new ItemStack(Items.BANNER, 1, p_190910_0_.getDyeDamage());
        if (p_190910_1_ != null && !p_190910_1_.hasNoTags()) {
            itemstack.func_190925_c("BlockEntityTag").setTag("Patterns", p_190910_1_.copy());
        }
        return itemstack;
    }
    
    @Override
    public CreativeTabs getCreativeTab() {
        return CreativeTabs.DECORATIONS;
    }
    
    public static EnumDyeColor getBaseColor(final ItemStack stack) {
        return EnumDyeColor.byDyeDamage(stack.getMetadata() & 0xF);
    }
}
