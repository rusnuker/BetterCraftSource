// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Double2IntSortedMap extends Double2IntMap, SortedMap<Double, Integer>
{
    ObjectSortedSet<Map.Entry<Double, Integer>> entrySet();
    
    ObjectSortedSet<Double2IntMap.Entry> double2IntEntrySet();
    
    DoubleSortedSet keySet();
    
    IntCollection values();
    
    DoubleComparator comparator();
    
    Double2IntSortedMap subMap(final Double p0, final Double p1);
    
    Double2IntSortedMap headMap(final Double p0);
    
    Double2IntSortedMap tailMap(final Double p0);
    
    Double2IntSortedMap subMap(final double p0, final double p1);
    
    Double2IntSortedMap headMap(final double p0);
    
    Double2IntSortedMap tailMap(final double p0);
    
    double firstDoubleKey();
    
    double lastDoubleKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Double2IntMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Double2IntMap.Entry> fastIterator(final Double2IntMap.Entry p0);
    }
}
