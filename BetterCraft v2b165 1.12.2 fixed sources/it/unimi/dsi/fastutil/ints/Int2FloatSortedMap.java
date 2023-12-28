// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Int2FloatSortedMap extends Int2FloatMap, SortedMap<Integer, Float>
{
    ObjectSortedSet<Map.Entry<Integer, Float>> entrySet();
    
    ObjectSortedSet<Int2FloatMap.Entry> int2FloatEntrySet();
    
    IntSortedSet keySet();
    
    FloatCollection values();
    
    IntComparator comparator();
    
    Int2FloatSortedMap subMap(final Integer p0, final Integer p1);
    
    Int2FloatSortedMap headMap(final Integer p0);
    
    Int2FloatSortedMap tailMap(final Integer p0);
    
    Int2FloatSortedMap subMap(final int p0, final int p1);
    
    Int2FloatSortedMap headMap(final int p0);
    
    Int2FloatSortedMap tailMap(final int p0);
    
    int firstIntKey();
    
    int lastIntKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Int2FloatMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Int2FloatMap.Entry> fastIterator(final Int2FloatMap.Entry p0);
    }
}
