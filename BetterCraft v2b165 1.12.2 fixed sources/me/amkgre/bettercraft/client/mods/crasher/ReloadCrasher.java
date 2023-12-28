// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher;

import net.minecraft.client.Minecraft;

public class ReloadCrasher
{
    public static void start() {
        for (int i = 0; i < 15; ++i) {
            Minecraft.getMinecraft().player.sendChatMessage("/reload");
        }
    }
}
