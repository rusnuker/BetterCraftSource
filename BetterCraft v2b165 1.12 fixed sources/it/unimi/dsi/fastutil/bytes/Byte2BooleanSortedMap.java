// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Byte2BooleanSortedMap extends Byte2BooleanMap, SortedMap<Byte, Boolean>
{
    ObjectSortedSet<Map.Entry<Byte, Boolean>> entrySet();
    
    ObjectSortedSet<Byte2BooleanMap.Entry> byte2BooleanEntrySet();
    
    ByteSortedSet keySet();
    
    BooleanCollection values();
    
    ByteComparator comparator();
    
    Byte2BooleanSortedMap subMap(final Byte p0, final Byte p1);
    
    Byte2BooleanSortedMap headMap(final Byte p0);
    
    Byte2BooleanSortedMap tailMap(final Byte p0);
    
    Byte2BooleanSortedMap subMap(final byte p0, final byte p1);
    
    Byte2BooleanSortedMap headMap(final byte p0);
    
    Byte2BooleanSortedMap tailMap(final byte p0);
    
    byte firstByteKey();
    
    byte lastByteKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Byte2BooleanMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Byte2BooleanMap.Entry> fastIterator(final Byte2BooleanMap.Entry p0);
    }
}
