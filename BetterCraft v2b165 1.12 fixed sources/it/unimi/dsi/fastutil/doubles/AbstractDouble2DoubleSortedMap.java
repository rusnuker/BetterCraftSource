// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;

public abstract class AbstractDouble2DoubleSortedMap extends AbstractDouble2DoubleMap implements Double2DoubleSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractDouble2DoubleSortedMap() {
    }
    
    @Deprecated
    @Override
    public Double2DoubleSortedMap headMap(final Double to) {
        return this.headMap((double)to);
    }
    
    @Deprecated
    @Override
    public Double2DoubleSortedMap tailMap(final Double from) {
        return this.tailMap((double)from);
    }
    
    @Deprecated
    @Override
    public Double2DoubleSortedMap subMap(final Double from, final Double to) {
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
    public DoubleCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Double, Double>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Double, Double>>)this.double2DoubleEntrySet();
    }
    
    protected class KeySet extends AbstractDoubleSortedSet
    {
        @Override
        public boolean contains(final double k) {
            return AbstractDouble2DoubleSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractDouble2DoubleSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractDouble2DoubleSortedMap.this.clear();
        }
        
        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2DoubleSortedMap.this.comparator();
        }
        
        @Override
        public double firstDouble() {
            return AbstractDouble2DoubleSortedMap.this.firstDoubleKey();
        }
        
        @Override
        public double lastDouble() {
            return AbstractDouble2DoubleSortedMap.this.lastDoubleKey();
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            return AbstractDouble2DoubleSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            return AbstractDouble2DoubleSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            return AbstractDouble2DoubleSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Double, Double>>)AbstractDouble2DoubleSortedMap.this.entrySet().iterator(new BasicEntry(from, 0.0)));
        }
        
        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractDouble2DoubleSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractDoubleBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Double, Double>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Double, Double>> i) {
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
    
    protected class ValuesCollection extends AbstractDoubleCollection
    {
        @Override
        public DoubleIterator iterator() {
            return new ValuesIterator(AbstractDouble2DoubleSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final double k) {
            return AbstractDouble2DoubleSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractDouble2DoubleSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractDouble2DoubleSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractDoubleIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Double, Double>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Double, Double>> i) {
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
