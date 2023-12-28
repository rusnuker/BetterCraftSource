// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.util.Collection;

public interface ShortCollection extends Collection<Short>, ShortIterable
{
    ShortIterator iterator();
    
    @Deprecated
    ShortIterator shortIterator();
    
     <T> T[] toArray(final T[] p0);
    
    boolean contains(final short p0);
    
    short[] toShortArray();
    
    short[] toShortArray(final short[] p0);
    
    short[] toArray(final short[] p0);
    
    boolean add(final short p0);
    
    boolean rem(final short p0);
    
    boolean addAll(final ShortCollection p0);
    
    boolean containsAll(final ShortCollection p0);
    
    boolean removeAll(final ShortCollection p0);
    
    boolean retainAll(final ShortCollection p0);
}
