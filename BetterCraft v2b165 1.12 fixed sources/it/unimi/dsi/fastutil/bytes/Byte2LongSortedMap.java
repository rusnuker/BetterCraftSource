// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.longs.LongCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Byte2LongSortedMap extends Byte2LongMap, SortedMap<Byte, Long>
{
    ObjectSortedSet<Map.Entry<Byte, Long>> entrySet();
    
    ObjectSortedSet<Byte2LongMap.Entry> byte2LongEntrySet();
    
    ByteSortedSet keySet();
    
    LongCollection values();
    
    ByteComparator comparator();
    
    Byte2LongSortedMap subMap(final Byte p0, final Byte p1);
    
    Byte2LongSortedMap headMap(final Byte p0);
    
    Byte2LongSortedMap tailMap(final Byte p0);
    
    Byte2LongSortedMap subMap(final byte p0, final byte p1);
    
    Byte2LongSortedMap headMap(final byte p0);
    
    Byte2LongSortedMap tailMap(final byte p0);
    
    byte firstByteKey();
    
    byte lastByteKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Byte2LongMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Byte2LongMap.Entry> fastIterator(final Byte2LongMap.Entry p0);
    }
}
