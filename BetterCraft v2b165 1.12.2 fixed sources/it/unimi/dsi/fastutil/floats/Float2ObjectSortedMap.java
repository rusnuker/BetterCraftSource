// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Float2ObjectSortedMap<V> extends Float2ObjectMap<V>, SortedMap<Float, V>
{
    ObjectSortedSet<Map.Entry<Float, V>> entrySet();
    
    ObjectSortedSet<Float2ObjectMap.Entry<V>> float2ObjectEntrySet();
    
    FloatSortedSet keySet();
    
    ObjectCollection<V> values();
    
    FloatComparator comparator();
    
    Float2ObjectSortedMap<V> subMap(final Float p0, final Float p1);
    
    Float2ObjectSortedMap<V> headMap(final Float p0);
    
    Float2ObjectSortedMap<V> tailMap(final Float p0);
    
    Float2ObjectSortedMap<V> subMap(final float p0, final float p1);
    
    Float2ObjectSortedMap<V> headMap(final float p0);
    
    Float2ObjectSortedMap<V> tailMap(final float p0);
    
    float firstFloatKey();
    
    float lastFloatKey();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Float2ObjectMap.Entry<V>>, FastEntrySet<V>
    {
        ObjectBidirectionalIterator<Float2ObjectMap.Entry<V>> fastIterator(final Float2ObjectMap.Entry<V> p0);
    }
}
