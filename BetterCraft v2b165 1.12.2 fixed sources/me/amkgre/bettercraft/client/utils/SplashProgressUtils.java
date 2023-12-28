// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class SplashProgressUtils
{
    private static final int MAX = 11;
    private static int PROGRESS;
    private static String CURRENT;
    private static ResourceLocation splash;
    private static UnicodeFontRendererUtils ufr;
    
    static {
        SplashProgressUtils.PROGRESS = 0;
        SplashProgressUtils.CURRENT = "";
    }
    
    public static void update() {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().getLanguageManager() == null) {
            return;
        }
        drawSplash(Minecraft.getMinecraft().getTextureManager());
    }
    
    public static void setProgress(final int givenProgress, final String givenText) {
        SplashProgressUtils.PROGRESS = givenProgress;
        SplashProgressUtils.CURRENT = givenText;
        update();
    }
    
    public static void drawSplash(final TextureManager tm) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        final int scaleFactor = scaledResolution.getScaleFactor();
        final Framebuffer framebuffer = new Framebuffer(ScaledResolution.getScaledWidth() * scaleFactor, ScaledResolution.getScaledHeight() * scaleFactor, true);
        framebuffer.bindFramebuffer(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        if (SplashProgressUtils.splash == null) {
            SplashProgressUtils.splash = new ResourceLocation("textures/gui/title/mojang.png");
        }
        tm.bindTexture(SplashProgressUtils.splash);
        GlStateManager.resetColor();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        Gui.drawScaledCustomSizeModalRect(0, 0, 0.0f, 0.0f, 1920, 1080, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), 1920.0f, 1080.0f);
        drawProgress();
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(ScaledResolution.getScaledWidth() * scaleFactor, ScaledResolution.getScaledHeight() * scaleFactor);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        Minecraft.getMinecraft().updateDisplay();
    }
    
    private static void drawProgress() {
        if (Minecraft.getMinecraft().gameSettings == null || Minecraft.getMinecraft().getTextureManager() == null) {
            return;
        }
        if (SplashProgressUtils.ufr == null) {
            SplashProgressUtils.ufr = UnicodeFontRendererUtils.getFontOnPC("NONE", 20);
        }
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        final double nProgress = SplashProgressUtils.PROGRESS;
        final double calc = nProgress / 11.0 * ScaledResolution.getScaledWidth();
        Gui.drawRect(0, ScaledResolution.getScaledHeight() - 35, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), new Color(0, 0, 0, 50).getRGB());
        GlStateManager.resetColor();
        resetTextureState();
        SplashProgressUtils.ufr.drawString(SplashProgressUtils.CURRENT, 20.0f, (float)(ScaledResolution.getScaledHeight() - 25), -1);
        final String step = String.valueOf(SplashProgressUtils.PROGRESS) + "/" + 11;
        SplashProgressUtils.ufr.drawString(step, ScaledResolution.getScaledWidth() - 20 - SplashProgressUtils.ufr.getWidth(step), (float)(ScaledResolution.getScaledHeight() - 25), -505290241);
        GlStateManager.resetColor();
        resetTextureState();
        Gui.drawRect(0, ScaledResolution.getScaledHeight() - 4, (int)calc, ScaledResolution.getScaledHeight(), new Color(149, 201, 144).getRGB());
        Gui.drawRect(0, ScaledResolution.getScaledHeight() - 2, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), new Color(0, 0, 0, 10).getRGB());
    }
    
    private static void resetTextureState() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName = -1;
    }
}
