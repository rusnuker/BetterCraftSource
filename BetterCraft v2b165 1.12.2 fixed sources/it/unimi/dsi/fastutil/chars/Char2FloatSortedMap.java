// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Char2FloatSortedMap extends Char2FloatMap, SortedMap<Character, Float>
{
    ObjectSortedSet<Map.Entry<Character, Float>> entrySet();
    
    ObjectSortedSet<Char2FloatMap.Entry> char2FloatEntrySet();
    
    CharSortedSet keySet();
    
    FloatCollection values();
    
    CharComparator comparator();
    
    Char2FloatSortedMap subMap(final Character p0, final Character p1);
    
    Char2FloatSortedMap headMap(final Character p0);
    
    Char2FloatSortedMap tailMap(final Character p0);
    
    Char2FloatSortedMap subMap(final char p0, final char p1);
    
    Char2FloatSortedMap headMap(final char p0);
    
    Char2FloatSortedMap tailMap(final char p0);
    
    char firstCharKey();
    
    char lastCharKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Char2FloatMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Char2FloatMap.Entry> fastIterator(final Char2FloatMap.Entry p0);
    }
}
