// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Short2BooleanSortedMap extends Short2BooleanMap, SortedMap<Short, Boolean>
{
    ObjectSortedSet<Map.Entry<Short, Boolean>> entrySet();
    
    ObjectSortedSet<Short2BooleanMap.Entry> short2BooleanEntrySet();
    
    ShortSortedSet keySet();
    
    BooleanCollection values();
    
    ShortComparator comparator();
    
    Short2BooleanSortedMap subMap(final Short p0, final Short p1);
    
    Short2BooleanSortedMap headMap(final Short p0);
    
    Short2BooleanSortedMap tailMap(final Short p0);
    
    Short2BooleanSortedMap subMap(final short p0, final short p1);
    
    Short2BooleanSortedMap headMap(final short p0);
    
    Short2BooleanSortedMap tailMap(final short p0);
    
    short firstShortKey();
    
    short lastShortKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Short2BooleanMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Short2BooleanMap.Entry> fastIterator(final Short2BooleanMap.Entry p0);
    }
}
