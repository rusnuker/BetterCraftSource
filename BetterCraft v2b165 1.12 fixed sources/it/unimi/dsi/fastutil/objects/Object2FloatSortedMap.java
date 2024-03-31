// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Comparator;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Object2FloatSortedMap<K> extends Object2FloatMap<K>, SortedMap<K, Float>
{
    ObjectSortedSet<Map.Entry<K, Float>> entrySet();
    
    ObjectSortedSet<Object2FloatMap.Entry<K>> object2FloatEntrySet();
    
    ObjectSortedSet<K> keySet();
    
    FloatCollection values();
    
    Comparator<? super K> comparator();
    
    Object2FloatSortedMap<K> subMap(final K p0, final K p1);
    
    Object2FloatSortedMap<K> headMap(final K p0);
    
    Object2FloatSortedMap<K> tailMap(final K p0);
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Object2FloatMap.Entry<K>>, FastEntrySet<K>
    {
        ObjectBidirectionalIterator<Object2FloatMap.Entry<K>> fastIterator(final Object2FloatMap.Entry<K> p0);
    }
}
