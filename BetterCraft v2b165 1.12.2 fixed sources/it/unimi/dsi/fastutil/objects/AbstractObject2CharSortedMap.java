// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.chars.AbstractCharIterator;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import java.util.Map;
import it.unimi.dsi.fastutil.chars.CharCollection;

public abstract class AbstractObject2CharSortedMap<K> extends AbstractObject2CharMap<K> implements Object2CharSortedMap<K>
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractObject2CharSortedMap() {
    }
    
    @Override
    public ObjectSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public CharCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<K, Character>> entrySet() {
        return (ObjectSortedSet<Map.Entry<K, Character>>)this.object2CharEntrySet();
    }
    
    protected class KeySet extends AbstractObjectSortedSet<K>
    {
        @Override
        public boolean contains(final Object k) {
            return AbstractObject2CharSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractObject2CharSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractObject2CharSortedMap.this.clear();
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return AbstractObject2CharSortedMap.this.comparator();
        }
        
        @Override
        public K first() {
            return AbstractObject2CharSortedMap.this.firstKey();
        }
        
        @Override
        public K last() {
            return AbstractObject2CharSortedMap.this.lastKey();
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            return AbstractObject2CharSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            return AbstractObject2CharSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            return AbstractObject2CharSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Map.Entry<K, Character>>)AbstractObject2CharSortedMap.this.entrySet().iterator(new BasicEntry<K>(from, '\0')));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>(AbstractObject2CharSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator<K> extends AbstractObjectBidirectionalIterator<K>
    {
        protected final ObjectBidirectionalIterator<Map.Entry<K, Character>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<K, Character>> i) {
            this.i = i;
        }
        
        @Override
        public K next() {
            return (K)this.i.next().getKey();
        }
        
        @Override
        public K previous() {
            return (K)this.i.previous().getKey();
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
            return new ValuesIterator<Object>((ObjectBidirectionalIterator<Map.Entry<?, Character>>)AbstractObject2CharSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final char k) {
            return AbstractObject2CharSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractObject2CharSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractObject2CharSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> extends AbstractCharIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<K, Character>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<K, Character>> i) {
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
