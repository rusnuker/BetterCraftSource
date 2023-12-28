// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import java.util.concurrent.CompletableFuture;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.commands.Command;

public class CrackedLoginCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.clientMSG("Register in cracked: §ebccrack123", true);
            CompletableFuture.runAsync(() -> {
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage("/login bccrack123 bccrack123"));
                try {
                    Thread.sleep(2000L);
                }
                catch (final InterruptedException ex) {}
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage("/register bccrack123 bccrack123"));
            });
        }
    }
    
    @Override
    public String getName() {
        return "cracked";
    }
}
