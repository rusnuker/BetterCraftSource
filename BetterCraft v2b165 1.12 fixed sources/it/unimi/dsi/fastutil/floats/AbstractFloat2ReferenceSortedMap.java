// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.AbstractObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;

public abstract class AbstractFloat2ReferenceSortedMap<V> extends AbstractFloat2ReferenceMap<V> implements Float2ReferenceSortedMap<V>
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractFloat2ReferenceSortedMap() {
    }
    
    @Deprecated
    @Override
    public Float2ReferenceSortedMap<V> headMap(final Float to) {
        return this.headMap((float)to);
    }
    
    @Deprecated
    @Override
    public Float2ReferenceSortedMap<V> tailMap(final Float from) {
        return this.tailMap((float)from);
    }
    
    @Deprecated
    @Override
    public Float2ReferenceSortedMap<V> subMap(final Float from, final Float to) {
        return this.subMap((float)from, (float)to);
    }
    
    @Deprecated
    @Override
    public Float firstKey() {
        return this.firstFloatKey();
    }
    
    @Deprecated
    @Override
    public Float lastKey() {
        return this.lastFloatKey();
    }
    
    @Override
    public FloatSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ReferenceCollection<V> values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Float, V>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Float, V>>)this.float2ReferenceEntrySet();
    }
    
    protected class KeySet extends AbstractFloatSortedSet
    {
        @Override
        public boolean contains(final float k) {
            return AbstractFloat2ReferenceSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractFloat2ReferenceSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractFloat2ReferenceSortedMap.this.clear();
        }
        
        @Override
        public FloatComparator comparator() {
            return AbstractFloat2ReferenceSortedMap.this.comparator();
        }
        
        @Override
        public float firstFloat() {
            return AbstractFloat2ReferenceSortedMap.this.firstFloatKey();
        }
        
        @Override
        public float lastFloat() {
            return AbstractFloat2ReferenceSortedMap.this.lastFloatKey();
        }
        
        @Override
        public FloatSortedSet headSet(final float to) {
            return AbstractFloat2ReferenceSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public FloatSortedSet tailSet(final float from) {
            return AbstractFloat2ReferenceSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public FloatSortedSet subSet(final float from, final float to) {
            return AbstractFloat2ReferenceSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public FloatBidirectionalIterator iterator(final float from) {
            return new KeySetIterator<Object>((ObjectBidirectionalIterator<Map.Entry<Float, ?>>)AbstractFloat2ReferenceSortedMap.this.entrySet().iterator(new BasicEntry<V>(from, null)));
        }
        
        @Override
        public FloatBidirectionalIterator iterator() {
            return new KeySetIterator<Object>((ObjectBidirectionalIterator<Map.Entry<Float, ?>>)AbstractFloat2ReferenceSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator<V> extends AbstractFloatBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Float, V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Float, V>> i) {
            this.i = i;
        }
        
        @Override
        public float nextFloat() {
            return (float)this.i.next().getKey();
        }
        
        @Override
        public float previousFloat() {
            return (float)this.i.previous().getKey();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        @Override
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
    }
    
    protected class ValuesCollection extends AbstractReferenceCollection<V>
    {
        @Override
        public ObjectIterator<V> iterator() {
            return new ValuesIterator<V>(AbstractFloat2ReferenceSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final Object k) {
            return AbstractFloat2ReferenceSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractFloat2ReferenceSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractFloat2ReferenceSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<V> extends AbstractObjectIterator<V>
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Float, V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Float, V>> i) {
            this.i = i;
        }
        
        @Override
        public V next() {
            return (V)this.i.next().getValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
