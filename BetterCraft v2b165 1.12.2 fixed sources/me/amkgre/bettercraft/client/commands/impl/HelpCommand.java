// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import me.amkgre.bettercraft.client.commands.CommandManager;
import me.amkgre.bettercraft.client.commands.Command;

public class HelpCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.clientMSG(String.valueOf("§7Type §d" + CommandManager.syntax + "help 1-2"), true);
        }
        else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("1")) {
                Command.clientMSG("§m§8----------§r §5Help 1/3 §m§8----------", true);
                Command.clientMSG("§d" + CommandManager.syntax + "clientchat", true);
                Command.clientMSG("§d" + CommandManager.syntax + "labymod", true);
                Command.clientMSG("§d" + CommandManager.syntax + "irc", true);
                Command.clientMSG("", true);
                Command.clientMSG("§d" + CommandManager.syntax + "copyip", true);
                Command.clientMSG("§d" + CommandManager.syntax + "copydomain", true);
                Command.clientMSG("§d" + CommandManager.syntax + "copyremote", true);
                Command.clientMSG("§d" + CommandManager.syntax + "copyversion", true);
                Command.clientMSG("", true);
                Command.clientMSG("§d" + CommandManager.syntax + "getproxy", true);
                Command.clientMSG("§d" + CommandManager.syntax + "getgeo", true);
                Command.clientMSG("§d" + CommandManager.syntax + "getsrv", true);
                Command.clientMSG("§d" + CommandManager.syntax + "getapi", true);
                Command.clientMSG("", true);
                Command.clientMSG("§d" + CommandManager.syntax + "info", true);
                Command.clientMSG("§d" + CommandManager.syntax + "speech", true);
                Command.clientMSG("§d" + CommandManager.syntax + "exec", true);
                Command.clientMSG("§d" + CommandManager.syntax + "copymyip", true);
                Command.clientMSG("§d" + CommandManager.syntax + "cracked", true);
                Command.clientMSG("§d" + CommandManager.syntax + "hastebin", true);
                Command.clientMSG("§d" + CommandManager.syntax + "connect", true);
                Command.clientMSG("§d" + CommandManager.syntax + "disconnect", true);
                Command.clientMSG("§d" + CommandManager.syntax + "shutdown", true);
                Command.clientMSG("§d" + CommandManager.syntax + "converter", true);
                Command.clientMSG("§d" + CommandManager.syntax + "clearchat", true);
                Command.clientMSG("§d" + CommandManager.syntax + "gm", true);
                Command.clientMSG("§m§8----------§r §5Help 1/3 §m§8----------", true);
            }
            else if (args[0].equalsIgnoreCase("2")) {
                Command.clientMSG("§m§8----------§r §5Help 2/3 §m§8----------", true);
                Command.clientMSG("§d" + CommandManager.syntax + "bot", true);
                Command.clientMSG("§d" + CommandManager.syntax + "msg", true);
                Command.clientMSG("§d" + CommandManager.syntax + "hologram", true);
                Command.clientMSG("§d" + CommandManager.syntax + "cmdblock", true);
                Command.clientMSG("§d" + CommandManager.syntax + "singlecrasher", true);
                Command.clientMSG("§d" + CommandManager.syntax + "multicrasher", true);
                Command.clientMSG("§m§8----------§r §5Help 2/3 §m§8----------", true);
            }
            else if (args[0].equalsIgnoreCase("3")) {
                Command.clientMSG("§m§8----------§r §5Help 3/3 §m§8----------", true);
                Command.clientMSG("§d" + CommandManager.syntax + "drop", true);
                Command.clientMSG("§d" + CommandManager.syntax + "give", true);
                Command.clientMSG("§d" + CommandManager.syntax + "skull", true);
                Command.clientMSG("§d" + CommandManager.syntax + "tntplace", true);
                Command.clientMSG("§d" + CommandManager.syntax + "authme", true);
                Command.clientMSG("§d" + CommandManager.syntax + "pex", true);
                Command.clientMSG("§d" + CommandManager.syntax + "adminkicker", true);
                Command.clientMSG("§d" + CommandManager.syntax + "spam", true);
                Command.clientMSG("§m§8----------§r §5Help 3/3 §m§8----------", true);
            }
            else {
                Command.clientMSG("§cUnknown Page! Pages: 1-2", true);
            }
        }
    }
    
    @Override
    public String getName() {
        return "help";
    }
}
