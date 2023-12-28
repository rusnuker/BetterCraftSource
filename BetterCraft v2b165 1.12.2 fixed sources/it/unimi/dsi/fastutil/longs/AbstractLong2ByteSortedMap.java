// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.bytes.AbstractByteIterator;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.bytes.ByteCollection;

public abstract class AbstractLong2ByteSortedMap extends AbstractLong2ByteMap implements Long2ByteSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractLong2ByteSortedMap() {
    }
    
    @Deprecated
    @Override
    public Long2ByteSortedMap headMap(final Long to) {
        return this.headMap((long)to);
    }
    
    @Deprecated
    @Override
    public Long2ByteSortedMap tailMap(final Long from) {
        return this.tailMap((long)from);
    }
    
    @Deprecated
    @Override
    public Long2ByteSortedMap subMap(final Long from, final Long to) {
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
    public ByteCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Long, Byte>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Long, Byte>>)this.long2ByteEntrySet();
    }
    
    protected class KeySet extends AbstractLongSortedSet
    {
        @Override
        public boolean contains(final long k) {
            return AbstractLong2ByteSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractLong2ByteSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractLong2ByteSortedMap.this.clear();
        }
        
        @Override
        public LongComparator comparator() {
            return AbstractLong2ByteSortedMap.this.comparator();
        }
        
        @Override
        public long firstLong() {
            return AbstractLong2ByteSortedMap.this.firstLongKey();
        }
        
        @Override
        public long lastLong() {
            return AbstractLong2ByteSortedMap.this.lastLongKey();
        }
        
        @Override
        public LongSortedSet headSet(final long to) {
            return AbstractLong2ByteSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public LongSortedSet tailSet(final long from) {
            return AbstractLong2ByteSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            return AbstractLong2ByteSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Long, Byte>>)AbstractLong2ByteSortedMap.this.entrySet().iterator(new BasicEntry(from, (byte)0)));
        }
        
        @Override
        public LongBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractLong2ByteSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractLongBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Long, Byte>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Long, Byte>> i) {
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
    
    protected class ValuesCollection extends AbstractByteCollection
    {
        @Override
        public ByteIterator iterator() {
            return new ValuesIterator(AbstractLong2ByteSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final byte k) {
            return AbstractLong2ByteSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractLong2ByteSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractLong2ByteSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractByteIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Long, Byte>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Long, Byte>> i) {
            this.i = i;
        }
        
        @Override
        public byte nextByte() {
            return (byte)this.i.next().getValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
