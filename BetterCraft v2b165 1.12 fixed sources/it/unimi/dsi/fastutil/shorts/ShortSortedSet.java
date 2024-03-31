// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.util.SortedSet;

public interface ShortSortedSet extends ShortSet, SortedSet<Short>
{
    ShortBidirectionalIterator iterator(final short p0);
    
    @Deprecated
    ShortBidirectionalIterator shortIterator();
    
    ShortBidirectionalIterator iterator();
    
    ShortSortedSet subSet(final Short p0, final Short p1);
    
    ShortSortedSet headSet(final Short p0);
    
    ShortSortedSet tailSet(final Short p0);
    
    ShortComparator comparator();
    
    ShortSortedSet subSet(final short p0, final short p1);
    
    ShortSortedSet headSet(final short p0);
    
    ShortSortedSet tailSet(final short p0);
    
    short firstShort();
    
    short lastShort();
}
