// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.init.Blocks;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemFireball extends Item
{
    public ItemFireball() {
        this.setCreativeTab(CreativeTabs.MISC);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer stack, final World playerIn, BlockPos worldIn, final EnumHand pos, final EnumFacing hand, final float facing, final float hitX, final float hitY) {
        if (playerIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        worldIn = worldIn.offset(hand);
        final ItemStack itemstack = stack.getHeldItem(pos);
        if (!stack.canPlayerEdit(worldIn, hand, itemstack)) {
            return EnumActionResult.FAIL;
        }
        if (playerIn.getBlockState(worldIn).getMaterial() == Material.AIR) {
            playerIn.playSound(null, worldIn, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0f, (ItemFireball.itemRand.nextFloat() - ItemFireball.itemRand.nextFloat()) * 0.2f + 1.0f);
            playerIn.setBlockState(worldIn, Blocks.FIRE.getDefaultState());
        }
        if (!stack.capabilities.isCreativeMode) {
            itemstack.func_190918_g(1);
        }
        return EnumActionResult.SUCCESS;
    }
}
