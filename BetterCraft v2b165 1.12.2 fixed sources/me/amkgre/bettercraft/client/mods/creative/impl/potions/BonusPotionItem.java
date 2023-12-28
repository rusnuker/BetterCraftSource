// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.creative.impl.potions;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import me.amkgre.bettercraft.client.mods.creative.InventoryItem;

public class BonusPotionItem extends InventoryItem
{
    public BonusPotionItem() {
        super(generate(), "§dBonus Potion\n\n§7You are stronger then hulk");
    }
    
    public static ItemStack generate() {
        final ItemStack itm = new ItemStack(Items.POTIONITEM);
        try {
            itm.setTagCompound(JsonToNBT.getTagFromJson("{CustomPotionEffects:[{Id:1,Amplifier:4,Duration:22220},{Id:3,Amplifier:4,Duration:22220},{Id:5,Amplifier:4,Duration:22220},{Id:6,Amplifier:4,Duration:22220},{Id:8,Amplifier:4,Duration:22220},{Id:10,Amplifier:4,Duration:22220},{Id:11,Amplifier:4,Duration:22220},{Id:12,Amplifier:4,Duration:22220},{Id:13,Amplifier:4,Duration:22220},{Id:16,Amplifier:4,Duration:22220},{Id:21,Amplifier:4,Duration:22220},{Id:22,Amplifier:4,Duration:22220},{Id:23,Amplifier:4,Duration:22220}],}"));
        }
        catch (final Exception ex) {}
        return itm;
    }
}
