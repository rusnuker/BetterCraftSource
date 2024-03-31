// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import java.util.Set;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import java.util.EnumSet;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandTP extends CommandBase
{
    @Override
    public String getCommandName() {
        return "tp";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.tp.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.tp.usage", new Object[0]);
        }
        int i = 0;
        Entity entity;
        if (args.length != 2 && args.length != 4 && args.length != 6) {
            entity = CommandBase.getCommandSenderAsPlayer(sender);
        }
        else {
            entity = CommandBase.getEntity(server, sender, args[0]);
            i = 1;
        }
        if (args.length != 1 && args.length != 2) {
            if (args.length < i + 3) {
                throw new WrongUsageException("commands.tp.usage", new Object[0]);
            }
            if (entity.world != null) {
                final int j = 4096;
                int k = i + 1;
                final CoordinateArg commandbase$coordinatearg = CommandBase.parseCoordinate(entity.posX, args[i], true);
                final CoordinateArg commandbase$coordinatearg2 = CommandBase.parseCoordinate(entity.posY, args[k++], -4096, 4096, false);
                final CoordinateArg commandbase$coordinatearg3 = CommandBase.parseCoordinate(entity.posZ, args[k++], true);
                final CoordinateArg commandbase$coordinatearg4 = CommandBase.parseCoordinate(entity.rotationYaw, (args.length > k) ? args[k++] : "~", false);
                final CoordinateArg commandbase$coordinatearg5 = CommandBase.parseCoordinate(entity.rotationPitch, (args.length > k) ? args[k] : "~", false);
                teleportEntityToCoordinates(entity, commandbase$coordinatearg, commandbase$coordinatearg2, commandbase$coordinatearg3, commandbase$coordinatearg4, commandbase$coordinatearg5);
                CommandBase.notifyCommandListener(sender, this, "commands.tp.success.coordinates", entity.getName(), commandbase$coordinatearg.getResult(), commandbase$coordinatearg2.getResult(), commandbase$coordinatearg3.getResult());
            }
        }
        else {
            final Entity entity2 = CommandBase.getEntity(server, sender, args[args.length - 1]);
            if (entity2.world != entity.world) {
                throw new CommandException("commands.tp.notSameDimension", new Object[0]);
            }
            entity.dismountRidingEntity();
            if (entity instanceof EntityPlayerMP) {
                ((EntityPlayerMP)entity).connection.setPlayerLocation(entity2.posX, entity2.posY, entity2.posZ, entity2.rotationYaw, entity2.rotationPitch);
            }
            else {
                entity.setLocationAndAngles(entity2.posX, entity2.posY, entity2.posZ, entity2.rotationYaw, entity2.rotationPitch);
            }
            CommandBase.notifyCommandListener(sender, this, "commands.tp.success", entity.getName(), entity2.getName());
        }
    }
    
    private static void teleportEntityToCoordinates(final Entity p_189863_0_, final CoordinateArg p_189863_1_, final CoordinateArg p_189863_2_, final CoordinateArg p_189863_3_, final CoordinateArg p_189863_4_, final CoordinateArg p_189863_5_) {
        if (p_189863_0_ instanceof EntityPlayerMP) {
            final Set<SPacketPlayerPosLook.EnumFlags> set = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);
            if (p_189863_1_.isRelative()) {
                set.add(SPacketPlayerPosLook.EnumFlags.X);
            }
            if (p_189863_2_.isRelative()) {
                set.add(SPacketPlayerPosLook.EnumFlags.Y);
            }
            if (p_189863_3_.isRelative()) {
                set.add(SPacketPlayerPosLook.EnumFlags.Z);
            }
            if (p_189863_5_.isRelative()) {
                set.add(SPacketPlayerPosLook.EnumFlags.X_ROT);
            }
            if (p_189863_4_.isRelative()) {
                set.add(SPacketPlayerPosLook.EnumFlags.Y_ROT);
            }
            float f = (float)p_189863_4_.getAmount();
            if (!p_189863_4_.isRelative()) {
                f = MathHelper.wrapDegrees(f);
            }
            float f2 = (float)p_189863_5_.getAmount();
            if (!p_189863_5_.isRelative()) {
                f2 = MathHelper.wrapDegrees(f2);
            }
            p_189863_0_.dismountRidingEntity();
            ((EntityPlayerMP)p_189863_0_).connection.setPlayerLocation(p_189863_1_.getAmount(), p_189863_2_.getAmount(), p_189863_3_.getAmount(), f, f2, set);
            p_189863_0_.setRotationYawHead(f);
        }
        else {
            final float f3 = (float)MathHelper.wrapDegrees(p_189863_4_.getResult());
            float f4 = (float)MathHelper.wrapDegrees(p_189863_5_.getResult());
            f4 = MathHelper.clamp(f4, -90.0f, 90.0f);
            p_189863_0_.setLocationAndAngles(p_189863_1_.getResult(), p_189863_2_.getResult(), p_189863_3_.getResult(), f3, f4);
            p_189863_0_.setRotationYawHead(f3);
        }
        if (!(p_189863_0_ instanceof EntityLivingBase) || !((EntityLivingBase)p_189863_0_).isElytraFlying()) {
            p_189863_0_.motionY = 0.0;
            p_189863_0_.onGround = true;
        }
    }
    
    @Override
    public List<String> getTabCompletionOptions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos pos) {
        return (args.length != 1 && args.length != 2) ? Collections.emptyList() : CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
