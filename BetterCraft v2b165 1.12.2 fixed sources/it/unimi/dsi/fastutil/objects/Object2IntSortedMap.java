// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Comparator;
import it.unimi.dsi.fastutil.ints.IntCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Object2IntSortedMap<K> extends Object2IntMap<K>, SortedMap<K, Integer>
{
    ObjectSortedSet<Map.Entry<K, Integer>> entrySet();
    
    ObjectSortedSet<Object2IntMap.Entry<K>> object2IntEntrySet();
    
    ObjectSortedSet<K> keySet();
    
    IntCollection values();
    
    Comparator<? super K> comparator();
    
    Object2IntSortedMap<K> subMap(final K p0, final K p1);
    
    Object2IntSortedMap<K> headMap(final K p0);
    
    Object2IntSortedMap<K> tailMap(final K p0);
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Object2IntMap.Entry<K>>, FastEntrySet<K>
    {
        ObjectBidirectionalIterator<Object2IntMap.Entry<K>> fastIterator(final Object2IntMap.Entry<K> p0);
    }
}
