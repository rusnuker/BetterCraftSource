// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Float2IntSortedMap extends Float2IntMap, SortedMap<Float, Integer>
{
    ObjectSortedSet<Map.Entry<Float, Integer>> entrySet();
    
    ObjectSortedSet<Float2IntMap.Entry> float2IntEntrySet();
    
    FloatSortedSet keySet();
    
    IntCollection values();
    
    FloatComparator comparator();
    
    Float2IntSortedMap subMap(final Float p0, final Float p1);
    
    Float2IntSortedMap headMap(final Float p0);
    
    Float2IntSortedMap tailMap(final Float p0);
    
    Float2IntSortedMap subMap(final float p0, final float p1);
    
    Float2IntSortedMap headMap(final float p0);
    
    Float2IntSortedMap tailMap(final float p0);
    
    float firstFloatKey();
    
    float lastFloatKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Float2IntMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Float2IntMap.Entry> fastIterator(final Float2IntMap.Entry p0);
    }
}
