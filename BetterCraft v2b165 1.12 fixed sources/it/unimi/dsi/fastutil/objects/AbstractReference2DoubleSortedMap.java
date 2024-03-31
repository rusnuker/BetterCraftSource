// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.doubles.AbstractDoubleIterator;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import java.util.Map;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;

public abstract class AbstractReference2DoubleSortedMap<K> extends AbstractReference2DoubleMap<K> implements Reference2DoubleSortedMap<K>
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractReference2DoubleSortedMap() {
    }
    
    @Override
    public ReferenceSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public DoubleCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<K, Double>> entrySet() {
        return (ObjectSortedSet<Map.Entry<K, Double>>)this.reference2DoubleEntrySet();
    }
    
    protected class KeySet extends AbstractReferenceSortedSet<K>
    {
        @Override
        public boolean contains(final Object k) {
            return AbstractReference2DoubleSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractReference2DoubleSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractReference2DoubleSortedMap.this.clear();
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return AbstractReference2DoubleSortedMap.this.comparator();
        }
        
        @Override
        public K first() {
            return AbstractReference2DoubleSortedMap.this.firstKey();
        }
        
        @Override
        public K last() {
            return AbstractReference2DoubleSortedMap.this.lastKey();
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K to) {
            return AbstractReference2DoubleSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K from) {
            return AbstractReference2DoubleSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            return AbstractReference2DoubleSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Map.Entry<K, Double>>)AbstractReference2DoubleSortedMap.this.entrySet().iterator(new BasicEntry<K>(from, 0.0)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>(AbstractReference2DoubleSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator<K> extends AbstractObjectBidirectionalIterator<K>
    {
        protected final ObjectBidirectionalIterator<Map.Entry<K, Double>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<K, Double>> i) {
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
    
    protected class ValuesCollection extends AbstractDoubleCollection
    {
        @Override
        public DoubleIterator iterator() {
            return new ValuesIterator<Object>((ObjectBidirectionalIterator<Map.Entry<?, Double>>)AbstractReference2DoubleSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final double k) {
            return AbstractReference2DoubleSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractReference2DoubleSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractReference2DoubleSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> extends AbstractDoubleIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<K, Double>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<K, Double>> i) {
            this.i = i;
        }
        
        @Override
        public double nextDouble() {
            return (double)this.i.next().getValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
