// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.shorts.ShortCollections;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.shorts.ShortSets;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import java.util.Map;
import java.io.Serializable;

public class Object2ShortMaps
{
    public static final EmptyMap EMPTY_MAP;
    
    private Object2ShortMaps() {
    }
    
    public static <K> Object2ShortMap<K> emptyMap() {
        return Object2ShortMaps.EMPTY_MAP;
    }
    
    public static <K> Object2ShortMap<K> singleton(final K key, final short value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2ShortMap<K> singleton(final K key, final Short value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2ShortMap<K> synchronize(final Object2ShortMap<K> m) {
        return new SynchronizedMap<K>(m);
    }
    
    public static <K> Object2ShortMap<K> synchronize(final Object2ShortMap<K> m, final Object sync) {
        return new SynchronizedMap<K>(m, sync);
    }
    
    public static <K> Object2ShortMap<K> unmodifiable(final Object2ShortMap<K> m) {
        return new UnmodifiableMap<K>(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<K> extends Object2ShortFunctions.EmptyFunction<K> implements Object2ShortMap<K>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final short v) {
            return false;
        }
        
        @Override
        public void putAll(final Map<? extends K, ? extends Short> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> object2ShortEntrySet() {
            return ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSet<K> keySet() {
            return ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ShortCollection values() {
            return ShortSets.EMPTY_SET;
        }
        
        @Override
        public boolean containsValue(final Object ov) {
            return false;
        }
        
        private Object readResolve() {
            return Object2ShortMaps.EMPTY_MAP;
        }
        
        @Override
        public Object clone() {
            return Object2ShortMaps.EMPTY_MAP;
        }
        
        @Override
        public boolean isEmpty() {
            return true;
        }
        
        @Override
        public ObjectSet<Map.Entry<K, Short>> entrySet() {
            return (ObjectSet<Map.Entry<K, Short>>)this.object2ShortEntrySet();
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
    
    public static class Singleton<K> extends Object2ShortFunctions.Singleton<K> implements Object2ShortMap<K>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ObjectSet<K> keys;
        protected transient ShortCollection values;
        
        protected Singleton(final K key, final short value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final short v) {
            return this.value == v;
        }
        
        @Override
        public boolean containsValue(final Object ov) {
            return (short)ov == this.value;
        }
        
        @Override
        public void putAll(final Map<? extends K, ? extends Short> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> object2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Entry<K>>)ObjectSets.singleton(new SingletonEntry());
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
        public ShortCollection values() {
            if (this.values == null) {
                this.values = ShortSets.singleton(this.value);
            }
            return this.values;
        }
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        public ObjectSet<Map.Entry<K, Short>> entrySet() {
            return (ObjectSet<Map.Entry<K, Short>>)this.object2ShortEntrySet();
        }
        
        @Override
        public int hashCode() {
            return ((this.key == null) ? 0 : this.key.hashCode()) ^ this.value;
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
        
        protected class SingletonEntry implements Object2ShortMap.Entry<K>, Map.Entry<K, Short>
        {
            @Override
            public K getKey() {
                return (K)Singleton.this.key;
            }
            
            @Deprecated
            @Override
            public Short getValue() {
                return Singleton.this.value;
            }
            
            @Override
            public short getShortValue() {
                return Singleton.this.value;
            }
            
            @Override
            public short setValue(final short value) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public Short setValue(final Short value) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public boolean equals(final Object o) {
                if (!(o instanceof Map.Entry)) {
                    return false;
                }
                final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
                if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                    return false;
                }
                if (Singleton.this.key == null) {
                    if (e.getKey() != null) {
                        return false;
                    }
                }
                else if (!Singleton.this.key.equals(e.getKey())) {
                    return false;
                }
                if (Singleton.this.value == (short)e.getValue()) {
                    return true;
                }
                return false;
            }
            
            @Override
            public int hashCode() {
                return ((Singleton.this.key == null) ? 0 : Singleton.this.key.hashCode()) ^ Singleton.this.value;
            }
            
            @Override
            public String toString() {
                return Singleton.this.key + "->" + Singleton.this.value;
            }
        }
    }
    
    public static class SynchronizedMap<K> extends Object2ShortFunctions.SynchronizedFunction<K> implements Object2ShortMap<K>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ShortMap<K> map;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ObjectSet<K> keys;
        protected transient ShortCollection values;
        
        protected SynchronizedMap(final Object2ShortMap<K> m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Object2ShortMap<K> m) {
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
        public boolean containsValue(final short v) {
            synchronized (this.sync) {
                return this.map.containsValue(v);
            }
        }
        
        @Override
        public short defaultReturnValue() {
            synchronized (this.sync) {
                return this.map.defaultReturnValue();
            }
        }
        
        @Override
        public void defaultReturnValue(final short defRetValue) {
            synchronized (this.sync) {
                this.map.defaultReturnValue(defRetValue);
            }
        }
        
        @Override
        public short put(final K k, final short v) {
            synchronized (this.sync) {
                return this.map.put(k, v);
            }
        }
        
        @Override
        public void putAll(final Map<? extends K, ? extends Short> m) {
            synchronized (this.sync) {
                this.map.putAll((Map<?, ?>)m);
            }
        }
        
        @Override
        public ObjectSet<Entry<K>> object2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.synchronize(this.map.object2ShortEntrySet(), this.sync);
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
        public ShortCollection values() {
            if (this.values == null) {
                return ShortCollections.synchronize(this.map.values(), this.sync);
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
        
        @Deprecated
        @Override
        public Short put(final K k, final Short v) {
            synchronized (this.sync) {
                return this.map.put(k, v);
            }
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            synchronized (this.sync) {
                return this.map.containsValue(ov);
            }
        }
        
        @Deprecated
        @Override
        public short removeShort(final Object k) {
            synchronized (this.sync) {
                return this.map.removeShort(k);
            }
        }
        
        @Deprecated
        @Override
        public short getShort(final Object k) {
            synchronized (this.sync) {
                return this.map.getShort(k);
            }
        }
        
        @Override
        public boolean isEmpty() {
            synchronized (this.sync) {
                return this.map.isEmpty();
            }
        }
        
        @Override
        public ObjectSet<Map.Entry<K, Short>> entrySet() {
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
    
    public static class UnmodifiableMap<K> extends Object2ShortFunctions.UnmodifiableFunction<K> implements Object2ShortMap<K>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ShortMap<K> map;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ObjectSet<K> keys;
        protected transient ShortCollection values;
        
        protected UnmodifiableMap(final Object2ShortMap<K> m) {
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
        public boolean containsValue(final short v) {
            return this.map.containsValue(v);
        }
        
        @Override
        public short defaultReturnValue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void defaultReturnValue(final short defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short put(final K k, final short v) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void putAll(final Map<? extends K, ? extends Short> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> object2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.unmodifiable(this.map.object2ShortEntrySet());
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
        public ShortCollection values() {
            if (this.values == null) {
                return ShortCollections.unmodifiable(this.map.values());
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
        public boolean containsValue(final Object ov) {
            return this.map.containsValue(ov);
        }
        
        @Override
        public short removeShort(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short getShort(final Object k) {
            return this.map.getShort(k);
        }
        
        @Override
        public boolean isEmpty() {
            return this.map.isEmpty();
        }
        
        @Override
        public ObjectSet<Map.Entry<K, Short>> entrySet() {
            return ObjectSets.unmodifiable(this.map.entrySet());
        }
    }
}
