// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.ints.AbstractIntIterator;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.ints.IntCollection;

public abstract class AbstractDouble2IntSortedMap extends AbstractDouble2IntMap implements Double2IntSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractDouble2IntSortedMap() {
    }
    
    @Deprecated
    @Override
    public Double2IntSortedMap headMap(final Double to) {
        return this.headMap((double)to);
    }
    
    @Deprecated
    @Override
    public Double2IntSortedMap tailMap(final Double from) {
        return this.tailMap((double)from);
    }
    
    @Deprecated
    @Override
    public Double2IntSortedMap subMap(final Double from, final Double to) {
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
    public IntCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Double, Integer>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Double, Integer>>)this.double2IntEntrySet();
    }
    
    protected class KeySet extends AbstractDoubleSortedSet
    {
        @Override
        public boolean contains(final double k) {
            return AbstractDouble2IntSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractDouble2IntSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractDouble2IntSortedMap.this.clear();
        }
        
        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2IntSortedMap.this.comparator();
        }
        
        @Override
        public double firstDouble() {
            return AbstractDouble2IntSortedMap.this.firstDoubleKey();
        }
        
        @Override
        public double lastDouble() {
            return AbstractDouble2IntSortedMap.this.lastDoubleKey();
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            return AbstractDouble2IntSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            return AbstractDouble2IntSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            return AbstractDouble2IntSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Double, Integer>>)AbstractDouble2IntSortedMap.this.entrySet().iterator(new BasicEntry(from, 0)));
        }
        
        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractDouble2IntSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractDoubleBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Double, Integer>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Double, Integer>> i) {
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
    
    protected class ValuesCollection extends AbstractIntCollection
    {
        @Override
        public IntIterator iterator() {
            return new ValuesIterator(AbstractDouble2IntSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final int k) {
            return AbstractDouble2IntSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractDouble2IntSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractDouble2IntSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractIntIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Double, Integer>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Double, Integer>> i) {
            this.i = i;
        }
        
        @Override
        public int nextInt() {
            return (int)this.i.next().getValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
