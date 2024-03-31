// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18;

import net.minecraft.util.MathHelper;
import net.labymod.core.MathAdapter;

public class MathImplementation implements MathAdapter
{
    @Override
    public int clamp_int(final int num, final int min, final int max) {
        return MathHelper.clamp_int(num, min, max);
    }
    
    @Override
    public int floor_float(final float value) {
        return MathHelper.floor_float(value);
    }
    
    @Override
    public int ceiling_float_int(final float value) {
        return MathHelper.ceiling_float_int(value);
    }
    
    @Override
    public int ceiling_double_int(final double value) {
        return MathHelper.ceiling_double_int(value);
    }
    
    @Override
    public float sin(final float value) {
        return MathHelper.sin(value);
    }
    
    @Override
    public float cos(final float value) {
        return MathHelper.cos(value);
    }
    
    @Override
    public float clamp_float(final float num, final float min, final float max) {
        return MathHelper.clamp_float(num, min, max);
    }
    
    @Override
    public float sqrt_float(final float value) {
        return MathHelper.sqrt_float(value);
    }
    
    @Override
    public float abs(final float value) {
        return MathHelper.abs(value);
    }
    
    @Override
    public int hsvToRGB(final float r, final float g, final float b) {
        return MathHelper.hsvToRGB(r, g, b);
    }
    
    @Override
    public float wrapAngleTo180_float(final float value) {
        return MathHelper.wrapAngleTo180_float(value);
    }
    
    @Override
    public int floor_double(final double value) {
        return MathHelper.floor_double(value);
    }
    
    @Override
    public double clamp_double(final double num, final double min, final double max) {
        return MathHelper.clamp_double(num, min, max);
    }
}
