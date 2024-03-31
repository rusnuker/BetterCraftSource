// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

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

public abstract class AbstractByte2LongSortedMap extends AbstractByte2LongMap implements Byte2LongSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractByte2LongSortedMap() {
    }
    
    @Deprecated
    @Override
    public Byte2LongSortedMap headMap(final Byte to) {
        return this.headMap((byte)to);
    }
    
    @Deprecated
    @Override
    public Byte2LongSortedMap tailMap(final Byte from) {
        return this.tailMap((byte)from);
    }
    
    @Deprecated
    @Override
    public Byte2LongSortedMap subMap(final Byte from, final Byte to) {
        return this.subMap((byte)from, (byte)to);
    }
    
    @Deprecated
    @Override
    public Byte firstKey() {
        return this.firstByteKey();
    }
    
    @Deprecated
    @Override
    public Byte lastKey() {
        return this.lastByteKey();
    }
    
    @Override
    public ByteSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public LongCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Byte, Long>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Byte, Long>>)this.byte2LongEntrySet();
    }
    
    protected class KeySet extends AbstractByteSortedSet
    {
        @Override
        public boolean contains(final byte k) {
            return AbstractByte2LongSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractByte2LongSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractByte2LongSortedMap.this.clear();
        }
        
        @Override
        public ByteComparator comparator() {
            return AbstractByte2LongSortedMap.this.comparator();
        }
        
        @Override
        public byte firstByte() {
            return AbstractByte2LongSortedMap.this.firstByteKey();
        }
        
        @Override
        public byte lastByte() {
            return AbstractByte2LongSortedMap.this.lastByteKey();
        }
        
        @Override
        public ByteSortedSet headSet(final byte to) {
            return AbstractByte2LongSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ByteSortedSet tailSet(final byte from) {
            return AbstractByte2LongSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ByteSortedSet subSet(final byte from, final byte to) {
            return AbstractByte2LongSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Byte, Long>>)AbstractByte2LongSortedMap.this.entrySet().iterator(new BasicEntry(from, 0L)));
        }
        
        @Override
        public ByteBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractByte2LongSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractByteBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Byte, Long>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Byte, Long>> i) {
            this.i = i;
        }
        
        @Override
        public byte nextByte() {
            return (byte)this.i.next().getKey();
        }
        
        @Override
        public byte previousByte() {
            return (byte)this.i.previous().getKey();
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
            return new ValuesIterator(AbstractByte2LongSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final long k) {
            return AbstractByte2LongSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractByte2LongSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractByte2LongSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractLongIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Byte, Long>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Byte, Long>> i) {
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
