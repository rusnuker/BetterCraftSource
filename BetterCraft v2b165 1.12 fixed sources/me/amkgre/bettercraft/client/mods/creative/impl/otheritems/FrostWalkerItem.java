// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.creative.impl.otheritems;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import me.amkgre.bettercraft.client.mods.creative.InventoryItem;

public class FrostWalkerItem extends InventoryItem
{
    public FrostWalkerItem() {
        super(generate(), "§dFrostwalker\n\n§7You can walk over ice");
    }
    
    public static ItemStack generate() {
        final ItemStack itm = new ItemStack(Items.LEATHER_BOOTS);
        try {
            itm.setTagCompound(JsonToNBT.getTagFromJson("{ench:[{id:9,lvl:17}]}"));
        }
        catch (final Exception ex) {}
        return itm;
    }
}
