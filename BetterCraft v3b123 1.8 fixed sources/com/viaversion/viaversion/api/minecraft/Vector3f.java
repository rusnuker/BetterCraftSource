// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.minecraft;

public final class Vector3f
{
    private final float x;
    private final float y;
    private final float z;
    
    public Vector3f(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
}
