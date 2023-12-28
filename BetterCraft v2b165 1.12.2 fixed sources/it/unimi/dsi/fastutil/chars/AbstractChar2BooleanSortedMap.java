// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

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

public abstract class AbstractChar2BooleanSortedMap extends AbstractChar2BooleanMap implements Char2BooleanSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractChar2BooleanSortedMap() {
    }
    
    @Deprecated
    @Override
    public Char2BooleanSortedMap headMap(final Character to) {
        return this.headMap((char)to);
    }
    
    @Deprecated
    @Override
    public Char2BooleanSortedMap tailMap(final Character from) {
        return this.tailMap((char)from);
    }
    
    @Deprecated
    @Override
    public Char2BooleanSortedMap subMap(final Character from, final Character to) {
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
    public BooleanCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Character, Boolean>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Character, Boolean>>)this.char2BooleanEntrySet();
    }
    
    protected class KeySet extends AbstractCharSortedSet
    {
        @Override
        public boolean contains(final char k) {
            return AbstractChar2BooleanSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractChar2BooleanSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractChar2BooleanSortedMap.this.clear();
        }
        
        @Override
        public CharComparator comparator() {
            return AbstractChar2BooleanSortedMap.this.comparator();
        }
        
        @Override
        public char firstChar() {
            return AbstractChar2BooleanSortedMap.this.firstCharKey();
        }
        
        @Override
        public char lastChar() {
            return AbstractChar2BooleanSortedMap.this.lastCharKey();
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            return AbstractChar2BooleanSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            return AbstractChar2BooleanSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            return AbstractChar2BooleanSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Character, Boolean>>)AbstractChar2BooleanSortedMap.this.entrySet().iterator(new BasicEntry(from, false)));
        }
        
        @Override
        public CharBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractChar2BooleanSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractCharBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Character, Boolean>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Character, Boolean>> i) {
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
    
    protected class ValuesCollection extends AbstractBooleanCollection
    {
        @Override
        public BooleanIterator iterator() {
            return new ValuesIterator(AbstractChar2BooleanSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final boolean k) {
            return AbstractChar2BooleanSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractChar2BooleanSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractChar2BooleanSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractBooleanIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Character, Boolean>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Character, Boolean>> i) {
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
