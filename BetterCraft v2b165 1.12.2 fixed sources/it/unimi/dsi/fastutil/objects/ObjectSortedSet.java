// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.SortedSet;

public interface ObjectSortedSet<K> extends ObjectSet<K>, SortedSet<K>
{
    ObjectBidirectionalIterator<K> iterator(final K p0);
    
    @Deprecated
    ObjectBidirectionalIterator<K> objectIterator();
    
    ObjectBidirectionalIterator<K> iterator();
    
    ObjectSortedSet<K> subSet(final K p0, final K p1);
    
    ObjectSortedSet<K> headSet(final K p0);
    
    ObjectSortedSet<K> tailSet(final K p0);
}
