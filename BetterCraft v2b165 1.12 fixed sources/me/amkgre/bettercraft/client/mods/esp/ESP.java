// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.esp;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.AxisAlignedBB;
import me.amkgre.bettercraft.client.utils.RenderUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import me.amkgre.bettercraft.client.utils.ColorUtils;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;

public class ESP
{
    private static final Minecraft mc;
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public static void onRender() {
        for (final Object object : ESP.mc.world.loadedEntityList) {
            if (object instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer)object;
                if (player == ESP.mc.player) {
                    continue;
                }
                ESP.mc.getRenderManager();
                final double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * ESP.mc.timer.field_194147_b - RenderManager.renderPosX;
                ESP.mc.getRenderManager();
                final double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * ESP.mc.timer.field_194147_b - RenderManager.renderPosY;
                ESP.mc.getRenderManager();
                final double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * ESP.mc.timer.field_194147_b - RenderManager.renderPosZ;
                draw2D(player, x, y, z, ColorUtils.rainbowEffect(0L, 1.0f).getRGB(), -16777216);
            }
        }
    }
    
    private static void draw2D(final Entity entity, final double posX, final double posY, final double posZ, final int color, final int backgroundColor) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, posZ);
        GL11.glNormal3f(0.0f, 0.0f, 0.0f);
        ESP.mc.getRenderManager();
        GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(-0.1, -0.1, 0.1);
        GL11.glDisable(2929);
        GL11.glBlendFunc(770, 771);
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        RenderUtils.drawRect(-7.0f, 2.0f, -4.0f, 3.0f, color);
        RenderUtils.drawRect(4.0f, 2.0f, 7.0f, 3.0f, color);
        RenderUtils.drawRect(-7.0f, 0.5f, -6.0f, 3.0f, color);
        RenderUtils.drawRect(6.0f, 0.5f, 7.0f, 3.0f, color);
        RenderUtils.drawRect(-7.0f, 3.0f, -4.0f, 3.3f, backgroundColor);
        RenderUtils.drawRect(4.0f, 3.0f, 7.0f, 3.3f, backgroundColor);
        RenderUtils.drawRect(-7.3f, 0.5f, -7.0f, 3.3f, backgroundColor);
        RenderUtils.drawRect(7.0f, 0.5f, 7.3f, 3.3f, backgroundColor);
        GlStateManager.translate(0.0, 21.0 + -(entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * 12.0, 0.0);
        RenderUtils.drawRect(4.0f, -20.0f, 7.0f, -19.0f, color);
        RenderUtils.drawRect(-7.0f, -20.0f, -4.0f, -19.0f, color);
        RenderUtils.drawRect(6.0f, -20.0f, 7.0f, -17.5f, color);
        RenderUtils.drawRect(-7.0f, -20.0f, -6.0f, -17.5f, color);
        RenderUtils.drawRect(7.0f, -20.0f, 7.3f, -17.5f, backgroundColor);
        RenderUtils.drawRect(-7.3f, -20.0f, -7.0f, -17.5f, backgroundColor);
        RenderUtils.drawRect(4.0f, -20.3f, 7.3f, -20.0f, backgroundColor);
        RenderUtils.drawRect(-7.3f, -20.3f, -4.0f, -20.0f, backgroundColor);
        GL11.glEnable(2929);
        GlStateManager.popMatrix();
    }
    
    private static double getDiff(final double lastI, final double i, final float ticks, final double ownI) {
        return lastI + (i - lastI) * ticks - ownI;
    }
    
    public static void drawESP(float red, float green, float blue, final float alpha, final Entity entity) {
        final double n = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Minecraft.getMinecraft().timer.field_194147_b;
        Minecraft.getMinecraft().getRenderManager();
        final double xPos = n - RenderManager.renderPosX;
        final double n2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * Minecraft.getMinecraft().timer.field_194147_b;
        Minecraft.getMinecraft().getRenderManager();
        final double yPos = n2 - RenderManager.renderPosY;
        final double n3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Minecraft.getMinecraft().timer.field_194147_b;
        Minecraft.getMinecraft().getRenderManager();
        final double zPos = n3 - RenderManager.renderPosZ;
        red -= (float)(entity.hurtResistantTime / 30.0);
        green -= (float)(entity.hurtResistantTime / 30.0);
        blue -= (float)(entity.hurtResistantTime / 30.0);
        drawOutlinedEntityESP(xPos, yPos, zPos, entity.width / 2.0f, entity.height, red, green, blue, alpha, entity);
    }
    
    public static void drawOutlinedEntityESP(final double x, final double y, final double z, final double width, final double height, final float red, final float green, final float blue, final float alpha, final Entity entity) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        if (entity.hurtResistantTime > 0) {
            GL11.glColor4d(1.0, 0.0, 0.0, 1.0);
        }
        else {
            GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        }
        drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
    }
    
    public static void drawOutlinedBoundingBox(final AxisAlignedBB aa) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }
}
