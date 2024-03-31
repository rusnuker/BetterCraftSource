// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util;

import java.util.Iterator;
import java.util.Collection;
import java.util.List;
import io.netty.util.internal.InternalThreadLocalMap;
import java.util.regex.Pattern;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import java.nio.charset.CharsetEncoder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import io.netty.util.internal.MathUtil;
import java.util.Arrays;

public final class AsciiString implements CharSequence, Comparable<CharSequence>
{
    public static final AsciiString EMPTY_STRING;
    private static final char MAX_CHAR_VALUE = '\u00ff';
    public static final int INDEX_NOT_FOUND = -1;
    private final byte[] value;
    private final int offset;
    private final int length;
    private int hash;
    private String string;
    public static final HashingStrategy<CharSequence> CASE_INSENSITIVE_HASHER;
    public static final HashingStrategy<CharSequence> CASE_SENSITIVE_HASHER;
    
    public AsciiString(final byte[] value) {
        this(value, true);
    }
    
    public AsciiString(final byte[] value, final boolean copy) {
        this(value, 0, value.length, copy);
    }
    
    public AsciiString(final byte[] value, final int start, final int length, final boolean copy) {
        if (copy) {
            this.value = Arrays.copyOfRange(value, start, start + length);
            this.offset = 0;
        }
        else {
            if (MathUtil.isOutOfBounds(start, length, value.length)) {
                throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length + ") <= value.length(" + value.length + ')');
            }
            this.value = value;
            this.offset = start;
        }
        this.length = length;
    }
    
    public AsciiString(final ByteBuffer value) {
        this(value, true);
    }
    
    public AsciiString(final ByteBuffer value, final boolean copy) {
        this(value, value.position(), value.remaining(), copy);
    }
    
    public AsciiString(final ByteBuffer value, final int start, final int length, final boolean copy) {
        if (MathUtil.isOutOfBounds(start, length, value.capacity())) {
            throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length + ") <= value.capacity(" + value.capacity() + ')');
        }
        if (value.hasArray()) {
            if (copy) {
                final int bufferOffset = value.arrayOffset() + start;
                this.value = Arrays.copyOfRange(value.array(), bufferOffset, bufferOffset + length);
                this.offset = 0;
            }
            else {
                this.value = value.array();
                this.offset = start;
            }
        }
        else {
            this.value = new byte[length];
            final int oldPos = value.position();
            value.get(this.value, 0, length);
            value.position(oldPos);
            this.offset = 0;
        }
        this.length = length;
    }
    
    public AsciiString(final char[] value) {
        this(value, 0, value.length);
    }
    
    public AsciiString(final char[] value, final int start, final int length) {
        if (MathUtil.isOutOfBounds(start, length, value.length)) {
            throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length + ") <= value.length(" + value.length + ')');
        }
        this.value = new byte[length];
        for (int i = 0, j = start; i < length; ++i, ++j) {
            this.value[i] = c2b(value[j]);
        }
        this.offset = 0;
        this.length = length;
    }
    
    public AsciiString(final char[] value, final Charset charset) {
        this(value, charset, 0, value.length);
    }
    
    public AsciiString(final char[] value, final Charset charset, final int start, final int length) {
        final CharBuffer cbuf = CharBuffer.wrap(value, start, length);
        final CharsetEncoder encoder = CharsetUtil.encoder(charset);
        final ByteBuffer nativeBuffer = ByteBuffer.allocate((int)(encoder.maxBytesPerChar() * length));
        encoder.encode(cbuf, nativeBuffer, true);
        final int bufferOffset = nativeBuffer.arrayOffset();
        this.value = Arrays.copyOfRange(nativeBuffer.array(), bufferOffset, bufferOffset + nativeBuffer.position());
        this.offset = 0;
        this.length = this.value.length;
    }
    
    public AsciiString(final CharSequence value) {
        this(value, 0, value.length());
    }
    
    public AsciiString(final CharSequence value, final int start, final int length) {
        if (MathUtil.isOutOfBounds(start, length, value.length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length + ") <= value.length(" + value.length() + ')');
        }
        this.value = new byte[length];
        for (int i = 0, j = start; i < length; ++i, ++j) {
            this.value[i] = c2b(value.charAt(j));
        }
        this.offset = 0;
        this.length = length;
    }
    
    public AsciiString(final CharSequence value, final Charset charset) {
        this(value, charset, 0, value.length());
    }
    
    public AsciiString(final CharSequence value, final Charset charset, final int start, final int length) {
        final CharBuffer cbuf = CharBuffer.wrap(value, start, start + length);
        final CharsetEncoder encoder = CharsetUtil.encoder(charset);
        final ByteBuffer nativeBuffer = ByteBuffer.allocate((int)(encoder.maxBytesPerChar() * length));
        encoder.encode(cbuf, nativeBuffer, true);
        final int offset = nativeBuffer.arrayOffset();
        this.value = Arrays.copyOfRange(nativeBuffer.array(), offset, offset + nativeBuffer.position());
        this.offset = 0;
        this.length = this.value.length;
    }
    
    public int forEachByte(final ByteProcessor visitor) throws Exception {
        return this.forEachByte0(0, this.length(), visitor);
    }
    
    public int forEachByte(final int index, final int length, final ByteProcessor visitor) throws Exception {
        if (MathUtil.isOutOfBounds(index, length, this.length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= index(" + index + ") <= start + length(" + length + ") <= length(" + this.length() + ')');
        }
        return this.forEachByte0(index, length, visitor);
    }
    
    private int forEachByte0(final int index, final int length, final ByteProcessor visitor) throws Exception {
        for (int len = this.offset + index + length, i = this.offset + index; i < len; ++i) {
            if (!visitor.process(this.value[i])) {
                return i - this.offset;
            }
        }
        return -1;
    }
    
    public int forEachByteDesc(final ByteProcessor visitor) throws Exception {
        return this.forEachByteDesc0(0, this.length(), visitor);
    }
    
    public int forEachByteDesc(final int index, final int length, final ByteProcessor visitor) throws Exception {
        if (MathUtil.isOutOfBounds(index, length, this.length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= index(" + index + ") <= start + length(" + length + ") <= length(" + this.length() + ')');
        }
        return this.forEachByteDesc0(index, length, visitor);
    }
    
    private int forEachByteDesc0(final int index, final int length, final ByteProcessor visitor) throws Exception {
        for (int end = this.offset + index, i = this.offset + index + length - 1; i >= end; --i) {
            if (!visitor.process(this.value[i])) {
                return i - this.offset;
            }
        }
        return -1;
    }
    
    public byte byteAt(final int index) {
        if (index < 0 || index >= this.length) {
            throw new IndexOutOfBoundsException("index: " + index + " must be in the range [0," + this.length + ")");
        }
        if (PlatformDependent.hasUnsafe()) {
            return PlatformDependent.getByte(this.value, index + this.offset);
        }
        return this.value[index + this.offset];
    }
    
    public boolean isEmpty() {
        return this.length == 0;
    }
    
    @Override
    public int length() {
        return this.length;
    }
    
    public void arrayChanged() {
        this.string = null;
        this.hash = 0;
    }
    
    public byte[] array() {
        return this.value;
    }
    
    public int arrayOffset() {
        return this.offset;
    }
    
    public boolean isEntireArrayUsed() {
        return this.offset == 0 && this.length == this.value.length;
    }
    
    public byte[] toByteArray() {
        return this.toByteArray(0, this.length());
    }
    
    public byte[] toByteArray(final int start, final int end) {
        return Arrays.copyOfRange(this.value, start + this.offset, end + this.offset);
    }
    
    public void copy(final int srcIdx, final byte[] dst, final int dstIdx, final int length) {
        if (MathUtil.isOutOfBounds(srcIdx, length, this.length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= srcIdx(" + srcIdx + ") <= srcIdx + length(" + length + ") <= srcLen(" + this.length() + ')');
        }
        System.arraycopy(this.value, srcIdx + this.offset, ObjectUtil.checkNotNull(dst, "dst"), dstIdx, length);
    }
    
    @Override
    public char charAt(final int index) {
        return b2c(this.byteAt(index));
    }
    
    public boolean contains(final CharSequence cs) {
        return this.indexOf(cs) >= 0;
    }
    
    @Override
    public int compareTo(final CharSequence string) {
        if (this == string) {
            return 0;
        }
        final int length1 = this.length();
        final int length2 = string.length();
        for (int minLength = Math.min(length1, length2), i = 0, j = this.arrayOffset(); i < minLength; ++i, ++j) {
            final int result = b2c(this.value[j]) - string.charAt(i);
            if (result != 0) {
                return result;
            }
        }
        return length1 - length2;
    }
    
    public AsciiString concat(final CharSequence string) {
        final int thisLen = this.length();
        final int thatLen = string.length();
        if (thatLen == 0) {
            return this;
        }
        if (string.getClass() == AsciiString.class) {
            final AsciiString that = (AsciiString)string;
            if (this.isEmpty()) {
                return that;
            }
            final byte[] newValue = new byte[thisLen + thatLen];
            System.arraycopy(this.value, this.arrayOffset(), newValue, 0, thisLen);
            System.arraycopy(that.value, that.arrayOffset(), newValue, thisLen, thatLen);
            return new AsciiString(newValue, false);
        }
        else {
            if (this.isEmpty()) {
                return new AsciiString(string);
            }
            final byte[] newValue2 = new byte[thisLen + thatLen];
            System.arraycopy(this.value, this.arrayOffset(), newValue2, 0, thisLen);
            for (int i = thisLen, j = 0; i < newValue2.length; ++i, ++j) {
                newValue2[i] = c2b(string.charAt(j));
            }
            return new AsciiString(newValue2, false);
        }
    }
    
    public boolean endsWith(final CharSequence suffix) {
        final int suffixLen = suffix.length();
        return this.regionMatches(this.length() - suffixLen, suffix, 0, suffixLen);
    }
    
    public boolean contentEqualsIgnoreCase(final CharSequence string) {
        if (string == null || string.length() != this.length()) {
            return false;
        }
        if (string.getClass() == AsciiString.class) {
            final AsciiString rhs = (AsciiString)string;
            for (int i = this.arrayOffset(), j = rhs.arrayOffset(); i < this.length(); ++i, ++j) {
                if (!equalsIgnoreCase(this.value[i], rhs.value[j])) {
                    return false;
                }
            }
            return true;
        }
        for (int k = this.arrayOffset(), l = 0; k < this.length(); ++k, ++l) {
            if (!equalsIgnoreCase(b2c(this.value[k]), string.charAt(l))) {
                return false;
            }
        }
        return true;
    }
    
    public char[] toCharArray() {
        return this.toCharArray(0, this.length());
    }
    
    public char[] toCharArray(final int start, final int end) {
        final int length = end - start;
        if (length == 0) {
            return EmptyArrays.EMPTY_CHARS;
        }
        if (MathUtil.isOutOfBounds(start, length, this.length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= srcIdx + length(" + length + ") <= srcLen(" + this.length() + ')');
        }
        final char[] buffer = new char[length];
        for (int i = 0, j = start + this.arrayOffset(); i < length; ++i, ++j) {
            buffer[i] = b2c(this.value[j]);
        }
        return buffer;
    }
    
    public void copy(final int srcIdx, final char[] dst, final int dstIdx, final int length) {
        if (dst == null) {
            throw new NullPointerException("dst");
        }
        if (MathUtil.isOutOfBounds(srcIdx, length, this.length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= srcIdx(" + srcIdx + ") <= srcIdx + length(" + length + ") <= srcLen(" + this.length() + ')');
        }
        for (int dstEnd = dstIdx + length, i = dstIdx, j = srcIdx + this.arrayOffset(); i < dstEnd; ++i, ++j) {
            dst[i] = b2c(this.value[j]);
        }
    }
    
    public AsciiString subSequence(final int start) {
        return this.subSequence(start, this.length());
    }
    
    @Override
    public AsciiString subSequence(final int start, final int end) {
        return this.subSequence(start, end, true);
    }
    
    public AsciiString subSequence(final int start, final int end, final boolean copy) {
        if (MathUtil.isOutOfBounds(start, end - start, this.length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= end (" + end + ") <= length(" + this.length() + ')');
        }
        if (start == 0 && end == this.length()) {
            return this;
        }
        if (end == start) {
            return AsciiString.EMPTY_STRING;
        }
        return new AsciiString(this.value, start + this.offset, end - start, copy);
    }
    
    public int indexOf(final CharSequence string) {
        return this.indexOf(string, 0);
    }
    
    public int indexOf(final CharSequence subString, int start) {
        if (start < 0) {
            start = 0;
        }
        final int thisLen = this.length();
        final int subCount = subString.length();
        if (subCount <= 0) {
            return (start < thisLen) ? start : thisLen;
        }
        if (subCount > thisLen - start) {
            return -1;
        }
        final char firstChar = subString.charAt(0);
        if (firstChar > '\u00ff') {
            return -1;
        }
        final ByteProcessor IndexOfVisitor = new ByteProcessor.IndexOfProcessor((byte)firstChar);
        try {
            while (true) {
                final int i = this.forEachByte(start, thisLen - start, IndexOfVisitor);
                if (i == -1 || subCount + i > thisLen) {
                    return -1;
                }
                int o1 = i;
                int o2 = 0;
                while (++o2 < subCount && b2c(this.value[++o1 + this.arrayOffset()]) == subString.charAt(o2)) {}
                if (o2 == subCount) {
                    return i;
                }
                start = i + 1;
            }
        }
        catch (final Exception e) {
            PlatformDependent.throwException(e);
            return -1;
        }
    }
    
    public int indexOf(final char ch, int start) {
        if (start < 0) {
            start = 0;
        }
        final int thisLen = this.length();
        if (ch > '\u00ff') {
            return -1;
        }
        try {
            return this.forEachByte(start, thisLen - start, new ByteProcessor.IndexOfProcessor((byte)ch));
        }
        catch (final Exception e) {
            PlatformDependent.throwException(e);
            return -1;
        }
    }
    
    public int lastIndexOf(final CharSequence string) {
        return this.lastIndexOf(string, this.length());
    }
    
    public int lastIndexOf(final CharSequence subString, int start) {
        final int thisLen = this.length();
        final int subCount = subString.length();
        if (subCount > thisLen || start < 0) {
            return -1;
        }
        if (subCount <= 0) {
            return (start < thisLen) ? start : thisLen;
        }
        start = Math.min(start, thisLen - subCount);
        final char firstChar = subString.charAt(0);
        if (firstChar > '\u00ff') {
            return -1;
        }
        final ByteProcessor IndexOfVisitor = new ByteProcessor.IndexOfProcessor((byte)firstChar);
        try {
            while (true) {
                final int i = this.forEachByteDesc(start, thisLen - start, IndexOfVisitor);
                if (i == -1) {
                    return -1;
                }
                int o1 = i;
                int o2 = 0;
                while (++o2 < subCount && b2c(this.value[++o1 + this.arrayOffset()]) == subString.charAt(o2)) {}
                if (o2 == subCount) {
                    return i;
                }
                start = i - 1;
            }
        }
        catch (final Exception e) {
            PlatformDependent.throwException(e);
            return -1;
        }
    }
    
    public boolean regionMatches(final int thisStart, final CharSequence string, final int start, final int length) {
        if (string == null) {
            throw new NullPointerException("string");
        }
        if (start < 0 || string.length() - start < length) {
            return false;
        }
        final int thisLen = this.length();
        if (thisStart < 0 || thisLen - thisStart < length) {
            return false;
        }
        if (length <= 0) {
            return true;
        }
        for (int thatEnd = start + length, i = start, j = thisStart + this.arrayOffset(); i < thatEnd; ++i, ++j) {
            if (b2c(this.value[j]) != string.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean regionMatches(final boolean ignoreCase, int thisStart, final CharSequence string, int start, final int length) {
        if (!ignoreCase) {
            return this.regionMatches(thisStart, string, start, length);
        }
        if (string == null) {
            throw new NullPointerException("string");
        }
        final int thisLen = this.length();
        if (thisStart < 0 || length > thisLen - thisStart) {
            return false;
        }
        if (start < 0 || length > string.length() - start) {
            return false;
        }
        thisStart += this.arrayOffset();
        final int thisEnd = thisStart + length;
        while (thisStart < thisEnd) {
            if (!equalsIgnoreCase(b2c(this.value[thisStart++]), string.charAt(start++))) {
                return false;
            }
        }
        return true;
    }
    
    public AsciiString replace(final char oldChar, final char newChar) {
        if (oldChar > '\u00ff') {
            return this;
        }
        final byte oldCharByte = c2b(oldChar);
        int index;
        try {
            index = this.forEachByte(new ByteProcessor.IndexOfProcessor(oldCharByte));
        }
        catch (final Exception e) {
            PlatformDependent.throwException(e);
            return this;
        }
        if (index == -1) {
            return this;
        }
        final byte newCharByte = c2b(newChar);
        final byte[] buffer = new byte[this.length()];
        for (int i = 0, j = this.arrayOffset(); i < buffer.length; ++i, ++j) {
            byte b = this.value[j];
            if (b == oldCharByte) {
                b = newCharByte;
            }
            buffer[i] = b;
        }
        return new AsciiString(buffer, false);
    }
    
    public boolean startsWith(final CharSequence prefix) {
        return this.startsWith(prefix, 0);
    }
    
    public boolean startsWith(final CharSequence prefix, final int start) {
        return this.regionMatches(start, prefix, 0, prefix.length());
    }
    
    public AsciiString toLowerCase() {
        boolean lowercased = true;
        for (int len = this.length() + this.arrayOffset(), i = this.arrayOffset(); i < len; ++i) {
            final byte b = this.value[i];
            if (b >= 65 && b <= 90) {
                lowercased = false;
                break;
            }
        }
        if (lowercased) {
            return this;
        }
        final byte[] newValue = new byte[this.length()];
        for (int i = 0, j = this.arrayOffset(); i < newValue.length; ++i, ++j) {
            newValue[i] = toLowerCase(this.value[j]);
        }
        return new AsciiString(newValue, false);
    }
    
    public AsciiString toUpperCase() {
        boolean uppercased = true;
        for (int len = this.length() + this.arrayOffset(), i = this.arrayOffset(); i < len; ++i) {
            final byte b = this.value[i];
            if (b >= 97 && b <= 122) {
                uppercased = false;
                break;
            }
        }
        if (uppercased) {
            return this;
        }
        final byte[] newValue = new byte[this.length()];
        for (int i = 0, j = this.arrayOffset(); i < newValue.length; ++i, ++j) {
            newValue[i] = toUpperCase(this.value[j]);
        }
        return new AsciiString(newValue, false);
    }
    
    public AsciiString trim() {
        int start;
        int end;
        int last;
        for (start = this.arrayOffset(), last = (end = this.arrayOffset() + this.length() - 1); start <= end && this.value[start] <= 32; ++start) {}
        while (end >= start && this.value[end] <= 32) {
            --end;
        }
        if (start == 0 && end == last) {
            return this;
        }
        return new AsciiString(this.value, start, end - start + 1, false);
    }
    
    public boolean contentEquals(final CharSequence a) {
        if (a == null || a.length() != this.length()) {
            return false;
        }
        if (a.getClass() == AsciiString.class) {
            return this.equals(a);
        }
        int i = this.arrayOffset();
        for (int j = 0; j < a.length(); ++j) {
            if (b2c(this.value[i]) != a.charAt(j)) {
                return false;
            }
            ++i;
        }
        return true;
    }
    
    public boolean matches(final String expr) {
        return Pattern.matches(expr, this);
    }
    
    public AsciiString[] split(final String expr, final int max) {
        return toAsciiStringArray(Pattern.compile(expr).split(this, max));
    }
    
    public AsciiString[] split(final char delim) {
        final List<AsciiString> res = (List<AsciiString>)InternalThreadLocalMap.get().arrayList();
        int start = 0;
        final int length = this.length();
        for (int i = start; i < length; ++i) {
            if (this.charAt(i) == delim) {
                if (start == i) {
                    res.add(AsciiString.EMPTY_STRING);
                }
                else {
                    res.add(new AsciiString(this.value, start + this.arrayOffset(), i - start, false));
                }
                start = i + 1;
            }
        }
        if (start == 0) {
            res.add(this);
        }
        else if (start != length) {
            res.add(new AsciiString(this.value, start + this.arrayOffset(), length - start, false));
        }
        else {
            for (int i = res.size() - 1; i >= 0 && res.get(i).isEmpty(); --i) {
                res.remove(i);
            }
        }
        return res.toArray(new AsciiString[res.size()]);
    }
    
    @Override
    public int hashCode() {
        if (this.hash == 0) {
            this.hash = PlatformDependent.hashCodeAscii(this.value, this.offset, this.length);
        }
        return this.hash;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || obj.getClass() != AsciiString.class) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        final AsciiString other = (AsciiString)obj;
        return this.length() == other.length() && this.hashCode() == other.hashCode() && PlatformDependent.equals(this.array(), this.arrayOffset(), other.array(), other.arrayOffset(), this.length());
    }
    
    @Override
    public String toString() {
        if (this.string != null) {
            return this.string;
        }
        return this.string = this.toString(0);
    }
    
    public String toString(final int start) {
        return this.toString(start, this.length());
    }
    
    public String toString(final int start, final int end) {
        final int length = end - start;
        if (length == 0) {
            return "";
        }
        if (MathUtil.isOutOfBounds(start, length, this.length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= srcIdx + length(" + length + ") <= srcLen(" + this.length() + ')');
        }
        final String str = new String(this.value, 0, start + this.offset, length);
        return str;
    }
    
    public boolean parseBoolean() {
        return this.length >= 1 && this.value[this.offset] != 0;
    }
    
    public char parseChar() {
        return this.parseChar(0);
    }
    
    public char parseChar(final int start) {
        if (start + 1 >= this.length()) {
            throw new IndexOutOfBoundsException("2 bytes required to convert to character. index " + start + " would go out of bounds.");
        }
        final int startWithOffset = start + this.offset;
        return (char)(b2c(this.value[startWithOffset]) << 8 | b2c(this.value[startWithOffset + 1]));
    }
    
    public short parseShort() {
        return this.parseShort(0, this.length(), 10);
    }
    
    public short parseShort(final int radix) {
        return this.parseShort(0, this.length(), radix);
    }
    
    public short parseShort(final int start, final int end) {
        return this.parseShort(start, end, 10);
    }
    
    public short parseShort(final int start, final int end, final int radix) {
        final int intValue = this.parseInt(start, end, radix);
        final short result = (short)intValue;
        if (result != intValue) {
            throw new NumberFormatException(this.subSequence(start, end, false).toString());
        }
        return result;
    }
    
    public int parseInt() {
        return this.parseInt(0, this.length(), 10);
    }
    
    public int parseInt(final int radix) {
        return this.parseInt(0, this.length(), radix);
    }
    
    public int parseInt(final int start, final int end) {
        return this.parseInt(start, end, 10);
    }
    
    public int parseInt(final int start, final int end, final int radix) {
        if (radix < 2 || radix > 36) {
            throw new NumberFormatException();
        }
        if (start == end) {
            throw new NumberFormatException();
        }
        int i = start;
        final boolean negative = this.byteAt(i) == 45;
        if (negative && ++i == end) {
            throw new NumberFormatException(this.subSequence(start, end, false).toString());
        }
        return this.parseInt(i, end, radix, negative);
    }
    
    private int parseInt(final int start, final int end, final int radix, final boolean negative) {
        final int max = Integer.MIN_VALUE / radix;
        int result = 0;
        int currOffset = start;
        while (currOffset < end) {
            final int digit = Character.digit((char)(this.value[currOffset++ + this.offset] & 0xFF), radix);
            if (digit == -1) {
                throw new NumberFormatException(this.subSequence(start, end, false).toString());
            }
            if (max > result) {
                throw new NumberFormatException(this.subSequence(start, end, false).toString());
            }
            final int next = result * radix - digit;
            if (next > result) {
                throw new NumberFormatException(this.subSequence(start, end, false).toString());
            }
            result = next;
        }
        if (!negative) {
            result = -result;
            if (result < 0) {
                throw new NumberFormatException(this.subSequence(start, end, false).toString());
            }
        }
        return result;
    }
    
    public long parseLong() {
        return this.parseLong(0, this.length(), 10);
    }
    
    public long parseLong(final int radix) {
        return this.parseLong(0, this.length(), radix);
    }
    
    public long parseLong(final int start, final int end) {
        return this.parseLong(start, end, 10);
    }
    
    public long parseLong(final int start, final int end, final int radix) {
        if (radix < 2 || radix > 36) {
            throw new NumberFormatException();
        }
        if (start == end) {
            throw new NumberFormatException();
        }
        int i = start;
        final boolean negative = this.byteAt(i) == 45;
        if (negative && ++i == end) {
            throw new NumberFormatException(this.subSequence(start, end, false).toString());
        }
        return this.parseLong(i, end, radix, negative);
    }
    
    private long parseLong(final int start, final int end, final int radix, final boolean negative) {
        final long max = Long.MIN_VALUE / radix;
        long result = 0L;
        int currOffset = start;
        while (currOffset < end) {
            final int digit = Character.digit((char)(this.value[currOffset++ + this.offset] & 0xFF), radix);
            if (digit == -1) {
                throw new NumberFormatException(this.subSequence(start, end, false).toString());
            }
            if (max > result) {
                throw new NumberFormatException(this.subSequence(start, end, false).toString());
            }
            final long next = result * radix - digit;
            if (next > result) {
                throw new NumberFormatException(this.subSequence(start, end, false).toString());
            }
            result = next;
        }
        if (!negative) {
            result = -result;
            if (result < 0L) {
                throw new NumberFormatException(this.subSequence(start, end, false).toString());
            }
        }
        return result;
    }
    
    public float parseFloat() {
        return this.parseFloat(0, this.length());
    }
    
    public float parseFloat(final int start, final int end) {
        return Float.parseFloat(this.toString(start, end));
    }
    
    public double parseDouble() {
        return this.parseDouble(0, this.length());
    }
    
    public double parseDouble(final int start, final int end) {
        return Double.parseDouble(this.toString(start, end));
    }
    
    public static AsciiString of(final CharSequence string) {
        return (AsciiString)((string.getClass() == AsciiString.class) ? string : new AsciiString(string));
    }
    
    public static int hashCode(final CharSequence value) {
        if (value == null) {
            return 0;
        }
        if (value.getClass() == AsciiString.class) {
            return value.hashCode();
        }
        return PlatformDependent.hashCodeAscii(value);
    }
    
    public static boolean contains(final CharSequence a, final CharSequence b) {
        return contains(a, b, DefaultCharEqualityComparator.INSTANCE);
    }
    
    public static boolean containsIgnoreCase(final CharSequence a, final CharSequence b) {
        return contains(a, b, AsciiCaseInsensitiveCharEqualityComparator.INSTANCE);
    }
    
    public static boolean contentEqualsIgnoreCase(final CharSequence a, final CharSequence b) {
        if (a == null || b == null) {
            return a == b;
        }
        if (a.getClass() == AsciiString.class) {
            return ((AsciiString)a).contentEqualsIgnoreCase(b);
        }
        if (b.getClass() == AsciiString.class) {
            return ((AsciiString)b).contentEqualsIgnoreCase(a);
        }
        if (a.length() != b.length()) {
            return false;
        }
        for (int i = 0, j = 0; i < a.length(); ++i, ++j) {
            if (!equalsIgnoreCase(a.charAt(i), b.charAt(j))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean containsContentEqualsIgnoreCase(final Collection<CharSequence> collection, final CharSequence value) {
        for (final CharSequence v : collection) {
            if (contentEqualsIgnoreCase(value, v)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean containsAllContentEqualsIgnoreCase(final Collection<CharSequence> a, final Collection<CharSequence> b) {
        for (final CharSequence v : b) {
            if (!containsContentEqualsIgnoreCase(a, v)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean contentEquals(final CharSequence a, final CharSequence b) {
        if (a == null || b == null) {
            return a == b;
        }
        if (a.getClass() == AsciiString.class) {
            return ((AsciiString)a).contentEquals(b);
        }
        if (b.getClass() == AsciiString.class) {
            return ((AsciiString)b).contentEquals(a);
        }
        if (a.length() != b.length()) {
            return false;
        }
        for (int i = 0; i < a.length(); ++i) {
            if (a.charAt(i) != b.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    
    private static AsciiString[] toAsciiStringArray(final String[] jdkResult) {
        final AsciiString[] res = new AsciiString[jdkResult.length];
        for (int i = 0; i < jdkResult.length; ++i) {
            res[i] = new AsciiString(jdkResult[i]);
        }
        return res;
    }
    
    private static boolean contains(final CharSequence a, final CharSequence b, final CharEqualityComparator cmp) {
        if (a == null || b == null || a.length() < b.length()) {
            return false;
        }
        if (b.length() == 0) {
            return true;
        }
        int bStart = 0;
        for (int i = 0; i < a.length(); ++i) {
            if (cmp.equals(b.charAt(bStart), a.charAt(i))) {
                if (++bStart == b.length()) {
                    return true;
                }
            }
            else {
                if (a.length() - i < b.length()) {
                    return false;
                }
                bStart = 0;
            }
        }
        return false;
    }
    
    private static boolean regionMatchesCharSequences(final CharSequence cs, final int csStart, final CharSequence string, final int start, final int length, final CharEqualityComparator charEqualityComparator) {
        if (csStart < 0 || length > cs.length() - csStart) {
            return false;
        }
        if (start < 0 || length > string.length() - start) {
            return false;
        }
        int csIndex = csStart;
        final int csEnd = csIndex + length;
        int stringIndex = start;
        while (csIndex < csEnd) {
            final char c1 = cs.charAt(csIndex++);
            final char c2 = string.charAt(stringIndex++);
            if (!charEqualityComparator.equals(c1, c2)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int csStart, final CharSequence string, final int start, final int length) {
        if (cs == null || string == null) {
            return false;
        }
        if (cs instanceof String && string instanceof String) {
            return ((String)cs).regionMatches(ignoreCase, csStart, (String)string, start, length);
        }
        if (cs instanceof AsciiString) {
            return ((AsciiString)cs).regionMatches(ignoreCase, csStart, string, start, length);
        }
        return regionMatchesCharSequences(cs, csStart, string, start, length, ignoreCase ? GeneralCaseInsensitiveCharEqualityComparator.INSTANCE : DefaultCharEqualityComparator.INSTANCE);
    }
    
    public static boolean regionMatchesAscii(final CharSequence cs, final boolean ignoreCase, final int csStart, final CharSequence string, final int start, final int length) {
        if (cs == null || string == null) {
            return false;
        }
        if (!ignoreCase && cs instanceof String && string instanceof String) {
            return ((String)cs).regionMatches(false, csStart, (String)string, start, length);
        }
        if (cs instanceof AsciiString) {
            return ((AsciiString)cs).regionMatches(ignoreCase, csStart, string, start, length);
        }
        return regionMatchesCharSequences(cs, csStart, string, start, length, ignoreCase ? AsciiCaseInsensitiveCharEqualityComparator.INSTANCE : DefaultCharEqualityComparator.INSTANCE);
    }
    
    public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        }
        if (startPos < 0) {
            startPos = 0;
        }
        final int searchStrLen = searchStr.length();
        final int endLimit = str.length() - searchStrLen + 1;
        if (startPos > endLimit) {
            return -1;
        }
        if (searchStrLen == 0) {
            return startPos;
        }
        for (int i = startPos; i < endLimit; ++i) {
            if (regionMatches(str, true, i, searchStr, 0, searchStrLen)) {
                return i;
            }
        }
        return -1;
    }
    
    public static int indexOfIgnoreCaseAscii(final CharSequence str, final CharSequence searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        }
        if (startPos < 0) {
            startPos = 0;
        }
        final int searchStrLen = searchStr.length();
        final int endLimit = str.length() - searchStrLen + 1;
        if (startPos > endLimit) {
            return -1;
        }
        if (searchStrLen == 0) {
            return startPos;
        }
        for (int i = startPos; i < endLimit; ++i) {
            if (regionMatchesAscii(str, true, i, searchStr, 0, searchStrLen)) {
                return i;
            }
        }
        return -1;
    }
    
    public static int indexOf(final CharSequence cs, final char searchChar, int start) {
        if (cs instanceof String) {
            return ((String)cs).indexOf(searchChar, start);
        }
        if (cs instanceof AsciiString) {
            return ((AsciiString)cs).indexOf(searchChar, start);
        }
        if (cs == null) {
            return -1;
        }
        final int sz = cs.length();
        if (start < 0) {
            start = 0;
        }
        for (int i = start; i < sz; ++i) {
            if (cs.charAt(i) == searchChar) {
                return i;
            }
        }
        return -1;
    }
    
    private static boolean equalsIgnoreCase(final byte a, final byte b) {
        return a == b || toLowerCase(a) == toLowerCase(b);
    }
    
    private static boolean equalsIgnoreCase(final char a, final char b) {
        return a == b || toLowerCase(a) == toLowerCase(b);
    }
    
    private static byte toLowerCase(final byte b) {
        return isUpperCase(b) ? ((byte)(b + 32)) : b;
    }
    
    private static char toLowerCase(final char c) {
        return isUpperCase(c) ? ((char)(c + ' ')) : c;
    }
    
    private static byte toUpperCase(final byte b) {
        return isLowerCase(b) ? ((byte)(b - 32)) : b;
    }
    
    private static boolean isLowerCase(final byte value) {
        return value >= 97 && value <= 122;
    }
    
    public static boolean isUpperCase(final byte value) {
        return value >= 65 && value <= 90;
    }
    
    public static boolean isUpperCase(final char value) {
        return value >= 'A' && value <= 'Z';
    }
    
    public static byte c2b(final char c) {
        return (byte)((c > '\u00ff') ? '?' : c);
    }
    
    public static char b2c(final byte b) {
        return (char)(b & 0xFF);
    }
    
    static {
        EMPTY_STRING = new AsciiString("");
        CASE_INSENSITIVE_HASHER = new HashingStrategy<CharSequence>() {
            @Override
            public int hashCode(final CharSequence o) {
                return AsciiString.hashCode(o);
            }
            
            @Override
            public boolean equals(final CharSequence a, final CharSequence b) {
                return AsciiString.contentEqualsIgnoreCase(a, b);
            }
        };
        CASE_SENSITIVE_HASHER = new HashingStrategy<CharSequence>() {
            @Override
            public int hashCode(final CharSequence o) {
                return AsciiString.hashCode(o);
            }
            
            @Override
            public boolean equals(final CharSequence a, final CharSequence b) {
                return AsciiString.contentEquals(a, b);
            }
        };
    }
    
    private static final class DefaultCharEqualityComparator implements CharEqualityComparator
    {
        static final DefaultCharEqualityComparator INSTANCE;
        
        @Override
        public boolean equals(final char a, final char b) {
            return a == b;
        }
        
        static {
            INSTANCE = new DefaultCharEqualityComparator();
        }
    }
    
    private static final class AsciiCaseInsensitiveCharEqualityComparator implements CharEqualityComparator
    {
        static final AsciiCaseInsensitiveCharEqualityComparator INSTANCE;
        
        @Override
        public boolean equals(final char a, final char b) {
            return equalsIgnoreCase(a, b);
        }
        
        static {
            INSTANCE = new AsciiCaseInsensitiveCharEqualityComparator();
        }
    }
    
    private static final class GeneralCaseInsensitiveCharEqualityComparator implements CharEqualityComparator
    {
        static final GeneralCaseInsensitiveCharEqualityComparator INSTANCE;
        
        @Override
        public boolean equals(final char a, final char b) {
            return Character.toUpperCase(a) == Character.toUpperCase(b) || Character.toLowerCase(a) == Character.toLowerCase(b);
        }
        
        static {
            INSTANCE = new GeneralCaseInsensitiveCharEqualityComparator();
        }
    }
    
    private interface CharEqualityComparator
    {
        boolean equals(final char p0, final char p1);
    }
}
