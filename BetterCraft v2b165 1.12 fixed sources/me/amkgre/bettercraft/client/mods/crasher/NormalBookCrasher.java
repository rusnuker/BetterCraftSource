// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

public class NormalBookCrasher
{
    public static void start() {
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
    }
}
