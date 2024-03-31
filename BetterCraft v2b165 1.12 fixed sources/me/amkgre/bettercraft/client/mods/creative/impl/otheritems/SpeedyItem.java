// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.creative.impl.otheritems;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import me.amkgre.bettercraft.client.mods.creative.InventoryItem;

public class SpeedyItem extends InventoryItem
{
    public SpeedyItem() {
        super(generate(), "§dSpeddy\n\n§7Run like the wind");
    }
    
    public static ItemStack generate() {
        final ItemStack itm = new ItemStack(Items.BLAZE_ROD);
        try {
            itm.setTagCompound(JsonToNBT.getTagFromJson("{AttributeModifiers:[{AttributeName:\"generic.movementSpeed\",Name:\"generic.movementSpeed\",Amount:10,Operation:0,UUIDMost:28665,UUIDLeast:810666},{AttributeName:\"generic.attackDamage\",Name:\"generic.attackDamage\",Amount:4,Operation:0,UUIDMost:8921,UUIDLeast:477291}],ench:[{id:48,lvl:5}],HideFlags:7,Unbreakable:1}"));
        }
        catch (final Exception ex) {}
        return itm;
    }
}
