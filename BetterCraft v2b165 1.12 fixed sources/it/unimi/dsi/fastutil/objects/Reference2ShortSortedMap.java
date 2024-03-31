// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Comparator;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Reference2ShortSortedMap<K> extends Reference2ShortMap<K>, SortedMap<K, Short>
{
    ObjectSortedSet<Map.Entry<K, Short>> entrySet();
    
    ObjectSortedSet<Reference2ShortMap.Entry<K>> reference2ShortEntrySet();
    
    ReferenceSortedSet<K> keySet();
    
    ShortCollection values();
    
    Comparator<? super K> comparator();
    
    Reference2ShortSortedMap<K> subMap(final K p0, final K p1);
    
    Reference2ShortSortedMap<K> headMap(final K p0);
    
    Reference2ShortSortedMap<K> tailMap(final K p0);
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Reference2ShortMap.Entry<K>>, FastEntrySet<K>
    {
        ObjectBidirectionalIterator<Reference2ShortMap.Entry<K>> fastIterator(final Reference2ShortMap.Entry<K> p0);
    }
}
