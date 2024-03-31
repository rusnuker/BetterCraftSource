// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.commands.impl;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import me.nzxtercode.bettercraft.client.commands.CommandManager;
import me.nzxtercode.bettercraft.client.BetterCraft;
import net.minecraft.client.Minecraft;
import me.nzxtercode.bettercraft.client.commands.Command;

public class HelpCommand extends Command
{
    public HelpCommand() {
        super("hilfe", new String[] { "?", "help" });
    }
    
    @Override
    public void run(final String alias, final String[] args) {
        final GuiNewChat chatGUI = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        BetterCraft.getInstance();
        chatGUI.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "Commands: §f" + CommandManager.getInstance().getCommands().size()));
        final GuiNewChat chatGUI2 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        BetterCraft.getInstance();
        chatGUI2.printChatMessage(new ChatComponentText(new StringBuilder(String.valueOf(BetterCraft.clientPrefix)).toString()));
        final GuiNewChat chatGUI3 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        BetterCraft.getInstance();
        chatGUI3.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "§7" + CommandManager.getInstance().prefix + "help"));
        final GuiNewChat chatGUI4 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        BetterCraft.getInstance();
        chatGUI4.printChatMessage(new ChatComponentText(new StringBuilder(String.valueOf(BetterCraft.clientPrefix)).toString()));
        final GuiNewChat chatGUI5 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        BetterCraft.getInstance();
        chatGUI5.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "§7" + CommandManager.getInstance().prefix + "irc"));
        final GuiNewChat chatGUI6 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        BetterCraft.getInstance();
        chatGUI6.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "§7" + CommandManager.getInstance().prefix + "copyip"));
        final GuiNewChat chatGUI7 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        BetterCraft.getInstance();
        chatGUI7.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "§7" + CommandManager.getInstance().prefix + "gm"));
        final GuiNewChat chatGUI8 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        BetterCraft.getInstance();
        chatGUI8.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "§7" + CommandManager.getInstance().prefix + "cc"));
        final GuiNewChat chatGUI9 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        BetterCraft.getInstance();
        chatGUI9.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "§7" + CommandManager.getInstance().prefix + "getgeo"));
        final GuiNewChat chatGUI10 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        BetterCraft.getInstance();
        chatGUI10.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "§7" + CommandManager.getInstance().prefix + "give"));
    }
    
    @Override
    public List<String> autocomplete(final int arg, final String[] args) {
        return new ArrayList<String>();
    }
}
