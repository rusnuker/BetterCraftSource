// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.resources.DefaultPlayerSkin;
import java.util.UUID;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import me.nzxtercode.bettercraft.client.utils.ColorUtils;
import net.minecraft.client.gui.GuiScreen;
import net.labymod.core.WorldRendererAdapter;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.labymod.main.LabyMod;
import net.minecraft.util.ResourceLocation;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.core.LabyModCore;
import net.minecraft.client.model.ModelHumanoidHead;
import net.labymod.utils.texture.PlayerSkinTextureCache;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class DrawUtils extends Gui
{
    private Minecraft mc;
    public FontRenderer fontRenderer;
    private ScaledResolution scaledResolution;
    private final ModelSkeletonHead humanoidHead;
    private PlayerSkinTextureCache playerSkinTextureCache;
    
    public DrawUtils() {
        this.humanoidHead = new ModelHumanoidHead();
        this.playerSkinTextureCache = new PlayerSkinTextureCache(Minecraft.getMinecraft().getSkinManager(), Minecraft.getMinecraft().getSessionService());
        this.mc = Minecraft.getMinecraft();
        this.scaledResolution = new ScaledResolution(this.mc);
        this.fontRenderer = ((LabyModCore.getCoreAdapter() == null) ? null : LabyModCore.getMinecraft().getFontRenderer());
    }
    
    public void drawBox(final int left, final int top, final int right, final int bottom) {
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.9f);
        Gui.drawRect(left, top, right, bottom, Color.WHITE.getRGB());
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.9f);
        Gui.drawRect(left, top, right, top + 1, Color.WHITE.getRGB());
        Gui.drawRect(left, top, left + 1, bottom, Color.WHITE.getRGB());
        Gui.drawRect(right - 1, top, right, bottom, Color.WHITE.getRGB());
        Gui.drawRect(left, bottom - 1, right, bottom, Color.WHITE.getRGB());
    }
    
    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }
    
    public void setFontRenderer(final FontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
    }
    
    public void bindTexture(final ResourceLocation resourceLocation) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
    }
    
    public PlayerSkinTextureCache getPlayerSkinTextureCache() {
        return this.playerSkinTextureCache;
    }
    
    public void bindTexture(final String resourceLocation) {
        this.bindTexture(new ResourceLocation(resourceLocation));
    }
    
    public double getCustomScaling() {
        double factor;
        for (factor = 1.0 + LabyMod.getSettings().moduleEditorZoom * 0.03; Minecraft.getMinecraft().displayWidth / factor < 320.0; factor -= 0.1) {}
        while (Minecraft.getMinecraft().displayHeight / factor < 240.0) {
            factor -= 0.1;
        }
        return factor;
    }
    
    public int getWidth() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
    }
    
    public int getHeight() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
    }
    
    public void setScaledResolution(final ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }
    
    public ScaledResolution getScaledResolution() {
        return this.scaledResolution;
    }
    
    public void drawString(final String text, final double x, final double y) {
        this.fontRenderer.drawString(text, (float)x, (float)y, 16777215, true);
    }
    
    public void drawStringWithShadow(final String text, final double x, final double y, final int color) {
        LabyModCore.getMinecraft().getFontRenderer().drawStringWithShadow(text, (float)x, (float)y, color);
    }
    
    public void drawRightString(final String text, final double x, final double y) {
        this.drawString(text, x - this.getStringWidth(text), y);
    }
    
    public void drawRightStringWithShadow(final String text, final int x, final int y, final int color) {
        LabyModCore.getMinecraft().getFontRenderer().drawStringWithShadow(text, (float)(x - this.getStringWidth(text)), (float)y, color);
    }
    
    public void drawCenteredString(final String text, final double x, final double y) {
        this.drawString(text, x - this.getStringWidth(text) / 2, y);
    }
    
    public void drawString(final String text, final double x, final double y, final double size) {
        GL11.glPushMatrix();
        GL11.glScaled(size, size, size);
        this.drawString(text, x / size, y / size);
        GL11.glPopMatrix();
    }
    
    public void drawCenteredString(final String text, final double x, final double y, final double size) {
        GL11.glPushMatrix();
        GL11.glScaled(size, size, size);
        this.drawCenteredString(text, x / size, y / size);
        GL11.glPopMatrix();
    }
    
    public void drawRightString(final String text, final double x, final double y, final double size) {
        GL11.glPushMatrix();
        GL11.glScaled(size, size, size);
        this.drawString(text, x / size - this.getStringWidth(text), y / size);
        GL11.glPopMatrix();
    }
    
    public void drawItem(final ItemStack item, final double xPosition, final double yPosition, final String value) {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableCull();
        if (item.hasEffect()) {
            GlStateManager.enableDepth();
            this.renderItemIntoGUI(item, xPosition, yPosition);
            GlStateManager.disableDepth();
        }
        else {
            this.renderItemIntoGUI(item, xPosition, yPosition);
        }
        this.renderItemOverlayIntoGUI(item, xPosition, yPosition, value);
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
    }
    
    public void renderItemIntoGUI(final ItemStack stack, final double x, final double y) {
        LabyModCore.getRenderImplementation().renderItemIntoGUI(stack, x, y);
    }
    
    private void renderItemOverlayIntoGUI(final ItemStack stack, final double xPosition, final double yPosition, final String text) {
        LabyModCore.getRenderImplementation().renderItemOverlayIntoGUI(stack, xPosition, yPosition, text);
    }
    
    public int getStringWidth(final String text) {
        return this.fontRenderer.getStringWidth(text);
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
        final WorldRendererAdapter worldrenderer = LabyModCore.getWorldRenderer();
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
    
    public boolean drawRect(final int mouseX, final int mouseY, final double left, final double top, final double right, final double bottom, final int color, final int hoverColor) {
        final boolean hover = mouseX > left && mouseX < right && mouseY > top && mouseY < bottom;
        drawRect(left, top, right, bottom, hover ? hoverColor : color);
        return hover;
    }
    
    public boolean drawRect(final int mouseX, final int mouseY, final String displayString, final double left, final double top, final double right, final double bottom, final int color, final int hoverColor) {
        final boolean hover = mouseX > left && mouseX < right && mouseY > top && mouseY < bottom;
        drawRect(left, top, right, bottom, hover ? hoverColor : color);
        this.drawCenteredString(displayString, left + (right - left) / 2.0, top + (bottom - top) / 2.0 - 4.0);
        return hover;
    }
    
    public void drawRectangle(final int left, final int top, final int right, final int bottom, final int color) {
        Gui.drawRect(left, top, right, bottom, color);
    }
    
    public static void startScissor(final float startX, final float startY, final float endX, final float endY) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        final float width = endX - startX;
        final float height = endY - startY;
        assert Minecraft.getMinecraft().currentScreen != null;
        final GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
        final float bottomY = GuiScreen.height - endY;
        final float factor = (float)scaledResolution.getScaleFactor();
        final float scissorX = startX * factor;
        final float scissorY = bottomY * factor;
        final float scissorWidth = width * factor;
        final float scissorHeight = height * factor;
        GL11.glScissor((int)scissorX, (int)scissorY, (int)scissorWidth, (int)scissorHeight);
        GL11.glEnable(3089);
    }
    
    public static void stopScissor() {
        GL11.glDisable(3089);
    }
    
    public void drawGradientShadowTop(final double y, final double left, final double right) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRendererAdapter worldrenderer = LabyModCore.getWorldRenderer();
        GlStateManager.disableDepth();
        final int i1 = 1;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        GlStateManager.disableTexture2D();
        this.mc.getTextureManager().bindTexture(LabyModCore.getRenderImplementation().getOptionsBackground());
        final int rainbow = ColorUtils.rainbowEffect();
        final float r = (rainbow >> 16 & 0xFF) / 255.0f;
        final float g = (rainbow >> 8 & 0xFF) / 255.0f;
        final float b = (rainbow & 0xFF) / 255.0f;
        final float a = (rainbow >> 24 & 0xFF) / 255.0f;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(left, y + i1, 0.0).tex(0.0, 1.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(right, y + i1, 0.0).tex(1.0, 1.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(right, y, 0.0).tex(1.0, 0.0).color(r, g, b, a).endVertex();
        worldrenderer.pos(left, y, 0.0).tex(0.0, 0.0).color(r, g, b, a).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
    }
    
    public void drawGradientShadowBottom(final double y, final double left, final double right) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRendererAdapter worldrenderer = LabyModCore.getWorldRenderer();
        GlStateManager.disableDepth();
        final int i1 = 1;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        GlStateManager.disableTexture2D();
        this.mc.getTextureManager().bindTexture(LabyModCore.getRenderImplementation().getOptionsBackground());
        final int rainbow = ColorUtils.rainbowEffect();
        final float r = (rainbow >> 16 & 0xFF) / 255.0f;
        final float g = (rainbow >> 8 & 0xFF) / 255.0f;
        final float b = (rainbow & 0xFF) / 255.0f;
        final float a = (rainbow >> 24 & 0xFF) / 255.0f;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(left, y, 0.0).tex(0.0, 1.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(right, y, 0.0).tex(1.0, 1.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(right, y - i1, 0.0).tex(1.0, 0.0).color(r, g, b, a).endVertex();
        worldrenderer.pos(left, y - i1, 0.0).tex(0.0, 0.0).color(r, g, b, a).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
    }
    
    public void drawGradientShadowLeft(final double x, final double top, final double bottom) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRendererAdapter worldrenderer = LabyModCore.getWorldRenderer();
        final int i1 = 4;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        GlStateManager.enableTexture2D();
        this.mc.getTextureManager().bindTexture(LabyModCore.getRenderImplementation().getOptionsBackground());
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(x + i1, bottom, 0.0).tex(1.0, 0.0).color(0, 0, 0, 0).endVertex();
        worldrenderer.pos(x + i1, top, 0.0).tex(1.0, 1.0).color(0, 0, 0, 0).endVertex();
        worldrenderer.pos(x, top, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(x, bottom, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
    }
    
    public void drawGradientShadowRight(final double x, final double top, final double bottom) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRendererAdapter worldrenderer = LabyModCore.getWorldRenderer();
        final int i1 = 4;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        GlStateManager.enableTexture2D();
        this.mc.getTextureManager().bindTexture(LabyModCore.getRenderImplementation().getOptionsBackground());
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(x, bottom, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(x, top, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(x - i1, top, 0.0).tex(0.0, 1.0).color(0, 0, 0, 0).endVertex();
        worldrenderer.pos(x - i1, bottom, 0.0).tex(0.0, 0.0).color(0, 0, 0, 0).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
    }
    
    public void drawIngameBackground() {
        this.drawGradientRect(0, 0, this.getWidth(), this.getHeight(), -1072689136, -804253680);
    }
    
    public void drawAutoDimmedBackground(final double d) {
        if (LabyMod.getInstance().isInGame()) {
            this.drawIngameBackground();
        }
        else {
            this.drawDimmedBackground((int)d);
        }
    }
    
    public void drawAutoDimmedBackground(final int left, final int top, final int right, final int bottom) {
        if (LabyMod.getInstance().isInGame()) {
            this.drawGradientRect(left, top, right, bottom, -1072689136, -804253680);
        }
        else {
            this.drawDimmedOverlayBackground(left, top, right, bottom);
        }
    }
    
    public void drawBackground(final int tint) {
        this.drawBackground(tint, 0.0, 64);
    }
    
    public void drawDimmedBackground(final int scroll) {
        this.drawBackground(0, -scroll, 32);
    }
    
    public void drawBackground(final int tint, final double scrolling, final int brightness) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRendererAdapter worldrenderer = LabyModCore.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(LabyModCore.getRenderImplementation().getOptionsBackground());
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0, this.getHeight(), 0.0).tex(0.0, (this.getHeight() + scrolling) / 32.0 + tint).color(brightness, brightness, brightness, 255).endVertex();
        worldrenderer.pos(this.getWidth(), this.getHeight(), 0.0).tex(this.getWidth() / 32.0f, (this.getHeight() + scrolling) / 32.0 + tint).color(brightness, brightness, brightness, 255).endVertex();
        worldrenderer.pos(this.getWidth(), 0.0, 0.0).tex(this.getWidth() / 32.0f, tint + scrolling / 32.0).color(brightness, brightness, brightness, 255).endVertex();
        worldrenderer.pos(0.0, 0.0, 0.0).tex(0.0, tint + scrolling / 32.0).color(brightness, brightness, brightness, 255).endVertex();
        tessellator.draw();
    }
    
    public void drawOverlayBackground(final int startY, final int endY) {
        final int endAlpha = 255;
        final int startAlpha = 255;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRendererAdapter worldrenderer = LabyModCore.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(LabyModCore.getRenderImplementation().getOptionsBackground());
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0, endY, 0.0).tex(0.0, endY / 32.0f).color(64, 64, 64, endAlpha).endVertex();
        worldrenderer.pos(0 + this.getWidth(), endY, 0.0).tex(this.getWidth() / 32.0f, endY / 32.0f).color(64, 64, 64, endAlpha).endVertex();
        worldrenderer.pos(0 + this.getWidth(), startY, 0.0).tex(this.getWidth() / 32.0f, startY / 32.0f).color(64, 64, 64, startAlpha).endVertex();
        worldrenderer.pos(0.0, startY, 0.0).tex(0.0, startY / 32.0f).color(64, 64, 64, startAlpha).endVertex();
        tessellator.draw();
    }
    
    public void drawDimmedOverlayBackground(final int left, final int top, final int right, final int bottom) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRendererAdapter worldrenderer = LabyModCore.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(LabyModCore.getRenderImplementation().getOptionsBackground());
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final float f = 32.0f;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(left, bottom, 0.0).tex(left / f, bottom / f).color(32, 32, 32, 255).endVertex();
        worldrenderer.pos(right, bottom, 0.0).tex(right / f, bottom / f).color(32, 32, 32, 255).endVertex();
        worldrenderer.pos(right, top, 0.0).tex(right / f, top / f).color(32, 32, 32, 255).endVertex();
        worldrenderer.pos(left, top, 0.0).tex(left / f, top / f).color(32, 32, 32, 255).endVertex();
        tessellator.draw();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        GlStateManager.disableDepth();
    }
    
    public void drawOverlayBackground(final int startX, final int startY, final int width, final int endY) {
        this.drawOverlayBackground(startX, startY, width, endY, 64);
    }
    
    public void drawOverlayBackground(final int startX, final int startY, final int width, final int endY, final int brightness) {
        final int endAlpha = 255;
        final int startAlpha = 255;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRendererAdapter worldrenderer = LabyModCore.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(LabyModCore.getRenderImplementation().getOptionsBackground());
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(startX, endY, 0.0).tex(0.0, endY / 32.0f).color(brightness, brightness, brightness, endAlpha).endVertex();
        worldrenderer.pos(startX + width, endY, 0.0).tex(width / 32.0f, endY / 32.0f).color(brightness, brightness, brightness, endAlpha).endVertex();
        worldrenderer.pos(startX + width, startY, 0.0).tex(width / 32.0f, startY / 32.0f).color(brightness, brightness, brightness, startAlpha).endVertex();
        worldrenderer.pos(startX, startY, 0.0).tex(0.0, startY / 32.0f).color(brightness, brightness, brightness, startAlpha).endVertex();
        tessellator.draw();
    }
    
    public void drawTexturedModalRect(final double x, final double y, final double textureX, final double textureY, final double width, final double height) {
        this.drawTexturedModalRect((int)x, (int)y, (int)textureX, (int)textureY, (int)width, (int)height);
    }
    
    public void drawTexturedModalRect(final double left, final double top, final double right, final double bottom) {
        final double textureX = 0.0;
        final double textureY = 0.0;
        final double x = left;
        final double y = top;
        final double width = right - left;
        final double height = bottom - top;
        final float f = 0.00390625f;
        final float f2 = 0.00390625f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRendererAdapter worldrenderer = LabyModCore.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x + 0.0, y + height, this.zLevel).tex((float)(textureX + 0.0) * f, (float)(textureY + height) * f2).endVertex();
        worldrenderer.pos(x + width, y + height, this.zLevel).tex((float)(textureX + width) * f, (float)(textureY + height) * f2).endVertex();
        worldrenderer.pos(x + width, y + 0.0, this.zLevel).tex((float)(textureX + width) * f, (float)(textureY + 0.0) * f2).endVertex();
        worldrenderer.pos(x + 0.0, y + 0.0, this.zLevel).tex((float)(textureX + 0.0) * f, (float)(textureY + 0.0) * f2).endVertex();
        tessellator.draw();
    }
    
    public void drawTexture(final double x, final double y, final double imageWidth, final double imageHeight, final double maxWidth, final double maxHeight, final float alpha) {
        GL11.glPushMatrix();
        final double sizeWidth = maxWidth / imageWidth;
        final double sizeHeight = maxHeight / imageHeight;
        GL11.glScaled(sizeWidth, sizeHeight, 0.0);
        if (alpha <= 1.0f) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
        }
        this.drawTexturedModalRect(x / sizeWidth, y / sizeHeight, x / sizeWidth + imageWidth, y / sizeHeight + imageHeight);
        if (alpha <= 1.0f) {
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
        }
        GL11.glPopMatrix();
    }
    
    public void drawRawTexture(final double x, final double y, final double imageWidth, final double imageHeight, final double maxWidth, final double maxHeight) {
        GL11.glPushMatrix();
        final double sizeWidth = maxWidth / imageWidth;
        final double sizeHeight = maxHeight / imageHeight;
        GL11.glScaled(sizeWidth, sizeHeight, 0.0);
        this.drawTexturedModalRect(x / sizeWidth, y / sizeHeight, x / sizeWidth + imageWidth, y / sizeHeight + imageHeight);
        GL11.glPopMatrix();
    }
    
    public void drawTexture(final double x, final double y, final double imageWidth, final double imageHeight, final double maxWidth, final double maxHeight) {
        this.drawTexture(x, y, imageWidth, imageHeight, maxWidth, maxHeight, 1.0f);
    }
    
    public void drawTexture(final double x, final double y, final double texturePosX, final double texturePosY, final double imageWidth, final double imageHeight, final double maxWidth, final double maxHeight, final float alpha) {
        GL11.glPushMatrix();
        final double sizeWidth = maxWidth / imageWidth;
        final double sizeHeight = maxHeight / imageHeight;
        GL11.glScaled(sizeWidth, sizeHeight, 0.0);
        if (alpha <= 1.0f) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
        }
        this.drawUVTexture(x / sizeWidth, y / sizeHeight, texturePosX, texturePosY, x / sizeWidth + imageWidth - x / sizeWidth, y / sizeHeight + imageHeight - y / sizeHeight);
        if (alpha <= 1.0f) {
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
        }
        GL11.glPopMatrix();
    }
    
    public void drawTexture(final double x, final double y, final double texturePosX, final double texturePosY, final double imageWidth, final double imageHeight, final double maxWidth, final double maxHeight) {
        this.drawTexture(x, y, texturePosX, texturePosY, imageWidth, imageHeight, maxWidth, maxHeight, 1.0f);
    }
    
    private void drawUVTexture(final double x, final double y, final double textureX, final double textureY, final double width, final double height) {
        final float f = 0.00390625f;
        final float f2 = 0.00390625f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRendererAdapter worldrenderer = LabyModCore.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x + 0.0, y + height, this.zLevel).tex((float)(textureX + 0.0) * f, (float)(textureY + height) * f2).endVertex();
        worldrenderer.pos(x + width, y + height, this.zLevel).tex((float)(textureX + width) * f, (float)(textureY + height) * f2).endVertex();
        worldrenderer.pos(x + width, y + 0.0, this.zLevel).tex((float)(textureX + width) * f, (float)(textureY + 0.0) * f2).endVertex();
        worldrenderer.pos(x + 0.0, y + 0.0, this.zLevel).tex((float)(textureX + 0.0) * f, (float)(textureY + 0.0) * f2).endVertex();
        tessellator.draw();
    }
    
    public static void drawEntityOnScreen(final int x, final int y, final int size, final float mouseX, final float mouseY, final int rotationX, final int rotationY, final int rotationZ, final EntityLivingBase entity) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, 100.0f);
        GlStateManager.scale(-size - 30.0f, size + 30.0f, (float)size);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        final float var5 = entity.prevRenderYawOffset;
        final float var6 = entity.renderYawOffset;
        final float var7 = entity.rotationYaw;
        final float var8 = entity.rotationPitch;
        final float var9 = entity.prevRotationYawHead;
        final float var10 = entity.rotationYawHead;
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f + rotationX, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((float)rotationY, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate((float)rotationZ, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(-(float)Math.atan(mouseY / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        entity.renderYawOffset = (float)Math.atan(mouseX / 40.0f) * 20.0f;
        entity.rotationYaw = (float)Math.atan(mouseX / 40.0f) * 40.0f;
        entity.rotationPitch = -(float)Math.atan(mouseY / 40.0f) * 20.0f;
        entity.rotationYawHead = entity.rotationYaw;
        entity.prevRotationYawHead = entity.rotationYaw;
        entity.prevRenderYawOffset = entity.renderYawOffset;
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager var11 = Minecraft.getMinecraft().getRenderManager();
        var11.setPlayerViewY(180.0f);
        var11.setRenderShadow(false);
        LabyModCore.getRenderImplementation().renderEntity(var11, entity, 0.0, (rotationY == 0) ? 0.0 : -1.0, 0.0, 0.0f, 1.0f, false);
        var11.setRenderShadow(true);
        entity.renderYawOffset = var6;
        entity.rotationYaw = var7;
        entity.rotationPitch = var8;
        entity.prevRenderYawOffset = var5;
        entity.prevRotationYawHead = var9;
        entity.rotationYawHead = var10;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    
    public String trimStringToWidth(final String text, final int width) {
        if (text == null) {
            return text;
        }
        return this.fontRenderer.trimStringToWidth(text, width, false);
    }
    
    public List<String> listFormattedStringToWidth(final String str, int wrapWidth) {
        if (wrapWidth < 10) {
            wrapWidth = 10;
        }
        return this.fontRenderer.listFormattedStringToWidth(str, wrapWidth);
    }
    
    public List<String> listFormattedStringToWidth(final String str, final int wrapWidth, final int maxLines) {
        final List<String> list = this.listFormattedStringToWidth(str, wrapWidth);
        if (list.size() < maxLines) {
            return list;
        }
        final ArrayList<String> output = new ArrayList<String>();
        int count = 0;
        for (final String line : list) {
            output.add(line);
            if (++count < maxLines) {
                continue;
            }
            break;
        }
        return output;
    }
    
    public void drawHoveringText(final int x, final int y, final String... textLines) {
        if (textLines.length != 0) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int i = 0;
            for (final String s : textLines) {
                final int j = this.fontRenderer.getStringWidth(s);
                if (j > i) {
                    i = j;
                }
            }
            int l1 = x + 7;
            int i2 = y - 12;
            int k = 8;
            if (textLines.length > 1) {
                k += 2 + (textLines.length - 1) * 10;
            }
            if (i2 < 5) {
                i2 = 5;
            }
            if (l1 + i > this.getWidth()) {
                l1 -= 12 + i;
            }
            if (i2 + k + 6 > this.getHeight()) {
                i2 = this.getHeight() - k - 6;
            }
            this.zLevel = 300.0f;
            final int m = -267386864;
            this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, m, m);
            this.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, m, m);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, m, m);
            this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, m, m);
            this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, m, m);
            final int i3 = 1347420415;
            final int j2 = (i3 & 0xFEFEFE) >> 1 | (i3 & 0xFF000000);
            this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i3, j2);
            this.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i3, j2);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i3, i3);
            this.drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j2, j2);
            for (int k2 = 0; k2 < textLines.length; ++k2) {
                final String s2 = textLines[k2];
                this.fontRenderer.drawStringWithShadow(s2, (float)l1, (float)i2, -1);
                if (k2 == 0) {
                    i2 += 2;
                }
                i2 += 10;
            }
            this.zLevel = 0.0f;
        }
    }
    
    public void drawHoveringTextBoxField(final int x, final int y, final int width, final int height) {
        this.zLevel = 300.0f;
        final int color1 = -267386864;
        this.drawGradientRect(x - 3, y - 4, x + width + 3, y - 3, color1, color1);
        this.drawGradientRect(x - 3, y + height + 3, x + width + 3, y + height + 4, color1, color1);
        this.drawGradientRect(x - 3, y - 3, x + width + 3, y + height + 3, color1, color1);
        this.drawGradientRect(x - 4, y - 3, x - 3, y + height + 3, color1, color1);
        this.drawGradientRect(x + width + 3, y - 3, x + width + 4, y + height + 3, color1, color1);
        final int color2 = 1347420415;
        final int color3 = (color2 & 0xFEFEFE) >> 1 | (color2 & 0xFF000000);
        this.drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, color2, color3);
        this.drawGradientRect(x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, color2, color3);
        this.drawGradientRect(x - 3, y - 3, x + width + 3, y - 3 + 1, color2, color2);
        this.drawGradientRect(x - 3, y + height + 2, x + width + 3, y + height + 3, color3, color3);
        this.zLevel = 0.0f;
        GlStateManager.disableDepth();
    }
    
    public void drawPlayerHead(ResourceLocation resourceLocation, final int x, final int y, final int size) {
        if (resourceLocation == null) {
            resourceLocation = DefaultPlayerSkin.getDefaultSkin(UUID.randomUUID());
        }
        GlStateManager.enableAlpha();
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        Gui.drawScaledCustomSizeModalRect(x, y, 8.0f, 8.0f, 8, 8, size, size, 64.0f, 64.0f);
        Gui.drawScaledCustomSizeModalRect(x, y, 40.0f, 8.0f, 8, 8, size, size, 64.0f, 64.0f);
    }
    
    public void drawPlayerHead(final GameProfile gameProfile, final int x, final int y, final int size) {
        final ResourceLocation resourceLocation = this.playerSkinTextureCache.getSkinTexture(gameProfile);
        this.drawPlayerHead(resourceLocation, x, y, size);
    }
    
    public void drawPlayerHead(final String username, final int x, final int y, final int size) {
        final ResourceLocation resourceLocation = this.playerSkinTextureCache.getSkinTexture(username);
        this.drawPlayerHead(resourceLocation, x, y, size);
    }
    
    public void drawPlayerHead(final UUID uuid, final int x, final int y, final int size) {
        final ResourceLocation resourceLocation = this.playerSkinTextureCache.getSkinTexture(uuid);
        this.drawPlayerHead(resourceLocation, x, y, size);
    }
    
    @Deprecated
    public void drawMinotarHead(final GameProfile gameProfile, final int x, final int y, final int size) {
        this.drawPlayerHead(gameProfile, x, y, size);
    }
    
    public void drawRectBorder(final double left, final double top, final double right, final double bottom, final int color, final double thickness) {
        drawRect(left + thickness, top, right - thickness, top + thickness, color);
        drawRect(right - thickness, top, right, bottom, color);
        drawRect(left + thickness, bottom - thickness, right - thickness, bottom, color);
        drawRect(left, top, left + thickness, bottom, color);
    }
    
    public void drawImageUrl(final String url, final double x, final double y, final double imageWidth, final double imageHeight, final double maxWidth, final double maxHeight) {
        this.drawDynamicImageUrl(url, url, x, y, imageWidth, imageHeight, maxWidth, maxHeight);
    }
    
    public void drawDynamicImageUrl(final String identifier, final String url, final double x, final double y, final double imageWidth, final double imageHeight, final double maxWidth, final double maxHeight) {
        final ResourceLocation resourceLocation = LabyMod.getInstance().getDynamicTextureManager().getTexture(identifier, url);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        LabyMod.getInstance().getDrawUtils().drawTexture(x, y, imageWidth, imageHeight, maxWidth, maxHeight);
    }
    
    public void renderSkull(final GameProfile gameProfile) {
        final ModelSkeletonHead modelbase = this.humanoidHead;
        final ResourceLocation resourceSkin = this.playerSkinTextureCache.getSkinTexture(gameProfile);
        if (resourceSkin != null) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(resourceSkin);
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableAlpha();
            GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            GlStateManager.translate(0.0f, 0.2f, 0.0f);
            modelbase.render(null, 0.0f, 0.0f, 0.0f, 180.0f, 0.0f, 0.0625f);
            GlStateManager.popMatrix();
        }
    }
}
