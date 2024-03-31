// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.chars.CharCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Short2CharSortedMap extends Short2CharMap, SortedMap<Short, Character>
{
    ObjectSortedSet<Map.Entry<Short, Character>> entrySet();
    
    ObjectSortedSet<Short2CharMap.Entry> short2CharEntrySet();
    
    ShortSortedSet keySet();
    
    CharCollection values();
    
    ShortComparator comparator();
    
    Short2CharSortedMap subMap(final Short p0, final Short p1);
    
    Short2CharSortedMap headMap(final Short p0);
    
    Short2CharSortedMap tailMap(final Short p0);
    
    Short2CharSortedMap subMap(final short p0, final short p1);
    
    Short2CharSortedMap headMap(final short p0);
    
    Short2CharSortedMap tailMap(final short p0);
    
    short firstShortKey();
    
    short lastShortKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Short2CharMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Short2CharMap.Entry> fastIterator(final Short2CharMap.Entry p0);
    }
}
