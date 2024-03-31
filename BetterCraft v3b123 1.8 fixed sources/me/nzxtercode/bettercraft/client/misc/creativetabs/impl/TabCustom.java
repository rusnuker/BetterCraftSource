// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.creativetabs.impl;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import java.util.List;
import me.nzxtercode.bettercraft.client.misc.creativetabs.TabsCreative;

public abstract class TabCustom extends TabsCreative
{
    public TabCustom(final int index, final String label) {
        super(index, label);
    }
    
    @Override
    public void displayAllReleventItems(final List<ItemStack> itemList) {
        try {
            final ItemStack slim = new ItemStack(Items.spawn_egg, 1, 55);
            final NBTTagCompound nbt1 = new NBTTagCompound();
            nbt1.setDouble("PosX", Double.NaN);
            slim.stackTagCompound = nbt1;
            slim.setStackDisplayName("Big Slime");
            slim.setStackDisplayLore("This is a simple slime");
            itemList.add(slim);
            final ItemStack betterarmorstand = new ItemStack(Items.armor_stand);
            final String nbt2 = "{EntityTag:{ShowArms:1,NoBasePlate:1}}";
            betterarmorstand.setTagCompound(JsonToNBT.getTagFromJson(nbt2));
            betterarmorstand.setStackDisplayName("BetterArmorStand");
            betterarmorstand.setStackDisplayLore("This is a better armorstand");
            itemList.add(betterarmorstand);
        }
        catch (final Exception ex) {}
    }
    
    @Override
    public String getTranslatedTabLabel() {
        return "Custom";
    }
}
