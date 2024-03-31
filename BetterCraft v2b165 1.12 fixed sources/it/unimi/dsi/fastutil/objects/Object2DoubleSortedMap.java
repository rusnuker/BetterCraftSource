// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Comparator;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Object2DoubleSortedMap<K> extends Object2DoubleMap<K>, SortedMap<K, Double>
{
    ObjectSortedSet<Map.Entry<K, Double>> entrySet();
    
    ObjectSortedSet<Object2DoubleMap.Entry<K>> object2DoubleEntrySet();
    
    ObjectSortedSet<K> keySet();
    
    DoubleCollection values();
    
    Comparator<? super K> comparator();
    
    Object2DoubleSortedMap<K> subMap(final K p0, final K p1);
    
    Object2DoubleSortedMap<K> headMap(final K p0);
    
    Object2DoubleSortedMap<K> tailMap(final K p0);
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Object2DoubleMap.Entry<K>>, FastEntrySet<K>
    {
        ObjectBidirectionalIterator<Object2DoubleMap.Entry<K>> fastIterator(final Object2DoubleMap.Entry<K> p0);
    }
}
