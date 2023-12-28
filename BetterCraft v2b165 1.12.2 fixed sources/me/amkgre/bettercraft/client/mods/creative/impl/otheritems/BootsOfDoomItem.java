// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.creative.impl.otheritems;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import me.amkgre.bettercraft.client.mods.creative.InventoryItem;

public class BootsOfDoomItem extends InventoryItem
{
    public BootsOfDoomItem() {
        super(generate(), "§dBoots of Doom\n\n§7This are the best boots");
    }
    
    public static ItemStack generate() {
        final ItemStack itm = new ItemStack(Items.IRON_BOOTS);
        try {
            itm.setTagCompound(JsonToNBT.getTagFromJson("{AttributeModifiers:[{AttributeName:\"generic.maxHealth\",Name:\"generic.maxHealth\",Amount:1000,Operation:0,UUIDLeast:645221,UUIDMost:376184},{AttributeName:\"generic.followRange\",Name:\"generic.followRange\",Amount:1000,Operation:0,UUIDLeast:811344,UUIDMost:604026},{AttributeName:\"generic.knockbackResistance\",Name:\"generic.knockbackResistance\",Amount:1000,Operation:0,UUIDLeast:144031,UUIDMost:433711},{AttributeName:\"generic.movementSpeed\",Name:\"generic.movementSpeed\",Amount:29,Operation:0,UUIDLeast:296129,UUIDMost:936638},{AttributeName:\"generic.attackDamage\",Name:\"generic.attackDamage\",Amount:1000,Operation:0,UUIDLeast:705806,UUIDMost:812221},{AttributeName:\"generic.armor\",Name:\"generic.armor\",Amount:1000,Operation:0,UUIDLeast:326608,UUIDMost:542640},{AttributeName:\"generic.attackSpeed\",Name:\"generic.attackSpeed\",Amount:1000,Operation:0,UUIDLeast:644756,UUIDMost:802627},{AttributeName:\"generic.luck\",Name:\"generic.luck\",Amount:1000,Operation:0,UUIDLeast:596795,UUIDMost:622000},{AttributeName:\"generic.armorToughness\",Name:\"generic.armorToughness\",Amount:1000,Operation:0,UUIDLeast:831947,UUIDMost:997513}],Unbreakable:1,ench:[{id:0,lvl:1000},{id:1,lvl:1000},{id:2,lvl:1000},{id:3,lvl:1000},{id:4,lvl:1000},{id:7,lvl:1000},{id:8,lvl:1000},{id:9,lvl:1000},{id:34,lvl:1000},{id:70,lvl:1000}]}"));
        }
        catch (final Exception ex) {}
        return itm;
    }
}
