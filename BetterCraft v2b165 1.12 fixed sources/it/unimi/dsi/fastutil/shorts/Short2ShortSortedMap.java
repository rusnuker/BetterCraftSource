// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Short2ShortSortedMap extends Short2ShortMap, SortedMap<Short, Short>
{
    ObjectSortedSet<Map.Entry<Short, Short>> entrySet();
    
    ObjectSortedSet<Short2ShortMap.Entry> short2ShortEntrySet();
    
    ShortSortedSet keySet();
    
    ShortCollection values();
    
    ShortComparator comparator();
    
    Short2ShortSortedMap subMap(final Short p0, final Short p1);
    
    Short2ShortSortedMap headMap(final Short p0);
    
    Short2ShortSortedMap tailMap(final Short p0);
    
    Short2ShortSortedMap subMap(final short p0, final short p1);
    
    Short2ShortSortedMap headMap(final short p0);
    
    Short2ShortSortedMap tailMap(final short p0);
    
    short firstShortKey();
    
    short lastShortKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Short2ShortMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Short2ShortMap.Entry> fastIterator(final Short2ShortMap.Entry p0);
    }
}
