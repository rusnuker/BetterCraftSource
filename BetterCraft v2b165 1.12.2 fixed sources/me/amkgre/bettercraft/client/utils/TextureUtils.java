// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class TextureUtils
{
    public static void drawTextureAt(final int x, final int y, final ResourceLocation location, final int width, final int height, final boolean reverse) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
        GlStateManager.enableBlend();
        drawModalRectWithCustomSizedTexture((float)x, (float)y, 0.0f, 0.0f, (float)width, (float)height, (float)(reverse ? (-width) : width), (float)height);
        GlStateManager.disableBlend();
    }
    
    public static void drawTextureAt(final double x, final double y, final ResourceLocation location, final double width, final double height, final boolean reverse) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
        GlStateManager.enableBlend();
        drawModalRectWithCustomSizedTexture(x, y, 0.0, 0.0, width, height, reverse ? (-width) : width, height);
        GlStateManager.disableBlend();
    }
    
    public static void drawTextureAt(final float x, final float y, final ResourceLocation location, final float width, final float height, final boolean reverse) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
        GlStateManager.enableBlend();
        drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, reverse ? (-width) : width, height);
        GlStateManager.disableBlend();
    }
    
    public static void drawModalRectWithCustomSizedTexture(final int x, final int y, final int u, final int v, final int width, final int height, final int textureWidth, final int textureHeight) {
        final int f = 1 / textureWidth;
        final int f2 = 1 / textureHeight;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0).tex(u * f, (v + (float)height) * f2).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0).tex((u + (float)width) * f, (v + (float)height) * f2).endVertex();
        worldrenderer.pos(x + width, y, 0.0).tex((u + (float)width) * f, v * f2).endVertex();
        worldrenderer.pos(x, y, 0.0).tex(u * f, v * f2).endVertex();
        tessellator.draw();
    }
    
    public static void drawModalRectWithCustomSizedTexture(final float x, final float y, final float u, final float v, final float width, final float height, final float textureWidth, final float textureHeight) {
        final float f = 1.0f / textureWidth;
        final float f2 = 1.0f / textureHeight;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0).tex(u * f, (v + height) * f2).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0).tex((u + width) * f, (v + height) * f2).endVertex();
        worldrenderer.pos(x + width, y, 0.0).tex((u + width) * f, v * f2).endVertex();
        worldrenderer.pos(x, y, 0.0).tex(u * f, v * f2).endVertex();
        tessellator.draw();
    }
    
    public static void drawModalRectWithCustomSizedTexture(final double x, final double y, final double u, final double v, final double width, final double height, final double textureWidth, final double textureHeight) {
        final double f = 1.0 / textureWidth;
        final double f2 = 1.0 / textureHeight;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0).tex(u * f, (v + (float)height) * f2).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0).tex((u + (float)width) * f, (v + (float)height) * f2).endVertex();
        worldrenderer.pos(x + width, y, 0.0).tex((u + (float)width) * f, v * f2).endVertex();
        worldrenderer.pos(x, y, 0.0).tex(u * f, v * f2).endVertex();
        tessellator.draw();
    }
}
