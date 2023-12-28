// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.longs.AbstractLongIterator;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.longs.LongCollection;

public abstract class AbstractInt2LongSortedMap extends AbstractInt2LongMap implements Int2LongSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractInt2LongSortedMap() {
    }
    
    @Deprecated
    @Override
    public Int2LongSortedMap headMap(final Integer to) {
        return this.headMap((int)to);
    }
    
    @Deprecated
    @Override
    public Int2LongSortedMap tailMap(final Integer from) {
        return this.tailMap((int)from);
    }
    
    @Deprecated
    @Override
    public Int2LongSortedMap subMap(final Integer from, final Integer to) {
        return this.subMap((int)from, (int)to);
    }
    
    @Deprecated
    @Override
    public Integer firstKey() {
        return this.firstIntKey();
    }
    
    @Deprecated
    @Override
    public Integer lastKey() {
        return this.lastIntKey();
    }
    
    @Override
    public IntSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public LongCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Integer, Long>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Integer, Long>>)this.int2LongEntrySet();
    }
    
    protected class KeySet extends AbstractIntSortedSet
    {
        @Override
        public boolean contains(final int k) {
            return AbstractInt2LongSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractInt2LongSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractInt2LongSortedMap.this.clear();
        }
        
        @Override
        public IntComparator comparator() {
            return AbstractInt2LongSortedMap.this.comparator();
        }
        
        @Override
        public int firstInt() {
            return AbstractInt2LongSortedMap.this.firstIntKey();
        }
        
        @Override
        public int lastInt() {
            return AbstractInt2LongSortedMap.this.lastIntKey();
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            return AbstractInt2LongSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            return AbstractInt2LongSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return AbstractInt2LongSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Integer, Long>>)AbstractInt2LongSortedMap.this.entrySet().iterator(new BasicEntry(from, 0L)));
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractInt2LongSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractIntBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Integer, Long>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Integer, Long>> i) {
            this.i = i;
        }
        
        @Override
        public int nextInt() {
            return (int)this.i.next().getKey();
        }
        
        @Override
        public int previousInt() {
            return (int)this.i.previous().getKey();
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
            return new ValuesIterator(AbstractInt2LongSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final long k) {
            return AbstractInt2LongSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractInt2LongSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractInt2LongSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractLongIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Integer, Long>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Integer, Long>> i) {
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
