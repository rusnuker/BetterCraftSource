// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import java.util.Arrays;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Double2ObjectOpenHashMap<V> extends AbstractDouble2ObjectMap<V> implements Serializable, Cloneable, Hash
{
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient double[] key;
    protected transient V[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int n;
    protected transient int maxFill;
    protected int size;
    protected final float f;
    protected transient Double2ObjectMap.FastEntrySet<V> entries;
    protected transient DoubleSet keys;
    protected transient ObjectCollection<V> values;
    
    public Double2ObjectOpenHashMap(final int expected, final float f) {
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
        this.key = new double[this.n + 1];
        this.value = (V[])new Object[this.n + 1];
    }
    
    public Double2ObjectOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Double2ObjectOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Double2ObjectOpenHashMap(final Map<? extends Double, ? extends V> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Double2ObjectOpenHashMap(final Map<? extends Double, ? extends V> m) {
        this(m, 0.75f);
    }
    
    public Double2ObjectOpenHashMap(final Double2ObjectMap<V> m, final float f) {
        this(m.size(), f);
        this.putAll((Map<? extends Double, ? extends V>)m);
    }
    
    public Double2ObjectOpenHashMap(final Double2ObjectMap<V> m) {
        this(m, 0.75f);
    }
    
    public Double2ObjectOpenHashMap(final double[] k, final V[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Double2ObjectOpenHashMap(final double[] k, final V[] v) {
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
        final V oldValue = this.value[this.n];
        this.value[this.n] = null;
        --this.size;
        if (this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends Double, ? extends V> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int insert(final double k, final V v) {
        int pos;
        if (Double.doubleToLongBits(k) == 0L) {
            if (this.containsNullKey) {
                return this.n;
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final double[] key = this.key;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) != 0L) {
                if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
                    return pos;
                }
                while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                    if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
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
    public V put(final double k, final V v) {
        final int pos = this.insert(k, v);
        if (pos < 0) {
            return this.defRetValue;
        }
        final V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    @Deprecated
    @Override
    public V put(final Double ok, final V ov) {
        final V v = ov;
        final int pos = this.insert(ok, v);
        if (pos < 0) {
            return this.defRetValue;
        }
        final V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    protected final void shiftKeys(int pos) {
        final double[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            double curr;
            while (Double.doubleToLongBits(curr = key[pos]) != 0L) {
                final int slot = (int)HashCommon.mix(Double.doubleToRawLongBits(curr)) & this.mask;
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
        key[last] = 0.0;
        this.value[last] = null;
    }
    
    @Override
    public V remove(final double k) {
        if (Double.doubleToLongBits(k) == 0L) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final double[] key = this.key;
            int pos;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) == 0L) {
                return this.defRetValue;
            }
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                return this.removeEntry(pos);
            }
            while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    @Deprecated
    @Override
    public V remove(final Object ok) {
        final double k = (double)ok;
        if (Double.doubleToLongBits(k) == 0L) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final double[] key = this.key;
            int pos;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) == 0L) {
                return this.defRetValue;
            }
            if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
                return this.removeEntry(pos);
            }
            while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
                if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    @Deprecated
    public V get(final Double ok) {
        if (ok == null) {
            return null;
        }
        final double k = ok;
        if (Double.doubleToLongBits(k) == 0L) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final double[] key = this.key;
        int pos;
        double curr;
        if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) == 0L) {
            return this.defRetValue;
        }
        if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
            return this.value[pos];
        }
        while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public V get(final double k) {
        if (Double.doubleToLongBits(k) == 0L) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final double[] key = this.key;
        int pos;
        double curr;
        if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) == 0L) {
            return this.defRetValue;
        }
        if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
            return this.value[pos];
        }
        while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public boolean containsKey(final double k) {
        if (Double.doubleToLongBits(k) == 0L) {
            return this.containsNullKey;
        }
        final double[] key = this.key;
        int pos;
        double curr;
        if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask)]) == 0L) {
            return false;
        }
        if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
            return true;
        }
        while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & this.mask)]) != 0L) {
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final Object v) {
        final V[] value = this.value;
        final double[] key = this.key;
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
            if (Double.doubleToLongBits(key[i]) != 0L) {
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
        Arrays.fill(this.key, 0.0);
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
    public Double2ObjectMap.FastEntrySet<V> double2ObjectEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public DoubleSet keySet() {
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
                    return Double2ObjectOpenHashMap.this.size;
                }
                
                @Override
                public boolean contains(final Object v) {
                    return Double2ObjectOpenHashMap.this.containsValue(v);
                }
                
                @Override
                public void clear() {
                    Double2ObjectOpenHashMap.this.clear();
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
        final double[] key = this.key;
        final V[] value = this.value;
        final int mask = newN - 1;
        final double[] newKey = new double[newN + 1];
        final V[] newValue = (V[])new Object[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (Double.doubleToLongBits(key[--i]) == 0L) {}
            int pos;
            if (Double.doubleToLongBits(newKey[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(key[i])) & mask)]) != 0L) {
                while (Double.doubleToLongBits(newKey[pos = (pos + 1 & mask)]) != 0L) {}
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
    
    public Double2ObjectOpenHashMap<V> clone() {
        Double2ObjectOpenHashMap<V> c;
        try {
            c = (Double2ObjectOpenHashMap)super.clone();
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
            while (Double.doubleToLongBits(this.key[i]) == 0L) {
                ++i;
            }
            t = HashCommon.double2int(this.key[i]);
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
        final double[] key = this.key;
        final V[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeDouble(key[e]);
            s.writeObject(value[e]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        final double[] key2 = new double[this.n + 1];
        this.key = key2;
        final double[] key = key2;
        final Object[] value2 = new Object[this.n + 1];
        this.value = (V[])value2;
        final V[] value = (V[])value2;
        int i = this.size;
        while (i-- != 0) {
            final double k = s.readDouble();
            final V v = (V)s.readObject();
            int pos;
            if (Double.doubleToLongBits(k) == 0L) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask); Double.doubleToLongBits(key[pos]) != 0L; pos = (pos + 1 & this.mask)) {}
            }
            key[pos] = k;
            value[pos] = v;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Double2ObjectMap.Entry<V>, Map.Entry<Double, V>
    {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        @Deprecated
        @Override
        public Double getKey() {
            return Double2ObjectOpenHashMap.this.key[this.index];
        }
        
        @Override
        public double getDoubleKey() {
            return Double2ObjectOpenHashMap.this.key[this.index];
        }
        
        @Override
        public V getValue() {
            return Double2ObjectOpenHashMap.this.value[this.index];
        }
        
        @Override
        public V setValue(final V v) {
            final V oldValue = Double2ObjectOpenHashMap.this.value[this.index];
            Double2ObjectOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Double, V> e = (Map.Entry<Double, V>)o;
            return Double.doubleToLongBits(Double2ObjectOpenHashMap.this.key[this.index]) == Double.doubleToLongBits(e.getKey()) && ((Double2ObjectOpenHashMap.this.value[this.index] != null) ? Double2ObjectOpenHashMap.this.value[this.index].equals(e.getValue()) : (e.getValue() == null));
        }
        
        @Override
        public int hashCode() {
            return HashCommon.double2int(Double2ObjectOpenHashMap.this.key[this.index]) ^ ((Double2ObjectOpenHashMap.this.value[this.index] == null) ? 0 : Double2ObjectOpenHashMap.this.value[this.index].hashCode());
        }
        
        @Override
        public String toString() {
            return Double2ObjectOpenHashMap.this.key[this.index] + "=>" + Double2ObjectOpenHashMap.this.value[this.index];
        }
    }
    
    private class MapIterator
    {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        DoubleArrayList wrapped;
        
        private MapIterator() {
            this.pos = Double2ObjectOpenHashMap.this.n;
            this.last = -1;
            this.c = Double2ObjectOpenHashMap.this.size;
            this.mustReturnNullKey = Double2ObjectOpenHashMap.this.containsNullKey;
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
                return this.last = Double2ObjectOpenHashMap.this.n;
            }
            final double[] key = Double2ObjectOpenHashMap.this.key;
            while (--this.pos >= 0) {
                if (Double.doubleToLongBits(key[this.pos]) != 0L) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            double k;
            int p;
            for (k = this.wrapped.getDouble(-this.pos - 1), p = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & Double2ObjectOpenHashMap.this.mask); Double.doubleToLongBits(k) != Double.doubleToLongBits(key[p]); p = (p + 1 & Double2ObjectOpenHashMap.this.mask)) {}
            return p;
        }
        
        private final void shiftKeys(int pos) {
            final double[] key = Double2ObjectOpenHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Double2ObjectOpenHashMap.this.mask);
                double curr;
                while (Double.doubleToLongBits(curr = key[pos]) != 0L) {
                    final int slot = (int)HashCommon.mix(Double.doubleToRawLongBits(curr)) & Double2ObjectOpenHashMap.this.mask;
                    Label_0118: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0118;
                            }
                            if (slot > pos) {
                                break Label_0118;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0118;
                        }
                        pos = (pos + 1 & Double2ObjectOpenHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new DoubleArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Double2ObjectOpenHashMap.this.value[last] = Double2ObjectOpenHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = 0.0;
            Double2ObjectOpenHashMap.this.value[last] = null;
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Double2ObjectOpenHashMap.this.n) {
                Double2ObjectOpenHashMap.this.containsNullKey = false;
                Double2ObjectOpenHashMap.this.value[Double2ObjectOpenHashMap.this.n] = null;
            }
            else {
                if (this.pos < 0) {
                    Double2ObjectOpenHashMap.this.remove(this.wrapped.getDouble(-this.pos - 1));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Double2ObjectOpenHashMap this$0 = Double2ObjectOpenHashMap.this;
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
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Double2ObjectMap.Entry<V>>
    {
        private MapEntry entry;
        
        @Override
        public Double2ObjectMap.Entry<V> next() {
            return this.entry = new MapEntry(this.nextEntry());
        }
        
        @Override
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Double2ObjectMap.Entry<V>>
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
    
    private final class MapEntrySet extends AbstractObjectSet<Double2ObjectMap.Entry<V>> implements Double2ObjectMap.FastEntrySet<V>
    {
        @Override
        public ObjectIterator<Double2ObjectMap.Entry<V>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Double2ObjectMap.Entry<V>> fastIterator() {
            return new FastEntryIterator();
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            if (e.getKey() == null || !(e.getKey() instanceof Double)) {
                return false;
            }
            final double k = (double)e.getKey();
            final V v = (V)e.getValue();
            if (Double.doubleToLongBits(k) == 0L) {
                return Double2ObjectOpenHashMap.this.containsNullKey && ((Double2ObjectOpenHashMap.this.value[Double2ObjectOpenHashMap.this.n] != null) ? Double2ObjectOpenHashMap.this.value[Double2ObjectOpenHashMap.this.n].equals(v) : (v == null));
            }
            final double[] key = Double2ObjectOpenHashMap.this.key;
            int pos;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & Double2ObjectOpenHashMap.this.mask)]) == 0L) {
                return false;
            }
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                return (Double2ObjectOpenHashMap.this.value[pos] == null) ? (v == null) : Double2ObjectOpenHashMap.this.value[pos].equals(v);
            }
            while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & Double2ObjectOpenHashMap.this.mask)]) != 0L) {
                if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
                    return (Double2ObjectOpenHashMap.this.value[pos] == null) ? (v == null) : Double2ObjectOpenHashMap.this.value[pos].equals(v);
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
            if (e.getKey() == null || !(e.getKey() instanceof Double)) {
                return false;
            }
            final double k = (double)e.getKey();
            final V v = (V)e.getValue();
            if (Double.doubleToLongBits(k) == 0L) {
                if (Double2ObjectOpenHashMap.this.containsNullKey) {
                    if (Double2ObjectOpenHashMap.this.value[Double2ObjectOpenHashMap.this.n] == null) {
                        if (v != null) {
                            return false;
                        }
                    }
                    else if (!Double2ObjectOpenHashMap.this.value[Double2ObjectOpenHashMap.this.n].equals(v)) {
                        return false;
                    }
                    Double2ObjectOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            final double[] key = Double2ObjectOpenHashMap.this.key;
            int pos;
            double curr;
            if (Double.doubleToLongBits(curr = key[pos = ((int)HashCommon.mix(Double.doubleToRawLongBits(k)) & Double2ObjectOpenHashMap.this.mask)]) == 0L) {
                return false;
            }
            if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
                if (Double2ObjectOpenHashMap.this.value[pos] == null) {
                    if (v != null) {
                        return false;
                    }
                }
                else if (!Double2ObjectOpenHashMap.this.value[pos].equals(v)) {
                    return false;
                }
                Double2ObjectOpenHashMap.this.removeEntry(pos);
                return true;
            }
            while (Double.doubleToLongBits(curr = key[pos = (pos + 1 & Double2ObjectOpenHashMap.this.mask)]) != 0L) {
                if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
                    if (Double2ObjectOpenHashMap.this.value[pos] == null) {
                        if (v != null) {
                            continue;
                        }
                    }
                    else if (!Double2ObjectOpenHashMap.this.value[pos].equals(v)) {
                        continue;
                    }
                    Double2ObjectOpenHashMap.this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public int size() {
            return Double2ObjectOpenHashMap.this.size;
        }
        
        @Override
        public void clear() {
            Double2ObjectOpenHashMap.this.clear();
        }
    }
    
    private final class KeyIterator extends MapIterator implements DoubleIterator
    {
        public KeyIterator() {
        }
        
        @Override
        public double nextDouble() {
            return Double2ObjectOpenHashMap.this.key[this.nextEntry()];
        }
        
        @Override
        public Double next() {
            return Double2ObjectOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractDoubleSet
    {
        @Override
        public DoubleIterator iterator() {
            return new KeyIterator();
        }
        
        @Override
        public int size() {
            return Double2ObjectOpenHashMap.this.size;
        }
        
        @Override
        public boolean contains(final double k) {
            return Double2ObjectOpenHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean rem(final double k) {
            final int oldSize = Double2ObjectOpenHashMap.this.size;
            Double2ObjectOpenHashMap.this.remove(k);
            return Double2ObjectOpenHashMap.this.size != oldSize;
        }
        
        @Override
        public void clear() {
            Double2ObjectOpenHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements ObjectIterator<V>
    {
        public ValueIterator() {
        }
        
        @Override
        public V next() {
            return Double2ObjectOpenHashMap.this.value[this.nextEntry()];
        }
    }
}
