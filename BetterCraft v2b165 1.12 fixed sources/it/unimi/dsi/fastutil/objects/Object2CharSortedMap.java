// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Comparator;
import it.unimi.dsi.fastutil.chars.CharCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Object2CharSortedMap<K> extends Object2CharMap<K>, SortedMap<K, Character>
{
    ObjectSortedSet<Map.Entry<K, Character>> entrySet();
    
    ObjectSortedSet<Object2CharMap.Entry<K>> object2CharEntrySet();
    
    ObjectSortedSet<K> keySet();
    
    CharCollection values();
    
    Comparator<? super K> comparator();
    
    Object2CharSortedMap<K> subMap(final K p0, final K p1);
    
    Object2CharSortedMap<K> headMap(final K p0);
    
    Object2CharSortedMap<K> tailMap(final K p0);
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Object2CharMap.Entry<K>>, FastEntrySet<K>
    {
        ObjectBidirectionalIterator<Object2CharMap.Entry<K>> fastIterator(final Object2CharMap.Entry<K> p0);
    }
}
