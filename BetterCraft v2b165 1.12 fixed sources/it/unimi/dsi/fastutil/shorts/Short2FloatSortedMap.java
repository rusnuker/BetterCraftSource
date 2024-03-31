// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Short2FloatSortedMap extends Short2FloatMap, SortedMap<Short, Float>
{
    ObjectSortedSet<Map.Entry<Short, Float>> entrySet();
    
    ObjectSortedSet<Short2FloatMap.Entry> short2FloatEntrySet();
    
    ShortSortedSet keySet();
    
    FloatCollection values();
    
    ShortComparator comparator();
    
    Short2FloatSortedMap subMap(final Short p0, final Short p1);
    
    Short2FloatSortedMap headMap(final Short p0);
    
    Short2FloatSortedMap tailMap(final Short p0);
    
    Short2FloatSortedMap subMap(final short p0, final short p1);
    
    Short2FloatSortedMap headMap(final short p0);
    
    Short2FloatSortedMap tailMap(final short p0);
    
    short firstShortKey();
    
    short lastShortKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Short2FloatMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Short2FloatMap.Entry> fastIterator(final Short2FloatMap.Entry p0);
    }
}
