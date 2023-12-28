// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumActionResult;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import net.minecraft.creativetab.CreativeTabs;

public class ItemElytra extends Item
{
    public ItemElytra() {
        this.maxStackSize = 1;
        this.setMaxDamage(432);
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
        this.addPropertyOverride(new ResourceLocation("broken"), new IItemPropertyGetter() {
            @Override
            public float apply(final ItemStack stack, @Nullable final World worldIn, @Nullable final EntityLivingBase entityIn) {
                return ItemElytra.isBroken(stack) ? 0.0f : 1.0f;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
    }
    
    public static boolean isBroken(final ItemStack stack) {
        return stack.getItemDamage() < stack.getMaxDamage() - 1;
    }
    
    @Override
    public boolean getIsRepairable(final ItemStack toRepair, final ItemStack repair) {
        return repair.getItem() == Items.LEATHER;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World itemStackIn, final EntityPlayer worldIn, final EnumHand playerIn) {
        final ItemStack itemstack = worldIn.getHeldItem(playerIn);
        final EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);
        final ItemStack itemstack2 = worldIn.getItemStackFromSlot(entityequipmentslot);
        if (itemstack2.func_190926_b()) {
            worldIn.setItemStackToSlot(entityequipmentslot, itemstack.copy());
            itemstack.func_190920_e(0);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }
}
