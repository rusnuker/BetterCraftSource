// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Byte2ObjectSortedMap<V> extends Byte2ObjectMap<V>, SortedMap<Byte, V>
{
    ObjectSortedSet<Map.Entry<Byte, V>> entrySet();
    
    ObjectSortedSet<Byte2ObjectMap.Entry<V>> byte2ObjectEntrySet();
    
    ByteSortedSet keySet();
    
    ObjectCollection<V> values();
    
    ByteComparator comparator();
    
    Byte2ObjectSortedMap<V> subMap(final Byte p0, final Byte p1);
    
    Byte2ObjectSortedMap<V> headMap(final Byte p0);
    
    Byte2ObjectSortedMap<V> tailMap(final Byte p0);
    
    Byte2ObjectSortedMap<V> subMap(final byte p0, final byte p1);
    
    Byte2ObjectSortedMap<V> headMap(final byte p0);
    
    Byte2ObjectSortedMap<V> tailMap(final byte p0);
    
    byte firstByteKey();
    
    byte lastByteKey();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Byte2ObjectMap.Entry<V>>, FastEntrySet<V>
    {
        ObjectBidirectionalIterator<Byte2ObjectMap.Entry<V>> fastIterator(final Byte2ObjectMap.Entry<V> p0);
    }
}
