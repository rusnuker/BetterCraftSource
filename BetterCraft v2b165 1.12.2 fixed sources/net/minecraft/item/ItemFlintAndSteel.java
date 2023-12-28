// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
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

public class ItemFlintAndSteel extends Item
{
    public ItemFlintAndSteel() {
        this.maxStackSize = 1;
        this.setMaxDamage(64);
        this.setCreativeTab(CreativeTabs.TOOLS);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer stack, final World playerIn, BlockPos worldIn, final EnumHand pos, final EnumFacing hand, final float facing, final float hitX, final float hitY) {
        worldIn = worldIn.offset(hand);
        final ItemStack itemstack = stack.getHeldItem(pos);
        if (!stack.canPlayerEdit(worldIn, hand, itemstack)) {
            return EnumActionResult.FAIL;
        }
        if (playerIn.getBlockState(worldIn).getMaterial() == Material.AIR) {
            playerIn.playSound(stack, worldIn, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, ItemFlintAndSteel.itemRand.nextFloat() * 0.4f + 0.8f);
            playerIn.setBlockState(worldIn, Blocks.FIRE.getDefaultState(), 11);
        }
        if (stack instanceof EntityPlayerMP) {
            CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)stack, worldIn, itemstack);
        }
        itemstack.damageItem(1, stack);
        return EnumActionResult.SUCCESS;
    }
}
