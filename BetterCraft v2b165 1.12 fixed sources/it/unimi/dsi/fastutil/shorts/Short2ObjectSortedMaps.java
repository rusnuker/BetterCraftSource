// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.util.SortedMap;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.NoSuchElementException;
import it.unimi.dsi.fastutil.objects.ObjectSortedSets;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.Serializable;
import java.util.Map;
import java.util.Comparator;

public class Short2ObjectSortedMaps
{
    public static final EmptySortedMap EMPTY_MAP;
    
    private Short2ObjectSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Short, ?>> entryComparator(final ShortComparator comparator) {
        return new Comparator<Map.Entry<Short, ?>>() {
            @Override
            public int compare(final Map.Entry<Short, ?> x, final Map.Entry<Short, ?> y) {
                return comparator.compare(x.getKey(), y.getKey());
            }
        };
    }
    
    public static <V> Short2ObjectSortedMap<V> emptyMap() {
        return Short2ObjectSortedMaps.EMPTY_MAP;
    }
    
    public static <V> Short2ObjectSortedMap<V> singleton(final Short key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Short2ObjectSortedMap<V> singleton(final Short key, final V value, final ShortComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Short2ObjectSortedMap<V> singleton(final short key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Short2ObjectSortedMap<V> singleton(final short key, final V value, final ShortComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Short2ObjectSortedMap<V> synchronize(final Short2ObjectSortedMap<V> m) {
        return new SynchronizedSortedMap<V>(m);
    }
    
    public static <V> Short2ObjectSortedMap<V> synchronize(final Short2ObjectSortedMap<V> m, final Object sync) {
        return new SynchronizedSortedMap<V>(m, sync);
    }
    
    public static <V> Short2ObjectSortedMap<V> unmodifiable(final Short2ObjectSortedMap<V> m) {
        return new UnmodifiableSortedMap<V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<V> extends Short2ObjectMaps.EmptyMap<V> implements Short2ObjectSortedMap<V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public ShortComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Short2ObjectMap.Entry<V>> short2ObjectEntrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Short, V>> entrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ShortSortedSet keySet() {
            return ShortSortedSets.EMPTY_SET;
        }
        
        @Override
        public Short2ObjectSortedMap<V> subMap(final short from, final short to) {
            return Short2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Short2ObjectSortedMap<V> headMap(final short to) {
            return Short2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Short2ObjectSortedMap<V> tailMap(final short from) {
            return Short2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public short firstShortKey() {
            throw new NoSuchElementException();
        }
        
        @Override
        public short lastShortKey() {
            throw new NoSuchElementException();
        }
        
        @Deprecated
        @Override
        public Short2ObjectSortedMap<V> headMap(final Short oto) {
            return this.headMap((short)oto);
        }
        
        @Deprecated
        @Override
        public Short2ObjectSortedMap<V> tailMap(final Short ofrom) {
            return this.tailMap((short)ofrom);
        }
        
        @Deprecated
        @Override
        public Short2ObjectSortedMap<V> subMap(final Short ofrom, final Short oto) {
            return this.subMap((short)ofrom, (short)oto);
        }
        
        @Deprecated
        @Override
        public Short firstKey() {
            return this.firstShortKey();
        }
        
        @Deprecated
        @Override
        public Short lastKey() {
            return this.lastShortKey();
        }
    }
    
    public static class Singleton<V> extends Short2ObjectMaps.Singleton<V> implements Short2ObjectSortedMap<V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ShortComparator comparator;
        
        protected Singleton(final short key, final V value, final ShortComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final short key, final V value) {
            this(key, value, null);
        }
        
        final int compare(final short k1, final short k2) {
            return (this.comparator == null) ? Short.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public ShortComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Short2ObjectMap.Entry<V>> short2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Short2ObjectMap.Entry<V>>)ObjectSortedSets.singleton(new SingletonEntry(), Short2ObjectSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Short, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Short, V>>)this.short2ObjectEntrySet();
        }
        
        @Override
        public ShortSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ShortSortedSets.singleton(this.key, this.comparator);
            }
            return (ShortSortedSet)this.keys;
        }
        
        @Override
        public Short2ObjectSortedMap<V> subMap(final short from, final short to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Short2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Short2ObjectSortedMap<V> headMap(final short to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Short2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Short2ObjectSortedMap<V> tailMap(final short from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Short2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public short firstShortKey() {
            return this.key;
        }
        
        @Override
        public short lastShortKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Short2ObjectSortedMap<V> headMap(final Short oto) {
            return this.headMap((short)oto);
        }
        
        @Deprecated
        @Override
        public Short2ObjectSortedMap<V> tailMap(final Short ofrom) {
            return this.tailMap((short)ofrom);
        }
        
        @Deprecated
        @Override
        public Short2ObjectSortedMap<V> subMap(final Short ofrom, final Short oto) {
            return this.subMap((short)ofrom, (short)oto);
        }
        
        @Deprecated
        @Override
        public Short firstKey() {
            return this.firstShortKey();
        }
        
        @Deprecated
        @Override
        public Short lastKey() {
            return this.lastShortKey();
        }
    }
    
    public static class SynchronizedSortedMap<V> extends Short2ObjectMaps.SynchronizedMap<V> implements Short2ObjectSortedMap<V>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Short2ObjectSortedMap<V> sortedMap;
        
        protected SynchronizedSortedMap(final Short2ObjectSortedMap<V> m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Short2ObjectSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public ShortComparator comparator() {
            synchronized (this.sync) {
                return this.sortedMap.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<Short2ObjectMap.Entry<V>> short2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.synchronize(this.sortedMap.short2ObjectEntrySet(), this.sync);
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Short, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Short, V>>)this.short2ObjectEntrySet();
        }
        
        @Override
        public ShortSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ShortSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (ShortSortedSet)this.keys;
        }
        
        @Override
        public Short2ObjectSortedMap<V> subMap(final short from, final short to) {
            return new SynchronizedSortedMap((Short2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Short2ObjectSortedMap<V> headMap(final short to) {
            return new SynchronizedSortedMap((Short2ObjectSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Short2ObjectSortedMap<V> tailMap(final short from) {
            return new SynchronizedSortedMap((Short2ObjectSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
        
        @Override
        public short firstShortKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstShortKey();
            }
        }
        
        @Override
        public short lastShortKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastShortKey();
            }
        }
        
        @Override
        public Short firstKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstKey();
            }
        }
        
        @Override
        public Short lastKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastKey();
            }
        }
        
        @Override
        public Short2ObjectSortedMap<V> subMap(final Short from, final Short to) {
            return new SynchronizedSortedMap((Short2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Short2ObjectSortedMap<V> headMap(final Short to) {
            return new SynchronizedSortedMap((Short2ObjectSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Short2ObjectSortedMap<V> tailMap(final Short from) {
            return new SynchronizedSortedMap((Short2ObjectSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap<V> extends Short2ObjectMaps.UnmodifiableMap<V> implements Short2ObjectSortedMap<V>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Short2ObjectSortedMap<V> sortedMap;
        
        protected UnmodifiableSortedMap(final Short2ObjectSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public ShortComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Short2ObjectMap.Entry<V>> short2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.unmodifiable(this.sortedMap.short2ObjectEntrySet());
            }
            return (ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<Short, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Short, V>>)this.short2ObjectEntrySet();
        }
        
        @Override
        public ShortSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ShortSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (ShortSortedSet)this.keys;
        }
        
        @Override
        public Short2ObjectSortedMap<V> subMap(final short from, final short to) {
            return new UnmodifiableSortedMap((Short2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Short2ObjectSortedMap<V> headMap(final short to) {
            return new UnmodifiableSortedMap((Short2ObjectSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Short2ObjectSortedMap<V> tailMap(final short from) {
            return new UnmodifiableSortedMap((Short2ObjectSortedMap<Object>)this.sortedMap.tailMap(from));
        }
        
        @Override
        public short firstShortKey() {
            return this.sortedMap.firstShortKey();
        }
        
        @Override
        public short lastShortKey() {
            return this.sortedMap.lastShortKey();
        }
        
        @Override
        public Short firstKey() {
            return this.sortedMap.firstKey();
        }
        
        @Override
        public Short lastKey() {
            return this.sortedMap.lastKey();
        }
        
        @Override
        public Short2ObjectSortedMap<V> subMap(final Short from, final Short to) {
            return new UnmodifiableSortedMap((Short2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Short2ObjectSortedMap<V> headMap(final Short to) {
            return new UnmodifiableSortedMap((Short2ObjectSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Short2ObjectSortedMap<V> tailMap(final Short from) {
            return new UnmodifiableSortedMap((Short2ObjectSortedMap<Object>)this.sortedMap.tailMap(from));
        }
    }
}
