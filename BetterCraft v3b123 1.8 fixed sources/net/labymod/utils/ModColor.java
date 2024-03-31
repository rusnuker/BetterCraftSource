// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils;

import java.awt.Color;

public enum ModColor
{
    BLACK("BLACK", 0, '0', 0, 0, 0), 
    DARK_BLUE("DARK_BLUE", 1, '1', 0, 0, 170), 
    DARK_GREEN("DARK_GREEN", 2, '2', 0, 170, 0), 
    DARK_AQUA("DARK_AQUA", 3, '3', 0, 170, 170), 
    DARK_RED("DARK_RED", 4, '4', 170, 0, 0), 
    DARK_PURPLE("DARK_PURPLE", 5, '5', 170, 0, 170), 
    GOLD("GOLD", 6, '6', 255, 170, 0), 
    GRAY("GRAY", 7, '7', 170, 170, 170), 
    DARK_GRAY("DARK_GRAY", 8, '8', 85, 85, 85), 
    BLUE("BLUE", 9, '9', 85, 85, 255), 
    GREEN("GREEN", 10, 'a', 85, 255, 85), 
    AQUA("AQUA", 11, 'b', 85, 255, 255), 
    RED("RED", 12, 'c', 255, 85, 85), 
    PINK("PINK", 13, 'd', 255, 85, 255), 
    YELLOW("YELLOW", 14, 'e', 255, 255, 85), 
    WHITE("WHITE", 15, 'f', 255, 255, 255), 
    RESET("RESET", 16, 'r'), 
    BOLD("BOLD", 17, 'l'), 
    ITALIC("ITALIC", 18, 'o'), 
    UNDERLINE("UNDERLINE", 19, 'n'), 
    MAGIC("MAGIC", 20, 'k'), 
    STRIKETHROUGH("STRIKETHROUGH", 21, 'm');
    
    public static final String[] COLOR_CODES;
    public static final char COLOR_CHAR_PREFIX = '§';
    private final char colorChar;
    private Color color;
    
    static {
        COLOR_CODES = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "k", "m", "n", "l", "o", "r" };
    }
    
    private ModColor(final String s, final int n, final char colorChar, final int r, final int g, final int b) {
        this.colorChar = colorChar;
        this.color = new Color(r, g, b);
    }
    
    private ModColor(final String s, final int n, final char colorChar) {
        this.colorChar = colorChar;
    }
    
    public static String cl(final String colorChar) {
        return String.valueOf('§') + colorChar;
    }
    
    public static String cl(final char colorChar) {
        return String.valueOf(new char[] { '§', colorChar });
    }
    
    private static String getColorCharPrefix() {
        return String.valueOf('§');
    }
    
    public static String removeColor(final String string) {
        return string.replaceAll(String.valueOf(getColorCharPrefix()) + "[a-z0-9]", "");
    }
    
    public static String createColors(final String string) {
        return string.replaceAll("(?i)&([a-z0-9])", "§$1");
    }
    
    public static String booleanToColor(final boolean value) {
        return value ? ModColor.GREEN.toString() : ModColor.RED.toString();
    }
    
    public static int toRGB(final int r, final int g, final int b, final int a) {
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }
    
    public static String getCharAsString() {
        return String.valueOf('§');
    }
    
    public static Color changeBrightness(final Color color, final float fraction) {
        float red = color.getRed() + 255.0f * fraction;
        if (red > 255.0f) {
            red = 255.0f;
        }
        if (red < 0.0f) {
            red = 0.0f;
        }
        float green = color.getGreen() + 255.0f * fraction;
        if (green > 255.0f) {
            green = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        }
        float blue = color.getBlue() + 255.0f * fraction;
        if (blue > 255.0f) {
            blue = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        }
        return new Color((int)red, (int)green, (int)blue);
    }
    
    public static Color getColorByString(final String color) {
        if (color == null) {
            return null;
        }
        if (color.equals("-1")) {
            return null;
        }
        return new Color(Integer.parseInt(color));
    }
    
    @Override
    public String toString() {
        return String.valueOf(String.valueOf('§')) + this.colorChar;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public char getColorChar() {
        return this.colorChar;
    }
}
