// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

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

public abstract class AbstractByte2CharSortedMap extends AbstractByte2CharMap implements Byte2CharSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractByte2CharSortedMap() {
    }
    
    @Deprecated
    @Override
    public Byte2CharSortedMap headMap(final Byte to) {
        return this.headMap((byte)to);
    }
    
    @Deprecated
    @Override
    public Byte2CharSortedMap tailMap(final Byte from) {
        return this.tailMap((byte)from);
    }
    
    @Deprecated
    @Override
    public Byte2CharSortedMap subMap(final Byte from, final Byte to) {
        return this.subMap((byte)from, (byte)to);
    }
    
    @Deprecated
    @Override
    public Byte firstKey() {
        return this.firstByteKey();
    }
    
    @Deprecated
    @Override
    public Byte lastKey() {
        return this.lastByteKey();
    }
    
    @Override
    public ByteSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public CharCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Byte, Character>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Byte, Character>>)this.byte2CharEntrySet();
    }
    
    protected class KeySet extends AbstractByteSortedSet
    {
        @Override
        public boolean contains(final byte k) {
            return AbstractByte2CharSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractByte2CharSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractByte2CharSortedMap.this.clear();
        }
        
        @Override
        public ByteComparator comparator() {
            return AbstractByte2CharSortedMap.this.comparator();
        }
        
        @Override
        public byte firstByte() {
            return AbstractByte2CharSortedMap.this.firstByteKey();
        }
        
        @Override
        public byte lastByte() {
            return AbstractByte2CharSortedMap.this.lastByteKey();
        }
        
        @Override
        public ByteSortedSet headSet(final byte to) {
            return AbstractByte2CharSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ByteSortedSet tailSet(final byte from) {
            return AbstractByte2CharSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ByteSortedSet subSet(final byte from, final byte to) {
            return AbstractByte2CharSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Byte, Character>>)AbstractByte2CharSortedMap.this.entrySet().iterator(new BasicEntry(from, '\0')));
        }
        
        @Override
        public ByteBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractByte2CharSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractByteBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Byte, Character>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Byte, Character>> i) {
            this.i = i;
        }
        
        @Override
        public byte nextByte() {
            return (byte)this.i.next().getKey();
        }
        
        @Override
        public byte previousByte() {
            return (byte)this.i.previous().getKey();
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
            return new ValuesIterator(AbstractByte2CharSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final char k) {
            return AbstractByte2CharSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractByte2CharSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractByte2CharSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractCharIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Byte, Character>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Byte, Character>> i) {
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
