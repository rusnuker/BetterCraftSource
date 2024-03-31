// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

public class ItemBook extends Item
{
    @Override
    public boolean isItemTool(final ItemStack stack) {
        return stack.func_190916_E() == 1;
    }
    
    @Override
    public int getItemEnchantability() {
        return 1;
    }
}
