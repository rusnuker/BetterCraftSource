// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.util.Set;

public interface ByteSet extends ByteCollection, Set<Byte>
{
    ByteIterator iterator();
    
    boolean remove(final byte p0);
}
