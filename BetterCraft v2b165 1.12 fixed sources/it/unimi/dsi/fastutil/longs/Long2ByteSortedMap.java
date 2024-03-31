// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Long2ByteSortedMap extends Long2ByteMap, SortedMap<Long, Byte>
{
    ObjectSortedSet<Map.Entry<Long, Byte>> entrySet();
    
    ObjectSortedSet<Long2ByteMap.Entry> long2ByteEntrySet();
    
    LongSortedSet keySet();
    
    ByteCollection values();
    
    LongComparator comparator();
    
    Long2ByteSortedMap subMap(final Long p0, final Long p1);
    
    Long2ByteSortedMap headMap(final Long p0);
    
    Long2ByteSortedMap tailMap(final Long p0);
    
    Long2ByteSortedMap subMap(final long p0, final long p1);
    
    Long2ByteSortedMap headMap(final long p0);
    
    Long2ByteSortedMap tailMap(final long p0);
    
    long firstLongKey();
    
    long lastLongKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Long2ByteMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Long2ByteMap.Entry> fastIterator(final Long2ByteMap.Entry p0);
    }
}
