// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Long2DoubleSortedMap extends Long2DoubleMap, SortedMap<Long, Double>
{
    ObjectSortedSet<Map.Entry<Long, Double>> entrySet();
    
    ObjectSortedSet<Long2DoubleMap.Entry> long2DoubleEntrySet();
    
    LongSortedSet keySet();
    
    DoubleCollection values();
    
    LongComparator comparator();
    
    Long2DoubleSortedMap subMap(final Long p0, final Long p1);
    
    Long2DoubleSortedMap headMap(final Long p0);
    
    Long2DoubleSortedMap tailMap(final Long p0);
    
    Long2DoubleSortedMap subMap(final long p0, final long p1);
    
    Long2DoubleSortedMap headMap(final long p0);
    
    Long2DoubleSortedMap tailMap(final long p0);
    
    long firstLongKey();
    
    long lastLongKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Long2DoubleMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Long2DoubleMap.Entry> fastIterator(final Long2DoubleMap.Entry p0);
    }
}
