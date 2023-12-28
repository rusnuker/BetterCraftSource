// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.creative.impl.otheritems;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import me.amkgre.bettercraft.client.mods.creative.InventoryItem;

public class UltraEnchantedBookItem extends InventoryItem
{
    public UltraEnchantedBookItem() {
        super(generate(), "§dUltra Enchanted Book\n\n§7Ultra ench");
    }
    
    public static ItemStack generate() {
        final ItemStack itm = new ItemStack(Items.ENCHANTED_BOOK);
        try {
            itm.setTagCompound(JsonToNBT.getTagFromJson("{AttributeModifiers:[{AttributeName:\"generic.maxHealth\",Name:\"generic.maxHealth\",Amount:40,Operation:0,UUIDLeast:822841,UUIDMost:150445},{AttributeName:\"generic.knockbackResistance\",Name:\"generic.knockbackResistance\",Amount:100,Operation:0,UUIDLeast:746718,UUIDMost:148647},{AttributeName:\"generic.movementSpeed\",Name:\"generic.movementSpeed\",Amount:100,Operation:0,UUIDLeast:492790,UUIDMost:484247},{AttributeName:\"generic.attackDamage\",Name:\"generic.attackDamage\",Amount:1000000,Operation:0,UUIDLeast:264817,UUIDMost:221810},{AttributeName:\"generic.armor\",Name:\"generic.armor\",Amount:1000,Operation:0,UUIDLeast:30294,UUIDMost:247968},{AttributeName:\"generic.attackSpeed\",Name:\"generic.attackSpeed\",Amount:1000,Operation:0,UUIDLeast:131249,UUIDMost:153309},{AttributeName:\"generic.luck\",Name:\"generic.luck\",Amount:10000,Operation:0,UUIDLeast:567123,UUIDMost:622451},{AttributeName:\"generic.armorToughness\",Name:\"generic.armorToughness\",Amount:1000,Operation:0,UUIDLeast:603460,UUIDMost:562239}],StoredEnchantments:[{id:0,lvl:32767},{id:1,lvl:32767},{id:2,lvl:32767},{id:3,lvl:32767},{id:4,lvl:32767},{id:5,lvl:32767},{id:6,lvl:32767},{id:7,lvl:32767},{id:8,lvl:32767},{id:9,lvl:32767},{id:16,lvl:32767},{id:17,lvl:32767},{id:18,lvl:32767},{id:19,lvl:32767},{id:20,lvl:32767},{id:21,lvl:32767},{id:22,lvl:1},{id:32,lvl:32767},{id:34,lvl:32767},{id:35,lvl:32767},{id:48,lvl:32767},{id:49,lvl:32767},{id:50,lvl:32767},{id:51,lvl:32767},{id:61,lvl:32767},{id:62,lvl:32767},{id:70,lvl:1}]}"));
        }
        catch (final Exception ex) {}
        return itm;
    }
}
