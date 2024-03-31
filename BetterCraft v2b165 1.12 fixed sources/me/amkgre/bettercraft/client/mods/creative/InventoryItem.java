// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.creative;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class InventoryItem
{
    private ItemStack item;
    private InventorySection section;
    private String toolTip;
    
    public InventoryItem(final ItemStack item, final String toolTip) {
        this.setItem(item);
        this.setToolTip(toolTip);
    }
    
    public static ItemStack spawnHologram(final String text2) {
        final ItemStack holo = new ItemStack(Items.ARMOR_STAND);
        final NBTTagCompound base = new NBTTagCompound();
        final NBTTagCompound entityTag = new NBTTagCompound();
        entityTag.setString("CustomName", text2);
        entityTag.setInteger("CustomNameVisible", 1);
        entityTag.setInteger("Invisible", 1);
        final NBTTagList Pos2 = new NBTTagList();
        Pos2.appendTag(new NBTTagDouble(Minecraft.getMinecraft().player.posX));
        Pos2.appendTag(new NBTTagDouble(Minecraft.getMinecraft().player.posY));
        Pos2.appendTag(new NBTTagDouble(Minecraft.getMinecraft().player.posZ));
        entityTag.setTag("Pos", Pos2);
        base.setTag("EntityTag", entityTag);
        holo.setTagCompound(base);
        return holo;
    }
    
    public void setToolTip(final String toolTip) {
        this.toolTip = toolTip;
    }
    
    public String getToolTip() {
        return this.toolTip;
    }
    
    public void setSection(final InventorySection section) {
        this.section = section;
    }
    
    public InventorySection getSection() {
        return this.section;
    }
    
    public void setItem(final ItemStack item) {
        this.item = item;
    }
    
    public ItemStack getItem() {
        return this.item;
    }
}
