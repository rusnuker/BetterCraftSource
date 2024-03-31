// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Short2ReferenceSortedMap<V> extends Short2ReferenceMap<V>, SortedMap<Short, V>
{
    ObjectSortedSet<Map.Entry<Short, V>> entrySet();
    
    ObjectSortedSet<Short2ReferenceMap.Entry<V>> short2ReferenceEntrySet();
    
    ShortSortedSet keySet();
    
    ReferenceCollection<V> values();
    
    ShortComparator comparator();
    
    Short2ReferenceSortedMap<V> subMap(final Short p0, final Short p1);
    
    Short2ReferenceSortedMap<V> headMap(final Short p0);
    
    Short2ReferenceSortedMap<V> tailMap(final Short p0);
    
    Short2ReferenceSortedMap<V> subMap(final short p0, final short p1);
    
    Short2ReferenceSortedMap<V> headMap(final short p0);
    
    Short2ReferenceSortedMap<V> tailMap(final short p0);
    
    short firstShortKey();
    
    short lastShortKey();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Short2ReferenceMap.Entry<V>>, FastEntrySet<V>
    {
        ObjectBidirectionalIterator<Short2ReferenceMap.Entry<V>> fastIterator(final Short2ReferenceMap.Entry<V> p0);
    }
}
