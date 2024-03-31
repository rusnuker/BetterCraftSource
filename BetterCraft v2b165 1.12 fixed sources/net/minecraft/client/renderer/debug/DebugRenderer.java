// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.debug;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;

public class DebugRenderer
{
    public final IDebugRenderer debugRendererPathfinding;
    public final IDebugRenderer debugRendererWater;
    public final IDebugRenderer debugRendererChunkBorder;
    public final IDebugRenderer debugRendererHeightMap;
    public final IDebugRenderer field_191325_e;
    public final IDebugRenderer field_191557_f;
    public final IDebugRenderer field_193852_g;
    private boolean chunkBordersEnabled;
    private boolean pathfindingEnabled;
    private boolean waterEnabled;
    private boolean heightmapEnabled;
    private boolean field_191326_j;
    private boolean field_191558_l;
    private boolean field_193853_n;
    
    public DebugRenderer(final Minecraft clientIn) {
        this.debugRendererPathfinding = new DebugRendererPathfinding(clientIn);
        this.debugRendererWater = new DebugRendererWater(clientIn);
        this.debugRendererChunkBorder = new DebugRendererChunkBorder(clientIn);
        this.debugRendererHeightMap = new DebugRendererHeightMap(clientIn);
        this.field_191325_e = new DebugRendererCollisionBox(clientIn);
        this.field_191557_f = new DebugRendererNeighborsUpdate(clientIn);
        this.field_193852_g = new DebugRendererSolidFace(clientIn);
    }
    
    public boolean shouldRender() {
        return this.chunkBordersEnabled || this.pathfindingEnabled || this.waterEnabled || this.heightmapEnabled || this.field_191326_j || this.field_191558_l || this.field_193853_n;
    }
    
    public boolean toggleDebugScreen() {
        return this.chunkBordersEnabled = !this.chunkBordersEnabled;
    }
    
    public void renderDebug(final float partialTicks, final long finishTimeNano) {
        if (this.pathfindingEnabled) {
            this.debugRendererPathfinding.render(partialTicks, finishTimeNano);
        }
        if (this.chunkBordersEnabled && !Minecraft.getMinecraft().isReducedDebug()) {
            this.debugRendererChunkBorder.render(partialTicks, finishTimeNano);
        }
        if (this.waterEnabled) {
            this.debugRendererWater.render(partialTicks, finishTimeNano);
        }
        if (this.heightmapEnabled) {
            this.debugRendererHeightMap.render(partialTicks, finishTimeNano);
        }
        if (this.field_191326_j) {
            this.field_191325_e.render(partialTicks, finishTimeNano);
        }
        if (this.field_191558_l) {
            this.field_191557_f.render(partialTicks, finishTimeNano);
        }
        if (this.field_193853_n) {
            this.field_193852_g.render(partialTicks, finishTimeNano);
        }
    }
    
    public static void func_191556_a(final String p_191556_0_, final int p_191556_1_, final int p_191556_2_, final int p_191556_3_, final float p_191556_4_, final int p_191556_5_) {
        renderDebugText(p_191556_0_, p_191556_1_ + 0.5, p_191556_2_ + 0.5, p_191556_3_ + 0.5, p_191556_4_, p_191556_5_);
    }
    
    public static void renderDebugText(final String str, final double x, final double y, final double z, final float partialTicks, final int color) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.player != null && minecraft.getRenderManager() != null && minecraft.getRenderManager().options != null) {
            final FontRenderer fontrenderer = minecraft.fontRendererObj;
            final EntityPlayer entityplayer = minecraft.player;
            final double d0 = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * partialTicks;
            final double d2 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * partialTicks;
            final double d3 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * partialTicks;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(x - d0), (float)(y - d2) + 0.07f, (float)(z - d3));
            GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.scale(0.02f, -0.02f, 0.02f);
            final RenderManager rendermanager = minecraft.getRenderManager();
            GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(((rendermanager.options.thirdPersonView == 2) ? 1 : -1) * rendermanager.playerViewX, 1.0f, 0.0f, 0.0f);
            GlStateManager.disableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0, color);
            GlStateManager.enableLighting();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }
    
    public interface IDebugRenderer
    {
        void render(final float p0, final long p1);
    }
}
