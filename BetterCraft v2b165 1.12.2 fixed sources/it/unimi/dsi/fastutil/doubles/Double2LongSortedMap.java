// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.longs.LongCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Double2LongSortedMap extends Double2LongMap, SortedMap<Double, Long>
{
    ObjectSortedSet<Map.Entry<Double, Long>> entrySet();
    
    ObjectSortedSet<Double2LongMap.Entry> double2LongEntrySet();
    
    DoubleSortedSet keySet();
    
    LongCollection values();
    
    DoubleComparator comparator();
    
    Double2LongSortedMap subMap(final Double p0, final Double p1);
    
    Double2LongSortedMap headMap(final Double p0);
    
    Double2LongSortedMap tailMap(final Double p0);
    
    Double2LongSortedMap subMap(final double p0, final double p1);
    
    Double2LongSortedMap headMap(final double p0);
    
    Double2LongSortedMap tailMap(final double p0);
    
    double firstDoubleKey();
    
    double lastDoubleKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Double2LongMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Double2LongMap.Entry> fastIterator(final Double2LongMap.Entry p0);
    }
}
