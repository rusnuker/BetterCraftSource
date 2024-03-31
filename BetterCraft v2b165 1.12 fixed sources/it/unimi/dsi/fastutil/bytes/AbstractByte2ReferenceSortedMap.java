// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

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

public abstract class AbstractByte2ReferenceSortedMap<V> extends AbstractByte2ReferenceMap<V> implements Byte2ReferenceSortedMap<V>
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractByte2ReferenceSortedMap() {
    }
    
    @Deprecated
    @Override
    public Byte2ReferenceSortedMap<V> headMap(final Byte to) {
        return this.headMap((byte)to);
    }
    
    @Deprecated
    @Override
    public Byte2ReferenceSortedMap<V> tailMap(final Byte from) {
        return this.tailMap((byte)from);
    }
    
    @Deprecated
    @Override
    public Byte2ReferenceSortedMap<V> subMap(final Byte from, final Byte to) {
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
    public ReferenceCollection<V> values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Byte, V>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Byte, V>>)this.byte2ReferenceEntrySet();
    }
    
    protected class KeySet extends AbstractByteSortedSet
    {
        @Override
        public boolean contains(final byte k) {
            return AbstractByte2ReferenceSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractByte2ReferenceSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractByte2ReferenceSortedMap.this.clear();
        }
        
        @Override
        public ByteComparator comparator() {
            return AbstractByte2ReferenceSortedMap.this.comparator();
        }
        
        @Override
        public byte firstByte() {
            return AbstractByte2ReferenceSortedMap.this.firstByteKey();
        }
        
        @Override
        public byte lastByte() {
            return AbstractByte2ReferenceSortedMap.this.lastByteKey();
        }
        
        @Override
        public ByteSortedSet headSet(final byte to) {
            return AbstractByte2ReferenceSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ByteSortedSet tailSet(final byte from) {
            return AbstractByte2ReferenceSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ByteSortedSet subSet(final byte from, final byte to) {
            return AbstractByte2ReferenceSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            return new KeySetIterator<Object>((ObjectBidirectionalIterator<Map.Entry<Byte, ?>>)AbstractByte2ReferenceSortedMap.this.entrySet().iterator(new BasicEntry<V>(from, null)));
        }
        
        @Override
        public ByteBidirectionalIterator iterator() {
            return new KeySetIterator<Object>((ObjectBidirectionalIterator<Map.Entry<Byte, ?>>)AbstractByte2ReferenceSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator<V> extends AbstractByteBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Byte, V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Byte, V>> i) {
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
    
    protected class ValuesCollection extends AbstractReferenceCollection<V>
    {
        @Override
        public ObjectIterator<V> iterator() {
            return new ValuesIterator<V>(AbstractByte2ReferenceSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final Object k) {
            return AbstractByte2ReferenceSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractByte2ReferenceSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractByte2ReferenceSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<V> extends AbstractObjectIterator<V>
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Byte, V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Byte, V>> i) {
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
