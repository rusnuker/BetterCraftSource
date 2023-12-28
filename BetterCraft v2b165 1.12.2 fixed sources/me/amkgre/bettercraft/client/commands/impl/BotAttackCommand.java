// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import me.amkgre.bettercraft.client.mods.botattack.BotCrackedConnector;
import me.amkgre.bettercraft.client.commands.CommandManager;
import me.amkgre.bettercraft.client.commands.Command;

public class BotAttackCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.clientMSG("§m§8----------§r §5Bot §m§8----------", true);
            Command.clientMSG("§d" + CommandManager.syntax + "bot start", true);
            Command.clientMSG("§d" + CommandManager.syntax + "bot stop", true);
            Command.clientMSG("§m§8----------§r §5Bot §m§8----------", true);
        }
        else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("start")) {
                Command.clientMSG("Botattack started...", true);
                BotCrackedConnector.start();
            }
            else if (args[0].equalsIgnoreCase("stop")) {
                Command.clientMSG("Botattack stopped", true);
                BotCrackedConnector.executor.shutdownNow();
            }
        }
        else {
            Command.clientMSG("§cType bot", true);
        }
    }
    
    @Override
    public String getName() {
        return "bot";
    }
}
