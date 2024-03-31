// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Int2ByteSortedMap extends Int2ByteMap, SortedMap<Integer, Byte>
{
    ObjectSortedSet<Map.Entry<Integer, Byte>> entrySet();
    
    ObjectSortedSet<Int2ByteMap.Entry> int2ByteEntrySet();
    
    IntSortedSet keySet();
    
    ByteCollection values();
    
    IntComparator comparator();
    
    Int2ByteSortedMap subMap(final Integer p0, final Integer p1);
    
    Int2ByteSortedMap headMap(final Integer p0);
    
    Int2ByteSortedMap tailMap(final Integer p0);
    
    Int2ByteSortedMap subMap(final int p0, final int p1);
    
    Int2ByteSortedMap headMap(final int p0);
    
    Int2ByteSortedMap tailMap(final int p0);
    
    int firstIntKey();
    
    int lastIntKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Int2ByteMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Int2ByteMap.Entry> fastIterator(final Int2ByteMap.Entry p0);
    }
}
