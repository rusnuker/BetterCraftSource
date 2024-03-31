// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.command;

import net.minecraft.world.storage.WorldInfo;
import net.minecraft.server.MinecraftServer;

public class CommandToggleDownfall extends CommandBase
{
    @Override
    public String getCommandName() {
        return "toggledownfall";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.downfall.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        this.toggleRainfall(server);
        CommandBase.notifyCommandListener(sender, this, "commands.downfall.success", new Object[0]);
    }
    
    protected void toggleRainfall(final MinecraftServer server) {
        final WorldInfo worldinfo = server.worldServers[0].getWorldInfo();
        worldinfo.setRaining(!worldinfo.isRaining());
    }
}
