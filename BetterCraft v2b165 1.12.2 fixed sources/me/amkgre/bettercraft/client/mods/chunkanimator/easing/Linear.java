// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.chunkanimator.easing;

public class Linear
{
    public static float easeNone(final float t, final float b, final float c, final float d) {
        return c * t / d + b;
    }
    
    public static float easeIn(final float t, final float b, final float c, final float d) {
        return c * t / d + b;
    }
    
    public static float easeOut(final float t, final float b, final float c, final float d) {
        return c * t / d + b;
    }
    
    public static float easeInOut(final float t, final float b, final float c, final float d) {
        return c * t / d + b;
    }
}
