// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.longs.LongCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Float2LongSortedMap extends Float2LongMap, SortedMap<Float, Long>
{
    ObjectSortedSet<Map.Entry<Float, Long>> entrySet();
    
    ObjectSortedSet<Float2LongMap.Entry> float2LongEntrySet();
    
    FloatSortedSet keySet();
    
    LongCollection values();
    
    FloatComparator comparator();
    
    Float2LongSortedMap subMap(final Float p0, final Float p1);
    
    Float2LongSortedMap headMap(final Float p0);
    
    Float2LongSortedMap tailMap(final Float p0);
    
    Float2LongSortedMap subMap(final float p0, final float p1);
    
    Float2LongSortedMap headMap(final float p0);
    
    Float2LongSortedMap tailMap(final float p0);
    
    float firstFloatKey();
    
    float lastFloatKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Float2LongMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Float2LongMap.Entry> fastIterator(final Float2LongMap.Entry p0);
    }
}
