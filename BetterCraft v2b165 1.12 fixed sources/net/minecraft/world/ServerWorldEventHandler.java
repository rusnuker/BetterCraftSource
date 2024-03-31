// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world;

import java.util.Iterator;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

public class ServerWorldEventHandler implements IWorldEventListener
{
    private final MinecraftServer mcServer;
    private final WorldServer theWorldServer;
    
    public ServerWorldEventHandler(final MinecraftServer mcServerIn, final WorldServer worldServerIn) {
        this.mcServer = mcServerIn;
        this.theWorldServer = worldServerIn;
    }
    
    @Override
    public void spawnParticle(final int particleID, final boolean ignoreRange, final double xCoord, final double yCoord, final double zCoord, final double xSpeed, final double ySpeed, final double zSpeed, final int... parameters) {
    }
    
    @Override
    public void func_190570_a(final int p_190570_1_, final boolean p_190570_2_, final boolean p_190570_3_, final double p_190570_4_, final double p_190570_6_, final double p_190570_8_, final double p_190570_10_, final double p_190570_12_, final double p_190570_14_, final int... p_190570_16_) {
    }
    
    @Override
    public void onEntityAdded(final Entity entityIn) {
        this.theWorldServer.getEntityTracker().trackEntity(entityIn);
        if (entityIn instanceof EntityPlayerMP) {
            this.theWorldServer.provider.onPlayerAdded((EntityPlayerMP)entityIn);
        }
    }
    
    @Override
    public void onEntityRemoved(final Entity entityIn) {
        this.theWorldServer.getEntityTracker().untrackEntity(entityIn);
        this.theWorldServer.getScoreboard().removeEntity(entityIn);
        if (entityIn instanceof EntityPlayerMP) {
            this.theWorldServer.provider.onPlayerRemoved((EntityPlayerMP)entityIn);
        }
    }
    
    @Override
    public void playSoundToAllNearExcept(@Nullable final EntityPlayer player, final SoundEvent soundIn, final SoundCategory category, final double x, final double y, final double z, final float volume, final float pitch) {
        this.mcServer.getPlayerList().sendToAllNearExcept(player, x, y, z, (volume > 1.0f) ? ((double)(16.0f * volume)) : 16.0, this.theWorldServer.provider.getDimensionType().getId(), new SPacketSoundEffect(soundIn, category, x, y, z, volume, pitch));
    }
    
    @Override
    public void markBlockRangeForRenderUpdate(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
    }
    
    @Override
    public void notifyBlockUpdate(final World worldIn, final BlockPos pos, final IBlockState oldState, final IBlockState newState, final int flags) {
        this.theWorldServer.getPlayerChunkMap().markBlockForUpdate(pos);
    }
    
    @Override
    public void notifyLightSet(final BlockPos pos) {
    }
    
    @Override
    public void playRecord(final SoundEvent soundIn, final BlockPos pos) {
    }
    
    @Override
    public void playEvent(final EntityPlayer player, final int type, final BlockPos blockPosIn, final int data) {
        this.mcServer.getPlayerList().sendToAllNearExcept(player, blockPosIn.getX(), blockPosIn.getY(), blockPosIn.getZ(), 64.0, this.theWorldServer.provider.getDimensionType().getId(), new SPacketEffect(type, blockPosIn, data, false));
    }
    
    @Override
    public void broadcastSound(final int soundID, final BlockPos pos, final int data) {
        this.mcServer.getPlayerList().sendPacketToAllPlayers(new SPacketEffect(soundID, pos, data, true));
    }
    
    @Override
    public void sendBlockBreakProgress(final int breakerId, final BlockPos pos, final int progress) {
        for (final EntityPlayerMP entityplayermp : this.mcServer.getPlayerList().getPlayerList()) {
            if (entityplayermp != null && entityplayermp.world == this.theWorldServer && entityplayermp.getEntityId() != breakerId) {
                final double d0 = pos.getX() - entityplayermp.posX;
                final double d2 = pos.getY() - entityplayermp.posY;
                final double d3 = pos.getZ() - entityplayermp.posZ;
                if (d0 * d0 + d2 * d2 + d3 * d3 >= 1024.0) {
                    continue;
                }
                entityplayermp.connection.sendPacket(new SPacketBlockBreakAnim(breakerId, pos, progress));
            }
        }
    }
}
