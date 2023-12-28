// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;

public abstract class AbstractShort2ShortSortedMap extends AbstractShort2ShortMap implements Short2ShortSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractShort2ShortSortedMap() {
    }
    
    @Deprecated
    @Override
    public Short2ShortSortedMap headMap(final Short to) {
        return this.headMap((short)to);
    }
    
    @Deprecated
    @Override
    public Short2ShortSortedMap tailMap(final Short from) {
        return this.tailMap((short)from);
    }
    
    @Deprecated
    @Override
    public Short2ShortSortedMap subMap(final Short from, final Short to) {
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
    public ShortCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Short, Short>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Short, Short>>)this.short2ShortEntrySet();
    }
    
    protected class KeySet extends AbstractShortSortedSet
    {
        @Override
        public boolean contains(final short k) {
            return AbstractShort2ShortSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractShort2ShortSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractShort2ShortSortedMap.this.clear();
        }
        
        @Override
        public ShortComparator comparator() {
            return AbstractShort2ShortSortedMap.this.comparator();
        }
        
        @Override
        public short firstShort() {
            return AbstractShort2ShortSortedMap.this.firstShortKey();
        }
        
        @Override
        public short lastShort() {
            return AbstractShort2ShortSortedMap.this.lastShortKey();
        }
        
        @Override
        public ShortSortedSet headSet(final short to) {
            return AbstractShort2ShortSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ShortSortedSet tailSet(final short from) {
            return AbstractShort2ShortSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ShortSortedSet subSet(final short from, final short to) {
            return AbstractShort2ShortSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Short, Short>>)AbstractShort2ShortSortedMap.this.entrySet().iterator(new BasicEntry(from, (short)0)));
        }
        
        @Override
        public ShortBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractShort2ShortSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractShortBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Short, Short>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Short, Short>> i) {
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
    
    protected class ValuesCollection extends AbstractShortCollection
    {
        @Override
        public ShortIterator iterator() {
            return new ValuesIterator(AbstractShort2ShortSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final short k) {
            return AbstractShort2ShortSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractShort2ShortSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractShort2ShortSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractShortIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Short, Short>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Short, Short>> i) {
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
