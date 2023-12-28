// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.mc;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.Minecraft;

public class MoveCrasher3
{
    public static void start() {
        for (int i2 = 0; i2 < 2000; ++i2) {
            Minecraft.getMinecraft().getConnection().getNetworkManager().dispatchPacket(new CPacketPlayer.Position(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ, true), null);
            Minecraft.getMinecraft().getConnection().getNetworkManager().dispatchPacket(new CPacketPlayer.Position(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ, false), null);
        }
    }
}
