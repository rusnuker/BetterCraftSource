// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.bytes.AbstractByteIterator;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.bytes.ByteCollection;

public abstract class AbstractChar2ByteSortedMap extends AbstractChar2ByteMap implements Char2ByteSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractChar2ByteSortedMap() {
    }
    
    @Deprecated
    @Override
    public Char2ByteSortedMap headMap(final Character to) {
        return this.headMap((char)to);
    }
    
    @Deprecated
    @Override
    public Char2ByteSortedMap tailMap(final Character from) {
        return this.tailMap((char)from);
    }
    
    @Deprecated
    @Override
    public Char2ByteSortedMap subMap(final Character from, final Character to) {
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
    public ByteCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Character, Byte>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Character, Byte>>)this.char2ByteEntrySet();
    }
    
    protected class KeySet extends AbstractCharSortedSet
    {
        @Override
        public boolean contains(final char k) {
            return AbstractChar2ByteSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractChar2ByteSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractChar2ByteSortedMap.this.clear();
        }
        
        @Override
        public CharComparator comparator() {
            return AbstractChar2ByteSortedMap.this.comparator();
        }
        
        @Override
        public char firstChar() {
            return AbstractChar2ByteSortedMap.this.firstCharKey();
        }
        
        @Override
        public char lastChar() {
            return AbstractChar2ByteSortedMap.this.lastCharKey();
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            return AbstractChar2ByteSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            return AbstractChar2ByteSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            return AbstractChar2ByteSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Character, Byte>>)AbstractChar2ByteSortedMap.this.entrySet().iterator(new BasicEntry(from, (byte)0)));
        }
        
        @Override
        public CharBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractChar2ByteSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractCharBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Character, Byte>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Character, Byte>> i) {
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
    
    protected class ValuesCollection extends AbstractByteCollection
    {
        @Override
        public ByteIterator iterator() {
            return new ValuesIterator(AbstractChar2ByteSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final byte k) {
            return AbstractChar2ByteSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractChar2ByteSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractChar2ByteSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractByteIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Character, Byte>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Character, Byte>> i) {
            this.i = i;
        }
        
        @Override
        public byte nextByte() {
            return (byte)this.i.next().getValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
