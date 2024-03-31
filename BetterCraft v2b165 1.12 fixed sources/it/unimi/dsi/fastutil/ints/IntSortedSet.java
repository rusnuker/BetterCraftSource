// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.util.SortedSet;

public interface IntSortedSet extends IntSet, SortedSet<Integer>
{
    IntBidirectionalIterator iterator(final int p0);
    
    @Deprecated
    IntBidirectionalIterator intIterator();
    
    IntBidirectionalIterator iterator();
    
    IntSortedSet subSet(final Integer p0, final Integer p1);
    
    IntSortedSet headSet(final Integer p0);
    
    IntSortedSet tailSet(final Integer p0);
    
    IntComparator comparator();
    
    IntSortedSet subSet(final int p0, final int p1);
    
    IntSortedSet headSet(final int p0);
    
    IntSortedSet tailSet(final int p0);
    
    int firstInt();
    
    int lastInt();
}
