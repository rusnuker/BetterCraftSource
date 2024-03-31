// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Comparator;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Reference2FloatSortedMap<K> extends Reference2FloatMap<K>, SortedMap<K, Float>
{
    ObjectSortedSet<Map.Entry<K, Float>> entrySet();
    
    ObjectSortedSet<Reference2FloatMap.Entry<K>> reference2FloatEntrySet();
    
    ReferenceSortedSet<K> keySet();
    
    FloatCollection values();
    
    Comparator<? super K> comparator();
    
    Reference2FloatSortedMap<K> subMap(final K p0, final K p1);
    
    Reference2FloatSortedMap<K> headMap(final K p0);
    
    Reference2FloatSortedMap<K> tailMap(final K p0);
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Reference2FloatMap.Entry<K>>, FastEntrySet<K>
    {
        ObjectBidirectionalIterator<Reference2FloatMap.Entry<K>> fastIterator(final Reference2FloatMap.Entry<K> p0);
    }
}
