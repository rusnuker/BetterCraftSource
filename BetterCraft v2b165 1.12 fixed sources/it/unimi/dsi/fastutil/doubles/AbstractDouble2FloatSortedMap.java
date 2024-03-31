// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

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

public abstract class AbstractDouble2FloatSortedMap extends AbstractDouble2FloatMap implements Double2FloatSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractDouble2FloatSortedMap() {
    }
    
    @Deprecated
    @Override
    public Double2FloatSortedMap headMap(final Double to) {
        return this.headMap((double)to);
    }
    
    @Deprecated
    @Override
    public Double2FloatSortedMap tailMap(final Double from) {
        return this.tailMap((double)from);
    }
    
    @Deprecated
    @Override
    public Double2FloatSortedMap subMap(final Double from, final Double to) {
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
    public FloatCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Double, Float>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Double, Float>>)this.double2FloatEntrySet();
    }
    
    protected class KeySet extends AbstractDoubleSortedSet
    {
        @Override
        public boolean contains(final double k) {
            return AbstractDouble2FloatSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractDouble2FloatSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractDouble2FloatSortedMap.this.clear();
        }
        
        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2FloatSortedMap.this.comparator();
        }
        
        @Override
        public double firstDouble() {
            return AbstractDouble2FloatSortedMap.this.firstDoubleKey();
        }
        
        @Override
        public double lastDouble() {
            return AbstractDouble2FloatSortedMap.this.lastDoubleKey();
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            return AbstractDouble2FloatSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            return AbstractDouble2FloatSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            return AbstractDouble2FloatSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Double, Float>>)AbstractDouble2FloatSortedMap.this.entrySet().iterator(new BasicEntry(from, 0.0f)));
        }
        
        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractDouble2FloatSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractDoubleBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Double, Float>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Double, Float>> i) {
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
    
    protected class ValuesCollection extends AbstractFloatCollection
    {
        @Override
        public FloatIterator iterator() {
            return new ValuesIterator(AbstractDouble2FloatSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final float k) {
            return AbstractDouble2FloatSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractDouble2FloatSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractDouble2FloatSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractFloatIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Double, Float>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Double, Float>> i) {
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
