// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.doubles.AbstractDoubleIterator;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;

public abstract class AbstractChar2DoubleSortedMap extends AbstractChar2DoubleMap implements Char2DoubleSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractChar2DoubleSortedMap() {
    }
    
    @Deprecated
    @Override
    public Char2DoubleSortedMap headMap(final Character to) {
        return this.headMap((char)to);
    }
    
    @Deprecated
    @Override
    public Char2DoubleSortedMap tailMap(final Character from) {
        return this.tailMap((char)from);
    }
    
    @Deprecated
    @Override
    public Char2DoubleSortedMap subMap(final Character from, final Character to) {
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
    public DoubleCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Character, Double>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Character, Double>>)this.char2DoubleEntrySet();
    }
    
    protected class KeySet extends AbstractCharSortedSet
    {
        @Override
        public boolean contains(final char k) {
            return AbstractChar2DoubleSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractChar2DoubleSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractChar2DoubleSortedMap.this.clear();
        }
        
        @Override
        public CharComparator comparator() {
            return AbstractChar2DoubleSortedMap.this.comparator();
        }
        
        @Override
        public char firstChar() {
            return AbstractChar2DoubleSortedMap.this.firstCharKey();
        }
        
        @Override
        public char lastChar() {
            return AbstractChar2DoubleSortedMap.this.lastCharKey();
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            return AbstractChar2DoubleSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            return AbstractChar2DoubleSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            return AbstractChar2DoubleSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Character, Double>>)AbstractChar2DoubleSortedMap.this.entrySet().iterator(new BasicEntry(from, 0.0)));
        }
        
        @Override
        public CharBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractChar2DoubleSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractCharBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Character, Double>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Character, Double>> i) {
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
    
    protected class ValuesCollection extends AbstractDoubleCollection
    {
        @Override
        public DoubleIterator iterator() {
            return new ValuesIterator(AbstractChar2DoubleSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final double k) {
            return AbstractChar2DoubleSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractChar2DoubleSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractChar2DoubleSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractDoubleIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Character, Double>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Character, Double>> i) {
            this.i = i;
        }
        
        @Override
        public double nextDouble() {
            return (double)this.i.next().getValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
