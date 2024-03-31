// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.ircclient.command.commands;

import me.amkgre.bettercraft.client.Client;
import me.amkgre.bettercraft.client.mods.ircclient.command.IrcCmd;

public class JoinLeaveCommand extends IrcCmd
{
    public JoinLeaveCommand() {
        super("joinleave", new String[] { "" });
    }
    
    @Override
    public void performCommand(final String alias, final String[] args) {
        Client.getInstance().ircbot.joinLeaveMessages = !Client.getInstance().ircbot.joinLeaveMessages;
        this.sendMessage("Join/Leave Message's: " + (Client.getInstance().ircbot.joinLeaveMessages ? "enabled" : "disabled"));
    }
}
