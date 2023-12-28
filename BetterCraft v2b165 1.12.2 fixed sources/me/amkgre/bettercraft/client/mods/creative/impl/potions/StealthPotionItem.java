// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.creative.impl.potions;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import me.amkgre.bettercraft.client.mods.creative.InventoryItem;

public class StealthPotionItem extends InventoryItem
{
    public StealthPotionItem() {
        super(generate(), "§dStealth Potion\n\n§7Lets you to be stealth player");
    }
    
    public static ItemStack generate() {
        final ItemStack itm = new ItemStack(Items.POTIONITEM);
        try {
            itm.setTagCompound(JsonToNBT.getTagFromJson("{CustomPotionEffects:[{Id:1,Amplifier:10,Duration:20000,ShowParticles:0b},{Id:3,Amplifier:20,Duration:20000,ShowParticles:0b},{Id:5,Amplifier:15,Duration:20000,ShowParticles:0b},{Id:6,Amplifier:10,Duration:20000,ShowParticles:0b},{Id:8,Amplifier:10,Duration:20000,ShowParticles:0b},{Id:10,Amplifier:500,Duration:20000,ShowParticles:0b},{Id:11,Amplifier:20,Duration:20000,ShowParticles:0b},{Id:12,Amplifier:10,Duration:20000,ShowParticles:0b},{Id:13,Amplifier:10,Duration:20000,ShowParticles:0b},{Id:14,Amplifier:10,Duration:20000,ShowParticles:0b},{Id:16,Amplifier:10,Duration:20000,ShowParticles:0b},{Id:21,Amplifier:100,Duration:20000,ShowParticles:0b},{Id:22,Amplifier:100,Duration:20000,ShowParticles:0b},{Id:23,Amplifier:10,Duration:20000,ShowParticles:0b},{Id:26,Amplifier:100,Duration:20000,ShowParticles:0b}],CustomPotionColor:16711680}"));
        }
        catch (final Exception ex) {}
        return itm;
    }
}
