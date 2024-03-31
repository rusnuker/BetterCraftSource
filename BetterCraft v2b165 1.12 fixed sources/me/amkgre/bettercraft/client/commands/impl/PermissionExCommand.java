// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.commands.Command;

public class PermissionExCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage("/minecraft:execute @e ~ ~ ~ pex group default add permissions.*"));
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage("/execute @e ~ ~ ~ pex group default add permissions.*"));
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage("/gamerule sendCommandfeedback false"));
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage("/minecraft:gamerule sendCommandfeedback false"));
        }
    }
    
    @Override
    public String getName() {
        return "pex";
    }
}
