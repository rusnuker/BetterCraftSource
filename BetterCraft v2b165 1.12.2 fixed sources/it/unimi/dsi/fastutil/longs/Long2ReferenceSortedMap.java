// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Long2ReferenceSortedMap<V> extends Long2ReferenceMap<V>, SortedMap<Long, V>
{
    ObjectSortedSet<Map.Entry<Long, V>> entrySet();
    
    ObjectSortedSet<Long2ReferenceMap.Entry<V>> long2ReferenceEntrySet();
    
    LongSortedSet keySet();
    
    ReferenceCollection<V> values();
    
    LongComparator comparator();
    
    Long2ReferenceSortedMap<V> subMap(final Long p0, final Long p1);
    
    Long2ReferenceSortedMap<V> headMap(final Long p0);
    
    Long2ReferenceSortedMap<V> tailMap(final Long p0);
    
    Long2ReferenceSortedMap<V> subMap(final long p0, final long p1);
    
    Long2ReferenceSortedMap<V> headMap(final long p0);
    
    Long2ReferenceSortedMap<V> tailMap(final long p0);
    
    long firstLongKey();
    
    long lastLongKey();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Long2ReferenceMap.Entry<V>>, FastEntrySet<V>
    {
        ObjectBidirectionalIterator<Long2ReferenceMap.Entry<V>> fastIterator(final Long2ReferenceMap.Entry<V> p0);
    }
}
