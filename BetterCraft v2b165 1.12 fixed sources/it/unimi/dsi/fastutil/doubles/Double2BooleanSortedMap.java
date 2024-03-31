// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Double2BooleanSortedMap extends Double2BooleanMap, SortedMap<Double, Boolean>
{
    ObjectSortedSet<Map.Entry<Double, Boolean>> entrySet();
    
    ObjectSortedSet<Double2BooleanMap.Entry> double2BooleanEntrySet();
    
    DoubleSortedSet keySet();
    
    BooleanCollection values();
    
    DoubleComparator comparator();
    
    Double2BooleanSortedMap subMap(final Double p0, final Double p1);
    
    Double2BooleanSortedMap headMap(final Double p0);
    
    Double2BooleanSortedMap tailMap(final Double p0);
    
    Double2BooleanSortedMap subMap(final double p0, final double p1);
    
    Double2BooleanSortedMap headMap(final double p0);
    
    Double2BooleanSortedMap tailMap(final double p0);
    
    double firstDoubleKey();
    
    double lastDoubleKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Double2BooleanMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Double2BooleanMap.Entry> fastIterator(final Double2BooleanMap.Entry p0);
    }
}
