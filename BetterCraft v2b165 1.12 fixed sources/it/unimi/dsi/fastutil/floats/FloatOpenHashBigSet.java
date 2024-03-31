// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.util.NoSuchElementException;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Collection;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class FloatOpenHashBigSet extends AbstractFloatSet implements Serializable, Cloneable, Hash, Size64
{
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient float[][] key;
    protected transient long mask;
    protected transient int segmentMask;
    protected transient int baseMask;
    protected transient boolean containsNull;
    protected transient long n;
    protected transient long maxFill;
    protected final float f;
    protected long size;
    
    private void initMasks() {
        this.mask = this.n - 1L;
        this.segmentMask = this.key[0].length - 1;
        this.baseMask = this.key.length - 1;
    }
    
    public FloatOpenHashBigSet(final long expected, final float f) {
        if (f <= 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1");
        }
        if (this.n < 0L) {
            throw new IllegalArgumentException("The expected number of elements must be nonnegative");
        }
        this.f = f;
        this.n = HashCommon.bigArraySize(expected, f);
        this.maxFill = HashCommon.maxFill(this.n, f);
        this.key = FloatBigArrays.newBigArray(this.n);
        this.initMasks();
    }
    
    public FloatOpenHashBigSet(final long expected) {
        this(expected, 0.75f);
    }
    
    public FloatOpenHashBigSet() {
        this(16L, 0.75f);
    }
    
    public FloatOpenHashBigSet(final Collection<? extends Float> c, final float f) {
        this(c.size(), f);
        this.addAll(c);
    }
    
    public FloatOpenHashBigSet(final Collection<? extends Float> c) {
        this(c, 0.75f);
    }
    
    public FloatOpenHashBigSet(final FloatCollection c, final float f) {
        this(c.size(), f);
        this.addAll(c);
    }
    
    public FloatOpenHashBigSet(final FloatCollection c) {
        this(c, 0.75f);
    }
    
    public FloatOpenHashBigSet(final FloatIterator i, final float f) {
        this(16L, f);
        while (i.hasNext()) {
            this.add(i.nextFloat());
        }
    }
    
    public FloatOpenHashBigSet(final FloatIterator i) {
        this(i, 0.75f);
    }
    
    public FloatOpenHashBigSet(final Iterator<?> i, final float f) {
        this(FloatIterators.asFloatIterator(i), f);
    }
    
    public FloatOpenHashBigSet(final Iterator<?> i) {
        this(FloatIterators.asFloatIterator(i));
    }
    
    public FloatOpenHashBigSet(final float[] a, final int offset, final int length, final float f) {
        this((length < 0) ? 0L : length, f);
        FloatArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; ++i) {
            this.add(a[offset + i]);
        }
    }
    
    public FloatOpenHashBigSet(final float[] a, final int offset, final int length) {
        this(a, offset, length, 0.75f);
    }
    
    public FloatOpenHashBigSet(final float[] a, final float f) {
        this(a, 0, a.length, f);
    }
    
    public FloatOpenHashBigSet(final float[] a) {
        this(a, 0.75f);
    }
    
    private long realSize() {
        return this.containsNull ? (this.size - 1L) : this.size;
    }
    
    private void ensureCapacity(final long capacity) {
        final long needed = HashCommon.bigArraySize(capacity, this.f);
        if (needed > this.n) {
            this.rehash(needed);
        }
    }
    
    @Override
    public boolean addAll(final FloatCollection c) {
        final long size = (c instanceof Size64) ? ((Size64)c).size64() : c.size();
        if (this.f <= 0.5) {
            this.ensureCapacity(size);
        }
        else {
            this.ensureCapacity(this.size64() + size);
        }
        return super.addAll(c);
    }
    
    @Override
    public boolean addAll(final Collection<? extends Float> c) {
        final long size = (c instanceof Size64) ? ((Size64)c).size64() : c.size();
        if (this.f <= 0.5) {
            this.ensureCapacity(size);
        }
        else {
            this.ensureCapacity(this.size64() + size);
        }
        return super.addAll(c);
    }
    
    @Override
    public boolean add(final float k) {
        if (Float.floatToIntBits(k) == 0) {
            if (this.containsNull) {
                return false;
            }
            this.containsNull = true;
        }
        else {
            final float[][] key = this.key;
            final long h = HashCommon.mix((long)HashCommon.float2int(k));
            int base;
            int displ;
            float curr;
            if (Float.floatToIntBits(curr = key[base = (int)((h & this.mask) >>> 27)][displ = (int)(h & (long)this.segmentMask)]) != 0) {
                if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
                    return false;
                }
                while (Float.floatToIntBits(curr = key[base = (base + (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0) & this.baseMask)][displ]) != 0) {
                    if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
                        return false;
                    }
                }
            }
            key[base][displ] = k;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(2L * this.n);
        }
        return true;
    }
    
    protected final void shiftKeys(long pos) {
        final float[][] key = this.key;
        long last = 0L;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1L & this.mask);
            while (Float.floatToIntBits(FloatBigArrays.get(key, pos)) != 0) {
                final long slot = HashCommon.mix((long)HashCommon.float2int(FloatBigArrays.get(key, pos))) & this.mask;
                Label_0109: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0109;
                        }
                        if (slot > pos) {
                            break Label_0109;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0109;
                    }
                    pos = (pos + 1L & this.mask);
                    continue;
                }
                FloatBigArrays.set(key, last, FloatBigArrays.get(key, pos));
                continue Label_0006;
            }
            break;
        }
        FloatBigArrays.set(key, last, 0.0f);
    }
    
    private boolean removeEntry(final int base, final int displ) {
        this.shiftKeys(base * 134217728L + displ);
        final long size = this.size - 1L;
        this.size = size;
        if (size < this.maxFill / 4L && this.n > 16L) {
            this.rehash(this.n / 2L);
        }
        return true;
    }
    
    private boolean removeNullEntry() {
        this.containsNull = false;
        final long size = this.size - 1L;
        this.size = size;
        if (size < this.maxFill / 4L && this.n > 16L) {
            this.rehash(this.n / 2L);
        }
        return true;
    }
    
    @Override
    public boolean rem(final float k) {
        if (Float.floatToIntBits(k) == 0) {
            return this.containsNull && this.removeNullEntry();
        }
        final float[][] key = this.key;
        final long h = HashCommon.mix((long)HashCommon.float2int(k));
        int base;
        int displ;
        float curr;
        if (Float.floatToIntBits(curr = key[base = (int)((h & this.mask) >>> 27)][displ = (int)(h & (long)this.segmentMask)]) == 0) {
            return false;
        }
        if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
            return this.removeEntry(base, displ);
        }
        while (Float.floatToIntBits(curr = key[base = (base + (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0) & this.baseMask)][displ]) != 0) {
            if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
                return this.removeEntry(base, displ);
            }
        }
        return false;
    }
    
    @Override
    public boolean contains(final float k) {
        if (Float.floatToIntBits(k) == 0) {
            return this.containsNull;
        }
        final float[][] key = this.key;
        final long h = HashCommon.mix((long)HashCommon.float2int(k));
        int base;
        int displ;
        float curr;
        if (Float.floatToIntBits(curr = key[base = (int)((h & this.mask) >>> 27)][displ = (int)(h & (long)this.segmentMask)]) == 0) {
            return false;
        }
        if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
            return true;
        }
        while (Float.floatToIntBits(curr = key[base = (base + (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0) & this.baseMask)][displ]) != 0) {
            if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void clear() {
        if (this.size == 0L) {
            return;
        }
        this.size = 0L;
        this.containsNull = false;
        FloatBigArrays.fill(this.key, 0.0f);
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
        final long l = HashCommon.bigArraySize(this.size, this.f);
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
    
    public boolean trim(final long n) {
        final long l = HashCommon.bigArraySize(n, this.f);
        if (this.n <= l) {
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
    
    protected void rehash(final long newN) {
        final float[][] key = this.key;
        final float[][] newKey = FloatBigArrays.newBigArray(newN);
        final long mask = newN - 1L;
        final int newSegmentMask = newKey[0].length - 1;
        final int newBaseMask = newKey.length - 1;
        int base = 0;
        int displ = 0;
        long i = this.realSize();
        while (i-- != 0L) {
            while (Float.floatToIntBits(key[base][displ]) == 0) {
                base += (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0);
            }
            final float k = key[base][displ];
            final long h = HashCommon.mix((long)HashCommon.float2int(k));
            int b;
            int d;
            if (Float.floatToIntBits(newKey[b = (int)((h & mask) >>> 27)][d = (int)(h & (long)newSegmentMask)]) != 0) {
                while (Float.floatToIntBits(newKey[b = (b + (((d = (d + 1 & newSegmentMask)) == 0) ? 1 : 0) & newBaseMask)][d]) != 0) {}
            }
            newKey[b][d] = k;
            base += (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0);
        }
        this.n = newN;
        this.key = newKey;
        this.initMasks();
        this.maxFill = HashCommon.maxFill(this.n, this.f);
    }
    
    @Deprecated
    @Override
    public int size() {
        return (int)Math.min(2147483647L, this.size);
    }
    
    @Override
    public long size64() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0L;
    }
    
    public FloatOpenHashBigSet clone() {
        FloatOpenHashBigSet c;
        try {
            c = (FloatOpenHashBigSet)super.clone();
        }
        catch (final CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.key = FloatBigArrays.copy(this.key);
        c.containsNull = this.containsNull;
        return c;
    }
    
    @Override
    public int hashCode() {
        final float[][] key = this.key;
        int h = 0;
        int base = 0;
        int displ = 0;
        long j = this.realSize();
        while (j-- != 0L) {
            while (Float.floatToIntBits(key[base][displ]) == 0) {
                base += (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0);
            }
            h += HashCommon.float2int(key[base][displ]);
            base += (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0);
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final FloatIterator i = this.iterator();
        s.defaultWriteObject();
        long j = this.size;
        while (j-- != 0L) {
            s.writeFloat(i.nextFloat());
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.bigArraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        final float[][] bigArray = FloatBigArrays.newBigArray(this.n);
        this.key = bigArray;
        final float[][] key = bigArray;
        this.initMasks();
        long i = this.size;
        while (i-- != 0L) {
            final float k = s.readFloat();
            if (Float.floatToIntBits(k) == 0) {
                this.containsNull = true;
            }
            else {
                final long h = HashCommon.mix((long)HashCommon.float2int(k));
                int base;
                int displ;
                if (Float.floatToIntBits(key[base = (int)((h & this.mask) >>> 27)][displ = (int)(h & (long)this.segmentMask)]) != 0) {
                    while (Float.floatToIntBits(key[base = (base + (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0) & this.baseMask)][displ]) != 0) {}
                }
                key[base][displ] = k;
            }
        }
    }
    
    private void checkTable() {
    }
    
    private class SetIterator extends AbstractFloatIterator
    {
        int base;
        int displ;
        long last;
        long c;
        boolean mustReturnNull;
        FloatArrayList wrapped;
        
        private SetIterator() {
            this.base = FloatOpenHashBigSet.this.key.length;
            this.last = -1L;
            this.c = FloatOpenHashBigSet.this.size;
            this.mustReturnNull = FloatOpenHashBigSet.this.containsNull;
        }
        
        @Override
        public boolean hasNext() {
            return this.c != 0L;
        }
        
        @Override
        public float nextFloat() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            --this.c;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.last = FloatOpenHashBigSet.this.n;
                return 0.0f;
            }
            final float[][] key = FloatOpenHashBigSet.this.key;
            while (this.displ != 0 || this.base > 0) {
                if (this.displ-- == 0) {
                    final float[][] array = key;
                    final int base = this.base - 1;
                    this.base = base;
                    this.displ = array[base].length - 1;
                }
                final float k = key[this.base][this.displ];
                if (Float.floatToIntBits(k) != 0) {
                    this.last = this.base * 134217728L + this.displ;
                    return k;
                }
            }
            this.last = Long.MIN_VALUE;
            final FloatArrayList wrapped = this.wrapped;
            final int base2 = this.base - 1;
            this.base = base2;
            return wrapped.getFloat(-base2 - 1);
        }
        
        private final void shiftKeys(long pos) {
            final float[][] key = FloatOpenHashBigSet.this.key;
            long last = 0L;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1L & FloatOpenHashBigSet.this.mask);
                float curr;
                while (Float.floatToIntBits(curr = FloatBigArrays.get(key, pos)) != 0) {
                    final long slot = HashCommon.mix((long)HashCommon.float2int(curr)) & FloatOpenHashBigSet.this.mask;
                    Label_0120: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0120;
                            }
                            if (slot > pos) {
                                break Label_0120;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0120;
                        }
                        pos = (pos + 1L & FloatOpenHashBigSet.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new FloatArrayList();
                        }
                        this.wrapped.add(FloatBigArrays.get(key, pos));
                    }
                    FloatBigArrays.set(key, last, curr);
                    continue Label_0009;
                }
                break;
            }
            FloatBigArrays.set(key, last, 0.0f);
        }
        
        @Override
        public void remove() {
            if (this.last == -1L) {
                throw new IllegalStateException();
            }
            if (this.last == FloatOpenHashBigSet.this.n) {
                FloatOpenHashBigSet.this.containsNull = false;
            }
            else {
                if (this.base < 0) {
                    FloatOpenHashBigSet.this.remove(this.wrapped.getFloat(-this.base - 1));
                    this.last = -1L;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final FloatOpenHashBigSet this$0 = FloatOpenHashBigSet.this;
            --this$0.size;
            this.last = -1L;
        }
    }
}
