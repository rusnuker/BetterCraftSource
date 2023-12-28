// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.chunkanimator.easing;

public class Bounce
{
    public static float easeIn(final float t, final float b, final float c, final float d) {
        return c - easeOut(d - t, 0.0f, c, d) + b;
    }
    
    public static float easeOut(float t, final float b, final float c, final float d) {
        if ((t /= d) < 0.36363637f) {
            return c * (7.5625f * t * t) + b;
        }
        if (t < 0.72727275f) {
            return c * (7.5625f * (t -= 0.54545456f) * t + 0.75f) + b;
        }
        if (t < 0.9090909090909091) {
            return c * (7.5625f * (t -= 0.8181818f) * t + 0.9375f) + b;
        }
        return c * (7.5625f * (t -= 0.95454544f) * t + 0.984375f) + b;
    }
    
    public static float easeInOut(final float t, final float b, final float c, final float d) {
        if (t < d / 2.0f) {
            return easeIn(t * 2.0f, 0.0f, c, d) * 0.5f + b;
        }
        return easeOut(t * 2.0f - d, 0.0f, c, d) * 0.5f + c * 0.5f + b;
    }
}
