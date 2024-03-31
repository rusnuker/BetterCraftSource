// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.bytes.AbstractByteIterator;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import java.util.Map;
import it.unimi.dsi.fastutil.bytes.ByteCollection;

public abstract class AbstractReference2ByteSortedMap<K> extends AbstractReference2ByteMap<K> implements Reference2ByteSortedMap<K>
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractReference2ByteSortedMap() {
    }
    
    @Override
    public ReferenceSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public ByteCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<K, Byte>> entrySet() {
        return (ObjectSortedSet<Map.Entry<K, Byte>>)this.reference2ByteEntrySet();
    }
    
    protected class KeySet extends AbstractReferenceSortedSet<K>
    {
        @Override
        public boolean contains(final Object k) {
            return AbstractReference2ByteSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractReference2ByteSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractReference2ByteSortedMap.this.clear();
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return AbstractReference2ByteSortedMap.this.comparator();
        }
        
        @Override
        public K first() {
            return AbstractReference2ByteSortedMap.this.firstKey();
        }
        
        @Override
        public K last() {
            return AbstractReference2ByteSortedMap.this.lastKey();
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K to) {
            return AbstractReference2ByteSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K from) {
            return AbstractReference2ByteSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            return AbstractReference2ByteSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Map.Entry<K, Byte>>)AbstractReference2ByteSortedMap.this.entrySet().iterator(new BasicEntry<K>(from, (byte)0)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>(AbstractReference2ByteSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator<K> extends AbstractObjectBidirectionalIterator<K>
    {
        protected final ObjectBidirectionalIterator<Map.Entry<K, Byte>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<K, Byte>> i) {
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
    
    protected class ValuesCollection extends AbstractByteCollection
    {
        @Override
        public ByteIterator iterator() {
            return new ValuesIterator<Object>((ObjectBidirectionalIterator<Map.Entry<?, Byte>>)AbstractReference2ByteSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final byte k) {
            return AbstractReference2ByteSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractReference2ByteSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractReference2ByteSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> extends AbstractByteIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<K, Byte>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<K, Byte>> i) {
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
