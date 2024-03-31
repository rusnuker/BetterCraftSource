// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Iterator;
import java.util.SortedSet;

public interface ObjectSortedSet<K> extends ObjectSet<K>, SortedSet<K>, ObjectBidirectionalIterable<K>
{
    ObjectBidirectionalIterator<K> iterator(final K p0);
    
    ObjectBidirectionalIterator<K> iterator();
    
    ObjectSortedSet<K> subSet(final K p0, final K p1);
    
    ObjectSortedSet<K> headSet(final K p0);
    
    ObjectSortedSet<K> tailSet(final K p0);
}
