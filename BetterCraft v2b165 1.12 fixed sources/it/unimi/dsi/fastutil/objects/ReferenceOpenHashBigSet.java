// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

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

public class ReferenceOpenHashBigSet<K> extends AbstractReferenceSet<K> implements Serializable, Cloneable, Hash, Size64
{
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient K[][] key;
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
    
    public ReferenceOpenHashBigSet(final long expected, final float f) {
        if (f <= 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1");
        }
        if (this.n < 0L) {
            throw new IllegalArgumentException("The expected number of elements must be nonnegative");
        }
        this.f = f;
        this.n = HashCommon.bigArraySize(expected, f);
        this.maxFill = HashCommon.maxFill(this.n, f);
        this.key = (K[][])ObjectBigArrays.newBigArray(this.n);
        this.initMasks();
    }
    
    public ReferenceOpenHashBigSet(final long expected) {
        this(expected, 0.75f);
    }
    
    public ReferenceOpenHashBigSet() {
        this(16L, 0.75f);
    }
    
    public ReferenceOpenHashBigSet(final Collection<? extends K> c, final float f) {
        this(c.size(), f);
        this.addAll(c);
    }
    
    public ReferenceOpenHashBigSet(final Collection<? extends K> c) {
        this(c, 0.75f);
    }
    
    public ReferenceOpenHashBigSet(final ReferenceCollection<? extends K> c, final float f) {
        this(c.size(), f);
        this.addAll(c);
    }
    
    public ReferenceOpenHashBigSet(final ReferenceCollection<? extends K> c) {
        this(c, 0.75f);
    }
    
    public ReferenceOpenHashBigSet(final Iterator<? extends K> i, final float f) {
        this(16L, f);
        while (i.hasNext()) {
            this.add(i.next());
        }
    }
    
    public ReferenceOpenHashBigSet(final Iterator<? extends K> i) {
        this(i, 0.75f);
    }
    
    public ReferenceOpenHashBigSet(final K[] a, final int offset, final int length, final float f) {
        this((length < 0) ? 0L : length, f);
        ObjectArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; ++i) {
            this.add(a[offset + i]);
        }
    }
    
    public ReferenceOpenHashBigSet(final K[] a, final int offset, final int length) {
        this(a, offset, length, 0.75f);
    }
    
    public ReferenceOpenHashBigSet(final K[] a, final float f) {
        this(a, 0, a.length, f);
    }
    
    public ReferenceOpenHashBigSet(final K[] a) {
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
    public boolean addAll(final Collection<? extends K> c) {
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
    public boolean add(final K k) {
        if (k == null) {
            if (this.containsNull) {
                return false;
            }
            this.containsNull = true;
        }
        else {
            final K[][] key = this.key;
            final long h = HashCommon.mix((long)System.identityHashCode(k));
            int base;
            int displ;
            K curr;
            if ((curr = key[base = (int)((h & this.mask) >>> 27)][displ = (int)(h & (long)this.segmentMask)]) != null) {
                if (curr == k) {
                    return false;
                }
                while ((curr = key[base = (base + (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0) & this.baseMask)][displ]) != null) {
                    if (curr == k) {
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
        final K[][] key = this.key;
        long last = 0L;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1L & this.mask);
            while (ObjectBigArrays.get(key, pos) != null) {
                final long slot = HashCommon.mix((long)System.identityHashCode(ObjectBigArrays.get(key, pos))) & this.mask;
                Label_0107: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0107;
                        }
                        if (slot > pos) {
                            break Label_0107;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0107;
                    }
                    pos = (pos + 1L & this.mask);
                    continue;
                }
                ObjectBigArrays.set(key, last, (K)ObjectBigArrays.get((K[][])key, pos));
                continue Label_0006;
            }
            break;
        }
        ObjectBigArrays.set(key, last, (K)null);
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
    public boolean rem(final Object k) {
        if (k == null) {
            return this.containsNull && this.removeNullEntry();
        }
        final K[][] key = this.key;
        final long h = HashCommon.mix((long)System.identityHashCode(k));
        int base;
        int displ;
        K curr;
        if ((curr = key[base = (int)((h & this.mask) >>> 27)][displ = (int)(h & (long)this.segmentMask)]) == null) {
            return false;
        }
        if (curr == k) {
            return this.removeEntry(base, displ);
        }
        while ((curr = key[base = (base + (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0) & this.baseMask)][displ]) != null) {
            if (curr == k) {
                return this.removeEntry(base, displ);
            }
        }
        return false;
    }
    
    @Override
    public boolean contains(final Object k) {
        if (k == null) {
            return this.containsNull;
        }
        final K[][] key = this.key;
        final long h = HashCommon.mix((long)System.identityHashCode(k));
        int base;
        int displ;
        K curr;
        if ((curr = key[base = (int)((h & this.mask) >>> 27)][displ = (int)(h & (long)this.segmentMask)]) == null) {
            return false;
        }
        if (curr == k) {
            return true;
        }
        while ((curr = key[base = (base + (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0) & this.baseMask)][displ]) != null) {
            if (curr == k) {
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
        ObjectBigArrays.fill(this.key, (K)null);
    }
    
    @Override
    public ObjectIterator<K> iterator() {
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
        final K[][] key = this.key;
        final K[][] newKey = (K[][])ObjectBigArrays.newBigArray(newN);
        final long mask = newN - 1L;
        final int newSegmentMask = newKey[0].length - 1;
        final int newBaseMask = newKey.length - 1;
        int base = 0;
        int displ = 0;
        long i = this.realSize();
        while (i-- != 0L) {
            while (key[base][displ] == null) {
                base += (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0);
            }
            final K k = key[base][displ];
            final long h = HashCommon.mix((long)System.identityHashCode(k));
            int b;
            int d;
            if (newKey[b = (int)((h & mask) >>> 27)][d = (int)(h & (long)newSegmentMask)] != null) {
                while (newKey[b = (b + (((d = (d + 1 & newSegmentMask)) == 0) ? 1 : 0) & newBaseMask)][d] != null) {}
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
    
    public ReferenceOpenHashBigSet<K> clone() {
        ReferenceOpenHashBigSet<K> c;
        try {
            c = (ReferenceOpenHashBigSet)super.clone();
        }
        catch (final CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.key = ObjectBigArrays.copy(this.key);
        c.containsNull = this.containsNull;
        return c;
    }
    
    @Override
    public int hashCode() {
        final K[][] key = this.key;
        int h = 0;
        int base = 0;
        int displ = 0;
        long j = this.realSize();
        while (j-- != 0L) {
            while (key[base][displ] == null) {
                base += (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0);
            }
            if (this != key[base][displ]) {
                h += System.identityHashCode(key[base][displ]);
            }
            base += (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0);
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final ObjectIterator<K> i = this.iterator();
        s.defaultWriteObject();
        long j = this.size;
        while (j-- != 0L) {
            s.writeObject(i.next());
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.bigArraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        final Object[][] key2 = ObjectBigArrays.newBigArray(this.n);
        this.key = (K[][])key2;
        final K[][] key = (K[][])key2;
        this.initMasks();
        long i = this.size;
        while (i-- != 0L) {
            final K k = (K)s.readObject();
            if (k == null) {
                this.containsNull = true;
            }
            else {
                final long h = HashCommon.mix((long)System.identityHashCode(k));
                int base;
                int displ;
                if (key[base = (int)((h & this.mask) >>> 27)][displ = (int)(h & (long)this.segmentMask)] != null) {
                    while (key[base = (base + (((displ = (displ + 1 & this.segmentMask)) == 0) ? 1 : 0) & this.baseMask)][displ] != null) {}
                }
                key[base][displ] = k;
            }
        }
    }
    
    private void checkTable() {
    }
    
    private class SetIterator extends AbstractObjectIterator<K>
    {
        int base;
        int displ;
        long last;
        long c;
        boolean mustReturnNull;
        ReferenceArrayList<K> wrapped;
        
        private SetIterator() {
            this.base = ReferenceOpenHashBigSet.this.key.length;
            this.last = -1L;
            this.c = ReferenceOpenHashBigSet.this.size;
            this.mustReturnNull = ReferenceOpenHashBigSet.this.containsNull;
        }
        
        @Override
        public boolean hasNext() {
            return this.c != 0L;
        }
        
        @Override
        public K next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            --this.c;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.last = ReferenceOpenHashBigSet.this.n;
                return null;
            }
            final K[][] key = ReferenceOpenHashBigSet.this.key;
            while (this.displ != 0 || this.base > 0) {
                if (this.displ-- == 0) {
                    final K[][] array = key;
                    final int base = this.base - 1;
                    this.base = base;
                    this.displ = array[base].length - 1;
                }
                final K k = key[this.base][this.displ];
                if (k != null) {
                    this.last = this.base * 134217728L + this.displ;
                    return k;
                }
            }
            this.last = Long.MIN_VALUE;
            final ReferenceArrayList<K> wrapped = this.wrapped;
            final int base2 = this.base - 1;
            this.base = base2;
            return wrapped.get(-base2 - 1);
        }
        
        private final void shiftKeys(long pos) {
            final K[][] key = ReferenceOpenHashBigSet.this.key;
            long last = 0L;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1L & ReferenceOpenHashBigSet.this.mask);
                K curr;
                while ((curr = ObjectBigArrays.get(key, pos)) != null) {
                    final long slot = HashCommon.mix((long)System.identityHashCode(curr)) & ReferenceOpenHashBigSet.this.mask;
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
                        pos = (pos + 1L & ReferenceOpenHashBigSet.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new ReferenceArrayList<K>();
                        }
                        this.wrapped.add(ObjectBigArrays.get(key, pos));
                    }
                    ObjectBigArrays.set(key, last, curr);
                    continue Label_0009;
                }
                break;
            }
            ObjectBigArrays.set(key, last, (K)null);
        }
        
        @Override
        public void remove() {
            if (this.last == -1L) {
                throw new IllegalStateException();
            }
            if (this.last == ReferenceOpenHashBigSet.this.n) {
                ReferenceOpenHashBigSet.this.containsNull = false;
            }
            else {
                if (this.base < 0) {
                    ReferenceOpenHashBigSet.this.remove(this.wrapped.set(-this.base - 1, null));
                    this.last = -1L;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final ReferenceOpenHashBigSet this$0 = ReferenceOpenHashBigSet.this;
            --this$0.size;
            this.last = -1L;
        }
    }
}
