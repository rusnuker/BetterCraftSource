// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.chars.CharCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Byte2CharSortedMap extends Byte2CharMap, SortedMap<Byte, Character>
{
    ObjectSortedSet<Map.Entry<Byte, Character>> entrySet();
    
    ObjectSortedSet<Byte2CharMap.Entry> byte2CharEntrySet();
    
    ByteSortedSet keySet();
    
    CharCollection values();
    
    ByteComparator comparator();
    
    Byte2CharSortedMap subMap(final Byte p0, final Byte p1);
    
    Byte2CharSortedMap headMap(final Byte p0);
    
    Byte2CharSortedMap tailMap(final Byte p0);
    
    Byte2CharSortedMap subMap(final byte p0, final byte p1);
    
    Byte2CharSortedMap headMap(final byte p0);
    
    Byte2CharSortedMap tailMap(final byte p0);
    
    byte firstByteKey();
    
    byte lastByteKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Byte2CharMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Byte2CharMap.Entry> fastIterator(final Byte2CharMap.Entry p0);
    }
}
