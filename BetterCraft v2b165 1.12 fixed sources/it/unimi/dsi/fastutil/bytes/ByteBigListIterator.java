// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.BigListIterator;

public interface ByteBigListIterator extends ByteBidirectionalIterator, BigListIterator<Byte>
{
    void set(final byte p0);
    
    void add(final byte p0);
    
    void set(final Byte p0);
    
    void add(final Byte p0);
}
