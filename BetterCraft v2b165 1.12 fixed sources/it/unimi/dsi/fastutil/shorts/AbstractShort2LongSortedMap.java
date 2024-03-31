// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.longs.AbstractLongIterator;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.longs.LongCollection;

public abstract class AbstractShort2LongSortedMap extends AbstractShort2LongMap implements Short2LongSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractShort2LongSortedMap() {
    }
    
    @Deprecated
    @Override
    public Short2LongSortedMap headMap(final Short to) {
        return this.headMap((short)to);
    }
    
    @Deprecated
    @Override
    public Short2LongSortedMap tailMap(final Short from) {
        return this.tailMap((short)from);
    }
    
    @Deprecated
    @Override
    public Short2LongSortedMap subMap(final Short from, final Short to) {
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
    public LongCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Short, Long>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Short, Long>>)this.short2LongEntrySet();
    }
    
    protected class KeySet extends AbstractShortSortedSet
    {
        @Override
        public boolean contains(final short k) {
            return AbstractShort2LongSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractShort2LongSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractShort2LongSortedMap.this.clear();
        }
        
        @Override
        public ShortComparator comparator() {
            return AbstractShort2LongSortedMap.this.comparator();
        }
        
        @Override
        public short firstShort() {
            return AbstractShort2LongSortedMap.this.firstShortKey();
        }
        
        @Override
        public short lastShort() {
            return AbstractShort2LongSortedMap.this.lastShortKey();
        }
        
        @Override
        public ShortSortedSet headSet(final short to) {
            return AbstractShort2LongSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ShortSortedSet tailSet(final short from) {
            return AbstractShort2LongSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ShortSortedSet subSet(final short from, final short to) {
            return AbstractShort2LongSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Short, Long>>)AbstractShort2LongSortedMap.this.entrySet().iterator(new BasicEntry(from, 0L)));
        }
        
        @Override
        public ShortBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractShort2LongSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractShortBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Short, Long>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Short, Long>> i) {
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
    
    protected class ValuesCollection extends AbstractLongCollection
    {
        @Override
        public LongIterator iterator() {
            return new ValuesIterator(AbstractShort2LongSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final long k) {
            return AbstractShort2LongSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractShort2LongSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractShort2LongSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractLongIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Short, Long>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Short, Long>> i) {
            this.i = i;
        }
        
        @Override
        public long nextLong() {
            return (long)this.i.next().getValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
