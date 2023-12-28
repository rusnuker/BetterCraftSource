// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.command.server;

import java.util.Collections;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.ICommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandOp extends CommandBase
{
    @Override
    public String getCommandName() {
        return "op";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.op.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length != 1 || args[0].length() <= 0) {
            throw new WrongUsageException("commands.op.usage", new Object[0]);
        }
        final GameProfile gameprofile = server.getPlayerProfileCache().getGameProfileForUsername(args[0]);
        if (gameprofile == null) {
            throw new CommandException("commands.op.failed", new Object[] { args[0] });
        }
        server.getPlayerList().addOp(gameprofile);
        CommandBase.notifyCommandListener(sender, this, "commands.op.success", args[0]);
    }
    
    @Override
    public List<String> getTabCompletionOptions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos pos) {
        if (args.length == 1) {
            final String s = args[args.length - 1];
            final List<String> list = (List<String>)Lists.newArrayList();
            GameProfile[] gameProfiles;
            for (int length = (gameProfiles = server.getGameProfiles()).length, i = 0; i < length; ++i) {
                final GameProfile gameprofile = gameProfiles[i];
                if (!server.getPlayerList().canSendCommands(gameprofile) && CommandBase.doesStringStartWith(s, gameprofile.getName())) {
                    list.add(gameprofile.getName());
                }
            }
            return list;
        }
        return Collections.emptyList();
    }
}
