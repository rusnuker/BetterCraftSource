// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

public interface Object2ReferenceSortedMap<K, V> extends Object2ReferenceMap<K, V>, SortedMap<K, V>
{
    ObjectSortedSet<Map.Entry<K, V>> entrySet();
    
    ObjectSortedSet<Object2ReferenceMap.Entry<K, V>> object2ReferenceEntrySet();
    
    ObjectSortedSet<K> keySet();
    
    ReferenceCollection<V> values();
    
    Comparator<? super K> comparator();
    
    Object2ReferenceSortedMap<K, V> subMap(final K p0, final K p1);
    
    Object2ReferenceSortedMap<K, V> headMap(final K p0);
    
    Object2ReferenceSortedMap<K, V> tailMap(final K p0);
    
    public interface FastSortedEntrySet<K, V> extends ObjectSortedSet<Object2ReferenceMap.Entry<K, V>>, FastEntrySet<K, V>
    {
        ObjectBidirectionalIterator<Object2ReferenceMap.Entry<K, V>> fastIterator(final Object2ReferenceMap.Entry<K, V> p0);
    }
}
