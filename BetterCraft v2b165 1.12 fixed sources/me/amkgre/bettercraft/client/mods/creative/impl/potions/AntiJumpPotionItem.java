// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.creative.impl.potions;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import me.amkgre.bettercraft.client.mods.creative.InventoryItem;

public class AntiJumpPotionItem extends InventoryItem
{
    public AntiJumpPotionItem() {
        super(generate(), "§dAnti Jump Potion\n\n§7Throw a player to no jump");
    }
    
    public static ItemStack generate() {
        final ItemStack itm = new ItemStack(Items.SPLASH_POTION);
        try {
            itm.setTagCompound(JsonToNBT.getTagFromJson("{CustomPotionEffects:[{Id:8,Amplifier:128,Duration:20000000}]}"));
        }
        catch (final Exception ex) {}
        return itm;
    }
}
