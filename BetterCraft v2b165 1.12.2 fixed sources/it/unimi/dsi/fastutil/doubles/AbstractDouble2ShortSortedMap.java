// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

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

public abstract class AbstractDouble2ShortSortedMap extends AbstractDouble2ShortMap implements Double2ShortSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractDouble2ShortSortedMap() {
    }
    
    @Deprecated
    @Override
    public Double2ShortSortedMap headMap(final Double to) {
        return this.headMap((double)to);
    }
    
    @Deprecated
    @Override
    public Double2ShortSortedMap tailMap(final Double from) {
        return this.tailMap((double)from);
    }
    
    @Deprecated
    @Override
    public Double2ShortSortedMap subMap(final Double from, final Double to) {
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
    public ShortCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Double, Short>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Double, Short>>)this.double2ShortEntrySet();
    }
    
    protected class KeySet extends AbstractDoubleSortedSet
    {
        @Override
        public boolean contains(final double k) {
            return AbstractDouble2ShortSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractDouble2ShortSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractDouble2ShortSortedMap.this.clear();
        }
        
        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2ShortSortedMap.this.comparator();
        }
        
        @Override
        public double firstDouble() {
            return AbstractDouble2ShortSortedMap.this.firstDoubleKey();
        }
        
        @Override
        public double lastDouble() {
            return AbstractDouble2ShortSortedMap.this.lastDoubleKey();
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            return AbstractDouble2ShortSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            return AbstractDouble2ShortSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            return AbstractDouble2ShortSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Double, Short>>)AbstractDouble2ShortSortedMap.this.entrySet().iterator(new BasicEntry(from, (short)0)));
        }
        
        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractDouble2ShortSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractDoubleBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Double, Short>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Double, Short>> i) {
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
    
    protected class ValuesCollection extends AbstractShortCollection
    {
        @Override
        public ShortIterator iterator() {
            return new ValuesIterator(AbstractDouble2ShortSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final short k) {
            return AbstractDouble2ShortSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractDouble2ShortSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractDouble2ShortSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractShortIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Double, Short>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Double, Short>> i) {
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
