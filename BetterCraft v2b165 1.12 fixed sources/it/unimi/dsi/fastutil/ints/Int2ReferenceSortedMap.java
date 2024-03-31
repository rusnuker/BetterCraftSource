// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Int2ReferenceSortedMap<V> extends Int2ReferenceMap<V>, SortedMap<Integer, V>
{
    ObjectSortedSet<Map.Entry<Integer, V>> entrySet();
    
    ObjectSortedSet<Int2ReferenceMap.Entry<V>> int2ReferenceEntrySet();
    
    IntSortedSet keySet();
    
    ReferenceCollection<V> values();
    
    IntComparator comparator();
    
    Int2ReferenceSortedMap<V> subMap(final Integer p0, final Integer p1);
    
    Int2ReferenceSortedMap<V> headMap(final Integer p0);
    
    Int2ReferenceSortedMap<V> tailMap(final Integer p0);
    
    Int2ReferenceSortedMap<V> subMap(final int p0, final int p1);
    
    Int2ReferenceSortedMap<V> headMap(final int p0);
    
    Int2ReferenceSortedMap<V> tailMap(final int p0);
    
    int firstIntKey();
    
    int lastIntKey();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Int2ReferenceMap.Entry<V>>, FastEntrySet<V>
    {
        ObjectBidirectionalIterator<Int2ReferenceMap.Entry<V>> fastIterator(final Int2ReferenceMap.Entry<V> p0);
    }
}
