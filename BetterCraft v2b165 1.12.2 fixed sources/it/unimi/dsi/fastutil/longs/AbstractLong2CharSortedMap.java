// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

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

public abstract class AbstractLong2CharSortedMap extends AbstractLong2CharMap implements Long2CharSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractLong2CharSortedMap() {
    }
    
    @Deprecated
    @Override
    public Long2CharSortedMap headMap(final Long to) {
        return this.headMap((long)to);
    }
    
    @Deprecated
    @Override
    public Long2CharSortedMap tailMap(final Long from) {
        return this.tailMap((long)from);
    }
    
    @Deprecated
    @Override
    public Long2CharSortedMap subMap(final Long from, final Long to) {
        return this.subMap((long)from, (long)to);
    }
    
    @Deprecated
    @Override
    public Long firstKey() {
        return this.firstLongKey();
    }
    
    @Deprecated
    @Override
    public Long lastKey() {
        return this.lastLongKey();
    }
    
    @Override
    public LongSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public CharCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Long, Character>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Long, Character>>)this.long2CharEntrySet();
    }
    
    protected class KeySet extends AbstractLongSortedSet
    {
        @Override
        public boolean contains(final long k) {
            return AbstractLong2CharSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractLong2CharSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractLong2CharSortedMap.this.clear();
        }
        
        @Override
        public LongComparator comparator() {
            return AbstractLong2CharSortedMap.this.comparator();
        }
        
        @Override
        public long firstLong() {
            return AbstractLong2CharSortedMap.this.firstLongKey();
        }
        
        @Override
        public long lastLong() {
            return AbstractLong2CharSortedMap.this.lastLongKey();
        }
        
        @Override
        public LongSortedSet headSet(final long to) {
            return AbstractLong2CharSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public LongSortedSet tailSet(final long from) {
            return AbstractLong2CharSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            return AbstractLong2CharSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Long, Character>>)AbstractLong2CharSortedMap.this.entrySet().iterator(new BasicEntry(from, '\0')));
        }
        
        @Override
        public LongBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractLong2CharSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractLongBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Long, Character>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Long, Character>> i) {
            this.i = i;
        }
        
        @Override
        public long nextLong() {
            return (long)this.i.next().getKey();
        }
        
        @Override
        public long previousLong() {
            return (long)this.i.previous().getKey();
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
            return new ValuesIterator(AbstractLong2CharSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final char k) {
            return AbstractLong2CharSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractLong2CharSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractLong2CharSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractCharIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Long, Character>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Long, Character>> i) {
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
