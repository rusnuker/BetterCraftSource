// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.longs.LongCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Char2LongSortedMap extends Char2LongMap, SortedMap<Character, Long>
{
    ObjectSortedSet<Map.Entry<Character, Long>> entrySet();
    
    ObjectSortedSet<Char2LongMap.Entry> char2LongEntrySet();
    
    CharSortedSet keySet();
    
    LongCollection values();
    
    CharComparator comparator();
    
    Char2LongSortedMap subMap(final Character p0, final Character p1);
    
    Char2LongSortedMap headMap(final Character p0);
    
    Char2LongSortedMap tailMap(final Character p0);
    
    Char2LongSortedMap subMap(final char p0, final char p1);
    
    Char2LongSortedMap headMap(final char p0);
    
    Char2LongSortedMap tailMap(final char p0);
    
    char firstCharKey();
    
    char lastCharKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Char2LongMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Char2LongMap.Entry> fastIterator(final Char2LongMap.Entry p0);
    }
}
