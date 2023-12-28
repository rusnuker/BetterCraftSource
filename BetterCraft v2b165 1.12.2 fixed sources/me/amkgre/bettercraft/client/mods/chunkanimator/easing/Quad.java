// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.chunkanimator.easing;

public class Quad
{
    public static float easeIn(float t, final float b, final float c, final float d) {
        return c * (t /= d) * t + b;
    }
    
    public static float easeOut(float t, final float b, final float c, final float d) {
        return -c * (t /= d) * (t - 2.0f) + b;
    }
    
    public static float easeInOut(float t, final float b, final float c, final float d) {
        if ((t /= d / 2.0f) < 1.0f) {
            return c / 2.0f * t * t + b;
        }
        return -c / 2.0f * (--t * (t - 2.0f) - 1.0f) + b;
    }
}
