// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Byte2DoubleSortedMap extends Byte2DoubleMap, SortedMap<Byte, Double>
{
    ObjectSortedSet<Map.Entry<Byte, Double>> entrySet();
    
    ObjectSortedSet<Byte2DoubleMap.Entry> byte2DoubleEntrySet();
    
    ByteSortedSet keySet();
    
    DoubleCollection values();
    
    ByteComparator comparator();
    
    Byte2DoubleSortedMap subMap(final Byte p0, final Byte p1);
    
    Byte2DoubleSortedMap headMap(final Byte p0);
    
    Byte2DoubleSortedMap tailMap(final Byte p0);
    
    Byte2DoubleSortedMap subMap(final byte p0, final byte p1);
    
    Byte2DoubleSortedMap headMap(final byte p0);
    
    Byte2DoubleSortedMap tailMap(final byte p0);
    
    byte firstByteKey();
    
    byte lastByteKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Byte2DoubleMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Byte2DoubleMap.Entry> fastIterator(final Byte2DoubleMap.Entry p0);
    }
}
