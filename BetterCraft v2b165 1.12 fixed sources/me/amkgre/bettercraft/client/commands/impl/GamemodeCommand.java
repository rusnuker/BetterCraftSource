// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import net.minecraft.world.GameType;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.commands.CommandManager;
import me.amkgre.bettercraft.client.commands.Command;

public class GamemodeCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.clientMSG("§m§8----------§r §5GM §m§8----------", true);
            Command.clientMSG("§d" + CommandManager.syntax + "gm 0", true);
            Command.clientMSG("§d" + CommandManager.syntax + "gm 1", true);
            Command.clientMSG("§d" + CommandManager.syntax + "gm 2", true);
            Command.clientMSG("§d" + CommandManager.syntax + "gm 3", true);
            Command.clientMSG("§m§8----------§r §5GM §m§8----------", true);
        }
        else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("0")) {
                Minecraft.getMinecraft().playerController.setGameType(GameType.SURVIVAL);
                Command.clientMSG("§7Your Gamemode switch to §d" + Minecraft.getMinecraft().playerController.getCurrentGameType().name(), true);
            }
            else if (args[0].equalsIgnoreCase("1")) {
                Minecraft.getMinecraft().playerController.setGameType(GameType.CREATIVE);
                Command.clientMSG("§7Your Gamemode switch to §d" + Minecraft.getMinecraft().playerController.getCurrentGameType().name(), true);
            }
            else if (args[0].equalsIgnoreCase("2")) {
                Minecraft.getMinecraft().playerController.setGameType(GameType.ADVENTURE);
                Command.clientMSG("§7Your Gamemode switch to §d" + Minecraft.getMinecraft().playerController.getCurrentGameType().name(), true);
            }
            else if (args[0].equalsIgnoreCase("3")) {
                Minecraft.getMinecraft().playerController.setGameType(GameType.SPECTATOR);
                Command.clientMSG("§7Your Gamemode switch to §d" + Minecraft.getMinecraft().playerController.getCurrentGameType().name(), true);
            }
        }
        else {
            Command.clientMSG("§cType gm", true);
        }
    }
    
    @Override
    public String getName() {
        return "gm";
    }
}
