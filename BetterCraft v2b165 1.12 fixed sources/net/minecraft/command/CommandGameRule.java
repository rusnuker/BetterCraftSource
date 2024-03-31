// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.GameRules;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.server.MinecraftServer;

public class CommandGameRule extends CommandBase
{
    @Override
    public String getCommandName() {
        return "gamerule";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.gamerule.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        final GameRules gamerules = this.getOverWorldGameRules(server);
        final String s = (args.length > 0) ? args[0] : "";
        final String s2 = (args.length > 1) ? CommandBase.buildString(args, 1) : "";
        switch (args.length) {
            case 0: {
                sender.addChatMessage(new TextComponentString(CommandBase.joinNiceString(gamerules.getRules())));
                break;
            }
            case 1: {
                if (!gamerules.hasRule(s)) {
                    throw new CommandException("commands.gamerule.norule", new Object[] { s });
                }
                final String s3 = gamerules.getString(s);
                sender.addChatMessage(new TextComponentString(s).appendText(" = ").appendText(s3));
                sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, gamerules.getInt(s));
                break;
            }
            default: {
                if (gamerules.areSameType(s, GameRules.ValueType.BOOLEAN_VALUE) && !"true".equals(s2) && !"false".equals(s2)) {
                    throw new CommandException("commands.generic.boolean.invalid", new Object[] { s2 });
                }
                gamerules.setOrCreateGameRule(s, s2);
                notifyGameRuleChange(gamerules, s, server);
                CommandBase.notifyCommandListener(sender, this, "commands.gamerule.success", s, s2);
                break;
            }
        }
    }
    
    public static void notifyGameRuleChange(final GameRules rules, final String p_184898_1_, final MinecraftServer server) {
        if ("reducedDebugInfo".equals(p_184898_1_)) {
            final byte b0 = (byte)(rules.getBoolean(p_184898_1_) ? 22 : 23);
            for (final EntityPlayerMP entityplayermp : server.getPlayerList().getPlayerList()) {
                entityplayermp.connection.sendPacket(new SPacketEntityStatus(entityplayermp, b0));
            }
        }
    }
    
    @Override
    public List<String> getTabCompletionOptions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos pos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, this.getOverWorldGameRules(server).getRules());
        }
        if (args.length == 2) {
            final GameRules gamerules = this.getOverWorldGameRules(server);
            if (gamerules.areSameType(args[0], GameRules.ValueType.BOOLEAN_VALUE)) {
                return CommandBase.getListOfStringsMatchingLastWord(args, "true", "false");
            }
            if (gamerules.areSameType(args[0], GameRules.ValueType.FUNCTION)) {
                return CommandBase.getListOfStringsMatchingLastWord(args, server.func_193030_aL().func_193066_d().keySet());
            }
        }
        return Collections.emptyList();
    }
    
    private GameRules getOverWorldGameRules(final MinecraftServer server) {
        return server.worldServerForDimension(0).getGameRules();
    }
}
