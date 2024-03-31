// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Byte2ShortSortedMap extends Byte2ShortMap, SortedMap<Byte, Short>
{
    ObjectSortedSet<Map.Entry<Byte, Short>> entrySet();
    
    ObjectSortedSet<Byte2ShortMap.Entry> byte2ShortEntrySet();
    
    ByteSortedSet keySet();
    
    ShortCollection values();
    
    ByteComparator comparator();
    
    Byte2ShortSortedMap subMap(final Byte p0, final Byte p1);
    
    Byte2ShortSortedMap headMap(final Byte p0);
    
    Byte2ShortSortedMap tailMap(final Byte p0);
    
    Byte2ShortSortedMap subMap(final byte p0, final byte p1);
    
    Byte2ShortSortedMap headMap(final byte p0);
    
    Byte2ShortSortedMap tailMap(final byte p0);
    
    byte firstByteKey();
    
    byte lastByteKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Byte2ShortMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Byte2ShortMap.Entry> fastIterator(final Byte2ShortMap.Entry p0);
    }
}
