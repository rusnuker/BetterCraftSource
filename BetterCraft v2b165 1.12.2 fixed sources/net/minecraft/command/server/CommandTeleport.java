// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import java.util.Set;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import java.util.EnumSet;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.command.CommandException;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.command.ICommand;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandTeleport extends CommandBase
{
    @Override
    public String getCommandName() {
        return "teleport";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.teleport.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 4) {
            throw new WrongUsageException("commands.teleport.usage", new Object[0]);
        }
        final Entity entity = CommandBase.getEntity(server, sender, args[0]);
        if (entity.world != null) {
            final int i = 4096;
            final Vec3d vec3d = sender.getPositionVector();
            int j = 1;
            final CoordinateArg commandbase$coordinatearg = CommandBase.parseCoordinate(vec3d.xCoord, args[j++], true);
            final CoordinateArg commandbase$coordinatearg2 = CommandBase.parseCoordinate(vec3d.yCoord, args[j++], -4096, 4096, false);
            final CoordinateArg commandbase$coordinatearg3 = CommandBase.parseCoordinate(vec3d.zCoord, args[j++], true);
            final Entity entity2 = (sender.getCommandSenderEntity() == null) ? entity : sender.getCommandSenderEntity();
            final CoordinateArg commandbase$coordinatearg4 = CommandBase.parseCoordinate((args.length > j) ? ((double)entity2.rotationYaw) : ((double)entity.rotationYaw), (args.length > j) ? args[j] : "~", false);
            ++j;
            final CoordinateArg commandbase$coordinatearg5 = CommandBase.parseCoordinate((args.length > j) ? ((double)entity2.rotationPitch) : ((double)entity.rotationPitch), (args.length > j) ? args[j] : "~", false);
            doTeleport(entity, commandbase$coordinatearg, commandbase$coordinatearg2, commandbase$coordinatearg3, commandbase$coordinatearg4, commandbase$coordinatearg5);
            CommandBase.notifyCommandListener(sender, this, "commands.teleport.success.coordinates", entity.getName(), commandbase$coordinatearg.getResult(), commandbase$coordinatearg2.getResult(), commandbase$coordinatearg3.getResult());
        }
    }
    
    private static void doTeleport(final Entity p_189862_0_, final CoordinateArg p_189862_1_, final CoordinateArg p_189862_2_, final CoordinateArg p_189862_3_, final CoordinateArg p_189862_4_, final CoordinateArg p_189862_5_) {
        if (p_189862_0_ instanceof EntityPlayerMP) {
            final Set<SPacketPlayerPosLook.EnumFlags> set = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);
            float f = (float)p_189862_4_.getAmount();
            if (p_189862_4_.isRelative()) {
                set.add(SPacketPlayerPosLook.EnumFlags.Y_ROT);
            }
            else {
                f = MathHelper.wrapDegrees(f);
            }
            float f2 = (float)p_189862_5_.getAmount();
            if (p_189862_5_.isRelative()) {
                set.add(SPacketPlayerPosLook.EnumFlags.X_ROT);
            }
            else {
                f2 = MathHelper.wrapDegrees(f2);
            }
            p_189862_0_.dismountRidingEntity();
            ((EntityPlayerMP)p_189862_0_).connection.setPlayerLocation(p_189862_1_.getResult(), p_189862_2_.getResult(), p_189862_3_.getResult(), f, f2, set);
            p_189862_0_.setRotationYawHead(f);
        }
        else {
            final float f3 = (float)MathHelper.wrapDegrees(p_189862_4_.getResult());
            float f4 = (float)MathHelper.wrapDegrees(p_189862_5_.getResult());
            f4 = MathHelper.clamp(f4, -90.0f, 90.0f);
            p_189862_0_.setLocationAndAngles(p_189862_1_.getResult(), p_189862_2_.getResult(), p_189862_3_.getResult(), f3, f4);
            p_189862_0_.setRotationYawHead(f3);
        }
        if (!(p_189862_0_ instanceof EntityLivingBase) || !((EntityLivingBase)p_189862_0_).isElytraFlying()) {
            p_189862_0_.motionY = 0.0;
            p_189862_0_.onGround = true;
        }
    }
    
    @Override
    public List<String> getTabCompletionOptions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos pos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }
        return (args.length > 1 && args.length <= 4) ? CommandBase.getTabCompletionCoordinate(args, 1, pos) : Collections.emptyList();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
