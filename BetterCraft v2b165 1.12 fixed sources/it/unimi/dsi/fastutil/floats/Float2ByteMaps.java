// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.bytes.ByteCollections;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.bytes.ByteSets;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import java.io.Serializable;

public class Float2ByteMaps
{
    public static final EmptyMap EMPTY_MAP;
    
    private Float2ByteMaps() {
    }
    
    public static Float2ByteMap singleton(final float key, final byte value) {
        return new Singleton(key, value);
    }
    
    public static Float2ByteMap singleton(final Float key, final Byte value) {
        return new Singleton(key, value);
    }
    
    public static Float2ByteMap synchronize(final Float2ByteMap m) {
        return new SynchronizedMap(m);
    }
    
    public static Float2ByteMap synchronize(final Float2ByteMap m, final Object sync) {
        return new SynchronizedMap(m, sync);
    }
    
    public static Float2ByteMap unmodifiable(final Float2ByteMap m) {
        return new UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Float2ByteFunctions.EmptyFunction implements Float2ByteMap, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final byte v) {
            return false;
        }
        
        @Override
        public void putAll(final Map<? extends Float, ? extends Byte> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> float2ByteEntrySet() {
            return ObjectSets.EMPTY_SET;
        }
        
        @Override
        public FloatSet keySet() {
            return FloatSets.EMPTY_SET;
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
            return Float2ByteMaps.EMPTY_MAP;
        }
        
        @Override
        public Object clone() {
            return Float2ByteMaps.EMPTY_MAP;
        }
        
        @Override
        public boolean isEmpty() {
            return true;
        }
        
        @Override
        public ObjectSet<Map.Entry<Float, Byte>> entrySet() {
            return (ObjectSet<Map.Entry<Float, Byte>>)this.float2ByteEntrySet();
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
    
    public static class Singleton extends Float2ByteFunctions.Singleton implements Float2ByteMap, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient FloatSet keys;
        protected transient ByteCollection values;
        
        protected Singleton(final float key, final byte value) {
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
        public void putAll(final Map<? extends Float, ? extends Byte> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> float2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Entry>)ObjectSets.singleton(new SingletonEntry());
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
        public ObjectSet<Map.Entry<Float, Byte>> entrySet() {
            return (ObjectSet<Map.Entry<Float, Byte>>)this.float2ByteEntrySet();
        }
        
        @Override
        public int hashCode() {
            return HashCommon.float2int(this.key) ^ this.value;
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
        
        protected class SingletonEntry implements Float2ByteMap.Entry, Map.Entry<Float, Byte>
        {
            @Deprecated
            @Override
            public Float getKey() {
                return Singleton.this.key;
            }
            
            @Deprecated
            @Override
            public Byte getValue() {
                return Singleton.this.value;
            }
            
            @Override
            public float getFloatKey() {
                return Singleton.this.key;
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
                return e.getKey() != null && e.getKey() instanceof Float && e.getValue() != null && e.getValue() instanceof Byte && Float.floatToIntBits(Singleton.this.key) == Float.floatToIntBits((float)e.getKey()) && Singleton.this.value == (byte)e.getValue();
            }
            
            @Override
            public int hashCode() {
                return HashCommon.float2int(Singleton.this.key) ^ Singleton.this.value;
            }
            
            @Override
            public String toString() {
                return Singleton.this.key + "->" + Singleton.this.value;
            }
        }
    }
    
    public static class SynchronizedMap extends Float2ByteFunctions.SynchronizedFunction implements Float2ByteMap, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2ByteMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient FloatSet keys;
        protected transient ByteCollection values;
        
        protected SynchronizedMap(final Float2ByteMap m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Float2ByteMap m) {
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
        public byte put(final float k, final byte v) {
            synchronized (this.sync) {
                return this.map.put(k, v);
            }
        }
        
        @Override
        public void putAll(final Map<? extends Float, ? extends Byte> m) {
            synchronized (this.sync) {
                this.map.putAll(m);
            }
        }
        
        @Override
        public ObjectSet<Entry> float2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.synchronize(this.map.float2ByteEntrySet(), this.sync);
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
        public Byte put(final Float k, final Byte v) {
            synchronized (this.sync) {
                return this.map.put(k, v);
            }
        }
        
        @Deprecated
        @Override
        public byte remove(final float k) {
            synchronized (this.sync) {
                return this.map.remove(k);
            }
        }
        
        @Deprecated
        @Override
        public byte get(final float k) {
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
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            synchronized (this.sync) {
                return this.map.containsValue(ov);
            }
        }
        
        @Override
        public boolean isEmpty() {
            synchronized (this.sync) {
                return this.map.isEmpty();
            }
        }
        
        @Override
        public ObjectSet<Map.Entry<Float, Byte>> entrySet() {
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
    
    public static class UnmodifiableMap extends Float2ByteFunctions.UnmodifiableFunction implements Float2ByteMap, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2ByteMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient FloatSet keys;
        protected transient ByteCollection values;
        
        protected UnmodifiableMap(final Float2ByteMap m) {
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
        public byte put(final float k, final byte v) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void putAll(final Map<? extends Float, ? extends Byte> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> float2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.unmodifiable(this.map.float2ByteEntrySet());
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
        
        @Deprecated
        @Override
        public Byte put(final Float k, final Byte v) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public byte remove(final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public byte get(final float k) {
            return this.map.get(k);
        }
        
        @Override
        public boolean containsKey(final Object ok) {
            return this.map.containsKey(ok);
        }
        
        @Override
        public boolean containsValue(final Object ov) {
            return this.map.containsValue(ov);
        }
        
        @Override
        public boolean isEmpty() {
            return this.map.isEmpty();
        }
        
        @Override
        public ObjectSet<Map.Entry<Float, Byte>> entrySet() {
            return ObjectSets.unmodifiable(this.map.entrySet());
        }
    }
}
