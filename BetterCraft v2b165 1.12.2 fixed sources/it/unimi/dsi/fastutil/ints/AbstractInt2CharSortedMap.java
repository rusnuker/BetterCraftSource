// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

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

public abstract class AbstractInt2CharSortedMap extends AbstractInt2CharMap implements Int2CharSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractInt2CharSortedMap() {
    }
    
    @Deprecated
    @Override
    public Int2CharSortedMap headMap(final Integer to) {
        return this.headMap((int)to);
    }
    
    @Deprecated
    @Override
    public Int2CharSortedMap tailMap(final Integer from) {
        return this.tailMap((int)from);
    }
    
    @Deprecated
    @Override
    public Int2CharSortedMap subMap(final Integer from, final Integer to) {
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
    public CharCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Integer, Character>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Integer, Character>>)this.int2CharEntrySet();
    }
    
    protected class KeySet extends AbstractIntSortedSet
    {
        @Override
        public boolean contains(final int k) {
            return AbstractInt2CharSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractInt2CharSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractInt2CharSortedMap.this.clear();
        }
        
        @Override
        public IntComparator comparator() {
            return AbstractInt2CharSortedMap.this.comparator();
        }
        
        @Override
        public int firstInt() {
            return AbstractInt2CharSortedMap.this.firstIntKey();
        }
        
        @Override
        public int lastInt() {
            return AbstractInt2CharSortedMap.this.lastIntKey();
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            return AbstractInt2CharSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            return AbstractInt2CharSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return AbstractInt2CharSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Integer, Character>>)AbstractInt2CharSortedMap.this.entrySet().iterator(new BasicEntry(from, '\0')));
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractInt2CharSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractIntBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Integer, Character>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Integer, Character>> i) {
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
    
    protected class ValuesCollection extends AbstractCharCollection
    {
        @Override
        public CharIterator iterator() {
            return new ValuesIterator(AbstractInt2CharSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final char k) {
            return AbstractInt2CharSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractInt2CharSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractInt2CharSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractCharIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Integer, Character>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Integer, Character>> i) {
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
