// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import java.util.Map;

public abstract class AbstractObject2ReferenceSortedMap<K, V> extends AbstractObject2ReferenceMap<K, V> implements Object2ReferenceSortedMap<K, V>
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractObject2ReferenceSortedMap() {
    }
    
    @Override
    public ObjectSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public ReferenceCollection<V> values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<K, V>> entrySet() {
        return (ObjectSortedSet<Map.Entry<K, V>>)this.object2ReferenceEntrySet();
    }
    
    protected class KeySet extends AbstractObjectSortedSet<K>
    {
        @Override
        public boolean contains(final Object k) {
            return AbstractObject2ReferenceSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractObject2ReferenceSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractObject2ReferenceSortedMap.this.clear();
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return AbstractObject2ReferenceSortedMap.this.comparator();
        }
        
        @Override
        public K first() {
            return AbstractObject2ReferenceSortedMap.this.firstKey();
        }
        
        @Override
        public K last() {
            return AbstractObject2ReferenceSortedMap.this.lastKey();
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            return AbstractObject2ReferenceSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            return AbstractObject2ReferenceSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            return AbstractObject2ReferenceSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return (ObjectBidirectionalIterator<K>)new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Object, Object>>)AbstractObject2ReferenceSortedMap.this.entrySet().iterator(new BasicEntry<K, V>(from, null)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return (ObjectBidirectionalIterator<K>)new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Object, Object>>)AbstractObject2ReferenceSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator<K, V> extends AbstractObjectBidirectionalIterator<K>
    {
        protected final ObjectBidirectionalIterator<Map.Entry<K, V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<K, V>> i) {
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
    
    protected class ValuesCollection extends AbstractReferenceCollection<V>
    {
        @Override
        public ObjectIterator<V> iterator() {
            return (ObjectIterator<V>)new ValuesIterator((ObjectBidirectionalIterator<Map.Entry<Object, Object>>)AbstractObject2ReferenceSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final Object k) {
            return AbstractObject2ReferenceSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractObject2ReferenceSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractObject2ReferenceSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K, V> extends AbstractObjectIterator<V>
    {
        protected final ObjectBidirectionalIterator<Map.Entry<K, V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<K, V>> i) {
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
