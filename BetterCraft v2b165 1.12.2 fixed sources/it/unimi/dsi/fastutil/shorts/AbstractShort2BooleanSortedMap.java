// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

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

public abstract class AbstractShort2BooleanSortedMap extends AbstractShort2BooleanMap implements Short2BooleanSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractShort2BooleanSortedMap() {
    }
    
    @Deprecated
    @Override
    public Short2BooleanSortedMap headMap(final Short to) {
        return this.headMap((short)to);
    }
    
    @Deprecated
    @Override
    public Short2BooleanSortedMap tailMap(final Short from) {
        return this.tailMap((short)from);
    }
    
    @Deprecated
    @Override
    public Short2BooleanSortedMap subMap(final Short from, final Short to) {
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
    public BooleanCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Short, Boolean>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Short, Boolean>>)this.short2BooleanEntrySet();
    }
    
    protected class KeySet extends AbstractShortSortedSet
    {
        @Override
        public boolean contains(final short k) {
            return AbstractShort2BooleanSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractShort2BooleanSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractShort2BooleanSortedMap.this.clear();
        }
        
        @Override
        public ShortComparator comparator() {
            return AbstractShort2BooleanSortedMap.this.comparator();
        }
        
        @Override
        public short firstShort() {
            return AbstractShort2BooleanSortedMap.this.firstShortKey();
        }
        
        @Override
        public short lastShort() {
            return AbstractShort2BooleanSortedMap.this.lastShortKey();
        }
        
        @Override
        public ShortSortedSet headSet(final short to) {
            return AbstractShort2BooleanSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ShortSortedSet tailSet(final short from) {
            return AbstractShort2BooleanSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ShortSortedSet subSet(final short from, final short to) {
            return AbstractShort2BooleanSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Short, Boolean>>)AbstractShort2BooleanSortedMap.this.entrySet().iterator(new BasicEntry(from, false)));
        }
        
        @Override
        public ShortBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractShort2BooleanSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractShortBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Short, Boolean>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Short, Boolean>> i) {
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
    
    protected class ValuesCollection extends AbstractBooleanCollection
    {
        @Override
        public BooleanIterator iterator() {
            return new ValuesIterator(AbstractShort2BooleanSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final boolean k) {
            return AbstractShort2BooleanSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractShort2BooleanSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractShort2BooleanSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractBooleanIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Short, Boolean>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Short, Boolean>> i) {
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
