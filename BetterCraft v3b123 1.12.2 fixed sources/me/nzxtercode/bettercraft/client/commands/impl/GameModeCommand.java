// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.commands.impl;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiNewChat;
import me.nzxtercode.bettercraft.client.commands.CommandManager;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import me.nzxtercode.bettercraft.client.BetterCraft;
import net.minecraft.world.WorldSettings;
import net.minecraft.client.Minecraft;
import me.nzxtercode.bettercraft.client.commands.Command;

public class GameModeCommand extends Command
{
    private String tocopy;
    
    public GameModeCommand() {
        super("gm", new String[] { "gamemode" });
    }
    
    @Override
    public void run(final String alias, final String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("survival")) {
                Minecraft.getMinecraft().playerController.setGameType(WorldSettings.GameType.SURVIVAL);
                final GuiNewChat chatGUI = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                BetterCraft.getInstance();
                chatGUI.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "Your Gamemode switch to §f" + Minecraft.getMinecraft().playerController.getCurrentGameType().name()));
            }
            else if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("creative")) {
                Minecraft.getMinecraft().playerController.setGameType(WorldSettings.GameType.CREATIVE);
                final GuiNewChat chatGUI2 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                BetterCraft.getInstance();
                chatGUI2.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "Your Gamemode switch to §f" + Minecraft.getMinecraft().playerController.getCurrentGameType().name()));
            }
            else if (args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("adventure")) {
                Minecraft.getMinecraft().playerController.setGameType(WorldSettings.GameType.ADVENTURE);
                final GuiNewChat chatGUI3 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                BetterCraft.getInstance();
                chatGUI3.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "Your Gamemode switch to §f" + Minecraft.getMinecraft().playerController.getCurrentGameType().name()));
            }
            else if (args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("spectator")) {
                Minecraft.getMinecraft().playerController.setGameType(WorldSettings.GameType.SPECTATOR);
                final GuiNewChat chatGUI4 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                BetterCraft.getInstance();
                chatGUI4.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "Your Gamemode switch to §f" + Minecraft.getMinecraft().playerController.getCurrentGameType().name()));
            }
        }
        else {
            final GuiNewChat chatGUI5 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
            BetterCraft.getInstance();
            chatGUI5.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + CommandManager.getInstance().prefix + alias + " 0"));
            final GuiNewChat chatGUI6 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
            BetterCraft.getInstance();
            chatGUI6.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + CommandManager.getInstance().prefix + alias + " 1"));
            final GuiNewChat chatGUI7 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
            BetterCraft.getInstance();
            chatGUI7.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + CommandManager.getInstance().prefix + alias + " 2"));
            final GuiNewChat chatGUI8 = Minecraft.getMinecraft().ingameGUI.getChatGUI();
            BetterCraft.getInstance();
            chatGUI8.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + CommandManager.getInstance().prefix + alias + " 3"));
        }
    }
    
    @Override
    public List<String> autocomplete(final int arg, final String[] args) {
        return new ArrayList<String>();
    }
}
