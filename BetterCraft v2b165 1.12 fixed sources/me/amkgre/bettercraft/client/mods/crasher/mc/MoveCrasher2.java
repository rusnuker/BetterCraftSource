// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.mc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.Minecraft;

public class MoveCrasher2
{
    public static void start() {
        Minecraft.getMinecraft().player.setPosition(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY + 0.3, Minecraft.getMinecraft().player.posZ);
        final double x = Minecraft.getMinecraft().player.posX;
        final double y = Minecraft.getMinecraft().player.posY;
        final double z = Minecraft.getMinecraft().player.posZ;
        for (int i = 0; i < 3000; ++i) {
            Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayer.Position(x, y + 0.09999999999999, z, false));
            Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayer.Position(x, y, z, true));
        }
        Minecraft.getMinecraft().player.motionY = 0.0;
    }
}
