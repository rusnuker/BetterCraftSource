// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Float2DoubleSortedMap extends Float2DoubleMap, SortedMap<Float, Double>
{
    ObjectSortedSet<Map.Entry<Float, Double>> entrySet();
    
    ObjectSortedSet<Float2DoubleMap.Entry> float2DoubleEntrySet();
    
    FloatSortedSet keySet();
    
    DoubleCollection values();
    
    FloatComparator comparator();
    
    Float2DoubleSortedMap subMap(final Float p0, final Float p1);
    
    Float2DoubleSortedMap headMap(final Float p0);
    
    Float2DoubleSortedMap tailMap(final Float p0);
    
    Float2DoubleSortedMap subMap(final float p0, final float p1);
    
    Float2DoubleSortedMap headMap(final float p0);
    
    Float2DoubleSortedMap tailMap(final float p0);
    
    float firstFloatKey();
    
    float lastFloatKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Float2DoubleMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Float2DoubleMap.Entry> fastIterator(final Float2DoubleMap.Entry p0);
    }
}
