// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Comparator;
import it.unimi.dsi.fastutil.longs.LongCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Object2LongSortedMap<K> extends Object2LongMap<K>, SortedMap<K, Long>
{
    ObjectSortedSet<Map.Entry<K, Long>> entrySet();
    
    ObjectSortedSet<Object2LongMap.Entry<K>> object2LongEntrySet();
    
    ObjectSortedSet<K> keySet();
    
    LongCollection values();
    
    Comparator<? super K> comparator();
    
    Object2LongSortedMap<K> subMap(final K p0, final K p1);
    
    Object2LongSortedMap<K> headMap(final K p0);
    
    Object2LongSortedMap<K> tailMap(final K p0);
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Object2LongMap.Entry<K>>, FastEntrySet<K>
    {
        ObjectBidirectionalIterator<Object2LongMap.Entry<K>> fastIterator(final Object2LongMap.Entry<K> p0);
    }
}
