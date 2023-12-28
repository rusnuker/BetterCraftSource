// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.AbstractObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectCollection;

public abstract class AbstractInt2ObjectSortedMap<V> extends AbstractInt2ObjectMap<V> implements Int2ObjectSortedMap<V>
{
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractInt2ObjectSortedMap() {
    }
    
    @Deprecated
    @Override
    public Int2ObjectSortedMap<V> headMap(final Integer to) {
        return this.headMap((int)to);
    }
    
    @Deprecated
    @Override
    public Int2ObjectSortedMap<V> tailMap(final Integer from) {
        return this.tailMap((int)from);
    }
    
    @Deprecated
    @Override
    public Int2ObjectSortedMap<V> subMap(final Integer from, final Integer to) {
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
    public ObjectCollection<V> values() {
        return new ValuesCollection();
    }
    
    @Override
    public ObjectSortedSet<Map.Entry<Integer, V>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Integer, V>>)this.int2ObjectEntrySet();
    }
    
    protected class KeySet extends AbstractIntSortedSet
    {
        @Override
        public boolean contains(final int k) {
            return AbstractInt2ObjectSortedMap.this.containsKey(k);
        }
        
        @Override
        public int size() {
            return AbstractInt2ObjectSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractInt2ObjectSortedMap.this.clear();
        }
        
        @Override
        public IntComparator comparator() {
            return AbstractInt2ObjectSortedMap.this.comparator();
        }
        
        @Override
        public int firstInt() {
            return AbstractInt2ObjectSortedMap.this.firstIntKey();
        }
        
        @Override
        public int lastInt() {
            return AbstractInt2ObjectSortedMap.this.lastIntKey();
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            return AbstractInt2ObjectSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            return AbstractInt2ObjectSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return AbstractInt2ObjectSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return new KeySetIterator<Object>((ObjectBidirectionalIterator<Map.Entry<Integer, ?>>)AbstractInt2ObjectSortedMap.this.entrySet().iterator(new BasicEntry<V>(from, null)));
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return new KeySetIterator<Object>((ObjectBidirectionalIterator<Map.Entry<Integer, ?>>)AbstractInt2ObjectSortedMap.this.entrySet().iterator());
        }
    }
    
    protected static class KeySetIterator<V> extends AbstractIntBidirectionalIterator
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Integer, V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Map.Entry<Integer, V>> i) {
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
    
    protected class ValuesCollection extends AbstractObjectCollection<V>
    {
        @Override
        public ObjectIterator<V> iterator() {
            return new ValuesIterator<V>(AbstractInt2ObjectSortedMap.this.entrySet().iterator());
        }
        
        @Override
        public boolean contains(final Object k) {
            return AbstractInt2ObjectSortedMap.this.containsValue(k);
        }
        
        @Override
        public int size() {
            return AbstractInt2ObjectSortedMap.this.size();
        }
        
        @Override
        public void clear() {
            AbstractInt2ObjectSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<V> extends AbstractObjectIterator<V>
    {
        protected final ObjectBidirectionalIterator<Map.Entry<Integer, V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Map.Entry<Integer, V>> i) {
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
