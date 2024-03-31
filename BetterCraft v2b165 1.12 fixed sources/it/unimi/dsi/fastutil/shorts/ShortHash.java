// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

public interface ShortHash
{
    public interface Strategy
    {
        int hashCode(final short p0);
        
        boolean equals(final short p0, final short p1);
    }
}
