// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.minecraft;

public final class Quaternion
{
    private final float x;
    private final float y;
    private final float z;
    private final float w;
    
    public Quaternion(final float x, final float y, final float z, final float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public float x() {
        return this.x;
    }
    
    public float y() {
        return this.y;
    }
    
    public float z() {
        return this.z;
    }
    
    public float w() {
        return this.w;
    }
}
