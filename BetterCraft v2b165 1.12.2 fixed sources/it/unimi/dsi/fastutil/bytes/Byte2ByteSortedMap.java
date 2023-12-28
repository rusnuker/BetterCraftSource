// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Byte2ByteSortedMap extends Byte2ByteMap, SortedMap<Byte, Byte>
{
    ObjectSortedSet<Map.Entry<Byte, Byte>> entrySet();
    
    ObjectSortedSet<Byte2ByteMap.Entry> byte2ByteEntrySet();
    
    ByteSortedSet keySet();
    
    ByteCollection values();
    
    ByteComparator comparator();
    
    Byte2ByteSortedMap subMap(final Byte p0, final Byte p1);
    
    Byte2ByteSortedMap headMap(final Byte p0);
    
    Byte2ByteSortedMap tailMap(final Byte p0);
    
    Byte2ByteSortedMap subMap(final byte p0, final byte p1);
    
    Byte2ByteSortedMap headMap(final byte p0);
    
    Byte2ByteSortedMap tailMap(final byte p0);
    
    byte firstByteKey();
    
    byte lastByteKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Byte2ByteMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Byte2ByteMap.Entry> fastIterator(final Byte2ByteMap.Entry p0);
    }
}
