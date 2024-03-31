// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

public interface CharHash
{
    public interface Strategy
    {
        int hashCode(final char p0);
        
        boolean equals(final char p0, final char p1);
    }
}
