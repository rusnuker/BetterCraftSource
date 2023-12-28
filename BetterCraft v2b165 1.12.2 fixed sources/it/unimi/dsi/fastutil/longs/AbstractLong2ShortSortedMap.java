// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.shorts.AbstractShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.shorts.ShortCollection;

public abstract class AbstractLong2ShortSortedMap extends AbstractLong2ShortMap implements Long2ShortSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractLong2ShortSortedMap() {
    }
    
    @Deprecated
    @Override
    public Long2ShortSortedMap headMap(final Long to) {
        return this.headMap((long)to);
    }
    
    @Deprecated
    @Override
    public Long2ShortSortedMap tailMap(final Long from) {
        return this.tailMap((long)from);
    }
    
    @Deprecated
    @Override
    public Long2ShortSortedMap subMap(final Long from, final Long to) {
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
    public ShortCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Long, Short>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Long, Short>>)this.long2ShortEntrySet();
    }
    
    protected class KeySet extends AbstractLongSortedSet
    {
        @Override
        public boolean contains(final long k) {
            return AbstractLong2ShortSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractLong2ShortSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractLong2ShortSortedMap.this.clear();
        }
        
        @Override
        public LongComparator comparator() {
            return AbstractLong2ShortSortedMap.this.comparator();
        }
        
        @Override
        public long firstLong() {
            return AbstractLong2ShortSortedMap.this.firstLongKey();
        }
        
        @Override
        public long lastLong() {
            return AbstractLong2ShortSortedMap.this.lastLongKey();
        }
        
        @Override
        public LongSortedSet headSet(final long to) {
            return AbstractLong2ShortSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public LongSortedSet tailSet(final long from) {
            return AbstractLong2ShortSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            return AbstractLong2ShortSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Long, Short>>)AbstractLong2ShortSortedMap.this.entrySet().iterator(new BasicEntry(from, (short)0)));
        }
        
        @Override
        public LongBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractLong2ShortSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractLongBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Long, Short>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Long, Short>> i) {
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
    
    protected class ValuesCollection extends AbstractShortCollection
    {
        @Override
        public ShortIterator iterator() {
            return new ValuesIterator(AbstractLong2ShortSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final short k) {
            return AbstractLong2ShortSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractLong2ShortSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractLong2ShortSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractShortIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Long, Short>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Long, Short>> i) {
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
