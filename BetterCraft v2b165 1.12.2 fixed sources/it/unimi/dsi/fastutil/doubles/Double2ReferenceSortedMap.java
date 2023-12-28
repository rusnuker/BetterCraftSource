// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Double2ReferenceSortedMap<V> extends Double2ReferenceMap<V>, SortedMap<Double, V>
{
    ObjectSortedSet<Map.Entry<Double, V>> entrySet();
    
    ObjectSortedSet<Double2ReferenceMap.Entry<V>> double2ReferenceEntrySet();
    
    DoubleSortedSet keySet();
    
    ReferenceCollection<V> values();
    
    DoubleComparator comparator();
    
    Double2ReferenceSortedMap<V> subMap(final Double p0, final Double p1);
    
    Double2ReferenceSortedMap<V> headMap(final Double p0);
    
    Double2ReferenceSortedMap<V> tailMap(final Double p0);
    
    Double2ReferenceSortedMap<V> subMap(final double p0, final double p1);
    
    Double2ReferenceSortedMap<V> headMap(final double p0);
    
    Double2ReferenceSortedMap<V> tailMap(final double p0);
    
    double firstDoubleKey();
    
    double lastDoubleKey();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Double2ReferenceMap.Entry<V>>, FastEntrySet<V>
    {
        ObjectBidirectionalIterator<Double2ReferenceMap.Entry<V>> fastIterator(final Double2ReferenceMap.Entry<V> p0);
    }
}
