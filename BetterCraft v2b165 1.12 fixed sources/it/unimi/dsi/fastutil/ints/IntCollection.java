// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.util.Collection;

public interface IntCollection extends Collection<Integer>, IntIterable
{
    IntIterator iterator();
    
    @Deprecated
    IntIterator intIterator();
    
     <T> T[] toArray(final T[] p0);
    
    boolean contains(final int p0);
    
    int[] toIntArray();
    
    int[] toIntArray(final int[] p0);
    
    int[] toArray(final int[] p0);
    
    boolean add(final int p0);
    
    boolean rem(final int p0);
    
    boolean addAll(final IntCollection p0);
    
    boolean containsAll(final IntCollection p0);
    
    boolean removeAll(final IntCollection p0);
    
    boolean retainAll(final IntCollection p0);
}
