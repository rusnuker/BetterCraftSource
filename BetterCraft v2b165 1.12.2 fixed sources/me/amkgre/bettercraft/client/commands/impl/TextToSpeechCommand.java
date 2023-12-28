// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.commands.CommandManager;
import me.amkgre.bettercraft.client.commands.Command;

public class TextToSpeechCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.clientMSG("", false);
            Command.clientMSG("§d" + CommandManager.syntax + "speech <text>", true);
        }
        else if (args.length >= 1) {
            Minecraft.getMinecraft().narrator.clear();
            final StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < args.length; ++i) {
                stringBuilder.append(" ").append(args[i]);
            }
            Minecraft.getMinecraft().narrator.say(stringBuilder.toString());
        }
        else {
            Command.clientMSG("§cType speech", true);
        }
    }
    
    @Override
    public String getName() {
        return "speech";
    }
}
