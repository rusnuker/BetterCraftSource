// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

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

public abstract class AbstractInt2ByteSortedMap extends AbstractInt2ByteMap implements Int2ByteSortedMap
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractInt2ByteSortedMap() {
    }
    
    @Deprecated
    @Override
    public Int2ByteSortedMap headMap(final Integer to) {
        return this.headMap((int)to);
    }
    
    @Deprecated
    @Override
    public Int2ByteSortedMap tailMap(final Integer from) {
        return this.tailMap((int)from);
    }
    
    @Deprecated
    @Override
    public Int2ByteSortedMap subMap(final Integer from, final Integer to) {
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
    public ByteCollection values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Integer, Byte>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Integer, Byte>>)this.int2ByteEntrySet();
    }
    
    protected class KeySet extends AbstractIntSortedSet
    {
        @Override
        public boolean contains(final int k) {
            return AbstractInt2ByteSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractInt2ByteSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractInt2ByteSortedMap.this.clear();
        }
        
        @Override
        public IntComparator comparator() {
            return AbstractInt2ByteSortedMap.this.comparator();
        }
        
        @Override
        public int firstInt() {
            return AbstractInt2ByteSortedMap.this.firstIntKey();
        }
        
        @Override
        public int lastInt() {
            return AbstractInt2ByteSortedMap.this.lastIntKey();
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            return AbstractInt2ByteSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            return AbstractInt2ByteSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return AbstractInt2ByteSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Map.Entry<Integer, Byte>>)AbstractInt2ByteSortedMap.this.entrySet().iterator(new BasicEntry(from, (byte)0)));
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return new KeySetIterator(AbstractInt2ByteSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator extends AbstractIntBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Integer, Byte>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Integer, Byte>> i) {
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
    
    protected class ValuesCollection extends AbstractByteCollection
    {
        @Override
        public ByteIterator iterator() {
            return new ValuesIterator(AbstractInt2ByteSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final byte k) {
            return AbstractInt2ByteSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractInt2ByteSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractInt2ByteSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator extends AbstractByteIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Integer, Byte>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Integer, Byte>> i) {
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
