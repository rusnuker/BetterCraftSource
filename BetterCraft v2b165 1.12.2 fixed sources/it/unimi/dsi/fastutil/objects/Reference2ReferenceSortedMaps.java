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

public class Reference2ReferenceSortedMaps
{
    public static final EmptySortedMap EMPTY_MAP;
    
    private Reference2ReferenceSortedMaps() {
    }
    
    public static <K> Comparator<? super Map.Entry<K, ?>> entryComparator(final Comparator<K> comparator) {
        return new Comparator<Map.Entry<K, ?>>() {
            @Override
            public int compare(final Map.Entry<K, ?> x, final Map.Entry<K, ?> y) {
                return comparator.compare(x.getKey(), y.getKey());
            }
        };
    }
    
    public static <K, V> Reference2ReferenceSortedMap<K, V> emptyMap() {
        return Reference2ReferenceSortedMaps.EMPTY_MAP;
    }
    
    public static <K, V> Reference2ReferenceSortedMap<K, V> singleton(final K key, final V value) {
        return new Singleton<K, V>(key, value);
    }
    
    public static <K, V> Reference2ReferenceSortedMap<K, V> singleton(final K key, final V value, final Comparator<? super K> comparator) {
        return new Singleton<K, V>(key, value, comparator);
    }
    
    public static <K, V> Reference2ReferenceSortedMap<K, V> synchronize(final Reference2ReferenceSortedMap<K, V> m) {
        return new SynchronizedSortedMap<K, V>(m);
    }
    
    public static <K, V> Reference2ReferenceSortedMap<K, V> synchronize(final Reference2ReferenceSortedMap<K, V> m, final Object sync) {
        return new SynchronizedSortedMap<K, V>(m, sync);
    }
    
    public static <K, V> Reference2ReferenceSortedMap<K, V> unmodifiable(final Reference2ReferenceSortedMap<K, V> m) {
        return new UnmodifiableSortedMap<K, V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<K, V> extends Reference2ReferenceMaps.EmptyMap<K, V> implements Reference2ReferenceSortedMap<K, V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Reference2ReferenceMap.Entry<K, V>> reference2ReferenceEntrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<K, V>> entrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            return ReferenceSortedSets.EMPTY_SET;
        }
        
        @Override
        public Reference2ReferenceSortedMap<K, V> subMap(final K from, final K to) {
            return Reference2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2ReferenceSortedMap<K, V> headMap(final K to) {
            return Reference2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2ReferenceSortedMap<K, V> tailMap(final K from) {
            return Reference2ReferenceSortedMaps.EMPTY_MAP;
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
    
    public static class Singleton<K, V> extends Reference2ReferenceMaps.Singleton<K, V> implements Reference2ReferenceSortedMap<K, V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Comparator<? super K> comparator;
        
        protected Singleton(final K key, final V value, final Comparator<? super K> comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final K key, final V value) {
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
        public ObjectSortedSet<Reference2ReferenceMap.Entry<K, V>> reference2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Reference2ReferenceMap.Entry<K, V>>)ObjectSortedSets.singleton(new SingletonEntry(), (Comparator<? super SingletonEntry>)Reference2ReferenceSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<K, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, V>>)this.reference2ReferenceEntrySet();
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.singleton(this.key, this.comparator);
            }
            return (ReferenceSortedSet)this.keys;
        }
        
        @Override
        public Reference2ReferenceSortedMap<K, V> subMap(final K from, final K to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Reference2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2ReferenceSortedMap<K, V> headMap(final K to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Reference2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2ReferenceSortedMap<K, V> tailMap(final K from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Reference2ReferenceSortedMaps.EMPTY_MAP;
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
    
    public static class SynchronizedSortedMap<K, V> extends Reference2ReferenceMaps.SynchronizedMap<K, V> implements Reference2ReferenceSortedMap<K, V>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2ReferenceSortedMap<K, V> sortedMap;
        
        protected SynchronizedSortedMap(final Reference2ReferenceSortedMap<K, V> m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Reference2ReferenceSortedMap<K, V> m) {
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
        public ObjectSortedSet<Reference2ReferenceMap.Entry<K, V>> reference2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.synchronize(this.sortedMap.reference2ReferenceEntrySet(), this.sync);
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<K, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, V>>)this.reference2ReferenceEntrySet();
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (ReferenceSortedSet)this.keys;
        }
        
        @Override
        public Reference2ReferenceSortedMap<K, V> subMap(final K from, final K to) {
            return new SynchronizedSortedMap((Reference2ReferenceSortedMap<Object, Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Reference2ReferenceSortedMap<K, V> headMap(final K to) {
            return new SynchronizedSortedMap((Reference2ReferenceSortedMap<Object, Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Reference2ReferenceSortedMap<K, V> tailMap(final K from) {
            return new SynchronizedSortedMap((Reference2ReferenceSortedMap<Object, Object>)this.sortedMap.tailMap(from), this.sync);
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
    
    public static class UnmodifiableSortedMap<K, V> extends Reference2ReferenceMaps.UnmodifiableMap<K, V> implements Reference2ReferenceSortedMap<K, V>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2ReferenceSortedMap<K, V> sortedMap;
        
        protected UnmodifiableSortedMap(final Reference2ReferenceSortedMap<K, V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Reference2ReferenceMap.Entry<K, V>> reference2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.unmodifiable(this.sortedMap.reference2ReferenceEntrySet());
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<K, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, V>>)this.reference2ReferenceEntrySet();
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (ReferenceSortedSet)this.keys;
        }
        
        @Override
        public Reference2ReferenceSortedMap<K, V> subMap(final K from, final K to) {
            return new UnmodifiableSortedMap((Reference2ReferenceSortedMap<Object, Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Reference2ReferenceSortedMap<K, V> headMap(final K to) {
            return new UnmodifiableSortedMap((Reference2ReferenceSortedMap<Object, Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Reference2ReferenceSortedMap<K, V> tailMap(final K from) {
            return new UnmodifiableSortedMap((Reference2ReferenceSortedMap<Object, Object>)this.sortedMap.tailMap(from));
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
