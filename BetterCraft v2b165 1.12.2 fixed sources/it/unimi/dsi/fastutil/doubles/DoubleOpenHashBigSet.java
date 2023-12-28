// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

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

public class DoubleOpenHashBigSet extends AbstractDoubleSet implements Serializable, Cloneable, Hash, Size64
{
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient double[][] key;
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
    
    public DoubleOpenHashBigSet(final long expected, final float f) {
        if (f <= 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1");
        }
        if (this.n < 0L) {
            throw new IllegalArgumentException("The expected number of elements must be nonnegative");
        }
        this.f = f;
        this.n = HashCommon.bigArraySize(expected, f);
        this.maxFill = HashCommon.maxFill(this.n, f);
        this.key = DoubleBigArrays.newBigArray(this.n);
        this.initMasks();
    }
    
    public DoubleOpenHashBigSet(final long expected) {
        this(expected, 0.75f);
    }
    
    public DoubleOpenHashBigSet() {
        this(16L, 0.75f);
    }
    
    public DoubleOpenHashBigSet(final Collection<? extends Double> c, final float f) {
        this(c.size(), f);
        this.addAll(c);
    }
    
    public DoubleOpenHashBigSet(final Collection<? extends Double> c) {
        this(c, 0.75f);
    }
    
    public DoubleOpenHashBigSet(final DoubleCollection c, final float f) {
        this(c.size(), f);
        this.addAll(c);
    }
    
    public DoubleOpenHashBigSet(final DoubleCollection c) {
        this(c, 0.75f);
    }
    
    public DoubleOpenHashBigSet(final DoubleIterator i, final float f) {
        this(16L, f);
        while (i.hasNext()) {
            this.add(i.nextDouble());
        }
    }
    
    public DoubleOpenHashBigSet(final DoubleIterator i) {
        this(i, 0.75f);
    }
    
    public DoubleOpenHashBigSet(final Iterator<?> i, final float f) {
        this(DoubleIterators.asDoubleIterator(i), f);
    }
    
    public DoubleOpenHashBigSet(final Iterator<?> i) {
        this(DoubleIterators.asDoubleIterator(i));
    }
    
    public DoubleOpenHashBigSet(final double[] a, final int offset, final int length, final float f) {
        this((length < 0) ? 0L : length, f);
        DoubleArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; ++i) {
            this.add(a[offset + i]);
        }
    }
    
    public DoubleOpenHashBigSet(final double[] a, final int offset, final int length) {
        this(a, offset, length, 0.75f);
    }
    
    public DoubleOpenHashBigSet(final double[] a, final float f) {
        this(a, 0, a.length, f);
    }
    
    public DoubleOpenHashBigSet(final double[] a) {
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
    public boolean addAll(final DoubleCollection c) {
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
    public boolean addAll(final Collection<? extends Double> c) {
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
    public boolean add(final double k) {
        if (Double.doubleToLongBits(k) == 0L) {
            if (this.containsNull) {
                return false;
            }
            this.containsNull = true;
        }
        else {
            final double[][] key = this.key;
            final long h = HashCommon.mix(Double.doubleToRawLongBits(k));
            int base;
            int displ;
            double curr;
            if (Double.doubleToLongBits(curr = key[base = (int)((h & this.mask) >>> 27)][displ = (int)(h & (long)this.segmentMask)]) != 0L) {
                if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
                    return false;
                }
                while (Double.doubleToLongBits(curr = key[base = (base + (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0) & this.baseMask)][displ]) != 0L) {
                    if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
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
        final double[][] key = this.key;
        long last = 0L;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1L & this.mask);
            while (Double.doubleToLongBits(DoubleBigArrays.get(key, pos)) != 0L) {
                final long slot = HashCommon.mix(Double.doubleToRawLongBits(DoubleBigArrays.get(key, pos))) & this.mask;
                Label_0110: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0110;
                        }
                        if (slot > pos) {
                            break Label_0110;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0110;
                    }
                    pos = (pos + 1L & this.mask);
                    continue;
                }
                DoubleBigArrays.set(key, last, DoubleBigArrays.get(key, pos));
                continue Label_0006;
            }
            break;
        }
        DoubleBigArrays.set(key, last, 0.0);
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
    public boolean rem(final double k) {
        if (Double.doubleToLongBits(k) == 0L) {
            return this.containsNull && this.removeNullEntry();
        }
        final double[][] key = this.key;
        final long h = HashCommon.mix(Double.doubleToRawLongBits(k));
        int base;
        int displ;
        double curr;
        if (Double.doubleToLongBits(curr = key[base = (int)((h & this.mask) >>> 27)][displ = (int)(h & (long)this.segmentMask)]) == 0L) {
            return false;
        }
        if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
            return this.removeEntry(base, displ);
        }
        while (Double.doubleToLongBits(curr = key[base = (base + (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0) & this.baseMask)][displ]) != 0L) {
            if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
                return this.removeEntry(base, displ);
            }
        }
        return false;
    }
    
    @Override
    public boolean contains(final double k) {
        if (Double.doubleToLongBits(k) == 0L) {
            return this.containsNull;
        }
        final double[][] key = this.key;
        final long h = HashCommon.mix(Double.doubleToRawLongBits(k));
        int base;
        int displ;
        double curr;
        if (Double.doubleToLongBits(curr = key[base = (int)((h & this.mask) >>> 27)][displ = (int)(h & (long)this.segmentMask)]) == 0L) {
            return false;
        }
        if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
            return true;
        }
        while (Double.doubleToLongBits(curr = key[base = (base + (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0) & this.baseMask)][displ]) != 0L) {
            if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
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
        DoubleBigArrays.fill(this.key, 0.0);
    }
    
    @Override
    public DoubleIterator iterator() {
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
        final double[][] key = this.key;
        final double[][] newKey = DoubleBigArrays.newBigArray(newN);
        final long mask = newN - 1L;
        final int newSegmentMask = newKey[0].length - 1;
        final int newBaseMask = newKey.length - 1;
        int base = 0;
        int displ = 0;
        long i = this.realSize();
        while (i-- != 0L) {
            while (Double.doubleToLongBits(key[base][displ]) == 0L) {
                base += (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0);
            }
            final double k = key[base][displ];
            final long h = HashCommon.mix(Double.doubleToRawLongBits(k));
            int b;
            int d;
            if (Double.doubleToLongBits(newKey[b = (int)((h & mask) >>> 27)][d = (int)(h & (long)newSegmentMask)]) != 0L) {
                while (Double.doubleToLongBits(newKey[b = (b + (((d = (d + 1 & newSegmentMask)) == 0) ? 1 : 0) & newBaseMask)][d]) != 0L) {}
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
    
    public DoubleOpenHashBigSet clone() {
        DoubleOpenHashBigSet c;
        try {
            c = (DoubleOpenHashBigSet)super.clone();
        }
        catch (final CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.key = DoubleBigArrays.copy(this.key);
        c.containsNull = this.containsNull;
        return c;
    }
    
    @Override
    public int hashCode() {
        final double[][] key = this.key;
        int h = 0;
        int base = 0;
        int displ = 0;
        long j = this.realSize();
        while (j-- != 0L) {
            while (Double.doubleToLongBits(key[base][displ]) == 0L) {
                base += (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0);
            }
            h += HashCommon.double2int(key[base][displ]);
            base += (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0);
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final DoubleIterator i = this.iterator();
        s.defaultWriteObject();
        long j = this.size;
        while (j-- != 0L) {
            s.writeDouble(i.nextDouble());
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.bigArraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        final double[][] bigArray = DoubleBigArrays.newBigArray(this.n);
        this.key = bigArray;
        final double[][] key = bigArray;
        this.initMasks();
        long i = this.size;
        while (i-- != 0L) {
            final double k = s.readDouble();
            if (Double.doubleToLongBits(k) == 0L) {
                this.containsNull = true;
            }
            else {
                final long h = HashCommon.mix(Double.doubleToRawLongBits(k));
                int base;
                int displ;
                if (Double.doubleToLongBits(key[base = (int)((h & this.mask) >>> 27)][displ = (int)(h & (long)this.segmentMask)]) != 0L) {
                    while (Double.doubleToLongBits(key[base = (base + (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0) & this.baseMask)][displ]) != 0L) {}
                }
                key[base][displ] = k;
            }
        }
    }
    
    private void checkTable() {
    }
    
    private class SetIterator extends AbstractDoubleIterator
    {
        int base;
        int displ;
        long last;
        long c;
        boolean mustReturnNull;
        DoubleArrayList wrapped;
        
        private SetIterator() {
            this.base = DoubleOpenHashBigSet.this.key.length;
            this.last = -1L;
            this.c = DoubleOpenHashBigSet.this.size;
            this.mustReturnNull = DoubleOpenHashBigSet.this.containsNull;
        }
        
        @Override
        public boolean hasNext() {
            return this.c != 0L;
        }
        
        @Override
        public double nextDouble() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            --this.c;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.last = DoubleOpenHashBigSet.this.n;
                return 0.0;
            }
            final double[][] key = DoubleOpenHashBigSet.this.key;
            while (this.displ != 0 || this.base > 0) {
                if (this.displ-- == 0) {
                    final double[][] array = key;
                    final int base = this.base - 1;
                    this.base = base;
                    this.displ = array[base].length - 1;
                }
                final double k = key[this.base][this.displ];
                if (Double.doubleToLongBits(k) != 0L) {
                    this.last = this.base * 134217728L + this.displ;
                    return k;
                }
            }
            this.last = Long.MIN_VALUE;
            final DoubleArrayList wrapped = this.wrapped;
            final int base2 = this.base - 1;
            this.base = base2;
            return wrapped.getDouble(-base2 - 1);
        }
        
        private final void shiftKeys(long pos) {
            final double[][] key = DoubleOpenHashBigSet.this.key;
            long last = 0L;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1L & DoubleOpenHashBigSet.this.mask);
                double curr;
                while (Double.doubleToLongBits(curr = DoubleBigArrays.get(key, pos)) != 0L) {
                    final long slot = HashCommon.mix(Double.doubleToRawLongBits(curr)) & DoubleOpenHashBigSet.this.mask;
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
                        pos = (pos + 1L & DoubleOpenHashBigSet.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new DoubleArrayList();
                        }
                        this.wrapped.add(DoubleBigArrays.get(key, pos));
                    }
                    DoubleBigArrays.set(key, last, curr);
                    continue Label_0009;
                }
                break;
            }
            DoubleBigArrays.set(key, last, 0.0);
        }
        
        @Override
        public void remove() {
            if (this.last == -1L) {
                throw new IllegalStateException();
            }
            if (this.last == DoubleOpenHashBigSet.this.n) {
                DoubleOpenHashBigSet.this.containsNull = false;
            }
            else {
                if (this.base < 0) {
                    DoubleOpenHashBigSet.this.remove(this.wrapped.getDouble(-this.base - 1));
                    this.last = -1L;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final DoubleOpenHashBigSet this$0 = DoubleOpenHashBigSet.this;
            --this$0.size;
            this.last = -1L;
        }
    }
}
