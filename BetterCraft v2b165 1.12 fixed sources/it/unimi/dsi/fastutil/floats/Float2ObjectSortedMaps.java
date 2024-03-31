// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.util.SortedMap;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.NoSuchElementException;
import it.unimi.dsi.fastutil.objects.ObjectSortedSets;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.Serializable;
import java.util.Map;
import java.util.Comparator;

public class Float2ObjectSortedMaps
{
    public static final EmptySortedMap EMPTY_MAP;
    
    private Float2ObjectSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Float, ?>> entryComparator(final FloatComparator comparator) {
        return new Comparator<Map.Entry<Float, ?>>() {
            @Override
            public int compare(final Map.Entry<Float, ?> x, final Map.Entry<Float, ?> y) {
                return comparator.compare(x.getKey(), y.getKey());
            }
        };
    }
    
    public static <V> Float2ObjectSortedMap<V> emptyMap() {
        return Float2ObjectSortedMaps.EMPTY_MAP;
    }
    
    public static <V> Float2ObjectSortedMap<V> singleton(final Float key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Float2ObjectSortedMap<V> singleton(final Float key, final V value, final FloatComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Float2ObjectSortedMap<V> singleton(final float key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Float2ObjectSortedMap<V> singleton(final float key, final V value, final FloatComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Float2ObjectSortedMap<V> synchronize(final Float2ObjectSortedMap<V> m) {
        return new SynchronizedSortedMap<V>(m);
    }
    
    public static <V> Float2ObjectSortedMap<V> synchronize(final Float2ObjectSortedMap<V> m, final Object sync) {
        return new SynchronizedSortedMap<V>(m, sync);
    }
    
    public static <V> Float2ObjectSortedMap<V> unmodifiable(final Float2ObjectSortedMap<V> m) {
        return new UnmodifiableSortedMap<V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<V> extends Float2ObjectMaps.EmptyMap<V> implements Float2ObjectSortedMap<V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public FloatComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Float2ObjectMap.Entry<V>> float2ObjectEntrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Float, V>> entrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public FloatSortedSet keySet() {
            return FloatSortedSets.EMPTY_SET;
        }
        
        @Override
        public Float2ObjectSortedMap<V> subMap(final float from, final float to) {
            return Float2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Float2ObjectSortedMap<V> headMap(final float to) {
            return Float2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Float2ObjectSortedMap<V> tailMap(final float from) {
            return Float2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public float firstFloatKey() {
            throw new NoSuchElementException();
        }
        
        @Override
        public float lastFloatKey() {
            throw new NoSuchElementException();
        }
        
        @Deprecated
        @Override
        public Float2ObjectSortedMap<V> headMap(final Float oto) {
            return this.headMap((float)oto);
        }
        
        @Deprecated
        @Override
        public Float2ObjectSortedMap<V> tailMap(final Float ofrom) {
            return this.tailMap((float)ofrom);
        }
        
        @Deprecated
        @Override
        public Float2ObjectSortedMap<V> subMap(final Float ofrom, final Float oto) {
            return this.subMap((float)ofrom, (float)oto);
        }
        
        @Deprecated
        @Override
        public Float firstKey() {
            return this.firstFloatKey();
        }
        
        @Deprecated
        @Override
        public Float lastKey() {
            return this.lastFloatKey();
        }
    }
    
    public static class Singleton<V> extends Float2ObjectMaps.Singleton<V> implements Float2ObjectSortedMap<V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final FloatComparator comparator;
        
        protected Singleton(final float key, final V value, final FloatComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final float key, final V value) {
            this(key, value, null);
        }
        
        final int compare(final float k1, final float k2) {
            return (this.comparator == null) ? Float.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public FloatComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Float2ObjectMap.Entry<V>> float2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Float2ObjectMap.Entry<V>>)ObjectSortedSets.singleton(new SingletonEntry(), Float2ObjectSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Float, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Float, V>>)this.float2ObjectEntrySet();
        }
        
        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.singleton(this.key, this.comparator);
            }
            return (FloatSortedSet)this.keys;
        }
        
        @Override
        public Float2ObjectSortedMap<V> subMap(final float from, final float to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Float2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Float2ObjectSortedMap<V> headMap(final float to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Float2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Float2ObjectSortedMap<V> tailMap(final float from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Float2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public float firstFloatKey() {
            return this.key;
        }
        
        @Override
        public float lastFloatKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Float2ObjectSortedMap<V> headMap(final Float oto) {
            return this.headMap((float)oto);
        }
        
        @Deprecated
        @Override
        public Float2ObjectSortedMap<V> tailMap(final Float ofrom) {
            return this.tailMap((float)ofrom);
        }
        
        @Deprecated
        @Override
        public Float2ObjectSortedMap<V> subMap(final Float ofrom, final Float oto) {
            return this.subMap((float)ofrom, (float)oto);
        }
        
        @Deprecated
        @Override
        public Float firstKey() {
            return this.firstFloatKey();
        }
        
        @Deprecated
        @Override
        public Float lastKey() {
            return this.lastFloatKey();
        }
    }
    
    public static class SynchronizedSortedMap<V> extends Float2ObjectMaps.SynchronizedMap<V> implements Float2ObjectSortedMap<V>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2ObjectSortedMap<V> sortedMap;
        
        protected SynchronizedSortedMap(final Float2ObjectSortedMap<V> m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Float2ObjectSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public FloatComparator comparator() {
            synchronized (this.sync) {
                return this.sortedMap.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<Float2ObjectMap.Entry<V>> float2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.synchronize(this.sortedMap.float2ObjectEntrySet(), this.sync);
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Float, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Float, V>>)this.float2ObjectEntrySet();
        }
        
        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (FloatSortedSet)this.keys;
        }
        
        @Override
        public Float2ObjectSortedMap<V> subMap(final float from, final float to) {
            return new SynchronizedSortedMap((Float2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Float2ObjectSortedMap<V> headMap(final float to) {
            return new SynchronizedSortedMap((Float2ObjectSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Float2ObjectSortedMap<V> tailMap(final float from) {
            return new SynchronizedSortedMap((Float2ObjectSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
        
        @Override
        public float firstFloatKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstFloatKey();
            }
        }
        
        @Override
        public float lastFloatKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastFloatKey();
            }
        }
        
        @Override
        public Float firstKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstKey();
            }
        }
        
        @Override
        public Float lastKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastKey();
            }
        }
        
        @Override
        public Float2ObjectSortedMap<V> subMap(final Float from, final Float to) {
            return new SynchronizedSortedMap((Float2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Float2ObjectSortedMap<V> headMap(final Float to) {
            return new SynchronizedSortedMap((Float2ObjectSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Float2ObjectSortedMap<V> tailMap(final Float from) {
            return new SynchronizedSortedMap((Float2ObjectSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap<V> extends Float2ObjectMaps.UnmodifiableMap<V> implements Float2ObjectSortedMap<V>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2ObjectSortedMap<V> sortedMap;
        
        protected UnmodifiableSortedMap(final Float2ObjectSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public FloatComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Float2ObjectMap.Entry<V>> float2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.unmodifiable(this.sortedMap.float2ObjectEntrySet());
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Float, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Float, V>>)this.float2ObjectEntrySet();
        }
        
        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (FloatSortedSet)this.keys;
        }
        
        @Override
        public Float2ObjectSortedMap<V> subMap(final float from, final float to) {
            return new UnmodifiableSortedMap((Float2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Float2ObjectSortedMap<V> headMap(final float to) {
            return new UnmodifiableSortedMap((Float2ObjectSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Float2ObjectSortedMap<V> tailMap(final float from) {
            return new UnmodifiableSortedMap((Float2ObjectSortedMap<Object>)this.sortedMap.tailMap(from));
        }
        
        @Override
        public float firstFloatKey() {
            return this.sortedMap.firstFloatKey();
        }
        
        @Override
        public float lastFloatKey() {
            return this.sortedMap.lastFloatKey();
        }
        
        @Override
        public Float firstKey() {
            return this.sortedMap.firstKey();
        }
        
        @Override
        public Float lastKey() {
            return this.sortedMap.lastKey();
        }
        
        @Override
        public Float2ObjectSortedMap<V> subMap(final Float from, final Float to) {
            return new UnmodifiableSortedMap((Float2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Float2ObjectSortedMap<V> headMap(final Float to) {
            return new UnmodifiableSortedMap((Float2ObjectSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Float2ObjectSortedMap<V> tailMap(final Float from) {
            return new UnmodifiableSortedMap((Float2ObjectSortedMap<Object>)this.sortedMap.tailMap(from));
        }
    }
}
