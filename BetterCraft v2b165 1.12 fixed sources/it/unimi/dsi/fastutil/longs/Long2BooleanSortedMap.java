// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Long2BooleanSortedMap extends Long2BooleanMap, SortedMap<Long, Boolean>
{
    ObjectSortedSet<Map.Entry<Long, Boolean>> entrySet();
    
    ObjectSortedSet<Long2BooleanMap.Entry> long2BooleanEntrySet();
    
    LongSortedSet keySet();
    
    BooleanCollection values();
    
    LongComparator comparator();
    
    Long2BooleanSortedMap subMap(final Long p0, final Long p1);
    
    Long2BooleanSortedMap headMap(final Long p0);
    
    Long2BooleanSortedMap tailMap(final Long p0);
    
    Long2BooleanSortedMap subMap(final long p0, final long p1);
    
    Long2BooleanSortedMap headMap(final long p0);
    
    Long2BooleanSortedMap tailMap(final long p0);
    
    long firstLongKey();
    
    long lastLongKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Long2BooleanMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Long2BooleanMap.Entry> fastIterator(final Long2BooleanMap.Entry p0);
    }
}
