// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Float2ShortSortedMap extends Float2ShortMap, SortedMap<Float, Short>
{
    ObjectSortedSet<Map.Entry<Float, Short>> entrySet();
    
    ObjectSortedSet<Float2ShortMap.Entry> float2ShortEntrySet();
    
    FloatSortedSet keySet();
    
    ShortCollection values();
    
    FloatComparator comparator();
    
    Float2ShortSortedMap subMap(final Float p0, final Float p1);
    
    Float2ShortSortedMap headMap(final Float p0);
    
    Float2ShortSortedMap tailMap(final Float p0);
    
    Float2ShortSortedMap subMap(final float p0, final float p1);
    
    Float2ShortSortedMap headMap(final float p0);
    
    Float2ShortSortedMap tailMap(final float p0);
    
    float firstFloatKey();
    
    float lastFloatKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Float2ShortMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Float2ShortMap.Entry> fastIterator(final Float2ShortMap.Entry p0);
    }
}
