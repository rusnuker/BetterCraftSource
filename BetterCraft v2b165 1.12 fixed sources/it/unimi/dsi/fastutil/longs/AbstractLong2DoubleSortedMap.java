// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.doubles.AbstractDoubleIterator;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;

public abstract class AbstractLong2DoubleSortedMap extends AbstractLong2DoubleMap implements Long2DoubleSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractLong2DoubleSortedMap() {
    }
    
    @Deprecated
    @Override
    public Long2DoubleSortedMap headMap(final Long to) {
        return this.headMap((long)to);
    }
    
    @Deprecated
    @Override
    public Long2DoubleSortedMap tailMap(final Long from) {
        return this.tailMap((long)from);
    }
    
    @Deprecated
    @Override
    public Long2DoubleSortedMap subMap(final Long from, final Long to) {
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
    public DoubleCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Long, Double>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Long, Double>>)this.long2DoubleEntrySet();
    }
    
    protected class KeySet extends AbstractLongSortedSet
    {
        @Override
        public boolean contains(final long k) {
            return AbstractLong2DoubleSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractLong2DoubleSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractLong2DoubleSortedMap.this.clear();
        }
        
        @Override
        public LongComparator comparator() {
            return AbstractLong2DoubleSortedMap.this.comparator();
        }
        
        @Override
        public long firstLong() {
            return AbstractLong2DoubleSortedMap.this.firstLongKey();
        }
        
        @Override
        public long lastLong() {
            return AbstractLong2DoubleSortedMap.this.lastLongKey();
        }
        
        @Override
        public LongSortedSet headSet(final long to) {
            return AbstractLong2DoubleSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public LongSortedSet tailSet(final long from) {
            return AbstractLong2DoubleSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            return AbstractLong2DoubleSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Long, Double>>)AbstractLong2DoubleSortedMap.this.entrySet().iterator(new BasicEntry(from, 0.0)));
        }
        
        @Override
        public LongBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractLong2DoubleSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractLongBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Long, Double>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Long, Double>> i) {
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
    
    protected class ValuesCollection extends AbstractDoubleCollection
    {
        @Override
        public DoubleIterator iterator() {
            return new ValuesIterator(AbstractLong2DoubleSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final double k) {
            return AbstractLong2DoubleSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractLong2DoubleSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractLong2DoubleSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractDoubleIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Long, Double>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Long, Double>> i) {
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
