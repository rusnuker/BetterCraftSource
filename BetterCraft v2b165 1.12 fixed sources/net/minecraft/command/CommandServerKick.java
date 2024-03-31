// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.server.MinecraftServer;

public class CommandServerKick extends CommandBase
{
    @Override
    public String getCommandName() {
        return "kick";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.kick.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length <= 0 || args[0].length() <= 1) {
            throw new WrongUsageException("commands.kick.usage", new Object[0]);
        }
        final EntityPlayerMP entityplayermp = server.getPlayerList().getPlayerByUsername(args[0]);
        if (entityplayermp == null) {
            throw new PlayerNotFoundException("commands.generic.player.notFound", new Object[] { args[0] });
        }
        if (args.length >= 2) {
            final ITextComponent itextcomponent = CommandBase.getChatComponentFromNthArg(sender, args, 1);
            entityplayermp.connection.func_194028_b(itextcomponent);
            CommandBase.notifyCommandListener(sender, this, "commands.kick.success.reason", entityplayermp.getName(), itextcomponent.getUnformattedText());
        }
        else {
            entityplayermp.connection.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.kicked", new Object[0]));
            CommandBase.notifyCommandListener(sender, this, "commands.kick.success", entityplayermp.getName());
        }
    }
    
    @Override
    public List<String> getTabCompletionOptions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos pos) {
        return (args.length >= 1) ? CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.emptyList();
    }
}
