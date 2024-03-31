// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import java.io.IOException;
import me.amkgre.bettercraft.client.commands.CommandManager;
import me.amkgre.bettercraft.client.commands.Command;

public class ExecCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.clientMSG("", false);
            Command.clientMSG("§d" + CommandManager.syntax + "exec <program>", true);
        }
        else if (args.length >= 1) {
            try {
                final StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < args.length; ++i) {
                    stringBuilder.append(" ").append(args[i]);
                }
                Runtime.getRuntime().exec("cmd /c start " + stringBuilder.toString());
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Command.clientMSG("§cType exec", true);
        }
    }
    
    @Override
    public String getName() {
        return "exec";
    }
}
