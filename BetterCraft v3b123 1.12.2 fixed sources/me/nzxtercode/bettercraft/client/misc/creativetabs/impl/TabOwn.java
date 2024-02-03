// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.creativetabs.impl;

import java.util.Collection;
import me.nzxtercode.bettercraft.client.misc.creativetabs.TabCreativeOwnItems;
import net.minecraft.item.ItemStack;
import java.util.List;
import me.nzxtercode.bettercraft.client.misc.creativetabs.TabsCreative;

public abstract class TabOwn extends TabsCreative
{
    public TabOwn(final int index, final String label) {
        super(index, label);
    }
    
    @Override
    public void displayAllReleventItems(final List<ItemStack> itemList) {
        itemList.addAll(TabCreativeOwnItems.getItemstacks());
    }
    
    @Override
    public String getTranslatedTabLabel() {
        return "Own";
    }
}
