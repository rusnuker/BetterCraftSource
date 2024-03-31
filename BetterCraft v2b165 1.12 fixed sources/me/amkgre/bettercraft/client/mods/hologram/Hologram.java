// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.hologram;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat;
import net.minecraft.item.ItemStack;

public class Hologram
{
    private String text;
    private String date;
    private double x;
    private double y;
    private double z;
    private ItemStack item;
    DateFormat df;
    Date today;
    String renderDate;
    DateFormat dff;
    Date todayy;
    String renderTime;
    
    public Hologram(final String text2, final double x, final double y, final double z) {
        this.df = new SimpleDateFormat("dd/MM/yyyy");
        this.today = Calendar.getInstance().getTime();
        this.renderDate = this.df.format(this.today);
        this.dff = new SimpleDateFormat("HH:mm:ss");
        this.todayy = Calendar.getInstance().getTime();
        this.renderTime = this.dff.format(this.todayy);
        this.setText(text2);
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        this.setDate(String.valueOf(String.valueOf(this.renderDate) + " | " + this.renderTime));
    }
    
    public void spawnHologram() {
        this.item = new ItemStack(Items.ARMOR_STAND);
        final NBTTagCompound base = new NBTTagCompound();
        final NBTTagCompound entityTag = new NBTTagCompound();
        entityTag.setString("CustomName", this.text);
        entityTag.setInteger("CustomNameVisible", 1);
        entityTag.setInteger("Invisible", 1);
        final NBTTagList Pos2 = new NBTTagList();
        Pos2.appendTag(new NBTTagDouble(this.x));
        Pos2.appendTag(new NBTTagDouble(this.y));
        Pos2.appendTag(new NBTTagDouble(this.z));
        entityTag.setTag("Pos", Pos2);
        base.setTag("EntityTag", entityTag);
        this.item.setTagCompound(base);
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCreativeInventoryAction(36, this.item));
        this.item.setTagCompound(null);
    }
    
    public ItemStack getItem() {
        return this.item;
    }
    
    public void setItem(final ItemStack item) {
        this.item = item;
    }
    
    public String getDate() {
        return this.date;
    }
    
    public String getText() {
        return this.text;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setDate(final String date) {
        this.date = date;
    }
    
    public void setText(final String text2) {
        this.text = text2;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
}
