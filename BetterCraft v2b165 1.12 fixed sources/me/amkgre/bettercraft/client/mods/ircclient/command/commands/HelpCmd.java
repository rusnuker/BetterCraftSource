// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.ircclient.command.commands;

import java.util.Iterator;
import me.amkgre.bettercraft.client.Client;
import me.amkgre.bettercraft.client.mods.ircclient.command.IrcCmd;

public class HelpCmd extends IrcCmd
{
    public HelpCmd() {
        super("help", new String[] { "" });
    }
    
    @Override
    public void performCommand(final String alias, final String[] args) {
        for (final IrcCmd ircCmd : Client.getInstance().getIrcCmdManager().getCommands()) {
            this.sendMessage("/" + ircCmd.getName());
        }
    }
}
