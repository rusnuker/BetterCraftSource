// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.util.SortedMap;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.NoSuchElementException;
import it.unimi.dsi.fastutil.objects.ObjectSortedSets;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.Serializable;
import java.util.Map;
import java.util.Comparator;

public class Int2ReferenceSortedMaps
{
    public static final EmptySortedMap EMPTY_MAP;
    
    private Int2ReferenceSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Integer, ?>> entryComparator(final IntComparator comparator) {
        return new Comparator<Map.Entry<Integer, ?>>() {
            @Override
            public int compare(final Map.Entry<Integer, ?> x, final Map.Entry<Integer, ?> y) {
                return comparator.compare(x.getKey(), y.getKey());
            }
        };
    }
    
    public static <V> Int2ReferenceSortedMap<V> emptyMap() {
        return Int2ReferenceSortedMaps.EMPTY_MAP;
    }
    
    public static <V> Int2ReferenceSortedMap<V> singleton(final Integer key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Int2ReferenceSortedMap<V> singleton(final Integer key, final V value, final IntComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Int2ReferenceSortedMap<V> singleton(final int key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Int2ReferenceSortedMap<V> singleton(final int key, final V value, final IntComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Int2ReferenceSortedMap<V> synchronize(final Int2ReferenceSortedMap<V> m) {
        return new SynchronizedSortedMap<V>(m);
    }
    
    public static <V> Int2ReferenceSortedMap<V> synchronize(final Int2ReferenceSortedMap<V> m, final Object sync) {
        return new SynchronizedSortedMap<V>(m, sync);
    }
    
    public static <V> Int2ReferenceSortedMap<V> unmodifiable(final Int2ReferenceSortedMap<V> m) {
        return new UnmodifiableSortedMap<V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<V> extends Int2ReferenceMaps.EmptyMap<V> implements Int2ReferenceSortedMap<V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public IntComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Int2ReferenceMap.Entry<V>> int2ReferenceEntrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Integer, V>> entrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public IntSortedSet keySet() {
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public Int2ReferenceSortedMap<V> subMap(final int from, final int to) {
            return Int2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2ReferenceSortedMap<V> headMap(final int to) {
            return Int2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2ReferenceSortedMap<V> tailMap(final int from) {
            return Int2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public int firstIntKey() {
            throw new NoSuchElementException();
        }
        
        @Override
        public int lastIntKey() {
            throw new NoSuchElementException();
        }
        
        @Deprecated
        @Override
        public Int2ReferenceSortedMap<V> headMap(final Integer oto) {
            return this.headMap((int)oto);
        }
        
        @Deprecated
        @Override
        public Int2ReferenceSortedMap<V> tailMap(final Integer ofrom) {
            return this.tailMap((int)ofrom);
        }
        
        @Deprecated
        @Override
        public Int2ReferenceSortedMap<V> subMap(final Integer ofrom, final Integer oto) {
            return this.subMap((int)ofrom, (int)oto);
        }
        
        @Deprecated
        @Override
        public Integer firstKey() {
            return this.firstIntKey();
        }
        
        @Deprecated
        @Override
        public Integer lastKey() {
            return this.lastIntKey();
        }
    }
    
    public static class Singleton<V> extends Int2ReferenceMaps.Singleton<V> implements Int2ReferenceSortedMap<V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntComparator comparator;
        
        protected Singleton(final int key, final V value, final IntComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final int key, final V value) {
            this(key, value, null);
        }
        
        final int compare(final int k1, final int k2) {
            return (this.comparator == null) ? Integer.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public IntComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Int2ReferenceMap.Entry<V>> int2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Int2ReferenceMap.Entry<V>>)ObjectSortedSets.singleton(new SingletonEntry(), Int2ReferenceSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Integer, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, V>>)this.int2ReferenceEntrySet();
        }
        
        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = IntSortedSets.singleton(this.key, this.comparator);
            }
            return (IntSortedSet)this.keys;
        }
        
        @Override
        public Int2ReferenceSortedMap<V> subMap(final int from, final int to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Int2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2ReferenceSortedMap<V> headMap(final int to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Int2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2ReferenceSortedMap<V> tailMap(final int from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Int2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public int firstIntKey() {
            return this.key;
        }
        
        @Override
        public int lastIntKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Int2ReferenceSortedMap<V> headMap(final Integer oto) {
            return this.headMap((int)oto);
        }
        
        @Deprecated
        @Override
        public Int2ReferenceSortedMap<V> tailMap(final Integer ofrom) {
            return this.tailMap((int)ofrom);
        }
        
        @Deprecated
        @Override
        public Int2ReferenceSortedMap<V> subMap(final Integer ofrom, final Integer oto) {
            return this.subMap((int)ofrom, (int)oto);
        }
        
        @Deprecated
        @Override
        public Integer firstKey() {
            return this.firstIntKey();
        }
        
        @Deprecated
        @Override
        public Integer lastKey() {
            return this.lastIntKey();
        }
    }
    
    public static class SynchronizedSortedMap<V> extends Int2ReferenceMaps.SynchronizedMap<V> implements Int2ReferenceSortedMap<V>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2ReferenceSortedMap<V> sortedMap;
        
        protected SynchronizedSortedMap(final Int2ReferenceSortedMap<V> m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Int2ReferenceSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public IntComparator comparator() {
            synchronized (this.sync) {
                return this.sortedMap.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<Int2ReferenceMap.Entry<V>> int2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.synchronize(this.sortedMap.int2ReferenceEntrySet(), this.sync);
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Integer, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, V>>)this.int2ReferenceEntrySet();
        }
        
        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = IntSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (IntSortedSet)this.keys;
        }
        
        @Override
        public Int2ReferenceSortedMap<V> subMap(final int from, final int to) {
            return new SynchronizedSortedMap((Int2ReferenceSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Int2ReferenceSortedMap<V> headMap(final int to) {
            return new SynchronizedSortedMap((Int2ReferenceSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Int2ReferenceSortedMap<V> tailMap(final int from) {
            return new SynchronizedSortedMap((Int2ReferenceSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
        
        @Override
        public int firstIntKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstIntKey();
            }
        }
        
        @Override
        public int lastIntKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastIntKey();
            }
        }
        
        @Override
        public Integer firstKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstKey();
            }
        }
        
        @Override
        public Integer lastKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastKey();
            }
        }
        
        @Override
        public Int2ReferenceSortedMap<V> subMap(final Integer from, final Integer to) {
            return new SynchronizedSortedMap((Int2ReferenceSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Int2ReferenceSortedMap<V> headMap(final Integer to) {
            return new SynchronizedSortedMap((Int2ReferenceSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Int2ReferenceSortedMap<V> tailMap(final Integer from) {
            return new SynchronizedSortedMap((Int2ReferenceSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap<V> extends Int2ReferenceMaps.UnmodifiableMap<V> implements Int2ReferenceSortedMap<V>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2ReferenceSortedMap<V> sortedMap;
        
        protected UnmodifiableSortedMap(final Int2ReferenceSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public IntComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Int2ReferenceMap.Entry<V>> int2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.unmodifiable(this.sortedMap.int2ReferenceEntrySet());
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Integer, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, V>>)this.int2ReferenceEntrySet();
        }
        
        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = IntSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (IntSortedSet)this.keys;
        }
        
        @Override
        public Int2ReferenceSortedMap<V> subMap(final int from, final int to) {
            return new UnmodifiableSortedMap((Int2ReferenceSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Int2ReferenceSortedMap<V> headMap(final int to) {
            return new UnmodifiableSortedMap((Int2ReferenceSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Int2ReferenceSortedMap<V> tailMap(final int from) {
            return new UnmodifiableSortedMap((Int2ReferenceSortedMap<Object>)this.sortedMap.tailMap(from));
        }
        
        @Override
        public int firstIntKey() {
            return this.sortedMap.firstIntKey();
        }
        
        @Override
        public int lastIntKey() {
            return this.sortedMap.lastIntKey();
        }
        
        @Override
        public Integer firstKey() {
            return this.sortedMap.firstKey();
        }
        
        @Override
        public Integer lastKey() {
            return this.sortedMap.lastKey();
        }
        
        @Override
        public Int2ReferenceSortedMap<V> subMap(final Integer from, final Integer to) {
            return new UnmodifiableSortedMap((Int2ReferenceSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Int2ReferenceSortedMap<V> headMap(final Integer to) {
            return new UnmodifiableSortedMap((Int2ReferenceSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Int2ReferenceSortedMap<V> tailMap(final Integer from) {
            return new UnmodifiableSortedMap((Int2ReferenceSortedMap<Object>)this.sortedMap.tailMap(from));
        }
    }
}
