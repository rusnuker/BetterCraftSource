// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.commands.Command;

public class AuthMeBypassCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage("/xlogin changuepassword Hacked123 Hacked123"));
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage("/pswadminchange"));
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage("/cp unregister"));
        }
    }
    
    @Override
    public String getName() {
        return "authme";
    }
}
