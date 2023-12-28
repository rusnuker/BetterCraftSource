// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import io.netty.util.Recycler;
import java.util.Locale;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.charset.CodingErrorAction;
import io.netty.util.internal.ObjectUtil;
import java.util.Arrays;
import io.netty.util.internal.MathUtil;
import io.netty.util.internal.PlatformDependent;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import io.netty.util.AsciiString;
import io.netty.util.internal.StringUtil;
import io.netty.util.CharsetUtil;
import java.nio.ByteOrder;
import io.netty.util.ByteProcessor;
import java.nio.CharBuffer;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.logging.InternalLogger;

public final class ByteBufUtil
{
    private static final InternalLogger logger;
    private static final FastThreadLocal<CharBuffer> CHAR_BUFFERS;
    private static final byte WRITE_UTF_UNKNOWN = 63;
    private static final int MAX_CHAR_BUFFER_SIZE;
    private static final int THREAD_LOCAL_BUFFER_SIZE;
    private static final int MAX_BYTES_PER_CHAR_UTF8;
    static final ByteBufAllocator DEFAULT_ALLOCATOR;
    private static final ByteProcessor FIND_NON_ASCII;
    
    public static String hexDump(final ByteBuf buffer) {
        return hexDump(buffer, buffer.readerIndex(), buffer.readableBytes());
    }
    
    public static String hexDump(final ByteBuf buffer, final int fromIndex, final int length) {
        return hexDump(buffer, fromIndex, length);
    }
    
    public static String hexDump(final byte[] array) {
        return hexDump(array, 0, array.length);
    }
    
    public static String hexDump(final byte[] array, final int fromIndex, final int length) {
        return hexDump(array, fromIndex, length);
    }
    
    public static int hashCode(final ByteBuf buffer) {
        final int aLen = buffer.readableBytes();
        final int intCount = aLen >>> 2;
        final int byteCount = aLen & 0x3;
        int hashCode = 1;
        int arrayIndex = buffer.readerIndex();
        if (buffer.order() == ByteOrder.BIG_ENDIAN) {
            for (int i = intCount; i > 0; --i) {
                hashCode = 31 * hashCode + buffer.getInt(arrayIndex);
                arrayIndex += 4;
            }
        }
        else {
            for (int i = intCount; i > 0; --i) {
                hashCode = 31 * hashCode + swapInt(buffer.getInt(arrayIndex));
                arrayIndex += 4;
            }
        }
        for (int i = byteCount; i > 0; --i) {
            hashCode = 31 * hashCode + buffer.getByte(arrayIndex++);
        }
        if (hashCode == 0) {
            hashCode = 1;
        }
        return hashCode;
    }
    
    public static int indexOf(final ByteBuf needle, final ByteBuf haystack) {
        for (int attempts = haystack.readableBytes() - needle.readableBytes() + 1, i = 0; i < attempts; ++i) {
            if (equals(needle, needle.readerIndex(), haystack, haystack.readerIndex() + i, needle.readableBytes())) {
                return haystack.readerIndex() + i;
            }
        }
        return -1;
    }
    
    public static boolean equals(final ByteBuf a, int aStartIndex, final ByteBuf b, int bStartIndex, final int length) {
        if (aStartIndex < 0 || bStartIndex < 0 || length < 0) {
            throw new IllegalArgumentException("All indexes and lengths must be non-negative");
        }
        if (a.writerIndex() - length < aStartIndex || b.writerIndex() - length < bStartIndex) {
            return false;
        }
        final int longCount = length >>> 3;
        final int byteCount = length & 0x7;
        if (a.order() == b.order()) {
            for (int i = longCount; i > 0; --i) {
                if (a.getLong(aStartIndex) != b.getLong(bStartIndex)) {
                    return false;
                }
                aStartIndex += 8;
                bStartIndex += 8;
            }
        }
        else {
            for (int i = longCount; i > 0; --i) {
                if (a.getLong(aStartIndex) != swapLong(b.getLong(bStartIndex))) {
                    return false;
                }
                aStartIndex += 8;
                bStartIndex += 8;
            }
        }
        for (int i = byteCount; i > 0; --i) {
            if (a.getByte(aStartIndex) != b.getByte(bStartIndex)) {
                return false;
            }
            ++aStartIndex;
            ++bStartIndex;
        }
        return true;
    }
    
    public static boolean equals(final ByteBuf bufferA, final ByteBuf bufferB) {
        final int aLen = bufferA.readableBytes();
        return aLen == bufferB.readableBytes() && equals(bufferA, bufferA.readerIndex(), bufferB, bufferB.readerIndex(), aLen);
    }
    
    public static int compare(final ByteBuf bufferA, final ByteBuf bufferB) {
        final int aLen = bufferA.readableBytes();
        final int bLen = bufferB.readableBytes();
        final int minLength = Math.min(aLen, bLen);
        final int uintCount = minLength >>> 2;
        final int byteCount = minLength & 0x3;
        int aIndex = bufferA.readerIndex();
        int bIndex = bufferB.readerIndex();
        if (uintCount > 0) {
            final boolean bufferAIsBigEndian = bufferA.order() == ByteOrder.BIG_ENDIAN;
            final int uintCountIncrement = uintCount << 2;
            long res;
            if (bufferA.order() == bufferB.order()) {
                res = (bufferAIsBigEndian ? compareUintBigEndian(bufferA, bufferB, aIndex, bIndex, uintCountIncrement) : compareUintLittleEndian(bufferA, bufferB, aIndex, bIndex, uintCountIncrement));
            }
            else {
                res = (bufferAIsBigEndian ? compareUintBigEndianA(bufferA, bufferB, aIndex, bIndex, uintCountIncrement) : compareUintBigEndianB(bufferA, bufferB, aIndex, bIndex, uintCountIncrement));
            }
            if (res != 0L) {
                return (int)Math.min(2147483647L, Math.max(-2147483648L, res));
            }
            aIndex += uintCountIncrement;
            bIndex += uintCountIncrement;
        }
        for (int aEnd = aIndex + byteCount; aIndex < aEnd; ++aIndex, ++bIndex) {
            final int comp = bufferA.getUnsignedByte(aIndex) - bufferB.getUnsignedByte(bIndex);
            if (comp != 0) {
                return comp;
            }
        }
        return aLen - bLen;
    }
    
    private static long compareUintBigEndian(final ByteBuf bufferA, final ByteBuf bufferB, int aIndex, int bIndex, final int uintCountIncrement) {
        for (int aEnd = aIndex + uintCountIncrement; aIndex < aEnd; aIndex += 4, bIndex += 4) {
            final long comp = bufferA.getUnsignedInt(aIndex) - bufferB.getUnsignedInt(bIndex);
            if (comp != 0L) {
                return comp;
            }
        }
        return 0L;
    }
    
    private static long compareUintLittleEndian(final ByteBuf bufferA, final ByteBuf bufferB, int aIndex, int bIndex, final int uintCountIncrement) {
        for (int aEnd = aIndex + uintCountIncrement; aIndex < aEnd; aIndex += 4, bIndex += 4) {
            final long comp = bufferA.getUnsignedIntLE(aIndex) - bufferB.getUnsignedIntLE(bIndex);
            if (comp != 0L) {
                return comp;
            }
        }
        return 0L;
    }
    
    private static long compareUintBigEndianA(final ByteBuf bufferA, final ByteBuf bufferB, int aIndex, int bIndex, final int uintCountIncrement) {
        for (int aEnd = aIndex + uintCountIncrement; aIndex < aEnd; aIndex += 4, bIndex += 4) {
            final long comp = bufferA.getUnsignedInt(aIndex) - bufferB.getUnsignedIntLE(bIndex);
            if (comp != 0L) {
                return comp;
            }
        }
        return 0L;
    }
    
    private static long compareUintBigEndianB(final ByteBuf bufferA, final ByteBuf bufferB, int aIndex, int bIndex, final int uintCountIncrement) {
        for (int aEnd = aIndex + uintCountIncrement; aIndex < aEnd; aIndex += 4, bIndex += 4) {
            final long comp = bufferA.getUnsignedIntLE(aIndex) - bufferB.getUnsignedInt(bIndex);
            if (comp != 0L) {
                return comp;
            }
        }
        return 0L;
    }
    
    public static int indexOf(final ByteBuf buffer, final int fromIndex, final int toIndex, final byte value) {
        if (fromIndex <= toIndex) {
            return firstIndexOf(buffer, fromIndex, toIndex, value);
        }
        return lastIndexOf(buffer, fromIndex, toIndex, value);
    }
    
    public static short swapShort(final short value) {
        return Short.reverseBytes(value);
    }
    
    public static int swapMedium(final int value) {
        int swapped = (value << 16 & 0xFF0000) | (value & 0xFF00) | (value >>> 16 & 0xFF);
        if ((swapped & 0x800000) != 0x0) {
            swapped |= 0xFF000000;
        }
        return swapped;
    }
    
    public static int swapInt(final int value) {
        return Integer.reverseBytes(value);
    }
    
    public static long swapLong(final long value) {
        return Long.reverseBytes(value);
    }
    
    public static ByteBuf readBytes(final ByteBufAllocator alloc, final ByteBuf buffer, final int length) {
        boolean release = true;
        final ByteBuf dst = alloc.buffer(length);
        try {
            buffer.readBytes(dst);
            release = false;
            return dst;
        }
        finally {
            if (release) {
                dst.release();
            }
        }
    }
    
    private static int firstIndexOf(final ByteBuf buffer, int fromIndex, final int toIndex, final byte value) {
        fromIndex = Math.max(fromIndex, 0);
        if (fromIndex >= toIndex || buffer.capacity() == 0) {
            return -1;
        }
        return buffer.forEachByte(fromIndex, toIndex - fromIndex, new ByteProcessor.IndexOfProcessor(value));
    }
    
    private static int lastIndexOf(final ByteBuf buffer, int fromIndex, final int toIndex, final byte value) {
        fromIndex = Math.min(fromIndex, buffer.capacity());
        if (fromIndex < 0 || buffer.capacity() == 0) {
            return -1;
        }
        return buffer.forEachByteDesc(toIndex, fromIndex - toIndex, new ByteProcessor.IndexOfProcessor(value));
    }
    
    public static ByteBuf writeUtf8(final ByteBufAllocator alloc, final CharSequence seq) {
        final ByteBuf buf = alloc.buffer(utf8MaxBytes(seq));
        writeUtf8(buf, seq);
        return buf;
    }
    
    public static int writeUtf8(ByteBuf buf, final CharSequence seq) {
        final int len = seq.length();
        buf.ensureWritable(utf8MaxBytes(seq));
        while (!(buf instanceof AbstractByteBuf)) {
            if (!(buf instanceof WrappedByteBuf)) {
                final byte[] bytes = seq.toString().getBytes(CharsetUtil.UTF_8);
                buf.writeBytes(bytes);
                return bytes.length;
            }
            buf = buf.unwrap();
        }
        final AbstractByteBuf byteBuf = (AbstractByteBuf)buf;
        final int written = writeUtf8(byteBuf, byteBuf.writerIndex, seq, len);
        final AbstractByteBuf abstractByteBuf = byteBuf;
        abstractByteBuf.writerIndex += written;
        return written;
    }
    
    static int writeUtf8(final AbstractByteBuf buffer, int writerIndex, final CharSequence seq, final int len) {
        final int oldWriterIndex = writerIndex;
        for (int i = 0; i < len; ++i) {
            final char c = seq.charAt(i);
            if (c < '\u0080') {
                buffer._setByte(writerIndex++, (byte)c);
            }
            else if (c < '\u0800') {
                buffer._setByte(writerIndex++, (byte)(0xC0 | c >> 6));
                buffer._setByte(writerIndex++, (byte)(0x80 | (c & '?')));
            }
            else if (StringUtil.isSurrogate(c)) {
                if (!Character.isHighSurrogate(c)) {
                    buffer._setByte(writerIndex++, 63);
                }
                else {
                    char c2;
                    try {
                        c2 = seq.charAt(++i);
                    }
                    catch (final IndexOutOfBoundsException e) {
                        buffer._setByte(writerIndex++, 63);
                        break;
                    }
                    if (!Character.isLowSurrogate(c2)) {
                        buffer._setByte(writerIndex++, 63);
                        buffer._setByte(writerIndex++, Character.isHighSurrogate(c2) ? '?' : c2);
                    }
                    else {
                        final int codePoint = Character.toCodePoint(c, c2);
                        buffer._setByte(writerIndex++, (byte)(0xF0 | codePoint >> 18));
                        buffer._setByte(writerIndex++, (byte)(0x80 | (codePoint >> 12 & 0x3F)));
                        buffer._setByte(writerIndex++, (byte)(0x80 | (codePoint >> 6 & 0x3F)));
                        buffer._setByte(writerIndex++, (byte)(0x80 | (codePoint & 0x3F)));
                    }
                }
            }
            else {
                buffer._setByte(writerIndex++, (byte)(0xE0 | c >> 12));
                buffer._setByte(writerIndex++, (byte)(0x80 | (c >> 6 & 0x3F)));
                buffer._setByte(writerIndex++, (byte)(0x80 | (c & '?')));
            }
        }
        return writerIndex - oldWriterIndex;
    }
    
    public static int utf8MaxBytes(final CharSequence seq) {
        return seq.length() * ByteBufUtil.MAX_BYTES_PER_CHAR_UTF8;
    }
    
    public static ByteBuf writeAscii(final ByteBufAllocator alloc, final CharSequence seq) {
        final ByteBuf buf = alloc.buffer(seq.length());
        writeAscii(buf, seq);
        return buf;
    }
    
    public static int writeAscii(ByteBuf buf, final CharSequence seq) {
        final int len = seq.length();
        buf.ensureWritable(len);
        if (seq instanceof AsciiString) {
            final AsciiString asciiString = (AsciiString)seq;
            buf.writeBytes(asciiString.array(), asciiString.arrayOffset(), asciiString.length());
            return len;
        }
        while (!(buf instanceof AbstractByteBuf)) {
            if (buf instanceof WrappedByteBuf) {
                buf = buf.unwrap();
            }
            else {
                buf.writeBytes(seq.toString().getBytes(CharsetUtil.US_ASCII));
            }
        }
        final AbstractByteBuf byteBuf = (AbstractByteBuf)buf;
        final int written = writeAscii(byteBuf, byteBuf.writerIndex, seq, len);
        final AbstractByteBuf abstractByteBuf = byteBuf;
        abstractByteBuf.writerIndex += written;
        return written;
    }
    
    static int writeAscii(final AbstractByteBuf buffer, int writerIndex, final CharSequence seq, final int len) {
        for (int i = 0; i < len; ++i) {
            buffer._setByte(writerIndex++, (byte)seq.charAt(i));
        }
        return len;
    }
    
    public static ByteBuf encodeString(final ByteBufAllocator alloc, final CharBuffer src, final Charset charset) {
        return encodeString0(alloc, false, src, charset, 0);
    }
    
    public static ByteBuf encodeString(final ByteBufAllocator alloc, final CharBuffer src, final Charset charset, final int extraCapacity) {
        return encodeString0(alloc, false, src, charset, extraCapacity);
    }
    
    static ByteBuf encodeString0(final ByteBufAllocator alloc, final boolean enforceHeap, final CharBuffer src, final Charset charset, final int extraCapacity) {
        final CharsetEncoder encoder = CharsetUtil.encoder(charset);
        final int length = (int)(src.remaining() * (double)encoder.maxBytesPerChar()) + extraCapacity;
        boolean release = true;
        ByteBuf dst;
        if (enforceHeap) {
            dst = alloc.heapBuffer(length);
        }
        else {
            dst = alloc.buffer(length);
        }
        try {
            final ByteBuffer dstBuf = dst.internalNioBuffer(dst.readerIndex(), length);
            final int pos = dstBuf.position();
            CoderResult cr = encoder.encode(src, dstBuf, true);
            if (!cr.isUnderflow()) {
                cr.throwException();
            }
            cr = encoder.flush(dstBuf);
            if (!cr.isUnderflow()) {
                cr.throwException();
            }
            dst.writerIndex(dst.writerIndex() + dstBuf.position() - pos);
            release = false;
            return dst;
        }
        catch (final CharacterCodingException x) {
            throw new IllegalStateException(x);
        }
        finally {
            if (release) {
                dst.release();
            }
        }
    }
    
    static String decodeString(final ByteBuf src, final int readerIndex, final int len, final Charset charset) {
        if (len == 0) {
            return "";
        }
        final CharsetDecoder decoder = CharsetUtil.decoder(charset);
        final int maxLength = (int)(len * (double)decoder.maxCharsPerByte());
        CharBuffer dst = ByteBufUtil.CHAR_BUFFERS.get();
        if (dst.length() < maxLength) {
            dst = CharBuffer.allocate(maxLength);
            if (maxLength <= ByteBufUtil.MAX_CHAR_BUFFER_SIZE) {
                ByteBufUtil.CHAR_BUFFERS.set(dst);
            }
        }
        else {
            dst.clear();
        }
        if (src.nioBufferCount() == 1) {
            decodeString(decoder, src.internalNioBuffer(readerIndex, len), dst);
        }
        else {
            final ByteBuf buffer = src.alloc().heapBuffer(len);
            try {
                buffer.writeBytes(src, readerIndex, len);
                decodeString(decoder, buffer.internalNioBuffer(buffer.readerIndex(), len), dst);
            }
            finally {
                buffer.release();
            }
        }
        return dst.flip().toString();
    }
    
    private static void decodeString(final CharsetDecoder decoder, final ByteBuffer src, final CharBuffer dst) {
        try {
            CoderResult cr = decoder.decode(src, dst, true);
            if (!cr.isUnderflow()) {
                cr.throwException();
            }
            cr = decoder.flush(dst);
            if (!cr.isUnderflow()) {
                cr.throwException();
            }
        }
        catch (final CharacterCodingException x) {
            throw new IllegalStateException(x);
        }
    }
    
    public static ByteBuf threadLocalDirectBuffer() {
        if (ByteBufUtil.THREAD_LOCAL_BUFFER_SIZE <= 0) {
            return null;
        }
        if (PlatformDependent.hasUnsafe()) {
            return ThreadLocalUnsafeDirectByteBuf.newInstance();
        }
        return ThreadLocalDirectByteBuf.newInstance();
    }
    
    public static byte[] getBytes(final ByteBuf buf) {
        return getBytes(buf, buf.readerIndex(), buf.readableBytes());
    }
    
    public static byte[] getBytes(final ByteBuf buf, final int start, final int length) {
        return getBytes(buf, start, length, true);
    }
    
    public static byte[] getBytes(final ByteBuf buf, final int start, final int length, final boolean copy) {
        if (MathUtil.isOutOfBounds(start, length, buf.capacity())) {
            throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length + ") <= buf.capacity(" + buf.capacity() + ')');
        }
        if (!buf.hasArray()) {
            final byte[] v = new byte[length];
            buf.getBytes(start, v);
            return v;
        }
        if (copy || start != 0 || length != buf.capacity()) {
            final int baseOffset = buf.arrayOffset() + start;
            return Arrays.copyOfRange(buf.array(), baseOffset, baseOffset + length);
        }
        return buf.array();
    }
    
    public static void copy(final AsciiString src, final int srcIdx, final ByteBuf dst, final int dstIdx, final int length) {
        if (MathUtil.isOutOfBounds(srcIdx, length, src.length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= srcIdx(" + srcIdx + ") <= srcIdx + length(" + length + ") <= srcLen(" + src.length() + ')');
        }
        ObjectUtil.checkNotNull(dst, "dst").setBytes(dstIdx, src.array(), srcIdx + src.arrayOffset(), length);
    }
    
    public static void copy(final AsciiString src, final int srcIdx, final ByteBuf dst, final int length) {
        if (MathUtil.isOutOfBounds(srcIdx, length, src.length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= srcIdx(" + srcIdx + ") <= srcIdx + length(" + length + ") <= srcLen(" + src.length() + ')');
        }
        ObjectUtil.checkNotNull(dst, "dst").writeBytes(src.array(), srcIdx + src.arrayOffset(), length);
    }
    
    public static String prettyHexDump(final ByteBuf buffer) {
        return prettyHexDump(buffer, buffer.readerIndex(), buffer.readableBytes());
    }
    
    public static String prettyHexDump(final ByteBuf buffer, final int offset, final int length) {
        return prettyHexDump(buffer, offset, length);
    }
    
    public static void appendPrettyHexDump(final StringBuilder dump, final ByteBuf buf) {
        appendPrettyHexDump(dump, buf, buf.readerIndex(), buf.readableBytes());
    }
    
    public static void appendPrettyHexDump(final StringBuilder dump, final ByteBuf buf, final int offset, final int length) {
        appendPrettyHexDump(dump, buf, offset, length);
    }
    
    public static boolean isText(final ByteBuf buf, final Charset charset) {
        return isText(buf, buf.readerIndex(), buf.readableBytes(), charset);
    }
    
    public static boolean isText(final ByteBuf buf, final int index, final int length, final Charset charset) {
        ObjectUtil.checkNotNull(buf, "buf");
        ObjectUtil.checkNotNull(charset, "charset");
        final int maxIndex = buf.readerIndex() + buf.readableBytes();
        if (index < 0 || length < 0 || index > maxIndex - length) {
            throw new IndexOutOfBoundsException("index: " + index + " length: " + length);
        }
        if (charset.equals(CharsetUtil.UTF_8)) {
            return isUtf8(buf, index, length);
        }
        if (charset.equals(CharsetUtil.US_ASCII)) {
            return isAscii(buf, index, length);
        }
        final CharsetDecoder decoder = CharsetUtil.decoder(charset, CodingErrorAction.REPORT, CodingErrorAction.REPORT);
        try {
            if (buf.nioBufferCount() == 1) {
                decoder.decode(buf.internalNioBuffer(index, length));
            }
            else {
                final ByteBuf heapBuffer = buf.alloc().heapBuffer(length);
                try {
                    heapBuffer.writeBytes(buf, index, length);
                    decoder.decode(heapBuffer.internalNioBuffer(heapBuffer.readerIndex(), length));
                }
                finally {
                    heapBuffer.release();
                }
            }
            return true;
        }
        catch (final CharacterCodingException ignore) {
            return false;
        }
    }
    
    private static boolean isAscii(final ByteBuf buf, final int index, final int length) {
        return buf.forEachByte(index, length, ByteBufUtil.FIND_NON_ASCII) == -1;
    }
    
    private static boolean isUtf8(final ByteBuf buf, int index, final int length) {
        final int endIndex = index + length;
        while (index < endIndex) {
            final byte b1 = buf.getByte(index++);
            if ((b1 & 0x80) == 0x0) {
                continue;
            }
            if ((b1 & 0xE0) == 0xC0) {
                if (index >= endIndex) {
                    return false;
                }
                final byte b2 = buf.getByte(index++);
                if ((b2 & 0xC0) != 0x80) {
                    return false;
                }
                if ((b1 & 0xFF) < 194) {
                    return false;
                }
                continue;
            }
            else if ((b1 & 0xF0) == 0xE0) {
                if (index > endIndex - 2) {
                    return false;
                }
                final byte b2 = buf.getByte(index++);
                final byte b3 = buf.getByte(index++);
                if ((b2 & 0xC0) != 0x80 || (b3 & 0xC0) != 0x80) {
                    return false;
                }
                if ((b1 & 0xF) == 0x0 && (b2 & 0xFF) < 160) {
                    return false;
                }
                if ((b1 & 0xF) == 0xD && (b2 & 0xFF) > 159) {
                    return false;
                }
                continue;
            }
            else {
                if ((b1 & 0xF8) != 0xF0) {
                    return false;
                }
                if (index > endIndex - 3) {
                    return false;
                }
                final byte b2 = buf.getByte(index++);
                final byte b3 = buf.getByte(index++);
                final byte b4 = buf.getByte(index++);
                if ((b2 & 0xC0) != 0x80 || (b3 & 0xC0) != 0x80 || (b4 & 0xC0) != 0x80) {
                    return false;
                }
                if ((b1 & 0xFF) > 244 || ((b1 & 0xFF) == 0xF0 && (b2 & 0xFF) < 144) || ((b1 & 0xFF) == 0xF4 && (b2 & 0xFF) > 143)) {
                    return false;
                }
                continue;
            }
        }
        return true;
    }
    
    private ByteBufUtil() {
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(ByteBufUtil.class);
        CHAR_BUFFERS = new FastThreadLocal<CharBuffer>() {
            @Override
            protected CharBuffer initialValue() throws Exception {
                return CharBuffer.allocate(1024);
            }
        };
        MAX_BYTES_PER_CHAR_UTF8 = (int)CharsetUtil.encoder(CharsetUtil.UTF_8).maxBytesPerChar();
        String allocType = SystemPropertyUtil.get("io.netty.allocator.type", PlatformDependent.isAndroid() ? "unpooled" : "pooled");
        allocType = allocType.toLowerCase(Locale.US).trim();
        ByteBufAllocator alloc;
        if ("unpooled".equals(allocType)) {
            alloc = UnpooledByteBufAllocator.DEFAULT;
            ByteBufUtil.logger.debug("-Dio.netty.allocator.type: {}", allocType);
        }
        else if ("pooled".equals(allocType)) {
            alloc = PooledByteBufAllocator.DEFAULT;
            ByteBufUtil.logger.debug("-Dio.netty.allocator.type: {}", allocType);
        }
        else {
            alloc = PooledByteBufAllocator.DEFAULT;
            ByteBufUtil.logger.debug("-Dio.netty.allocator.type: pooled (unknown: {})", allocType);
        }
        DEFAULT_ALLOCATOR = alloc;
        THREAD_LOCAL_BUFFER_SIZE = SystemPropertyUtil.getInt("io.netty.threadLocalDirectBufferSize", 65536);
        ByteBufUtil.logger.debug("-Dio.netty.threadLocalDirectBufferSize: {}", (Object)ByteBufUtil.THREAD_LOCAL_BUFFER_SIZE);
        MAX_CHAR_BUFFER_SIZE = SystemPropertyUtil.getInt("io.netty.maxThreadLocalCharBufferSize", 16384);
        ByteBufUtil.logger.debug("-Dio.netty.maxThreadLocalCharBufferSize: {}", (Object)ByteBufUtil.MAX_CHAR_BUFFER_SIZE);
        FIND_NON_ASCII = new ByteProcessor() {
            @Override
            public boolean process(final byte value) {
                return value >= 0;
            }
        };
    }
    
    private static final class HexUtil
    {
        private static final char[] BYTE2CHAR;
        private static final char[] HEXDUMP_TABLE;
        private static final String[] HEXPADDING;
        private static final String[] HEXDUMP_ROWPREFIXES;
        private static final String[] BYTE2HEX;
        private static final String[] BYTEPADDING;
        
        private static String hexDump(final ByteBuf buffer, final int fromIndex, final int length) {
            if (length < 0) {
                throw new IllegalArgumentException("length: " + length);
            }
            if (length == 0) {
                return "";
            }
            final int endIndex = fromIndex + length;
            final char[] buf = new char[length << 1];
            for (int srcIdx = fromIndex, dstIdx = 0; srcIdx < endIndex; ++srcIdx, dstIdx += 2) {
                System.arraycopy(HexUtil.HEXDUMP_TABLE, buffer.getUnsignedByte(srcIdx) << 1, buf, dstIdx, 2);
            }
            return new String(buf);
        }
        
        private static String hexDump(final byte[] array, final int fromIndex, final int length) {
            if (length < 0) {
                throw new IllegalArgumentException("length: " + length);
            }
            if (length == 0) {
                return "";
            }
            final int endIndex = fromIndex + length;
            final char[] buf = new char[length << 1];
            for (int srcIdx = fromIndex, dstIdx = 0; srcIdx < endIndex; ++srcIdx, dstIdx += 2) {
                System.arraycopy(HexUtil.HEXDUMP_TABLE, (array[srcIdx] & 0xFF) << 1, buf, dstIdx, 2);
            }
            return new String(buf);
        }
        
        private static String prettyHexDump(final ByteBuf buffer, final int offset, final int length) {
            if (length == 0) {
                return "";
            }
            final int rows = length / 16 + ((length % 15 != 0) ? 1 : 0) + 4;
            final StringBuilder buf = new StringBuilder(rows * 80);
            appendPrettyHexDump(buf, buffer, offset, length);
            return buf.toString();
        }
        
        private static void appendPrettyHexDump(final StringBuilder dump, final ByteBuf buf, final int offset, final int length) {
            if (MathUtil.isOutOfBounds(offset, length, buf.capacity())) {
                throw new IndexOutOfBoundsException("expected: 0 <= offset(" + offset + ") <= offset + length(" + length + ") <= buf.capacity(" + buf.capacity() + ')');
            }
            if (length == 0) {
                return;
            }
            dump.append("         +-------------------------------------------------+" + StringUtil.NEWLINE + "         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |" + StringUtil.NEWLINE + "+--------+-------------------------------------------------+----------------+");
            final int startIndex = offset;
            final int fullRows = length >>> 4;
            final int remainder = length & 0xF;
            for (int row = 0; row < fullRows; ++row) {
                final int rowStartIndex = (row << 4) + startIndex;
                appendHexDumpRowPrefix(dump, row, rowStartIndex);
                final int rowEndIndex = rowStartIndex + 16;
                for (int j = rowStartIndex; j < rowEndIndex; ++j) {
                    dump.append(HexUtil.BYTE2HEX[buf.getUnsignedByte(j)]);
                }
                dump.append(" |");
                for (int j = rowStartIndex; j < rowEndIndex; ++j) {
                    dump.append(HexUtil.BYTE2CHAR[buf.getUnsignedByte(j)]);
                }
                dump.append('|');
            }
            if (remainder != 0) {
                final int rowStartIndex2 = (fullRows << 4) + startIndex;
                appendHexDumpRowPrefix(dump, fullRows, rowStartIndex2);
                final int rowEndIndex2 = rowStartIndex2 + remainder;
                for (int i = rowStartIndex2; i < rowEndIndex2; ++i) {
                    dump.append(HexUtil.BYTE2HEX[buf.getUnsignedByte(i)]);
                }
                dump.append(HexUtil.HEXPADDING[remainder]);
                dump.append(" |");
                for (int i = rowStartIndex2; i < rowEndIndex2; ++i) {
                    dump.append(HexUtil.BYTE2CHAR[buf.getUnsignedByte(i)]);
                }
                dump.append(HexUtil.BYTEPADDING[remainder]);
                dump.append('|');
            }
            dump.append(StringUtil.NEWLINE + "+--------+-------------------------------------------------+----------------+");
        }
        
        private static void appendHexDumpRowPrefix(final StringBuilder dump, final int row, final int rowStartIndex) {
            if (row < HexUtil.HEXDUMP_ROWPREFIXES.length) {
                dump.append(HexUtil.HEXDUMP_ROWPREFIXES[row]);
            }
            else {
                dump.append(StringUtil.NEWLINE);
                dump.append(Long.toHexString(((long)rowStartIndex & 0xFFFFFFFFL) | 0x100000000L));
                dump.setCharAt(dump.length() - 9, '|');
                dump.append('|');
            }
        }
        
        static {
            BYTE2CHAR = new char[256];
            HEXDUMP_TABLE = new char[1024];
            HEXPADDING = new String[16];
            HEXDUMP_ROWPREFIXES = new String[4096];
            BYTE2HEX = new String[256];
            BYTEPADDING = new String[16];
            final char[] DIGITS = "0123456789abcdef".toCharArray();
            for (int i = 0; i < 256; ++i) {
                HexUtil.HEXDUMP_TABLE[i << 1] = DIGITS[i >>> 4 & 0xF];
                HexUtil.HEXDUMP_TABLE[(i << 1) + 1] = DIGITS[i & 0xF];
            }
            for (int i = 0; i < HexUtil.HEXPADDING.length; ++i) {
                final int padding = HexUtil.HEXPADDING.length - i;
                final StringBuilder buf = new StringBuilder(padding * 3);
                for (int j = 0; j < padding; ++j) {
                    buf.append("   ");
                }
                HexUtil.HEXPADDING[i] = buf.toString();
            }
            for (int i = 0; i < HexUtil.HEXDUMP_ROWPREFIXES.length; ++i) {
                final StringBuilder buf2 = new StringBuilder(12);
                buf2.append(StringUtil.NEWLINE);
                buf2.append(Long.toHexString(((long)(i << 4) & 0xFFFFFFFFL) | 0x100000000L));
                buf2.setCharAt(buf2.length() - 9, '|');
                buf2.append('|');
                HexUtil.HEXDUMP_ROWPREFIXES[i] = buf2.toString();
            }
            for (int i = 0; i < HexUtil.BYTE2HEX.length; ++i) {
                HexUtil.BYTE2HEX[i] = ' ' + StringUtil.byteToHexStringPadded(i);
            }
            for (int i = 0; i < HexUtil.BYTEPADDING.length; ++i) {
                final int padding = HexUtil.BYTEPADDING.length - i;
                final StringBuilder buf = new StringBuilder(padding);
                for (int j = 0; j < padding; ++j) {
                    buf.append(' ');
                }
                HexUtil.BYTEPADDING[i] = buf.toString();
            }
            for (int i = 0; i < HexUtil.BYTE2CHAR.length; ++i) {
                if (i <= 31 || i >= 127) {
                    HexUtil.BYTE2CHAR[i] = '.';
                }
                else {
                    HexUtil.BYTE2CHAR[i] = (char)i;
                }
            }
        }
    }
    
    static final class ThreadLocalUnsafeDirectByteBuf extends UnpooledUnsafeDirectByteBuf
    {
        private static final Recycler<ThreadLocalUnsafeDirectByteBuf> RECYCLER;
        private final Recycler.Handle<ThreadLocalUnsafeDirectByteBuf> handle;
        
        static ThreadLocalUnsafeDirectByteBuf newInstance() {
            final ThreadLocalUnsafeDirectByteBuf buf = ThreadLocalUnsafeDirectByteBuf.RECYCLER.get();
            buf.setRefCnt(1);
            return buf;
        }
        
        private ThreadLocalUnsafeDirectByteBuf(final Recycler.Handle<ThreadLocalUnsafeDirectByteBuf> handle) {
            super(UnpooledByteBufAllocator.DEFAULT, 256, Integer.MAX_VALUE);
            this.handle = handle;
        }
        
        @Override
        protected void deallocate() {
            if (this.capacity() > ByteBufUtil.THREAD_LOCAL_BUFFER_SIZE) {
                super.deallocate();
            }
            else {
                this.clear();
                this.handle.recycle(this);
            }
        }
        
        static {
            RECYCLER = new Recycler<ThreadLocalUnsafeDirectByteBuf>() {
                @Override
                protected ThreadLocalUnsafeDirectByteBuf newObject(final Handle<ThreadLocalUnsafeDirectByteBuf> handle) {
                    return new ThreadLocalUnsafeDirectByteBuf((Handle)handle);
                }
            };
        }
    }
    
    static final class ThreadLocalDirectByteBuf extends UnpooledDirectByteBuf
    {
        private static final Recycler<ThreadLocalDirectByteBuf> RECYCLER;
        private final Recycler.Handle<ThreadLocalDirectByteBuf> handle;
        
        static ThreadLocalDirectByteBuf newInstance() {
            final ThreadLocalDirectByteBuf buf = ThreadLocalDirectByteBuf.RECYCLER.get();
            buf.setRefCnt(1);
            return buf;
        }
        
        private ThreadLocalDirectByteBuf(final Recycler.Handle<ThreadLocalDirectByteBuf> handle) {
            super(UnpooledByteBufAllocator.DEFAULT, 256, Integer.MAX_VALUE);
            this.handle = handle;
        }
        
        @Override
        protected void deallocate() {
            if (this.capacity() > ByteBufUtil.THREAD_LOCAL_BUFFER_SIZE) {
                super.deallocate();
            }
            else {
                this.clear();
                this.handle.recycle(this);
            }
        }
        
        static {
            RECYCLER = new Recycler<ThreadLocalDirectByteBuf>() {
                @Override
                protected ThreadLocalDirectByteBuf newObject(final Handle<ThreadLocalDirectByteBuf> handle) {
                    return new ThreadLocalDirectByteBuf((Handle)handle);
                }
            };
        }
    }
}
