// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

public interface BooleanHash
{
    public interface Strategy
    {
        int hashCode(final boolean p0);
        
        boolean equals(final boolean p0, final boolean p1);
    }
}
