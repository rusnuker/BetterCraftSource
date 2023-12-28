// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.chars.CharCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Double2CharSortedMap extends Double2CharMap, SortedMap<Double, Character>
{
    ObjectSortedSet<Map.Entry<Double, Character>> entrySet();
    
    ObjectSortedSet<Double2CharMap.Entry> double2CharEntrySet();
    
    DoubleSortedSet keySet();
    
    CharCollection values();
    
    DoubleComparator comparator();
    
    Double2CharSortedMap subMap(final Double p0, final Double p1);
    
    Double2CharSortedMap headMap(final Double p0);
    
    Double2CharSortedMap tailMap(final Double p0);
    
    Double2CharSortedMap subMap(final double p0, final double p1);
    
    Double2CharSortedMap headMap(final double p0);
    
    Double2CharSortedMap tailMap(final double p0);
    
    double firstDoubleKey();
    
    double lastDoubleKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Double2CharMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Double2CharMap.Entry> fastIterator(final Double2CharMap.Entry p0);
    }
}
