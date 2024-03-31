// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Long2FloatSortedMap extends Long2FloatMap, SortedMap<Long, Float>
{
    ObjectSortedSet<Map.Entry<Long, Float>> entrySet();
    
    ObjectSortedSet<Long2FloatMap.Entry> long2FloatEntrySet();
    
    LongSortedSet keySet();
    
    FloatCollection values();
    
    LongComparator comparator();
    
    Long2FloatSortedMap subMap(final Long p0, final Long p1);
    
    Long2FloatSortedMap headMap(final Long p0);
    
    Long2FloatSortedMap tailMap(final Long p0);
    
    Long2FloatSortedMap subMap(final long p0, final long p1);
    
    Long2FloatSortedMap headMap(final long p0);
    
    Long2FloatSortedMap tailMap(final long p0);
    
    long firstLongKey();
    
    long lastLongKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Long2FloatMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Long2FloatMap.Entry> fastIterator(final Long2FloatMap.Entry p0);
    }
}
