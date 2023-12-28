// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Float2ByteSortedMap extends Float2ByteMap, SortedMap<Float, Byte>
{
    ObjectSortedSet<Map.Entry<Float, Byte>> entrySet();
    
    ObjectSortedSet<Float2ByteMap.Entry> float2ByteEntrySet();
    
    FloatSortedSet keySet();
    
    ByteCollection values();
    
    FloatComparator comparator();
    
    Float2ByteSortedMap subMap(final Float p0, final Float p1);
    
    Float2ByteSortedMap headMap(final Float p0);
    
    Float2ByteSortedMap tailMap(final Float p0);
    
    Float2ByteSortedMap subMap(final float p0, final float p1);
    
    Float2ByteSortedMap headMap(final float p0);
    
    Float2ByteSortedMap tailMap(final float p0);
    
    float firstFloatKey();
    
    float lastFloatKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Float2ByteMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Float2ByteMap.Entry> fastIterator(final Float2ByteMap.Entry p0);
    }
}
