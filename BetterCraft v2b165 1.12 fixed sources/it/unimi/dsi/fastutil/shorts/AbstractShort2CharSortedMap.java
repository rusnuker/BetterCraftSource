// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.chars.AbstractCharIterator;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.chars.CharCollection;

public abstract class AbstractShort2CharSortedMap extends AbstractShort2CharMap implements Short2CharSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractShort2CharSortedMap() {
    }
    
    @Deprecated
    @Override
    public Short2CharSortedMap headMap(final Short to) {
        return this.headMap((short)to);
    }
    
    @Deprecated
    @Override
    public Short2CharSortedMap tailMap(final Short from) {
        return this.tailMap((short)from);
    }
    
    @Deprecated
    @Override
    public Short2CharSortedMap subMap(final Short from, final Short to) {
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
    public CharCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Short, Character>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Short, Character>>)this.short2CharEntrySet();
    }
    
    protected class KeySet extends AbstractShortSortedSet
    {
        @Override
        public boolean contains(final short k) {
            return AbstractShort2CharSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractShort2CharSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractShort2CharSortedMap.this.clear();
        }
        
        @Override
        public ShortComparator comparator() {
            return AbstractShort2CharSortedMap.this.comparator();
        }
        
        @Override
        public short firstShort() {
            return AbstractShort2CharSortedMap.this.firstShortKey();
        }
        
        @Override
        public short lastShort() {
            return AbstractShort2CharSortedMap.this.lastShortKey();
        }
        
        @Override
        public ShortSortedSet headSet(final short to) {
            return AbstractShort2CharSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ShortSortedSet tailSet(final short from) {
            return AbstractShort2CharSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ShortSortedSet subSet(final short from, final short to) {
            return AbstractShort2CharSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Short, Character>>)AbstractShort2CharSortedMap.this.entrySet().iterator(new BasicEntry(from, '\0')));
        }
        
        @Override
        public ShortBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractShort2CharSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractShortBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Short, Character>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Short, Character>> i) {
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
    
    protected class ValuesCollection extends AbstractCharCollection
    {
        @Override
        public CharIterator iterator() {
            return new ValuesIterator(AbstractShort2CharSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final char k) {
            return AbstractShort2CharSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractShort2CharSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractShort2CharSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractCharIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Short, Character>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Short, Character>> i) {
            this.i = i;
        }
        
        @Override
        public char nextChar() {
            return (char)this.i.next().getValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
