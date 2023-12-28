// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.creative.impl.equip;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import me.amkgre.bettercraft.client.mods.creative.InventoryItem;

public class DiamondHelmetItem extends InventoryItem
{
    public DiamondHelmetItem() {
        super(generate(), "");
    }
    
    public static ItemStack generate() {
        final ItemStack itm = new ItemStack(Items.DIAMOND_HELMET);
        try {
            itm.setTagCompound(JsonToNBT.getTagFromJson("{ench: [ { id: 0, lvl: 999999 }, { id: 1, lvl: 999999 }, { id: 6, lvl: 999999 }, { id: 7, lvl: 999999 } ], Unbreakable: 1 }"));
        }
        catch (final Exception ex) {}
        return itm;
    }
}
