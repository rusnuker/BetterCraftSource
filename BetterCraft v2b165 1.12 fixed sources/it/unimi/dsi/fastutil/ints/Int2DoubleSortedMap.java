// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Int2DoubleSortedMap extends Int2DoubleMap, SortedMap<Integer, Double>
{
    ObjectSortedSet<Map.Entry<Integer, Double>> entrySet();
    
    ObjectSortedSet<Int2DoubleMap.Entry> int2DoubleEntrySet();
    
    IntSortedSet keySet();
    
    DoubleCollection values();
    
    IntComparator comparator();
    
    Int2DoubleSortedMap subMap(final Integer p0, final Integer p1);
    
    Int2DoubleSortedMap headMap(final Integer p0);
    
    Int2DoubleSortedMap tailMap(final Integer p0);
    
    Int2DoubleSortedMap subMap(final int p0, final int p1);
    
    Int2DoubleSortedMap headMap(final int p0);
    
    Int2DoubleSortedMap tailMap(final int p0);
    
    int firstIntKey();
    
    int lastIntKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Int2DoubleMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Int2DoubleMap.Entry> fastIterator(final Int2DoubleMap.Entry p0);
    }
}
