// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.Minecraft;

public class WalkCrasher
{
    public static void start() {
        final float yaw = Minecraft.getMinecraft().player.rotationYaw;
        final int sped = 5;
        Minecraft.getMinecraft().player.setVelocity(5.0 * Math.cos(Math.toRadians(yaw + 90.0f)), 0.0, 5.0 * Math.sin(Math.toRadians(yaw + 90.0f)));
        if (!Minecraft.getMinecraft().player.onGround) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer(true));
            Minecraft.getMinecraft().player.setSprinting(true);
            Minecraft.getMinecraft().player.motionY = -0.05;
        }
        else if (Minecraft.getMinecraft().player.isCollidedHorizontally) {
            Minecraft.getMinecraft().player.motionY = 1.0;
        }
        Minecraft.getMinecraft().player.rotationYaw = 0.0f;
    }
}
