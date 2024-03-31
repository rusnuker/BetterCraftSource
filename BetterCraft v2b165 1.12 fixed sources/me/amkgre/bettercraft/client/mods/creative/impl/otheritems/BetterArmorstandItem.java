// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.creative.impl.otheritems;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import me.amkgre.bettercraft.client.mods.creative.InventoryItem;

public class BetterArmorstandItem extends InventoryItem
{
    public BetterArmorstandItem() {
        super(generate(), "§dBetter Amorstand\n\n§7Armorstand without plate");
    }
    
    public static ItemStack generate() {
        final ItemStack itm = new ItemStack(Items.ARMOR_STAND);
        try {
            itm.setTagCompound(JsonToNBT.getTagFromJson("{EntityTag:{ShowArms:1,NoBasePlate:1}}"));
        }
        catch (final Exception ex) {}
        return itm;
    }
}
