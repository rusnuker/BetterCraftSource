// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

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

public abstract class AbstractShort2IntSortedMap extends AbstractShort2IntMap implements Short2IntSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractShort2IntSortedMap() {
    }
    
    @Deprecated
    @Override
    public Short2IntSortedMap headMap(final Short to) {
        return this.headMap((short)to);
    }
    
    @Deprecated
    @Override
    public Short2IntSortedMap tailMap(final Short from) {
        return this.tailMap((short)from);
    }
    
    @Deprecated
    @Override
    public Short2IntSortedMap subMap(final Short from, final Short to) {
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
    public IntCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Short, Integer>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Short, Integer>>)this.short2IntEntrySet();
    }
    
    protected class KeySet extends AbstractShortSortedSet
    {
        @Override
        public boolean contains(final short k) {
            return AbstractShort2IntSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractShort2IntSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractShort2IntSortedMap.this.clear();
        }
        
        @Override
        public ShortComparator comparator() {
            return AbstractShort2IntSortedMap.this.comparator();
        }
        
        @Override
        public short firstShort() {
            return AbstractShort2IntSortedMap.this.firstShortKey();
        }
        
        @Override
        public short lastShort() {
            return AbstractShort2IntSortedMap.this.lastShortKey();
        }
        
        @Override
        public ShortSortedSet headSet(final short to) {
            return AbstractShort2IntSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ShortSortedSet tailSet(final short from) {
            return AbstractShort2IntSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ShortSortedSet subSet(final short from, final short to) {
            return AbstractShort2IntSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Short, Integer>>)AbstractShort2IntSortedMap.this.entrySet().iterator(new BasicEntry(from, 0)));
        }
        
        @Override
        public ShortBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractShort2IntSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractShortBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Short, Integer>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Short, Integer>> i) {
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
    
    protected class ValuesCollection extends AbstractIntCollection
    {
        @Override
        public IntIterator iterator() {
            return new ValuesIterator(AbstractShort2IntSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final int k) {
            return AbstractShort2IntSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractShort2IntSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractShort2IntSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractIntIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Short, Integer>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Short, Integer>> i) {
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
