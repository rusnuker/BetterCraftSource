// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Object2ObjectOpenCustomHashMap<K, V> extends AbstractObject2ObjectMap<K, V> implements Serializable, Cloneable, Hash
{
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient K[] key;
    protected transient V[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected Strategy<K> strategy;
    protected transient int n;
    protected transient int maxFill;
    protected int size;
    protected final float f;
    protected transient Object2ObjectMap.FastEntrySet<K, V> entries;
    protected transient ObjectSet<K> keys;
    protected transient ObjectCollection<V> values;
    
    public Object2ObjectOpenCustomHashMap(final int expected, final float f, final Strategy<K> strategy) {
        this.strategy = strategy;
        if (f <= 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1");
        }
        if (expected < 0) {
            throw new IllegalArgumentException("The expected number of elements must be nonnegative");
        }
        this.f = f;
        this.n = HashCommon.arraySize(expected, f);
        this.mask = this.n - 1;
        this.maxFill = HashCommon.maxFill(this.n, f);
        this.key = (K[])new Object[this.n + 1];
        this.value = (V[])new Object[this.n + 1];
    }
    
    public Object2ObjectOpenCustomHashMap(final int expected, final Strategy<K> strategy) {
        this(expected, 0.75f, strategy);
    }
    
    public Object2ObjectOpenCustomHashMap(final Strategy<K> strategy) {
        this(16, 0.75f, strategy);
    }
    
    public Object2ObjectOpenCustomHashMap(final Map<? extends K, ? extends V> m, final float f, final Strategy<K> strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Object2ObjectOpenCustomHashMap(final Map<? extends K, ? extends V> m, final Strategy<K> strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Object2ObjectOpenCustomHashMap(final Object2ObjectMap<K, V> m, final float f, final Strategy<K> strategy) {
        this(m.size(), f, strategy);
        this.putAll((Map<? extends K, ? extends V>)m);
    }
    
    public Object2ObjectOpenCustomHashMap(final Object2ObjectMap<K, V> m, final Strategy<K> strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Object2ObjectOpenCustomHashMap(final K[] k, final V[] v, final float f, final Strategy<K> strategy) {
        this(k.length, f, strategy);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Object2ObjectOpenCustomHashMap(final K[] k, final V[] v, final Strategy<K> strategy) {
        this(k, v, 0.75f, (Strategy<Object>)strategy);
    }
    
    public Strategy<K> strategy() {
        return this.strategy;
    }
    
    private int realSize() {
        return this.containsNullKey ? (this.size - 1) : this.size;
    }
    
    private void ensureCapacity(final int capacity) {
        final int needed = HashCommon.arraySize(capacity, this.f);
        if (needed > this.n) {
            this.rehash(needed);
        }
    }
    
    private void tryCapacity(final long capacity) {
        final int needed = (int)Math.min(1073741824L, Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(capacity / this.f))));
        if (needed > this.n) {
            this.rehash(needed);
        }
    }
    
    private V removeEntry(final int pos) {
        final V oldValue = this.value[pos];
        this.value[pos] = null;
        --this.size;
        this.shiftKeys(pos);
        if (this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    private V removeNullEntry() {
        this.containsNullKey = false;
        this.key[this.n] = null;
        final V oldValue = this.value[this.n];
        this.value[this.n] = null;
        --this.size;
        if (this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int insert(final K k, final V v) {
        int pos;
        if (this.strategy.equals(k, null)) {
            if (this.containsNullKey) {
                return this.n;
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final K[] key = this.key;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) != null) {
                if (this.strategy.equals(curr, k)) {
                    return pos;
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                    if (this.strategy.equals(curr, k)) {
                        return pos;
                    }
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return -1;
    }
    
    @Override
    public V put(final K k, final V v) {
        final int pos = this.insert(k, v);
        if (pos < 0) {
            return this.defRetValue;
        }
        final V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    protected final void shiftKeys(int pos) {
        final K[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            K curr;
            while ((curr = key[pos]) != null) {
                final int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
                Label_0103: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0103;
                        }
                        if (slot > pos) {
                            break Label_0103;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0103;
                    }
                    pos = (pos + 1 & this.mask);
                    continue;
                }
                key[last] = curr;
                this.value[last] = this.value[pos];
                continue Label_0006;
            }
            break;
        }
        key[last] = null;
        this.value[last] = null;
    }
    
    @Override
    public V remove(final Object k) {
        if (this.strategy.equals((K)k, null)) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final K[] key = this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode((K)k)) & this.mask)]) == null) {
                return this.defRetValue;
            }
            if (this.strategy.equals((K)k, curr)) {
                return this.removeEntry(pos);
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (this.strategy.equals((K)k, curr)) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    @Override
    public V get(final Object k) {
        if (this.strategy.equals((K)k, null)) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode((K)k)) & this.mask)]) == null) {
            return this.defRetValue;
        }
        if (this.strategy.equals((K)k, curr)) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (this.strategy.equals((K)k, curr)) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public boolean containsKey(final Object k) {
        if (this.strategy.equals((K)k, null)) {
            return this.containsNullKey;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode((K)k)) & this.mask)]) == null) {
            return false;
        }
        if (this.strategy.equals((K)k, curr)) {
            return true;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (this.strategy.equals((K)k, curr)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final Object v) {
        final V[] value = this.value;
        final K[] key = this.key;
        Label_0048: {
            if (this.containsNullKey) {
                if (value[this.n] == null) {
                    if (v != null) {
                        break Label_0048;
                    }
                }
                else if (!value[this.n].equals(v)) {
                    break Label_0048;
                }
                return true;
            }
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] != null) {
                if (value[i] == null) {
                    if (v != null) {
                        continue;
                    }
                }
                else if (!value[i].equals(v)) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNullKey = false;
        Arrays.fill(this.key, null);
        Arrays.fill(this.value, null);
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    @Deprecated
    public void growthFactor(final int growthFactor) {
    }
    
    @Deprecated
    public int growthFactor() {
        return 16;
    }
    
    @Override
    public Object2ObjectMap.FastEntrySet<K, V> object2ObjectEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public ObjectSet<K> keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public ObjectCollection<V> values() {
        if (this.values == null) {
            this.values = new AbstractObjectCollection<V>() {
                @Override
                public ObjectIterator<V> iterator() {
                    return new ValueIterator();
                }
                
                @Override
                public int size() {
                    return Object2ObjectOpenCustomHashMap.this.size;
                }
                
                @Override
                public boolean contains(final Object v) {
                    return Object2ObjectOpenCustomHashMap.this.containsValue(v);
                }
                
                @Override
                public void clear() {
                    Object2ObjectOpenCustomHashMap.this.clear();
                }
            };
        }
        return this.values;
    }
    
    @Deprecated
    public boolean rehash() {
        return true;
    }
    
    public boolean trim() {
        final int l = HashCommon.arraySize(this.size, this.f);
        if (l >= this.n || this.size > HashCommon.maxFill(l, this.f)) {
            return true;
        }
        try {
            this.rehash(l);
        }
        catch (final OutOfMemoryError cantDoIt) {
            return false;
        }
        return true;
    }
    
    public boolean trim(final int n) {
        final int l = HashCommon.nextPowerOfTwo((int)Math.ceil(n / this.f));
        if (l >= n || this.size > HashCommon.maxFill(l, this.f)) {
            return true;
        }
        try {
            this.rehash(l);
        }
        catch (final OutOfMemoryError cantDoIt) {
            return false;
        }
        return true;
    }
    
    protected void rehash(final int newN) {
        final K[] key = this.key;
        final V[] value = this.value;
        final int mask = newN - 1;
        final K[] newKey = (K[])new Object[newN + 1];
        final V[] newValue = (V[])new Object[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == null) {}
            int pos;
            if (newKey[pos = (HashCommon.mix(this.strategy.hashCode(key[i])) & mask)] != null) {
                while (newKey[pos = (pos + 1 & mask)] != null) {}
            }
            newKey[pos] = key[i];
            newValue[pos] = value[i];
        }
        newValue[newN] = value[this.n];
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
        this.value = newValue;
    }
    
    public Object2ObjectOpenCustomHashMap<K, V> clone() {
        Object2ObjectOpenCustomHashMap<K, V> c;
        try {
            c = (Object2ObjectOpenCustomHashMap)super.clone();
        }
        catch (final CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys = null;
        c.values = null;
        c.entries = null;
        c.containsNullKey = this.containsNullKey;
        c.key = this.key.clone();
        c.value = this.value.clone();
        c.strategy = this.strategy;
        return c;
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int j = this.realSize();
        int i = 0;
        int t = 0;
        while (j-- != 0) {
            while (this.key[i] == null) {
                ++i;
            }
            if (this != this.key[i]) {
                t = this.strategy.hashCode(this.key[i]);
            }
            if (this != this.value[i]) {
                t ^= ((this.value[i] == null) ? 0 : this.value[i].hashCode());
            }
            h += t;
            ++i;
        }
        if (this.containsNullKey) {
            h += ((this.value[this.n] == null) ? 0 : this.value[this.n].hashCode());
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final K[] key = this.key;
        final V[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeObject(key[e]);
            s.writeObject(value[e]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        final Object[] key2 = new Object[this.n + 1];
        this.key = (K[])key2;
        final K[] key = (K[])key2;
        final Object[] value2 = new Object[this.n + 1];
        this.value = (V[])value2;
        final V[] value = (V[])value2;
        int i = this.size;
        while (i-- != 0) {
            final K k = (K)s.readObject();
            final V v = (V)s.readObject();
            int pos;
            if (this.strategy.equals(k, null)) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask); key[pos] != null; pos = (pos + 1 & this.mask)) {}
            }
            key[pos] = k;
            value[pos] = v;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Object2ObjectMap.Entry<K, V>, Map.Entry<K, V>
    {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        @Override
        public K getKey() {
            return Object2ObjectOpenCustomHashMap.this.key[this.index];
        }
        
        @Override
        public V getValue() {
            return Object2ObjectOpenCustomHashMap.this.value[this.index];
        }
        
        @Override
        public V setValue(final V v) {
            final V oldValue = Object2ObjectOpenCustomHashMap.this.value[this.index];
            Object2ObjectOpenCustomHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<K, V> e = (Map.Entry<K, V>)o;
            return Object2ObjectOpenCustomHashMap.this.strategy.equals(Object2ObjectOpenCustomHashMap.this.key[this.index], e.getKey()) && ((Object2ObjectOpenCustomHashMap.this.value[this.index] != null) ? Object2ObjectOpenCustomHashMap.this.value[this.index].equals(e.getValue()) : (e.getValue() == null));
        }
        
        @Override
        public int hashCode() {
            return Object2ObjectOpenCustomHashMap.this.strategy.hashCode(Object2ObjectOpenCustomHashMap.this.key[this.index]) ^ ((Object2ObjectOpenCustomHashMap.this.value[this.index] == null) ? 0 : Object2ObjectOpenCustomHashMap.this.value[this.index].hashCode());
        }
        
        @Override
        public String toString() {
            return Object2ObjectOpenCustomHashMap.this.key[this.index] + "=>" + Object2ObjectOpenCustomHashMap.this.value[this.index];
        }
    }
    
    private class MapIterator
    {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        ObjectArrayList<K> wrapped;
        
        private MapIterator() {
            this.pos = Object2ObjectOpenCustomHashMap.this.n;
            this.last = -1;
            this.c = Object2ObjectOpenCustomHashMap.this.size;
            this.mustReturnNullKey = Object2ObjectOpenCustomHashMap.this.containsNullKey;
        }
        
        public boolean hasNext() {
            return this.c != 0;
        }
        
        public int nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            --this.c;
            if (this.mustReturnNullKey) {
                this.mustReturnNullKey = false;
                return this.last = Object2ObjectOpenCustomHashMap.this.n;
            }
            final K[] key = Object2ObjectOpenCustomHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != null) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            K k;
            int p;
            for (k = this.wrapped.get(-this.pos - 1), p = (HashCommon.mix(Object2ObjectOpenCustomHashMap.this.strategy.hashCode(k)) & Object2ObjectOpenCustomHashMap.this.mask); !Object2ObjectOpenCustomHashMap.this.strategy.equals(k, key[p]); p = (p + 1 & Object2ObjectOpenCustomHashMap.this.mask)) {}
            return p;
        }
        
        private final void shiftKeys(int pos) {
            final K[] key = Object2ObjectOpenCustomHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Object2ObjectOpenCustomHashMap.this.mask);
                K curr;
                while ((curr = key[pos]) != null) {
                    final int slot = HashCommon.mix(Object2ObjectOpenCustomHashMap.this.strategy.hashCode(curr)) & Object2ObjectOpenCustomHashMap.this.mask;
                    Label_0121: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0121;
                            }
                            if (slot > pos) {
                                break Label_0121;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0121;
                        }
                        pos = (pos + 1 & Object2ObjectOpenCustomHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new ObjectArrayList<K>(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Object2ObjectOpenCustomHashMap.this.value[last] = Object2ObjectOpenCustomHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = null;
            Object2ObjectOpenCustomHashMap.this.value[last] = null;
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Object2ObjectOpenCustomHashMap.this.n) {
                Object2ObjectOpenCustomHashMap.this.containsNullKey = false;
                Object2ObjectOpenCustomHashMap.this.key[Object2ObjectOpenCustomHashMap.this.n] = null;
                Object2ObjectOpenCustomHashMap.this.value[Object2ObjectOpenCustomHashMap.this.n] = null;
            }
            else {
                if (this.pos < 0) {
                    Object2ObjectOpenCustomHashMap.this.remove(this.wrapped.set(-this.pos - 1, null));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Object2ObjectOpenCustomHashMap this$0 = Object2ObjectOpenCustomHashMap.this;
            --this$0.size;
            this.last = -1;
        }
        
        public int skip(final int n) {
            int i = n;
            while (i-- != 0 && this.hasNext()) {
                this.nextEntry();
            }
            return n - i - 1;
        }
    }
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Object2ObjectMap.Entry<K, V>>
    {
        private MapEntry entry;
        
        @Override
        public Object2ObjectMap.Entry<K, V> next() {
            return this.entry = new MapEntry(this.nextEntry());
        }
        
        @Override
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Object2ObjectMap.Entry<K, V>>
    {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        @Override
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Object2ObjectMap.Entry<K, V>> implements Object2ObjectMap.FastEntrySet<K, V>
    {
        @Override
        public ObjectIterator<Object2ObjectMap.Entry<K, V>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Object2ObjectMap.Entry<K, V>> fastIterator() {
            return new FastEntryIterator();
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            final K k = (K)e.getKey();
            final V v = (V)e.getValue();
            if (Object2ObjectOpenCustomHashMap.this.strategy.equals(k, null)) {
                return Object2ObjectOpenCustomHashMap.this.containsNullKey && ((Object2ObjectOpenCustomHashMap.this.value[Object2ObjectOpenCustomHashMap.this.n] != null) ? Object2ObjectOpenCustomHashMap.this.value[Object2ObjectOpenCustomHashMap.this.n].equals(v) : (v == null));
            }
            final K[] key = Object2ObjectOpenCustomHashMap.this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(Object2ObjectOpenCustomHashMap.this.strategy.hashCode(k)) & Object2ObjectOpenCustomHashMap.this.mask)]) == null) {
                return false;
            }
            if (Object2ObjectOpenCustomHashMap.this.strategy.equals(k, curr)) {
                return (Object2ObjectOpenCustomHashMap.this.value[pos] == null) ? (v == null) : Object2ObjectOpenCustomHashMap.this.value[pos].equals(v);
            }
            while ((curr = key[pos = (pos + 1 & Object2ObjectOpenCustomHashMap.this.mask)]) != null) {
                if (Object2ObjectOpenCustomHashMap.this.strategy.equals(k, curr)) {
                    return (Object2ObjectOpenCustomHashMap.this.value[pos] == null) ? (v == null) : Object2ObjectOpenCustomHashMap.this.value[pos].equals(v);
                }
            }
            return false;
        }
        
        @Override
        public boolean rem(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            final K k = (K)e.getKey();
            final V v = (V)e.getValue();
            if (Object2ObjectOpenCustomHashMap.this.strategy.equals(k, null)) {
                if (Object2ObjectOpenCustomHashMap.this.containsNullKey) {
                    if (Object2ObjectOpenCustomHashMap.this.value[Object2ObjectOpenCustomHashMap.this.n] == null) {
                        if (v != null) {
                            return false;
                        }
                    }
                    else if (!Object2ObjectOpenCustomHashMap.this.value[Object2ObjectOpenCustomHashMap.this.n].equals(v)) {
                        return false;
                    }
                    Object2ObjectOpenCustomHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            final K[] key = Object2ObjectOpenCustomHashMap.this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(Object2ObjectOpenCustomHashMap.this.strategy.hashCode(k)) & Object2ObjectOpenCustomHashMap.this.mask)]) == null) {
                return false;
            }
            if (Object2ObjectOpenCustomHashMap.this.strategy.equals(curr, k)) {
                if (Object2ObjectOpenCustomHashMap.this.value[pos] == null) {
                    if (v != null) {
                        return false;
                    }
                }
                else if (!Object2ObjectOpenCustomHashMap.this.value[pos].equals(v)) {
                    return false;
                }
                Object2ObjectOpenCustomHashMap.this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & Object2ObjectOpenCustomHashMap.this.mask)]) != null) {
                if (Object2ObjectOpenCustomHashMap.this.strategy.equals(curr, k)) {
                    if (Object2ObjectOpenCustomHashMap.this.value[pos] == null) {
                        if (v != null) {
                            continue;
                        }
                    }
                    else if (!Object2ObjectOpenCustomHashMap.this.value[pos].equals(v)) {
                        continue;
                    }
                    Object2ObjectOpenCustomHashMap.this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public int size() {
            return Object2ObjectOpenCustomHashMap.this.size;
        }
        
        @Override
        public void clear() {
            Object2ObjectOpenCustomHashMap.this.clear();
        }
    }
    
    private final class KeyIterator extends MapIterator implements ObjectIterator<K>
    {
        public KeyIterator() {
        }
        
        @Override
        public K next() {
            return Object2ObjectOpenCustomHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractObjectSet<K>
    {
        @Override
        public ObjectIterator<K> iterator() {
            return new KeyIterator();
        }
        
        @Override
        public int size() {
            return Object2ObjectOpenCustomHashMap.this.size;
        }
        
        @Override
        public boolean contains(final Object k) {
            return Object2ObjectOpenCustomHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean rem(final Object k) {
            final int oldSize = Object2ObjectOpenCustomHashMap.this.size;
            Object2ObjectOpenCustomHashMap.this.remove(k);
            return Object2ObjectOpenCustomHashMap.this.size != oldSize;
        }
        
        @Override
        public void clear() {
            Object2ObjectOpenCustomHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements ObjectIterator<V>
    {
        public ValueIterator() {
        }
        
        @Override
        public V next() {
            return Object2ObjectOpenCustomHashMap.this.value[this.nextEntry()];
        }
    }
}
