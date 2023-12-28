// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Byte2IntSortedMap extends Byte2IntMap, SortedMap<Byte, Integer>
{
    ObjectSortedSet<Map.Entry<Byte, Integer>> entrySet();
    
    ObjectSortedSet<Byte2IntMap.Entry> byte2IntEntrySet();
    
    ByteSortedSet keySet();
    
    IntCollection values();
    
    ByteComparator comparator();
    
    Byte2IntSortedMap subMap(final Byte p0, final Byte p1);
    
    Byte2IntSortedMap headMap(final Byte p0);
    
    Byte2IntSortedMap tailMap(final Byte p0);
    
    Byte2IntSortedMap subMap(final byte p0, final byte p1);
    
    Byte2IntSortedMap headMap(final byte p0);
    
    Byte2IntSortedMap tailMap(final byte p0);
    
    byte firstByteKey();
    
    byte lastByteKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Byte2IntMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Byte2IntMap.Entry> fastIterator(final Byte2IntMap.Entry p0);
    }
}
