// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Short2DoubleSortedMap extends Short2DoubleMap, SortedMap<Short, Double>
{
    ObjectSortedSet<Map.Entry<Short, Double>> entrySet();
    
    ObjectSortedSet<Short2DoubleMap.Entry> short2DoubleEntrySet();
    
    ShortSortedSet keySet();
    
    DoubleCollection values();
    
    ShortComparator comparator();
    
    Short2DoubleSortedMap subMap(final Short p0, final Short p1);
    
    Short2DoubleSortedMap headMap(final Short p0);
    
    Short2DoubleSortedMap tailMap(final Short p0);
    
    Short2DoubleSortedMap subMap(final short p0, final short p1);
    
    Short2DoubleSortedMap headMap(final short p0);
    
    Short2DoubleSortedMap tailMap(final short p0);
    
    short firstShortKey();
    
    short lastShortKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Short2DoubleMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Short2DoubleMap.Entry> fastIterator(final Short2DoubleMap.Entry p0);
    }
}
