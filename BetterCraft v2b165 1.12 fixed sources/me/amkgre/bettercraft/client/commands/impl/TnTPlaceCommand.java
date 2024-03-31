// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.commands.Command;

public class TnTPlaceCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            if (Minecraft.getMinecraft().player.capabilities.isCreativeMode) {
                for (int i2 = 0; i2 < 150; ++i2) {
                    final Item item = Item.getItemById(46);
                    final ItemStack itemStack = new ItemStack(item, 64);
                    itemStack.setStackDisplayName("RageTNT");
                    itemStack.setItemDamage(999);
                    Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCreativeInventoryAction(36, itemStack));
                    Minecraft.getMinecraft().player.dropItem(true);
                }
            }
            else {
                Command.clientMSG("This work only in Creative", true);
            }
        }
    }
    
    @Override
    public String getName() {
        return "tntplace";
    }
}
