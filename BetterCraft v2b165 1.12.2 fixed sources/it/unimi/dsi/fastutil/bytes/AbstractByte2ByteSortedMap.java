// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;

public abstract class AbstractByte2ByteSortedMap extends AbstractByte2ByteMap implements Byte2ByteSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractByte2ByteSortedMap() {
    }
    
    @Deprecated
    @Override
    public Byte2ByteSortedMap headMap(final Byte to) {
        return this.headMap((byte)to);
    }
    
    @Deprecated
    @Override
    public Byte2ByteSortedMap tailMap(final Byte from) {
        return this.tailMap((byte)from);
    }
    
    @Deprecated
    @Override
    public Byte2ByteSortedMap subMap(final Byte from, final Byte to) {
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
    public ByteCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Byte, Byte>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Byte, Byte>>)this.byte2ByteEntrySet();
    }
    
    protected class KeySet extends AbstractByteSortedSet
    {
        @Override
        public boolean contains(final byte k) {
            return AbstractByte2ByteSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractByte2ByteSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractByte2ByteSortedMap.this.clear();
        }
        
        @Override
        public ByteComparator comparator() {
            return AbstractByte2ByteSortedMap.this.comparator();
        }
        
        @Override
        public byte firstByte() {
            return AbstractByte2ByteSortedMap.this.firstByteKey();
        }
        
        @Override
        public byte lastByte() {
            return AbstractByte2ByteSortedMap.this.lastByteKey();
        }
        
        @Override
        public ByteSortedSet headSet(final byte to) {
            return AbstractByte2ByteSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ByteSortedSet tailSet(final byte from) {
            return AbstractByte2ByteSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ByteSortedSet subSet(final byte from, final byte to) {
            return AbstractByte2ByteSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Byte, Byte>>)AbstractByte2ByteSortedMap.this.entrySet().iterator(new BasicEntry(from, (byte)0)));
        }
        
        @Override
        public ByteBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractByte2ByteSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractByteBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Byte, Byte>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Byte, Byte>> i) {
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
    
    protected class ValuesCollection extends AbstractByteCollection
    {
        @Override
        public ByteIterator iterator() {
            return new ValuesIterator(AbstractByte2ByteSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final byte k) {
            return AbstractByte2ByteSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractByte2ByteSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractByte2ByteSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractByteIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Byte, Byte>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Byte, Byte>> i) {
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
