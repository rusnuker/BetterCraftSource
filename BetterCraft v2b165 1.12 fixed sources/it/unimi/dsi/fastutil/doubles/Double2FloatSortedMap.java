// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Double2FloatSortedMap extends Double2FloatMap, SortedMap<Double, Float>
{
    ObjectSortedSet<Map.Entry<Double, Float>> entrySet();
    
    ObjectSortedSet<Double2FloatMap.Entry> double2FloatEntrySet();
    
    DoubleSortedSet keySet();
    
    FloatCollection values();
    
    DoubleComparator comparator();
    
    Double2FloatSortedMap subMap(final Double p0, final Double p1);
    
    Double2FloatSortedMap headMap(final Double p0);
    
    Double2FloatSortedMap tailMap(final Double p0);
    
    Double2FloatSortedMap subMap(final double p0, final double p1);
    
    Double2FloatSortedMap headMap(final double p0);
    
    Double2FloatSortedMap tailMap(final double p0);
    
    double firstDoubleKey();
    
    double lastDoubleKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Double2FloatMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Double2FloatMap.Entry> fastIterator(final Double2FloatMap.Entry p0);
    }
}
