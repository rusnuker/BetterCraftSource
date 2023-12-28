// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Float2ReferenceSortedMap<V> extends Float2ReferenceMap<V>, SortedMap<Float, V>
{
    ObjectSortedSet<Map.Entry<Float, V>> entrySet();
    
    ObjectSortedSet<Float2ReferenceMap.Entry<V>> float2ReferenceEntrySet();
    
    FloatSortedSet keySet();
    
    ReferenceCollection<V> values();
    
    FloatComparator comparator();
    
    Float2ReferenceSortedMap<V> subMap(final Float p0, final Float p1);
    
    Float2ReferenceSortedMap<V> headMap(final Float p0);
    
    Float2ReferenceSortedMap<V> tailMap(final Float p0);
    
    Float2ReferenceSortedMap<V> subMap(final float p0, final float p1);
    
    Float2ReferenceSortedMap<V> headMap(final float p0);
    
    Float2ReferenceSortedMap<V> tailMap(final float p0);
    
    float firstFloatKey();
    
    float lastFloatKey();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Float2ReferenceMap.Entry<V>>, FastEntrySet<V>
    {
        ObjectBidirectionalIterator<Float2ReferenceMap.Entry<V>> fastIterator(final Float2ReferenceMap.Entry<V> p0);
    }
}
