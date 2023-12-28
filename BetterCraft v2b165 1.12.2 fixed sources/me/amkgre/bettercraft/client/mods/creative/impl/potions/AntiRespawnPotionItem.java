// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.creative.impl.potions;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import me.amkgre.bettercraft.client.mods.creative.InventoryItem;

public class AntiRespawnPotionItem extends InventoryItem
{
    public AntiRespawnPotionItem() {
        super(generate(), "§dAnti Respawn Potion\n\n§7Throw a player to no respawn");
    }
    
    public static ItemStack generate() {
        final ItemStack itm = new ItemStack(Items.SPLASH_POTION);
        try {
            itm.setTagCompound(JsonToNBT.getTagFromJson("{CustomPotionEffects:[{Id:6,Amplifier:-24,Duration:0},{Id:10,Amplifier:-24,Duration:19999980},{Id:21,Amplifier:-24,Duration:19999980}]}"));
        }
        catch (final Exception ex) {}
        return itm;
    }
}
