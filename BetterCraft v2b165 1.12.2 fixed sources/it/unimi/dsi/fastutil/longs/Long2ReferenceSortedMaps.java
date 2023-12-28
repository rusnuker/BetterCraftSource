// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.util.SortedMap;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.NoSuchElementException;
import it.unimi.dsi.fastutil.objects.ObjectSortedSets;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.Serializable;
import java.util.Map;
import java.util.Comparator;

public class Long2ReferenceSortedMaps
{
    public static final EmptySortedMap EMPTY_MAP;
    
    private Long2ReferenceSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Long, ?>> entryComparator(final LongComparator comparator) {
        return new Comparator<Map.Entry<Long, ?>>() {
            @Override
            public int compare(final Map.Entry<Long, ?> x, final Map.Entry<Long, ?> y) {
                return comparator.compare(x.getKey(), y.getKey());
            }
        };
    }
    
    public static <V> Long2ReferenceSortedMap<V> emptyMap() {
        return Long2ReferenceSortedMaps.EMPTY_MAP;
    }
    
    public static <V> Long2ReferenceSortedMap<V> singleton(final Long key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Long2ReferenceSortedMap<V> singleton(final Long key, final V value, final LongComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Long2ReferenceSortedMap<V> singleton(final long key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Long2ReferenceSortedMap<V> singleton(final long key, final V value, final LongComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Long2ReferenceSortedMap<V> synchronize(final Long2ReferenceSortedMap<V> m) {
        return new SynchronizedSortedMap<V>(m);
    }
    
    public static <V> Long2ReferenceSortedMap<V> synchronize(final Long2ReferenceSortedMap<V> m, final Object sync) {
        return new SynchronizedSortedMap<V>(m, sync);
    }
    
    public static <V> Long2ReferenceSortedMap<V> unmodifiable(final Long2ReferenceSortedMap<V> m) {
        return new UnmodifiableSortedMap<V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<V> extends Long2ReferenceMaps.EmptyMap<V> implements Long2ReferenceSortedMap<V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public LongComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Long2ReferenceMap.Entry<V>> long2ReferenceEntrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Long, V>> entrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public LongSortedSet keySet() {
            return LongSortedSets.EMPTY_SET;
        }
        
        @Override
        public Long2ReferenceSortedMap<V> subMap(final long from, final long to) {
            return Long2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Long2ReferenceSortedMap<V> headMap(final long to) {
            return Long2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Long2ReferenceSortedMap<V> tailMap(final long from) {
            return Long2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public long firstLongKey() {
            throw new NoSuchElementException();
        }
        
        @Override
        public long lastLongKey() {
            throw new NoSuchElementException();
        }
        
        @Deprecated
        @Override
        public Long2ReferenceSortedMap<V> headMap(final Long oto) {
            return this.headMap((long)oto);
        }
        
        @Deprecated
        @Override
        public Long2ReferenceSortedMap<V> tailMap(final Long ofrom) {
            return this.tailMap((long)ofrom);
        }
        
        @Deprecated
        @Override
        public Long2ReferenceSortedMap<V> subMap(final Long ofrom, final Long oto) {
            return this.subMap((long)ofrom, (long)oto);
        }
        
        @Deprecated
        @Override
        public Long firstKey() {
            return this.firstLongKey();
        }
        
        @Deprecated
        @Override
        public Long lastKey() {
            return this.lastLongKey();
        }
    }
    
    public static class Singleton<V> extends Long2ReferenceMaps.Singleton<V> implements Long2ReferenceSortedMap<V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongComparator comparator;
        
        protected Singleton(final long key, final V value, final LongComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final long key, final V value) {
            this(key, value, null);
        }
        
        final int compare(final long k1, final long k2) {
            return (this.comparator == null) ? Long.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public LongComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Long2ReferenceMap.Entry<V>> long2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Long2ReferenceMap.Entry<V>>)ObjectSortedSets.singleton(new SingletonEntry(), Long2ReferenceSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Long, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Long, V>>)this.long2ReferenceEntrySet();
        }
        
        @Override
        public LongSortedSet keySet() {
            if (this.keys == null) {
                this.keys = LongSortedSets.singleton(this.key, this.comparator);
            }
            return (LongSortedSet)this.keys;
        }
        
        @Override
        public Long2ReferenceSortedMap<V> subMap(final long from, final long to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Long2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Long2ReferenceSortedMap<V> headMap(final long to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Long2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Long2ReferenceSortedMap<V> tailMap(final long from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Long2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public long firstLongKey() {
            return this.key;
        }
        
        @Override
        public long lastLongKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Long2ReferenceSortedMap<V> headMap(final Long oto) {
            return this.headMap((long)oto);
        }
        
        @Deprecated
        @Override
        public Long2ReferenceSortedMap<V> tailMap(final Long ofrom) {
            return this.tailMap((long)ofrom);
        }
        
        @Deprecated
        @Override
        public Long2ReferenceSortedMap<V> subMap(final Long ofrom, final Long oto) {
            return this.subMap((long)ofrom, (long)oto);
        }
        
        @Deprecated
        @Override
        public Long firstKey() {
            return this.firstLongKey();
        }
        
        @Deprecated
        @Override
        public Long lastKey() {
            return this.lastLongKey();
        }
    }
    
    public static class SynchronizedSortedMap<V> extends Long2ReferenceMaps.SynchronizedMap<V> implements Long2ReferenceSortedMap<V>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2ReferenceSortedMap<V> sortedMap;
        
        protected SynchronizedSortedMap(final Long2ReferenceSortedMap<V> m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Long2ReferenceSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public LongComparator comparator() {
            synchronized (this.sync) {
                return this.sortedMap.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<Long2ReferenceMap.Entry<V>> long2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.synchronize(this.sortedMap.long2ReferenceEntrySet(), this.sync);
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Long, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Long, V>>)this.long2ReferenceEntrySet();
        }
        
        @Override
        public LongSortedSet keySet() {
            if (this.keys == null) {
                this.keys = LongSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (LongSortedSet)this.keys;
        }
        
        @Override
        public Long2ReferenceSortedMap<V> subMap(final long from, final long to) {
            return new SynchronizedSortedMap((Long2ReferenceSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Long2ReferenceSortedMap<V> headMap(final long to) {
            return new SynchronizedSortedMap((Long2ReferenceSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Long2ReferenceSortedMap<V> tailMap(final long from) {
            return new SynchronizedSortedMap((Long2ReferenceSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
        
        @Override
        public long firstLongKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstLongKey();
            }
        }
        
        @Override
        public long lastLongKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastLongKey();
            }
        }
        
        @Override
        public Long firstKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstKey();
            }
        }
        
        @Override
        public Long lastKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastKey();
            }
        }
        
        @Override
        public Long2ReferenceSortedMap<V> subMap(final Long from, final Long to) {
            return new SynchronizedSortedMap((Long2ReferenceSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Long2ReferenceSortedMap<V> headMap(final Long to) {
            return new SynchronizedSortedMap((Long2ReferenceSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Long2ReferenceSortedMap<V> tailMap(final Long from) {
            return new SynchronizedSortedMap((Long2ReferenceSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap<V> extends Long2ReferenceMaps.UnmodifiableMap<V> implements Long2ReferenceSortedMap<V>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2ReferenceSortedMap<V> sortedMap;
        
        protected UnmodifiableSortedMap(final Long2ReferenceSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public LongComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Long2ReferenceMap.Entry<V>> long2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.unmodifiable(this.sortedMap.long2ReferenceEntrySet());
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Long, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Long, V>>)this.long2ReferenceEntrySet();
        }
        
        @Override
        public LongSortedSet keySet() {
            if (this.keys == null) {
                this.keys = LongSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (LongSortedSet)this.keys;
        }
        
        @Override
        public Long2ReferenceSortedMap<V> subMap(final long from, final long to) {
            return new UnmodifiableSortedMap((Long2ReferenceSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Long2ReferenceSortedMap<V> headMap(final long to) {
            return new UnmodifiableSortedMap((Long2ReferenceSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Long2ReferenceSortedMap<V> tailMap(final long from) {
            return new UnmodifiableSortedMap((Long2ReferenceSortedMap<Object>)this.sortedMap.tailMap(from));
        }
        
        @Override
        public long firstLongKey() {
            return this.sortedMap.firstLongKey();
        }
        
        @Override
        public long lastLongKey() {
            return this.sortedMap.lastLongKey();
        }
        
        @Override
        public Long firstKey() {
            return this.sortedMap.firstKey();
        }
        
        @Override
        public Long lastKey() {
            return this.sortedMap.lastKey();
        }
        
        @Override
        public Long2ReferenceSortedMap<V> subMap(final Long from, final Long to) {
            return new UnmodifiableSortedMap((Long2ReferenceSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Long2ReferenceSortedMap<V> headMap(final Long to) {
            return new UnmodifiableSortedMap((Long2ReferenceSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Long2ReferenceSortedMap<V> tailMap(final Long from) {
            return new UnmodifiableSortedMap((Long2ReferenceSortedMap<Object>)this.sortedMap.tailMap(from));
        }
    }
}
