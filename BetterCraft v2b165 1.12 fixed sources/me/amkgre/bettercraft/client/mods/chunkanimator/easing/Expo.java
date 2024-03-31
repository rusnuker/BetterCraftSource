// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.chunkanimator.easing;

public class Expo
{
    public static float easeIn(final float t, final float b, final float c, final float d) {
        return (t == 0.0f) ? b : (c * (float)Math.pow(2.0, 10.0f * (t / d - 1.0f)) + b);
    }
    
    public static float easeOut(final float t, final float b, final float c, final float d) {
        return (t == d) ? (b + c) : (c * (-(float)Math.pow(2.0, -10.0f * t / d) + 1.0f) + b);
    }
    
    public static float easeInOut(float t, final float b, final float c, final float d) {
        if (t == 0.0f) {
            return b;
        }
        if (t == d) {
            return b + c;
        }
        if ((t /= d / 2.0f) < 1.0f) {
            return c / 2.0f * (float)Math.pow(2.0, 10.0f * (t - 1.0f)) + b;
        }
        return c / 2.0f * (-(float)Math.pow(2.0, -10.0f * --t) + 2.0f) + b;
    }
}
