// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.creative.impl.potions;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import me.amkgre.bettercraft.client.mods.creative.InventoryItem;

public class StackUpPotionItem extends InventoryItem
{
    public StackUpPotionItem() {
        super(generate(), "§dStack Up Potion\n\n§7Get guys fly away");
    }
    
    public static ItemStack generate() {
        final ItemStack itm = new ItemStack(Items.SPLASH_POTION);
        try {
            itm.setTagCompound(JsonToNBT.getTagFromJson("{CustomPotionEffects:[{Id:1b,Amplifier:10b,Duration:9999},{Id:2b,Amplifier:10b,Duration:9999},{Id:3b,Amplifier:10b,Duration:9999},{Id:4b,Amplifier:10b,Duration:9999},{Id:5b,Amplifier:10b,Duration:9999},{Id:6b,Amplifier:10b,Duration:9999},{Id:7b,Amplifier:10b,Duration:9999},{Id:8b,Amplifier:10b,Duration:9999},{Id:9b,Amplifier:10b,Duration:9999},{Id:10b,Amplifier:10b,Duration:9999},{Id:11b,Amplifier:10b,Duration:9999},{Id:12b,Amplifier:10b,Duration:9999},{Id:13b,Amplifier:10b,Duration:9999},{Id:14b,Amplifier:10b,Duration:9999},{Id:15b,Amplifier:10b,Duration:9999},{Id:16b,Amplifier:10b,Duration:9999},{Id:17b,Amplifier:10b,Duration:9999},{Id:18b,Amplifier:10b,Duration:9999},{Id:19b,Amplifier:10b,Duration:9999},{Id:20b,Amplifier:10b,Duration:9999},{Id:21b,Amplifier:10b,Duration:9999},{Id:22b,Amplifier:10b,Duration:9999},{Id:23b,Amplifier:10b,Duration:9999},{Id:24b,Amplifier:10b,Duration:9999},{Id:25b,Amplifier:10b,Duration:9999},{Id:26b,Amplifier:10b,Duration:9999},{Id:27b,Amplifier:10b,Duration:9999},{Id:28b,Amplifier:10b,Duration:9999},{Id:29b,Amplifier:10b,Duration:9999},{Id:30b,Amplifier:10b,Duration:9999},{Id:31b,Amplifier:10b,Duration:9999},{Id:32b,Amplifier:10b,Duration:9999}],CustomPotionColor:0}"));
        }
        catch (final Exception ex) {}
        return itm;
    }
}
