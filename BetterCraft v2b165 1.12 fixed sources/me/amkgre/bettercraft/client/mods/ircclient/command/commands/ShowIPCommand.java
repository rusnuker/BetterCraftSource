// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.ircclient.command.commands;

import me.amkgre.bettercraft.client.Client;
import me.amkgre.bettercraft.client.mods.ircclient.command.IrcCmd;

public class ShowIPCommand extends IrcCmd
{
    public ShowIPCommand() {
        super("showip", new String[] { "" });
    }
    
    @Override
    public void performCommand(final String alias, final String[] args) {
        Client.getInstance().ircbot.showIP = !Client.getInstance().ircbot.showIP;
        this.sendMessage("Show IP's: " + (Client.getInstance().ircbot.showIP ? "enabled" : "disabled"));
    }
}
