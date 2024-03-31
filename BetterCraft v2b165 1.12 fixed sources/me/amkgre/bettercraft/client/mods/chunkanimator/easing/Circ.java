// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.chunkanimator.easing;

public class Circ
{
    public static float easeIn(float t, final float b, final float c, final float d) {
        return -c * ((float)Math.sqrt(1.0f - (t /= d) * t) - 1.0f) + b;
    }
    
    public static float easeOut(float t, final float b, final float c, final float d) {
        return c * (float)Math.sqrt(1.0f - (t = t / d - 1.0f) * t) + b;
    }
    
    public static float easeInOut(float t, final float b, final float c, final float d) {
        if ((t /= d / 2.0f) < 1.0f) {
            return -c / 2.0f * ((float)Math.sqrt(1.0f - t * t) - 1.0f) + b;
        }
        return c / 2.0f * ((float)Math.sqrt(1.0f - (t -= 2.0f) * t) + 1.0f) + b;
    }
}
