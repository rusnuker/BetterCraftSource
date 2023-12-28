// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.chunkanimator.easing;

public class Elastic
{
    public static float easeIn(float t, final float b, final float c, final float d) {
        if (t == 0.0f) {
            return b;
        }
        if ((t /= d) == 1.0f) {
            return b + c;
        }
        final float p = d * 0.3f;
        final float a = c;
        final float s = p / 4.0f;
        return -(a * (float)Math.pow(2.0, 10.0f * --t) * (float)Math.sin((t * d - s) * 6.2831855f / p)) + b;
    }
    
    public static float easeIn(float t, final float b, final float c, final float d, float a, final float p) {
        if (t == 0.0f) {
            return b;
        }
        if ((t /= d) == 1.0f) {
            return b + c;
        }
        float s;
        if (a < Math.abs(c)) {
            a = c;
            s = p / 4.0f;
        }
        else {
            s = p / 6.2831855f * (float)Math.asin(c / a);
        }
        return -(a * (float)Math.pow(2.0, 10.0f * --t) * (float)Math.sin((t * d - s) * 6.283185307179586 / p)) + b;
    }
    
    public static float easeOut(float t, final float b, final float c, final float d) {
        if (t == 0.0f) {
            return b;
        }
        if ((t /= d) == 1.0f) {
            return b + c;
        }
        final float p = d * 0.3f;
        final float a = c;
        final float s = p / 4.0f;
        return a * (float)Math.pow(2.0, -10.0f * t) * (float)Math.sin((t * d - s) * 6.2831855f / p) + c + b;
    }
    
    public static float easeOut(float t, final float b, final float c, final float d, float a, final float p) {
        if (t == 0.0f) {
            return b;
        }
        if ((t /= d) == 1.0f) {
            return b + c;
        }
        float s;
        if (a < Math.abs(c)) {
            a = c;
            s = p / 4.0f;
        }
        else {
            s = p / 6.2831855f * (float)Math.asin(c / a);
        }
        return a * (float)Math.pow(2.0, -10.0f * t) * (float)Math.sin((t * d - s) * 6.2831855f / p) + c + b;
    }
    
    public static float easeInOut(float t, final float b, final float c, final float d) {
        if (t == 0.0f) {
            return b;
        }
        if ((t /= d / 2.0f) == 2.0f) {
            return b + c;
        }
        final float p = d * 0.45000002f;
        final float a = c;
        final float s = p / 4.0f;
        if (t < 1.0f) {
            return -0.5f * (a * (float)Math.pow(2.0, 10.0f * --t) * (float)Math.sin((t * d - s) * 6.2831855f / p)) + b;
        }
        return a * (float)Math.pow(2.0, -10.0f * --t) * (float)Math.sin((t * d - s) * 6.2831855f / p) * 0.5f + c + b;
    }
    
    public static float easeInOut(float t, final float b, final float c, final float d, float a, final float p) {
        if (t == 0.0f) {
            return b;
        }
        if ((t /= d / 2.0f) == 2.0f) {
            return b + c;
        }
        float s;
        if (a < Math.abs(c)) {
            a = c;
            s = p / 4.0f;
        }
        else {
            s = p / 6.2831855f * (float)Math.asin(c / a);
        }
        if (t < 1.0f) {
            return -0.5f * (a * (float)Math.pow(2.0, 10.0f * --t) * (float)Math.sin((t * d - s) * 6.2831855f / p)) + b;
        }
        return a * (float)Math.pow(2.0, -10.0f * --t) * (float)Math.sin((t * d - s) * 6.2831855f / p) * 0.5f + c + b;
    }
}
