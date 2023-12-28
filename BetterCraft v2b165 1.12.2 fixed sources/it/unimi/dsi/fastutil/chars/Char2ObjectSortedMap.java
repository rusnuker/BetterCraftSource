// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Char2ObjectSortedMap<V> extends Char2ObjectMap<V>, SortedMap<Character, V>
{
    ObjectSortedSet<Map.Entry<Character, V>> entrySet();
    
    ObjectSortedSet<Char2ObjectMap.Entry<V>> char2ObjectEntrySet();
    
    CharSortedSet keySet();
    
    ObjectCollection<V> values();
    
    CharComparator comparator();
    
    Char2ObjectSortedMap<V> subMap(final Character p0, final Character p1);
    
    Char2ObjectSortedMap<V> headMap(final Character p0);
    
    Char2ObjectSortedMap<V> tailMap(final Character p0);
    
    Char2ObjectSortedMap<V> subMap(final char p0, final char p1);
    
    Char2ObjectSortedMap<V> headMap(final char p0);
    
    Char2ObjectSortedMap<V> tailMap(final char p0);
    
    char firstCharKey();
    
    char lastCharKey();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Char2ObjectMap.Entry<V>>, FastEntrySet<V>
    {
        ObjectBidirectionalIterator<Char2ObjectMap.Entry<V>> fastIterator(final Char2ObjectMap.Entry<V> p0);
    }
}
