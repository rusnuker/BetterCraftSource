// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.net.URL;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.Map;
import net.minecraft.client.Minecraft;

public class RenderUtils
{
    public static Minecraft mc;
    private static final Map<Integer, Boolean> glCapMap;
    private static final AxisAlignedBB DEFAULT_AABB;
    public static DynamicTexture dynamicTexture;
    public static BufferedImage bufferedImage;
    
    static {
        RenderUtils.mc = Minecraft.getMinecraft();
        glCapMap = new HashMap<Integer, Boolean>();
        DEFAULT_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
    }
    
    public static void downloadSkin(final String username) {
        RenderUtils.dynamicTexture = null;
        RenderUtils.bufferedImage = null;
        try {
            RenderUtils.bufferedImage = getSkinBuffer("https://minotar.net/helm/" + username + "/55.png");
            RenderUtils.dynamicTexture = new DynamicTexture(RenderUtils.bufferedImage);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static BufferedImage getSkinBuffer(final String web) {
        try {
            return ImageIO.read(new URL(web).openStream());
        }
        catch (final Exception e) {
            return null;
        }
    }
    
    public static void drawLine(final double fromX, final double fromY, final double toX, final double toY, final Color color, final float lineWidth) {
        GL11.glPushMatrix();
        GL11.glPushAttrib(1048575);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glEnable(3042);
        GL11.glLineWidth(lineWidth);
        GL11.glBlendFunc(770, 771);
        final Color c = color;
        GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, c.getAlpha() / 255.0f);
        GL11.glBegin(2);
        GL11.glVertex2d(fromX, fromY);
        GL11.glVertex2d(toX, toY);
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
    
    public static float[] RGBA(int color) {
        if ((color & 0xFC000000) == 0x0) {
            color |= 0xFF000000;
        }
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        return new float[] { red, green, blue, alpha };
    }
    
    public static void drawMovedBackground(final int mouseX, final int mouseY, final String backgroundLocation, final boolean move) {
        final ScaledResolution si = new ScaledResolution(RenderUtils.mc);
        if (move) {
            RenderUtils.mc.getTextureManager().bindTexture(new ResourceLocation(backgroundLocation));
            Gui.drawModalRectWithCustomSizedTexture(0 - mouseX / 40, 0 - mouseY / 40, 0.0f, 0.0f, ScaledResolution.getScaledWidth() + 30, ScaledResolution.getScaledHeight() + 30, (float)(ScaledResolution.getScaledWidth() + 30), (float)(ScaledResolution.getScaledHeight() + 30));
        }
        else {
            RenderUtils.mc.getTextureManager().bindTexture(new ResourceLocation(backgroundLocation));
            Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), (float)ScaledResolution.getScaledWidth(), (float)ScaledResolution.getScaledHeight());
        }
    }
    
    public static void drawMovedLogo(final int mouseX, final int mouseY, final String logoLocation, final boolean move) {
        final ScaledResolution si = new ScaledResolution(RenderUtils.mc);
        if (move) {
            RenderUtils.mc.getTextureManager().bindTexture(new ResourceLocation(logoLocation));
            Gui.drawModalRectWithCustomSizedTexture(GuiScreen.width / 2 - 90 - mouseX / 40, GuiScreen.height / 2 - 110 - mouseY / 40, 0.0f, 0.0f, 190, 190, 190.0f, 190.0f);
        }
        else {
            RenderUtils.mc.getTextureManager().bindTexture(new ResourceLocation(logoLocation));
            Gui.drawModalRectWithCustomSizedTexture(GuiScreen.width / 2 - 90, GuiScreen.height / 2 - 80, 1.0f, 1.0f, 160, 160, 160.0f, 160.0f);
        }
    }
    
    public static void drawImage(final ResourceLocation image, final int x, final int y, final int width, final int height) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float)width, (float)height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
    
    public static void drawImage(final ResourceLocation image, final int x, final int y, final int width, final int height, final Color color) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor3f(color.getRed() / 255.0f, color.getBlue() / 255.0f, color.getRed() / 255.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float)width, (float)height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
    
    public void drawCircle(final double x, final double y, final float radius, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glLineWidth(1.0f);
        GL11.glBegin(9);
        for (int i = 0; i <= 90; ++i) {
            GL11.glVertex2d(x + Math.sin(i * 3.141592653589793 / 45.0) * radius, y + Math.cos(i * 3.141592653589793 / 45.0) * radius);
        }
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public void connectPoints(final int xOne, final int yOne, final int xTwo, final int yTwo, final int dif) {
        GL11.glPushMatrix();
        GL11.glEnable(2848);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, this.getLineIntensity(dif));
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(0.5f);
        GL11.glBegin(1);
        GL11.glVertex2i(xOne, yOne);
        GL11.glVertex2i(xTwo, yTwo);
        GL11.glEnd();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }
    
    private float getLineIntensity(final int dif) {
        if (dif < 59) {
            return 0.8f;
        }
        return (9 - (dif - 59) / 2) / 10.0f;
    }
    
    public int getRainbow(final int s, final int o) {
        float f = (float)((System.currentTimeMillis() + o) % s);
        f /= s;
        return Color.getHSBColor(f, 1.0f, 1.0f).hashCode();
    }
    
    public static void drawBorderedRect(final double x, final double y, final double x2, final double y2, final int borderedColor, final int color) {
        drawRect(x + 1.0, y + 1.0, x2 - 1.0, y2 - 1.0, color);
        drawRect(x, y + 1.0, x2, y, borderedColor);
        drawRect(x2 - 1.0, y, x2, y2, borderedColor);
        drawRect(x, y2, x2, y2 - 1.0, borderedColor);
        drawRect(x, y, x + 1.0, y2, borderedColor);
    }
    
    public static void drawRect(int left, int top, int right, int bottom, final int color) {
        if (left < right) {
            final int i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final int j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder worldrenderer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f4, f5, f6, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        worldrenderer.pos(right, top, 0.0).endVertex();
        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawRect(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final double j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder worldrenderer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f4, f5, f6, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        worldrenderer.pos(right, top, 0.0).endVertex();
        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawRect(float left, float top, float right, float bottom, final int color) {
        if (left < right) {
            final float i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final float j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder worldrenderer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f4, f5, f6, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        worldrenderer.pos(right, top, 0.0).endVertex();
        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawCircle(final int x2, final int y2, final float radius, final Color color) {
        final int sections = 50;
        final double dAngle = 6.283185307179586 / sections;
        GL11.glPushAttrib(8192);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glBegin(6);
        for (int i = 0; i < sections; ++i) {
            final float x3 = (float)(radius * Math.sin(i * dAngle));
            final float y3 = (float)(radius * Math.cos(i * dAngle));
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
            GL11.glVertex2f(x3 + x2, y2 + y3);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnd();
        GL11.glPopAttrib();
    }
    
    public static void drawRoundedRect(final int left, final int top, final int right, final int bottom, final float radius, final Color color) {
        Gui.drawRect(left + (int)radius, top, right - (int)radius, bottom, color.hashCode());
        Gui.drawRect(left, top + (int)radius, right, bottom - (int)radius, color.hashCode());
        drawCircle(left + (int)radius, top + (int)radius, radius, color);
        drawCircle(left + (int)radius, bottom - (int)radius, radius, color);
        drawCircle(right - (int)radius, top + (int)radius, radius, color);
        drawCircle(right - (int)radius, bottom - (int)radius, radius, color);
    }
    
    public static long drawLine(final int fromX, final int fromY, final int toX, final int toY, final int steps, final long startOffset, final int color) {
        double count = 0.0;
        final double distX = toX - fromX;
        final double distY = toY - fromY;
        final double dist = Math.sqrt(distX * distX + distY * distY);
        for (int i = 0; i < dist; i += steps) {
            ++count;
        }
        final double length = dist / count;
        long offset = 0L;
        int i2 = 0;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float blue = (color >> 8 & 0xFF) / 255.0f;
        final float green = (color & 0xFF) / 255.0f;
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        while (i2 < count) {
            offset = startOffset + i2 * 50000000;
            GL11.glColor4d(red, blue, green, alpha);
            final double x = fromX + i2 * (distX / count);
            final double y = fromY + i2 * (distY / count);
            final double x2 = fromX + (i2 + 1) * (distX / count);
            final double y2 = fromY + (i2 + 1) * (distY / count);
            GL11.glLineWidth(3.0f);
            GL11.glDisable(2884);
            GL11.glDisable(3553);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glBegin(1);
            GL11.glVertex2d(x, y);
            GL11.glVertex2d(x2, y2);
            GL11.glEnd();
            GL11.glEnable(3553);
            ++i2;
        }
        return offset;
    }
    
    public static void drawTextureAt(final int x, final int y, final ResourceLocation location, final int gr\u00f6\u00dfe) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, gr\u00f6\u00dfe, gr\u00f6\u00dfe, (float)gr\u00f6\u00dfe, (float)gr\u00f6\u00dfe);
        GlStateManager.disableBlend();
    }
    
    public static void drawTextureAt(final int x, final int y, final ResourceLocation location, final int width, final int height) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float)width, (float)height);
        GlStateManager.disableBlend();
    }
}
