// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Short2ByteSortedMap extends Short2ByteMap, SortedMap<Short, Byte>
{
    ObjectSortedSet<Map.Entry<Short, Byte>> entrySet();
    
    ObjectSortedSet<Short2ByteMap.Entry> short2ByteEntrySet();
    
    ShortSortedSet keySet();
    
    ByteCollection values();
    
    ShortComparator comparator();
    
    Short2ByteSortedMap subMap(final Short p0, final Short p1);
    
    Short2ByteSortedMap headMap(final Short p0);
    
    Short2ByteSortedMap tailMap(final Short p0);
    
    Short2ByteSortedMap subMap(final short p0, final short p1);
    
    Short2ByteSortedMap headMap(final short p0);
    
    Short2ByteSortedMap tailMap(final short p0);
    
    short firstShortKey();
    
    short lastShortKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Short2ByteMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Short2ByteMap.Entry> fastIterator(final Short2ByteMap.Entry p0);
    }
}
