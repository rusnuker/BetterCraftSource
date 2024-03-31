// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Char2CharSortedMap extends Char2CharMap, SortedMap<Character, Character>
{
    ObjectSortedSet<Map.Entry<Character, Character>> entrySet();
    
    ObjectSortedSet<Char2CharMap.Entry> char2CharEntrySet();
    
    CharSortedSet keySet();
    
    CharCollection values();
    
    CharComparator comparator();
    
    Char2CharSortedMap subMap(final Character p0, final Character p1);
    
    Char2CharSortedMap headMap(final Character p0);
    
    Char2CharSortedMap tailMap(final Character p0);
    
    Char2CharSortedMap subMap(final char p0, final char p1);
    
    Char2CharSortedMap headMap(final char p0);
    
    Char2CharSortedMap tailMap(final char p0);
    
    char firstCharKey();
    
    char lastCharKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Char2CharMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Char2CharMap.Entry> fastIterator(final Char2CharMap.Entry p0);
    }
}
