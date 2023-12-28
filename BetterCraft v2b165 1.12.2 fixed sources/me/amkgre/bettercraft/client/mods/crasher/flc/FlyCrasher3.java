// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.flc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.Minecraft;

public class FlyCrasher3
{
    public static void start() {
        final double x = Minecraft.getMinecraft().player.posX;
        final double y = Minecraft.getMinecraft().player.posY;
        final double z = Minecraft.getMinecraft().player.posZ;
        for (int i = 0; i < 3000; ++i) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(x, y + 0.09999999999999, z, false));
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, true));
        }
        Minecraft.getMinecraft().player.motionY = 0.0;
        for (int i = 0; i < 10000; ++i) {
            final double x2 = Minecraft.getMinecraft().player.posX;
            final double y2 = Minecraft.getMinecraft().player.posY;
            final double z2 = Minecraft.getMinecraft().player.posZ;
            for (int k = 0; k < 3000; ++k) {
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(x2, y2 + 0.09999999999999, z2, false));
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(x2, y2, z2, true));
            }
            Minecraft.getMinecraft().player.motionY = 0.0;
            try {
                Thread.sleep(30L);
            }
            catch (final InterruptedException ex) {}
        }
    }
}
