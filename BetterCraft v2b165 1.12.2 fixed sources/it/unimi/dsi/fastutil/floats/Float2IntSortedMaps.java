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

public class Float2IntSortedMaps
{
    public static final EmptySortedMap EMPTY_MAP;
    
    private Float2IntSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Float, ?>> entryComparator(final FloatComparator comparator) {
        return new Comparator<Map.Entry<Float, ?>>() {
            @Override
            public int compare(final Map.Entry<Float, ?> x, final Map.Entry<Float, ?> y) {
                return comparator.compare(x.getKey(), y.getKey());
            }
        };
    }
    
    public static Float2IntSortedMap singleton(final Float key, final Integer value) {
        return new Singleton(key, value);
    }
    
    public static Float2IntSortedMap singleton(final Float key, final Integer value, final FloatComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Float2IntSortedMap singleton(final float key, final int value) {
        return new Singleton(key, value);
    }
    
    public static Float2IntSortedMap singleton(final float key, final int value, final FloatComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Float2IntSortedMap synchronize(final Float2IntSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Float2IntSortedMap synchronize(final Float2IntSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Float2IntSortedMap unmodifiable(final Float2IntSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Float2IntMaps.EmptyMap implements Float2IntSortedMap, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public FloatComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Float2IntMap.Entry> float2IntEntrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Float, Integer>> entrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public FloatSortedSet keySet() {
            return FloatSortedSets.EMPTY_SET;
        }
        
        @Override
        public Float2IntSortedMap subMap(final float from, final float to) {
            return Float2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Float2IntSortedMap headMap(final float to) {
            return Float2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Float2IntSortedMap tailMap(final float from) {
            return Float2IntSortedMaps.EMPTY_MAP;
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
        public Float2IntSortedMap headMap(final Float oto) {
            return this.headMap((float)oto);
        }
        
        @Deprecated
        @Override
        public Float2IntSortedMap tailMap(final Float ofrom) {
            return this.tailMap((float)ofrom);
        }
        
        @Deprecated
        @Override
        public Float2IntSortedMap subMap(final Float ofrom, final Float oto) {
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
    
    public static class Singleton extends Float2IntMaps.Singleton implements Float2IntSortedMap, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final FloatComparator comparator;
        
        protected Singleton(final float key, final int value, final FloatComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final float key, final int value) {
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
        public ObjectSortedSet<Float2IntMap.Entry> float2IntEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Float2IntMap.Entry>)ObjectSortedSets.singleton(new SingletonEntry(), Float2IntSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Float, Integer>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Float, Integer>>)this.float2IntEntrySet();
        }
        
        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.singleton(this.key, this.comparator);
            }
            return (FloatSortedSet)this.keys;
        }
        
        @Override
        public Float2IntSortedMap subMap(final float from, final float to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Float2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Float2IntSortedMap headMap(final float to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Float2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Float2IntSortedMap tailMap(final float from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Float2IntSortedMaps.EMPTY_MAP;
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
        public Float2IntSortedMap headMap(final Float oto) {
            return this.headMap((float)oto);
        }
        
        @Deprecated
        @Override
        public Float2IntSortedMap tailMap(final Float ofrom) {
            return this.tailMap((float)ofrom);
        }
        
        @Deprecated
        @Override
        public Float2IntSortedMap subMap(final Float ofrom, final Float oto) {
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
    
    public static class SynchronizedSortedMap extends Float2IntMaps.SynchronizedMap implements Float2IntSortedMap, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2IntSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Float2IntSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Float2IntSortedMap m) {
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
        public ObjectSortedSet<Float2IntMap.Entry> float2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.synchronize(this.sortedMap.float2IntEntrySet(), this.sync);
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Float, Integer>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Float, Integer>>)this.float2IntEntrySet();
        }
        
        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (FloatSortedSet)this.keys;
        }
        
        @Override
        public Float2IntSortedMap subMap(final float from, final float to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Float2IntSortedMap headMap(final float to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Float2IntSortedMap tailMap(final float from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
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
                return ((SortedMap<Float, V>)this.sortedMap).firstKey();
            }
        }
        
        @Override
        public Float lastKey() {
            synchronized (this.sync) {
                return ((SortedMap<Float, V>)this.sortedMap).lastKey();
            }
        }
        
        @Override
        public Float2IntSortedMap subMap(final Float from, final Float to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Float2IntSortedMap headMap(final Float to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Float2IntSortedMap tailMap(final Float from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Float2IntMaps.UnmodifiableMap implements Float2IntSortedMap, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2IntSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Float2IntSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public FloatComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Float2IntMap.Entry> float2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.unmodifiable(this.sortedMap.float2IntEntrySet());
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Float, Integer>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Float, Integer>>)this.float2IntEntrySet();
        }
        
        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (FloatSortedSet)this.keys;
        }
        
        @Override
        public Float2IntSortedMap subMap(final float from, final float to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Float2IntSortedMap headMap(final float to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Float2IntSortedMap tailMap(final float from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
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
            return ((SortedMap<Float, V>)this.sortedMap).firstKey();
        }
        
        @Override
        public Float lastKey() {
            return ((SortedMap<Float, V>)this.sortedMap).lastKey();
        }
        
        @Override
        public Float2IntSortedMap subMap(final Float from, final Float to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Float2IntSortedMap headMap(final Float to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Float2IntSortedMap tailMap(final Float from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
