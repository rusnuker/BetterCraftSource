// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Comparator;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Reference2BooleanSortedMap<K> extends Reference2BooleanMap<K>, SortedMap<K, Boolean>
{
    ObjectSortedSet<Map.Entry<K, Boolean>> entrySet();
    
    ObjectSortedSet<Reference2BooleanMap.Entry<K>> reference2BooleanEntrySet();
    
    ReferenceSortedSet<K> keySet();
    
    BooleanCollection values();
    
    Comparator<? super K> comparator();
    
    Reference2BooleanSortedMap<K> subMap(final K p0, final K p1);
    
    Reference2BooleanSortedMap<K> headMap(final K p0);
    
    Reference2BooleanSortedMap<K> tailMap(final K p0);
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Reference2BooleanMap.Entry<K>>, FastEntrySet<K>
    {
        ObjectBidirectionalIterator<Reference2BooleanMap.Entry<K>> fastIterator(final Reference2BooleanMap.Entry<K> p0);
    }
}
