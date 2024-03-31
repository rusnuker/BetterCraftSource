// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.command.server;

import net.minecraft.server.management.UserList;
import java.util.Iterator;
import net.minecraft.command.ICommand;
import net.minecraft.util.text.TextComponentTranslation;
import java.util.Date;
import net.minecraft.server.management.UserListIPBansEntry;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.regex.Matcher;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import java.util.regex.Pattern;
import net.minecraft.command.CommandBase;

public class CommandBanIp extends CommandBase
{
    public static final Pattern IP_PATTERN;
    
    static {
        IP_PATTERN = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    }
    
    @Override
    public String getCommandName() {
        return "ban-ip";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
    
    @Override
    public boolean checkPermission(final MinecraftServer server, final ICommandSender sender) {
        return server.getPlayerList().getBannedIPs().isLanServer() && super.checkPermission(server, sender);
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.banip.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length >= 1 && args[0].length() > 1) {
            final ITextComponent itextcomponent = (args.length >= 2) ? CommandBase.getChatComponentFromNthArg(sender, args, 1) : null;
            final Matcher matcher = CommandBanIp.IP_PATTERN.matcher(args[0]);
            if (matcher.matches()) {
                this.banIp(server, sender, args[0], (itextcomponent == null) ? null : itextcomponent.getUnformattedText());
            }
            else {
                final EntityPlayerMP entityplayermp = server.getPlayerList().getPlayerByUsername(args[0]);
                if (entityplayermp == null) {
                    throw new PlayerNotFoundException("commands.banip.invalid");
                }
                this.banIp(server, sender, entityplayermp.getPlayerIP(), (itextcomponent == null) ? null : itextcomponent.getUnformattedText());
            }
            return;
        }
        throw new WrongUsageException("commands.banip.usage", new Object[0]);
    }
    
    @Override
    public List<String> getTabCompletionOptions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.emptyList();
    }
    
    protected void banIp(final MinecraftServer server, final ICommandSender sender, final String ipAddress, @Nullable final String banReason) {
        final UserListIPBansEntry userlistipbansentry = new UserListIPBansEntry(ipAddress, null, sender.getName(), null, banReason);
        ((UserList<K, UserListIPBansEntry>)server.getPlayerList().getBannedIPs()).addEntry(userlistipbansentry);
        final List<EntityPlayerMP> list = server.getPlayerList().getPlayersMatchingAddress(ipAddress);
        final String[] astring = new String[list.size()];
        int i = 0;
        for (final EntityPlayerMP entityplayermp : list) {
            entityplayermp.connection.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.ip_banned", new Object[0]));
            astring[i++] = entityplayermp.getName();
        }
        if (list.isEmpty()) {
            CommandBase.notifyCommandListener(sender, this, "commands.banip.success", ipAddress);
        }
        else {
            CommandBase.notifyCommandListener(sender, this, "commands.banip.success.players", ipAddress, CommandBase.joinNiceString(astring));
        }
    }
}
