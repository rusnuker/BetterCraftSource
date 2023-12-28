// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

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
import it.unimi.dsi.fastutil.booleans.BooleanCollections;
import it.unimi.dsi.fastutil.booleans.BooleanArraySet;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.booleans.BooleanArrays;
import java.io.Serializable;

public class Int2BooleanArrayMap extends AbstractInt2BooleanMap implements Serializable, Cloneable
{
    private static final long serialVersionUID = 1L;
    private transient int[] key;
    private transient boolean[] value;
    private int size;
    
    public Int2BooleanArrayMap(final int[] key, final boolean[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
        }
    }
    
    public Int2BooleanArrayMap() {
        this.key = IntArrays.EMPTY_ARRAY;
        this.value = BooleanArrays.EMPTY_ARRAY;
    }
    
    public Int2BooleanArrayMap(final int capacity) {
        this.key = new int[capacity];
        this.value = new boolean[capacity];
    }
    
    public Int2BooleanArrayMap(final Int2BooleanMap m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Int2BooleanArrayMap(final Map<? extends Integer, ? extends Boolean> m) {
        this(m.size());
        this.putAll(m);
    }
    
    public Int2BooleanArrayMap(final int[] key, final boolean[] value, final int size) {
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
    public Int2BooleanMap.FastEntrySet int2BooleanEntrySet() {
        return new EntrySet();
    }
    
    private int findKey(final int k) {
        final int[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] == k) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public boolean get(final int k) {
        final int[] key = this.key;
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
    public boolean containsKey(final int k) {
        return this.findKey(k) != -1;
    }
    
    @Override
    public boolean containsValue(final boolean v) {
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
    public boolean put(final int k, final boolean v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final boolean oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final int[] newKey = new int[(this.size == 0) ? 2 : (this.size * 2)];
            final boolean[] newValue = new boolean[(this.size == 0) ? 2 : (this.size * 2)];
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
    public boolean remove(final int k) {
        final int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        final boolean oldValue = this.value[oldPos];
        final int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        return oldValue;
    }
    
    @Override
    public IntSet keySet() {
        return new IntArraySet(this.key, this.size);
    }
    
    @Override
    public BooleanCollection values() {
        return BooleanCollections.unmodifiable(new BooleanArraySet(this.value, this.size));
    }
    
    public Int2BooleanArrayMap clone() {
        Int2BooleanArrayMap c;
        try {
            c = (Int2BooleanArrayMap)super.clone();
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
            s.writeInt(this.key[i]);
            s.writeBoolean(this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new int[this.size];
        this.value = new boolean[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readInt();
            this.value[i] = s.readBoolean();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Int2BooleanMap.Entry> implements Int2BooleanMap.FastEntrySet
    {
        @Override
        public ObjectIterator<Int2BooleanMap.Entry> iterator() {
            return new AbstractObjectIterator<Int2BooleanMap.Entry>() {
                int curr = -1;
                int next = 0;
                
                @Override
                public boolean hasNext() {
                    return this.next < Int2BooleanArrayMap.this.size;
                }
                
                @Override
                public Int2BooleanMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final int[] access$100 = Int2BooleanArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry(access$100[next], Int2BooleanArrayMap.this.value[this.next++]);
                }
                
                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Int2BooleanArrayMap.this.size-- - this.next--;
                    System.arraycopy(Int2BooleanArrayMap.this.key, this.next + 1, Int2BooleanArrayMap.this.key, this.next, tail);
                    System.arraycopy(Int2BooleanArrayMap.this.value, this.next + 1, Int2BooleanArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        @Override
        public ObjectIterator<Int2BooleanMap.Entry> fastIterator() {
            return new AbstractObjectIterator<Int2BooleanMap.Entry>() {
                int next = 0;
                int curr = -1;
                final BasicEntry entry = new BasicEntry(0, false);
                
                @Override
                public boolean hasNext() {
                    return this.next < Int2BooleanArrayMap.this.size;
                }
                
                @Override
                public Int2BooleanMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry entry = this.entry;
                    final int[] access$100 = Int2BooleanArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = access$100[next];
                    this.entry.value = Int2BooleanArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Int2BooleanArrayMap.this.size-- - this.next--;
                    System.arraycopy(Int2BooleanArrayMap.this.key, this.next + 1, Int2BooleanArrayMap.this.key, this.next, tail);
                    System.arraycopy(Int2BooleanArrayMap.this.value, this.next + 1, Int2BooleanArrayMap.this.value, this.next, tail);
                }
            };
        }
        
        @Override
        public int size() {
            return Int2BooleanArrayMap.this.size;
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
                return false;
            }
            final int k = (int)e.getKey();
            return Int2BooleanArrayMap.this.containsKey(k) && Int2BooleanArrayMap.this.get(k) == (boolean)e.getValue();
        }
        
        @Override
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
                return false;
            }
            final int k = (int)e.getKey();
            final boolean v = (boolean)e.getValue();
            final int oldPos = Int2BooleanArrayMap.this.findKey(k);
            if (oldPos == -1 || v != Int2BooleanArrayMap.this.value[oldPos]) {
                return false;
            }
            final int tail = Int2BooleanArrayMap.this.size - oldPos - 1;
            System.arraycopy(Int2BooleanArrayMap.this.key, oldPos + 1, Int2BooleanArrayMap.this.key, oldPos, tail);
            System.arraycopy(Int2BooleanArrayMap.this.value, oldPos + 1, Int2BooleanArrayMap.this.value, oldPos, tail);
            Int2BooleanArrayMap.this.size--;
            return true;
        }
    }
}
