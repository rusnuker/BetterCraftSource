// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

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

public abstract class AbstractInt2BooleanSortedMap extends AbstractInt2BooleanMap implements Int2BooleanSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractInt2BooleanSortedMap() {
    }
    
    @Deprecated
    @Override
    public Int2BooleanSortedMap headMap(final Integer to) {
        return this.headMap((int)to);
    }
    
    @Deprecated
    @Override
    public Int2BooleanSortedMap tailMap(final Integer from) {
        return this.tailMap((int)from);
    }
    
    @Deprecated
    @Override
    public Int2BooleanSortedMap subMap(final Integer from, final Integer to) {
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
    public BooleanCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Integer, Boolean>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Integer, Boolean>>)this.int2BooleanEntrySet();
    }
    
    protected class KeySet extends AbstractIntSortedSet
    {
        @Override
        public boolean contains(final int k) {
            return AbstractInt2BooleanSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractInt2BooleanSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractInt2BooleanSortedMap.this.clear();
        }
        
        @Override
        public IntComparator comparator() {
            return AbstractInt2BooleanSortedMap.this.comparator();
        }
        
        @Override
        public int firstInt() {
            return AbstractInt2BooleanSortedMap.this.firstIntKey();
        }
        
        @Override
        public int lastInt() {
            return AbstractInt2BooleanSortedMap.this.lastIntKey();
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            return AbstractInt2BooleanSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            return AbstractInt2BooleanSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return AbstractInt2BooleanSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Integer, Boolean>>)AbstractInt2BooleanSortedMap.this.entrySet().iterator(new BasicEntry(from, false)));
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractInt2BooleanSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractIntBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Integer, Boolean>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Integer, Boolean>> i) {
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
    
    protected class ValuesCollection extends AbstractBooleanCollection
    {
        @Override
        public BooleanIterator iterator() {
            return new ValuesIterator(AbstractInt2BooleanSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final boolean k) {
            return AbstractInt2BooleanSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractInt2BooleanSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractInt2BooleanSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractBooleanIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Integer, Boolean>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Integer, Boolean>> i) {
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
