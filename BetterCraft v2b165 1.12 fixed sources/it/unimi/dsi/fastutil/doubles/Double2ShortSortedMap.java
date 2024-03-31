// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Double2ShortSortedMap extends Double2ShortMap, SortedMap<Double, Short>
{
    ObjectSortedSet<Map.Entry<Double, Short>> entrySet();
    
    ObjectSortedSet<Double2ShortMap.Entry> double2ShortEntrySet();
    
    DoubleSortedSet keySet();
    
    ShortCollection values();
    
    DoubleComparator comparator();
    
    Double2ShortSortedMap subMap(final Double p0, final Double p1);
    
    Double2ShortSortedMap headMap(final Double p0);
    
    Double2ShortSortedMap tailMap(final Double p0);
    
    Double2ShortSortedMap subMap(final double p0, final double p1);
    
    Double2ShortSortedMap headMap(final double p0);
    
    Double2ShortSortedMap tailMap(final double p0);
    
    double firstDoubleKey();
    
    double lastDoubleKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Double2ShortMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Double2ShortMap.Entry> fastIterator(final Double2ShortMap.Entry p0);
    }
}
