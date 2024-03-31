// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.chunkanimator.handler;

import me.amkgre.bettercraft.client.gui.GuiMods;
import me.amkgre.bettercraft.client.mods.chunkanimator.easing.Elastic;
import me.amkgre.bettercraft.client.mods.chunkanimator.easing.Bounce;
import me.amkgre.bettercraft.client.mods.chunkanimator.easing.Back;
import me.amkgre.bettercraft.client.mods.chunkanimator.easing.Circ;
import me.amkgre.bettercraft.client.mods.chunkanimator.easing.Sine;
import me.amkgre.bettercraft.client.mods.chunkanimator.easing.Expo;
import me.amkgre.bettercraft.client.mods.chunkanimator.easing.Quint;
import me.amkgre.bettercraft.client.mods.chunkanimator.easing.Quart;
import me.amkgre.bettercraft.client.mods.chunkanimator.easing.Cubic;
import me.amkgre.bettercraft.client.mods.chunkanimator.easing.Quad;
import me.amkgre.bettercraft.client.mods.chunkanimator.easing.Linear;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.mods.chunkanimator.ChunkAnimator;
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
            int mode = ChunkAnimator.INSTANCE.mode;
            if (time == -1L) {
                time = System.currentTimeMillis();
                animationData.timeStamp = time;
                if (mode == 4) {
                    BlockPos zeroedPlayerPosition = Minecraft.getMinecraft().player.getPosition();
                    zeroedPlayerPosition = zeroedPlayerPosition.add(0, -zeroedPlayerPosition.getY(), 0);
                    final BlockPos zeroedCenteredChunkPos = renderChunk.getPosition().add(8, -renderChunk.getPosition().getY(), 8);
                    final Vec3i dif = zeroedPlayerPosition.subtract(zeroedCenteredChunkPos);
                    final int difX = Math.abs(dif.getX());
                    final int difZ = Math.abs(dif.getZ());
                    EnumFacing chunkFacing;
                    if (difX > difZ) {
                        if (dif.getX() > 0) {
                            chunkFacing = EnumFacing.EAST;
                        }
                        else {
                            chunkFacing = EnumFacing.WEST;
                        }
                    }
                    else if (dif.getZ() > 0) {
                        chunkFacing = EnumFacing.SOUTH;
                    }
                    else {
                        chunkFacing = EnumFacing.NORTH;
                    }
                    animationData.chunkFacing = chunkFacing;
                }
            }
            final long timeDif = System.currentTimeMillis() - time;
            final int animationDuration = ChunkAnimator.INSTANCE.animationDuration;
            if (timeDif < animationDuration) {
                final int chunkY = renderChunk.getPosition().getY();
                if (mode == 2) {
                    if (chunkY < Minecraft.getMinecraft().world.getHorizon()) {
                        mode = 0;
                    }
                    else {
                        mode = 1;
                    }
                }
                if (mode == 4) {
                    mode = 3;
                }
                switch (mode) {
                    case 0: {
                        GlStateManager.translate(0.0f, -chunkY + this.getFunctionValue((float)timeDif, 0.0f, (float)chunkY, (float)animationDuration), 0.0f);
                        break;
                    }
                    case 1: {
                        GlStateManager.translate(0.0f, 256 - chunkY - this.getFunctionValue((float)timeDif, 0.0f, (float)(256 - chunkY), (float)animationDuration), 0.0f);
                        break;
                    }
                    case 3: {
                        final EnumFacing chunkFacing2 = animationData.chunkFacing;
                        if (chunkFacing2 != null) {
                            final Vec3i vec = chunkFacing2.getDirectionVec();
                            double mod = -(200.0 - 200.0 / animationDuration * timeDif);
                            mod = -(200.0f - this.getFunctionValue((float)timeDif, 0.0f, 200.0f, (float)animationDuration));
                            GlStateManager.translate(vec.getX() * mod, 0.0, vec.getZ() * mod);
                            break;
                        }
                        break;
                    }
                }
            }
            else {
                this.timeStamps.remove(renderChunk);
            }
        }
    }
    
    private float getFunctionValue(final float t, final float b, final float c, final float d) {
        switch (ChunkAnimator.INSTANCE.easingFunction) {
            case 0: {
                return Linear.easeOut(t, b, c, d);
            }
            case 1: {
                return Quad.easeOut(t, b, c, d);
            }
            case 2: {
                return Cubic.easeOut(t, b, c, d);
            }
            case 3: {
                return Quart.easeOut(t, b, c, d);
            }
            case 4: {
                return Quint.easeOut(t, b, c, d);
            }
            case 5: {
                return Expo.easeOut(t, b, c, d);
            }
            case 6: {
                return Sine.easeOut(t, b, c, d);
            }
            case 7: {
                return Circ.easeOut(t, b, c, d);
            }
            case 8: {
                return Back.easeOut(t, b, c, d);
            }
            case 9: {
                return Bounce.easeOut(t, b, c, d);
            }
            case 10: {
                return Elastic.easeOut(t, b, c, d);
            }
            default: {
                return Sine.easeOut(t, b, c, d);
            }
        }
    }
    
    public void setOrigin(final RenderChunk renderChunk, final BlockPos position) {
        if (GuiMods.chunkanimator && Minecraft.getMinecraft().player != null) {
            boolean flag = true;
            BlockPos zeroedPlayerPosition = Minecraft.getMinecraft().player.getPosition();
            zeroedPlayerPosition = zeroedPlayerPosition.add(0, -zeroedPlayerPosition.getY(), 0);
            final BlockPos zeroedCenteredChunkPos = position.add(8, -position.getY(), 8);
            if (ChunkAnimator.INSTANCE.disableAroundPlayer) {
                flag = (zeroedPlayerPosition.distanceSq(zeroedCenteredChunkPos) > 4096.0);
            }
            if (flag) {
                EnumFacing chunkFacing = null;
                if (ChunkAnimator.INSTANCE.mode == 3) {
                    final Vec3i dif = zeroedPlayerPosition.subtract(zeroedCenteredChunkPos);
                    final int difX = Math.abs(dif.getX());
                    final int difZ = Math.abs(dif.getZ());
                    if (difX > difZ) {
                        if (dif.getX() > 0) {
                            chunkFacing = EnumFacing.EAST;
                        }
                        else {
                            chunkFacing = EnumFacing.WEST;
                        }
                    }
                    else if (dif.getZ() > 0) {
                        chunkFacing = EnumFacing.SOUTH;
                    }
                    else {
                        chunkFacing = EnumFacing.NORTH;
                    }
                }
                final AnimationData animationData = new AnimationData(-1L, chunkFacing);
                this.timeStamps.put(renderChunk, animationData);
            }
            else if (this.timeStamps.containsKey(renderChunk)) {
                this.timeStamps.remove(renderChunk);
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
