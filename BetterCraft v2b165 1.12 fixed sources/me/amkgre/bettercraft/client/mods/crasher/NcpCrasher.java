// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.Minecraft;

public class NcpCrasher
{
    public static void start() {
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Rotation(9.223372E18f, 9.223372E18f, true));
    }
}
