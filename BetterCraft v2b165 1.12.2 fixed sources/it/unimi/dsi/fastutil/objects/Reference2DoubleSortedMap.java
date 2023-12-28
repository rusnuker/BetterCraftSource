// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Comparator;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Reference2DoubleSortedMap<K> extends Reference2DoubleMap<K>, SortedMap<K, Double>
{
    ObjectSortedSet<Map.Entry<K, Double>> entrySet();
    
    ObjectSortedSet<Reference2DoubleMap.Entry<K>> reference2DoubleEntrySet();
    
    ReferenceSortedSet<K> keySet();
    
    DoubleCollection values();
    
    Comparator<? super K> comparator();
    
    Reference2DoubleSortedMap<K> subMap(final K p0, final K p1);
    
    Reference2DoubleSortedMap<K> headMap(final K p0);
    
    Reference2DoubleSortedMap<K> tailMap(final K p0);
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Reference2DoubleMap.Entry<K>>, FastEntrySet<K>
    {
        ObjectBidirectionalIterator<Reference2DoubleMap.Entry<K>> fastIterator(final Reference2DoubleMap.Entry<K> p0);
    }
}
