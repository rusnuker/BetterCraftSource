// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.chars.CharCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Int2CharSortedMap extends Int2CharMap, SortedMap<Integer, Character>
{
    ObjectSortedSet<Map.Entry<Integer, Character>> entrySet();
    
    ObjectSortedSet<Int2CharMap.Entry> int2CharEntrySet();
    
    IntSortedSet keySet();
    
    CharCollection values();
    
    IntComparator comparator();
    
    Int2CharSortedMap subMap(final Integer p0, final Integer p1);
    
    Int2CharSortedMap headMap(final Integer p0);
    
    Int2CharSortedMap tailMap(final Integer p0);
    
    Int2CharSortedMap subMap(final int p0, final int p1);
    
    Int2CharSortedMap headMap(final int p0);
    
    Int2CharSortedMap tailMap(final int p0);
    
    int firstIntKey();
    
    int lastIntKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Int2CharMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Int2CharMap.Entry> fastIterator(final Int2CharMap.Entry p0);
    }
}
