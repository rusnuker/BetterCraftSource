// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.ims;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.client.Minecraft;

public class ItemCrasher3
{
    public static void start() {
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketHeldItemChange(2));
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer(true));
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketHeldItemChange(3));
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer(true));
    }
}
