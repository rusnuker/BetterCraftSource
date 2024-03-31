// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.creative.impl.potions;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import me.amkgre.bettercraft.client.mods.creative.InventoryItem;

public class CreativeKillerPotionItem extends InventoryItem
{
    public CreativeKillerPotionItem() {
        super(generate(), "§dCreative Killer Potion\n\n§7With this potion you can kill any entity also creative players");
    }
    
    public static ItemStack generate() {
        final ItemStack itm = new ItemStack(Items.SPLASH_POTION);
        try {
            itm.setTagCompound(JsonToNBT.getTagFromJson("{HideFlags:63,CustomPotionEffects:[{Id:6b,Amplifier:125b,Duration:1}],CustomPotionColor:21972}"));
        }
        catch (final Exception ex) {}
        return itm;
    }
}
