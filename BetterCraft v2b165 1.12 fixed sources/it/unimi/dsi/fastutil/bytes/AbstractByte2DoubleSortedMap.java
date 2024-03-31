// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

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

public abstract class AbstractByte2DoubleSortedMap extends AbstractByte2DoubleMap implements Byte2DoubleSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractByte2DoubleSortedMap() {
    }
    
    @Deprecated
    @Override
    public Byte2DoubleSortedMap headMap(final Byte to) {
        return this.headMap((byte)to);
    }
    
    @Deprecated
    @Override
    public Byte2DoubleSortedMap tailMap(final Byte from) {
        return this.tailMap((byte)from);
    }
    
    @Deprecated
    @Override
    public Byte2DoubleSortedMap subMap(final Byte from, final Byte to) {
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
    public DoubleCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Byte, Double>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Byte, Double>>)this.byte2DoubleEntrySet();
    }
    
    protected class KeySet extends AbstractByteSortedSet
    {
        @Override
        public boolean contains(final byte k) {
            return AbstractByte2DoubleSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractByte2DoubleSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractByte2DoubleSortedMap.this.clear();
        }
        
        @Override
        public ByteComparator comparator() {
            return AbstractByte2DoubleSortedMap.this.comparator();
        }
        
        @Override
        public byte firstByte() {
            return AbstractByte2DoubleSortedMap.this.firstByteKey();
        }
        
        @Override
        public byte lastByte() {
            return AbstractByte2DoubleSortedMap.this.lastByteKey();
        }
        
        @Override
        public ByteSortedSet headSet(final byte to) {
            return AbstractByte2DoubleSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ByteSortedSet tailSet(final byte from) {
            return AbstractByte2DoubleSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ByteSortedSet subSet(final byte from, final byte to) {
            return AbstractByte2DoubleSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Byte, Double>>)AbstractByte2DoubleSortedMap.this.entrySet().iterator(new BasicEntry(from, 0.0)));
        }
        
        @Override
        public ByteBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractByte2DoubleSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractByteBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Byte, Double>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Byte, Double>> i) {
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
    
    protected class ValuesCollection extends AbstractDoubleCollection
    {
        @Override
        public DoubleIterator iterator() {
            return new ValuesIterator(AbstractByte2DoubleSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final double k) {
            return AbstractByte2DoubleSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractByte2DoubleSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractByte2DoubleSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractDoubleIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Byte, Double>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Byte, Double>> i) {
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
