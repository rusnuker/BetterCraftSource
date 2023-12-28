// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.util.Iterator;
import java.util.NoSuchElementException;
import it.unimi.dsi.fastutil.objects.AbstractObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.ints.IntCollections;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.ints.IntArrays;
import java.io.Serializable;

public class Long2IntArrayMap extends AbstractLong2IntMap implements Serializable, Cloneable
{
    private static final long serialVersionUID = 1L;
    private transient long[] key;
    private transient int[] value;
    private int size;
    
    public Long2IntArrayMap(final long[] key, final int[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
        }
    }
    
    public Long2IntArrayMap() {
        this.key = LongArrays.EMPTY_ARRAY;
        this.value = IntArrays.EMPTY_ARRAY;
    }
    
    public Long2IntArrayMap(final int capacity) {
        this.key = new long[capacity];
        this.value = new int[capacity];
    }
    
    public Long2IntArrayMap(final Long2IntMap m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Long2IntArrayMap(final Map<? extends Long, ? extends Integer> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Long2IntArrayMap(final long[] key, final int[] value, final int size) {
        this.key = key;
        this.value = value;
        this.size = size;
        if (key.length != value.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
        }
        if (size > key.length) {
            throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the backing-arrays size (" + key.length + ")");
        }
    }
    
    @Override
    public Long2IntMap.FastEntrySet long2IntEntrySet() {
        return new EntrySet();
    }
    
    private int findKey(final long k) {
        final long[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] == k) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int get(final long k) {
        final long[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] == k) {
                return this.value[i];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public void clear() {
        this.size = 0;
    }
    
    @Override
    public boolean containsKey(final long k) {
        return this.findKey(k) != -1;
    }
    
    @Override
    public boolean containsValue(final int v) {
        int i = this.size;
        while (i-- != 0) {
            if (this.value[i] == v) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    @Override
    public int put(final long k, final int v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final int oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final long[] newKey = new long[(this.size == 0) ? 2 : (this.size * 2)];
            final int[] newValue = new int[(this.size == 0) ? 2 : (this.size * 2)];
            int i = this.size;
            while (i-- != 0) {
                newKey[i] = this.key[i];
                newValue[i] = this.value[i];
            }
            this.key = newKey;
            this.value = newValue;
        }
        this.key[this.size] = k;
        this.value[this.size] = v;
        ++this.size;
        return this.defRetValue;
    }
    
    @Override
    public int remove(final long k) {
        final int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        final int oldValue = this.value[oldPos];
        final int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        return oldValue;
    }
    
    @Override
    public LongSet keySet() {
        return new LongArraySet(this.key, this.size);
    }
    
    @Override
    public IntCollection values() {
        return IntCollections.unmodifiable(new IntArraySet(this.value, this.size));
    }
    
    public Long2IntArrayMap clone() {
        Long2IntArrayMap c;
        try {
            c = (Long2IntArrayMap)super.clone();
        }
        catch (final CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.key = this.key.clone();
        c.value = this.value.clone();
        return c;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeLong(this.key[i]);
            s.writeInt(this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new long[this.size];
        this.value = new int[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readLong();
            this.value[i] = s.readInt();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Long2IntMap.Entry> implements Long2IntMap.FastEntrySet
    {
        @Override
        public ObjectIterator<Long2IntMap.Entry> iterator() {
            return new AbstractObjectIterator<Long2IntMap.Entry>() {
                int curr = -1;
                int next = 0;
                
                @Override
                public boolean hasNext() {
                    return this.next < Long2IntArrayMap.this.size;
                }
                
                @Override
                public Long2IntMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final long[] access$100 = Long2IntArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry(access$100[next], Long2IntArrayMap.this.value[this.next++]);
                }
                
                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Long2IntArrayMap.this.size-- - this.next--;
                    System.arraycopy(Long2IntArrayMap.this.key, this.next + 1, Long2IntArrayMap.this.key, this.next, tail);
                    System.arraycopy(Long2IntArrayMap.this.value, this.next + 1, Long2IntArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        @Override
        public ObjectIterator<Long2IntMap.Entry> fastIterator() {
            return new AbstractObjectIterator<Long2IntMap.Entry>() {
                int next = 0;
                int curr = -1;
                final BasicEntry entry = new BasicEntry(0L, 0);
                
                @Override
                public boolean hasNext() {
                    return this.next < Long2IntArrayMap.this.size;
                }
                
                @Override
                public Long2IntMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry entry = this.entry;
                    final long[] access$100 = Long2IntArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = access$100[next];
                    this.entry.value = Long2IntArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Long2IntArrayMap.this.size-- - this.next--;
                    System.arraycopy(Long2IntArrayMap.this.key, this.next + 1, Long2IntArrayMap.this.key, this.next, tail);
                    System.arraycopy(Long2IntArrayMap.this.value, this.next + 1, Long2IntArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        @Override
        public int size() {
            return Long2IntArrayMap.this.size;
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Integer)) {
                return false;
            }
            final long k = (long)e.getKey();
            return Long2IntArrayMap.this.containsKey(k) && Long2IntArrayMap.this.get(k) == (int)e.getValue();
        }
        
        @Override
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Integer)) {
                return false;
            }
            final long k = (long)e.getKey();
            final int v = (int)e.getValue();
            final int oldPos = Long2IntArrayMap.this.findKey(k);
            if (oldPos == -1 || v != Long2IntArrayMap.this.value[oldPos]) {
                return false;
            }
            final int tail = Long2IntArrayMap.this.size - oldPos - 1;
            System.arraycopy(Long2IntArrayMap.this.key, oldPos + 1, Long2IntArrayMap.this.key, oldPos, tail);
            System.arraycopy(Long2IntArrayMap.this.value, oldPos + 1, Long2IntArrayMap.this.value, oldPos, tail);
            Long2IntArrayMap.this.size--;
            return true;
        }
    }
}
