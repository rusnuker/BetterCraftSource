// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.AbstractObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;

public abstract class AbstractChar2ReferenceSortedMap<V> extends AbstractChar2ReferenceMap<V> implements Char2ReferenceSortedMap<V>
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractChar2ReferenceSortedMap() {
    }
    
    @Deprecated
    @Override
    public Char2ReferenceSortedMap<V> headMap(final Character to) {
        return this.headMap((char)to);
    }
    
    @Deprecated
    @Override
    public Char2ReferenceSortedMap<V> tailMap(final Character from) {
        return this.tailMap((char)from);
    }
    
    @Deprecated
    @Override
    public Char2ReferenceSortedMap<V> subMap(final Character from, final Character to) {
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
    public ReferenceCollection<V> values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Character, V>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Character, V>>)this.char2ReferenceEntrySet();
    }
    
    protected class KeySet extends AbstractCharSortedSet
    {
        @Override
        public boolean contains(final char k) {
            return AbstractChar2ReferenceSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractChar2ReferenceSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractChar2ReferenceSortedMap.this.clear();
        }
        
        @Override
        public CharComparator comparator() {
            return AbstractChar2ReferenceSortedMap.this.comparator();
        }
        
        @Override
        public char firstChar() {
            return AbstractChar2ReferenceSortedMap.this.firstCharKey();
        }
        
        @Override
        public char lastChar() {
            return AbstractChar2ReferenceSortedMap.this.lastCharKey();
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            return AbstractChar2ReferenceSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            return AbstractChar2ReferenceSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            return AbstractChar2ReferenceSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return new KeySetIterator<Object>((ObjectBidirectionalIterator<Map.Entry<Character, ?>>)AbstractChar2ReferenceSortedMap.this.entrySet().iterator(new BasicEntry<V>(from, null)));
        }
        
        @Override
        public CharBidirectionalIterator iterator() {
            return new KeySetIterator<Object>((ObjectBidirectionalIterator<Map.Entry<Character, ?>>)AbstractChar2ReferenceSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator<V> extends AbstractCharBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Character, V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Character, V>> i) {
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
    
    protected class ValuesCollection extends AbstractReferenceCollection<V>
    {
        @Override
        public ObjectIterator<V> iterator() {
            return new ValuesIterator<V>(AbstractChar2ReferenceSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final Object k) {
            return AbstractChar2ReferenceSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractChar2ReferenceSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractChar2ReferenceSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<V> extends AbstractObjectIterator<V>
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Character, V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Character, V>> i) {
            this.i = i;
        }
        
        @Override
        public V next() {
            return (V)this.i.next().getValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
