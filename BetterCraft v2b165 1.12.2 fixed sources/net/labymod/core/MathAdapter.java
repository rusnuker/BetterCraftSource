// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core;

public interface MathAdapter
{
    int clamp_int(final int p0, final int p1, final int p2);
    
    double clamp_double(final double p0, final double p1, final double p2);
    
    int floor_float(final float p0);
    
    int ceiling_float_int(final float p0);
    
    int ceiling_double_int(final double p0);
    
    float sin(final float p0);
    
    float cos(final float p0);
    
    float clamp_float(final float p0, final float p1, final float p2);
    
    float sqrt_float(final float p0);
    
    float abs(final float p0);
    
    int hsvToRGB(final float p0, final float p1, final float p2);
    
    float wrapAngleTo180_float(final float p0);
    
    int floor_double(final double p0);
}
