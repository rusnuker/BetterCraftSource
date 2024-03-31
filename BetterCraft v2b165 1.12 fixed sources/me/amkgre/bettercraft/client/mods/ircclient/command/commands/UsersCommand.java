// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.ircclient.command.commands;

import org.jibble.pircbot.User;
import me.amkgre.bettercraft.client.Client;
import me.amkgre.bettercraft.client.mods.ircclient.command.IrcCmd;

public class UsersCommand extends IrcCmd
{
    public UsersCommand() {
        super("users", new String[] { "list" });
    }
    
    @Override
    public void performCommand(final String alias, final String[] args) {
        this.sendMessage("Total users: " + Client.getInstance().ircbot.getUsers(Client.getInstance().ircbot.currentChannel).length);
        User[] users;
        for (int length = (users = Client.getInstance().ircbot.getUsers(Client.getInstance().ircbot.currentChannel)).length, i = 0; i < length; ++i) {
            final User user = users[i];
            this.sendMessage(String.valueOf(user.isOp() ? "§5" : "§d") + user.getNick());
        }
    }
}
