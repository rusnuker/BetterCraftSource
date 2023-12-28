// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import me.amkgre.bettercraft.client.modules.Module;
import me.amkgre.bettercraft.client.modules.impl.WerbungModule;
import me.amkgre.bettercraft.client.Client;
import me.amkgre.bettercraft.client.commands.CommandManager;
import me.amkgre.bettercraft.client.commands.Command;

public class WerbungCommand extends Command
{
    public static String msg;
    
    static {
        WerbungCommand.msg = "BetterCraft is a Nice Client :)";
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.clientMSG("§m§8----------§r §5MSG §m§8----------", true);
            Command.clientMSG("§d" + CommandManager.syntax + "msg text", true);
            Command.clientMSG("§d" + CommandManager.syntax + "msg toggle", true);
            Command.clientMSG("§m§8----------§r §5MSG §m§8----------", true);
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
            Client.getInstance().moduleManager.getModule(WerbungModule.class).toggle();
            Command.clientMSG(Client.getInstance().moduleManager.getModule(WerbungModule.class).isEnabled() ? "Werbung §don" : "Werbung §doff", true);
        }
        if (args.length > 0 && !args[0].equalsIgnoreCase("toggle")) {
            String message = " ";
            message = args[0];
            int i = 0;
            while (++i < args.length) {
                message = String.valueOf(String.valueOf(String.valueOf(message))) + " " + args[i];
            }
            Command.clientMSG("Text set to §f'" + message + "'", true);
            WerbungCommand.msg = message;
        }
    }
    
    @Override
    public String getName() {
        return "msg";
    }
}
