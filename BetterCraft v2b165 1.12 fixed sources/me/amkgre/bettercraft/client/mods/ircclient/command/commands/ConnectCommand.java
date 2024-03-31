// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.ircclient.command.commands;

import me.amkgre.bettercraft.client.mods.ircclient.IrcLine;
import me.amkgre.bettercraft.client.Client;
import me.amkgre.bettercraft.client.mods.ircclient.command.IrcCmd;

public class ConnectCommand extends IrcCmd
{
    public ConnectCommand() {
        super("connect", new String[] { "" });
    }
    
    @Override
    public void performCommand(final String alias, final String[] args) {
        if (!Client.getInstance().ircbot.isConnected()) {
            if (args.length == 1) {
                IrcLine.lines.clear();
                this.sendMessage("You are currently connected to " + args[0] + ".");
                Client.getInstance().ircbot.connectBot(Client.getInstance().ircbot.freenode, "#" + args[0]);
            }
            else {
                this.sendMessage("Usage: /connect <channel>");
            }
        }
        else {
            this.sendMessage("You are already connected to a Channel!");
        }
    }
}
