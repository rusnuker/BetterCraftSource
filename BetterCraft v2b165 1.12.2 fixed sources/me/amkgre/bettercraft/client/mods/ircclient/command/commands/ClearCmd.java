// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.ircclient.command.commands;

import me.amkgre.bettercraft.client.mods.ircclient.IrcLine;
import me.amkgre.bettercraft.client.mods.ircclient.command.IrcCmd;

public class ClearCmd extends IrcCmd
{
    public ClearCmd() {
        super("clear", new String[] { "" });
    }
    
    @Override
    public void performCommand(final String alias, final String[] args) {
        IrcLine.lines.clear();
    }
}
