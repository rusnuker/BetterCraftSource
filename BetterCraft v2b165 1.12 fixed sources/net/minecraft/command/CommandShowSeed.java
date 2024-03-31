// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.command;

import net.minecraft.world.World;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandShowSeed extends CommandBase
{
    @Override
    public boolean checkPermission(final MinecraftServer server, final ICommandSender sender) {
        return server.isSinglePlayer() || super.checkPermission(server, sender);
    }
    
    @Override
    public String getCommandName() {
        return "seed";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.seed.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        final World world = (sender instanceof EntityPlayer) ? ((EntityPlayer)sender).world : server.worldServerForDimension(0);
        sender.addChatMessage(new TextComponentTranslation("commands.seed.success", new Object[] { world.getSeed() }));
    }
}
