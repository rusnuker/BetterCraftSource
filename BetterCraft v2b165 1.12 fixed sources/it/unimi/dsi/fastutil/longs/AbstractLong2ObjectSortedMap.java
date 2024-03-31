// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.AbstractObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectCollection;

public abstract class AbstractLong2ObjectSortedMap<V> extends AbstractLong2ObjectMap<V> implements Long2ObjectSortedMap<V>
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractLong2ObjectSortedMap() {
    }
    
    @Deprecated
    @Override
    public Long2ObjectSortedMap<V> headMap(final Long to) {
        return this.headMap((long)to);
    }
    
    @Deprecated
    @Override
    public Long2ObjectSortedMap<V> tailMap(final Long from) {
        return this.tailMap((long)from);
    }
    
    @Deprecated
    @Override
    public Long2ObjectSortedMap<V> subMap(final Long from, final Long to) {
        return this.subMap((long)from, (long)to);
    }
    
    @Deprecated
    @Override
    public Long firstKey() {
        return this.firstLongKey();
    }
    
    @Deprecated
    @Override
    public Long lastKey() {
        return this.lastLongKey();
    }
    
    @Override
    public LongSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ObjectCollection<V> values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Long, V>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Long, V>>)this.long2ObjectEntrySet();
    }
    
    protected class KeySet extends AbstractLongSortedSet
    {
        @Override
        public boolean contains(final long k) {
            return AbstractLong2ObjectSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractLong2ObjectSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractLong2ObjectSortedMap.this.clear();
        }
        
        @Override
        public LongComparator comparator() {
            return AbstractLong2ObjectSortedMap.this.comparator();
        }
        
        @Override
        public long firstLong() {
            return AbstractLong2ObjectSortedMap.this.firstLongKey();
        }
        
        @Override
        public long lastLong() {
            return AbstractLong2ObjectSortedMap.this.lastLongKey();
        }
        
        @Override
        public LongSortedSet headSet(final long to) {
            return AbstractLong2ObjectSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public LongSortedSet tailSet(final long from) {
            return AbstractLong2ObjectSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            return AbstractLong2ObjectSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            return new KeySetIterator<Object>((ObjectBidirectionalIterator<Map.Entry<Long, ?>>)AbstractLong2ObjectSortedMap.this.entrySet().iterator(new BasicEntry<V>(from, null)));
        }
        
        @Override
        public LongBidirectionalIterator iterator() {
            return new KeySetIterator<Object>((ObjectBidirectionalIterator<Map.Entry<Long, ?>>)AbstractLong2ObjectSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator<V> extends AbstractLongBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Long, V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Long, V>> i) {
            this.i = i;
        }
        
        @Override
        public long nextLong() {
            return (long)this.i.next().getKey();
        }
        
        @Override
        public long previousLong() {
            return (long)this.i.previous().getKey();
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
    
    protected class ValuesCollection extends AbstractObjectCollection<V>
    {
        @Override
        public ObjectIterator<V> iterator() {
            return new ValuesIterator<V>(AbstractLong2ObjectSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final Object k) {
            return AbstractLong2ObjectSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractLong2ObjectSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractLong2ObjectSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<V> extends AbstractObjectIterator<V>
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Long, V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Long, V>> i) {
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
