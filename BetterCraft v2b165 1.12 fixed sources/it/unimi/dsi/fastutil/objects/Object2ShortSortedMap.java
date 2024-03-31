// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Comparator;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Object2ShortSortedMap<K> extends Object2ShortMap<K>, SortedMap<K, Short>
{
    ObjectSortedSet<Map.Entry<K, Short>> entrySet();
    
    ObjectSortedSet<Object2ShortMap.Entry<K>> object2ShortEntrySet();
    
    ObjectSortedSet<K> keySet();
    
    ShortCollection values();
    
    Comparator<? super K> comparator();
    
    Object2ShortSortedMap<K> subMap(final K p0, final K p1);
    
    Object2ShortSortedMap<K> headMap(final K p0);
    
    Object2ShortSortedMap<K> tailMap(final K p0);
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Object2ShortMap.Entry<K>>, FastEntrySet<K>
    {
        ObjectBidirectionalIterator<Object2ShortMap.Entry<K>> fastIterator(final Object2ShortMap.Entry<K> p0);
    }
}
