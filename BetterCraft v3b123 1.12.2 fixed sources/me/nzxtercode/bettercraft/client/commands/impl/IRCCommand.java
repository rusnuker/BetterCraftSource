// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.commands.impl;

import java.util.ArrayList;
import java.util.List;
import org.jibble.pircbot.User;
import net.minecraft.client.gui.GuiNewChat;
import me.nzxtercode.bettercraft.client.commands.CommandManager;
import java.util.Arrays;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import me.nzxtercode.bettercraft.client.BetterCraft;
import net.minecraft.client.Minecraft;
import me.nzxtercode.bettercraft.client.misc.irc.IRC;
import me.nzxtercode.bettercraft.client.commands.Command;

public class IRCCommand extends Command
{
    public IRCCommand() {
        super("irc", new String[] { "clientchat" });
    }
    
    @Override
    public void run(final String alias, final String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("connect")) {
                IRC.getInstance().init();
                final GuiNewChat chatGUI = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                BetterCraft.getInstance();
                final StringBuilder append = new StringBuilder(String.valueOf(BetterCraft.clientPrefix)).append("Connect to §f");
                BetterCraft.getInstance();
                chatGUI.printChatMessage(new ChatComponentText(append.append(BetterCraft.clientName).append("s Channel").toString()));
            }
            else if (args[0].equalsIgnoreCase("disconnect")) {
                IRC.getInstance().disconnectBot();
                final GuiNewChat chatGUI2 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                BetterCraft.getInstance();
                chatGUI2.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "Disconnect from §fIRC"));
            }
            else if (args[0].equalsIgnoreCase("users")) {
                final User[] userArray = IRC.getInstance().getUsers(IRC.getInstance().currentChannel);
                final GuiNewChat chatGUI3 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                BetterCraft.getInstance();
                chatGUI3.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "Total users: §f" + userArray.length));
                final GuiNewChat chatGUI4 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                BetterCraft.getInstance();
                chatGUI4.printChatMessage(new ChatComponentText(BetterCraft.clientPrefix));
                Arrays.asList(userArray).forEach(user -> {
                    Minecraft.getMinecraft().ingameGUI.getChatGUI();
                    new(net.minecraft.util.ChatComponentText.class)();
                    new(java.lang.StringBuilder.class)();
                    final Object[] array = new Object[2];
                    BetterCraft.getInstance();
                    final Object o;
                    array[o] = BetterCraft.clientPrefix;
                    array[1] = (user.isOp() ? "§c" : "§f");
                    final String s;
                    new StringBuilder(String.valueOf(String.format(s, array)));
                    final StringBuilder sb;
                    new ChatComponentText(sb.append(user.getNick()).toString());
                    final IChatComponent chatComponent;
                    final GuiNewChat guiNewChat;
                    guiNewChat.printChatMessage(chatComponent);
                });
            }
        }
        else {
            final GuiNewChat chatGUI5 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
            BetterCraft.getInstance();
            chatGUI5.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + CommandManager.getInstance().prefix + alias + " connect"));
            final GuiNewChat chatGUI6 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
            BetterCraft.getInstance();
            chatGUI6.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + CommandManager.getInstance().prefix + alias + " disconnect"));
            final GuiNewChat chatGUI7 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
            BetterCraft.getInstance();
            chatGUI7.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + CommandManager.getInstance().prefix + alias + " users"));
        }
    }
    
    @Override
    public List<String> autocomplete(final int arg, final String[] args) {
        return new ArrayList<String>();
    }
}
