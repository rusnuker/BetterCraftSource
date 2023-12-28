// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands;

import java.util.Iterator;
import java.util.Arrays;
import me.amkgre.bettercraft.client.commands.impl.ConverterCommand;
import me.amkgre.bettercraft.client.commands.impl.LabymodCommand;
import me.amkgre.bettercraft.client.commands.impl.SpamCommand;
import me.amkgre.bettercraft.client.commands.impl.AuthMeBypassCommand;
import me.amkgre.bettercraft.client.commands.impl.PermissionExCommand;
import me.amkgre.bettercraft.client.commands.impl.AdminKickerCommand;
import me.amkgre.bettercraft.client.commands.impl.TnTPlaceCommand;
import me.amkgre.bettercraft.client.commands.impl.GiveCommand;
import me.amkgre.bettercraft.client.commands.impl.SkullCommand;
import me.amkgre.bettercraft.client.commands.impl.ConnectCommand;
import me.amkgre.bettercraft.client.commands.impl.DisconnectCommand;
import me.amkgre.bettercraft.client.commands.impl.ShutdownCommand;
import me.amkgre.bettercraft.client.commands.impl.DropCommand;
import me.amkgre.bettercraft.client.commands.impl.HologramCommand;
import me.amkgre.bettercraft.client.commands.impl.CheckCmdBlockCommand;
import me.amkgre.bettercraft.client.commands.impl.CopyMyIPCommand;
import me.amkgre.bettercraft.client.commands.impl.HastebinCommand;
import me.amkgre.bettercraft.client.commands.impl.IrcCommand;
import me.amkgre.bettercraft.client.commands.impl.ClientChatCommand;
import me.amkgre.bettercraft.client.commands.impl.MultiCrasherCommand;
import me.amkgre.bettercraft.client.commands.impl.SingleCrasherCommand;
import me.amkgre.bettercraft.client.commands.impl.BotAttackCommand;
import me.amkgre.bettercraft.client.commands.impl.WerbungCommand;
import me.amkgre.bettercraft.client.commands.impl.GamemodeCommand;
import me.amkgre.bettercraft.client.commands.impl.ClearChatCommand;
import me.amkgre.bettercraft.client.commands.impl.InfoCommand;
import me.amkgre.bettercraft.client.commands.impl.CrackedLoginCommand;
import me.amkgre.bettercraft.client.commands.impl.ExecCommand;
import me.amkgre.bettercraft.client.commands.impl.TextToSpeechCommand;
import me.amkgre.bettercraft.client.commands.impl.GetProxyCommand;
import me.amkgre.bettercraft.client.commands.impl.GetApiCommand;
import me.amkgre.bettercraft.client.commands.impl.GetSrvCommand;
import me.amkgre.bettercraft.client.commands.impl.GetServerGeoCommand;
import me.amkgre.bettercraft.client.commands.impl.CopyServerVersionCommand;
import me.amkgre.bettercraft.client.commands.impl.CopyDomainCommand;
import me.amkgre.bettercraft.client.commands.impl.CopyIPCommand;
import me.amkgre.bettercraft.client.commands.impl.CopyRemoteAdressCommand;
import me.amkgre.bettercraft.client.commands.impl.HelpCommand;
import java.util.ArrayList;
import java.util.List;

public class CommandManager
{
    private static List<Command> commands;
    public static String syntax;
    
    static {
        CommandManager.commands = new ArrayList<Command>();
        CommandManager.syntax = "+";
    }
    
    public static void commands() {
        addCommand(new HelpCommand());
        addCommand(new CopyRemoteAdressCommand());
        addCommand(new CopyIPCommand());
        addCommand(new CopyDomainCommand());
        addCommand(new CopyServerVersionCommand());
        addCommand(new GetServerGeoCommand());
        addCommand(new GetSrvCommand());
        addCommand(new GetApiCommand());
        addCommand(new GetProxyCommand());
        addCommand(new TextToSpeechCommand());
        addCommand(new ExecCommand());
        addCommand(new CrackedLoginCommand());
        addCommand(new InfoCommand());
        addCommand(new ClearChatCommand());
        addCommand(new GamemodeCommand());
        addCommand(new WerbungCommand());
        addCommand(new BotAttackCommand());
        addCommand(new SingleCrasherCommand());
        addCommand(new MultiCrasherCommand());
        addCommand(new ClientChatCommand());
        addCommand(new IrcCommand());
        addCommand(new HastebinCommand());
        addCommand(new CopyMyIPCommand());
        addCommand(new CheckCmdBlockCommand());
        addCommand(new HologramCommand());
        addCommand(new DropCommand());
        addCommand(new ShutdownCommand());
        addCommand(new DisconnectCommand());
        addCommand(new ConnectCommand());
        addCommand(new SkullCommand());
        addCommand(new GiveCommand());
        addCommand(new TnTPlaceCommand());
        addCommand(new AdminKickerCommand());
        addCommand(new PermissionExCommand());
        addCommand(new AuthMeBypassCommand());
        addCommand(new SpamCommand());
        addCommand(new LabymodCommand());
        addCommand(new ConverterCommand());
    }
    
    public static boolean execute(String text) {
        if (!text.startsWith("+") && !text.startsWith("#") && !text.startsWith("-") && !text.startsWith(".") && !text.startsWith("!") && !text.startsWith("-")) {
            return false;
        }
        text = text.substring(1);
        final String[] arguments = text.split(" ");
        for (final Command cmd : CommandManager.commands) {
            if (!cmd.getName().equalsIgnoreCase(arguments[0])) {
                continue;
            }
            final String[] args = Arrays.copyOfRange(arguments, 1, arguments.length);
            cmd.execute(args);
            return true;
        }
        return false;
    }
    
    public static void addCommand(final Command command) {
        CommandManager.commands.add(command);
    }
}
