// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.doubles.DoubleCollections;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.doubles.DoubleSets;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import java.io.Serializable;

public class Char2DoubleMaps
{
    public static final EmptyMap EMPTY_MAP;
    
    private Char2DoubleMaps() {
    }
    
    public static Char2DoubleMap singleton(final char key, final double value) {
        return new Singleton(key, value);
    }
    
    public static Char2DoubleMap singleton(final Character key, final Double value) {
        return new Singleton(key, value);
    }
    
    public static Char2DoubleMap synchronize(final Char2DoubleMap m) {
        return new SynchronizedMap(m);
    }
    
    public static Char2DoubleMap synchronize(final Char2DoubleMap m, final Object sync) {
        return new SynchronizedMap(m, sync);
    }
    
    public static Char2DoubleMap unmodifiable(final Char2DoubleMap m) {
        return new UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Char2DoubleFunctions.EmptyFunction implements Char2DoubleMap, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final double v) {
            return false;
        }
        
        @Override
        public void putAll(final Map<? extends Character, ? extends Double> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> char2DoubleEntrySet() {
            return ObjectSets.EMPTY_SET;
        }
        
        @Override
        public CharSet keySet() {
            return CharSets.EMPTY_SET;
        }
        
        @Override
        public DoubleCollection values() {
            return DoubleSets.EMPTY_SET;
        }
        
        @Override
        public boolean containsValue(final Object ov) {
            return false;
        }
        
        private Object readResolve() {
            return Char2DoubleMaps.EMPTY_MAP;
        }
        
        @Override
        public Object clone() {
            return Char2DoubleMaps.EMPTY_MAP;
        }
        
        @Override
        public boolean isEmpty() {
            return true;
        }
        
        @Override
        public ObjectSet<Map.Entry<Character, Double>> entrySet() {
            return (ObjectSet<Map.Entry<Character, Double>>)this.char2DoubleEntrySet();
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
    
    public static class Singleton extends Char2DoubleFunctions.Singleton implements Char2DoubleMap, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient CharSet keys;
        protected transient DoubleCollection values;
        
        protected Singleton(final char key, final double value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final double v) {
            return this.value == v;
        }
        
        @Override
        public boolean containsValue(final Object ov) {
            return (double)ov == this.value;
        }
        
        @Override
        public void putAll(final Map<? extends Character, ? extends Double> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> char2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Entry>)ObjectSets.singleton(new SingletonEntry());
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
        public DoubleCollection values() {
            if (this.values == null) {
                this.values = DoubleSets.singleton(this.value);
            }
            return this.values;
        }
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        public ObjectSet<Map.Entry<Character, Double>> entrySet() {
            return (ObjectSet<Map.Entry<Character, Double>>)this.char2DoubleEntrySet();
        }
        
        @Override
        public int hashCode() {
            return this.key ^ HashCommon.double2int(this.value);
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
        
        protected class SingletonEntry implements Char2DoubleMap.Entry, Map.Entry<Character, Double>
        {
            @Deprecated
            @Override
            public Character getKey() {
                return Singleton.this.key;
            }
            
            @Deprecated
            @Override
            public Double getValue() {
                return Singleton.this.value;
            }
            
            @Override
            public char getCharKey() {
                return Singleton.this.key;
            }
            
            @Override
            public double getDoubleValue() {
                return Singleton.this.value;
            }
            
            @Override
            public double setValue(final double value) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public Double setValue(final Double value) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public boolean equals(final Object o) {
                if (!(o instanceof Map.Entry)) {
                    return false;
                }
                final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
                return e.getKey() != null && e.getKey() instanceof Character && e.getValue() != null && e.getValue() instanceof Double && Singleton.this.key == (char)e.getKey() && Singleton.this.value == (double)e.getValue();
            }
            
            @Override
            public int hashCode() {
                return Singleton.this.key ^ HashCommon.double2int(Singleton.this.value);
            }
            
            @Override
            public String toString() {
                return Singleton.this.key + "->" + Singleton.this.value;
            }
        }
    }
    
    public static class SynchronizedMap extends Char2DoubleFunctions.SynchronizedFunction implements Char2DoubleMap, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2DoubleMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient CharSet keys;
        protected transient DoubleCollection values;
        
        protected SynchronizedMap(final Char2DoubleMap m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Char2DoubleMap m) {
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
        public boolean containsValue(final double v) {
            synchronized (this.sync) {
                return this.map.containsValue(v);
            }
        }
        
        @Override
        public double defaultReturnValue() {
            synchronized (this.sync) {
                return this.map.defaultReturnValue();
            }
        }
        
        @Override
        public void defaultReturnValue(final double defRetValue) {
            synchronized (this.sync) {
                this.map.defaultReturnValue(defRetValue);
            }
        }
        
        @Override
        public double put(final char k, final double v) {
            synchronized (this.sync) {
                return this.map.put(k, v);
            }
        }
        
        @Override
        public void putAll(final Map<? extends Character, ? extends Double> m) {
            synchronized (this.sync) {
                this.map.putAll(m);
            }
        }
        
        @Override
        public ObjectSet<Entry> char2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.synchronize(this.map.char2DoubleEntrySet(), this.sync);
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
        public DoubleCollection values() {
            if (this.values == null) {
                return DoubleCollections.synchronize(this.map.values(), this.sync);
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
        public Double put(final Character k, final Double v) {
            synchronized (this.sync) {
                return this.map.put(k, v);
            }
        }
        
        @Deprecated
        @Override
        public double remove(final char k) {
            synchronized (this.sync) {
                return this.map.remove(k);
            }
        }
        
        @Deprecated
        @Override
        public double get(final char k) {
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
        public ObjectSet<Map.Entry<Character, Double>> entrySet() {
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
    
    public static class UnmodifiableMap extends Char2DoubleFunctions.UnmodifiableFunction implements Char2DoubleMap, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2DoubleMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient CharSet keys;
        protected transient DoubleCollection values;
        
        protected UnmodifiableMap(final Char2DoubleMap m) {
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
        public boolean containsValue(final double v) {
            return this.map.containsValue(v);
        }
        
        @Override
        public double defaultReturnValue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void defaultReturnValue(final double defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double put(final char k, final double v) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void putAll(final Map<? extends Character, ? extends Double> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> char2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.unmodifiable(this.map.char2DoubleEntrySet());
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
        public DoubleCollection values() {
            if (this.values == null) {
                return DoubleCollections.unmodifiable(this.map.values());
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
        public Double put(final Character k, final Double v) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public double remove(final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public double get(final char k) {
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
        public ObjectSet<Map.Entry<Character, Double>> entrySet() {
            return ObjectSets.unmodifiable(this.map.entrySet());
        }
    }
}
