// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

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

public abstract class AbstractInt2ShortSortedMap extends AbstractInt2ShortMap implements Int2ShortSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractInt2ShortSortedMap() {
    }
    
    @Deprecated
    @Override
    public Int2ShortSortedMap headMap(final Integer to) {
        return this.headMap((int)to);
    }
    
    @Deprecated
    @Override
    public Int2ShortSortedMap tailMap(final Integer from) {
        return this.tailMap((int)from);
    }
    
    @Deprecated
    @Override
    public Int2ShortSortedMap subMap(final Integer from, final Integer to) {
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
    public ShortCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Integer, Short>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Integer, Short>>)this.int2ShortEntrySet();
    }
    
    protected class KeySet extends AbstractIntSortedSet
    {
        @Override
        public boolean contains(final int k) {
            return AbstractInt2ShortSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractInt2ShortSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractInt2ShortSortedMap.this.clear();
        }
        
        @Override
        public IntComparator comparator() {
            return AbstractInt2ShortSortedMap.this.comparator();
        }
        
        @Override
        public int firstInt() {
            return AbstractInt2ShortSortedMap.this.firstIntKey();
        }
        
        @Override
        public int lastInt() {
            return AbstractInt2ShortSortedMap.this.lastIntKey();
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            return AbstractInt2ShortSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            return AbstractInt2ShortSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return AbstractInt2ShortSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Integer, Short>>)AbstractInt2ShortSortedMap.this.entrySet().iterator(new BasicEntry(from, (short)0)));
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractInt2ShortSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractIntBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Integer, Short>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Integer, Short>> i) {
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
    
    protected class ValuesCollection extends AbstractShortCollection
    {
        @Override
        public ShortIterator iterator() {
            return new ValuesIterator(AbstractInt2ShortSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final short k) {
            return AbstractInt2ShortSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractInt2ShortSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractInt2ShortSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractShortIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Integer, Short>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Integer, Short>> i) {
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
