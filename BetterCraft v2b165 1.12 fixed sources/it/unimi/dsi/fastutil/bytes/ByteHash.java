// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

public interface ByteHash
{
    public interface Strategy
    {
        int hashCode(final byte p0);
        
        boolean equals(final byte p0, final byte p1);
    }
}
