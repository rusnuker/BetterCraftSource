// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.commands;

import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Collection;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import me.nzxtercode.bettercraft.client.BetterCraft;
import net.minecraft.client.Minecraft;
import me.nzxtercode.bettercraft.client.commands.impl.GiveCommand;
import me.nzxtercode.bettercraft.client.commands.impl.GetGeoServerDataCommand;
import me.nzxtercode.bettercraft.client.commands.impl.ClearChatCommand;
import me.nzxtercode.bettercraft.client.commands.impl.GameModeCommand;
import me.nzxtercode.bettercraft.client.commands.impl.CopyIPCommand;
import me.nzxtercode.bettercraft.client.commands.impl.IRCCommand;
import me.nzxtercode.bettercraft.client.commands.impl.HelpCommand;
import me.nzxtercode.bettercraft.client.Config;
import java.util.ArrayList;
import java.util.List;

public class CommandManager
{
    private static final CommandManager INSTANCE;
    public List<Command> commands;
    public List<String> commandsNames;
    public String prefix;
    
    static {
        INSTANCE = new CommandManager();
    }
    
    public CommandManager() {
        this.commands = new ArrayList<Command>();
        this.commandsNames = new ArrayList<String>();
        this.prefix = Config.getInstance().getConfig("Command").get("prefix").getAsString();
    }
    
    public static CommandManager getInstance() {
        return CommandManager.INSTANCE;
    }
    
    public void addCommands() {
        this.addCommand(new HelpCommand());
        this.addCommand(new IRCCommand());
        this.addCommand(new CopyIPCommand());
        this.addCommand(new GameModeCommand());
        this.addCommand(new ClearChatCommand());
        this.addCommand(new GetGeoServerDataCommand());
        this.addCommand(new GiveCommand());
    }
    
    private void addCommand(final Command cmd) {
        this.commands.add(cmd);
    }
    
    public List<Command> getCommands() {
        return this.commands;
    }
    
    public boolean executeCommand(final String string) {
        final String raw = string.substring(1);
        final String[] split = raw.split(" ");
        if (split.length == 0) {
            return false;
        }
        final String cmdName = split[0];
        final Command command = this.commands.stream().filter(cmd -> cmd.match(name)).findFirst().orElse(null);
        try {
            if (command == null) {
                final GuiNewChat chatGUI = Minecraft.getMinecraft().ingameGUI.getChatGUI();
                BetterCraft.getInstance();
                chatGUI.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "§7Command §f'" + cmdName + "'§7 doesn't exist"));
                return false;
            }
            final String[] args = new String[split.length - 1];
            System.arraycopy(split, 1, args, 0, split.length - 1);
            command.run(split[0], args);
            return true;
        }
        catch (final CommandException e) {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§c" + e.getMessage()));
            return true;
        }
    }
    
    public Collection<String> autoComplete(final String currCmd) {
        final String raw = currCmd.substring(1);
        final String[] split = raw.split(" ");
        final List<String> ret = new ArrayList<String>();
        final Command currentCommand = (split.length >= 1) ? this.commands.stream().filter(cmd -> cmd.match(array[0])).findFirst().orElse(null) : null;
        if (split.length >= 2 || (currentCommand != null && currCmd.endsWith(" "))) {
            if (currentCommand == null) {
                return ret;
            }
            final String[] args = new String[split.length - 1];
            System.arraycopy(split, 1, args, 0, split.length - 1);
            final List<String> autocomplete = currentCommand.autocomplete(args.length + (currCmd.endsWith(" ") ? 1 : 0), args);
            return (autocomplete == null) ? new ArrayList<String>() : autocomplete;
        }
        else {
            if (split.length == 1) {
                for (final Command command : this.commands) {
                    ret.addAll(command.getNameAndAliases());
                }
                return ret.stream().map(str -> "." + str).filter(str -> str.toLowerCase().startsWith(s.toLowerCase())).collect((Collector<? super Object, ?, Collection<String>>)Collectors.toList());
            }
            return ret;
        }
    }
}
