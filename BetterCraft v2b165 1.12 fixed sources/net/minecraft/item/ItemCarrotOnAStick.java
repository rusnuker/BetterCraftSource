// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.stats.StatList;
import net.minecraft.init.Items;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;

public class ItemCarrotOnAStick extends Item
{
    public ItemCarrotOnAStick() {
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
        this.setMaxStackSize(1);
        this.setMaxDamage(25);
    }
    
    @Override
    public boolean isFull3D() {
        return true;
    }
    
    @Override
    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World itemStackIn, final EntityPlayer worldIn, final EnumHand playerIn) {
        final ItemStack itemstack = worldIn.getHeldItem(playerIn);
        if (itemStackIn.isRemote) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        if (worldIn.isRiding() && worldIn.getRidingEntity() instanceof EntityPig) {
            final EntityPig entitypig = (EntityPig)worldIn.getRidingEntity();
            if (itemstack.getMaxDamage() - itemstack.getMetadata() >= 7 && entitypig.boost()) {
                itemstack.damageItem(7, worldIn);
                if (itemstack.func_190926_b()) {
                    final ItemStack itemstack2 = new ItemStack(Items.FISHING_ROD);
                    itemstack2.setTagCompound(itemstack.getTagCompound());
                    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack2);
                }
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
            }
        }
        worldIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
    }
}
