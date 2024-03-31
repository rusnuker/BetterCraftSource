// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.commands.impl;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import me.nzxtercode.bettercraft.client.BetterCraft;
import net.minecraft.client.Minecraft;
import me.nzxtercode.bettercraft.client.commands.Command;

public class ClearChatCommand extends Command
{
    private String tocopy;
    
    public ClearChatCommand() {
        super("cc", new String[] { "clearchat" });
    }
    
    @Override
    public void run(final String alias, final String[] args) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
        final GuiNewChat chatGUI = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        BetterCraft.getInstance();
        chatGUI.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "Chat cleared"));
    }
    
    @Override
    public List<String> autocomplete(final int arg, final String[] args) {
        return new ArrayList<String>();
    }
}
