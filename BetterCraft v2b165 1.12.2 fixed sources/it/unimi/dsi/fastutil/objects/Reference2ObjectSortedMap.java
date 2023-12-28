// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

public interface Reference2ObjectSortedMap<K, V> extends Reference2ObjectMap<K, V>, SortedMap<K, V>
{
    ObjectSortedSet<Map.Entry<K, V>> entrySet();
    
    ObjectSortedSet<Reference2ObjectMap.Entry<K, V>> reference2ObjectEntrySet();
    
    ReferenceSortedSet<K> keySet();
    
    ObjectCollection<V> values();
    
    Comparator<? super K> comparator();
    
    Reference2ObjectSortedMap<K, V> subMap(final K p0, final K p1);
    
    Reference2ObjectSortedMap<K, V> headMap(final K p0);
    
    Reference2ObjectSortedMap<K, V> tailMap(final K p0);
    
    public interface FastSortedEntrySet<K, V> extends ObjectSortedSet<Reference2ObjectMap.Entry<K, V>>, FastEntrySet<K, V>
    {
        ObjectBidirectionalIterator<Reference2ObjectMap.Entry<K, V>> fastIterator(final Reference2ObjectMap.Entry<K, V> p0);
    }
}
