// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Int2BooleanSortedMap extends Int2BooleanMap, SortedMap<Integer, Boolean>
{
    ObjectSortedSet<Map.Entry<Integer, Boolean>> entrySet();
    
    ObjectSortedSet<Int2BooleanMap.Entry> int2BooleanEntrySet();
    
    IntSortedSet keySet();
    
    BooleanCollection values();
    
    IntComparator comparator();
    
    Int2BooleanSortedMap subMap(final Integer p0, final Integer p1);
    
    Int2BooleanSortedMap headMap(final Integer p0);
    
    Int2BooleanSortedMap tailMap(final Integer p0);
    
    Int2BooleanSortedMap subMap(final int p0, final int p1);
    
    Int2BooleanSortedMap headMap(final int p0);
    
    Int2BooleanSortedMap tailMap(final int p0);
    
    int firstIntKey();
    
    int lastIntKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Int2BooleanMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Int2BooleanMap.Entry> fastIterator(final Int2BooleanMap.Entry p0);
    }
}
