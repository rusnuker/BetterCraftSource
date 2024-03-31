// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.inventory;

import net.minecraft.util.NonNullList;
import net.minecraft.item.crafting.CraftingManager;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;

public class SlotCrafting extends Slot
{
    private final InventoryCrafting craftMatrix;
    private final EntityPlayer thePlayer;
    private int amountCrafted;
    
    public SlotCrafting(final EntityPlayer player, final InventoryCrafting craftingInventory, final IInventory inventoryIn, final int slotIndex, final int xPosition, final int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
        this.thePlayer = player;
        this.craftMatrix = craftingInventory;
    }
    
    @Override
    public boolean isItemValid(final ItemStack stack) {
        return false;
    }
    
    @Override
    public ItemStack decrStackSize(final int amount) {
        if (this.getHasStack()) {
            this.amountCrafted += Math.min(amount, this.getStack().func_190916_E());
        }
        return super.decrStackSize(amount);
    }
    
    @Override
    protected void onCrafting(final ItemStack stack, final int amount) {
        this.amountCrafted += amount;
        this.onCrafting(stack);
    }
    
    @Override
    protected void func_190900_b(final int p_190900_1_) {
        this.amountCrafted += p_190900_1_;
    }
    
    @Override
    protected void onCrafting(final ItemStack stack) {
        if (this.amountCrafted > 0) {
            stack.onCrafting(this.thePlayer.world, this.thePlayer, this.amountCrafted);
        }
        this.amountCrafted = 0;
        final InventoryCraftResult inventorycraftresult = (InventoryCraftResult)this.inventory;
        final IRecipe irecipe = inventorycraftresult.func_193055_i();
        if (irecipe != null && !irecipe.func_192399_d()) {
            this.thePlayer.func_192021_a(Lists.newArrayList(irecipe));
            inventorycraftresult.func_193056_a(null);
        }
    }
    
    @Override
    public ItemStack func_190901_a(final EntityPlayer p_190901_1_, final ItemStack p_190901_2_) {
        this.onCrafting(p_190901_2_);
        final NonNullList<ItemStack> nonnulllist = CraftingManager.getRemainingItems(this.craftMatrix, p_190901_1_.world);
        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
            final ItemStack itemstack2 = nonnulllist.get(i);
            if (!itemstack.func_190926_b()) {
                this.craftMatrix.decrStackSize(i, 1);
                itemstack = this.craftMatrix.getStackInSlot(i);
            }
            if (!itemstack2.func_190926_b()) {
                if (itemstack.func_190926_b()) {
                    this.craftMatrix.setInventorySlotContents(i, itemstack2);
                }
                else if (ItemStack.areItemsEqual(itemstack, itemstack2) && ItemStack.areItemStackTagsEqual(itemstack, itemstack2)) {
                    itemstack2.func_190917_f(itemstack.func_190916_E());
                    this.craftMatrix.setInventorySlotContents(i, itemstack2);
                }
                else if (!this.thePlayer.inventory.addItemStackToInventory(itemstack2)) {
                    this.thePlayer.dropItem(itemstack2, false);
                }
            }
        }
        return p_190901_2_;
    }
}
