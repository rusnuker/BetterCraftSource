// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.util.ListIterator;

public interface ByteListIterator extends ListIterator<Byte>, ByteBidirectionalIterator
{
    void set(final byte p0);
    
    void add(final byte p0);
}
