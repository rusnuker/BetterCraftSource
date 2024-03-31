// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.util.Iterator;

public interface ByteIterator extends Iterator<Byte>
{
    byte nextByte();
    
    int skip(final int p0);
}
