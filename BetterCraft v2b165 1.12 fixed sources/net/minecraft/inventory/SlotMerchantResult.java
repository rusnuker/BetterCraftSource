// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.inventory;

import net.minecraft.village.MerchantRecipe;
import net.minecraft.stats.StatList;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;

public class SlotMerchantResult extends Slot
{
    private final InventoryMerchant theMerchantInventory;
    private final EntityPlayer thePlayer;
    private int removeCount;
    private final IMerchant theMerchant;
    
    public SlotMerchantResult(final EntityPlayer player, final IMerchant merchant, final InventoryMerchant merchantInventory, final int slotIndex, final int xPosition, final int yPosition) {
        super(merchantInventory, slotIndex, xPosition, yPosition);
        this.thePlayer = player;
        this.theMerchant = merchant;
        this.theMerchantInventory = merchantInventory;
    }
    
    @Override
    public boolean isItemValid(final ItemStack stack) {
        return false;
    }
    
    @Override
    public ItemStack decrStackSize(final int amount) {
        if (this.getHasStack()) {
            this.removeCount += Math.min(amount, this.getStack().func_190916_E());
        }
        return super.decrStackSize(amount);
    }
    
    @Override
    protected void onCrafting(final ItemStack stack, final int amount) {
        this.removeCount += amount;
        this.onCrafting(stack);
    }
    
    @Override
    protected void onCrafting(final ItemStack stack) {
        stack.onCrafting(this.thePlayer.world, this.thePlayer, this.removeCount);
        this.removeCount = 0;
    }
    
    @Override
    public ItemStack func_190901_a(final EntityPlayer p_190901_1_, final ItemStack p_190901_2_) {
        this.onCrafting(p_190901_2_);
        final MerchantRecipe merchantrecipe = this.theMerchantInventory.getCurrentRecipe();
        if (merchantrecipe != null) {
            final ItemStack itemstack = this.theMerchantInventory.getStackInSlot(0);
            final ItemStack itemstack2 = this.theMerchantInventory.getStackInSlot(1);
            if (this.doTrade(merchantrecipe, itemstack, itemstack2) || this.doTrade(merchantrecipe, itemstack2, itemstack)) {
                this.theMerchant.useRecipe(merchantrecipe);
                p_190901_1_.addStat(StatList.TRADED_WITH_VILLAGER);
                this.theMerchantInventory.setInventorySlotContents(0, itemstack);
                this.theMerchantInventory.setInventorySlotContents(1, itemstack2);
            }
        }
        return p_190901_2_;
    }
    
    private boolean doTrade(final MerchantRecipe trade, final ItemStack firstItem, final ItemStack secondItem) {
        final ItemStack itemstack = trade.getItemToBuy();
        final ItemStack itemstack2 = trade.getSecondItemToBuy();
        if (firstItem.getItem() == itemstack.getItem() && firstItem.func_190916_E() >= itemstack.func_190916_E()) {
            if (!itemstack2.func_190926_b() && !secondItem.func_190926_b() && itemstack2.getItem() == secondItem.getItem() && secondItem.func_190916_E() >= itemstack2.func_190916_E()) {
                firstItem.func_190918_g(itemstack.func_190916_E());
                secondItem.func_190918_g(itemstack2.func_190916_E());
                return true;
            }
            if (itemstack2.func_190926_b() && secondItem.func_190926_b()) {
                firstItem.func_190918_g(itemstack.func_190916_E());
                return true;
            }
        }
        return false;
    }
}
