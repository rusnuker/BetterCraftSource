// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Double2DoubleSortedMap extends Double2DoubleMap, SortedMap<Double, Double>
{
    ObjectSortedSet<Map.Entry<Double, Double>> entrySet();
    
    ObjectSortedSet<Double2DoubleMap.Entry> double2DoubleEntrySet();
    
    DoubleSortedSet keySet();
    
    DoubleCollection values();
    
    DoubleComparator comparator();
    
    Double2DoubleSortedMap subMap(final Double p0, final Double p1);
    
    Double2DoubleSortedMap headMap(final Double p0);
    
    Double2DoubleSortedMap tailMap(final Double p0);
    
    Double2DoubleSortedMap subMap(final double p0, final double p1);
    
    Double2DoubleSortedMap headMap(final double p0);
    
    Double2DoubleSortedMap tailMap(final double p0);
    
    double firstDoubleKey();
    
    double lastDoubleKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Double2DoubleMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Double2DoubleMap.Entry> fastIterator(final Double2DoubleMap.Entry p0);
    }
}
