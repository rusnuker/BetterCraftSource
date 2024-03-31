// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.inventory;

import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.item.ItemStack;

public class SlotShulkerBox extends Slot
{
    public SlotShulkerBox(final IInventory p_i47265_1_, final int p_i47265_2_, final int p_i47265_3_, final int p_i47265_4_) {
        super(p_i47265_1_, p_i47265_2_, p_i47265_3_, p_i47265_4_);
    }
    
    @Override
    public boolean isItemValid(final ItemStack stack) {
        return !(Block.getBlockFromItem(stack.getItem()) instanceof BlockShulkerBox);
    }
}
