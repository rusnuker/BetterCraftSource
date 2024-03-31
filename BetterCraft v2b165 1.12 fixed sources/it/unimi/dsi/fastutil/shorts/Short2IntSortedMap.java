// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Short2IntSortedMap extends Short2IntMap, SortedMap<Short, Integer>
{
    ObjectSortedSet<Map.Entry<Short, Integer>> entrySet();
    
    ObjectSortedSet<Short2IntMap.Entry> short2IntEntrySet();
    
    ShortSortedSet keySet();
    
    IntCollection values();
    
    ShortComparator comparator();
    
    Short2IntSortedMap subMap(final Short p0, final Short p1);
    
    Short2IntSortedMap headMap(final Short p0);
    
    Short2IntSortedMap tailMap(final Short p0);
    
    Short2IntSortedMap subMap(final short p0, final short p1);
    
    Short2IntSortedMap headMap(final short p0);
    
    Short2IntSortedMap tailMap(final short p0);
    
    short firstShortKey();
    
    short lastShortKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Short2IntMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Short2IntMap.Entry> fastIterator(final Short2IntMap.Entry p0);
    }
}
