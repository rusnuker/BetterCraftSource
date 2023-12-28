// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ReferenceCollections;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ReferenceSets;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import java.io.Serializable;

public class Char2ReferenceMaps
{
    public static final EmptyMap EMPTY_MAP;
    
    private Char2ReferenceMaps() {
    }
    
    public static <V> Char2ReferenceMap<V> emptyMap() {
        return Char2ReferenceMaps.EMPTY_MAP;
    }
    
    public static <V> Char2ReferenceMap<V> singleton(final char key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Char2ReferenceMap<V> singleton(final Character key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Char2ReferenceMap<V> synchronize(final Char2ReferenceMap<V> m) {
        return new SynchronizedMap<V>(m);
    }
    
    public static <V> Char2ReferenceMap<V> synchronize(final Char2ReferenceMap<V> m, final Object sync) {
        return new SynchronizedMap<V>(m, sync);
    }
    
    public static <V> Char2ReferenceMap<V> unmodifiable(final Char2ReferenceMap<V> m) {
        return new UnmodifiableMap<V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<V> extends Char2ReferenceFunctions.EmptyFunction<V> implements Char2ReferenceMap<V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final Object v) {
            return false;
        }
        
        @Override
        public void putAll(final Map<? extends Character, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> char2ReferenceEntrySet() {
            return ObjectSets.EMPTY_SET;
        }
        
        @Override
        public CharSet keySet() {
            return CharSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceCollection<V> values() {
            return ReferenceSets.EMPTY_SET;
        }
        
        private Object readResolve() {
            return Char2ReferenceMaps.EMPTY_MAP;
        }
        
        @Override
        public Object clone() {
            return Char2ReferenceMaps.EMPTY_MAP;
        }
        
        @Override
        public boolean isEmpty() {
            return true;
        }
        
        @Override
        public ObjectSet<Map.Entry<Character, V>> entrySet() {
            return (ObjectSet<Map.Entry<Character, V>>)this.char2ReferenceEntrySet();
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
    
    public static class Singleton<V> extends Char2ReferenceFunctions.Singleton<V> implements Char2ReferenceMap<V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient CharSet keys;
        protected transient ReferenceCollection<V> values;
        
        protected Singleton(final char key, final V value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final Object v) {
            return this.value == v;
        }
        
        @Override
        public void putAll(final Map<? extends Character, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> char2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Entry<V>>)ObjectSets.singleton(new SingletonEntry());
            }
            return this.entries;
        }
        
        @Override
        public CharSet keySet() {
            if (this.keys == null) {
                this.keys = CharSets.singleton(this.key);
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
        public ObjectSet<Map.Entry<Character, V>> entrySet() {
            return (ObjectSet<Map.Entry<Character, V>>)this.char2ReferenceEntrySet();
        }
        
        @Override
        public int hashCode() {
            return this.key ^ ((this.value == null) ? 0 : System.identityHashCode(this.value));
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
        
        protected class SingletonEntry implements Char2ReferenceMap.Entry<V>, Map.Entry<Character, V>
        {
            @Deprecated
            @Override
            public Character getKey() {
                return Singleton.this.key;
            }
            
            @Override
            public V getValue() {
                return (V)Singleton.this.value;
            }
            
            @Override
            public char getCharKey() {
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
                return e.getKey() != null && e.getKey() instanceof Character && Singleton.this.key == (char)e.getKey() && Singleton.this.value == e.getValue();
            }
            
            @Override
            public int hashCode() {
                return Singleton.this.key ^ ((Singleton.this.value == null) ? 0 : System.identityHashCode(Singleton.this.value));
            }
            
            @Override
            public String toString() {
                return Singleton.this.key + "->" + Singleton.this.value;
            }
        }
    }
    
    public static class SynchronizedMap<V> extends Char2ReferenceFunctions.SynchronizedFunction<V> implements Char2ReferenceMap<V>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2ReferenceMap<V> map;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient CharSet keys;
        protected transient ReferenceCollection<V> values;
        
        protected SynchronizedMap(final Char2ReferenceMap<V> m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Char2ReferenceMap<V> m) {
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
        public boolean containsKey(final char k) {
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
        public V put(final char k, final V v) {
            synchronized (this.sync) {
                return this.map.put(k, v);
            }
        }
        
        @Override
        public void putAll(final Map<? extends Character, ? extends V> m) {
            synchronized (this.sync) {
                this.map.putAll((Map<?, ?>)m);
            }
        }
        
        @Override
        public ObjectSet<Entry<V>> char2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.synchronize(this.map.char2ReferenceEntrySet(), this.sync);
            }
            return this.entries;
        }
        
        @Override
        public CharSet keySet() {
            if (this.keys == null) {
                this.keys = CharSets.synchronize(this.map.keySet(), this.sync);
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
        
        @Deprecated
        @Override
        public V put(final Character k, final V v) {
            synchronized (this.sync) {
                return this.map.put(k, v);
            }
        }
        
        @Deprecated
        @Override
        public V remove(final char k) {
            synchronized (this.sync) {
                return this.map.remove(k);
            }
        }
        
        @Deprecated
        @Override
        public V get(final char k) {
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
        public ObjectSet<Map.Entry<Character, V>> entrySet() {
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
    
    public static class UnmodifiableMap<V> extends Char2ReferenceFunctions.UnmodifiableFunction<V> implements Char2ReferenceMap<V>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2ReferenceMap<V> map;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient CharSet keys;
        protected transient ReferenceCollection<V> values;
        
        protected UnmodifiableMap(final Char2ReferenceMap<V> m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public int size() {
            return this.map.size();
        }
        
        @Override
        public boolean containsKey(final char k) {
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
        public V put(final char k, final V v) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void putAll(final Map<? extends Character, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> char2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.unmodifiable(this.map.char2ReferenceEntrySet());
            }
            return this.entries;
        }
        
        @Override
        public CharSet keySet() {
            if (this.keys == null) {
                this.keys = CharSets.unmodifiable(this.map.keySet());
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
        
        @Deprecated
        @Override
        public V remove(final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V get(final char k) {
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
        public ObjectSet<Map.Entry<Character, V>> entrySet() {
            return ObjectSets.unmodifiable(this.map.entrySet());
        }
    }
}
