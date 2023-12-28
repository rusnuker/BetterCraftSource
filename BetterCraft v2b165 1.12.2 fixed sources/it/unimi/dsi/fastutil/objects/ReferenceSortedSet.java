// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.SortedSet;

public interface ReferenceSortedSet<K> extends ReferenceSet<K>, SortedSet<K>
{
    ObjectBidirectionalIterator<K> iterator(final K p0);
    
    @Deprecated
    ObjectBidirectionalIterator<K> objectIterator();
    
    ObjectBidirectionalIterator<K> iterator();
    
    ReferenceSortedSet<K> subSet(final K p0, final K p1);
    
    ReferenceSortedSet<K> headSet(final K p0);
    
    ReferenceSortedSet<K> tailSet(final K p0);
}
