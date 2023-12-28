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

public class Long2CharSortedMaps
{
    public static final EmptySortedMap EMPTY_MAP;
    
    private Long2CharSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Long, ?>> entryComparator(final LongComparator comparator) {
        return new Comparator<Map.Entry<Long, ?>>() {
            @Override
            public int compare(final Map.Entry<Long, ?> x, final Map.Entry<Long, ?> y) {
                return comparator.compare(x.getKey(), y.getKey());
            }
        };
    }
    
    public static Long2CharSortedMap singleton(final Long key, final Character value) {
        return new Singleton(key, value);
    }
    
    public static Long2CharSortedMap singleton(final Long key, final Character value, final LongComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Long2CharSortedMap singleton(final long key, final char value) {
        return new Singleton(key, value);
    }
    
    public static Long2CharSortedMap singleton(final long key, final char value, final LongComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Long2CharSortedMap synchronize(final Long2CharSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Long2CharSortedMap synchronize(final Long2CharSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Long2CharSortedMap unmodifiable(final Long2CharSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Long2CharMaps.EmptyMap implements Long2CharSortedMap, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public LongComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Long2CharMap.Entry> long2CharEntrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Long, Character>> entrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public LongSortedSet keySet() {
            return LongSortedSets.EMPTY_SET;
        }
        
        @Override
        public Long2CharSortedMap subMap(final long from, final long to) {
            return Long2CharSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Long2CharSortedMap headMap(final long to) {
            return Long2CharSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Long2CharSortedMap tailMap(final long from) {
            return Long2CharSortedMaps.EMPTY_MAP;
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
        public Long2CharSortedMap headMap(final Long oto) {
            return this.headMap((long)oto);
        }
        
        @Deprecated
        @Override
        public Long2CharSortedMap tailMap(final Long ofrom) {
            return this.tailMap((long)ofrom);
        }
        
        @Deprecated
        @Override
        public Long2CharSortedMap subMap(final Long ofrom, final Long oto) {
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
    
    public static class Singleton extends Long2CharMaps.Singleton implements Long2CharSortedMap, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongComparator comparator;
        
        protected Singleton(final long key, final char value, final LongComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final long key, final char value) {
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
        public ObjectSortedSet<Long2CharMap.Entry> long2CharEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Long2CharMap.Entry>)ObjectSortedSets.singleton(new SingletonEntry(), Long2CharSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Long, Character>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Long, Character>>)this.long2CharEntrySet();
        }
        
        @Override
        public LongSortedSet keySet() {
            if (this.keys == null) {
                this.keys = LongSortedSets.singleton(this.key, this.comparator);
            }
            return (LongSortedSet)this.keys;
        }
        
        @Override
        public Long2CharSortedMap subMap(final long from, final long to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Long2CharSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Long2CharSortedMap headMap(final long to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Long2CharSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Long2CharSortedMap tailMap(final long from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Long2CharSortedMaps.EMPTY_MAP;
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
        public Long2CharSortedMap headMap(final Long oto) {
            return this.headMap((long)oto);
        }
        
        @Deprecated
        @Override
        public Long2CharSortedMap tailMap(final Long ofrom) {
            return this.tailMap((long)ofrom);
        }
        
        @Deprecated
        @Override
        public Long2CharSortedMap subMap(final Long ofrom, final Long oto) {
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
    
    public static class SynchronizedSortedMap extends Long2CharMaps.SynchronizedMap implements Long2CharSortedMap, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2CharSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Long2CharSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Long2CharSortedMap m) {
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
        public ObjectSortedSet<Long2CharMap.Entry> long2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.synchronize(this.sortedMap.long2CharEntrySet(), this.sync);
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Long, Character>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Long, Character>>)this.long2CharEntrySet();
        }
        
        @Override
        public LongSortedSet keySet() {
            if (this.keys == null) {
                this.keys = LongSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (LongSortedSet)this.keys;
        }
        
        @Override
        public Long2CharSortedMap subMap(final long from, final long to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Long2CharSortedMap headMap(final long to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Long2CharSortedMap tailMap(final long from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
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
                return ((SortedMap<Long, V>)this.sortedMap).firstKey();
            }
        }
        
        @Override
        public Long lastKey() {
            synchronized (this.sync) {
                return ((SortedMap<Long, V>)this.sortedMap).lastKey();
            }
        }
        
        @Override
        public Long2CharSortedMap subMap(final Long from, final Long to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Long2CharSortedMap headMap(final Long to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Long2CharSortedMap tailMap(final Long from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Long2CharMaps.UnmodifiableMap implements Long2CharSortedMap, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2CharSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Long2CharSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public LongComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Long2CharMap.Entry> long2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.unmodifiable(this.sortedMap.long2CharEntrySet());
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Long, Character>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Long, Character>>)this.long2CharEntrySet();
        }
        
        @Override
        public LongSortedSet keySet() {
            if (this.keys == null) {
                this.keys = LongSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (LongSortedSet)this.keys;
        }
        
        @Override
        public Long2CharSortedMap subMap(final long from, final long to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Long2CharSortedMap headMap(final long to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Long2CharSortedMap tailMap(final long from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
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
            return ((SortedMap<Long, V>)this.sortedMap).firstKey();
        }
        
        @Override
        public Long lastKey() {
            return ((SortedMap<Long, V>)this.sortedMap).lastKey();
        }
        
        @Override
        public Long2CharSortedMap subMap(final Long from, final Long to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Long2CharSortedMap headMap(final Long to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Long2CharSortedMap tailMap(final Long from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
