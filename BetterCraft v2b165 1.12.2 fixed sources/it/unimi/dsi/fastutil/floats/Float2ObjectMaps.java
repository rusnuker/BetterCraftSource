// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectCollections;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import java.io.Serializable;

public class Float2ObjectMaps
{
    public static final EmptyMap EMPTY_MAP;
    
    private Float2ObjectMaps() {
    }
    
    public static <V> Float2ObjectMap<V> emptyMap() {
        return Float2ObjectMaps.EMPTY_MAP;
    }
    
    public static <V> Float2ObjectMap<V> singleton(final float key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Float2ObjectMap<V> singleton(final Float key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Float2ObjectMap<V> synchronize(final Float2ObjectMap<V> m) {
        return new SynchronizedMap<V>(m);
    }
    
    public static <V> Float2ObjectMap<V> synchronize(final Float2ObjectMap<V> m, final Object sync) {
        return new SynchronizedMap<V>(m, sync);
    }
    
    public static <V> Float2ObjectMap<V> unmodifiable(final Float2ObjectMap<V> m) {
        return new UnmodifiableMap<V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<V> extends Float2ObjectFunctions.EmptyFunction<V> implements Float2ObjectMap<V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final Object v) {
            return false;
        }
        
        @Override
        public void putAll(final Map<? extends Float, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> float2ObjectEntrySet() {
            return ObjectSets.EMPTY_SET;
        }
        
        @Override
        public FloatSet keySet() {
            return FloatSets.EMPTY_SET;
        }
        
        @Override
        public ObjectCollection<V> values() {
            return ObjectSets.EMPTY_SET;
        }
        
        private Object readResolve() {
            return Float2ObjectMaps.EMPTY_MAP;
        }
        
        @Override
        public Object clone() {
            return Float2ObjectMaps.EMPTY_MAP;
        }
        
        @Override
        public boolean isEmpty() {
            return true;
        }
        
        @Override
        public ObjectSet<Map.Entry<Float, V>> entrySet() {
            return (ObjectSet<Map.Entry<Float, V>>)this.float2ObjectEntrySet();
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
    
    public static class Singleton<V> extends Float2ObjectFunctions.Singleton<V> implements Float2ObjectMap<V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient FloatSet keys;
        protected transient ObjectCollection<V> values;
        
        protected Singleton(final float key, final V value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final Object v) {
            return (this.value == null) ? (v == null) : this.value.equals(v);
        }
        
        @Override
        public void putAll(final Map<? extends Float, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> float2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Entry<V>>)ObjectSets.singleton(new SingletonEntry());
            }
            return this.entries;
        }
        
        @Override
        public FloatSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSets.singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public ObjectCollection<V> values() {
            if (this.values == null) {
                this.values = ObjectSets.singleton(this.value);
            }
            return this.values;
        }
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        public ObjectSet<Map.Entry<Float, V>> entrySet() {
            return (ObjectSet<Map.Entry<Float, V>>)this.float2ObjectEntrySet();
        }
        
        @Override
        public int hashCode() {
            return HashCommon.float2int(this.key) ^ ((this.value == null) ? 0 : this.value.hashCode());
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
        
        protected class SingletonEntry implements Float2ObjectMap.Entry<V>, Map.Entry<Float, V>
        {
            @Deprecated
            @Override
            public Float getKey() {
                return Singleton.this.key;
            }
            
            @Override
            public V getValue() {
                return (V)Singleton.this.value;
            }
            
            @Override
            public float getFloatKey() {
                return Singleton.this.key;
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
                return e.getKey() != null && e.getKey() instanceof Float && Float.floatToIntBits(Singleton.this.key) == Float.floatToIntBits((float)e.getKey()) && ((Singleton.this.value != null) ? Singleton.this.value.equals(e.getValue()) : (e.getValue() == null));
            }
            
            @Override
            public int hashCode() {
                return HashCommon.float2int(Singleton.this.key) ^ ((Singleton.this.value == null) ? 0 : Singleton.this.value.hashCode());
            }
            
            @Override
            public String toString() {
                return Singleton.this.key + "->" + Singleton.this.value;
            }
        }
    }
    
    public static class SynchronizedMap<V> extends Float2ObjectFunctions.SynchronizedFunction<V> implements Float2ObjectMap<V>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2ObjectMap<V> map;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient FloatSet keys;
        protected transient ObjectCollection<V> values;
        
        protected SynchronizedMap(final Float2ObjectMap<V> m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Float2ObjectMap<V> m) {
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
        public boolean containsKey(final float k) {
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
        public V put(final float k, final V v) {
            synchronized (this.sync) {
                return this.map.put(k, v);
            }
        }
        
        @Override
        public void putAll(final Map<? extends Float, ? extends V> m) {
            synchronized (this.sync) {
                this.map.putAll((Map<?, ?>)m);
            }
        }
        
        @Override
        public ObjectSet<Entry<V>> float2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.synchronize(this.map.float2ObjectEntrySet(), this.sync);
            }
            return this.entries;
        }
        
        @Override
        public FloatSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSets.synchronize(this.map.keySet(), this.sync);
            }
            return this.keys;
        }
        
        @Override
        public ObjectCollection<V> values() {
            if (this.values == null) {
                return ObjectCollections.synchronize(this.map.values(), this.sync);
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
        public V put(final Float k, final V v) {
            synchronized (this.sync) {
                return this.map.put(k, v);
            }
        }
        
        @Deprecated
        @Override
        public V remove(final float k) {
            synchronized (this.sync) {
                return this.map.remove(k);
            }
        }
        
        @Deprecated
        @Override
        public V get(final float k) {
            synchronized (this.sync) {
                return this.map.get(k);
            }
        }
        
        @Override
        public boolean containsKey(final Object ok) {
            synchronized (this.sync) {
                return this.map.containsKey(ok);
            }
        }
        
        @Override
        public boolean isEmpty() {
            synchronized (this.sync) {
                return this.map.isEmpty();
            }
        }
        
        @Override
        public ObjectSet<Map.Entry<Float, V>> entrySet() {
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
    
    public static class UnmodifiableMap<V> extends Float2ObjectFunctions.UnmodifiableFunction<V> implements Float2ObjectMap<V>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2ObjectMap<V> map;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient FloatSet keys;
        protected transient ObjectCollection<V> values;
        
        protected UnmodifiableMap(final Float2ObjectMap<V> m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public int size() {
            return this.map.size();
        }
        
        @Override
        public boolean containsKey(final float k) {
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
        public V put(final float k, final V v) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void putAll(final Map<? extends Float, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> float2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.unmodifiable(this.map.float2ObjectEntrySet());
            }
            return this.entries;
        }
        
        @Override
        public FloatSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSets.unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public ObjectCollection<V> values() {
            if (this.values == null) {
                return ObjectCollections.unmodifiable(this.map.values());
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
        
        @Deprecated
        @Override
        public V remove(final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V get(final float k) {
            return this.map.get(k);
        }
        
        @Override
        public boolean containsKey(final Object ok) {
            return this.map.containsKey(ok);
        }
        
        @Deprecated
        @Override
        public V remove(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V get(final Object k) {
            return this.map.get(k);
        }
        
        @Override
        public boolean isEmpty() {
            return this.map.isEmpty();
        }
        
        @Override
        public ObjectSet<Map.Entry<Float, V>> entrySet() {
            return ObjectSets.unmodifiable(this.map.entrySet());
        }
    }
}
