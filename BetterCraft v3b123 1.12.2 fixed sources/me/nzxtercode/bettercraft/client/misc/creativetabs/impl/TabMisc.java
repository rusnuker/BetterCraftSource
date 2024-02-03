// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.creativetabs.impl;

import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import java.util.List;
import me.nzxtercode.bettercraft.client.misc.creativetabs.TabsCreative;

public abstract class TabMisc extends TabsCreative
{
    public TabMisc(final int index, final String label) {
        super(index, label);
    }
    
    @Override
    public void displayAllReleventItems(final List<ItemStack> itemList) {
        try {
            final ItemStack commandBlock = new ItemStack(Blocks.command_block);
            itemList.add(commandBlock);
            final ItemStack commandblockminecart = new ItemStack(Items.command_block_minecart);
            itemList.add(commandblockminecart);
            final ItemStack enderDragonEgg = new ItemStack(Blocks.dragon_egg);
            itemList.add(enderDragonEgg);
            final ItemStack barrier = new ItemStack(Blocks.barrier);
            itemList.add(barrier);
            final ItemStack spawner = new ItemStack(Blocks.mob_spawner);
            itemList.add(spawner);
            final ItemStack redmushroom = new ItemStack(Blocks.red_mushroom_block);
            itemList.add(redmushroom);
            final ItemStack brownmushroom = new ItemStack(Blocks.brown_mushroom_block);
            itemList.add(brownmushroom);
        }
        catch (final Exception ex) {}
    }
    
    @Override
    public String getTranslatedTabLabel() {
        return "Misc";
    }
}
