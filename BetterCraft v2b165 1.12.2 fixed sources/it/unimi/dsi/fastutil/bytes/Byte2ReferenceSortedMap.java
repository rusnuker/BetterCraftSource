// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Byte2ReferenceSortedMap<V> extends Byte2ReferenceMap<V>, SortedMap<Byte, V>
{
    ObjectSortedSet<Map.Entry<Byte, V>> entrySet();
    
    ObjectSortedSet<Byte2ReferenceMap.Entry<V>> byte2ReferenceEntrySet();
    
    ByteSortedSet keySet();
    
    ReferenceCollection<V> values();
    
    ByteComparator comparator();
    
    Byte2ReferenceSortedMap<V> subMap(final Byte p0, final Byte p1);
    
    Byte2ReferenceSortedMap<V> headMap(final Byte p0);
    
    Byte2ReferenceSortedMap<V> tailMap(final Byte p0);
    
    Byte2ReferenceSortedMap<V> subMap(final byte p0, final byte p1);
    
    Byte2ReferenceSortedMap<V> headMap(final byte p0);
    
    Byte2ReferenceSortedMap<V> tailMap(final byte p0);
    
    byte firstByteKey();
    
    byte lastByteKey();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Byte2ReferenceMap.Entry<V>>, FastEntrySet<V>
    {
        ObjectBidirectionalIterator<Byte2ReferenceMap.Entry<V>> fastIterator(final Byte2ReferenceMap.Entry<V> p0);
    }
}
