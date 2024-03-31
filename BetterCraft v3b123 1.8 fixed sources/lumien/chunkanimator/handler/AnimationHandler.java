// 
// Decompiled by Procyon v0.6.0
// 

package lumien.chunkanimator.handler;

import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.client.Minecraft;
import lumien.chunkanimator.ChunkAnimator;
import net.minecraft.client.renderer.chunk.RenderChunk;
import java.util.WeakHashMap;

public class AnimationHandler
{
    WeakHashMap<RenderChunk, AnimationData> timeStamps;
    
    public AnimationHandler() {
        this.timeStamps = new WeakHashMap<RenderChunk, AnimationData>();
    }
    
    public void preRender(final RenderChunk renderChunk) {
        if (this.timeStamps.containsKey(renderChunk)) {
            final AnimationData animationData = this.timeStamps.get(renderChunk);
            long time = animationData.timeStamp;
            int mode = ChunkAnimator.getInstance().mode;
            if (time == -1L) {
                time = (animationData.timeStamp = System.currentTimeMillis());
                if (mode == 4) {
                    BlockPos zeroedPlayerPosition = Minecraft.getMinecraft().thePlayer.getPosition();
                    zeroedPlayerPosition = zeroedPlayerPosition.add(0, -zeroedPlayerPosition.getY(), 0);
                    final BlockPos zeroedCenteredChunkPos;
                    final BlockPos dif = zeroedPlayerPosition.subtract(zeroedCenteredChunkPos = renderChunk.getPosition().add(8, -renderChunk.getPosition().getY(), 8));
                    final int difX = Math.abs(dif.getX());
                    final int difZ;
                    final EnumFacing chunkFacing = (difX > (difZ = Math.abs(dif.getZ()))) ? ((dif.getX() > 0) ? EnumFacing.EAST : EnumFacing.WEST) : ((dif.getZ() > 0) ? EnumFacing.SOUTH : EnumFacing.NORTH);
                    animationData.chunkFacing = chunkFacing;
                }
            }
            final long timeDif;
            final int animationDuration;
            if ((timeDif = System.currentTimeMillis() - time) < (animationDuration = ChunkAnimator.getInstance().animationDuration)) {
                final double chunkY = renderChunk.getPosition().getY();
                if (mode == 2) {
                    mode = ((chunkY >= Minecraft.getMinecraft().theWorld.getHorizon()) ? 1 : 0);
                }
                if (mode == 4) {
                    mode = 3;
                }
                switch (mode) {
                    case 0: {
                        final double modY = chunkY / animationDuration * timeDif;
                        GlStateManager.translate(0.0, -chunkY + modY, 0.0);
                        break;
                    }
                    case 1: {
                        final double modY = (256.0 - chunkY) / animationDuration * timeDif;
                        GlStateManager.translate(0.0, 256.0 - chunkY - modY, 0.0);
                        break;
                    }
                    case 3: {
                        final EnumFacing chunkFacing2 = animationData.chunkFacing;
                        if (chunkFacing2 == null) {
                            break;
                        }
                        final Vec3i vec = chunkFacing2.getDirectionVec();
                        final double mod = -(200.0 - 200.0 / animationDuration * timeDif);
                        GlStateManager.translate(vec.getX() * mod, 0.0, vec.getZ() * mod);
                        break;
                    }
                }
            }
            else {
                this.timeStamps.remove(renderChunk);
            }
        }
    }
    
    public void setPosition(final RenderChunk renderChunk, final BlockPos position) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            boolean flag = true;
            BlockPos zeroedPlayerPosition = Minecraft.getMinecraft().thePlayer.getPosition();
            zeroedPlayerPosition = zeroedPlayerPosition.add(0, -zeroedPlayerPosition.getY(), 0);
            final BlockPos zeroedCenteredChunkPos = position.add(8, -position.getY(), 8);
            if (ChunkAnimator.getInstance().disableAroundPlayer) {
                boolean b = false;
                if (zeroedPlayerPosition.distanceSq(zeroedCenteredChunkPos) <= 4096.0) {
                    b = false;
                }
                flag = b;
            }
            if (flag) {
                EnumFacing chunkFacing = null;
                if (ChunkAnimator.getInstance().mode == 3) {
                    final BlockPos dif = zeroedPlayerPosition.subtract(zeroedCenteredChunkPos);
                    final int difX = Math.abs(dif.getX());
                    final int difZ;
                    chunkFacing = ((difX > (difZ = Math.abs(dif.getZ()))) ? ((dif.getX() > 0) ? EnumFacing.EAST : EnumFacing.WEST) : ((dif.getZ() > 0) ? EnumFacing.SOUTH : EnumFacing.NORTH));
                }
                final AnimationData animationData = new AnimationData(-1L, chunkFacing);
                this.timeStamps.put(renderChunk, animationData);
            }
        }
    }
    
    private class AnimationData
    {
        public long timeStamp;
        public EnumFacing chunkFacing;
        
        public AnimationData(final long timeStamp, final EnumFacing chunkFacing) {
            this.timeStamp = timeStamp;
            this.chunkFacing = chunkFacing;
        }
    }
}
