// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.commands.CommandManager;
import me.amkgre.bettercraft.client.commands.Command;

public class SkullCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.clientMSG("", false);
            Command.clientMSG("§d" + CommandManager.syntax + "skull player", true);
        }
        else if (args.length == 1) {
            try {
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCreativeInventoryAction(36, this.generatePlayerSkull(args[0])));
            }
            catch (final Exception ex) {}
        }
        else {
            Command.clientMSG("§cType skull", true);
        }
    }
    
    public ItemStack generatePlayerSkull(final String playerName) {
        final ItemStack item = new ItemStack(Items.SKULL, 1, 3);
        final NBTTagCompound entityTag = new NBTTagCompound();
        entityTag.setString("SkullOwner", playerName);
        item.setTagCompound(entityTag);
        return item;
    }
    
    @Override
    public String getName() {
        return "skull";
    }
}
