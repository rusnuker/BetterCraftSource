// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import java.util.Random;
import net.minecraft.client.Minecraft;

public class OnGroundCrasher
{
    public static void start() {
        for (int i = 0; i < 3000; ++i) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer(new Random().nextBoolean()));
        }
    }
}
