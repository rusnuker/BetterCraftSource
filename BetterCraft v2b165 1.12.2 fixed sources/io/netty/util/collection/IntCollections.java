// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.collection;

import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map;
import java.util.Collections;
import java.util.Set;

public final class IntCollections
{
    private static final IntObjectMap<Object> EMPTY_MAP;
    
    private IntCollections() {
    }
    
    public static <V> IntObjectMap<V> emptyMap() {
        return (IntObjectMap<V>)IntCollections.EMPTY_MAP;
    }
    
    public static <V> IntObjectMap<V> unmodifiableMap(final IntObjectMap<V> map) {
        return new UnmodifiableMap<V>(map);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    private static final class EmptyMap implements IntObjectMap<Object>
    {
        @Override
        public Object get(final int key) {
            return null;
        }
        
        @Override
        public Object put(final int key, final Object value) {
            throw new UnsupportedOperationException("put");
        }
        
        @Override
        public Object remove(final int key) {
            return null;
        }
        
        @Override
        public int size() {
            return 0;
        }
        
        @Override
        public boolean isEmpty() {
            return true;
        }
        
        @Override
        public boolean containsKey(final Object key) {
            return false;
        }
        
        @Override
        public void clear() {
        }
        
        @Override
        public Set<Integer> keySet() {
            return Collections.emptySet();
        }
        
        @Override
        public boolean containsKey(final int key) {
            return false;
        }
        
        @Override
        public boolean containsValue(final Object value) {
            return false;
        }
        
        @Override
        public Iterable<PrimitiveEntry<Object>> entries() {
            return (Iterable<PrimitiveEntry<Object>>)Collections.emptySet();
        }
        
        @Override
        public Object get(final Object key) {
            return null;
        }
        
        @Override
        public Object put(final Integer key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Object remove(final Object key) {
            return null;
        }
        
        @Override
        public void putAll(final Map<? extends Integer, ?> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Collection<Object> values() {
            return Collections.emptyList();
        }
        
        @Override
        public Set<Map.Entry<Integer, Object>> entrySet() {
            return Collections.emptySet();
        }
    }
    
    private static final class UnmodifiableMap<V> implements IntObjectMap<V>
    {
        private final IntObjectMap<V> map;
        private Set<Integer> keySet;
        private Set<Map.Entry<Integer, V>> entrySet;
        private Collection<V> values;
        private Iterable<PrimitiveEntry<V>> entries;
        
        UnmodifiableMap(final IntObjectMap<V> map) {
            this.map = map;
        }
        
        @Override
        public V get(final int key) {
            return this.map.get(key);
        }
        
        @Override
        public V put(final int key, final V value) {
            throw new UnsupportedOperationException("put");
        }
        
        @Override
        public V remove(final int key) {
            throw new UnsupportedOperationException("remove");
        }
        
        @Override
        public int size() {
            return this.map.size();
        }
        
        @Override
        public boolean isEmpty() {
            return this.map.isEmpty();
        }
        
        @Override
        public void clear() {
            throw new UnsupportedOperationException("clear");
        }
        
        @Override
        public boolean containsKey(final int key) {
            return this.map.containsKey(key);
        }
        
        @Override
        public boolean containsValue(final Object value) {
            return this.map.containsValue(value);
        }
        
        @Override
        public boolean containsKey(final Object key) {
            return this.map.containsKey(key);
        }
        
        @Override
        public V get(final Object key) {
            return this.map.get(key);
        }
        
        @Override
        public V put(final Integer key, final V value) {
            throw new UnsupportedOperationException("put");
        }
        
        @Override
        public V remove(final Object key) {
            throw new UnsupportedOperationException("remove");
        }
        
        @Override
        public void putAll(final Map<? extends Integer, ? extends V> m) {
            throw new UnsupportedOperationException("putAll");
        }
        
        @Override
        public Iterable<PrimitiveEntry<V>> entries() {
            if (this.entries == null) {
                this.entries = new Iterable<PrimitiveEntry<V>>() {
                    @Override
                    public Iterator<PrimitiveEntry<V>> iterator() {
                        return new IteratorImpl(UnmodifiableMap.this.map.entries().iterator());
                    }
                };
            }
            return this.entries;
        }
        
        @Override
        public Set<Integer> keySet() {
            if (this.keySet == null) {
                this.keySet = Collections.unmodifiableSet(this.map.keySet());
            }
            return this.keySet;
        }
        
        @Override
        public Set<Map.Entry<Integer, V>> entrySet() {
            if (this.entrySet == null) {
                this.entrySet = Collections.unmodifiableSet((Set<? extends Map.Entry<Integer, V>>)this.map.entrySet());
            }
            return this.entrySet;
        }
        
        @Override
        public Collection<V> values() {
            if (this.values == null) {
                this.values = Collections.unmodifiableCollection(this.map.values());
            }
            return this.values;
        }
        
        private class IteratorImpl implements Iterator<PrimitiveEntry<V>>
        {
            final Iterator<PrimitiveEntry<V>> iter;
            
            IteratorImpl(final Iterator<PrimitiveEntry<V>> iter) {
                this.iter = iter;
            }
            
            @Override
            public boolean hasNext() {
                return this.iter.hasNext();
            }
            
            @Override
            public PrimitiveEntry<V> next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return new EntryImpl(this.iter.next());
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        }
        
        private class EntryImpl implements PrimitiveEntry<V>
        {
            private final PrimitiveEntry<V> entry;
            
            EntryImpl(final PrimitiveEntry<V> entry) {
                this.entry = entry;
            }
            
            @Override
            public int key() {
                return this.entry.key();
            }
            
            @Override
            public V value() {
                return this.entry.value();
            }
            
            @Override
            public void setValue(final V value) {
                throw new UnsupportedOperationException("setValue");
            }
        }
    }
}
