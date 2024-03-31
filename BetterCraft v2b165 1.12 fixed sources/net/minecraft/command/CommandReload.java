// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;

public class CommandReload extends CommandBase
{
    @Override
    public String getCommandName() {
        return "reload";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.reload.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length > 0) {
            throw new WrongUsageException("commands.reload.usage", new Object[0]);
        }
        server.func_193031_aM();
        CommandBase.notifyCommandListener(sender, this, "commands.reload.success", new Object[0]);
    }
}
