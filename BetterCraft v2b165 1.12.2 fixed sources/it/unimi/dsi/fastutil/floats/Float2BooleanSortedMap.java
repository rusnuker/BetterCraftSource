// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Float2BooleanSortedMap extends Float2BooleanMap, SortedMap<Float, Boolean>
{
    ObjectSortedSet<Map.Entry<Float, Boolean>> entrySet();
    
    ObjectSortedSet<Float2BooleanMap.Entry> float2BooleanEntrySet();
    
    FloatSortedSet keySet();
    
    BooleanCollection values();
    
    FloatComparator comparator();
    
    Float2BooleanSortedMap subMap(final Float p0, final Float p1);
    
    Float2BooleanSortedMap headMap(final Float p0);
    
    Float2BooleanSortedMap tailMap(final Float p0);
    
    Float2BooleanSortedMap subMap(final float p0, final float p1);
    
    Float2BooleanSortedMap headMap(final float p0);
    
    Float2BooleanSortedMap tailMap(final float p0);
    
    float firstFloatKey();
    
    float lastFloatKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Float2BooleanMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Float2BooleanMap.Entry> fastIterator(final Float2BooleanMap.Entry p0);
    }
}
