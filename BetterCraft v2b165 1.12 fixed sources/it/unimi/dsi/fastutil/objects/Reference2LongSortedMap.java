// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Comparator;
import it.unimi.dsi.fastutil.longs.LongCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Reference2LongSortedMap<K> extends Reference2LongMap<K>, SortedMap<K, Long>
{
    ObjectSortedSet<Map.Entry<K, Long>> entrySet();
    
    ObjectSortedSet<Reference2LongMap.Entry<K>> reference2LongEntrySet();
    
    ReferenceSortedSet<K> keySet();
    
    LongCollection values();
    
    Comparator<? super K> comparator();
    
    Reference2LongSortedMap<K> subMap(final K p0, final K p1);
    
    Reference2LongSortedMap<K> headMap(final K p0);
    
    Reference2LongSortedMap<K> tailMap(final K p0);
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Reference2LongMap.Entry<K>>, FastEntrySet<K>
    {
        ObjectBidirectionalIterator<Reference2LongMap.Entry<K>> fastIterator(final Reference2LongMap.Entry<K> p0);
    }
}
