// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

public interface FloatHash
{
    public interface Strategy
    {
        int hashCode(final float p0);
        
        boolean equals(final float p0, final float p1);
    }
}
