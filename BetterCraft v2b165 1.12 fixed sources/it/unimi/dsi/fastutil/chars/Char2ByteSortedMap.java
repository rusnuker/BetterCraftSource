// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Char2ByteSortedMap extends Char2ByteMap, SortedMap<Character, Byte>
{
    ObjectSortedSet<Map.Entry<Character, Byte>> entrySet();
    
    ObjectSortedSet<Char2ByteMap.Entry> char2ByteEntrySet();
    
    CharSortedSet keySet();
    
    ByteCollection values();
    
    CharComparator comparator();
    
    Char2ByteSortedMap subMap(final Character p0, final Character p1);
    
    Char2ByteSortedMap headMap(final Character p0);
    
    Char2ByteSortedMap tailMap(final Character p0);
    
    Char2ByteSortedMap subMap(final char p0, final char p1);
    
    Char2ByteSortedMap headMap(final char p0);
    
    Char2ByteSortedMap tailMap(final char p0);
    
    char firstCharKey();
    
    char lastCharKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Char2ByteMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Char2ByteMap.Entry> fastIterator(final Char2ByteMap.Entry p0);
    }
}
