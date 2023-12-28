// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.util.NonNullList;
import net.minecraft.creativetab.CreativeTabs;

public class ItemCoal extends Item
{
    public ItemCoal() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.MATERIALS);
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        return (stack.getMetadata() == 1) ? "item.charcoal" : "item.coal";
    }
    
    @Override
    public void getSubItems(final CreativeTabs itemIn, final NonNullList<ItemStack> tab) {
        if (this.func_194125_a(itemIn)) {
            tab.add(new ItemStack(this, 1, 0));
            tab.add(new ItemStack(this, 1, 1));
        }
    }
}
