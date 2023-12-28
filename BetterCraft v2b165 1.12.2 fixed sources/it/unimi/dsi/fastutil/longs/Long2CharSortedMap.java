// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.chars.CharCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Long2CharSortedMap extends Long2CharMap, SortedMap<Long, Character>
{
    ObjectSortedSet<Map.Entry<Long, Character>> entrySet();
    
    ObjectSortedSet<Long2CharMap.Entry> long2CharEntrySet();
    
    LongSortedSet keySet();
    
    CharCollection values();
    
    LongComparator comparator();
    
    Long2CharSortedMap subMap(final Long p0, final Long p1);
    
    Long2CharSortedMap headMap(final Long p0);
    
    Long2CharSortedMap tailMap(final Long p0);
    
    Long2CharSortedMap subMap(final long p0, final long p1);
    
    Long2CharSortedMap headMap(final long p0);
    
    Long2CharSortedMap tailMap(final long p0);
    
    long firstLongKey();
    
    long lastLongKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Long2CharMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Long2CharMap.Entry> fastIterator(final Long2CharMap.Entry p0);
    }
}
