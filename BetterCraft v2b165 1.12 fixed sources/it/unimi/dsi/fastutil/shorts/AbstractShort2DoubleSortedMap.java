// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

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

public abstract class AbstractShort2DoubleSortedMap extends AbstractShort2DoubleMap implements Short2DoubleSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractShort2DoubleSortedMap() {
    }
    
    @Deprecated
    @Override
    public Short2DoubleSortedMap headMap(final Short to) {
        return this.headMap((short)to);
    }
    
    @Deprecated
    @Override
    public Short2DoubleSortedMap tailMap(final Short from) {
        return this.tailMap((short)from);
    }
    
    @Deprecated
    @Override
    public Short2DoubleSortedMap subMap(final Short from, final Short to) {
        return this.subMap((short)from, (short)to);
    }
    
    @Deprecated
    @Override
    public Short firstKey() {
        return this.firstShortKey();
    }
    
    @Deprecated
    @Override
    public Short lastKey() {
        return this.lastShortKey();
    }
    
    @Override
    public ShortSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public DoubleCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Short, Double>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Short, Double>>)this.short2DoubleEntrySet();
    }
    
    protected class KeySet extends AbstractShortSortedSet
    {
        @Override
        public boolean contains(final short k) {
            return AbstractShort2DoubleSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractShort2DoubleSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractShort2DoubleSortedMap.this.clear();
        }
        
        @Override
        public ShortComparator comparator() {
            return AbstractShort2DoubleSortedMap.this.comparator();
        }
        
        @Override
        public short firstShort() {
            return AbstractShort2DoubleSortedMap.this.firstShortKey();
        }
        
        @Override
        public short lastShort() {
            return AbstractShort2DoubleSortedMap.this.lastShortKey();
        }
        
        @Override
        public ShortSortedSet headSet(final short to) {
            return AbstractShort2DoubleSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ShortSortedSet tailSet(final short from) {
            return AbstractShort2DoubleSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ShortSortedSet subSet(final short from, final short to) {
            return AbstractShort2DoubleSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Short, Double>>)AbstractShort2DoubleSortedMap.this.entrySet().iterator(new BasicEntry(from, 0.0)));
        }
        
        @Override
        public ShortBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractShort2DoubleSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractShortBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Short, Double>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Short, Double>> i) {
            this.i = i;
        }
        
        @Override
        public short nextShort() {
            return (short)this.i.next().getKey();
        }
        
        @Override
        public short previousShort() {
            return (short)this.i.previous().getKey();
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
            return new ValuesIterator(AbstractShort2DoubleSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final double k) {
            return AbstractShort2DoubleSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractShort2DoubleSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractShort2DoubleSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractDoubleIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Short, Double>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Short, Double>> i) {
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
