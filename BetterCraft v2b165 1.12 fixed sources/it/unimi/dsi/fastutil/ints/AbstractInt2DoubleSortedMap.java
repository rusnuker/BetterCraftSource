// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

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

public abstract class AbstractInt2DoubleSortedMap extends AbstractInt2DoubleMap implements Int2DoubleSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractInt2DoubleSortedMap() {
    }
    
    @Deprecated
    @Override
    public Int2DoubleSortedMap headMap(final Integer to) {
        return this.headMap((int)to);
    }
    
    @Deprecated
    @Override
    public Int2DoubleSortedMap tailMap(final Integer from) {
        return this.tailMap((int)from);
    }
    
    @Deprecated
    @Override
    public Int2DoubleSortedMap subMap(final Integer from, final Integer to) {
        return this.subMap((int)from, (int)to);
    }
    
    @Deprecated
    @Override
    public Integer firstKey() {
        return this.firstIntKey();
    }
    
    @Deprecated
    @Override
    public Integer lastKey() {
        return this.lastIntKey();
    }
    
    @Override
    public IntSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public DoubleCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Integer, Double>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Integer, Double>>)this.int2DoubleEntrySet();
    }
    
    protected class KeySet extends AbstractIntSortedSet
    {
        @Override
        public boolean contains(final int k) {
            return AbstractInt2DoubleSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractInt2DoubleSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractInt2DoubleSortedMap.this.clear();
        }
        
        @Override
        public IntComparator comparator() {
            return AbstractInt2DoubleSortedMap.this.comparator();
        }
        
        @Override
        public int firstInt() {
            return AbstractInt2DoubleSortedMap.this.firstIntKey();
        }
        
        @Override
        public int lastInt() {
            return AbstractInt2DoubleSortedMap.this.lastIntKey();
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            return AbstractInt2DoubleSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            return AbstractInt2DoubleSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return AbstractInt2DoubleSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Integer, Double>>)AbstractInt2DoubleSortedMap.this.entrySet().iterator(new BasicEntry(from, 0.0)));
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractInt2DoubleSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractIntBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Integer, Double>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Integer, Double>> i) {
            this.i = i;
        }
        
        @Override
        public int nextInt() {
            return (int)this.i.next().getKey();
        }
        
        @Override
        public int previousInt() {
            return (int)this.i.previous().getKey();
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
            return new ValuesIterator(AbstractInt2DoubleSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final double k) {
            return AbstractInt2DoubleSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractInt2DoubleSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractInt2DoubleSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractDoubleIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Integer, Double>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Integer, Double>> i) {
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
