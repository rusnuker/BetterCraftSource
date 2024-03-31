// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.commands.Command;

public class CheckCmdBlockCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            if (!Minecraft.getMinecraft().isSingleplayer()) {
                final PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
                buf.writeString("§eCheck");
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload("MC|AdvCdm", buf));
            }
            else {
                Command.clientMSG("§7Only in Multiplayer", true);
            }
        }
    }
    
    @Override
    public String getName() {
        return "cmdblock";
    }
}
