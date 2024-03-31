// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

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

public abstract class AbstractDouble2ByteSortedMap extends AbstractDouble2ByteMap implements Double2ByteSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractDouble2ByteSortedMap() {
    }
    
    @Deprecated
    @Override
    public Double2ByteSortedMap headMap(final Double to) {
        return this.headMap((double)to);
    }
    
    @Deprecated
    @Override
    public Double2ByteSortedMap tailMap(final Double from) {
        return this.tailMap((double)from);
    }
    
    @Deprecated
    @Override
    public Double2ByteSortedMap subMap(final Double from, final Double to) {
        return this.subMap((double)from, (double)to);
    }
    
    @Deprecated
    @Override
    public Double firstKey() {
        return this.firstDoubleKey();
    }
    
    @Deprecated
    @Override
    public Double lastKey() {
        return this.lastDoubleKey();
    }
    
    @Override
    public DoubleSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ByteCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Double, Byte>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Double, Byte>>)this.double2ByteEntrySet();
    }
    
    protected class KeySet extends AbstractDoubleSortedSet
    {
        @Override
        public boolean contains(final double k) {
            return AbstractDouble2ByteSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractDouble2ByteSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractDouble2ByteSortedMap.this.clear();
        }
        
        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2ByteSortedMap.this.comparator();
        }
        
        @Override
        public double firstDouble() {
            return AbstractDouble2ByteSortedMap.this.firstDoubleKey();
        }
        
        @Override
        public double lastDouble() {
            return AbstractDouble2ByteSortedMap.this.lastDoubleKey();
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            return AbstractDouble2ByteSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            return AbstractDouble2ByteSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            return AbstractDouble2ByteSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Double, Byte>>)AbstractDouble2ByteSortedMap.this.entrySet().iterator(new BasicEntry(from, (byte)0)));
        }
        
        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractDouble2ByteSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractDoubleBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Double, Byte>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Double, Byte>> i) {
            this.i = i;
        }
        
        @Override
        public double nextDouble() {
            return (double)this.i.next().getKey();
        }
        
        @Override
        public double previousDouble() {
            return (double)this.i.previous().getKey();
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
            return new ValuesIterator(AbstractDouble2ByteSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final byte k) {
            return AbstractDouble2ByteSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractDouble2ByteSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractDouble2ByteSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractByteIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Double, Byte>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Double, Byte>> i) {
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
