// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerShulkerBox extends Container
{
    private final IInventory field_190899_a;
    
    public ContainerShulkerBox(final InventoryPlayer p_i47266_1_, final IInventory p_i47266_2_, final EntityPlayer p_i47266_3_) {
        (this.field_190899_a = p_i47266_2_).openInventory(p_i47266_3_);
        final int i = 3;
        final int j = 9;
        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 9; ++l) {
                this.addSlotToContainer(new SlotShulkerBox(p_i47266_2_, l + k * 9, 8 + l * 18, 18 + k * 18));
            }
        }
        for (int i2 = 0; i2 < 3; ++i2) {
            for (int k2 = 0; k2 < 9; ++k2) {
                this.addSlotToContainer(new Slot(p_i47266_1_, k2 + i2 * 9 + 9, 8 + k2 * 18, 84 + i2 * 18));
            }
        }
        for (int j2 = 0; j2 < 9; ++j2) {
            this.addSlotToContainer(new Slot(p_i47266_1_, j2, 8 + j2 * 18, 142));
        }
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return this.field_190899_a.isUsableByPlayer(playerIn);
    }
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack itemstack = ItemStack.field_190927_a;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            if (index < this.field_190899_a.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack2, this.field_190899_a.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.field_190927_a;
                }
            }
            else if (!this.mergeItemStack(itemstack2, 0, this.field_190899_a.getSizeInventory(), false)) {
                return ItemStack.field_190927_a;
            }
            if (itemstack2.func_190926_b()) {
                slot.putStack(ItemStack.field_190927_a);
            }
            else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }
    
    @Override
    public void onContainerClosed(final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.field_190899_a.closeInventory(playerIn);
    }
}
