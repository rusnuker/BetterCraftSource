// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.longs.AbstractLongIterator;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import java.util.Map;
import it.unimi.dsi.fastutil.longs.LongCollection;

public abstract class AbstractObject2LongSortedMap<K> extends AbstractObject2LongMap<K> implements Object2LongSortedMap<K>
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractObject2LongSortedMap() {
    }
    
    @Override
    public ObjectSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public LongCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<K, Long>> entrySet() {
        return (ObjectSortedSet<Map.Entry<K, Long>>)this.object2LongEntrySet();
    }
    
    protected class KeySet extends AbstractObjectSortedSet<K>
    {
        @Override
        public boolean contains(final Object k) {
            return AbstractObject2LongSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractObject2LongSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractObject2LongSortedMap.this.clear();
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return AbstractObject2LongSortedMap.this.comparator();
        }
        
        @Override
        public K first() {
            return AbstractObject2LongSortedMap.this.firstKey();
        }
        
        @Override
        public K last() {
            return AbstractObject2LongSortedMap.this.lastKey();
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            return AbstractObject2LongSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            return AbstractObject2LongSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            return AbstractObject2LongSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Map.Entry<K, Long>>)AbstractObject2LongSortedMap.this.entrySet().iterator(new BasicEntry<K>(from, 0L)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>(AbstractObject2LongSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator<K> extends AbstractObjectBidirectionalIterator<K>
    {
        protected final ObjectBidirectionalIterator<Map.Entry<K, Long>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<K, Long>> i) {
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
    
    protected class ValuesCollection extends AbstractLongCollection
    {
        @Override
        public LongIterator iterator() {
            return new ValuesIterator<Object>((ObjectBidirectionalIterator<Map.Entry<?, Long>>)AbstractObject2LongSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final long k) {
            return AbstractObject2LongSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractObject2LongSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractObject2LongSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> extends AbstractLongIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<K, Long>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<K, Long>> i) {
            this.i = i;
        }
        
        @Override
        public long nextLong() {
            return (long)this.i.next().getValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
