// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Comparator;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Object2BooleanSortedMap<K> extends Object2BooleanMap<K>, SortedMap<K, Boolean>
{
    ObjectSortedSet<Map.Entry<K, Boolean>> entrySet();
    
    ObjectSortedSet<Object2BooleanMap.Entry<K>> object2BooleanEntrySet();
    
    ObjectSortedSet<K> keySet();
    
    BooleanCollection values();
    
    Comparator<? super K> comparator();
    
    Object2BooleanSortedMap<K> subMap(final K p0, final K p1);
    
    Object2BooleanSortedMap<K> headMap(final K p0);
    
    Object2BooleanSortedMap<K> tailMap(final K p0);
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Object2BooleanMap.Entry<K>>, FastEntrySet<K>
    {
        ObjectBidirectionalIterator<Object2BooleanMap.Entry<K>> fastIterator(final Object2BooleanMap.Entry<K> p0);
    }
}
