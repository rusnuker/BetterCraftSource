// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

public interface DoubleHash
{
    public interface Strategy
    {
        int hashCode(final double p0);
        
        boolean equals(final double p0, final double p1);
    }
}
