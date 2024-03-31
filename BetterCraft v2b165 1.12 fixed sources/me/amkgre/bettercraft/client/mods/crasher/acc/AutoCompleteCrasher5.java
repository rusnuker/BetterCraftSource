// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.acc;

import net.minecraft.network.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketTabComplete;

public class AutoCompleteCrasher5
{
    public static void start() {
        final CPacketTabComplete complete = new CPacketTabComplete(" ", null, false);
        for (int packets = 0, j = 0; j < packets; ++j) {
            Minecraft.getMinecraft().player.connection.sendPacket(complete);
        }
    }
}
