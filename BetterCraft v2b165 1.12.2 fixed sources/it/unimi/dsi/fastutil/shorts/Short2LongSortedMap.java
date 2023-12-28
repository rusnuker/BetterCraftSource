// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.longs.LongCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Short2LongSortedMap extends Short2LongMap, SortedMap<Short, Long>
{
    ObjectSortedSet<Map.Entry<Short, Long>> entrySet();
    
    ObjectSortedSet<Short2LongMap.Entry> short2LongEntrySet();
    
    ShortSortedSet keySet();
    
    LongCollection values();
    
    ShortComparator comparator();
    
    Short2LongSortedMap subMap(final Short p0, final Short p1);
    
    Short2LongSortedMap headMap(final Short p0);
    
    Short2LongSortedMap tailMap(final Short p0);
    
    Short2LongSortedMap subMap(final short p0, final short p1);
    
    Short2LongSortedMap headMap(final short p0);
    
    Short2LongSortedMap tailMap(final short p0);
    
    short firstShortKey();
    
    short lastShortKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Short2LongMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Short2LongMap.Entry> fastIterator(final Short2LongMap.Entry p0);
    }
}
