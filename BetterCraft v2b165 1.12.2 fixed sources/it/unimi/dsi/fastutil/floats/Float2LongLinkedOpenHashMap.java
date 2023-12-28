// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.longs.LongListIterator;
import java.util.SortedSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Float2LongLinkedOpenHashMap extends AbstractFloat2LongSortedMap implements Serializable, Cloneable, Hash
{
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient float[] key;
    protected transient long[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int first;
    protected transient int last;
    protected transient long[] link;
    protected transient int n;
    protected transient int maxFill;
    protected int size;
    protected final float f;
    protected transient Float2LongSortedMap.FastSortedEntrySet entries;
    protected transient FloatSortedSet keys;
    protected transient LongCollection values;
    
    public Float2LongLinkedOpenHashMap(final int expected, final float f) {
        this.first = -1;
        this.last = -1;
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
        this.key = new float[this.n + 1];
        this.value = new long[this.n + 1];
        this.link = new long[this.n + 1];
    }
    
    public Float2LongLinkedOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Float2LongLinkedOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Float2LongLinkedOpenHashMap(final Map<? extends Float, ? extends Long> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Float2LongLinkedOpenHashMap(final Map<? extends Float, ? extends Long> m) {
        this(m, 0.75f);
    }
    
    public Float2LongLinkedOpenHashMap(final Float2LongMap m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Float2LongLinkedOpenHashMap(final Float2LongMap m) {
        this(m, 0.75f);
    }
    
    public Float2LongLinkedOpenHashMap(final float[] k, final long[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Float2LongLinkedOpenHashMap(final float[] k, final long[] v) {
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
    
    private long removeEntry(final int pos) {
        final long oldValue = this.value[pos];
        --this.size;
        this.fixPointers(pos);
        this.shiftKeys(pos);
        if (this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    private long removeNullEntry() {
        this.containsNullKey = false;
        final long oldValue = this.value[this.n];
        --this.size;
        this.fixPointers(this.n);
        if (this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends Float, ? extends Long> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int insert(final float k, final long v) {
        int pos;
        if (Float.floatToIntBits(k) == 0) {
            if (this.containsNullKey) {
                return this.n;
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final float[] key = this.key;
            float curr;
            if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(HashCommon.float2int(k)) & this.mask)]) != 0) {
                if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
                    return pos;
                }
                while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                    if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
                        return pos;
                    }
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size == 0) {
            final int n = pos;
            this.last = n;
            this.first = n;
            this.link[pos] = -1L;
        }
        else {
            final long[] link = this.link;
            final int last = this.last;
            link[last] ^= ((this.link[this.last] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            this.link[pos] = (((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL);
            this.last = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return -1;
    }
    
    @Override
    public long put(final float k, final long v) {
        final int pos = this.insert(k, v);
        if (pos < 0) {
            return this.defRetValue;
        }
        final long oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    @Deprecated
    @Override
    public Long put(final Float ok, final Long ov) {
        final long v = ov;
        final int pos = this.insert(ok, v);
        if (pos < 0) {
            return null;
        }
        final long oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    private long addToValue(final int pos, final long incr) {
        final long oldValue = this.value[pos];
        this.value[pos] = oldValue + incr;
        return oldValue;
    }
    
    public long addTo(final float k, final long incr) {
        int pos;
        if (Float.floatToIntBits(k) == 0) {
            if (this.containsNullKey) {
                return this.addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        }
        else {
            final float[] key = this.key;
            float curr;
            if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(HashCommon.float2int(k)) & this.mask)]) != 0) {
                if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
                    return this.addToValue(pos, incr);
                }
                while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                    if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
                        return this.addToValue(pos, incr);
                    }
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = this.defRetValue + incr;
        if (this.size == 0) {
            final int n = pos;
            this.last = n;
            this.first = n;
            this.link[pos] = -1L;
        }
        else {
            final long[] link = this.link;
            final int last = this.last;
            link[last] ^= ((this.link[this.last] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            this.link[pos] = (((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL);
            this.last = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return this.defRetValue;
    }
    
    protected final void shiftKeys(int pos) {
        final float[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            float curr;
            while (Float.floatToIntBits(curr = key[pos]) != 0) {
                final int slot = HashCommon.mix(HashCommon.float2int(curr)) & this.mask;
                Label_0093: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0093;
                        }
                        if (slot > pos) {
                            break Label_0093;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0093;
                    }
                    pos = (pos + 1 & this.mask);
                    continue;
                }
                key[last] = curr;
                this.value[last] = this.value[pos];
                this.fixPointers(pos, last);
                continue Label_0006;
            }
            break;
        }
        key[last] = 0.0f;
    }
    
    @Override
    public long remove(final float k) {
        if (Float.floatToIntBits(k) == 0) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final float[] key = this.key;
            int pos;
            float curr;
            if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(HashCommon.float2int(k)) & this.mask)]) == 0) {
                return this.defRetValue;
            }
            if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
                return this.removeEntry(pos);
            }
            while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    @Deprecated
    @Override
    public Long remove(final Object ok) {
        final float k = (float)ok;
        if (Float.floatToIntBits(k) == 0) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return null;
        }
        else {
            final float[] key = this.key;
            int pos;
            float curr;
            if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(HashCommon.float2int(k)) & this.mask)]) == 0) {
                return null;
            }
            if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
                return this.removeEntry(pos);
            }
            while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
                    return this.removeEntry(pos);
                }
            }
            return null;
        }
    }
    
    private long setValue(final int pos, final long v) {
        final long oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public long removeFirstLong() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final int pos = this.first;
        this.first = (int)this.link[pos];
        if (0 <= this.first) {
            final long[] link = this.link;
            final int first = this.first;
            link[first] |= 0xFFFFFFFF00000000L;
        }
        --this.size;
        final long v = this.value[pos];
        if (pos == this.n) {
            this.containsNullKey = false;
        }
        else {
            this.shiftKeys(pos);
        }
        if (this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return v;
    }
    
    public long removeLastLong() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final int pos = this.last;
        this.last = (int)(this.link[pos] >>> 32);
        if (0 <= this.last) {
            final long[] link = this.link;
            final int last = this.last;
            link[last] |= 0xFFFFFFFFL;
        }
        --this.size;
        final long v = this.value[pos];
        if (pos == this.n) {
            this.containsNullKey = false;
        }
        else {
            this.shiftKeys(pos);
        }
        if (this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return v;
    }
    
    private void moveIndexToFirst(final int i) {
        if (this.size == 1 || this.first == i) {
            return;
        }
        if (this.last == i) {
            this.last = (int)(this.link[i] >>> 32);
            final long[] link = this.link;
            final int last = this.last;
            link[last] |= 0xFFFFFFFFL;
        }
        else {
            final long linki = this.link[i];
            final int prev = (int)(linki >>> 32);
            final int next = (int)linki;
            final long[] link2 = this.link;
            final int n = prev;
            link2[n] ^= ((this.link[prev] ^ (linki & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            final long[] link3 = this.link;
            final int n2 = next;
            link3[n2] ^= ((this.link[next] ^ (linki & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
        }
        final long[] link4 = this.link;
        final int first = this.first;
        link4[first] ^= ((this.link[this.first] ^ ((long)i & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
        this.link[i] = (0xFFFFFFFF00000000L | ((long)this.first & 0xFFFFFFFFL));
        this.first = i;
    }
    
    private void moveIndexToLast(final int i) {
        if (this.size == 1 || this.last == i) {
            return;
        }
        if (this.first == i) {
            this.first = (int)this.link[i];
            final long[] link = this.link;
            final int first = this.first;
            link[first] |= 0xFFFFFFFF00000000L;
        }
        else {
            final long linki = this.link[i];
            final int prev = (int)(linki >>> 32);
            final int next = (int)linki;
            final long[] link2 = this.link;
            final int n = prev;
            link2[n] ^= ((this.link[prev] ^ (linki & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            final long[] link3 = this.link;
            final int n2 = next;
            link3[n2] ^= ((this.link[next] ^ (linki & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
        }
        final long[] link4 = this.link;
        final int last = this.last;
        link4[last] ^= ((this.link[this.last] ^ ((long)i & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
        this.link[i] = (((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL);
        this.last = i;
    }
    
    public long getAndMoveToFirst(final float k) {
        if (Float.floatToIntBits(k) == 0) {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.value[this.n];
            }
            return this.defRetValue;
        }
        else {
            final float[] key = this.key;
            int pos;
            float curr;
            if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(HashCommon.float2int(k)) & this.mask)]) == 0) {
                return this.defRetValue;
            }
            if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
                this.moveIndexToFirst(pos);
                return this.value[pos];
            }
            while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
                    this.moveIndexToFirst(pos);
                    return this.value[pos];
                }
            }
            return this.defRetValue;
        }
    }
    
    public long getAndMoveToLast(final float k) {
        if (Float.floatToIntBits(k) == 0) {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.value[this.n];
            }
            return this.defRetValue;
        }
        else {
            final float[] key = this.key;
            int pos;
            float curr;
            if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(HashCommon.float2int(k)) & this.mask)]) == 0) {
                return this.defRetValue;
            }
            if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
                this.moveIndexToLast(pos);
                return this.value[pos];
            }
            while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
                    this.moveIndexToLast(pos);
                    return this.value[pos];
                }
            }
            return this.defRetValue;
        }
    }
    
    public long putAndMoveToFirst(final float k, final long v) {
        int pos;
        if (Float.floatToIntBits(k) == 0) {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final float[] key = this.key;
            float curr;
            if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(HashCommon.float2int(k)) & this.mask)]) != 0) {
                if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
                    this.moveIndexToFirst(pos);
                    return this.setValue(pos, v);
                }
                while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                    if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
                        this.moveIndexToFirst(pos);
                        return this.setValue(pos, v);
                    }
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size == 0) {
            final int n = pos;
            this.last = n;
            this.first = n;
            this.link[pos] = -1L;
        }
        else {
            final long[] link = this.link;
            final int first = this.first;
            link[first] ^= ((this.link[this.first] ^ ((long)pos & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            this.link[pos] = (0xFFFFFFFF00000000L | ((long)this.first & 0xFFFFFFFFL));
            this.first = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size, this.f));
        }
        return this.defRetValue;
    }
    
    public long putAndMoveToLast(final float k, final long v) {
        int pos;
        if (Float.floatToIntBits(k) == 0) {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        }
        else {
            final float[] key = this.key;
            float curr;
            if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(HashCommon.float2int(k)) & this.mask)]) != 0) {
                if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
                    this.moveIndexToLast(pos);
                    return this.setValue(pos, v);
                }
                while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                    if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
                        this.moveIndexToLast(pos);
                        return this.setValue(pos, v);
                    }
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size == 0) {
            final int n = pos;
            this.last = n;
            this.first = n;
            this.link[pos] = -1L;
        }
        else {
            final long[] link = this.link;
            final int last = this.last;
            link[last] ^= ((this.link[this.last] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            this.link[pos] = (((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL);
            this.last = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size, this.f));
        }
        return this.defRetValue;
    }
    
    @Deprecated
    public Long get(final Float ok) {
        if (ok == null) {
            return null;
        }
        final float k = ok;
        if (Float.floatToIntBits(k) == 0) {
            return this.containsNullKey ? Long.valueOf(this.value[this.n]) : null;
        }
        final float[] key = this.key;
        int pos;
        float curr;
        if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(HashCommon.float2int(k)) & this.mask)]) == 0) {
            return null;
        }
        if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
            return this.value[pos];
        }
        while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
                return this.value[pos];
            }
        }
        return null;
    }
    
    @Override
    public long get(final float k) {
        if (Float.floatToIntBits(k) == 0) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final float[] key = this.key;
        int pos;
        float curr;
        if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(HashCommon.float2int(k)) & this.mask)]) == 0) {
            return this.defRetValue;
        }
        if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
            return this.value[pos];
        }
        while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public boolean containsKey(final float k) {
        if (Float.floatToIntBits(k) == 0) {
            return this.containsNullKey;
        }
        final float[] key = this.key;
        int pos;
        float curr;
        if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(HashCommon.float2int(k)) & this.mask)]) == 0) {
            return false;
        }
        if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
            return true;
        }
        while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final long v) {
        final long[] value = this.value;
        final float[] key = this.key;
        if (this.containsNullKey && value[this.n] == v) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (Float.floatToIntBits(key[i]) != 0 && value[i] == v) {
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
        Arrays.fill(this.key, 0.0f);
        final int n = -1;
        this.last = n;
        this.first = n;
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
    
    protected void fixPointers(final int i) {
        if (this.size == 0) {
            final int n = -1;
            this.last = n;
            this.first = n;
            return;
        }
        if (this.first == i) {
            this.first = (int)this.link[i];
            if (0 <= this.first) {
                final long[] link = this.link;
                final int first = this.first;
                link[first] |= 0xFFFFFFFF00000000L;
            }
            return;
        }
        if (this.last == i) {
            this.last = (int)(this.link[i] >>> 32);
            if (0 <= this.last) {
                final long[] link2 = this.link;
                final int last = this.last;
                link2[last] |= 0xFFFFFFFFL;
            }
            return;
        }
        final long linki = this.link[i];
        final int prev = (int)(linki >>> 32);
        final int next = (int)linki;
        final long[] link3 = this.link;
        final int n2 = prev;
        link3[n2] ^= ((this.link[prev] ^ (linki & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
        final long[] link4 = this.link;
        final int n3 = next;
        link4[n3] ^= ((this.link[next] ^ (linki & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
    }
    
    protected void fixPointers(final int s, final int d) {
        if (this.size == 1) {
            this.last = d;
            this.first = d;
            this.link[d] = -1L;
            return;
        }
        if (this.first == s) {
            this.first = d;
            final long[] link = this.link;
            final int n = (int)this.link[s];
            link[n] ^= ((this.link[(int)this.link[s]] ^ ((long)d & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            this.link[d] = this.link[s];
            return;
        }
        if (this.last == s) {
            this.last = d;
            final long[] link2 = this.link;
            final int n2 = (int)(this.link[s] >>> 32);
            link2[n2] ^= ((this.link[(int)(this.link[s] >>> 32)] ^ ((long)d & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            this.link[d] = this.link[s];
            return;
        }
        final long links = this.link[s];
        final int prev = (int)(links >>> 32);
        final int next = (int)links;
        final long[] link3 = this.link;
        final int n3 = prev;
        link3[n3] ^= ((this.link[prev] ^ ((long)d & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
        final long[] link4 = this.link;
        final int n4 = next;
        link4[n4] ^= ((this.link[next] ^ ((long)d & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
        this.link[d] = links;
    }
    
    @Override
    public float firstFloatKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.first];
    }
    
    @Override
    public float lastFloatKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.last];
    }
    
    @Override
    public FloatComparator comparator() {
        return null;
    }
    
    @Override
    public Float2LongSortedMap tailMap(final float from) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Float2LongSortedMap headMap(final float to) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Float2LongSortedMap subMap(final float from, final float to) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Float2LongSortedMap.FastSortedEntrySet float2LongEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public FloatSortedSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public LongCollection values() {
        if (this.values == null) {
            this.values = new AbstractLongCollection() {
                @Override
                public LongIterator iterator() {
                    return new ValueIterator();
                }
                
                @Override
                public int size() {
                    return Float2LongLinkedOpenHashMap.this.size;
                }
                
                @Override
                public boolean contains(final long v) {
                    return Float2LongLinkedOpenHashMap.this.containsValue(v);
                }
                
                @Override
                public void clear() {
                    Float2LongLinkedOpenHashMap.this.clear();
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
        final float[] key = this.key;
        final long[] value = this.value;
        final int mask = newN - 1;
        final float[] newKey = new float[newN + 1];
        final long[] newValue = new long[newN + 1];
        int i = this.first;
        int prev = -1;
        int newPrev = -1;
        final long[] link = this.link;
        final long[] newLink = new long[newN + 1];
        this.first = -1;
        int j = this.size;
        while (j-- != 0) {
            int pos;
            if (Float.floatToIntBits(key[i]) == 0) {
                pos = newN;
            }
            else {
                for (pos = (HashCommon.mix(HashCommon.float2int(key[i])) & mask); Float.floatToIntBits(newKey[pos]) != 0; pos = (pos + 1 & mask)) {}
            }
            newKey[pos] = key[i];
            newValue[pos] = value[i];
            if (prev != -1) {
                final long[] array = newLink;
                final int n = newPrev;
                array[n] ^= ((newLink[newPrev] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
                final long[] array2 = newLink;
                final int n2 = pos;
                array2[n2] ^= ((newLink[pos] ^ ((long)newPrev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
                newPrev = pos;
            }
            else {
                final int first = pos;
                this.first = first;
                newPrev = first;
                newLink[pos] = -1L;
            }
            final int t = i;
            i = (int)link[i];
            prev = t;
        }
        this.link = newLink;
        if ((this.last = newPrev) != -1) {
            final long[] array3 = newLink;
            final int n3 = newPrev;
            array3[n3] |= 0xFFFFFFFFL;
        }
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
        this.value = newValue;
    }
    
    public Float2LongLinkedOpenHashMap clone() {
        Float2LongLinkedOpenHashMap c;
        try {
            c = (Float2LongLinkedOpenHashMap)super.clone();
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
        c.link = this.link.clone();
        return c;
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int j = this.realSize();
        int i = 0;
        int t = 0;
        while (j-- != 0) {
            while (Float.floatToIntBits(this.key[i]) == 0) {
                ++i;
            }
            t = HashCommon.float2int(this.key[i]);
            t ^= HashCommon.long2int(this.value[i]);
            h += t;
            ++i;
        }
        if (this.containsNullKey) {
            h += HashCommon.long2int(this.value[this.n]);
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final float[] key = this.key;
        final long[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeFloat(key[e]);
            s.writeLong(value[e]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        final float[] key2 = new float[this.n + 1];
        this.key = key2;
        final float[] key = key2;
        final long[] value2 = new long[this.n + 1];
        this.value = value2;
        final long[] value = value2;
        final long[] link2 = new long[this.n + 1];
        this.link = link2;
        final long[] link = link2;
        int prev = -1;
        final int n = -1;
        this.last = n;
        this.first = n;
        int i = this.size;
        while (i-- != 0) {
            final float k = s.readFloat();
            final long v = s.readLong();
            int pos;
            if (Float.floatToIntBits(k) == 0) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = (HashCommon.mix(HashCommon.float2int(k)) & this.mask); Float.floatToIntBits(key[pos]) != 0; pos = (pos + 1 & this.mask)) {}
            }
            key[pos] = k;
            value[pos] = v;
            if (this.first != -1) {
                final long[] array = link;
                final int n2 = prev;
                array[n2] ^= ((link[prev] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
                final long[] array2 = link;
                final int n3 = pos;
                array2[n3] ^= ((link[pos] ^ ((long)prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
                prev = pos;
            }
            else {
                final int first = pos;
                this.first = first;
                prev = first;
                final long[] array3 = link;
                final int n4 = pos;
                array3[n4] |= 0xFFFFFFFF00000000L;
            }
        }
        if ((this.last = prev) != -1) {
            final long[] array4 = link;
            final int n5 = prev;
            array4[n5] |= 0xFFFFFFFFL;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Float2LongMap.Entry, Map.Entry<Float, Long>
    {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        @Deprecated
        @Override
        public Float getKey() {
            return Float2LongLinkedOpenHashMap.this.key[this.index];
        }
        
        @Override
        public float getFloatKey() {
            return Float2LongLinkedOpenHashMap.this.key[this.index];
        }
        
        @Deprecated
        @Override
        public Long getValue() {
            return Float2LongLinkedOpenHashMap.this.value[this.index];
        }
        
        @Override
        public long getLongValue() {
            return Float2LongLinkedOpenHashMap.this.value[this.index];
        }
        
        @Override
        public long setValue(final long v) {
            final long oldValue = Float2LongLinkedOpenHashMap.this.value[this.index];
            Float2LongLinkedOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Override
        public Long setValue(final Long v) {
            return this.setValue((long)v);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Float, Long> e = (Map.Entry<Float, Long>)o;
            return Float.floatToIntBits(Float2LongLinkedOpenHashMap.this.key[this.index]) == Float.floatToIntBits(e.getKey()) && Float2LongLinkedOpenHashMap.this.value[this.index] == e.getValue();
        }
        
        @Override
        public int hashCode() {
            return HashCommon.float2int(Float2LongLinkedOpenHashMap.this.key[this.index]) ^ HashCommon.long2int(Float2LongLinkedOpenHashMap.this.value[this.index]);
        }
        
        @Override
        public String toString() {
            return Float2LongLinkedOpenHashMap.this.key[this.index] + "=>" + Float2LongLinkedOpenHashMap.this.value[this.index];
        }
    }
    
    private class MapIterator
    {
        int prev;
        int next;
        int curr;
        int index;
        
        private MapIterator() {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            this.next = Float2LongLinkedOpenHashMap.this.first;
            this.index = 0;
        }
        
        private MapIterator(final float from) {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            if (Float.floatToIntBits(from) == 0) {
                if (Float2LongLinkedOpenHashMap.this.containsNullKey) {
                    this.next = (int)Float2LongLinkedOpenHashMap.this.link[Float2LongLinkedOpenHashMap.this.n];
                    this.prev = Float2LongLinkedOpenHashMap.this.n;
                    return;
                }
                throw new NoSuchElementException("The key " + from + " does not belong to this map.");
            }
            else {
                if (Float.floatToIntBits(Float2LongLinkedOpenHashMap.this.key[Float2LongLinkedOpenHashMap.this.last]) == Float.floatToIntBits(from)) {
                    this.prev = Float2LongLinkedOpenHashMap.this.last;
                    this.index = Float2LongLinkedOpenHashMap.this.size;
                    return;
                }
                for (int pos = HashCommon.mix(HashCommon.float2int(from)) & Float2LongLinkedOpenHashMap.this.mask; Float.floatToIntBits(Float2LongLinkedOpenHashMap.this.key[pos]) != 0; pos = (pos + 1 & Float2LongLinkedOpenHashMap.this.mask)) {
                    if (Float.floatToIntBits(Float2LongLinkedOpenHashMap.this.key[pos]) == Float.floatToIntBits(from)) {
                        this.next = (int)Float2LongLinkedOpenHashMap.this.link[pos];
                        this.prev = pos;
                        return;
                    }
                }
                throw new NoSuchElementException("The key " + from + " does not belong to this map.");
            }
        }
        
        public boolean hasNext() {
            return this.next != -1;
        }
        
        public boolean hasPrevious() {
            return this.prev != -1;
        }
        
        private final void ensureIndexKnown() {
            if (this.index >= 0) {
                return;
            }
            if (this.prev == -1) {
                this.index = 0;
                return;
            }
            if (this.next == -1) {
                this.index = Float2LongLinkedOpenHashMap.this.size;
                return;
            }
            int pos = Float2LongLinkedOpenHashMap.this.first;
            this.index = 1;
            while (pos != this.prev) {
                pos = (int)Float2LongLinkedOpenHashMap.this.link[pos];
                ++this.index;
            }
        }
        
        public int nextIndex() {
            this.ensureIndexKnown();
            return this.index;
        }
        
        public int previousIndex() {
            this.ensureIndexKnown();
            return this.index - 1;
        }
        
        public int nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = this.next;
            this.next = (int)Float2LongLinkedOpenHashMap.this.link[this.curr];
            this.prev = this.curr;
            if (this.index >= 0) {
                ++this.index;
            }
            return this.curr;
        }
        
        public int previousEntry() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = this.prev;
            this.prev = (int)(Float2LongLinkedOpenHashMap.this.link[this.curr] >>> 32);
            this.next = this.curr;
            if (this.index >= 0) {
                --this.index;
            }
            return this.curr;
        }
        
        public void remove() {
            this.ensureIndexKnown();
            if (this.curr == -1) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                --this.index;
                this.prev = (int)(Float2LongLinkedOpenHashMap.this.link[this.curr] >>> 32);
            }
            else {
                this.next = (int)Float2LongLinkedOpenHashMap.this.link[this.curr];
            }
            final Float2LongLinkedOpenHashMap this$0 = Float2LongLinkedOpenHashMap.this;
            --this$0.size;
            if (this.prev == -1) {
                Float2LongLinkedOpenHashMap.this.first = this.next;
            }
            else {
                final long[] link = Float2LongLinkedOpenHashMap.this.link;
                final int prev = this.prev;
                link[prev] ^= ((Float2LongLinkedOpenHashMap.this.link[this.prev] ^ ((long)this.next & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            }
            if (this.next == -1) {
                Float2LongLinkedOpenHashMap.this.last = this.prev;
            }
            else {
                final long[] link2 = Float2LongLinkedOpenHashMap.this.link;
                final int next = this.next;
                link2[next] ^= ((Float2LongLinkedOpenHashMap.this.link[this.next] ^ ((long)this.prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            }
            int pos = this.curr;
            this.curr = -1;
            if (pos == Float2LongLinkedOpenHashMap.this.n) {
                Float2LongLinkedOpenHashMap.this.containsNullKey = false;
                return;
            }
            final float[] key = Float2LongLinkedOpenHashMap.this.key;
            int last = 0;
        Label_0264:
            while (true) {
                pos = ((last = pos) + 1 & Float2LongLinkedOpenHashMap.this.mask);
                float curr;
                while (Float.floatToIntBits(curr = key[pos]) != 0) {
                    final int slot = HashCommon.mix(HashCommon.float2int(curr)) & Float2LongLinkedOpenHashMap.this.mask;
                    Label_0360: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0360;
                            }
                            if (slot > pos) {
                                break Label_0360;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0360;
                        }
                        pos = (pos + 1 & Float2LongLinkedOpenHashMap.this.mask);
                        continue;
                    }
                    key[last] = curr;
                    Float2LongLinkedOpenHashMap.this.value[last] = Float2LongLinkedOpenHashMap.this.value[pos];
                    if (this.next == pos) {
                        this.next = last;
                    }
                    if (this.prev == pos) {
                        this.prev = last;
                    }
                    Float2LongLinkedOpenHashMap.this.fixPointers(pos, last);
                    continue Label_0264;
                }
                break;
            }
            key[last] = 0.0f;
        }
        
        public int skip(final int n) {
            int i = n;
            while (i-- != 0 && this.hasNext()) {
                this.nextEntry();
            }
            return n - i - 1;
        }
        
        public int back(final int n) {
            int i = n;
            while (i-- != 0 && this.hasPrevious()) {
                this.previousEntry();
            }
            return n - i - 1;
        }
    }
    
    private class EntryIterator extends MapIterator implements ObjectListIterator<Float2LongMap.Entry>
    {
        private MapEntry entry;
        
        public EntryIterator() {
        }
        
        public EntryIterator(final float from) {
            super(from);
        }
        
        @Override
        public MapEntry next() {
            return this.entry = new MapEntry(this.nextEntry());
        }
        
        @Override
        public MapEntry previous() {
            return this.entry = new MapEntry(this.previousEntry());
        }
        
        @Override
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
        
        @Override
        public void set(final Float2LongMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final Float2LongMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private class FastEntryIterator extends MapIterator implements ObjectListIterator<Float2LongMap.Entry>
    {
        final MapEntry entry;
        
        public FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public FastEntryIterator(final float from) {
            super(from);
            this.entry = new MapEntry();
        }
        
        @Override
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
        
        @Override
        public MapEntry previous() {
            this.entry.index = this.previousEntry();
            return this.entry;
        }
        
        @Override
        public void set(final Float2LongMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final Float2LongMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSortedSet<Float2LongMap.Entry> implements Float2LongSortedMap.FastSortedEntrySet
    {
        @Override
        public ObjectBidirectionalIterator<Float2LongMap.Entry> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public Comparator<? super Float2LongMap.Entry> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Float2LongMap.Entry> subSet(final Float2LongMap.Entry fromElement, final Float2LongMap.Entry toElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<Float2LongMap.Entry> headSet(final Float2LongMap.Entry toElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSortedSet<Float2LongMap.Entry> tailSet(final Float2LongMap.Entry fromElement) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Float2LongMap.Entry first() {
            if (Float2LongLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Float2LongLinkedOpenHashMap.this.first);
        }
        
        @Override
        public Float2LongMap.Entry last() {
            if (Float2LongLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Float2LongLinkedOpenHashMap.this.last);
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            if (e.getKey() == null || !(e.getKey() instanceof Float)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Long)) {
                return false;
            }
            final float k = (float)e.getKey();
            final long v = (long)e.getValue();
            if (Float.floatToIntBits(k) == 0) {
                return Float2LongLinkedOpenHashMap.this.containsNullKey && Float2LongLinkedOpenHashMap.this.value[Float2LongLinkedOpenHashMap.this.n] == v;
            }
            final float[] key = Float2LongLinkedOpenHashMap.this.key;
            int pos;
            float curr;
            if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(HashCommon.float2int(k)) & Float2LongLinkedOpenHashMap.this.mask)]) == 0) {
                return false;
            }
            if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
                return Float2LongLinkedOpenHashMap.this.value[pos] == v;
            }
            while (Float.floatToIntBits(curr = key[pos = (pos + 1 & Float2LongLinkedOpenHashMap.this.mask)]) != 0) {
                if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
                    return Float2LongLinkedOpenHashMap.this.value[pos] == v;
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
            if (e.getKey() == null || !(e.getKey() instanceof Float)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Long)) {
                return false;
            }
            final float k = (float)e.getKey();
            final long v = (long)e.getValue();
            if (Float.floatToIntBits(k) == 0) {
                if (Float2LongLinkedOpenHashMap.this.containsNullKey && Float2LongLinkedOpenHashMap.this.value[Float2LongLinkedOpenHashMap.this.n] == v) {
                    Float2LongLinkedOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final float[] key = Float2LongLinkedOpenHashMap.this.key;
                int pos;
                float curr;
                if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(HashCommon.float2int(k)) & Float2LongLinkedOpenHashMap.this.mask)]) == 0) {
                    return false;
                }
                if (Float.floatToIntBits(curr) != Float.floatToIntBits(k)) {
                    while (Float.floatToIntBits(curr = key[pos = (pos + 1 & Float2LongLinkedOpenHashMap.this.mask)]) != 0) {
                        if (Float.floatToIntBits(curr) == Float.floatToIntBits(k) && Float2LongLinkedOpenHashMap.this.value[pos] == v) {
                            Float2LongLinkedOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Float2LongLinkedOpenHashMap.this.value[pos] == v) {
                    Float2LongLinkedOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        @Override
        public int size() {
            return Float2LongLinkedOpenHashMap.this.size;
        }
        
        @Override
        public void clear() {
            Float2LongLinkedOpenHashMap.this.clear();
        }
        
        @Override
        public ObjectBidirectionalIterator<Float2LongMap.Entry> iterator(final Float2LongMap.Entry from) {
            return new EntryIterator(from.getFloatKey());
        }
        
        @Override
        public ObjectBidirectionalIterator<Float2LongMap.Entry> fastIterator() {
            return new FastEntryIterator();
        }
        
        @Override
        public ObjectBidirectionalIterator<Float2LongMap.Entry> fastIterator(final Float2LongMap.Entry from) {
            return new FastEntryIterator(from.getFloatKey());
        }
    }
    
    private final class KeyIterator extends MapIterator implements FloatListIterator
    {
        public KeyIterator(final float k) {
            super(k);
        }
        
        @Override
        public float previousFloat() {
            return Float2LongLinkedOpenHashMap.this.key[this.previousEntry()];
        }
        
        @Override
        public void set(final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Float previous() {
            return Float2LongLinkedOpenHashMap.this.key[this.previousEntry()];
        }
        
        @Override
        public void set(final Float ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final Float ok) {
            throw new UnsupportedOperationException();
        }
        
        public KeyIterator() {
        }
        
        @Override
        public float nextFloat() {
            return Float2LongLinkedOpenHashMap.this.key[this.nextEntry()];
        }
        
        @Override
        public Float next() {
            return Float2LongLinkedOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractFloatSortedSet
    {
        @Override
        public FloatListIterator iterator(final float from) {
            return new KeyIterator(from);
        }
        
        @Override
        public FloatListIterator iterator() {
            return new KeyIterator();
        }
        
        @Override
        public int size() {
            return Float2LongLinkedOpenHashMap.this.size;
        }
        
        @Override
        public boolean contains(final float k) {
            return Float2LongLinkedOpenHashMap.this.containsKey(k);
        }
        
        @Override
        public boolean rem(final float k) {
            final int oldSize = Float2LongLinkedOpenHashMap.this.size;
            Float2LongLinkedOpenHashMap.this.remove(k);
            return Float2LongLinkedOpenHashMap.this.size != oldSize;
        }
        
        @Override
        public void clear() {
            Float2LongLinkedOpenHashMap.this.clear();
        }
        
        @Override
        public float firstFloat() {
            if (Float2LongLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Float2LongLinkedOpenHashMap.this.key[Float2LongLinkedOpenHashMap.this.first];
        }
        
        @Override
        public float lastFloat() {
            if (Float2LongLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Float2LongLinkedOpenHashMap.this.key[Float2LongLinkedOpenHashMap.this.last];
        }
        
        @Override
        public FloatComparator comparator() {
            return null;
        }
        
        @Override
        public final FloatSortedSet tailSet(final float from) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public final FloatSortedSet headSet(final float to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public final FloatSortedSet subSet(final float from, final float to) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class ValueIterator extends MapIterator implements LongListIterator
    {
        @Override
        public long previousLong() {
            return Float2LongLinkedOpenHashMap.this.value[this.previousEntry()];
        }
        
        @Override
        public Long previous() {
            return Float2LongLinkedOpenHashMap.this.value[this.previousEntry()];
        }
        
        @Override
        public void set(final Long ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final Long ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void set(final long v) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final long v) {
            throw new UnsupportedOperationException();
        }
        
        public ValueIterator() {
        }
        
        @Override
        public long nextLong() {
            return Float2LongLinkedOpenHashMap.this.value[this.nextEntry()];
        }
        
        @Deprecated
        @Override
        public Long next() {
            return Float2LongLinkedOpenHashMap.this.value[this.nextEntry()];
        }
    }
}
