// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.debug;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.pathfinding.PathPoint;
import java.util.Iterator;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.GlStateManager;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import java.util.Map;
import net.minecraft.client.Minecraft;

public class DebugRendererPathfinding implements DebugRenderer.IDebugRenderer
{
    private final Minecraft minecraft;
    private final Map<Integer, Path> pathMap;
    private final Map<Integer, Float> pathMaxDistance;
    private final Map<Integer, Long> creationMap;
    private EntityPlayer player;
    private double xo;
    private double yo;
    private double zo;
    
    public DebugRendererPathfinding(final Minecraft minecraftIn) {
        this.pathMap = (Map<Integer, Path>)Maps.newHashMap();
        this.pathMaxDistance = (Map<Integer, Float>)Maps.newHashMap();
        this.creationMap = (Map<Integer, Long>)Maps.newHashMap();
        this.minecraft = minecraftIn;
    }
    
    public void addPath(final int p_188289_1_, final Path p_188289_2_, final float p_188289_3_) {
        this.pathMap.put(p_188289_1_, p_188289_2_);
        this.creationMap.put(p_188289_1_, System.currentTimeMillis());
        this.pathMaxDistance.put(p_188289_1_, p_188289_3_);
    }
    
    @Override
    public void render(final float p_190060_1_, final long p_190060_2_) {
        if (!this.pathMap.isEmpty()) {
            final long i = System.currentTimeMillis();
            this.player = this.minecraft.player;
            this.xo = this.player.lastTickPosX + (this.player.posX - this.player.lastTickPosX) * p_190060_1_;
            this.yo = this.player.lastTickPosY + (this.player.posY - this.player.lastTickPosY) * p_190060_1_;
            this.zo = this.player.lastTickPosZ + (this.player.posZ - this.player.lastTickPosZ) * p_190060_1_;
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.color(0.0f, 1.0f, 0.0f, 0.75f);
            GlStateManager.disableTexture2D();
            GlStateManager.glLineWidth(6.0f);
            for (final Integer integer : this.pathMap.keySet()) {
                final Path path = this.pathMap.get(integer);
                final float f = this.pathMaxDistance.get(integer);
                this.renderPathLine(p_190060_1_, path);
                final PathPoint pathpoint = path.getTarget();
                if (this.addDistanceToPlayer(pathpoint) <= 40.0f) {
                    RenderGlobal.renderFilledBox(new AxisAlignedBB(pathpoint.xCoord + 0.25f, pathpoint.yCoord + 0.25f, pathpoint.zCoord + 0.25, pathpoint.xCoord + 0.75f, pathpoint.yCoord + 0.75f, pathpoint.zCoord + 0.75f).offset(-this.xo, -this.yo, -this.zo), 0.0f, 1.0f, 0.0f, 0.5f);
                    for (int j = 0; j < path.getCurrentPathLength(); ++j) {
                        final PathPoint pathpoint2 = path.getPathPointFromIndex(j);
                        if (this.addDistanceToPlayer(pathpoint2) <= 40.0f) {
                            final float f2 = (j == path.getCurrentPathIndex()) ? 1.0f : 0.0f;
                            final float f3 = (j == path.getCurrentPathIndex()) ? 0.0f : 1.0f;
                            RenderGlobal.renderFilledBox(new AxisAlignedBB(pathpoint2.xCoord + 0.5f - f, pathpoint2.yCoord + 0.01f * j, pathpoint2.zCoord + 0.5f - f, pathpoint2.xCoord + 0.5f + f, pathpoint2.yCoord + 0.25f + 0.01f * j, pathpoint2.zCoord + 0.5f + f).offset(-this.xo, -this.yo, -this.zo), f2, 0.0f, f3, 0.5f);
                        }
                    }
                }
            }
            for (final Integer integer2 : this.pathMap.keySet()) {
                final Path path2 = this.pathMap.get(integer2);
                PathPoint[] closedSet;
                for (int length = (closedSet = path2.getClosedSet()).length, l = 0; l < length; ++l) {
                    final PathPoint pathpoint3 = closedSet[l];
                    if (this.addDistanceToPlayer(pathpoint3) <= 40.0f) {
                        DebugRenderer.renderDebugText(String.format("%s", pathpoint3.nodeType), pathpoint3.xCoord + 0.5, pathpoint3.yCoord + 0.75, pathpoint3.zCoord + 0.5, p_190060_1_, -65536);
                        DebugRenderer.renderDebugText(String.format("%.2f", pathpoint3.costMalus), pathpoint3.xCoord + 0.5, pathpoint3.yCoord + 0.25, pathpoint3.zCoord + 0.5, p_190060_1_, -65536);
                    }
                }
                PathPoint[] openSet;
                for (int length2 = (openSet = path2.getOpenSet()).length, n = 0; n < length2; ++n) {
                    final PathPoint pathpoint4 = openSet[n];
                    if (this.addDistanceToPlayer(pathpoint4) <= 40.0f) {
                        DebugRenderer.renderDebugText(String.format("%s", pathpoint4.nodeType), pathpoint4.xCoord + 0.5, pathpoint4.yCoord + 0.75, pathpoint4.zCoord + 0.5, p_190060_1_, -16776961);
                        DebugRenderer.renderDebugText(String.format("%.2f", pathpoint4.costMalus), pathpoint4.xCoord + 0.5, pathpoint4.yCoord + 0.25, pathpoint4.zCoord + 0.5, p_190060_1_, -16776961);
                    }
                }
                for (int k = 0; k < path2.getCurrentPathLength(); ++k) {
                    final PathPoint pathpoint5 = path2.getPathPointFromIndex(k);
                    if (this.addDistanceToPlayer(pathpoint5) <= 40.0f) {
                        DebugRenderer.renderDebugText(String.format("%s", pathpoint5.nodeType), pathpoint5.xCoord + 0.5, pathpoint5.yCoord + 0.75, pathpoint5.zCoord + 0.5, p_190060_1_, -1);
                        DebugRenderer.renderDebugText(String.format("%.2f", pathpoint5.costMalus), pathpoint5.xCoord + 0.5, pathpoint5.yCoord + 0.25, pathpoint5.zCoord + 0.5, p_190060_1_, -1);
                    }
                }
            }
            Integer[] array;
            for (int length3 = (array = this.creationMap.keySet().toArray(new Integer[0])).length, n2 = 0; n2 < length3; ++n2) {
                final Integer integer3 = array[n2];
                if (i - this.creationMap.get(integer3) > 20000L) {
                    this.pathMap.remove(integer3);
                    this.creationMap.remove(integer3);
                }
            }
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
    
    public void renderPathLine(final float p_190067_1_, final Path p_190067_2_) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i < p_190067_2_.getCurrentPathLength(); ++i) {
            final PathPoint pathpoint = p_190067_2_.getPathPointFromIndex(i);
            if (this.addDistanceToPlayer(pathpoint) <= 40.0f) {
                final float f = i / (float)p_190067_2_.getCurrentPathLength() * 0.33f;
                final int j = (i == 0) ? 0 : MathHelper.hsvToRGB(f, 0.9f, 0.9f);
                final int k = j >> 16 & 0xFF;
                final int l = j >> 8 & 0xFF;
                final int i2 = j & 0xFF;
                bufferbuilder.pos(pathpoint.xCoord - this.xo + 0.5, pathpoint.yCoord - this.yo + 0.5, pathpoint.zCoord - this.zo + 0.5).color(k, l, i2, 255).endVertex();
            }
        }
        tessellator.draw();
    }
    
    private float addDistanceToPlayer(final PathPoint p_190066_1_) {
        return (float)(Math.abs(p_190066_1_.xCoord - this.player.posX) + Math.abs(p_190066_1_.yCoord - this.player.posY) + Math.abs(p_190066_1_.zCoord - this.player.posZ));
    }
}
