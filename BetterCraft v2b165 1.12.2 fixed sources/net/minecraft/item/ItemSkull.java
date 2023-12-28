// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.NonNullList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockSkull;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemSkull extends Item
{
    private static final String[] SKULL_TYPES;
    
    static {
        SKULL_TYPES = new String[] { "skeleton", "wither", "zombie", "char", "creeper", "dragon" };
    }
    
    public ItemSkull() {
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer stack, final World playerIn, BlockPos worldIn, final EnumHand pos, final EnumFacing hand, final float facing, final float hitX, final float hitY) {
        if (hand == EnumFacing.DOWN) {
            return EnumActionResult.FAIL;
        }
        final IBlockState iblockstate = playerIn.getBlockState(worldIn);
        final Block block = iblockstate.getBlock();
        final boolean flag = block.isReplaceable(playerIn, worldIn);
        if (!flag) {
            if (!playerIn.getBlockState(worldIn).getMaterial().isSolid()) {
                return EnumActionResult.FAIL;
            }
            worldIn = worldIn.offset(hand);
        }
        final ItemStack itemstack = stack.getHeldItem(pos);
        if (!stack.canPlayerEdit(worldIn, hand, itemstack) || !Blocks.SKULL.canPlaceBlockAt(playerIn, worldIn)) {
            return EnumActionResult.FAIL;
        }
        if (playerIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        playerIn.setBlockState(worldIn, Blocks.SKULL.getDefaultState().withProperty((IProperty<Comparable>)BlockSkull.FACING, hand), 11);
        int i = 0;
        if (hand == EnumFacing.UP) {
            i = (MathHelper.floor(stack.rotationYaw * 16.0f / 360.0f + 0.5) & 0xF);
        }
        final TileEntity tileentity = playerIn.getTileEntity(worldIn);
        if (tileentity instanceof TileEntitySkull) {
            final TileEntitySkull tileentityskull = (TileEntitySkull)tileentity;
            if (itemstack.getMetadata() == 3) {
                GameProfile gameprofile = null;
                if (itemstack.hasTagCompound()) {
                    final NBTTagCompound nbttagcompound = itemstack.getTagCompound();
                    if (nbttagcompound.hasKey("SkullOwner", 10)) {
                        gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
                    }
                    else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isBlank(nbttagcompound.getString("SkullOwner"))) {
                        gameprofile = new GameProfile(null, nbttagcompound.getString("SkullOwner"));
                    }
                }
                tileentityskull.setPlayerProfile(gameprofile);
            }
            else {
                tileentityskull.setType(itemstack.getMetadata());
            }
            tileentityskull.setSkullRotation(i);
            Blocks.SKULL.checkWitherSpawn(playerIn, worldIn, tileentityskull);
        }
        if (stack instanceof EntityPlayerMP) {
            CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)stack, worldIn, itemstack);
        }
        itemstack.func_190918_g(1);
        return EnumActionResult.SUCCESS;
    }
    
    @Override
    public void getSubItems(final CreativeTabs itemIn, final NonNullList<ItemStack> tab) {
        if (this.func_194125_a(itemIn)) {
            for (int i = 0; i < ItemSkull.SKULL_TYPES.length; ++i) {
                tab.add(new ItemStack(this, 1, i));
            }
        }
    }
    
    @Override
    public int getMetadata(final int damage) {
        return damage;
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        int i = stack.getMetadata();
        if (i < 0 || i >= ItemSkull.SKULL_TYPES.length) {
            i = 0;
        }
        return String.valueOf(super.getUnlocalizedName()) + "." + ItemSkull.SKULL_TYPES[i];
    }
    
    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        if (stack.getMetadata() == 3 && stack.hasTagCompound()) {
            if (stack.getTagCompound().hasKey("SkullOwner", 8)) {
                return I18n.translateToLocalFormatted("item.skull.player.name", stack.getTagCompound().getString("SkullOwner"));
            }
            if (stack.getTagCompound().hasKey("SkullOwner", 10)) {
                final NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("SkullOwner");
                if (nbttagcompound.hasKey("Name", 8)) {
                    return I18n.translateToLocalFormatted("item.skull.player.name", nbttagcompound.getString("Name"));
                }
            }
        }
        return super.getItemStackDisplayName(stack);
    }
    
    @Override
    public boolean updateItemStackNBT(final NBTTagCompound nbt) {
        super.updateItemStackNBT(nbt);
        if (nbt.hasKey("SkullOwner", 8) && !StringUtils.isBlank(nbt.getString("SkullOwner"))) {
            GameProfile gameprofile = new GameProfile(null, nbt.getString("SkullOwner"));
            gameprofile = TileEntitySkull.updateGameprofile(gameprofile);
            nbt.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
            return true;
        }
        return false;
    }
}
