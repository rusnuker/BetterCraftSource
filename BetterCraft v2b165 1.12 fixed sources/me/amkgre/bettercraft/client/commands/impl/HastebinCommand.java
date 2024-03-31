// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import java.io.IOException;
import me.amkgre.bettercraft.client.utils.HastebinUtils;
import me.amkgre.bettercraft.client.commands.CommandManager;
import me.amkgre.bettercraft.client.commands.Command;

public class HastebinCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.clientMSG("", false);
            Command.clientMSG("§d" + CommandManager.syntax + "hastebin text", true);
        }
        else if (args.length > 0) {
            try {
                String message = " ";
                message = args[0];
                int i = 0;
                while (++i < args.length) {
                    message = String.valueOf(String.valueOf(String.valueOf(message))) + " " + args[i];
                }
                HastebinUtils.uploadToHastebin(message);
                Command.clientMSG("Text was uploaded to §dhastebin §7and copy to §dclipboard", true);
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Command.clientMSG("§cType hastebin", true);
        }
    }
    
    @Override
    public String getName() {
        return "hastebin";
    }
}
