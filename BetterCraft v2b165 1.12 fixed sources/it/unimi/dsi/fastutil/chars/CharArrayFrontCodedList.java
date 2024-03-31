// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.ListIterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.NoSuchElementException;
import it.unimi.dsi.fastutil.objects.AbstractObjectListIterator;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Collection;
import it.unimi.dsi.fastutil.longs.LongArrays;
import java.util.Iterator;
import java.util.RandomAccess;
import java.io.Serializable;
import it.unimi.dsi.fastutil.objects.AbstractObjectList;

public class CharArrayFrontCodedList extends AbstractObjectList<char[]> implements Serializable, Cloneable, RandomAccess
{
    private static final long serialVersionUID = 1L;
    protected final int n;
    protected final int ratio;
    protected final char[][] array;
    protected transient long[] p;
    
    public CharArrayFrontCodedList(final Iterator<char[]> arrays, final int ratio) {
        if (ratio < 1) {
            throw new IllegalArgumentException("Illegal ratio (" + ratio + ")");
        }
        char[][] array = CharBigArrays.EMPTY_BIG_ARRAY;
        long[] p = LongArrays.EMPTY_ARRAY;
        final char[][] a = new char[2][];
        long curSize = 0L;
        int n = 0;
        int b = 0;
        while (arrays.hasNext()) {
            a[b] = arrays.next();
            int length = a[b].length;
            if (n % ratio == 0) {
                p = LongArrays.grow(p, n / ratio + 1);
                p[n / ratio] = curSize;
                array = CharBigArrays.grow(array, curSize + count(length) + length, curSize);
                curSize += writeInt(array, length, curSize);
                CharBigArrays.copyToBig(a[b], 0, array, curSize, length);
                curSize += length;
            }
            else {
                int minLength = a[1 - b].length;
                if (length < minLength) {
                    minLength = length;
                }
                int common;
                for (common = 0; common < minLength && a[0][common] == a[1][common]; ++common) {}
                length -= common;
                array = CharBigArrays.grow(array, curSize + count(length) + count(common) + length, curSize);
                curSize += writeInt(array, length, curSize);
                curSize += writeInt(array, common, curSize);
                CharBigArrays.copyToBig(a[b], common, array, curSize, length);
                curSize += length;
            }
            b = 1 - b;
            ++n;
        }
        this.n = n;
        this.ratio = ratio;
        this.array = CharBigArrays.trim(array, curSize);
        this.p = LongArrays.trim(p, (n + ratio - 1) / ratio);
    }
    
    public CharArrayFrontCodedList(final Collection<char[]> c, final int ratio) {
        this(c.iterator(), ratio);
    }
    
    private static int readInt(final char[][] a, final long pos) {
        final char c0 = CharBigArrays.get(a, pos);
        return (c0 < '\u8000') ? c0 : ((c0 & '\u7fff') << 16 | CharBigArrays.get(a, pos + 1L));
    }
    
    private static int count(final int length) {
        return (length < 32768) ? 1 : 2;
    }
    
    private static int writeInt(final char[][] a, final int length, long pos) {
        if (length < 32768) {
            CharBigArrays.set(a, pos, (char)length);
            return 1;
        }
        CharBigArrays.set(a, pos++, (char)(length >>> 16 | 0x8000));
        CharBigArrays.set(a, pos, (char)(length & 0xFFFF));
        return 2;
    }
    
    public int ratio() {
        return this.ratio;
    }
    
    private int length(final int index) {
        final char[][] array = this.array;
        final int delta = index % this.ratio;
        long pos = this.p[index / this.ratio];
        int length = readInt(array, pos);
        if (delta == 0) {
            return length;
        }
        pos += count(length) + length;
        length = readInt(array, pos);
        int common = readInt(array, pos + count(length));
        for (int i = 0; i < delta - 1; ++i) {
            pos += count(length) + count(common) + length;
            length = readInt(array, pos);
            common = readInt(array, pos + count(length));
        }
        return length + common;
    }
    
    public int arrayLength(final int index) {
        this.ensureRestrictedIndex(index);
        return this.length(index);
    }
    
    private int extract(final int index, final char[] a, final int offset, final int length) {
        final int delta = index % this.ratio;
        final long startPos = this.p[index / this.ratio];
        long pos;
        int arrayLength = readInt(this.array, pos = startPos);
        int currLen = 0;
        if (delta == 0) {
            pos = this.p[index / this.ratio] + count(arrayLength);
            CharBigArrays.copyFromBig(this.array, pos, a, offset, Math.min(length, arrayLength));
            return arrayLength;
        }
        int common = 0;
        for (int i = 0; i < delta; ++i) {
            final long prevArrayPos = pos + count(arrayLength) + ((i != 0) ? count(common) : 0);
            pos = prevArrayPos + arrayLength;
            arrayLength = readInt(this.array, pos);
            common = readInt(this.array, pos + count(arrayLength));
            final int actualCommon = Math.min(common, length);
            if (actualCommon <= currLen) {
                currLen = actualCommon;
            }
            else {
                CharBigArrays.copyFromBig(this.array, prevArrayPos, a, currLen + offset, actualCommon - currLen);
                currLen = actualCommon;
            }
        }
        if (currLen < length) {
            CharBigArrays.copyFromBig(this.array, pos + count(arrayLength) + count(common), a, currLen + offset, Math.min(arrayLength, length - currLen));
        }
        return arrayLength + common;
    }
    
    @Override
    public char[] get(final int index) {
        return this.getArray(index);
    }
    
    public char[] getArray(final int index) {
        this.ensureRestrictedIndex(index);
        final int length = this.length(index);
        final char[] a = new char[length];
        this.extract(index, a, 0, length);
        return a;
    }
    
    public int get(final int index, final char[] a, final int offset, final int length) {
        this.ensureRestrictedIndex(index);
        CharArrays.ensureOffsetLength(a, offset, length);
        final int arrayLength = this.extract(index, a, offset, length);
        if (length >= arrayLength) {
            return arrayLength;
        }
        return length - arrayLength;
    }
    
    public int get(final int index, final char[] a) {
        return this.get(index, a, 0, a.length);
    }
    
    @Override
    public int size() {
        return this.n;
    }
    
    @Override
    public ObjectListIterator<char[]> listIterator(final int start) {
        this.ensureIndex(start);
        return new AbstractObjectListIterator<char[]>() {
            char[] s = CharArrays.EMPTY_ARRAY;
            int i = start;
            long pos = 0L;
            boolean inSync;
            
            {
                this.i = 0;
                if (start != 0) {
                    if (start != CharArrayFrontCodedList.this.n) {
                        this.pos = CharArrayFrontCodedList.this.p[start / CharArrayFrontCodedList.this.ratio];
                        int j = start % CharArrayFrontCodedList.this.ratio;
                        this.i = start - j;
                        while (j-- != 0) {
                            this.next();
                        }
                    }
                }
            }
            
            @Override
            public boolean hasNext() {
                return this.i < CharArrayFrontCodedList.this.n;
            }
            
            @Override
            public boolean hasPrevious() {
                return this.i > 0;
            }
            
            @Override
            public int previousIndex() {
                return this.i - 1;
            }
            
            @Override
            public int nextIndex() {
                return this.i;
            }
            
            @Override
            public char[] next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                int length;
                if (this.i % CharArrayFrontCodedList.this.ratio == 0) {
                    this.pos = CharArrayFrontCodedList.this.p[this.i / CharArrayFrontCodedList.this.ratio];
                    length = readInt(CharArrayFrontCodedList.this.array, this.pos);
                    this.s = CharArrays.ensureCapacity(this.s, length, 0);
                    CharBigArrays.copyFromBig(CharArrayFrontCodedList.this.array, this.pos + count(length), this.s, 0, length);
                    this.pos += length + count(length);
                    this.inSync = true;
                }
                else if (this.inSync) {
                    length = readInt(CharArrayFrontCodedList.this.array, this.pos);
                    final int common = readInt(CharArrayFrontCodedList.this.array, this.pos + count(length));
                    this.s = CharArrays.ensureCapacity(this.s, length + common, common);
                    CharBigArrays.copyFromBig(CharArrayFrontCodedList.this.array, this.pos + count(length) + count(common), this.s, common, length);
                    this.pos += count(length) + count(common) + length;
                    length += common;
                }
                else {
                    this.s = CharArrays.ensureCapacity(this.s, length = CharArrayFrontCodedList.this.length(this.i), 0);
                    CharArrayFrontCodedList.this.extract(this.i, this.s, 0, length);
                }
                ++this.i;
                return CharArrays.copy(this.s, 0, length);
            }
            
            @Override
            public char[] previous() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                this.inSync = false;
                final CharArrayFrontCodedList this$0 = CharArrayFrontCodedList.this;
                final int n = this.i - 1;
                this.i = n;
                return this$0.getArray(n);
            }
        };
    }
    
    public CharArrayFrontCodedList clone() {
        return this;
    }
    
    @Override
    public String toString() {
        final StringBuffer s = new StringBuffer();
        s.append("[ ");
        for (int i = 0; i < this.n; ++i) {
            if (i != 0) {
                s.append(", ");
            }
            s.append(CharArrayList.wrap(this.getArray(i)).toString());
        }
        s.append(" ]");
        return s.toString();
    }
    
    protected long[] rebuildPointerArray() {
        final long[] p = new long[(this.n + this.ratio - 1) / this.ratio];
        final char[][] a = this.array;
        long pos = 0L;
        int i = 0;
        int j = 0;
        int skip = this.ratio - 1;
        while (i < this.n) {
            final int length = readInt(a, pos);
            final int count = count(length);
            if (++skip == this.ratio) {
                skip = 0;
                p[j++] = pos;
                pos += count + length;
            }
            else {
                pos += count + count(readInt(a, pos + count)) + length;
            }
            ++i;
        }
        return p;
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.p = this.rebuildPointerArray();
    }
}
