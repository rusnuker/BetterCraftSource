// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Comparator;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Reference2ByteSortedMap<K> extends Reference2ByteMap<K>, SortedMap<K, Byte>
{
    ObjectSortedSet<Map.Entry<K, Byte>> entrySet();
    
    ObjectSortedSet<Reference2ByteMap.Entry<K>> reference2ByteEntrySet();
    
    ReferenceSortedSet<K> keySet();
    
    ByteCollection values();
    
    Comparator<? super K> comparator();
    
    Reference2ByteSortedMap<K> subMap(final K p0, final K p1);
    
    Reference2ByteSortedMap<K> headMap(final K p0);
    
    Reference2ByteSortedMap<K> tailMap(final K p0);
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Reference2ByteMap.Entry<K>>, FastEntrySet<K>
    {
        ObjectBidirectionalIterator<Reference2ByteMap.Entry<K>> fastIterator(final Reference2ByteMap.Entry<K> p0);
    }
}
