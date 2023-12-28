// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Int2IntSortedMap extends Int2IntMap, SortedMap<Integer, Integer>
{
    ObjectSortedSet<Map.Entry<Integer, Integer>> entrySet();
    
    ObjectSortedSet<Int2IntMap.Entry> int2IntEntrySet();
    
    IntSortedSet keySet();
    
    IntCollection values();
    
    IntComparator comparator();
    
    Int2IntSortedMap subMap(final Integer p0, final Integer p1);
    
    Int2IntSortedMap headMap(final Integer p0);
    
    Int2IntSortedMap tailMap(final Integer p0);
    
    Int2IntSortedMap subMap(final int p0, final int p1);
    
    Int2IntSortedMap headMap(final int p0);
    
    Int2IntSortedMap tailMap(final int p0);
    
    int firstIntKey();
    
    int lastIntKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Int2IntMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Int2IntMap.Entry> fastIterator(final Int2IntMap.Entry p0);
    }
}
