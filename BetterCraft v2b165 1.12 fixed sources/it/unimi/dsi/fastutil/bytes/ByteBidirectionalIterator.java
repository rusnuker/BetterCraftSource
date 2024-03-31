// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public interface ByteBidirectionalIterator extends ByteIterator, ObjectBidirectionalIterator<Byte>
{
    byte previousByte();
    
    int back(final int p0);
}
