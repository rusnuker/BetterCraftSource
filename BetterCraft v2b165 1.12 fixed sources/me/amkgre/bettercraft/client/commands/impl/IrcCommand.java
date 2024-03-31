// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import org.jibble.pircbot.User;
import me.amkgre.bettercraft.client.mods.ircclient.IrcLine;
import me.amkgre.bettercraft.client.Client;
import me.amkgre.bettercraft.client.commands.CommandManager;
import me.amkgre.bettercraft.client.commands.Command;

public class IrcCommand extends Command
{
    private String ip;
    
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.ircMSG("§m§8----------§r §5IRC §m§8----------", true);
            Command.ircMSG("§d" + CommandManager.syntax + "irc message <text>", true);
            Command.ircMSG("§d" + CommandManager.syntax + "irc connect <channel>", true);
            Command.ircMSG("§d" + CommandManager.syntax + "irc disconnect", true);
            Command.ircMSG("§d" + CommandManager.syntax + "irc users", true);
            Command.ircMSG("§d" + CommandManager.syntax + "irc showip", true);
            Command.ircMSG("§d" + CommandManager.syntax + "irc joinleave", true);
            Command.ircMSG("", true);
            Command.ircMSG("§d" + CommandManager.syntax + "irc eazy", true);
            Command.ircMSG("§d" + CommandManager.syntax + "irc creeper", true);
            Command.ircMSG("§d" + CommandManager.syntax + "irc protocol", true);
            Command.ircMSG("§d" + CommandManager.syntax + "irc luna", true);
            Command.ircMSG("§m§8----------§r §5IRC §m§8----------", true);
        }
        else if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("connect")) {
                if (args.length == 2) {
                    if (!Client.getInstance().ircbot.isConnected()) {
                        IrcLine.lines.clear();
                        Command.ircMSG("Connected to §d" + args[1] + " §7please wait some seconds.", true);
                        Client.getInstance().ircbot.connectBot(Client.getInstance().ircbot.freenode, "#" + args[1]);
                    }
                    else {
                        Command.ircMSG("You are already connected to a other §dIRC!", true);
                    }
                }
                else {
                    Command.ircMSG("§d" + CommandManager.syntax + "irc connect <channel>", true);
                }
            }
            else if (args[0].equalsIgnoreCase("disconnect")) {
                if (Client.getInstance().ircbot.isConnected()) {
                    Client.getInstance().ircbot.disconnectBot();
                    Command.ircMSG("Disconnected from §dchannel", true);
                    IrcLine.lines.clear();
                }
                else {
                    Command.ircMSG("You are not §dconnected!", true);
                }
            }
            else if (args[0].equalsIgnoreCase("users")) {
                Command.ircMSG("§m§8----------§r §5IRC §m§8----------", true);
                Command.ircMSG("§5Total users: §d" + Client.getInstance().ircbot.getUsers(Client.getInstance().ircbot.currentChannel).length, true);
                Command.ircMSG("", true);
                User[] users;
                for (int length = (users = Client.getInstance().ircbot.getUsers(Client.getInstance().ircbot.currentChannel)).length, i = 0; i < length; ++i) {
                    final User user = users[i];
                    Command.ircMSG((user.isOp() ? "§5OP: §d" : "§d") + user.getNick(), true);
                }
                Command.ircMSG("§m§8----------§r §5IRC §m§8----------", true);
            }
            else if (args[0].equalsIgnoreCase("showip")) {
                Client.getInstance().ircbot.showIP = !Client.getInstance().ircbot.showIP;
                Command.ircMSG("Show IP's: " + (Client.getInstance().ircbot.showIP ? "§aenabled" : "§cdisabled"), true);
            }
            else if (args[0].equalsIgnoreCase("joinleave")) {
                Client.getInstance().ircbot.joinLeaveMessages = !Client.getInstance().ircbot.joinLeaveMessages;
                Command.ircMSG("Join/Leave Message's: " + (Client.getInstance().ircbot.joinLeaveMessages ? "enabled" : "disabled"), true);
            }
            else if (args[0].equalsIgnoreCase("eazy")) {
                if (!Client.getInstance().ircbot.isConnected()) {
                    IrcLine.lines.clear();
                    Command.ircMSG("Connected to §dEaZyClient IRC §7please wait some seconds.", true);
                    Client.getInstance().ircbot.connectBot(Client.getInstance().ircbot.freenode, "#eazyrofl");
                }
                else {
                    IrcLine.lines.clear();
                    Client.getInstance().ircbot.disconnectBot();
                    Command.ircMSG("Connected to §dEaZyClient IRC §7please wait some seconds.", true);
                    Client.getInstance().ircbot.connectBot(Client.getInstance().ircbot.freenode, "#eazyrofl");
                }
            }
            else if (args[0].equalsIgnoreCase("creeper")) {
                if (!Client.getInstance().ircbot.isConnected()) {
                    IrcLine.lines.clear();
                    Command.ircMSG("Connected to §dCreeperClient IRC §7please wait some seconds.", true);
                    Client.getInstance().ircbot.connectBot(Client.getInstance().ircbot.freenode, "#CreeperClientChatRoom");
                }
                else {
                    IrcLine.lines.clear();
                    Client.getInstance().ircbot.disconnectBot();
                    Command.ircMSG("Connected to §dCreeperClient IRC §7please wait some seconds.", true);
                    Client.getInstance().ircbot.connectBot(Client.getInstance().ircbot.freenode, "#CreeperClientChatRoom");
                }
            }
            else if (args[0].equalsIgnoreCase("protocol")) {
                if (!Client.getInstance().ircbot.isConnected()) {
                    IrcLine.lines.clear();
                    Command.ircMSG("Connected to §dProtocolClient IRC §7please wait some seconds.", true);
                    Client.getInstance().ircbot.connectBot(Client.getInstance().ircbot.freenode, "#Innocent");
                }
                else {
                    IrcLine.lines.clear();
                    Client.getInstance().ircbot.disconnectBot();
                    Command.ircMSG("Connected to §dProtocolClient IRC §7please wait some seconds.", true);
                    Client.getInstance().ircbot.connectBot(Client.getInstance().ircbot.freenode, "#Innocent");
                }
            }
            else if (args[0].equalsIgnoreCase("luna")) {
                if (!Client.getInstance().ircbot.isConnected()) {
                    IrcLine.lines.clear();
                    Command.ircMSG("Connected to §dLunaClient IRC §7please wait some seconds.", true);
                    Client.getInstance().ircbot.connectBot(Client.getInstance().ircbot.mibbit, "#LunaClient");
                }
                else {
                    IrcLine.lines.clear();
                    Client.getInstance().ircbot.disconnectBot();
                    Command.ircMSG("Connected to §dLunaClient IRC §7please wait some seconds.", true);
                    Client.getInstance().ircbot.connectBot(Client.getInstance().ircbot.mibbit, "#LunaClient");
                }
            }
            else if (args[0].equalsIgnoreCase("message")) {
                if (args.length >= 2) {
                    String msg = "";
                    for (int data = 1; data != args.length; ++data) {
                        msg = String.valueOf(msg) + args[data] + " ";
                    }
                    if (!msg.trim().isEmpty() && msg.startsWith("/")) {
                        Client.getInstance().ircmanager.onCommand(msg);
                        return;
                    }
                    Client.getInstance().ircbot.sendClientMessage(msg);
                }
            }
            else {
                Command.clientMSG("§cType irc", true);
            }
        }
    }
    
    @Override
    public String getName() {
        return "irc";
    }
}
