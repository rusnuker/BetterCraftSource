// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.command;

import net.minecraft.util.math.BlockPos;
import net.minecraft.server.MinecraftServer;
import java.util.List;

public interface ICommand extends Comparable<ICommand>
{
    String getCommandName();
    
    String getCommandUsage(final ICommandSender p0);
    
    List<String> getCommandAliases();
    
    void execute(final MinecraftServer p0, final ICommandSender p1, final String[] p2) throws CommandException;
    
    boolean checkPermission(final MinecraftServer p0, final ICommandSender p1);
    
    List<String> getTabCompletionOptions(final MinecraftServer p0, final ICommandSender p1, final String[] p2, final BlockPos p3);
    
    boolean isUsernameIndex(final String[] p0, final int p1);
}
