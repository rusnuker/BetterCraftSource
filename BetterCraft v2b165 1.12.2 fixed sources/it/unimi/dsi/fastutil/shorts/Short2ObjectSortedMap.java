// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Short2ObjectSortedMap<V> extends Short2ObjectMap<V>, SortedMap<Short, V>
{
    ObjectSortedSet<Map.Entry<Short, V>> entrySet();
    
    ObjectSortedSet<Short2ObjectMap.Entry<V>> short2ObjectEntrySet();
    
    ShortSortedSet keySet();
    
    ObjectCollection<V> values();
    
    ShortComparator comparator();
    
    Short2ObjectSortedMap<V> subMap(final Short p0, final Short p1);
    
    Short2ObjectSortedMap<V> headMap(final Short p0);
    
    Short2ObjectSortedMap<V> tailMap(final Short p0);
    
    Short2ObjectSortedMap<V> subMap(final short p0, final short p1);
    
    Short2ObjectSortedMap<V> headMap(final short p0);
    
    Short2ObjectSortedMap<V> tailMap(final short p0);
    
    short firstShortKey();
    
    short lastShortKey();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Short2ObjectMap.Entry<V>>, FastEntrySet<V>
    {
        ObjectBidirectionalIterator<Short2ObjectMap.Entry<V>> fastIterator(final Short2ObjectMap.Entry<V> p0);
    }
}
