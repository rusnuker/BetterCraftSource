// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Byte2FloatSortedMap extends Byte2FloatMap, SortedMap<Byte, Float>
{
    ObjectSortedSet<Map.Entry<Byte, Float>> entrySet();
    
    ObjectSortedSet<Byte2FloatMap.Entry> byte2FloatEntrySet();
    
    ByteSortedSet keySet();
    
    FloatCollection values();
    
    ByteComparator comparator();
    
    Byte2FloatSortedMap subMap(final Byte p0, final Byte p1);
    
    Byte2FloatSortedMap headMap(final Byte p0);
    
    Byte2FloatSortedMap tailMap(final Byte p0);
    
    Byte2FloatSortedMap subMap(final byte p0, final byte p1);
    
    Byte2FloatSortedMap headMap(final byte p0);
    
    Byte2FloatSortedMap tailMap(final byte p0);
    
    byte firstByteKey();
    
    byte lastByteKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Byte2FloatMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Byte2FloatMap.Entry> fastIterator(final Byte2FloatMap.Entry p0);
    }
}
