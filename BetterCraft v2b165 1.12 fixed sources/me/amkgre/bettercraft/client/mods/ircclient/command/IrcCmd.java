// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.ircclient.command;

import me.amkgre.bettercraft.client.Client;
import me.amkgre.bettercraft.client.mods.ircclient.IrcLine;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class IrcCmd
{
    private String name;
    private String description;
    private String[] aliases;
    
    public IrcCmd(final String name, final String... aliases) {
        this.name = name;
        this.aliases = aliases;
    }
    
    public String[] getAliases() {
        return this.aliases;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void performCommand(final String alias, final String[] args) {
    }
    
    public boolean match(final String name) {
        String[] aliases;
        for (int length = (aliases = this.getAliases()).length, i = 0; i < length; ++i) {
            final String alias = aliases[i];
            if (alias.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return this.getName().equalsIgnoreCase(name);
    }
    
    List<String> getNameAndAliases() {
        final List<String> l = new ArrayList<String>();
        l.add(this.getName());
        l.addAll(Arrays.asList(this.aliases));
        return l;
    }
    
    public void sendMessage(final String msg) {
        new IrcLine(1, String.valueOf(Client.getInstance().ircbot.prefix) + " " + msg);
        Client.getInstance().ircbot.sendChatWithoutPrefix(String.valueOf(Client.getInstance().ircbot.prefix) + " " + msg);
    }
}
