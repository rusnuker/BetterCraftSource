// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.floats.AbstractFloatIterator;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.floats.FloatCollection;

public abstract class AbstractLong2FloatSortedMap extends AbstractLong2FloatMap implements Long2FloatSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractLong2FloatSortedMap() {
    }
    
    @Deprecated
    @Override
    public Long2FloatSortedMap headMap(final Long to) {
        return this.headMap((long)to);
    }
    
    @Deprecated
    @Override
    public Long2FloatSortedMap tailMap(final Long from) {
        return this.tailMap((long)from);
    }
    
    @Deprecated
    @Override
    public Long2FloatSortedMap subMap(final Long from, final Long to) {
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
    public FloatCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Long, Float>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Long, Float>>)this.long2FloatEntrySet();
    }
    
    protected class KeySet extends AbstractLongSortedSet
    {
        @Override
        public boolean contains(final long k) {
            return AbstractLong2FloatSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractLong2FloatSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractLong2FloatSortedMap.this.clear();
        }
        
        @Override
        public LongComparator comparator() {
            return AbstractLong2FloatSortedMap.this.comparator();
        }
        
        @Override
        public long firstLong() {
            return AbstractLong2FloatSortedMap.this.firstLongKey();
        }
        
        @Override
        public long lastLong() {
            return AbstractLong2FloatSortedMap.this.lastLongKey();
        }
        
        @Override
        public LongSortedSet headSet(final long to) {
            return AbstractLong2FloatSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public LongSortedSet tailSet(final long from) {
            return AbstractLong2FloatSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            return AbstractLong2FloatSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Long, Float>>)AbstractLong2FloatSortedMap.this.entrySet().iterator(new BasicEntry(from, 0.0f)));
        }
        
        @Override
        public LongBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractLong2FloatSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractLongBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Long, Float>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Long, Float>> i) {
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
    
    protected class ValuesCollection extends AbstractFloatCollection
    {
        @Override
        public FloatIterator iterator() {
            return new ValuesIterator(AbstractLong2FloatSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final float k) {
            return AbstractLong2FloatSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractLong2FloatSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractLong2FloatSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractFloatIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Long, Float>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Long, Float>> i) {
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
