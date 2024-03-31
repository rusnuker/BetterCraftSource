// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Comparator;
import it.unimi.dsi.fastutil.chars.CharCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Reference2CharSortedMap<K> extends Reference2CharMap<K>, SortedMap<K, Character>
{
    ObjectSortedSet<Map.Entry<K, Character>> entrySet();
    
    ObjectSortedSet<Reference2CharMap.Entry<K>> reference2CharEntrySet();
    
    ReferenceSortedSet<K> keySet();
    
    CharCollection values();
    
    Comparator<? super K> comparator();
    
    Reference2CharSortedMap<K> subMap(final K p0, final K p1);
    
    Reference2CharSortedMap<K> headMap(final K p0);
    
    Reference2CharSortedMap<K> tailMap(final K p0);
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Reference2CharMap.Entry<K>>, FastEntrySet<K>
    {
        ObjectBidirectionalIterator<Reference2CharMap.Entry<K>> fastIterator(final Reference2CharMap.Entry<K> p0);
    }
}
