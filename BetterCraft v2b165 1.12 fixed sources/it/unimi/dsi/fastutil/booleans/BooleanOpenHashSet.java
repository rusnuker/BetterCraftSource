// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

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

public class BooleanOpenHashSet extends AbstractBooleanSet implements Serializable, Cloneable, Hash
{
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient boolean[] key;
    protected transient int mask;
    protected transient boolean containsNull;
    protected transient int n;
    protected transient int maxFill;
    protected int size;
    protected final float f;
    
    public BooleanOpenHashSet(final int expected, final float f) {
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
        this.key = new boolean[this.n + 1];
    }
    
    public BooleanOpenHashSet(final int expected) {
        this(expected, 0.75f);
    }
    
    public BooleanOpenHashSet() {
        this(16, 0.75f);
    }
    
    public BooleanOpenHashSet(final Collection<? extends Boolean> c, final float f) {
        this(c.size(), f);
        this.addAll(c);
    }
    
    public BooleanOpenHashSet(final Collection<? extends Boolean> c) {
        this(c, 0.75f);
    }
    
    public BooleanOpenHashSet(final BooleanCollection c, final float f) {
        this(c.size(), f);
        this.addAll(c);
    }
    
    public BooleanOpenHashSet(final BooleanCollection c) {
        this(c, 0.75f);
    }
    
    public BooleanOpenHashSet(final BooleanIterator i, final float f) {
        this(16, f);
        while (i.hasNext()) {
            this.add(i.nextBoolean());
        }
    }
    
    public BooleanOpenHashSet(final BooleanIterator i) {
        this(i, 0.75f);
    }
    
    public BooleanOpenHashSet(final Iterator<?> i, final float f) {
        this(BooleanIterators.asBooleanIterator(i), f);
    }
    
    public BooleanOpenHashSet(final Iterator<?> i) {
        this(BooleanIterators.asBooleanIterator(i));
    }
    
    public BooleanOpenHashSet(final boolean[] a, final int offset, final int length, final float f) {
        this((length < 0) ? 0 : length, f);
        BooleanArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; ++i) {
            this.add(a[offset + i]);
        }
    }
    
    public BooleanOpenHashSet(final boolean[] a, final int offset, final int length) {
        this(a, offset, length, 0.75f);
    }
    
    public BooleanOpenHashSet(final boolean[] a, final float f) {
        this(a, 0, a.length, f);
    }
    
    public BooleanOpenHashSet(final boolean[] a) {
        this(a, 0.75f);
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
    public boolean addAll(final BooleanCollection c) {
        if (this.f <= 0.5) {
            this.ensureCapacity(c.size());
        }
        else {
            this.tryCapacity(this.size() + c.size());
        }
        return super.addAll(c);
    }
    
    @Override
    public boolean addAll(final Collection<? extends Boolean> c) {
        if (this.f <= 0.5) {
            this.ensureCapacity(c.size());
        }
        else {
            this.tryCapacity(this.size() + c.size());
        }
        return super.addAll(c);
    }
    
    @Override
    public boolean add(final boolean k) {
        if (!k) {
            if (this.containsNull) {
                return false;
            }
            this.containsNull = true;
        }
        else {
            final boolean[] key = this.key;
            int pos;
            boolean curr;
            if (curr = key[pos = ((k ? 262886248 : -878682501) & this.mask)]) {
                if (curr == k) {
                    return false;
                }
                while (curr = key[pos = (pos + 1 & this.mask)]) {
                    if (curr == k) {
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
        final boolean[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            boolean curr;
            while (curr = key[pos]) {
                final int slot = (curr ? 262886248 : -878682501) & this.mask;
                Label_0094: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0094;
                        }
                        if (slot > pos) {
                            break Label_0094;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0094;
                    }
                    pos = (pos + 1 & this.mask);
                    continue;
                }
                key[last] = curr;
                continue Label_0006;
            }
            break;
        }
        key[last] = false;
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
        this.key[this.n] = false;
        --this.size;
        if (this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return true;
    }
    
    @Override
    public boolean rem(final boolean k) {
        if (!k) {
            return this.containsNull && this.removeNullEntry();
        }
        final boolean[] key = this.key;
        int pos;
        boolean curr;
        if (!(curr = key[pos = ((k ? 262886248 : -878682501) & this.mask)])) {
            return false;
        }
        if (k == curr) {
            return this.removeEntry(pos);
        }
        while (curr = key[pos = (pos + 1 & this.mask)]) {
            if (k == curr) {
                return this.removeEntry(pos);
            }
        }
        return false;
    }
    
    @Override
    public boolean contains(final boolean k) {
        if (!k) {
            return this.containsNull;
        }
        final boolean[] key = this.key;
        int pos;
        boolean curr;
        if (!(curr = key[pos = ((k ? 262886248 : -878682501) & this.mask)])) {
            return false;
        }
        if (k == curr) {
            return true;
        }
        while (curr = key[pos = (pos + 1 & this.mask)]) {
            if (k == curr) {
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
        Arrays.fill(this.key, false);
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
    public BooleanIterator iterator() {
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
        final boolean[] key = this.key;
        final int mask = newN - 1;
        final boolean[] newKey = new boolean[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (!key[--i]) {}
            int pos;
            if (newKey[pos = ((key[i] ? 262886248 : -878682501) & mask)]) {
                while (newKey[pos = (pos + 1 & mask)]) {}
            }
            newKey[pos] = key[i];
        }
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
    }
    
    public BooleanOpenHashSet clone() {
        BooleanOpenHashSet c;
        try {
            c = (BooleanOpenHashSet)super.clone();
        }
        catch (final CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.key = this.key.clone();
        c.containsNull = this.containsNull;
        return c;
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int j = this.realSize();
        int i = 0;
        while (j-- != 0) {
            while (!this.key[i]) {
                ++i;
            }
            h += (this.key[i] ? 1231 : 1237);
            ++i;
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final BooleanIterator i = this.iterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            s.writeBoolean(i.nextBoolean());
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        final boolean[] key2 = new boolean[this.n + 1];
        this.key = key2;
        final boolean[] key = key2;
        int i = this.size;
        while (i-- != 0) {
            final boolean k = s.readBoolean();
            int pos;
            if (!k) {
                pos = this.n;
                this.containsNull = true;
            }
            else if (key[pos = ((k ? 262886248 : -878682501) & this.mask)]) {
                while (key[pos = (pos + 1 & this.mask)]) {}
            }
            key[pos] = k;
        }
    }
    
    private void checkTable() {
    }
    
    private class SetIterator extends AbstractBooleanIterator
    {
        int pos;
        int last;
        int c;
        boolean mustReturnNull;
        BooleanArrayList wrapped;
        
        private SetIterator() {
            this.pos = BooleanOpenHashSet.this.n;
            this.last = -1;
            this.c = BooleanOpenHashSet.this.size;
            this.mustReturnNull = BooleanOpenHashSet.this.containsNull;
        }
        
        @Override
        public boolean hasNext() {
            return this.c != 0;
        }
        
        @Override
        public boolean nextBoolean() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            --this.c;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.last = BooleanOpenHashSet.this.n;
                return BooleanOpenHashSet.this.key[BooleanOpenHashSet.this.n];
            }
            final boolean[] key = BooleanOpenHashSet.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos]) {
                    final boolean[] array = key;
                    final int pos = this.pos;
                    this.last = pos;
                    return array[pos];
                }
            }
            this.last = Integer.MIN_VALUE;
            return this.wrapped.getBoolean(-this.pos - 1);
        }
        
        private final void shiftKeys(int pos) {
            final boolean[] key = BooleanOpenHashSet.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & BooleanOpenHashSet.this.mask);
                boolean curr;
                while (curr = key[pos]) {
                    final int slot = (curr ? 262886248 : -878682501) & BooleanOpenHashSet.this.mask;
                    Label_0106: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0106;
                            }
                            if (slot > pos) {
                                break Label_0106;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0106;
                        }
                        pos = (pos + 1 & BooleanOpenHashSet.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new BooleanArrayList(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    continue Label_0009;
                }
                break;
            }
            key[last] = false;
        }
        
        @Override
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == BooleanOpenHashSet.this.n) {
                BooleanOpenHashSet.this.containsNull = false;
                BooleanOpenHashSet.this.key[BooleanOpenHashSet.this.n] = false;
            }
            else {
                if (this.pos < 0) {
                    BooleanOpenHashSet.this.rem(this.wrapped.getBoolean(-this.pos - 1));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final BooleanOpenHashSet this$0 = BooleanOpenHashSet.this;
            --this$0.size;
            this.last = -1;
        }
    }
}
