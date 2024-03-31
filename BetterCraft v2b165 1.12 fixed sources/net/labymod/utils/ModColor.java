// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils;

import java.awt.Color;

public enum ModColor
{
    BLACK("BLACK", 0, "BLACK", 0, '0', 0, 0, 0), 
    DARK_BLUE("DARK_BLUE", 1, "DARK_BLUE", 1, '1', 0, 0, 170), 
    DARK_GREEN("DARK_GREEN", 2, "DARK_GREEN", 2, '2', 0, 170, 0), 
    DARK_AQUA("DARK_AQUA", 3, "DARK_AQUA", 3, '3', 0, 170, 170), 
    DARK_RED("DARK_RED", 4, "DARK_RED", 4, '4', 170, 0, 0), 
    DARK_PURPLE("DARK_PURPLE", 5, "DARK_PURPLE", 5, '5', 170, 0, 170), 
    GOLD("GOLD", 6, "GOLD", 6, '6', 255, 170, 0), 
    GRAY("GRAY", 7, "GRAY", 7, '7', 170, 170, 170), 
    DARK_GRAY("DARK_GRAY", 8, "DARK_GRAY", 8, '8', 85, 85, 85), 
    BLUE("BLUE", 9, "BLUE", 9, '9', 85, 85, 255), 
    GREEN("GREEN", 10, "GREEN", 10, 'a', 85, 255, 85), 
    AQUA("AQUA", 11, "AQUA", 11, 'b', 85, 255, 255), 
    RED("RED", 12, "RED", 12, 'c', 255, 85, 85), 
    PINK("PINK", 13, "PINK", 13, 'd', 255, 85, 255), 
    YELLOW("YELLOW", 14, "YELLOW", 14, 'e', 255, 255, 85), 
    WHITE("WHITE", 15, "WHITE", 15, 'f', 255, 255, 255), 
    RESET("RESET", 16, "RESET", 16, 'r'), 
    BOLD("BOLD", 17, "BOLD", 17, 'l'), 
    ITALIC("ITALIC", 18, "ITALIC", 18, 'o'), 
    UNDERLINE("UNDERLINE", 19, "UNDERLINE", 19, 'n'), 
    MAGIC("MAGIC", 20, "MAGIC", 20, 'k'), 
    STRIKETHROUGH("STRIKETHROUGH", 21, "STRIKETHROUGH", 21, 'm');
    
    public static final String[] COLOR_CODES;
    public static final char COLOR_CHAR_PREFIX = '§';
    private char colorChar;
    private Color color;
    
    static {
        COLOR_CODES = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "k", "m", "n", "l", "o", "r" };
    }
    
    private ModColor(final String s2, final int n3, final String s, final int n2, final char colorChar, final int r, final int g, final int b) {
        this.colorChar = colorChar;
        this.color = new Color(r, g, b);
    }
    
    private ModColor(final String s2, final int n3, final String s, final int n2, final char colorChar) {
        this.colorChar = colorChar;
    }
    
    @Override
    public String toString() {
        return String.valueOf(String.valueOf(String.valueOf('§'))) + this.colorChar;
    }
    
    public static String cl(final String colorChar) {
        return String.valueOf(String.valueOf('§')) + colorChar;
    }
    
    public static String cl(final char colorChar) {
        return String.valueOf(new char[] { '§', colorChar });
    }
    
    private static String getColorCharPrefix() {
        return String.valueOf('§');
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public char getColorChar() {
        return this.colorChar;
    }
    
    public static String removeColor(final String string) {
        return string.replaceAll(String.valueOf(String.valueOf(getColorCharPrefix())) + "[a-z0-9]", "");
    }
    
    public static String createColors(final String string) {
        return string.replaceAll("(?i)&([a-z0-9])", "§$1");
    }
    
    public static String booleanToColor(final boolean value) {
        return value ? ModColor.GREEN.toString() : ModColor.RED.toString();
    }
    
    public static int toRGB(final int r, final int g, final int b, final int a) {
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF) << 0;
    }
    
    public static String getCharAsString() {
        return String.valueOf('§');
    }
    
    public static Color changeBrightness(final Color color, final float fraction) {
        float f = color.getRed() + 255.0f * fraction;
        if (f > 255.0f) {
            f = 255.0f;
        }
        if (f < 0.0f) {
            f = 0.0f;
        }
        float f2;
        if ((f2 = color.getGreen() + 255.0f * fraction) > 255.0f) {
            f2 = 255.0f;
        }
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        float f3;
        if ((f3 = color.getBlue() + 255.0f * fraction) > 255.0f) {
            f3 = 255.0f;
        }
        if (f3 < 0.0f) {
            f3 = 0.0f;
        }
        return new Color((int)f, (int)f2, (int)f3);
    }
    
    public static Color getColorByString(final String color) {
        return (color == null) ? null : (color.equals("-1") ? null : new Color(Integer.parseInt(color)));
    }
}
