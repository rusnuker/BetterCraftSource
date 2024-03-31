// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.shorts.AbstractShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import java.util.Map;
import it.unimi.dsi.fastutil.shorts.ShortCollection;

public abstract class AbstractReference2ShortSortedMap<K> extends AbstractReference2ShortMap<K> implements Reference2ShortSortedMap<K>
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractReference2ShortSortedMap() {
    }
    
    @Override
    public ReferenceSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public ShortCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<K, Short>> entrySet() {
        return (ObjectSortedSet<Map.Entry<K, Short>>)this.reference2ShortEntrySet();
    }
    
    protected class KeySet extends AbstractReferenceSortedSet<K>
    {
        @Override
        public boolean contains(final Object k) {
            return AbstractReference2ShortSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractReference2ShortSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractReference2ShortSortedMap.this.clear();
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return AbstractReference2ShortSortedMap.this.comparator();
        }
        
        @Override
        public K first() {
            return AbstractReference2ShortSortedMap.this.firstKey();
        }
        
        @Override
        public K last() {
            return AbstractReference2ShortSortedMap.this.lastKey();
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K to) {
            return AbstractReference2ShortSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K from) {
            return AbstractReference2ShortSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            return AbstractReference2ShortSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Map.Entry<K, Short>>)AbstractReference2ShortSortedMap.this.entrySet().iterator(new BasicEntry<K>(from, (short)0)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>(AbstractReference2ShortSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator<K> extends AbstractObjectBidirectionalIterator<K>
    {
        protected final ObjectBidirectionalIterator<Map.Entry<K, Short>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<K, Short>> i) {
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
    
    protected class ValuesCollection extends AbstractShortCollection
    {
        @Override
        public ShortIterator iterator() {
            return new ValuesIterator<Object>((ObjectBidirectionalIterator<Map.Entry<?, Short>>)AbstractReference2ShortSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final short k) {
            return AbstractReference2ShortSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractReference2ShortSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractReference2ShortSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> extends AbstractShortIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<K, Short>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<K, Short>> i) {
            this.i = i;
        }
        
        @Override
        public short nextShort() {
            return (short)this.i.next().getValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
