// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.util.Collection;

public interface ByteCollection extends Collection<Byte>, ByteIterable
{
    ByteIterator iterator();
    
    @Deprecated
    ByteIterator byteIterator();
    
     <T> T[] toArray(final T[] p0);
    
    boolean contains(final byte p0);
    
    byte[] toByteArray();
    
    byte[] toByteArray(final byte[] p0);
    
    byte[] toArray(final byte[] p0);
    
    boolean add(final byte p0);
    
    boolean rem(final byte p0);
    
    boolean addAll(final ByteCollection p0);
    
    boolean containsAll(final ByteCollection p0);
    
    boolean removeAll(final ByteCollection p0);
    
    boolean retainAll(final ByteCollection p0);
}
