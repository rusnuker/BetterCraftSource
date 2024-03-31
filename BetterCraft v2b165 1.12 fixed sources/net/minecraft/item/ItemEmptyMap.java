// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;

public class ItemEmptyMap extends ItemMapBase
{
    protected ItemEmptyMap() {
        this.setCreativeTab(CreativeTabs.MISC);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World itemStackIn, final EntityPlayer worldIn, final EnumHand playerIn) {
        final ItemStack itemstack = ItemMap.func_190906_a(itemStackIn, worldIn.posX, worldIn.posZ, (byte)0, true, false);
        final ItemStack itemstack2 = worldIn.getHeldItem(playerIn);
        itemstack2.func_190918_g(1);
        if (itemstack2.func_190926_b()) {
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        if (!worldIn.inventory.addItemStackToInventory(itemstack.copy())) {
            worldIn.dropItem(itemstack, false);
        }
        worldIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack2);
    }
}
