// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.commands.Command;

public class ClearChatCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages(true);
            Command.clientMSG("Chat was §dcleared!", true);
        }
    }
    
    @Override
    public String getName() {
        return "clearchat";
    }
}
