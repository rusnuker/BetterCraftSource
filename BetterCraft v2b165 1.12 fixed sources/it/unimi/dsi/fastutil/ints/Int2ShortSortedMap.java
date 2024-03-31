// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Int2ShortSortedMap extends Int2ShortMap, SortedMap<Integer, Short>
{
    ObjectSortedSet<Map.Entry<Integer, Short>> entrySet();
    
    ObjectSortedSet<Int2ShortMap.Entry> int2ShortEntrySet();
    
    IntSortedSet keySet();
    
    ShortCollection values();
    
    IntComparator comparator();
    
    Int2ShortSortedMap subMap(final Integer p0, final Integer p1);
    
    Int2ShortSortedMap headMap(final Integer p0);
    
    Int2ShortSortedMap tailMap(final Integer p0);
    
    Int2ShortSortedMap subMap(final int p0, final int p1);
    
    Int2ShortSortedMap headMap(final int p0);
    
    Int2ShortSortedMap tailMap(final int p0);
    
    int firstIntKey();
    
    int lastIntKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Int2ShortMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Int2ShortMap.Entry> fastIterator(final Int2ShortMap.Entry p0);
    }
}
