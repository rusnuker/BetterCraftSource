// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Char2IntSortedMap extends Char2IntMap, SortedMap<Character, Integer>
{
    ObjectSortedSet<Map.Entry<Character, Integer>> entrySet();
    
    ObjectSortedSet<Char2IntMap.Entry> char2IntEntrySet();
    
    CharSortedSet keySet();
    
    IntCollection values();
    
    CharComparator comparator();
    
    Char2IntSortedMap subMap(final Character p0, final Character p1);
    
    Char2IntSortedMap headMap(final Character p0);
    
    Char2IntSortedMap tailMap(final Character p0);
    
    Char2IntSortedMap subMap(final char p0, final char p1);
    
    Char2IntSortedMap headMap(final char p0);
    
    Char2IntSortedMap tailMap(final char p0);
    
    char firstCharKey();
    
    char lastCharKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Char2IntMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Char2IntMap.Entry> fastIterator(final Char2IntMap.Entry p0);
    }
}
