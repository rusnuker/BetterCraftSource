// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import java.util.Random;

public final class MathUtils
{
    public static boolean isInteger(final String s2) {
        try {
            Integer.parseInt(s2);
            return true;
        }
        catch (final NumberFormatException e2) {
            return false;
        }
    }
    
    public static int randInt(final int min, final int max) {
        return new Random().nextInt(max - min + 1) + min;
    }
    
    public static boolean isDouble(final String s2) {
        try {
            Double.parseDouble(s2);
            return true;
        }
        catch (final NumberFormatException e2) {
            return false;
        }
    }
    
    public static int floor(final float value) {
        final int i2 = (int)value;
        return (value < i2) ? (i2 - 1) : i2;
    }
    
    public static int floor(final double value) {
        final int i2 = (int)value;
        return (value < i2) ? (i2 - 1) : i2;
    }
    
    public static int clamp(final int num, final int min, final int max) {
        return (num < min) ? min : ((num > max) ? max : num);
    }
    
    public static float clamp(final float num, final float min, final float max) {
        return (num < min) ? min : ((num > max) ? max : num);
    }
    
    public static double clamp(final double num, final double min, final double max) {
        return (num < min) ? min : ((num > max) ? max : num);
    }
}
