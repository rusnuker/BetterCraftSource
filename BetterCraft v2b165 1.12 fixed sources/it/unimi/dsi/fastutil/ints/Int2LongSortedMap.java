// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.longs.LongCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Int2LongSortedMap extends Int2LongMap, SortedMap<Integer, Long>
{
    ObjectSortedSet<Map.Entry<Integer, Long>> entrySet();
    
    ObjectSortedSet<Int2LongMap.Entry> int2LongEntrySet();
    
    IntSortedSet keySet();
    
    LongCollection values();
    
    IntComparator comparator();
    
    Int2LongSortedMap subMap(final Integer p0, final Integer p1);
    
    Int2LongSortedMap headMap(final Integer p0);
    
    Int2LongSortedMap tailMap(final Integer p0);
    
    Int2LongSortedMap subMap(final int p0, final int p1);
    
    Int2LongSortedMap headMap(final int p0);
    
    Int2LongSortedMap tailMap(final int p0);
    
    int firstIntKey();
    
    int lastIntKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Int2LongMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Int2LongMap.Entry> fastIterator(final Int2LongMap.Entry p0);
    }
}
