// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Long2LongSortedMap extends Long2LongMap, SortedMap<Long, Long>
{
    ObjectSortedSet<Map.Entry<Long, Long>> entrySet();
    
    ObjectSortedSet<Long2LongMap.Entry> long2LongEntrySet();
    
    LongSortedSet keySet();
    
    LongCollection values();
    
    LongComparator comparator();
    
    Long2LongSortedMap subMap(final Long p0, final Long p1);
    
    Long2LongSortedMap headMap(final Long p0);
    
    Long2LongSortedMap tailMap(final Long p0);
    
    Long2LongSortedMap subMap(final long p0, final long p1);
    
    Long2LongSortedMap headMap(final long p0);
    
    Long2LongSortedMap tailMap(final long p0);
    
    long firstLongKey();
    
    long lastLongKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Long2LongMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Long2LongMap.Entry> fastIterator(final Long2LongMap.Entry p0);
    }
}
