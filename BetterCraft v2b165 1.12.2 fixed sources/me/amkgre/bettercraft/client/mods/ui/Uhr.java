// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.ui;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import java.time.LocalDateTime;
import me.amkgre.bettercraft.client.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import java.awt.Color;

public class Uhr
{
    public static void drawRotated(final int x, final int y, final int w, final int h, final double len, final double percent, final Color col) {
        final int dx = (int)(Math.sin(percent * 6.283185307179586) * len);
        final int dy = (int)(Math.cos(percent * 6.283185307179586) * len);
        drawLine(x + w / 2, y + h / 2, x + (w / 2 + dx), y + (h / 2 - dy), col, 2.0f);
    }
    
    public static void drawText(final String text, int x, int y, final double len, final double percent, final Color col) {
        final int dx = (int)(Math.sin(percent * 6.283185307179586) * len);
        final int dy = (int)(Math.cos(percent * 6.283185307179586) * len);
        x += dx;
        y -= dy;
        final int string_height = 8;
        y -= string_height / 2;
        final int str_len = Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
        x -= str_len / 2;
        Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y, ColorUtils.getColor(col));
    }
    
    public static void render() {
        final LocalDateTime now = LocalDateTime.now();
        final double seconds = now.getSecond();
        final double minutes = now.getMinute();
        final double hours = now.getHour();
        final int width = ScaledResolution.getScaledWidth() - 30;
        Minecraft.getMinecraft();
        final boolean flag = Minecraft.currentScreen instanceof GuiChat;
        final boolean flag2 = Minecraft.getMinecraft().player.getActivePotionEffects().size() != 0;
        final int height = 58 + (flag ? 32 : (flag2 ? 26 : 4));
        final int clockW = 30;
        final int clockH = 30;
        for (int i = 1; i <= 12; ++i) {
            drawText(String.valueOf(i), width - clockW / 2, height - clockH / 2, clockW * 1.2, i / 12.0, Color.WHITE);
        }
        drawFilledCircle(width - clockW / 2, height - clockH / 2, clockW, clockH, 30.0f, new Color(0, 0, 0, 80));
        drawRotated(width - clockW, height - clockH, clockW, clockH, clockW, seconds / 60.0, Color.RED);
        drawRotated(width - clockW, height - clockH, clockW, clockH, clockW * 0.9, minutes / 60.0, Color.WHITE);
        drawRotated(width - clockW, height - clockH, clockW, clockH, clockW * 0.7, hours / 12.0, Color.GRAY);
        drawCircle((float)(width - clockW / 2), (float)(height - clockH / 2), (float)clockW, (float)clockH, false, Color.WHITE);
    }
    
    public static void drawFilledCircle(final int x2, final int y2, final int width, final int height, final float radius, final Color color) {
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
    
    public static void drawCircle(final float x, final float y, final float w, final float h, final boolean fill, final Color color) {
        final double twicePi = 6.283185307179586;
        final int triageAmount = (int)Math.max(4.0, Math.max(w, h) * twicePi / 4.0);
        GL11.glPushMatrix();
        GL11.glPushAttrib(1048575);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glEnable(3042);
        GlStateManager.enableAlpha();
        GL11.glLineWidth(1.0f);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        GL11.glBegin(fill ? 6 : 2);
        for (int i = 0; i <= triageAmount; ++i) {
            GL11.glVertex2d(x + w * Math.cos(i * twicePi / triageAmount), y + h * Math.sin(i * twicePi / triageAmount));
        }
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
    
    public static String insertNulls(final int val, final int min_digits) {
        String s;
        for (s = String.valueOf(val); s.length() < min_digits; s = "0" + s) {}
        return s;
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
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
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
}
