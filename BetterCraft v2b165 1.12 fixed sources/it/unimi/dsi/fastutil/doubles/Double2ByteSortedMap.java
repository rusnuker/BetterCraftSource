// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Double2ByteSortedMap extends Double2ByteMap, SortedMap<Double, Byte>
{
    ObjectSortedSet<Map.Entry<Double, Byte>> entrySet();
    
    ObjectSortedSet<Double2ByteMap.Entry> double2ByteEntrySet();
    
    DoubleSortedSet keySet();
    
    ByteCollection values();
    
    DoubleComparator comparator();
    
    Double2ByteSortedMap subMap(final Double p0, final Double p1);
    
    Double2ByteSortedMap headMap(final Double p0);
    
    Double2ByteSortedMap tailMap(final Double p0);
    
    Double2ByteSortedMap subMap(final double p0, final double p1);
    
    Double2ByteSortedMap headMap(final double p0);
    
    Double2ByteSortedMap tailMap(final double p0);
    
    double firstDoubleKey();
    
    double lastDoubleKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Double2ByteMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Double2ByteMap.Entry> fastIterator(final Double2ByteMap.Entry p0);
    }
}
