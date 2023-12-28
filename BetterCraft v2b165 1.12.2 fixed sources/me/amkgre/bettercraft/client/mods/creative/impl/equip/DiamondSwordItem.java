// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.creative.impl.equip;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import me.amkgre.bettercraft.client.mods.creative.InventoryItem;

public class DiamondSwordItem extends InventoryItem
{
    public DiamondSwordItem() {
        super(generate(), "");
    }
    
    public static ItemStack generate() {
        final ItemStack itm = new ItemStack(Items.DIAMOND_SWORD);
        try {
            itm.setTagCompound(JsonToNBT.getTagFromJson("{HideFlags:7,AttributeModifiers:[{AttributeName:\"generic.attackDamage\",Name:\"generic.attackDamage\",Amount:9999999,Operation:0,UUIDLeast:934093,UUIDMost:416361}],Unbreakable:1}"));
        }
        catch (final Exception ex) {}
        return itm;
    }
}
