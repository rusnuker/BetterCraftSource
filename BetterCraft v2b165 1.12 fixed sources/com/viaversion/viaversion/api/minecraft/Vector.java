// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.minecraft;

public class Vector
{
    private int blockX;
    private int blockY;
    private int blockZ;
    
    public Vector(final int blockX, final int blockY, final int blockZ) {
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
    }
    
    public int getBlockX() {
        return this.blockX;
    }
    
    public void setBlockX(final int blockX) {
        this.blockX = blockX;
    }
    
    public int getBlockY() {
        return this.blockY;
    }
    
    public void setBlockY(final int blockY) {
        this.blockY = blockY;
    }
    
    public int getBlockZ() {
        return this.blockZ;
    }
    
    public void setBlockZ(final int blockZ) {
        this.blockZ = blockZ;
    }
}
