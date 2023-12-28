// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Long2ShortSortedMap extends Long2ShortMap, SortedMap<Long, Short>
{
    ObjectSortedSet<Map.Entry<Long, Short>> entrySet();
    
    ObjectSortedSet<Long2ShortMap.Entry> long2ShortEntrySet();
    
    LongSortedSet keySet();
    
    ShortCollection values();
    
    LongComparator comparator();
    
    Long2ShortSortedMap subMap(final Long p0, final Long p1);
    
    Long2ShortSortedMap headMap(final Long p0);
    
    Long2ShortSortedMap tailMap(final Long p0);
    
    Long2ShortSortedMap subMap(final long p0, final long p1);
    
    Long2ShortSortedMap headMap(final long p0);
    
    Long2ShortSortedMap tailMap(final long p0);
    
    long firstLongKey();
    
    long lastLongKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Long2ShortMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Long2ShortMap.Entry> fastIterator(final Long2ShortMap.Entry p0);
    }
}
