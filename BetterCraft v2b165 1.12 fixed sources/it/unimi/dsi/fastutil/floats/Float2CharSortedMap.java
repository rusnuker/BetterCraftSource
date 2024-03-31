// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.chars.CharCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Float2CharSortedMap extends Float2CharMap, SortedMap<Float, Character>
{
    ObjectSortedSet<Map.Entry<Float, Character>> entrySet();
    
    ObjectSortedSet<Float2CharMap.Entry> float2CharEntrySet();
    
    FloatSortedSet keySet();
    
    CharCollection values();
    
    FloatComparator comparator();
    
    Float2CharSortedMap subMap(final Float p0, final Float p1);
    
    Float2CharSortedMap headMap(final Float p0);
    
    Float2CharSortedMap tailMap(final Float p0);
    
    Float2CharSortedMap subMap(final float p0, final float p1);
    
    Float2CharSortedMap headMap(final float p0);
    
    Float2CharSortedMap tailMap(final float p0);
    
    float firstFloatKey();
    
    float lastFloatKey();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Float2CharMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Float2CharMap.Entry> fastIterator(final Float2CharMap.Entry p0);
    }
}
