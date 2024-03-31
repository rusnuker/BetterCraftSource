// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.minecraft;

public interface BlockChangeRecord
{
    byte getSectionX();
    
    byte getSectionY();
    
    byte getSectionZ();
    
    short getY(final int p0);
    
    default short getY() {
        return this.getY(-1);
    }
    
    int getBlockId();
    
    void setBlockId(final int p0);
}
