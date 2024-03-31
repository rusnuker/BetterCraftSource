// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.AbstractObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;

public abstract class AbstractDouble2ReferenceSortedMap<V> extends AbstractDouble2ReferenceMap<V> implements Double2ReferenceSortedMap<V>
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractDouble2ReferenceSortedMap() {
    }
    
    @Deprecated
    @Override
    public Double2ReferenceSortedMap<V> headMap(final Double to) {
        return this.headMap((double)to);
    }
    
    @Deprecated
    @Override
    public Double2ReferenceSortedMap<V> tailMap(final Double from) {
        return this.tailMap((double)from);
    }
    
    @Deprecated
    @Override
    public Double2ReferenceSortedMap<V> subMap(final Double from, final Double to) {
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
    public ReferenceCollection<V> values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Double, V>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Double, V>>)this.double2ReferenceEntrySet();
    }
    
    protected class KeySet extends AbstractDoubleSortedSet
    {
        @Override
        public boolean contains(final double k) {
            return AbstractDouble2ReferenceSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractDouble2ReferenceSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractDouble2ReferenceSortedMap.this.clear();
        }
        
        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2ReferenceSortedMap.this.comparator();
        }
        
        @Override
        public double firstDouble() {
            return AbstractDouble2ReferenceSortedMap.this.firstDoubleKey();
        }
        
        @Override
        public double lastDouble() {
            return AbstractDouble2ReferenceSortedMap.this.lastDoubleKey();
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            return AbstractDouble2ReferenceSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            return AbstractDouble2ReferenceSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            return AbstractDouble2ReferenceSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            return new KeySetIterator<Object>((ObjectBidirectionalIterator<Map.Entry<Double, ?>>)AbstractDouble2ReferenceSortedMap.this.entrySet().iterator(new BasicEntry<V>(from, null)));
        }
        
        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator<Object>((ObjectBidirectionalIterator<Map.Entry<Double, ?>>)AbstractDouble2ReferenceSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator<V> extends AbstractDoubleBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Double, V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Double, V>> i) {
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
    
    protected class ValuesCollection extends AbstractReferenceCollection<V>
    {
        @Override
        public ObjectIterator<V> iterator() {
            return new ValuesIterator<V>(AbstractDouble2ReferenceSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final Object k) {
            return AbstractDouble2ReferenceSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractDouble2ReferenceSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractDouble2ReferenceSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<V> extends AbstractObjectIterator<V>
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Double, V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Double, V>> i) {
            this.i = i;
        }
        
        @Override
        public V next() {
            return (V)this.i.next().getValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
