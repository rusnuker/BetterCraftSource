// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.command.CommandException;
import net.minecraft.world.WorldServer;
import net.minecraft.world.MinecraftException;
import net.minecraft.command.ICommand;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandSaveAll extends CommandBase
{
    @Override
    public String getCommandName() {
        return "save-all";
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.save.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        sender.addChatMessage(new TextComponentTranslation("commands.save.start", new Object[0]));
        if (server.getPlayerList() != null) {
            server.getPlayerList().saveAllPlayerData();
        }
        try {
            for (int i = 0; i < server.worldServers.length; ++i) {
                if (server.worldServers[i] != null) {
                    final WorldServer worldserver = server.worldServers[i];
                    final boolean flag = worldserver.disableLevelSaving;
                    worldserver.disableLevelSaving = false;
                    worldserver.saveAllChunks(true, null);
                    worldserver.disableLevelSaving = flag;
                }
            }
            if (args.length > 0 && "flush".equals(args[0])) {
                sender.addChatMessage(new TextComponentTranslation("commands.save.flushStart", new Object[0]));
                for (int j = 0; j < server.worldServers.length; ++j) {
                    if (server.worldServers[j] != null) {
                        final WorldServer worldserver2 = server.worldServers[j];
                        final boolean flag2 = worldserver2.disableLevelSaving;
                        worldserver2.disableLevelSaving = false;
                        worldserver2.saveChunkData();
                        worldserver2.disableLevelSaving = flag2;
                    }
                }
                sender.addChatMessage(new TextComponentTranslation("commands.save.flushEnd", new Object[0]));
            }
        }
        catch (final MinecraftException minecraftexception) {
            CommandBase.notifyCommandListener(sender, this, "commands.save.failed", minecraftexception.getMessage());
            return;
        }
        CommandBase.notifyCommandListener(sender, this, "commands.save.success", new Object[0]);
    }
    
    @Override
    public List<String> getTabCompletionOptions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, "flush") : Collections.emptyList();
    }
}
