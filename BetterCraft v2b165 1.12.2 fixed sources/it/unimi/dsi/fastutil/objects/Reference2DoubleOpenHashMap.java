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
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import java.util.Arrays;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Reference2DoubleOpenHashMap<K> extends AbstractReference2DoubleMap<K> implements Serializable, Cloneable, Hash
{
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient K[] key;
    protected transient double[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int n;
    protected transient int maxFill;
    protected int size;
    protected final float f;
    protected transient Reference2DoubleMap.FastEntrySet<K> entries;
    protected transient ReferenceSet<K> keys;
    protected transient DoubleCollection values;
    
    public Reference2DoubleOpenHashMap(final int expected, final float f) {
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
        this.value = new double[this.n + 1];
    }
    
    public Reference2DoubleOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Reference2DoubleOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Reference2DoubleOpenHashMap(final Map<? extends K, ? extends Double> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Reference2DoubleOpenHashMap(final Map<? extends K, ? extends Double> m) {
        this(m, 0.75f);
    }
    
    public Reference2DoubleOpenHashMap(final Reference2DoubleMap<K> m, final float f) {
        this(m.size(), f);
        this.putAll((Map<? extends K, ? extends Double>)m);
    }
    
    public Reference2DoubleOpenHashMap(final Reference2DoubleMap<K> m) {
        this(m, 0.75f);
    }
    
    public Reference2DoubleOpenHashMap(final K[] k, final double[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Reference2DoubleOpenHashMap(final K[] k, final double[] v) {
        this(k, v, 0.75f);
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
    
    private double removeEntry(final int pos) {
        final double oldValue = this.value[pos];
        --this.size;
        this.shiftKeys(pos);
        if (this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    private double removeNullEntry() {
        this.containsNullKey = false;
        this.key[this.n] = null;
        final double oldValue = this.value[this.n];
        --this.size;
        if (this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends Double> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int insert(final K k, final double v) {
        int pos;
        if (k == null) {
            if (this.containsNullKey) {
                return this.n;
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final K[] key = this.key;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(System.identityHashCode(k)) & this.mask)]) != null) {
                if (curr == k) {
                    return pos;
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                    if (curr == k) {
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
    public double put(final K k, final double v) {
        final int pos = this.insert(k, v);
        if (pos < 0) {
            return this.defRetValue;
        }
        final double oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    @Deprecated
    @Override
    public Double put(final K ok, final Double ov) {
        final double v = ov;
        final int pos = this.insert(ok, v);
        if (pos < 0) {
            return null;
        }
        final double oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    private double addToValue(final int pos, final double incr) {
        final double oldValue = this.value[pos];
        this.value[pos] = oldValue + incr;
        return oldValue;
    }
    
    public double addTo(final K k, final double incr) {
        int pos;
        if (k == null) {
            if (this.containsNullKey) {
                return this.addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        }
        else {
            final K[] key = this.key;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(System.identityHashCode(k)) & this.mask)]) != null) {
                if (curr == k) {
                    return this.addToValue(pos, incr);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                    if (curr == k) {
                        return this.addToValue(pos, incr);
                    }
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = this.defRetValue + incr;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return this.defRetValue;
    }
    
    protected final void shiftKeys(int pos) {
        final K[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            K curr;
            while ((curr = key[pos]) != null) {
                final int slot = HashCommon.mix(System.identityHashCode(curr)) & this.mask;
                Label_0091: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0091;
                        }
                        if (slot > pos) {
                            break Label_0091;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0091;
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
    }
    
    @Override
    public double removeDouble(final Object k) {
        if (k == null) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final K[] key = this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(System.identityHashCode(k)) & this.mask)]) == null) {
                return this.defRetValue;
            }
            if (k == curr) {
                return this.removeEntry(pos);
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (k == curr) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    @Deprecated
    @Override
    public Double remove(final Object ok) {
        final K k = (K)ok;
        if (k == null) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return null;
        }
        else {
            final K[] key = this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(System.identityHashCode(k)) & this.mask)]) == null) {
                return null;
            }
            if (curr == k) {
                return this.removeEntry(pos);
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (curr == k) {
                    return this.removeEntry(pos);
                }
            }
            return null;
        }
    }
    
    @Override
    public double getDouble(final Object k) {
        if (k == null) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(System.identityHashCode(k)) & this.mask)]) == null) {
            return this.defRetValue;
        }
        if (k == curr) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k == curr) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public boolean containsKey(final Object k) {
        if (k == null) {
            return this.containsNullKey;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(System.identityHashCode(k)) & this.mask)]) == null) {
            return false;
        }
        if (k == curr) {
            return true;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k == curr) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final double v) {
        final double[] value = this.value;
        final K[] key = this.key;
        if (this.containsNullKey && value[this.n] == v) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] != null && value[i] == v) {
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
    public Reference2DoubleMap.FastEntrySet<K> reference2DoubleEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public ReferenceSet<K> keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public DoubleCollection values() {
        if (this.values == null) {
            this.values = new AbstractDoubleCollection() {
                @Override
                public DoubleIterator iterator() {
                    return new ValueIterator();
                }
                
                @Override
                public int size() {
                    return Reference2DoubleOpenHashMap.this.size;
                }
                
                @Override
                public boolean contains(final double v) {
                    return Reference2DoubleOpenHashMap.this.containsValue(v);
                }
                
                @Override
                public void clear() {
                    Reference2DoubleOpenHashMap.this.clear();
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
        final double[] value = this.value;
        final int mask = newN - 1;
        final K[] newKey = (K[])new Object[newN + 1];
        final double[] newValue = new double[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == null) {}
            int pos;
            if (newKey[pos = (HashCommon.mix(System.identityHashCode(key[i])) & mask)] != null) {
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
    
    public Reference2DoubleOpenHashMap<K> clone() {
        Reference2DoubleOpenHashMap<K> c;
        try {
            c = (Reference2DoubleOpenHashMap)super.clone();
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
                t = System.identityHashCode(this.key[i]);
            }
            t ^= HashCommon.double2int(this.value[i]);
            h += t;
            ++i;
        }
        if (this.containsNullKey) {
            h += HashCommon.double2int(this.value[this.n]);
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final K[] key = this.key;
        final double[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeObject(key[e]);
            s.writeDouble(value[e]);
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
        final double[] value2 = new double[this.n + 1];
        this.value = value2;
        final double[] value = value2;
        int i = this.size;
        while (i-- != 0) {
            final K k = (K)s.readObject();
            final double v = s.readDouble();
            int pos;
            if (k == null) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = (HashCommon.mix(System.identityHashCode(k)) & this.mask); key[pos] != null; pos = (pos + 1 & this.mask)) {}
            }
            key[pos] = k;
            value[pos] = v;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Reference2DoubleMap.Entry<K>, Map.Entry<K, Double>
    {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        @Override
        public K getKey() {
            return Reference2DoubleOpenHashMap.this.key[this.index];
        }
        
        @Deprecated
        @Override
        public Double getValue() {
            return Reference2DoubleOpenHashMap.this.value[this.index];
        }
        
        @Override
        public double getDoubleValue() {
            return Reference2DoubleOpenHashMap.this.value[this.index];
        }
        
        @Override
        public double setValue(final double v) {
            final double oldValue = Reference2DoubleOpenHashMap.this.value[this.index];
            Reference2DoubleOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Override
        public Double setValue(final Double v) {
            return this.setValue((double)v);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<K, Double> e = (Map.Entry<K, Double>)o;
            return Reference2DoubleOpenHashMap.this.key[this.index] == e.getKey() && Reference2DoubleOpenHashMap.this.value[this.index] == e.getValue();
        }
        
        @Override
        public int hashCode() {
            return System.identityHashCode(Reference2DoubleOpenHashMap.this.key[this.index]) ^ HashCommon.double2int(Reference2DoubleOpenHashMap.this.value[this.index]);
        }
        
        @Override
        public String toString() {
            return Reference2DoubleOpenHashMap.this.key[this.index] + "=>" + Reference2DoubleOpenHashMap.this.value[this.index];
        }
    }
    
    private class MapIterator
    {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        ReferenceArrayList<K> wrapped;
        
        private MapIterator() {
            this.pos = Reference2DoubleOpenHashMap.this.n;
            this.last = -1;
            this.c = Reference2DoubleOpenHashMap.this.size;
            this.mustReturnNullKey = Reference2DoubleOpenHashMap.this.containsNullKey;
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
                return this.last = Reference2DoubleOpenHashMap.this.n;
            }
            final K[] key = Reference2DoubleOpenHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != null) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            K k;
            int p;
            for (k = this.wrapped.get(-this.pos - 1), p = (HashCommon.mix(System.identityHashCode(k)) & Reference2DoubleOpenHashMap.this.mask); k != key[p]; p = (p + 1 & Reference2DoubleOpenHashMap.this.mask)) {}
            return p;
        }
        
        private final void shiftKeys(int pos) {
            final K[] key = Reference2DoubleOpenHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Reference2DoubleOpenHashMap.this.mask);
                K curr;
                while ((curr = key[pos]) != null) {
                    final int slot = HashCommon.mix(System.identityHashCode(curr)) & Reference2DoubleOpenHashMap.this.mask;
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
                        pos = (pos + 1 & Reference2DoubleOpenHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new ReferenceArrayList<K>(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Reference2DoubleOpenHashMap.this.value[last] = Reference2DoubleOpenHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = null;
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Reference2DoubleOpenHashMap.this.n) {
                Reference2DoubleOpenHashMap.this.containsNullKey = false;
                Reference2DoubleOpenHashMap.this.key[Reference2DoubleOpenHashMap.this.n] = null;
            }
            else {
                if (this.pos < 0) {
                    Reference2DoubleOpenHashMap.this.remove(this.wrapped.set(-this.pos - 1, null));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Reference2DoubleOpenHashMap this$0 = Reference2DoubleOpenHashMap.this;
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
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Reference2DoubleMap.Entry<K>>
    {
        private MapEntry entry;
        
        @Override
        public Reference2DoubleMap.Entry<K> next() {
            return this.entry = new MapEntry(this.nextEntry());
        }
        
        @Override
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Reference2DoubleMap.Entry<K>>
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
    
    private final class MapEntrySet extends AbstractObjectSet<Reference2DoubleMap.Entry<K>> implements Reference2DoubleMap.FastEntrySet<K>
    {
        @Override
        public ObjectIterator<Reference2DoubleMap.Entry<K>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Reference2DoubleMap.Entry<K>> fastIterator() {
            return new FastEntryIterator();
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            if (e.getValue() == null || !(e.getValue() instanceof Double)) {
                return false;
            }
            final K k = (K)e.getKey();
            final double v = (double)e.getValue();
            if (k == null) {
                return Reference2DoubleOpenHashMap.this.containsNullKey && Reference2DoubleOpenHashMap.this.value[Reference2DoubleOpenHashMap.this.n] == v;
            }
            final K[] key = Reference2DoubleOpenHashMap.this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(System.identityHashCode(k)) & Reference2DoubleOpenHashMap.this.mask)]) == null) {
                return false;
            }
            if (k == curr) {
                return Reference2DoubleOpenHashMap.this.value[pos] == v;
            }
            while ((curr = key[pos = (pos + 1 & Reference2DoubleOpenHashMap.this.mask)]) != null) {
                if (k == curr) {
                    return Reference2DoubleOpenHashMap.this.value[pos] == v;
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
            if (e.getValue() == null || !(e.getValue() instanceof Double)) {
                return false;
            }
            final K k = (K)e.getKey();
            final double v = (double)e.getValue();
            if (k == null) {
                if (Reference2DoubleOpenHashMap.this.containsNullKey && Reference2DoubleOpenHashMap.this.value[Reference2DoubleOpenHashMap.this.n] == v) {
                    Reference2DoubleOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final K[] key = Reference2DoubleOpenHashMap.this.key;
                int pos;
                K curr;
                if ((curr = key[pos = (HashCommon.mix(System.identityHashCode(k)) & Reference2DoubleOpenHashMap.this.mask)]) == null) {
                    return false;
                }
                if (curr != k) {
                    while ((curr = key[pos = (pos + 1 & Reference2DoubleOpenHashMap.this.mask)]) != null) {
                        if (curr == k && Reference2DoubleOpenHashMap.this.value[pos] == v) {
                            Reference2DoubleOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Reference2DoubleOpenHashMap.this.value[pos] == v) {
                    Reference2DoubleOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        @Override
        public int size() {
            return Reference2DoubleOpenHashMap.this.size;
        }
        
        @Override
        public void clear() {
            Reference2DoubleOpenHashMap.this.clear();
        }
    }
    
    private final class KeyIterator extends MapIterator implements ObjectIterator<K>
    {
        public KeyIterator() {
        }
        
        @Override
        public K next() {
            return Reference2DoubleOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractReferenceSet<K>
    {
        @Override
        public ObjectIterator<K> iterator() {
            return new KeyIterator();
        }
        
        @Override
        public int size() {
            return Reference2DoubleOpenHashMap.this.size;
        }
        
        @Override
        public boolean contains(final Object k) {
            return Reference2DoubleOpenHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean rem(final Object k) {
            final int oldSize = Reference2DoubleOpenHashMap.this.size;
            Reference2DoubleOpenHashMap.this.remove(k);
            return Reference2DoubleOpenHashMap.this.size != oldSize;
        }
        
        @Override
        public void clear() {
            Reference2DoubleOpenHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements DoubleIterator
    {
        public ValueIterator() {
        }
        
        @Override
        public double nextDouble() {
            return Reference2DoubleOpenHashMap.this.value[this.nextEntry()];
        }
        
        @Deprecated
        @Override
        public Double next() {
            return Reference2DoubleOpenHashMap.this.value[this.nextEntry()];
        }
    }
}
