// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.inventory;

import net.minecraft.village.MerchantRecipeList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.ITextComponent;
import java.util.List;
import java.util.Iterator;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.entity.IMerchant;

public class InventoryMerchant implements IInventory
{
    private final IMerchant theMerchant;
    private final NonNullList<ItemStack> theInventory;
    private final EntityPlayer thePlayer;
    private MerchantRecipe currentRecipe;
    private int currentRecipeIndex;
    
    public InventoryMerchant(final EntityPlayer thePlayerIn, final IMerchant theMerchantIn) {
        this.theInventory = NonNullList.func_191197_a(3, ItemStack.field_190927_a);
        this.thePlayer = thePlayerIn;
        this.theMerchant = theMerchantIn;
    }
    
    @Override
    public int getSizeInventory() {
        return this.theInventory.size();
    }
    
    @Override
    public boolean func_191420_l() {
        for (final ItemStack itemstack : this.theInventory) {
            if (!itemstack.func_190926_b()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        return this.theInventory.get(index);
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        final ItemStack itemstack = this.theInventory.get(index);
        if (index == 2 && !itemstack.func_190926_b()) {
            return ItemStackHelper.getAndSplit(this.theInventory, index, itemstack.func_190916_E());
        }
        final ItemStack itemstack2 = ItemStackHelper.getAndSplit(this.theInventory, index, count);
        if (!itemstack2.func_190926_b() && this.inventoryResetNeededOnSlotChange(index)) {
            this.resetRecipeAndSlots();
        }
        return itemstack2;
    }
    
    private boolean inventoryResetNeededOnSlotChange(final int slotIn) {
        return slotIn == 0 || slotIn == 1;
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        return ItemStackHelper.getAndRemove(this.theInventory, index);
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        this.theInventory.set(index, stack);
        if (!stack.func_190926_b() && stack.func_190916_E() > this.getInventoryStackLimit()) {
            stack.func_190920_e(this.getInventoryStackLimit());
        }
        if (this.inventoryResetNeededOnSlotChange(index)) {
            this.resetRecipeAndSlots();
        }
    }
    
    @Override
    public String getName() {
        return "mob.villager";
    }
    
    @Override
    public boolean hasCustomName() {
        return false;
    }
    
    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]);
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public boolean isUsableByPlayer(final EntityPlayer player) {
        return this.theMerchant.getCustomer() == player;
    }
    
    @Override
    public void openInventory(final EntityPlayer player) {
    }
    
    @Override
    public void closeInventory(final EntityPlayer player) {
    }
    
    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        return true;
    }
    
    @Override
    public void markDirty() {
        this.resetRecipeAndSlots();
    }
    
    public void resetRecipeAndSlots() {
        this.currentRecipe = null;
        ItemStack itemstack = this.theInventory.get(0);
        ItemStack itemstack2 = this.theInventory.get(1);
        if (itemstack.func_190926_b()) {
            itemstack = itemstack2;
            itemstack2 = ItemStack.field_190927_a;
        }
        if (itemstack.func_190926_b()) {
            this.setInventorySlotContents(2, ItemStack.field_190927_a);
        }
        else {
            final MerchantRecipeList merchantrecipelist = this.theMerchant.getRecipes(this.thePlayer);
            if (merchantrecipelist != null) {
                MerchantRecipe merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack, itemstack2, this.currentRecipeIndex);
                if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
                    this.currentRecipe = merchantrecipe;
                    this.setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
                }
                else if (!itemstack2.func_190926_b()) {
                    merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack2, itemstack, this.currentRecipeIndex);
                    if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
                        this.currentRecipe = merchantrecipe;
                        this.setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
                    }
                    else {
                        this.setInventorySlotContents(2, ItemStack.field_190927_a);
                    }
                }
                else {
                    this.setInventorySlotContents(2, ItemStack.field_190927_a);
                }
            }
            this.theMerchant.verifySellingItem(this.getStackInSlot(2));
        }
    }
    
    public MerchantRecipe getCurrentRecipe() {
        return this.currentRecipe;
    }
    
    public void setCurrentRecipeIndex(final int currentRecipeIndexIn) {
        this.currentRecipeIndex = currentRecipeIndexIn;
        this.resetRecipeAndSlots();
    }
    
    @Override
    public int getField(final int id) {
        return 0;
    }
    
    @Override
    public void setField(final int id, final int value) {
    }
    
    @Override
    public int getFieldCount() {
        return 0;
    }
    
    @Override
    public void clear() {
        this.theInventory.clear();
    }
}
