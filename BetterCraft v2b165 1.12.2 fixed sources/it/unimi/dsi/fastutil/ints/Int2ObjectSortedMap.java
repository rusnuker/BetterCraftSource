// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Int2ObjectSortedMap<V> extends Int2ObjectMap<V>, SortedMap<Integer, V>
{
    ObjectSortedSet<Map.Entry<Integer, V>> entrySet();
    
    ObjectSortedSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet();
    
    IntSortedSet keySet();
    
    ObjectCollection<V> values();
    
    IntComparator comparator();
    
    Int2ObjectSortedMap<V> subMap(final Integer p0, final Integer p1);
    
    Int2ObjectSortedMap<V> headMap(final Integer p0);
    
    Int2ObjectSortedMap<V> tailMap(final Integer p0);
    
    Int2ObjectSortedMap<V> subMap(final int p0, final int p1);
    
    Int2ObjectSortedMap<V> headMap(final int p0);
    
    Int2ObjectSortedMap<V> tailMap(final int p0);
    
    int firstIntKey();
    
    int lastIntKey();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Int2ObjectMap.Entry<V>>, FastEntrySet<V>
    {
        ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> fastIterator(final Int2ObjectMap.Entry<V> p0);
    }
}
