// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.bytes.ByteCollections;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.bytes.ByteSets;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import java.util.Map;
import java.io.Serializable;

public class Reference2ByteMaps
{
    public static final EmptyMap EMPTY_MAP;
    
    private Reference2ByteMaps() {
    }
    
    public static <K> Reference2ByteMap<K> emptyMap() {
        return Reference2ByteMaps.EMPTY_MAP;
    }
    
    public static <K> Reference2ByteMap<K> singleton(final K key, final byte value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2ByteMap<K> singleton(final K key, final Byte value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2ByteMap<K> synchronize(final Reference2ByteMap<K> m) {
        return new SynchronizedMap<K>(m);
    }
    
    public static <K> Reference2ByteMap<K> synchronize(final Reference2ByteMap<K> m, final Object sync) {
        return new SynchronizedMap<K>(m, sync);
    }
    
    public static <K> Reference2ByteMap<K> unmodifiable(final Reference2ByteMap<K> m) {
        return new UnmodifiableMap<K>(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<K> extends Reference2ByteFunctions.EmptyFunction<K> implements Reference2ByteMap<K>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final byte v) {
            return false;
        }
        
        @Override
        public void putAll(final Map<? extends K, ? extends Byte> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2ByteEntrySet() {
            return ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSet<K> keySet() {
            return ReferenceSets.EMPTY_SET;
        }
        
        @Override
        public ByteCollection values() {
            return ByteSets.EMPTY_SET;
        }
        
        @Override
        public boolean containsValue(final Object ov) {
            return false;
        }
        
        private Object readResolve() {
            return Reference2ByteMaps.EMPTY_MAP;
        }
        
        @Override
        public Object clone() {
            return Reference2ByteMaps.EMPTY_MAP;
        }
        
        @Override
        public boolean isEmpty() {
            return true;
        }
        
        @Override
        public ObjectSet<Map.Entry<K, Byte>> entrySet() {
            return (ObjectSet<Map.Entry<K, Byte>>)this.reference2ByteEntrySet();
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
    
    public static class Singleton<K> extends Reference2ByteFunctions.Singleton<K> implements Reference2ByteMap<K>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ReferenceSet<K> keys;
        protected transient ByteCollection values;
        
        protected Singleton(final K key, final byte value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final byte v) {
            return this.value == v;
        }
        
        @Override
        public boolean containsValue(final Object ov) {
            return (byte)ov == this.value;
        }
        
        @Override
        public void putAll(final Map<? extends K, ? extends Byte> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Entry<K>>)ObjectSets.singleton(new SingletonEntry());
            }
            return this.entries;
        }
        
        @Override
        public ReferenceSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSets.singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public ByteCollection values() {
            if (this.values == null) {
                this.values = ByteSets.singleton(this.value);
            }
            return this.values;
        }
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        public ObjectSet<Map.Entry<K, Byte>> entrySet() {
            return (ObjectSet<Map.Entry<K, Byte>>)this.reference2ByteEntrySet();
        }
        
        @Override
        public int hashCode() {
            return System.identityHashCode(this.key) ^ this.value;
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
        
        protected class SingletonEntry implements Reference2ByteMap.Entry<K>, Map.Entry<K, Byte>
        {
            @Override
            public K getKey() {
                return (K)Singleton.this.key;
            }
            
            @Deprecated
            @Override
            public Byte getValue() {
                return Singleton.this.value;
            }
            
            @Override
            public byte getByteValue() {
                return Singleton.this.value;
            }
            
            @Override
            public byte setValue(final byte value) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public Byte setValue(final Byte value) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public boolean equals(final Object o) {
                if (!(o instanceof Map.Entry)) {
                    return false;
                }
                final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
                return e.getValue() != null && e.getValue() instanceof Byte && Singleton.this.key == e.getKey() && Singleton.this.value == (byte)e.getValue();
            }
            
            @Override
            public int hashCode() {
                return System.identityHashCode(Singleton.this.key) ^ Singleton.this.value;
            }
            
            @Override
            public String toString() {
                return Singleton.this.key + "->" + Singleton.this.value;
            }
        }
    }
    
    public static class SynchronizedMap<K> extends Reference2ByteFunctions.SynchronizedFunction<K> implements Reference2ByteMap<K>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2ByteMap<K> map;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ReferenceSet<K> keys;
        protected transient ByteCollection values;
        
        protected SynchronizedMap(final Reference2ByteMap<K> m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Reference2ByteMap<K> m) {
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
        public boolean containsValue(final byte v) {
            synchronized (this.sync) {
                return this.map.containsValue(v);
            }
        }
        
        @Override
        public byte defaultReturnValue() {
            synchronized (this.sync) {
                return this.map.defaultReturnValue();
            }
        }
        
        @Override
        public void defaultReturnValue(final byte defRetValue) {
            synchronized (this.sync) {
                this.map.defaultReturnValue(defRetValue);
            }
        }
        
        @Override
        public byte put(final K k, final byte v) {
            synchronized (this.sync) {
                return this.map.put(k, v);
            }
        }
        
        @Override
        public void putAll(final Map<? extends K, ? extends Byte> m) {
            synchronized (this.sync) {
                this.map.putAll((Map<?, ?>)m);
            }
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.synchronize(this.map.reference2ByteEntrySet(), this.sync);
            }
            return this.entries;
        }
        
        @Override
        public ReferenceSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSets.synchronize(this.map.keySet(), this.sync);
            }
            return this.keys;
        }
        
        @Override
        public ByteCollection values() {
            if (this.values == null) {
                return ByteCollections.synchronize(this.map.values(), this.sync);
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
        public Byte put(final K k, final Byte v) {
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
        public byte removeByte(final Object k) {
            synchronized (this.sync) {
                return this.map.removeByte(k);
            }
        }
        
        @Deprecated
        @Override
        public byte getByte(final Object k) {
            synchronized (this.sync) {
                return this.map.getByte(k);
            }
        }
        
        @Override
        public boolean isEmpty() {
            synchronized (this.sync) {
                return this.map.isEmpty();
            }
        }
        
        @Override
        public ObjectSet<Map.Entry<K, Byte>> entrySet() {
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
    
    public static class UnmodifiableMap<K> extends Reference2ByteFunctions.UnmodifiableFunction<K> implements Reference2ByteMap<K>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2ByteMap<K> map;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ReferenceSet<K> keys;
        protected transient ByteCollection values;
        
        protected UnmodifiableMap(final Reference2ByteMap<K> m) {
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
        public boolean containsValue(final byte v) {
            return this.map.containsValue(v);
        }
        
        @Override
        public byte defaultReturnValue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void defaultReturnValue(final byte defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte put(final K k, final byte v) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void putAll(final Map<? extends K, ? extends Byte> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.unmodifiable(this.map.reference2ByteEntrySet());
            }
            return this.entries;
        }
        
        @Override
        public ReferenceSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSets.unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public ByteCollection values() {
            if (this.values == null) {
                return ByteCollections.unmodifiable(this.map.values());
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
        public byte removeByte(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte getByte(final Object k) {
            return this.map.getByte(k);
        }
        
        @Override
        public boolean isEmpty() {
            return this.map.isEmpty();
        }
        
        @Override
        public ObjectSet<Map.Entry<K, Byte>> entrySet() {
            return ObjectSets.unmodifiable(this.map.entrySet());
        }
    }
}
