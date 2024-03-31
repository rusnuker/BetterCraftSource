// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.creative.impl.otheritems;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import me.amkgre.bettercraft.client.mods.creative.InventoryItem;

public class MultiColorFirework2Item extends InventoryItem
{
    public MultiColorFirework2Item() {
        super(generate(), "§dMulti Color Firework\n\n§7firework with multicolor");
    }
    
    public static ItemStack generate() {
        final ItemStack itm = new ItemStack(Items.FIREWORKS);
        try {
            itm.setTagCompound(JsonToNBT.getTagFromJson("{Fireworks:{Flight:4b,Explosions:[{Type:0,Flicker:1b,Trail:1b,Colors:[I;10697727],FadeColors:[I;12901631]},{Type:1,Flicker:1b,Trail:1b,Colors:[I;16757791,917342],FadeColors:[I;16715743]},{Type:3,Flicker:1b,Trail:1b,Colors:[I;4659711],FadeColors:[I;65467]},{Type:3,Flicker:1b,Trail:1b,Colors:[I;16732012],FadeColors:[I;16748075]},{Type:4,Flicker:1b,Trail:1b,Colors:[I;9219071],FadeColors:[I;16773148]}]}}"));
        }
        catch (final Exception ex) {}
        return itm;
    }
}
