// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import java.util.Iterator;
import me.amkgre.bettercraft.client.mods.clientchat.InterClienChatConnection;
import me.amkgre.bettercraft.client.commands.CommandManager;
import me.amkgre.bettercraft.client.commands.Command;

public class ClientChatCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.clientMSG("§m§8----------§r §5ClientChat §m§8----------", true);
            Command.clientMSG("§d" + CommandManager.syntax + "clientchat connect", true);
            Command.clientMSG("§d" + CommandManager.syntax + "clientchat disconnect", true);
            Command.clientMSG("§d" + CommandManager.syntax + "clientchat reconnect", true);
            Command.clientMSG("§d" + CommandManager.syntax + "clientchat users", true);
            Command.clientMSG("§m§8----------§r §5ClientChat §m§8----------", true);
        }
        else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("connect")) {
                Command.clientMSG("ClientChat has §dstarted", true);
                InterClienChatConnection.start();
            }
            else if (args[0].equalsIgnoreCase("disconnect")) {
                Command.clientMSG("ClientChat has §dstopped", true);
                InterClienChatConnection.stop();
            }
            else if (args[0].equalsIgnoreCase("reconnect")) {
                Command.clientMSG("ClientChat has §dreconnected", true);
                InterClienChatConnection.stop();
                InterClienChatConnection.start();
            }
            else if (args[0].equalsIgnoreCase("users")) {
                Command.clientMSG("§m§8----------§r §5ClientChat §m§8----------", true);
                Command.clientMSG("§5Total users: §d" + InterClienChatConnection.onlinePlayers.size(), true);
                Command.clientMSG("", true);
                for (final String s : InterClienChatConnection.onlinePlayers) {
                    Command.clientMSG(s, true);
                }
                Command.clientMSG("§m§8----------§r §5ClientChat §m§8----------", true);
            }
            else {
                Command.clientMSG("§cType clientchat", true);
            }
        }
    }
    
    @Override
    public String getName() {
        return "clientchat";
    }
}
