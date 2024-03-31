// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

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

public abstract class AbstractFloat2DoubleSortedMap extends AbstractFloat2DoubleMap implements Float2DoubleSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractFloat2DoubleSortedMap() {
    }
    
    @Deprecated
    @Override
    public Float2DoubleSortedMap headMap(final Float to) {
        return this.headMap((float)to);
    }
    
    @Deprecated
    @Override
    public Float2DoubleSortedMap tailMap(final Float from) {
        return this.tailMap((float)from);
    }
    
    @Deprecated
    @Override
    public Float2DoubleSortedMap subMap(final Float from, final Float to) {
        return this.subMap((float)from, (float)to);
    }
    
    @Deprecated
    @Override
    public Float firstKey() {
        return this.firstFloatKey();
    }
    
    @Deprecated
    @Override
    public Float lastKey() {
        return this.lastFloatKey();
    }
    
    @Override
    public FloatSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public DoubleCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Float, Double>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Float, Double>>)this.float2DoubleEntrySet();
    }
    
    protected class KeySet extends AbstractFloatSortedSet
    {
        @Override
        public boolean contains(final float k) {
            return AbstractFloat2DoubleSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractFloat2DoubleSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractFloat2DoubleSortedMap.this.clear();
        }
        
        @Override
        public FloatComparator comparator() {
            return AbstractFloat2DoubleSortedMap.this.comparator();
        }
        
        @Override
        public float firstFloat() {
            return AbstractFloat2DoubleSortedMap.this.firstFloatKey();
        }
        
        @Override
        public float lastFloat() {
            return AbstractFloat2DoubleSortedMap.this.lastFloatKey();
        }
        
        @Override
        public FloatSortedSet headSet(final float to) {
            return AbstractFloat2DoubleSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public FloatSortedSet tailSet(final float from) {
            return AbstractFloat2DoubleSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public FloatSortedSet subSet(final float from, final float to) {
            return AbstractFloat2DoubleSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public FloatBidirectionalIterator iterator(final float from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Float, Double>>)AbstractFloat2DoubleSortedMap.this.entrySet().iterator(new BasicEntry(from, 0.0)));
        }
        
        @Override
        public FloatBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractFloat2DoubleSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractFloatBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Float, Double>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Float, Double>> i) {
            this.i = i;
        }
        
        @Override
        public float nextFloat() {
            return (float)this.i.next().getKey();
        }
        
        @Override
        public float previousFloat() {
            return (float)this.i.previous().getKey();
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
            return new ValuesIterator(AbstractFloat2DoubleSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final double k) {
            return AbstractFloat2DoubleSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractFloat2DoubleSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractFloat2DoubleSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractDoubleIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Float, Double>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Float, Double>> i) {
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
