// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.floats.AbstractFloatIterator;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import java.util.Map;
import it.unimi.dsi.fastutil.floats.FloatCollection;

public abstract class AbstractReference2FloatSortedMap<K> extends AbstractReference2FloatMap<K> implements Reference2FloatSortedMap<K>
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractReference2FloatSortedMap() {
    }
    
    @Override
    public ReferenceSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public FloatCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<K, Float>> entrySet() {
        return (ObjectSortedSet<Map.Entry<K, Float>>)this.reference2FloatEntrySet();
    }
    
    protected class KeySet extends AbstractReferenceSortedSet<K>
    {
        @Override
        public boolean contains(final Object k) {
            return AbstractReference2FloatSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractReference2FloatSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractReference2FloatSortedMap.this.clear();
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return AbstractReference2FloatSortedMap.this.comparator();
        }
        
        @Override
        public K first() {
            return AbstractReference2FloatSortedMap.this.firstKey();
        }
        
        @Override
        public K last() {
            return AbstractReference2FloatSortedMap.this.lastKey();
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K to) {
            return AbstractReference2FloatSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K from) {
            return AbstractReference2FloatSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            return AbstractReference2FloatSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Map.Entry<K, Float>>)AbstractReference2FloatSortedMap.this.entrySet().iterator(new BasicEntry<K>(from, 0.0f)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>(AbstractReference2FloatSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator<K> extends AbstractObjectBidirectionalIterator<K>
    {
        protected final ObjectBidirectionalIterator<Map.Entry<K, Float>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<K, Float>> i) {
            this.i = i;
        }
        
        @Override
        public K next() {
            return (K)this.i.next().getKey();
        }
        
        @Override
        public K previous() {
            return (K)this.i.previous().getKey();
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
    
    protected class ValuesCollection extends AbstractFloatCollection
    {
        @Override
        public FloatIterator iterator() {
            return new ValuesIterator<Object>((ObjectBidirectionalIterator<Map.Entry<?, Float>>)AbstractReference2FloatSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final float k) {
            return AbstractReference2FloatSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractReference2FloatSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractReference2FloatSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> extends AbstractFloatIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<K, Float>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<K, Float>> i) {
            this.i = i;
        }
        
        @Override
        public float nextFloat() {
            return (float)this.i.next().getValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
