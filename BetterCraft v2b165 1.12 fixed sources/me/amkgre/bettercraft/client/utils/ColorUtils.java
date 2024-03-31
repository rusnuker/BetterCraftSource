// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.math.MathHelper;
import java.awt.Color;

public class ColorUtils
{
    public static Color rainbowEffect(final long offset, final float fade) {
        final float hue = (System.nanoTime() + offset) / 1.0E10f % 1.0f;
        final long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
        final Color c = new Color((int)color);
        return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade, c.getAlpha() / 255.0f);
    }
    
    public static Color rainbowEffectHotbar(final long offset, final float fade) {
        final float hue = (System.nanoTime() + offset) / 1.0E10f % 1.0f;
        final long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
        final Color c = new Color((int)color);
        return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade, 0.5f);
    }
    
    public static int toARGB(final int r, final int g, final int b, final int a) {
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }
    
    public static void rainbowGL11() {
        final float x = System.currentTimeMillis() % 15000L / 4000.0f;
        final float red = 0.5f + 0.5f * MathHelper.sin(x * 3.1415927f);
        final float green = 0.5f + 0.5f * MathHelper.sin((x + 1.3333334f) * 3.1415927f);
        final float blue = 0.5f + 0.5f * MathHelper.sin((x + 2.6666667f) * 3.1415927f);
        GL11.glColor4d(red, green, blue, 255.0);
    }
    
    public static Color rainbowColor(final long offset, final int speed, final float fade) {
        final double millis = (System.currentTimeMillis() + offset) % (10000L / speed) / (10000.0f / speed);
        final Color c = Color.getHSBColor((float)millis, 0.8f, 0.8f);
        return new Color(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, fade);
    }
    
    public static Color rainbowColor(final long offset, final float fade) {
        final float huge = (System.nanoTime() + offset) / 1.0E10f % 1.0f;
        final long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(huge, 1.0f, 1.0f)), 16);
        final Color c = new Color((int)color);
        return new Color(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, fade);
    }
    
    public static int getRealStringLength(final String str, int virtualStrLen) {
        for (int i = 0; i < virtualStrLen && i < str.length(); ++i) {
            final char chr = str.charAt(i);
            if (chr == '§') {
                ++i;
                virtualStrLen += 2;
            }
        }
        if (virtualStrLen >= str.length()) {
            virtualStrLen = str.length();
        }
        return virtualStrLen;
    }
    
    public static int getColor(final Color color) {
        return getColor(color.getRed(), color.getGreen(), color.getBlue());
    }
    
    public static int getColor(final int r, final int g, final int b) {
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }
    
    public static long drawRainbowRectBorder(final int left, final int top, final int right, final int bottom, final int size) {
        final int size2 = 1;
        final long topCol = renderRainbowRect(left, top - size2, right, top, 2.0, 10L, 0L, RainbowDirection.RIGHT);
        final long downCol = renderRainbowRect(left - size2, top - size, left, bottom + size2, 2.0, 10L, 0L, RainbowDirection.DOWN);
        renderRainbowRect(right, top - size2, right + size2, bottom + size2, 2.0, 10L, topCol, RainbowDirection.DOWN);
        renderRainbowRect(left, bottom, right, bottom + size2, 2.0, 10L, downCol, RainbowDirection.RIGHT);
        return topCol;
    }
    
    public static long renderRainbowRect(final int left, final int top, final int right, final int bottom, final double time, final long difference, final long delay, final RainbowDirection rainbowDirection) {
        long endDelay = 0L;
        switch (rainbowDirection) {
            case RIGHT: {
                for (int i = 0; i < right - left; ++i) {
                    Gui.drawRect(left + i, top, right, bottom, getRainbow(endDelay = delay + i * -difference, time).getRGB());
                }
                break;
            }
            case LEFT: {
                for (int i = 0; i < right - left; ++i) {
                    Gui.drawRect(left + i, top, right, bottom, getRainbow(endDelay = delay + i * -difference, time).getRGB());
                }
                break;
            }
            case DOWN: {
                for (int i = 0; i < bottom - top; ++i) {
                    Gui.drawRect(left, top + i, right, bottom, getRainbow(endDelay = delay + i * -difference, time).getRGB());
                }
                break;
            }
            case UP: {
                for (int i = 0; i < bottom - top; ++i) {
                    Gui.drawRect(left, top + i, right, bottom, getRainbow(endDelay = delay + i * -difference, time).getRGB());
                }
                break;
            }
        }
        return endDelay;
    }
    
    public static Color getRainbow(final long delay, final double time) {
        double rainbowState = Math.ceil((System.currentTimeMillis() * time + delay) / 20.0);
        rainbowState %= 360.0;
        return Color.getHSBColor((float)(rainbowState / 360.0), 1.0f, 1.0f);
    }
    
    public static int RainbowEffect() {
        return Color.HSBtoRGB(System.currentTimeMillis() % 3000L / 3000.0f, 0.8f, 1.0f);
    }
    
    public static void drawChromaString(final String text, final int x, final int y, final boolean shadow) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        int i = x;
        char[] achar;
        char[] array;
        for (int length = (array = (achar = text.toCharArray())).length, l = 0; l < length; ++l) {
            final char c0 = array[l];
            final long j = System.currentTimeMillis() - (i * 10 - y * 10);
            final int k = Color.HSBtoRGB(j % 2000L / 2000.0f, 0.8f, 0.8f);
            final String s = String.valueOf(c0);
            minecraft.fontRendererObj.drawString(s, (float)i, (float)y, k, shadow);
            i += minecraft.fontRendererObj.getCharWidth(c0);
        }
    }
    
    public enum RainbowDirection
    {
        LEFT("LEFT", 0), 
        UP("UP", 1), 
        RIGHT("RIGHT", 2), 
        DOWN("DOWN", 3);
        
        private RainbowDirection(final String s, final int n) {
        }
    }
}
