// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

public interface LongHash
{
    public interface Strategy
    {
        int hashCode(final long p0);
        
        boolean equals(final long p0, final long p1);
    }
}
