// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Char2ShortSortedMap extends Char2ShortMap, SortedMap<Character, Short>
{
    ObjectSortedSet<Map.Entry<Character, Short>> entrySet();
    
    ObjectSortedSet<Char2ShortMap.Entry> char2ShortEntrySet();
    
    CharSortedSet keySet();
    
    ShortCollection values();
    
    CharComparator comparator();
    
    Char2ShortSortedMap subMap(final Character p0, final Character p1);
    
    Char2ShortSortedMap headMap(final Character p0);
    
    Char2ShortSortedMap tailMap(final Character p0);
    
    Char2ShortSortedMap subMap(final char p0, final char p1);
    
    Char2ShortSortedMap headMap(final char p0);
    
    Char2ShortSortedMap tailMap(final char p0);
    
    char firstCharKey();
    
    char lastCharKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Char2ShortMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Char2ShortMap.Entry> fastIterator(final Char2ShortMap.Entry p0);
    }
}
