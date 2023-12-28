// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.ircclient;

import me.amkgre.bettercraft.client.Client;
import net.minecraft.client.Minecraft;

public class IrcManager
{
    public void onCommand(final String msg) {
        final Minecraft mc = Minecraft.getMinecraft();
        if (msg.startsWith("/") && msg.length() > 1) {
            Client.getInstance().getIrcCmdManager().executeCommand(msg);
        }
    }
}
