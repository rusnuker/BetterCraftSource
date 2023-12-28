// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

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

public abstract class AbstractChar2LongSortedMap extends AbstractChar2LongMap implements Char2LongSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractChar2LongSortedMap() {
    }
    
    @Deprecated
    @Override
    public Char2LongSortedMap headMap(final Character to) {
        return this.headMap((char)to);
    }
    
    @Deprecated
    @Override
    public Char2LongSortedMap tailMap(final Character from) {
        return this.tailMap((char)from);
    }
    
    @Deprecated
    @Override
    public Char2LongSortedMap subMap(final Character from, final Character to) {
        return this.subMap((char)from, (char)to);
    }
    
    @Deprecated
    @Override
    public Character firstKey() {
        return this.firstCharKey();
    }
    
    @Deprecated
    @Override
    public Character lastKey() {
        return this.lastCharKey();
    }
    
    @Override
    public CharSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public LongCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Character, Long>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Character, Long>>)this.char2LongEntrySet();
    }
    
    protected class KeySet extends AbstractCharSortedSet
    {
        @Override
        public boolean contains(final char k) {
            return AbstractChar2LongSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractChar2LongSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractChar2LongSortedMap.this.clear();
        }
        
        @Override
        public CharComparator comparator() {
            return AbstractChar2LongSortedMap.this.comparator();
        }
        
        @Override
        public char firstChar() {
            return AbstractChar2LongSortedMap.this.firstCharKey();
        }
        
        @Override
        public char lastChar() {
            return AbstractChar2LongSortedMap.this.lastCharKey();
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            return AbstractChar2LongSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            return AbstractChar2LongSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            return AbstractChar2LongSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Character, Long>>)AbstractChar2LongSortedMap.this.entrySet().iterator(new BasicEntry(from, 0L)));
        }
        
        @Override
        public CharBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractChar2LongSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractCharBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Character, Long>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Character, Long>> i) {
            this.i = i;
        }
        
        @Override
        public char nextChar() {
            return (char)this.i.next().getKey();
        }
        
        @Override
        public char previousChar() {
            return (char)this.i.previous().getKey();
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
            return new ValuesIterator(AbstractChar2LongSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final long k) {
            return AbstractChar2LongSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractChar2LongSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractChar2LongSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractLongIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Character, Long>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Character, Long>> i) {
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
