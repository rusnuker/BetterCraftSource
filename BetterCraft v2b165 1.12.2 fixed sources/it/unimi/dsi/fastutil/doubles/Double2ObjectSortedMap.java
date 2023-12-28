// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Double2ObjectSortedMap<V> extends Double2ObjectMap<V>, SortedMap<Double, V>
{
    ObjectSortedSet<Map.Entry<Double, V>> entrySet();
    
    ObjectSortedSet<Double2ObjectMap.Entry<V>> double2ObjectEntrySet();
    
    DoubleSortedSet keySet();
    
    ObjectCollection<V> values();
    
    DoubleComparator comparator();
    
    Double2ObjectSortedMap<V> subMap(final Double p0, final Double p1);
    
    Double2ObjectSortedMap<V> headMap(final Double p0);
    
    Double2ObjectSortedMap<V> tailMap(final Double p0);
    
    Double2ObjectSortedMap<V> subMap(final double p0, final double p1);
    
    Double2ObjectSortedMap<V> headMap(final double p0);
    
    Double2ObjectSortedMap<V> tailMap(final double p0);
    
    double firstDoubleKey();
    
    double lastDoubleKey();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Double2ObjectMap.Entry<V>>, FastEntrySet<V>
    {
        ObjectBidirectionalIterator<Double2ObjectMap.Entry<V>> fastIterator(final Double2ObjectMap.Entry<V> p0);
    }
}
