// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import me.amkgre.bettercraft.client.commands.CommandManager;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.commands.Command;

public class GiveCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (Minecraft.getMinecraft().player.capabilities.isCreativeMode) {
            if (args.length == 0) {
                Command.clientMSG("§m§8----------§r §5Give §m§8----------", true);
                Command.clientMSG("§d" + CommandManager.syntax + "give cmdblock", true);
                Command.clientMSG("§d" + CommandManager.syntax + "give dragon_egg", true);
                Command.clientMSG("§d" + CommandManager.syntax + "give mob_spawner", true);
                Command.clientMSG("§d" + CommandManager.syntax + "give opsword", true);
                Command.clientMSG("§m§8----------§r §5Give §m§8----------", true);
            }
            else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("cmdblock") || args[0].equalsIgnoreCase("137")) {
                    final Item item = Item.getItemById(137);
                    final ItemStack itemStack = new ItemStack(item, 1);
                    Minecraft.getMinecraft().getConnection().sendPacket(new CPacketCreativeInventoryAction(5, itemStack));
                    Command.clientMSG("You get a CommandBlock", true);
                }
                else if (args[0].equalsIgnoreCase("dragon_egg") || args[0].equalsIgnoreCase("122")) {
                    final Item item = Item.getItemById(122);
                    final ItemStack itemStack = new ItemStack(item, 1);
                    Minecraft.getMinecraft().getConnection().sendPacket(new CPacketCreativeInventoryAction(5, itemStack));
                    Command.clientMSG("You get a DragonEgg", true);
                }
                else if (args[0].equalsIgnoreCase("mob_spawner") || args[0].equalsIgnoreCase("spawner") || args[0].equalsIgnoreCase("52")) {
                    final Item item = Item.getItemById(52);
                    final ItemStack itemStack = new ItemStack(item, 1);
                    Minecraft.getMinecraft().getConnection().sendPacket(new CPacketCreativeInventoryAction(5, itemStack));
                    Command.clientMSG("You get a Mobspawner", true);
                }
                else if (args[0].equalsIgnoreCase("opsword")) {
                    final Item item = Item.getItemById(276);
                    final ItemStack itemStack = new ItemStack(item, 1);
                    itemStack.setStackDisplayName("OP Sword");
                    itemStack.setItemDamage(9999999);
                    Minecraft.getMinecraft().getConnection().sendPacket(new CPacketCreativeInventoryAction(5, itemStack));
                    Command.clientMSG("You get a Op Sword", true);
                }
            }
            else {
                Command.clientMSG("§cType give", true);
            }
        }
        else {
            Command.clientMSG("§cYou need creative", true);
        }
    }
    
    @Override
    public String getName() {
        return "give";
    }
}
