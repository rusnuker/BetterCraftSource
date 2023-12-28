// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.booleans.AbstractBooleanIterator;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;

public abstract class AbstractDouble2BooleanSortedMap extends AbstractDouble2BooleanMap implements Double2BooleanSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractDouble2BooleanSortedMap() {
    }
    
    @Deprecated
    @Override
    public Double2BooleanSortedMap headMap(final Double to) {
        return this.headMap((double)to);
    }
    
    @Deprecated
    @Override
    public Double2BooleanSortedMap tailMap(final Double from) {
        return this.tailMap((double)from);
    }
    
    @Deprecated
    @Override
    public Double2BooleanSortedMap subMap(final Double from, final Double to) {
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
    public BooleanCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Double, Boolean>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Double, Boolean>>)this.double2BooleanEntrySet();
    }
    
    protected class KeySet extends AbstractDoubleSortedSet
    {
        @Override
        public boolean contains(final double k) {
            return AbstractDouble2BooleanSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractDouble2BooleanSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractDouble2BooleanSortedMap.this.clear();
        }
        
        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2BooleanSortedMap.this.comparator();
        }
        
        @Override
        public double firstDouble() {
            return AbstractDouble2BooleanSortedMap.this.firstDoubleKey();
        }
        
        @Override
        public double lastDouble() {
            return AbstractDouble2BooleanSortedMap.this.lastDoubleKey();
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            return AbstractDouble2BooleanSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            return AbstractDouble2BooleanSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            return AbstractDouble2BooleanSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Double, Boolean>>)AbstractDouble2BooleanSortedMap.this.entrySet().iterator(new BasicEntry(from, false)));
        }
        
        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractDouble2BooleanSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractDoubleBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Double, Boolean>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Double, Boolean>> i) {
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
    
    protected class ValuesCollection extends AbstractBooleanCollection
    {
        @Override
        public BooleanIterator iterator() {
            return new ValuesIterator(AbstractDouble2BooleanSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final boolean k) {
            return AbstractDouble2BooleanSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractDouble2BooleanSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractDouble2BooleanSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractBooleanIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Double, Boolean>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Double, Boolean>> i) {
            this.i = i;
        }
        
        @Override
        public boolean nextBoolean() {
            return (boolean)this.i.next().getValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
