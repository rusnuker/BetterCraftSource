// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.io.Serializable;

public abstract class AbstractReference2ObjectMap<K, V> extends AbstractReference2ObjectFunction<K, V> implements Reference2ObjectMap<K, V>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractReference2ObjectMap() {
    }
    
    @Override
    public boolean containsValue(final Object v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final Object k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends K, ? extends V>> i = m.entrySet().iterator();
        if (m instanceof Reference2ObjectMap) {
            while (n-- != 0) {
                final Entry<? extends K, ? extends V> e = (Entry<? extends K, ? extends V>)i.next();
                this.put((K)e.getKey(), (V)e.getValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends K, ? extends V> e2 = (Map.Entry<? extends K, ? extends V>)i.next();
                this.put((K)e2.getKey(), (V)e2.getValue());
            }
        }
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public ReferenceSet<K> keySet() {
        return new AbstractReferenceSet<K>() {
            @Override
            public boolean contains(final Object k) {
                return AbstractReference2ObjectMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractReference2ObjectMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractReference2ObjectMap.this.clear();
            }
            
            @Override
            public ObjectIterator<K> iterator() {
                return new AbstractObjectIterator<K>() {
                    final ObjectIterator<Map.Entry<K, V>> i = AbstractReference2ObjectMap.this.entrySet().iterator();
                    
                    @Override
                    public K next() {
                        return (K)this.i.next().getKey();
                    }
                    
                    @Override
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                    
                    @Override
                    public void remove() {
                        this.i.remove();
                    }
                };
            }
        };
    }
    
    @Override
    public ObjectCollection<V> values() {
        return new AbstractObjectCollection<V>() {
            @Override
            public boolean contains(final Object k) {
                return AbstractReference2ObjectMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractReference2ObjectMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractReference2ObjectMap.this.clear();
            }
            
            @Override
            public ObjectIterator<V> iterator() {
                return new AbstractObjectIterator<V>() {
                    final ObjectIterator<Map.Entry<K, V>> i = AbstractReference2ObjectMap.this.entrySet().iterator();
                    
                    @Deprecated
                    @Override
                    public V next() {
                        return (V)this.i.next().getValue();
                    }
                    
                    @Override
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                };
            }
        };
    }
    
    @Override
    public ObjectSet<Map.Entry<K, V>> entrySet() {
        return (ObjectSet<Map.Entry<K, V>>)this.reference2ObjectEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<K, V>> i = this.entrySet().iterator();
        while (n-- != 0) {
            h += i.next().hashCode();
        }
        return h;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }
        final Map<?, ?> m = (Map<?, ?>)o;
        return m.size() == this.size() && this.entrySet().containsAll(m.entrySet());
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<? extends Map.Entry<K, V>> i = this.entrySet().iterator();
        int n = this.size();
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) {
                first = false;
            }
            else {
                s.append(", ");
            }
            final Entry<K, V> e = i.next();
            if (this == e.getKey()) {
                s.append("(this map)");
            }
            else {
                s.append(String.valueOf(e.getKey()));
            }
            s.append("=>");
            if (this == e.getValue()) {
                s.append("(this map)");
            }
            else {
                s.append(String.valueOf(e.getValue()));
            }
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry<K, V> implements Entry<K, V>
    {
        protected K key;
        protected V value;
        
        public BasicEntry(final K key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }
        
        @Override
        public V getValue() {
            return this.value;
        }
        
        @Override
        public V setValue(final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            return this.key == e.getKey() && ((this.value != null) ? this.value.equals(e.getValue()) : (e.getValue() == null));
        }
        
        @Override
        public int hashCode() {
            return System.identityHashCode(this.key) ^ ((this.value == null) ? 0 : this.value.hashCode());
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}
