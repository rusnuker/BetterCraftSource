// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.util.NoSuchElementException;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class FloatOpenCustomHashSet extends AbstractFloatSet implements Serializable, Cloneable, Hash
{
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient float[] key;
    protected transient int mask;
    protected transient boolean containsNull;
    protected FloatHash.Strategy strategy;
    protected transient int n;
    protected transient int maxFill;
    protected int size;
    protected final float f;
    
    public FloatOpenCustomHashSet(final int expected, final float f, final FloatHash.Strategy strategy) {
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
        this.key = new float[this.n + 1];
    }
    
    public FloatOpenCustomHashSet(final int expected, final FloatHash.Strategy strategy) {
        this(expected, 0.75f, strategy);
    }
    
    public FloatOpenCustomHashSet(final FloatHash.Strategy strategy) {
        this(16, 0.75f, strategy);
    }
    
    public FloatOpenCustomHashSet(final Collection<? extends Float> c, final float f, final FloatHash.Strategy strategy) {
        this(c.size(), f, strategy);
        this.addAll(c);
    }
    
    public FloatOpenCustomHashSet(final Collection<? extends Float> c, final FloatHash.Strategy strategy) {
        this(c, 0.75f, strategy);
    }
    
    public FloatOpenCustomHashSet(final FloatCollection c, final float f, final FloatHash.Strategy strategy) {
        this(c.size(), f, strategy);
        this.addAll(c);
    }
    
    public FloatOpenCustomHashSet(final FloatCollection c, final FloatHash.Strategy strategy) {
        this(c, 0.75f, strategy);
    }
    
    public FloatOpenCustomHashSet(final FloatIterator i, final float f, final FloatHash.Strategy strategy) {
        this(16, f, strategy);
        while (i.hasNext()) {
            this.add(i.nextFloat());
        }
    }
    
    public FloatOpenCustomHashSet(final FloatIterator i, final FloatHash.Strategy strategy) {
        this(i, 0.75f, strategy);
    }
    
    public FloatOpenCustomHashSet(final Iterator<?> i, final float f, final FloatHash.Strategy strategy) {
        this(FloatIterators.asFloatIterator(i), f, strategy);
    }
    
    public FloatOpenCustomHashSet(final Iterator<?> i, final FloatHash.Strategy strategy) {
        this(FloatIterators.asFloatIterator(i), strategy);
    }
    
    public FloatOpenCustomHashSet(final float[] a, final int offset, final int length, final float f, final FloatHash.Strategy strategy) {
        this((length < 0) ? 0 : length, f, strategy);
        FloatArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; ++i) {
            this.add(a[offset + i]);
        }
    }
    
    public FloatOpenCustomHashSet(final float[] a, final int offset, final int length, final FloatHash.Strategy strategy) {
        this(a, offset, length, 0.75f, strategy);
    }
    
    public FloatOpenCustomHashSet(final float[] a, final float f, final FloatHash.Strategy strategy) {
        this(a, 0, a.length, f, strategy);
    }
    
    public FloatOpenCustomHashSet(final float[] a, final FloatHash.Strategy strategy) {
        this(a, 0.75f, strategy);
    }
    
    public FloatHash.Strategy strategy() {
        return this.strategy;
    }
    
    private int realSize() {
        return this.containsNull ? (this.size - 1) : this.size;
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
    
    @Override
    public boolean addAll(final FloatCollection c) {
        if (this.f <= 0.5) {
            this.ensureCapacity(c.size());
        }
        else {
            this.tryCapacity(this.size() + c.size());
        }
        return super.addAll(c);
    }
    
    @Override
    public boolean addAll(final Collection<? extends Float> c) {
        if (this.f <= 0.5) {
            this.ensureCapacity(c.size());
        }
        else {
            this.tryCapacity(this.size() + c.size());
        }
        return super.addAll(c);
    }
    
    @Override
    public boolean add(final float k) {
        if (this.strategy.equals(k, 0.0f)) {
            if (this.containsNull) {
                return false;
            }
            this.containsNull = true;
            this.key[this.n] = k;
        }
        else {
            final float[] key = this.key;
            int pos;
            float curr;
            if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) != 0) {
                if (this.strategy.equals(curr, k)) {
                    return false;
                }
                while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                    if (this.strategy.equals(curr, k)) {
                        return false;
                    }
                }
            }
            key[pos] = k;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return true;
    }
    
    protected final void shiftKeys(int pos) {
        final float[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            float curr;
            while (Float.floatToIntBits(curr = key[pos]) != 0) {
                final int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
                Label_0099: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0099;
                        }
                        if (slot > pos) {
                            break Label_0099;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0099;
                    }
                    pos = (pos + 1 & this.mask);
                    continue;
                }
                key[last] = curr;
                continue Label_0006;
            }
            break;
        }
        key[last] = 0.0f;
    }
    
    private boolean removeEntry(final int pos) {
        --this.size;
        this.shiftKeys(pos);
        if (this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return true;
    }
    
    private boolean removeNullEntry() {
        this.containsNull = false;
        this.key[this.n] = 0.0f;
        --this.size;
        if (this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return true;
    }
    
    @Override
    public boolean rem(final float k) {
        if (this.strategy.equals(k, 0.0f)) {
            return this.containsNull && this.removeNullEntry();
        }
        final float[] key = this.key;
        int pos;
        float curr;
        if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0) {
            return false;
        }
        if (this.strategy.equals(k, curr)) {
            return this.removeEntry(pos);
        }
        while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (this.strategy.equals(k, curr)) {
                return this.removeEntry(pos);
            }
        }
        return false;
    }
    
    @Override
    public boolean contains(final float k) {
        if (this.strategy.equals(k, 0.0f)) {
            return this.containsNull;
        }
        final float[] key = this.key;
        int pos;
        float curr;
        if (Float.floatToIntBits(curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0) {
            return false;
        }
        if (this.strategy.equals(k, curr)) {
            return true;
        }
        while (Float.floatToIntBits(curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (this.strategy.equals(k, curr)) {
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
        this.containsNull = false;
        Arrays.fill(this.key, 0.0f);
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
    public FloatIterator iterator() {
        return new SetIterator();
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
        final int mask = newN - 1;
        final float[] newKey = new float[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (Float.floatToIntBits(key[--i]) == 0) {}
            int pos;
            if (Float.floatToIntBits(newKey[pos = (HashCommon.mix(this.strategy.hashCode(key[i])) & mask)]) != 0) {
                while (Float.floatToIntBits(newKey[pos = (pos + 1 & mask)]) != 0) {}
            }
            newKey[pos] = key[i];
        }
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
    }
    
    public FloatOpenCustomHashSet clone() {
        FloatOpenCustomHashSet c;
        try {
            c = (FloatOpenCustomHashSet)super.clone();
        }
        catch (final CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.key = this.key.clone();
        c.containsNull = this.containsNull;
        c.strategy = this.strategy;
        return c;
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int j = this.realSize();
        int i = 0;
        while (j-- != 0) {
            while (Float.floatToIntBits(this.key[i]) == 0) {
                ++i;
            }
            h += this.strategy.hashCode(this.key[i]);
            ++i;
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final FloatIterator i = this.iterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            s.writeFloat(i.nextFloat());
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
        int i = this.size;
        while (i-- != 0) {
            final float k = s.readFloat();
            int pos;
            if (this.strategy.equals(k, 0.0f)) {
                pos = this.n;
                this.containsNull = true;
            }
            else if (Float.floatToIntBits(key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) != 0) {
                while (Float.floatToIntBits(key[pos = (pos + 1 & this.mask)]) != 0) {}
            }
            key[pos] = k;
        }
    }
    
    private void checkTable() {
    }
    
    private class SetIterator extends AbstractFloatIterator
    {
        int pos;
        int last;
        int c;
        boolean mustReturnNull;
        FloatArrayList wrapped;
        
        private SetIterator() {
            this.pos = FloatOpenCustomHashSet.this.n;
            this.last = -1;
            this.c = FloatOpenCustomHashSet.this.size;
            this.mustReturnNull = FloatOpenCustomHashSet.this.containsNull;
        }
        
        @Override
        public boolean hasNext() {
            return this.c != 0;
        }
        
        @Override
        public float nextFloat() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            --this.c;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.last = FloatOpenCustomHashSet.this.n;
                return FloatOpenCustomHashSet.this.key[FloatOpenCustomHashSet.this.n];
            }
            final float[] key = FloatOpenCustomHashSet.this.key;
            while (--this.pos >= 0) {
                if (Float.floatToIntBits(key[this.pos]) != 0) {
                    final float[] array = key;
                    final int pos = this.pos;
                    this.last = pos;
                    return array[pos];
                }
            }
            this.last = Integer.MIN_VALUE;
            return this.wrapped.getFloat(-this.pos - 1);
        }
        
        private final void shiftKeys(int pos) {
            final float[] key = FloatOpenCustomHashSet.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & FloatOpenCustomHashSet.this.mask);
                float curr;
                while (Float.floatToIntBits(curr = key[pos]) != 0) {
                    final int slot = HashCommon.mix(FloatOpenCustomHashSet.this.strategy.hashCode(curr)) & FloatOpenCustomHashSet.this.mask;
                    Label_0114: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0114;
                            }
                            if (slot > pos) {
                                break Label_0114;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0114;
                        }
                        pos = (pos + 1 & FloatOpenCustomHashSet.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new FloatArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    continue Label_0009;
                }
                break;
            }
            key[last] = 0.0f;
        }
        
        @Override
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == FloatOpenCustomHashSet.this.n) {
                FloatOpenCustomHashSet.this.containsNull = false;
                FloatOpenCustomHashSet.this.key[FloatOpenCustomHashSet.this.n] = 0.0f;
            }
            else {
                if (this.pos < 0) {
                    FloatOpenCustomHashSet.this.rem(this.wrapped.getFloat(-this.pos - 1));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final FloatOpenCustomHashSet this$0 = FloatOpenCustomHashSet.this;
            --this$0.size;
            this.last = -1;
        }
    }
}
