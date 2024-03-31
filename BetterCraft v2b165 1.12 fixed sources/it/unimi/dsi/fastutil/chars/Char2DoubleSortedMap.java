// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Char2DoubleSortedMap extends Char2DoubleMap, SortedMap<Character, Double>
{
    ObjectSortedSet<Map.Entry<Character, Double>> entrySet();
    
    ObjectSortedSet<Char2DoubleMap.Entry> char2DoubleEntrySet();
    
    CharSortedSet keySet();
    
    DoubleCollection values();
    
    CharComparator comparator();
    
    Char2DoubleSortedMap subMap(final Character p0, final Character p1);
    
    Char2DoubleSortedMap headMap(final Character p0);
    
    Char2DoubleSortedMap tailMap(final Character p0);
    
    Char2DoubleSortedMap subMap(final char p0, final char p1);
    
    Char2DoubleSortedMap headMap(final char p0);
    
    Char2DoubleSortedMap tailMap(final char p0);
    
    char firstCharKey();
    
    char lastCharKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Char2DoubleMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Char2DoubleMap.Entry> fastIterator(final Char2DoubleMap.Entry p0);
    }
}
