// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Long2ObjectSortedMap<V> extends Long2ObjectMap<V>, SortedMap<Long, V>
{
    ObjectSortedSet<Map.Entry<Long, V>> entrySet();
    
    ObjectSortedSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet();
    
    LongSortedSet keySet();
    
    ObjectCollection<V> values();
    
    LongComparator comparator();
    
    Long2ObjectSortedMap<V> subMap(final Long p0, final Long p1);
    
    Long2ObjectSortedMap<V> headMap(final Long p0);
    
    Long2ObjectSortedMap<V> tailMap(final Long p0);
    
    Long2ObjectSortedMap<V> subMap(final long p0, final long p1);
    
    Long2ObjectSortedMap<V> headMap(final long p0);
    
    Long2ObjectSortedMap<V> tailMap(final long p0);
    
    long firstLongKey();
    
    long lastLongKey();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Long2ObjectMap.Entry<V>>, FastEntrySet<V>
    {
        ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> fastIterator(final Long2ObjectMap.Entry<V> p0);
    }
}
