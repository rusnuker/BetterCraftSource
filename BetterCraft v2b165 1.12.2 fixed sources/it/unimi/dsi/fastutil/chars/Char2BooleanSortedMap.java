// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Char2BooleanSortedMap extends Char2BooleanMap, SortedMap<Character, Boolean>
{
    ObjectSortedSet<Map.Entry<Character, Boolean>> entrySet();
    
    ObjectSortedSet<Char2BooleanMap.Entry> char2BooleanEntrySet();
    
    CharSortedSet keySet();
    
    BooleanCollection values();
    
    CharComparator comparator();
    
    Char2BooleanSortedMap subMap(final Character p0, final Character p1);
    
    Char2BooleanSortedMap headMap(final Character p0);
    
    Char2BooleanSortedMap tailMap(final Character p0);
    
    Char2BooleanSortedMap subMap(final char p0, final char p1);
    
    Char2BooleanSortedMap headMap(final char p0);
    
    Char2BooleanSortedMap tailMap(final char p0);
    
    char firstCharKey();
    
    char lastCharKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Char2BooleanMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Char2BooleanMap.Entry> fastIterator(final Char2BooleanMap.Entry p0);
    }
}
