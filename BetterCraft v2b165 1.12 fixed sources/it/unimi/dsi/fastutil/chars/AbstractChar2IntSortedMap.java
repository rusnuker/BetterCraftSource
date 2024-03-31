// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

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

public abstract class AbstractChar2IntSortedMap extends AbstractChar2IntMap implements Char2IntSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractChar2IntSortedMap() {
    }
    
    @Deprecated
    @Override
    public Char2IntSortedMap headMap(final Character to) {
        return this.headMap((char)to);
    }
    
    @Deprecated
    @Override
    public Char2IntSortedMap tailMap(final Character from) {
        return this.tailMap((char)from);
    }
    
    @Deprecated
    @Override
    public Char2IntSortedMap subMap(final Character from, final Character to) {
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
    public IntCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Character, Integer>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Character, Integer>>)this.char2IntEntrySet();
    }
    
    protected class KeySet extends AbstractCharSortedSet
    {
        @Override
        public boolean contains(final char k) {
            return AbstractChar2IntSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractChar2IntSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractChar2IntSortedMap.this.clear();
        }
        
        @Override
        public CharComparator comparator() {
            return AbstractChar2IntSortedMap.this.comparator();
        }
        
        @Override
        public char firstChar() {
            return AbstractChar2IntSortedMap.this.firstCharKey();
        }
        
        @Override
        public char lastChar() {
            return AbstractChar2IntSortedMap.this.lastCharKey();
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            return AbstractChar2IntSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            return AbstractChar2IntSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            return AbstractChar2IntSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Character, Integer>>)AbstractChar2IntSortedMap.this.entrySet().iterator(new BasicEntry(from, 0)));
        }
        
        @Override
        public CharBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractChar2IntSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractCharBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Character, Integer>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Character, Integer>> i) {
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
    
    protected class ValuesCollection extends AbstractIntCollection
    {
        @Override
        public IntIterator iterator() {
            return new ValuesIterator(AbstractChar2IntSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final int k) {
            return AbstractChar2IntSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractChar2IntSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractChar2IntSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractIntIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Character, Integer>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Character, Integer>> i) {
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
