// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.ints.AbstractIntIterator;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.ints.IntCollection;

public abstract class AbstractLong2IntSortedMap extends AbstractLong2IntMap implements Long2IntSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractLong2IntSortedMap() {
    }
    
    @Deprecated
    @Override
    public Long2IntSortedMap headMap(final Long to) {
        return this.headMap((long)to);
    }
    
    @Deprecated
    @Override
    public Long2IntSortedMap tailMap(final Long from) {
        return this.tailMap((long)from);
    }
    
    @Deprecated
    @Override
    public Long2IntSortedMap subMap(final Long from, final Long to) {
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
    public IntCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Long, Integer>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Long, Integer>>)this.long2IntEntrySet();
    }
    
    protected class KeySet extends AbstractLongSortedSet
    {
        @Override
        public boolean contains(final long k) {
            return AbstractLong2IntSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractLong2IntSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractLong2IntSortedMap.this.clear();
        }
        
        @Override
        public LongComparator comparator() {
            return AbstractLong2IntSortedMap.this.comparator();
        }
        
        @Override
        public long firstLong() {
            return AbstractLong2IntSortedMap.this.firstLongKey();
        }
        
        @Override
        public long lastLong() {
            return AbstractLong2IntSortedMap.this.lastLongKey();
        }
        
        @Override
        public LongSortedSet headSet(final long to) {
            return AbstractLong2IntSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public LongSortedSet tailSet(final long from) {
            return AbstractLong2IntSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            return AbstractLong2IntSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Long, Integer>>)AbstractLong2IntSortedMap.this.entrySet().iterator(new BasicEntry(from, 0)));
        }
        
        @Override
        public LongBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractLong2IntSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractLongBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Long, Integer>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Long, Integer>> i) {
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
    
    protected class ValuesCollection extends AbstractIntCollection
    {
        @Override
        public IntIterator iterator() {
            return new ValuesIterator(AbstractLong2IntSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final int k) {
            return AbstractLong2IntSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractLong2IntSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractLong2IntSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractIntIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Long, Integer>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Long, Integer>> i) {
            this.i = i;
        }
        
        @Override
        public int nextInt() {
            return (int)this.i.next().getValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
