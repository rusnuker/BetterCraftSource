// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.SortedMap;
import java.util.Set;
import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.Map;
import java.util.Comparator;

public class Reference2LongSortedMaps
{
    public static final EmptySortedMap EMPTY_MAP;
    
    private Reference2LongSortedMaps() {
    }
    
    public static <K> Comparator<? super Map.Entry<K, ?>> entryComparator(final Comparator<K> comparator) {
        return new Comparator<Map.Entry<K, ?>>() {
            @Override
            public int compare(final Map.Entry<K, ?> x, final Map.Entry<K, ?> y) {
                return comparator.compare(x.getKey(), y.getKey());
            }
        };
    }
    
    public static <K> Reference2LongSortedMap<K> emptyMap() {
        return Reference2LongSortedMaps.EMPTY_MAP;
    }
    
    public static <K> Reference2LongSortedMap<K> singleton(final K key, final Long value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2LongSortedMap<K> singleton(final K key, final Long value, final Comparator<? super K> comparator) {
        return new Singleton<K>(key, value, comparator);
    }
    
    public static <K> Reference2LongSortedMap<K> singleton(final K key, final long value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2LongSortedMap<K> singleton(final K key, final long value, final Comparator<? super K> comparator) {
        return new Singleton<K>(key, value, comparator);
    }
    
    public static <K> Reference2LongSortedMap<K> synchronize(final Reference2LongSortedMap<K> m) {
        return new SynchronizedSortedMap<K>(m);
    }
    
    public static <K> Reference2LongSortedMap<K> synchronize(final Reference2LongSortedMap<K> m, final Object sync) {
        return new SynchronizedSortedMap<K>(m, sync);
    }
    
    public static <K> Reference2LongSortedMap<K> unmodifiable(final Reference2LongSortedMap<K> m) {
        return new UnmodifiableSortedMap<K>(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<K> extends Reference2LongMaps.EmptyMap<K> implements Reference2LongSortedMap<K>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Reference2LongMap.Entry<K>> reference2LongEntrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<K, Long>> entrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            return ReferenceSortedSets.EMPTY_SET;
        }
        
        @Override
        public Reference2LongSortedMap<K> subMap(final K from, final K to) {
            return Reference2LongSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2LongSortedMap<K> headMap(final K to) {
            return Reference2LongSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2LongSortedMap<K> tailMap(final K from) {
            return Reference2LongSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public K firstKey() {
            throw new NoSuchElementException();
        }
        
        @Override
        public K lastKey() {
            throw new NoSuchElementException();
        }
    }
    
    public static class Singleton<K> extends Reference2LongMaps.Singleton<K> implements Reference2LongSortedMap<K>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Comparator<? super K> comparator;
        
        protected Singleton(final K key, final long value, final Comparator<? super K> comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final K key, final long value) {
            this(key, value, null);
        }
        
        final int compare(final K k1, final K k2) {
            return (this.comparator == null) ? ((Comparable)k1).compareTo(k2) : this.comparator.compare((Object)k1, (Object)k2);
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Reference2LongMap.Entry<K>> reference2LongEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Reference2LongMap.Entry<K>>)ObjectSortedSets.singleton(new SingletonEntry(), (Comparator<? super SingletonEntry>)Reference2LongSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<K, Long>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Long>>)this.reference2LongEntrySet();
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.singleton(this.key, this.comparator);
            }
            return (ReferenceSortedSet)this.keys;
        }
        
        @Override
        public Reference2LongSortedMap<K> subMap(final K from, final K to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Reference2LongSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2LongSortedMap<K> headMap(final K to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Reference2LongSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2LongSortedMap<K> tailMap(final K from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Reference2LongSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public K firstKey() {
            return this.key;
        }
        
        @Override
        public K lastKey() {
            return this.key;
        }
    }
    
    public static class SynchronizedSortedMap<K> extends Reference2LongMaps.SynchronizedMap<K> implements Reference2LongSortedMap<K>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2LongSortedMap<K> sortedMap;
        
        protected SynchronizedSortedMap(final Reference2LongSortedMap<K> m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Reference2LongSortedMap<K> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public Comparator<? super K> comparator() {
            synchronized (this.sync) {
                return this.sortedMap.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<Reference2LongMap.Entry<K>> reference2LongEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.synchronize(this.sortedMap.reference2LongEntrySet(), this.sync);
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<K, Long>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Long>>)this.reference2LongEntrySet();
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (ReferenceSortedSet)this.keys;
        }
        
        @Override
        public Reference2LongSortedMap<K> subMap(final K from, final K to) {
            return new SynchronizedSortedMap((Reference2LongSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Reference2LongSortedMap<K> headMap(final K to) {
            return new SynchronizedSortedMap((Reference2LongSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Reference2LongSortedMap<K> tailMap(final K from) {
            return new SynchronizedSortedMap((Reference2LongSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
        
        @Override
        public K firstKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstKey();
            }
        }
        
        @Override
        public K lastKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastKey();
            }
        }
    }
    
    public static class UnmodifiableSortedMap<K> extends Reference2LongMaps.UnmodifiableMap<K> implements Reference2LongSortedMap<K>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2LongSortedMap<K> sortedMap;
        
        protected UnmodifiableSortedMap(final Reference2LongSortedMap<K> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Reference2LongMap.Entry<K>> reference2LongEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.unmodifiable(this.sortedMap.reference2LongEntrySet());
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<K, Long>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Long>>)this.reference2LongEntrySet();
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (ReferenceSortedSet)this.keys;
        }
        
        @Override
        public Reference2LongSortedMap<K> subMap(final K from, final K to) {
            return new UnmodifiableSortedMap((Reference2LongSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Reference2LongSortedMap<K> headMap(final K to) {
            return new UnmodifiableSortedMap((Reference2LongSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Reference2LongSortedMap<K> tailMap(final K from) {
            return new UnmodifiableSortedMap((Reference2LongSortedMap<Object>)this.sortedMap.tailMap(from));
        }
        
        @Override
        public K firstKey() {
            return this.sortedMap.firstKey();
        }
        
        @Override
        public K lastKey() {
            return this.sortedMap.lastKey();
        }
    }
}
