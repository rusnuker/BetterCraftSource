// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

public interface Reference2ReferenceSortedMap<K, V> extends Reference2ReferenceMap<K, V>, SortedMap<K, V>
{
    ObjectSortedSet<Map.Entry<K, V>> entrySet();
    
    ObjectSortedSet<Reference2ReferenceMap.Entry<K, V>> reference2ReferenceEntrySet();
    
    ReferenceSortedSet<K> keySet();
    
    ReferenceCollection<V> values();
    
    Comparator<? super K> comparator();
    
    Reference2ReferenceSortedMap<K, V> subMap(final K p0, final K p1);
    
    Reference2ReferenceSortedMap<K, V> headMap(final K p0);
    
    Reference2ReferenceSortedMap<K, V> tailMap(final K p0);
    
    public interface FastSortedEntrySet<K, V> extends ObjectSortedSet<Reference2ReferenceMap.Entry<K, V>>, FastEntrySet<K, V>
    {
        ObjectBidirectionalIterator<Reference2ReferenceMap.Entry<K, V>> fastIterator(final Reference2ReferenceMap.Entry<K, V> p0);
    }
}
