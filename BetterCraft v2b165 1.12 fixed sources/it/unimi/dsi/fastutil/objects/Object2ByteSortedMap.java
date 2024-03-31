// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Comparator;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Object2ByteSortedMap<K> extends Object2ByteMap<K>, SortedMap<K, Byte>
{
    ObjectSortedSet<Map.Entry<K, Byte>> entrySet();
    
    ObjectSortedSet<Object2ByteMap.Entry<K>> object2ByteEntrySet();
    
    ObjectSortedSet<K> keySet();
    
    ByteCollection values();
    
    Comparator<? super K> comparator();
    
    Object2ByteSortedMap<K> subMap(final K p0, final K p1);
    
    Object2ByteSortedMap<K> headMap(final K p0);
    
    Object2ByteSortedMap<K> tailMap(final K p0);
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Object2ByteMap.Entry<K>>, FastEntrySet<K>
    {
        ObjectBidirectionalIterator<Object2ByteMap.Entry<K>> fastIterator(final Object2ByteMap.Entry<K> p0);
    }
}
