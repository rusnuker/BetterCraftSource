// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Float2FloatSortedMap extends Float2FloatMap, SortedMap<Float, Float>
{
    ObjectSortedSet<Map.Entry<Float, Float>> entrySet();
    
    ObjectSortedSet<Float2FloatMap.Entry> float2FloatEntrySet();
    
    FloatSortedSet keySet();
    
    FloatCollection values();
    
    FloatComparator comparator();
    
    Float2FloatSortedMap subMap(final Float p0, final Float p1);
    
    Float2FloatSortedMap headMap(final Float p0);
    
    Float2FloatSortedMap tailMap(final Float p0);
    
    Float2FloatSortedMap subMap(final float p0, final float p1);
    
    Float2FloatSortedMap headMap(final float p0);
    
    Float2FloatSortedMap tailMap(final float p0);
    
    float firstFloatKey();
    
    float lastFloatKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Float2FloatMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Float2FloatMap.Entry> fastIterator(final Float2FloatMap.Entry p0);
    }
}
