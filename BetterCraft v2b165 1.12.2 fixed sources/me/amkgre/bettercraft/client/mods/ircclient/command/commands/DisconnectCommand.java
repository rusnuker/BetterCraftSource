// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.ircclient.command.commands;

import me.amkgre.bettercraft.client.mods.ircclient.IrcLine;
import me.amkgre.bettercraft.client.Client;
import me.amkgre.bettercraft.client.mods.ircclient.command.IrcCmd;

public class DisconnectCommand extends IrcCmd
{
    public DisconnectCommand() {
        super("disconnect", new String[] { "" });
    }
    
    @Override
    public void performCommand(final String alias, final String[] args) {
        if (Client.getInstance().ircbot.isConnected()) {
            Client.getInstance().ircbot.disconnectBot();
            this.sendMessage("Disconnected.");
            IrcLine.lines.clear();
            this.sendMessage("You are currently not connected to a channel!");
        }
        else {
            this.sendMessage("You are not connected!");
        }
    }
}
