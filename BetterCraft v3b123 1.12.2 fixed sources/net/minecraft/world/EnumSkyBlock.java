// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world;

public enum EnumSkyBlock
{
    SKY("SKY", 0, 15), 
    BLOCK("BLOCK", 1, 0);
    
    public final int defaultLightValue;
    
    private EnumSkyBlock(final String s, final int n, final int p_i1961_3_) {
        this.defaultLightValue = p_i1961_3_;
    }
}
