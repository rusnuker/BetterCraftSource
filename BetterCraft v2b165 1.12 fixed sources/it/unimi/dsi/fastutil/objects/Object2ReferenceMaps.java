// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Collection;
import java.util.Set;
import java.util.Map;
import java.io.Serializable;

public class Object2ReferenceMaps
{
    public static final EmptyMap EMPTY_MAP;
    
    private Object2ReferenceMaps() {
    }
    
    public static <K, V> Object2ReferenceMap<K, V> emptyMap() {
        return Object2ReferenceMaps.EMPTY_MAP;
    }
    
    public static <K, V> Object2ReferenceMap<K, V> singleton(final K key, final V value) {
        return new Singleton<K, V>(key, value);
    }
    
    public static <K, V> Object2ReferenceMap<K, V> synchronize(final Object2ReferenceMap<K, V> m) {
        return new SynchronizedMap<K, V>(m);
    }
    
    public static <K, V> Object2ReferenceMap<K, V> synchronize(final Object2ReferenceMap<K, V> m, final Object sync) {
        return new SynchronizedMap<K, V>(m, sync);
    }
    
    public static <K, V> Object2ReferenceMap<K, V> unmodifiable(final Object2ReferenceMap<K, V> m) {
        return new UnmodifiableMap<K, V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<K, V> extends Object2ReferenceFunctions.EmptyFunction<K, V> implements Object2ReferenceMap<K, V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final Object v) {
            return false;
        }
        
        @Override
        public void putAll(final Map<? extends K, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K, V>> object2ReferenceEntrySet() {
            return ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSet<K> keySet() {
            return ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceCollection<V> values() {
            return ReferenceSets.EMPTY_SET;
        }
        
        private Object readResolve() {
            return Object2ReferenceMaps.EMPTY_MAP;
        }
        
        @Override
        public Object clone() {
            return Object2ReferenceMaps.EMPTY_MAP;
        }
        
        @Override
        public boolean isEmpty() {
            return true;
        }
        
        @Override
        public ObjectSet<Map.Entry<K, V>> entrySet() {
            return (ObjectSet<Map.Entry<K, V>>)this.object2ReferenceEntrySet();
        }
        
        @Override
        public int hashCode() {
            return 0;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Map && ((Map)o).isEmpty();
        }
        
        @Override
        public String toString() {
            return "{}";
        }
    }
    
    public static class Singleton<K, V> extends Object2ReferenceFunctions.Singleton<K, V> implements Object2ReferenceMap<K, V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<K, V>> entries;
        protected transient ObjectSet<K> keys;
        protected transient ReferenceCollection<V> values;
        
        protected Singleton(final K key, final V value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final Object v) {
            return this.value == v;
        }
        
        @Override
        public void putAll(final Map<? extends K, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K, V>> object2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Entry<K, V>>)ObjectSets.singleton(new SingletonEntry());
            }
            return this.entries;
        }
        
        @Override
        public ObjectSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSets.singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public ReferenceCollection<V> values() {
            if (this.values == null) {
                this.values = ReferenceSets.singleton(this.value);
            }
            return this.values;
        }
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        public ObjectSet<Map.Entry<K, V>> entrySet() {
            return (ObjectSet<Map.Entry<K, V>>)this.object2ReferenceEntrySet();
        }
        
        @Override
        public int hashCode() {
            return ((this.key == null) ? 0 : this.key.hashCode()) ^ ((this.value == null) ? 0 : System.identityHashCode(this.value));
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
            return m.size() == 1 && this.entrySet().iterator().next().equals(m.entrySet().iterator().next());
        }
        
        @Override
        public String toString() {
            return "{" + this.key + "=>" + this.value + "}";
        }
        
        protected class SingletonEntry implements Object2ReferenceMap.Entry<K, V>, Map.Entry<K, V>
        {
            @Override
            public K getKey() {
                return (K)Singleton.this.key;
            }
            
            @Override
            public V getValue() {
                return (V)Singleton.this.value;
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
                if (Singleton.this.key == null) {
                    if (e.getKey() != null) {
                        return false;
                    }
                }
                else if (!Singleton.this.key.equals(e.getKey())) {
                    return false;
                }
                if (Singleton.this.value == e.getValue()) {
                    return true;
                }
                return false;
            }
            
            @Override
            public int hashCode() {
                return ((Singleton.this.key == null) ? 0 : Singleton.this.key.hashCode()) ^ ((Singleton.this.value == null) ? 0 : System.identityHashCode(Singleton.this.value));
            }
            
            @Override
            public String toString() {
                return Singleton.this.key + "->" + Singleton.this.value;
            }
        }
    }
    
    public static class SynchronizedMap<K, V> extends Object2ReferenceFunctions.SynchronizedFunction<K, V> implements Object2ReferenceMap<K, V>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ReferenceMap<K, V> map;
        protected transient ObjectSet<Entry<K, V>> entries;
        protected transient ObjectSet<K> keys;
        protected transient ReferenceCollection<V> values;
        
        protected SynchronizedMap(final Object2ReferenceMap<K, V> m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Object2ReferenceMap<K, V> m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public int size() {
            synchronized (this.sync) {
                return this.map.size();
            }
        }
        
        @Override
        public boolean containsKey(final Object k) {
            synchronized (this.sync) {
                return this.map.containsKey(k);
            }
        }
        
        @Override
        public boolean containsValue(final Object v) {
            synchronized (this.sync) {
                return this.map.containsValue(v);
            }
        }
        
        @Override
        public V defaultReturnValue() {
            synchronized (this.sync) {
                return this.map.defaultReturnValue();
            }
        }
        
        @Override
        public void defaultReturnValue(final V defRetValue) {
            synchronized (this.sync) {
                this.map.defaultReturnValue(defRetValue);
            }
        }
        
        @Override
        public V put(final K k, final V v) {
            synchronized (this.sync) {
                return this.map.put(k, v);
            }
        }
        
        @Override
        public void putAll(final Map<? extends K, ? extends V> m) {
            synchronized (this.sync) {
                this.map.putAll((Map<?, ?>)m);
            }
        }
        
        @Override
        public ObjectSet<Entry<K, V>> object2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.synchronize(this.map.object2ReferenceEntrySet(), this.sync);
            }
            return this.entries;
        }
        
        @Override
        public ObjectSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSets.synchronize(this.map.keySet(), this.sync);
            }
            return this.keys;
        }
        
        @Override
        public ReferenceCollection<V> values() {
            if (this.values == null) {
                return ReferenceCollections.synchronize(this.map.values(), this.sync);
            }
            return this.values;
        }
        
        @Override
        public void clear() {
            synchronized (this.sync) {
                this.map.clear();
            }
        }
        
        @Override
        public String toString() {
            synchronized (this.sync) {
                return this.map.toString();
            }
        }
        
        @Override
        public V remove(final Object k) {
            synchronized (this.sync) {
                return this.map.remove(k);
            }
        }
        
        @Override
        public V get(final Object k) {
            synchronized (this.sync) {
                return this.map.get(k);
            }
        }
        
        @Override
        public boolean isEmpty() {
            synchronized (this.sync) {
                return this.map.isEmpty();
            }
        }
        
        @Override
        public ObjectSet<Map.Entry<K, V>> entrySet() {
            synchronized (this.sync) {
                return this.map.entrySet();
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.sync) {
                return this.map.hashCode();
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            synchronized (this.sync) {
                return this.map.equals(o);
            }
        }
    }
    
    public static class UnmodifiableMap<K, V> extends Object2ReferenceFunctions.UnmodifiableFunction<K, V> implements Object2ReferenceMap<K, V>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ReferenceMap<K, V> map;
        protected transient ObjectSet<Entry<K, V>> entries;
        protected transient ObjectSet<K> keys;
        protected transient ReferenceCollection<V> values;
        
        protected UnmodifiableMap(final Object2ReferenceMap<K, V> m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public int size() {
            return this.map.size();
        }
        
        @Override
        public boolean containsKey(final Object k) {
            return this.map.containsKey(k);
        }
        
        @Override
        public boolean containsValue(final Object v) {
            return this.map.containsValue(v);
        }
        
        @Override
        public V defaultReturnValue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void defaultReturnValue(final V defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V put(final K k, final V v) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void putAll(final Map<? extends K, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K, V>> object2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.unmodifiable(this.map.object2ReferenceEntrySet());
            }
            return this.entries;
        }
        
        @Override
        public ObjectSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSets.unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public ReferenceCollection<V> values() {
            if (this.values == null) {
                return ReferenceCollections.unmodifiable(this.map.values());
            }
            return this.values;
        }
        
        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public String toString() {
            return this.map.toString();
        }
        
        @Override
        public V remove(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V get(final Object k) {
            return this.map.get(k);
        }
        
        @Override
        public boolean isEmpty() {
            return this.map.isEmpty();
        }
        
        @Override
        public ObjectSet<Map.Entry<K, V>> entrySet() {
            return ObjectSets.unmodifiable(this.map.entrySet());
        }
    }
}
