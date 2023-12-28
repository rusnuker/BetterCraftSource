// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.ircclient.command;

import me.amkgre.bettercraft.client.mods.ircclient.command.commands.DisconnectCommand;
import me.amkgre.bettercraft.client.mods.ircclient.command.commands.ShowIPCommand;
import me.amkgre.bettercraft.client.mods.ircclient.command.commands.ClearCmd;
import me.amkgre.bettercraft.client.mods.ircclient.command.commands.ConnectCommand;
import me.amkgre.bettercraft.client.mods.ircclient.command.commands.UsersCommand;
import me.amkgre.bettercraft.client.mods.ircclient.command.commands.HelpCmd;
import java.util.ArrayList;
import java.util.List;

public class IrcCmdManager
{
    private List<IrcCmd> commands;
    public static String cmdPrefix;
    
    static {
        IrcCmdManager.cmdPrefix = "/";
    }
    
    public IrcCmdManager() {
        this.commands = new ArrayList<IrcCmd>();
    }
    
    public void addCommands() {
        this.addCommand(new HelpCmd());
        this.addCommand(new UsersCommand());
        this.addCommand(new ConnectCommand());
        this.addCommand(new ClearCmd());
        this.addCommand(new ShowIPCommand());
        this.addCommand(new DisconnectCommand());
    }
    
    public List<IrcCmd> getCommands() {
        return this.commands;
    }
    
    private void addCommand(final IrcCmd cmd) {
        this.commands.add(cmd);
    }
    
    public boolean executeCommand(final String string) {
        final String raw = string.substring(1);
        final String[] split = raw.split(" ");
        if (split.length == 0) {
            return false;
        }
        final String cmdName = split[0];
        final IrcCmd command = this.commands.stream().filter(cmd -> cmd.match(name)).findFirst().orElse(null);
        if (command == null) {
            return false;
        }
        final String[] args = new String[split.length - 1];
        System.arraycopy(split, 1, args, 0, split.length - 1);
        command.performCommand(split[0], args);
        return true;
    }
}
