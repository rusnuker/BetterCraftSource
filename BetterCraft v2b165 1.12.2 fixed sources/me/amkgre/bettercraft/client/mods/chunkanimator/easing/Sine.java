// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.chunkanimator.easing;

public class Sine
{
    public static float easeIn(final float t, final float b, final float c, final float d) {
        return -c * (float)Math.cos(t / d * 1.5707963267948966) + c + b;
    }
    
    public static float easeOut(final float t, final float b, final float c, final float d) {
        return c * (float)Math.sin(t / d * 1.5707963267948966) + b;
    }
    
    public static float easeInOut(final float t, final float b, final float c, final float d) {
        return -c / 2.0f * ((float)Math.cos(3.141592653589793 * t / d) - 1.0f) + b;
    }
}
