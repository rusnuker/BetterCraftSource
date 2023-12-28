// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Char2ReferenceSortedMap<V> extends Char2ReferenceMap<V>, SortedMap<Character, V>
{
    ObjectSortedSet<Map.Entry<Character, V>> entrySet();
    
    ObjectSortedSet<Char2ReferenceMap.Entry<V>> char2ReferenceEntrySet();
    
    CharSortedSet keySet();
    
    ReferenceCollection<V> values();
    
    CharComparator comparator();
    
    Char2ReferenceSortedMap<V> subMap(final Character p0, final Character p1);
    
    Char2ReferenceSortedMap<V> headMap(final Character p0);
    
    Char2ReferenceSortedMap<V> tailMap(final Character p0);
    
    Char2ReferenceSortedMap<V> subMap(final char p0, final char p1);
    
    Char2ReferenceSortedMap<V> headMap(final char p0);
    
    Char2ReferenceSortedMap<V> tailMap(final char p0);
    
    char firstCharKey();
    
    char lastCharKey();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Char2ReferenceMap.Entry<V>>, FastEntrySet<V>
    {
        ObjectBidirectionalIterator<Char2ReferenceMap.Entry<V>> fastIterator(final Char2ReferenceMap.Entry<V> p0);
    }
}
