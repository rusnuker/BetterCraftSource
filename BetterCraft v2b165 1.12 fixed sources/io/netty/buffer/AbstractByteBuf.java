// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import io.netty.util.ResourceLeakDetectorFactory;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.internal.MathUtil;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.ByteProcessor;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.io.IOException;
import java.nio.channels.GatheringByteChannel;
import java.nio.ByteBuffer;
import io.netty.util.CharsetUtil;
import java.nio.charset.Charset;
import java.nio.ByteOrder;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.internal.logging.InternalLogger;

public abstract class AbstractByteBuf extends ByteBuf
{
    private static final InternalLogger logger;
    private static final String PROP_MODE = "io.netty.buffer.bytebuf.checkAccessible";
    private static final boolean checkAccessible;
    static final ResourceLeakDetector<ByteBuf> leakDetector;
    int readerIndex;
    int writerIndex;
    private int markedReaderIndex;
    private int markedWriterIndex;
    private int maxCapacity;
    
    protected AbstractByteBuf(final int maxCapacity) {
        if (maxCapacity < 0) {
            throw new IllegalArgumentException("maxCapacity: " + maxCapacity + " (expected: >= 0)");
        }
        this.maxCapacity = maxCapacity;
    }
    
    @Override
    public boolean isReadOnly() {
        return false;
    }
    
    @Override
    public ByteBuf asReadOnly() {
        if (this.isReadOnly()) {
            return this;
        }
        return Unpooled.unmodifiableBuffer(this);
    }
    
    @Override
    public int maxCapacity() {
        return this.maxCapacity;
    }
    
    protected final void maxCapacity(final int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
    
    @Override
    public int readerIndex() {
        return this.readerIndex;
    }
    
    @Override
    public ByteBuf readerIndex(final int readerIndex) {
        if (readerIndex < 0 || readerIndex > this.writerIndex) {
            throw new IndexOutOfBoundsException(String.format("readerIndex: %d (expected: 0 <= readerIndex <= writerIndex(%d))", readerIndex, this.writerIndex));
        }
        this.readerIndex = readerIndex;
        return this;
    }
    
    @Override
    public int writerIndex() {
        return this.writerIndex;
    }
    
    @Override
    public ByteBuf writerIndex(final int writerIndex) {
        if (writerIndex < this.readerIndex || writerIndex > this.capacity()) {
            throw new IndexOutOfBoundsException(String.format("writerIndex: %d (expected: readerIndex(%d) <= writerIndex <= capacity(%d))", writerIndex, this.readerIndex, this.capacity()));
        }
        this.writerIndex = writerIndex;
        return this;
    }
    
    @Override
    public ByteBuf setIndex(final int readerIndex, final int writerIndex) {
        if (readerIndex < 0 || readerIndex > writerIndex || writerIndex > this.capacity()) {
            throw new IndexOutOfBoundsException(String.format("readerIndex: %d, writerIndex: %d (expected: 0 <= readerIndex <= writerIndex <= capacity(%d))", readerIndex, writerIndex, this.capacity()));
        }
        this.setIndex0(readerIndex, writerIndex);
        return this;
    }
    
    @Override
    public ByteBuf clear() {
        final int n = 0;
        this.writerIndex = n;
        this.readerIndex = n;
        return this;
    }
    
    @Override
    public boolean isReadable() {
        return this.writerIndex > this.readerIndex;
    }
    
    @Override
    public boolean isReadable(final int numBytes) {
        return this.writerIndex - this.readerIndex >= numBytes;
    }
    
    @Override
    public boolean isWritable() {
        return this.capacity() > this.writerIndex;
    }
    
    @Override
    public boolean isWritable(final int numBytes) {
        return this.capacity() - this.writerIndex >= numBytes;
    }
    
    @Override
    public int readableBytes() {
        return this.writerIndex - this.readerIndex;
    }
    
    @Override
    public int writableBytes() {
        return this.capacity() - this.writerIndex;
    }
    
    @Override
    public int maxWritableBytes() {
        return this.maxCapacity() - this.writerIndex;
    }
    
    @Override
    public ByteBuf markReaderIndex() {
        this.markedReaderIndex = this.readerIndex;
        return this;
    }
    
    @Override
    public ByteBuf resetReaderIndex() {
        this.readerIndex(this.markedReaderIndex);
        return this;
    }
    
    @Override
    public ByteBuf markWriterIndex() {
        this.markedWriterIndex = this.writerIndex;
        return this;
    }
    
    @Override
    public ByteBuf resetWriterIndex() {
        this.writerIndex = this.markedWriterIndex;
        return this;
    }
    
    @Override
    public ByteBuf discardReadBytes() {
        this.ensureAccessible();
        if (this.readerIndex == 0) {
            return this;
        }
        if (this.readerIndex != this.writerIndex) {
            this.setBytes(0, this, this.readerIndex, this.writerIndex - this.readerIndex);
            this.writerIndex -= this.readerIndex;
            this.adjustMarkers(this.readerIndex);
            this.readerIndex = 0;
        }
        else {
            this.adjustMarkers(this.readerIndex);
            final int n = 0;
            this.readerIndex = n;
            this.writerIndex = n;
        }
        return this;
    }
    
    @Override
    public ByteBuf discardSomeReadBytes() {
        this.ensureAccessible();
        if (this.readerIndex == 0) {
            return this;
        }
        if (this.readerIndex == this.writerIndex) {
            this.adjustMarkers(this.readerIndex);
            final int n = 0;
            this.readerIndex = n;
            this.writerIndex = n;
            return this;
        }
        if (this.readerIndex >= this.capacity() >>> 1) {
            this.setBytes(0, this, this.readerIndex, this.writerIndex - this.readerIndex);
            this.writerIndex -= this.readerIndex;
            this.adjustMarkers(this.readerIndex);
            this.readerIndex = 0;
        }
        return this;
    }
    
    protected final void adjustMarkers(final int decrement) {
        final int markedReaderIndex = this.markedReaderIndex;
        if (markedReaderIndex <= decrement) {
            this.markedReaderIndex = 0;
            final int markedWriterIndex = this.markedWriterIndex;
            if (markedWriterIndex <= decrement) {
                this.markedWriterIndex = 0;
            }
            else {
                this.markedWriterIndex = markedWriterIndex - decrement;
            }
        }
        else {
            this.markedReaderIndex = markedReaderIndex - decrement;
            this.markedWriterIndex -= decrement;
        }
    }
    
    @Override
    public ByteBuf ensureWritable(final int minWritableBytes) {
        if (minWritableBytes < 0) {
            throw new IllegalArgumentException(String.format("minWritableBytes: %d (expected: >= 0)", minWritableBytes));
        }
        this.ensureWritable0(minWritableBytes);
        return this;
    }
    
    private void ensureWritable0(final int minWritableBytes) {
        if (minWritableBytes <= this.writableBytes()) {
            return;
        }
        if (minWritableBytes > this.maxCapacity - this.writerIndex) {
            throw new IndexOutOfBoundsException(String.format("writerIndex(%d) + minWritableBytes(%d) exceeds maxCapacity(%d): %s", this.writerIndex, minWritableBytes, this.maxCapacity, this));
        }
        final int newCapacity = this.alloc().calculateNewCapacity(this.writerIndex + minWritableBytes, this.maxCapacity);
        this.capacity(newCapacity);
    }
    
    @Override
    public int ensureWritable(final int minWritableBytes, final boolean force) {
        if (minWritableBytes < 0) {
            throw new IllegalArgumentException(String.format("minWritableBytes: %d (expected: >= 0)", minWritableBytes));
        }
        if (minWritableBytes <= this.writableBytes()) {
            return 0;
        }
        if (minWritableBytes <= this.maxCapacity - this.writerIndex || !force) {
            final int newCapacity = this.alloc().calculateNewCapacity(this.writerIndex + minWritableBytes, this.maxCapacity);
            this.capacity(newCapacity);
            return 2;
        }
        if (this.capacity() == this.maxCapacity()) {
            return 1;
        }
        this.capacity(this.maxCapacity());
        return 3;
    }
    
    @Override
    public ByteBuf order(final ByteOrder endianness) {
        if (endianness == null) {
            throw new NullPointerException("endianness");
        }
        if (endianness == this.order()) {
            return this;
        }
        return this.newSwappedByteBuf();
    }
    
    protected SwappedByteBuf newSwappedByteBuf() {
        return new SwappedByteBuf(this);
    }
    
    @Override
    public byte getByte(final int index) {
        this.checkIndex(index);
        return this._getByte(index);
    }
    
    protected abstract byte _getByte(final int p0);
    
    @Override
    public boolean getBoolean(final int index) {
        return this.getByte(index) != 0;
    }
    
    @Override
    public short getUnsignedByte(final int index) {
        return (short)(this.getByte(index) & 0xFF);
    }
    
    @Override
    public short getShort(final int index) {
        this.checkIndex(index, 2);
        return this._getShort(index);
    }
    
    protected abstract short _getShort(final int p0);
    
    @Override
    public short getShortLE(final int index) {
        this.checkIndex(index, 2);
        return this._getShortLE(index);
    }
    
    protected abstract short _getShortLE(final int p0);
    
    @Override
    public int getUnsignedShort(final int index) {
        return this.getShort(index) & 0xFFFF;
    }
    
    @Override
    public int getUnsignedShortLE(final int index) {
        return this.getShortLE(index) & 0xFFFF;
    }
    
    @Override
    public int getUnsignedMedium(final int index) {
        this.checkIndex(index, 3);
        return this._getUnsignedMedium(index);
    }
    
    protected abstract int _getUnsignedMedium(final int p0);
    
    @Override
    public int getUnsignedMediumLE(final int index) {
        this.checkIndex(index, 3);
        return this._getUnsignedMediumLE(index);
    }
    
    protected abstract int _getUnsignedMediumLE(final int p0);
    
    @Override
    public int getMedium(final int index) {
        int value = this.getUnsignedMedium(index);
        if ((value & 0x800000) != 0x0) {
            value |= 0xFF000000;
        }
        return value;
    }
    
    @Override
    public int getMediumLE(final int index) {
        int value = this.getUnsignedMediumLE(index);
        if ((value & 0x800000) != 0x0) {
            value |= 0xFF000000;
        }
        return value;
    }
    
    @Override
    public int getInt(final int index) {
        this.checkIndex(index, 4);
        return this._getInt(index);
    }
    
    protected abstract int _getInt(final int p0);
    
    @Override
    public int getIntLE(final int index) {
        this.checkIndex(index, 4);
        return this._getIntLE(index);
    }
    
    protected abstract int _getIntLE(final int p0);
    
    @Override
    public long getUnsignedInt(final int index) {
        return (long)this.getInt(index) & 0xFFFFFFFFL;
    }
    
    @Override
    public long getUnsignedIntLE(final int index) {
        return (long)this.getIntLE(index) & 0xFFFFFFFFL;
    }
    
    @Override
    public long getLong(final int index) {
        this.checkIndex(index, 8);
        return this._getLong(index);
    }
    
    protected abstract long _getLong(final int p0);
    
    @Override
    public long getLongLE(final int index) {
        this.checkIndex(index, 8);
        return this._getLongLE(index);
    }
    
    protected abstract long _getLongLE(final int p0);
    
    @Override
    public char getChar(final int index) {
        return (char)this.getShort(index);
    }
    
    @Override
    public float getFloat(final int index) {
        return Float.intBitsToFloat(this.getInt(index));
    }
    
    @Override
    public double getDouble(final int index) {
        return Double.longBitsToDouble(this.getLong(index));
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst) {
        this.getBytes(index, dst, 0, dst.length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst) {
        this.getBytes(index, dst, dst.writableBytes());
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int length) {
        this.getBytes(index, dst, dst.writerIndex(), length);
        dst.writerIndex(dst.writerIndex() + length);
        return this;
    }
    
    @Override
    public CharSequence getCharSequence(final int index, final int length, final Charset charset) {
        return this.toString(index, length, charset);
    }
    
    @Override
    public CharSequence readCharSequence(final int length, final Charset charset) {
        final CharSequence sequence = this.getCharSequence(this.readerIndex, length, charset);
        this.readerIndex += length;
        return sequence;
    }
    
    @Override
    public ByteBuf setByte(final int index, final int value) {
        this.checkIndex(index);
        this._setByte(index, value);
        return this;
    }
    
    protected abstract void _setByte(final int p0, final int p1);
    
    @Override
    public ByteBuf setBoolean(final int index, final boolean value) {
        this.setByte(index, value ? 1 : 0);
        return this;
    }
    
    @Override
    public ByteBuf setShort(final int index, final int value) {
        this.checkIndex(index, 2);
        this._setShort(index, value);
        return this;
    }
    
    protected abstract void _setShort(final int p0, final int p1);
    
    @Override
    public ByteBuf setShortLE(final int index, final int value) {
        this.checkIndex(index, 2);
        this._setShortLE(index, value);
        return this;
    }
    
    protected abstract void _setShortLE(final int p0, final int p1);
    
    @Override
    public ByteBuf setChar(final int index, final int value) {
        this.setShort(index, value);
        return this;
    }
    
    @Override
    public ByteBuf setMedium(final int index, final int value) {
        this.checkIndex(index, 3);
        this._setMedium(index, value);
        return this;
    }
    
    protected abstract void _setMedium(final int p0, final int p1);
    
    @Override
    public ByteBuf setMediumLE(final int index, final int value) {
        this.checkIndex(index, 3);
        this._setMediumLE(index, value);
        return this;
    }
    
    protected abstract void _setMediumLE(final int p0, final int p1);
    
    @Override
    public ByteBuf setInt(final int index, final int value) {
        this.checkIndex(index, 4);
        this._setInt(index, value);
        return this;
    }
    
    protected abstract void _setInt(final int p0, final int p1);
    
    @Override
    public ByteBuf setIntLE(final int index, final int value) {
        this.checkIndex(index, 4);
        this._setIntLE(index, value);
        return this;
    }
    
    protected abstract void _setIntLE(final int p0, final int p1);
    
    @Override
    public ByteBuf setFloat(final int index, final float value) {
        this.setInt(index, Float.floatToRawIntBits(value));
        return this;
    }
    
    @Override
    public ByteBuf setLong(final int index, final long value) {
        this.checkIndex(index, 8);
        this._setLong(index, value);
        return this;
    }
    
    protected abstract void _setLong(final int p0, final long p1);
    
    @Override
    public ByteBuf setLongLE(final int index, final long value) {
        this.checkIndex(index, 8);
        this._setLongLE(index, value);
        return this;
    }
    
    protected abstract void _setLongLE(final int p0, final long p1);
    
    @Override
    public ByteBuf setDouble(final int index, final double value) {
        this.setLong(index, Double.doubleToRawLongBits(value));
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src) {
        this.setBytes(index, src, 0, src.length);
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src) {
        this.setBytes(index, src, src.readableBytes());
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int length) {
        this.checkIndex(index, length);
        if (src == null) {
            throw new NullPointerException("src");
        }
        if (length > src.readableBytes()) {
            throw new IndexOutOfBoundsException(String.format("length(%d) exceeds src.readableBytes(%d) where src is: %s", length, src.readableBytes(), src));
        }
        this.setBytes(index, src, src.readerIndex(), length);
        src.readerIndex(src.readerIndex() + length);
        return this;
    }
    
    @Override
    public ByteBuf setZero(int index, final int length) {
        if (length == 0) {
            return this;
        }
        this.checkIndex(index, length);
        final int nLong = length >>> 3;
        final int nBytes = length & 0x7;
        for (int i = nLong; i > 0; --i) {
            this._setLong(index, 0L);
            index += 8;
        }
        if (nBytes == 4) {
            this._setInt(index, 0);
        }
        else if (nBytes < 4) {
            for (int i = nBytes; i > 0; --i) {
                this._setByte(index, 0);
                ++index;
            }
        }
        else {
            this._setInt(index, 0);
            index += 4;
            for (int i = nBytes - 4; i > 0; --i) {
                this._setByte(index, 0);
                ++index;
            }
        }
        return this;
    }
    
    @Override
    public int setCharSequence(final int index, final CharSequence sequence, final Charset charset) {
        if (charset.equals(CharsetUtil.UTF_8)) {
            this.ensureWritable(ByteBufUtil.utf8MaxBytes(sequence));
            return ByteBufUtil.writeUtf8(this, index, sequence, sequence.length());
        }
        if (charset.equals(CharsetUtil.US_ASCII)) {
            final int len = sequence.length();
            this.ensureWritable(len);
            return ByteBufUtil.writeAscii(this, index, sequence, len);
        }
        final byte[] bytes = sequence.toString().getBytes(charset);
        this.ensureWritable(bytes.length);
        this.setBytes(index, bytes);
        return bytes.length;
    }
    
    @Override
    public byte readByte() {
        this.checkReadableBytes0(1);
        final int i = this.readerIndex;
        final byte b = this._getByte(i);
        this.readerIndex = i + 1;
        return b;
    }
    
    @Override
    public boolean readBoolean() {
        return this.readByte() != 0;
    }
    
    @Override
    public short readUnsignedByte() {
        return (short)(this.readByte() & 0xFF);
    }
    
    @Override
    public short readShort() {
        this.checkReadableBytes0(2);
        final short v = this._getShort(this.readerIndex);
        this.readerIndex += 2;
        return v;
    }
    
    @Override
    public short readShortLE() {
        this.checkReadableBytes0(2);
        final short v = this._getShortLE(this.readerIndex);
        this.readerIndex += 2;
        return v;
    }
    
    @Override
    public int readUnsignedShort() {
        return this.readShort() & 0xFFFF;
    }
    
    @Override
    public int readUnsignedShortLE() {
        return this.readShortLE() & 0xFFFF;
    }
    
    @Override
    public int readMedium() {
        int value = this.readUnsignedMedium();
        if ((value & 0x800000) != 0x0) {
            value |= 0xFF000000;
        }
        return value;
    }
    
    @Override
    public int readMediumLE() {
        int value = this.readUnsignedMediumLE();
        if ((value & 0x800000) != 0x0) {
            value |= 0xFF000000;
        }
        return value;
    }
    
    @Override
    public int readUnsignedMedium() {
        this.checkReadableBytes0(3);
        final int v = this._getUnsignedMedium(this.readerIndex);
        this.readerIndex += 3;
        return v;
    }
    
    @Override
    public int readUnsignedMediumLE() {
        this.checkReadableBytes0(3);
        final int v = this._getUnsignedMediumLE(this.readerIndex);
        this.readerIndex += 3;
        return v;
    }
    
    @Override
    public int readInt() {
        this.checkReadableBytes0(4);
        final int v = this._getInt(this.readerIndex);
        this.readerIndex += 4;
        return v;
    }
    
    @Override
    public int readIntLE() {
        this.checkReadableBytes0(4);
        final int v = this._getIntLE(this.readerIndex);
        this.readerIndex += 4;
        return v;
    }
    
    @Override
    public long readUnsignedInt() {
        return (long)this.readInt() & 0xFFFFFFFFL;
    }
    
    @Override
    public long readUnsignedIntLE() {
        return (long)this.readIntLE() & 0xFFFFFFFFL;
    }
    
    @Override
    public long readLong() {
        this.checkReadableBytes0(8);
        final long v = this._getLong(this.readerIndex);
        this.readerIndex += 8;
        return v;
    }
    
    @Override
    public long readLongLE() {
        this.checkReadableBytes0(8);
        final long v = this._getLongLE(this.readerIndex);
        this.readerIndex += 8;
        return v;
    }
    
    @Override
    public char readChar() {
        return (char)this.readShort();
    }
    
    @Override
    public float readFloat() {
        return Float.intBitsToFloat(this.readInt());
    }
    
    @Override
    public double readDouble() {
        return Double.longBitsToDouble(this.readLong());
    }
    
    @Override
    public ByteBuf readBytes(final int length) {
        this.checkReadableBytes(length);
        if (length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        final ByteBuf buf = this.alloc().buffer(length, this.maxCapacity);
        buf.writeBytes(this, this.readerIndex, length);
        this.readerIndex += length;
        return buf;
    }
    
    @Override
    public ByteBuf readSlice(final int length) {
        final ByteBuf slice = this.slice(this.readerIndex, length);
        this.readerIndex += length;
        return slice;
    }
    
    @Override
    public ByteBuf readRetainedSlice(final int length) {
        final ByteBuf slice = this.retainedSlice(this.readerIndex, length);
        this.readerIndex += length;
        return slice;
    }
    
    @Override
    public ByteBuf readBytes(final byte[] dst, final int dstIndex, final int length) {
        this.checkReadableBytes(length);
        this.getBytes(this.readerIndex, dst, dstIndex, length);
        this.readerIndex += length;
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final byte[] dst) {
        this.readBytes(dst, 0, dst.length);
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst) {
        this.readBytes(dst, dst.writableBytes());
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst, final int length) {
        if (length > dst.writableBytes()) {
            throw new IndexOutOfBoundsException(String.format("length(%d) exceeds dst.writableBytes(%d) where dst is: %s", length, dst.writableBytes(), dst));
        }
        this.readBytes(dst, dst.writerIndex(), length);
        dst.writerIndex(dst.writerIndex() + length);
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst, final int dstIndex, final int length) {
        this.checkReadableBytes(length);
        this.getBytes(this.readerIndex, dst, dstIndex, length);
        this.readerIndex += length;
        return this;
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuffer dst) {
        final int length = dst.remaining();
        this.checkReadableBytes(length);
        this.getBytes(this.readerIndex, dst);
        this.readerIndex += length;
        return this;
    }
    
    @Override
    public int readBytes(final GatheringByteChannel out, final int length) throws IOException {
        this.checkReadableBytes(length);
        final int readBytes = this.getBytes(this.readerIndex, out, length);
        this.readerIndex += readBytes;
        return readBytes;
    }
    
    @Override
    public int readBytes(final FileChannel out, final long position, final int length) throws IOException {
        this.checkReadableBytes(length);
        final int readBytes = this.getBytes(this.readerIndex, out, position, length);
        this.readerIndex += readBytes;
        return readBytes;
    }
    
    @Override
    public ByteBuf readBytes(final OutputStream out, final int length) throws IOException {
        this.checkReadableBytes(length);
        this.getBytes(this.readerIndex, out, length);
        this.readerIndex += length;
        return this;
    }
    
    @Override
    public ByteBuf skipBytes(final int length) {
        this.checkReadableBytes(length);
        this.readerIndex += length;
        return this;
    }
    
    @Override
    public ByteBuf writeBoolean(final boolean value) {
        this.writeByte(value ? 1 : 0);
        return this;
    }
    
    @Override
    public ByteBuf writeByte(final int value) {
        this.ensureAccessible();
        this.ensureWritable0(1);
        this._setByte(this.writerIndex++, value);
        return this;
    }
    
    @Override
    public ByteBuf writeShort(final int value) {
        this.ensureAccessible();
        this.ensureWritable0(2);
        this._setShort(this.writerIndex, value);
        this.writerIndex += 2;
        return this;
    }
    
    @Override
    public ByteBuf writeShortLE(final int value) {
        this.ensureAccessible();
        this.ensureWritable0(2);
        this._setShortLE(this.writerIndex, value);
        this.writerIndex += 2;
        return this;
    }
    
    @Override
    public ByteBuf writeMedium(final int value) {
        this.ensureAccessible();
        this.ensureWritable0(3);
        this._setMedium(this.writerIndex, value);
        this.writerIndex += 3;
        return this;
    }
    
    @Override
    public ByteBuf writeMediumLE(final int value) {
        this.ensureAccessible();
        this.ensureWritable0(3);
        this._setMediumLE(this.writerIndex, value);
        this.writerIndex += 3;
        return this;
    }
    
    @Override
    public ByteBuf writeInt(final int value) {
        this.ensureAccessible();
        this.ensureWritable0(4);
        this._setInt(this.writerIndex, value);
        this.writerIndex += 4;
        return this;
    }
    
    @Override
    public ByteBuf writeIntLE(final int value) {
        this.ensureAccessible();
        this.ensureWritable0(4);
        this._setIntLE(this.writerIndex, value);
        this.writerIndex += 4;
        return this;
    }
    
    @Override
    public ByteBuf writeLong(final long value) {
        this.ensureAccessible();
        this.ensureWritable0(8);
        this._setLong(this.writerIndex, value);
        this.writerIndex += 8;
        return this;
    }
    
    @Override
    public ByteBuf writeLongLE(final long value) {
        this.ensureAccessible();
        this.ensureWritable0(8);
        this._setLongLE(this.writerIndex, value);
        this.writerIndex += 8;
        return this;
    }
    
    @Override
    public ByteBuf writeChar(final int value) {
        this.writeShort(value);
        return this;
    }
    
    @Override
    public ByteBuf writeFloat(final float value) {
        this.writeInt(Float.floatToRawIntBits(value));
        return this;
    }
    
    @Override
    public ByteBuf writeDouble(final double value) {
        this.writeLong(Double.doubleToRawLongBits(value));
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final byte[] src, final int srcIndex, final int length) {
        this.ensureAccessible();
        this.ensureWritable(length);
        this.setBytes(this.writerIndex, src, srcIndex, length);
        this.writerIndex += length;
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final byte[] src) {
        this.writeBytes(src, 0, src.length);
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src) {
        this.writeBytes(src, src.readableBytes());
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src, final int length) {
        if (length > src.readableBytes()) {
            throw new IndexOutOfBoundsException(String.format("length(%d) exceeds src.readableBytes(%d) where src is: %s", length, src.readableBytes(), src));
        }
        this.writeBytes(src, src.readerIndex(), length);
        src.readerIndex(src.readerIndex() + length);
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src, final int srcIndex, final int length) {
        this.ensureAccessible();
        this.ensureWritable(length);
        this.setBytes(this.writerIndex, src, srcIndex, length);
        this.writerIndex += length;
        return this;
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuffer src) {
        this.ensureAccessible();
        final int length = src.remaining();
        this.ensureWritable(length);
        this.setBytes(this.writerIndex, src);
        this.writerIndex += length;
        return this;
    }
    
    @Override
    public int writeBytes(final InputStream in, final int length) throws IOException {
        this.ensureAccessible();
        this.ensureWritable(length);
        final int writtenBytes = this.setBytes(this.writerIndex, in, length);
        if (writtenBytes > 0) {
            this.writerIndex += writtenBytes;
        }
        return writtenBytes;
    }
    
    @Override
    public int writeBytes(final ScatteringByteChannel in, final int length) throws IOException {
        this.ensureAccessible();
        this.ensureWritable(length);
        final int writtenBytes = this.setBytes(this.writerIndex, in, length);
        if (writtenBytes > 0) {
            this.writerIndex += writtenBytes;
        }
        return writtenBytes;
    }
    
    @Override
    public int writeBytes(final FileChannel in, final long position, final int length) throws IOException {
        this.ensureAccessible();
        this.ensureWritable(length);
        final int writtenBytes = this.setBytes(this.writerIndex, in, position, length);
        if (writtenBytes > 0) {
            this.writerIndex += writtenBytes;
        }
        return writtenBytes;
    }
    
    @Override
    public ByteBuf writeZero(final int length) {
        if (length == 0) {
            return this;
        }
        this.ensureWritable(length);
        int wIndex = this.writerIndex;
        this.checkIndex(wIndex, length);
        final int nLong = length >>> 3;
        final int nBytes = length & 0x7;
        for (int i = nLong; i > 0; --i) {
            this._setLong(wIndex, 0L);
            wIndex += 8;
        }
        if (nBytes == 4) {
            this._setInt(wIndex, 0);
            wIndex += 4;
        }
        else if (nBytes < 4) {
            for (int i = nBytes; i > 0; --i) {
                this._setByte(wIndex, 0);
                ++wIndex;
            }
        }
        else {
            this._setInt(wIndex, 0);
            wIndex += 4;
            for (int i = nBytes - 4; i > 0; --i) {
                this._setByte(wIndex, 0);
                ++wIndex;
            }
        }
        this.writerIndex = wIndex;
        return this;
    }
    
    @Override
    public int writeCharSequence(final CharSequence sequence, final Charset charset) {
        final int written = this.setCharSequence(this.writerIndex, sequence, charset);
        this.writerIndex += written;
        return written;
    }
    
    @Override
    public ByteBuf copy() {
        return this.copy(this.readerIndex, this.readableBytes());
    }
    
    @Override
    public ByteBuf duplicate() {
        return new UnpooledDuplicatedByteBuf(this);
    }
    
    @Override
    public ByteBuf retainedDuplicate() {
        return this.duplicate().retain();
    }
    
    @Override
    public ByteBuf slice() {
        return this.slice(this.readerIndex, this.readableBytes());
    }
    
    @Override
    public ByteBuf retainedSlice() {
        return this.slice().retain();
    }
    
    @Override
    public ByteBuf slice(final int index, final int length) {
        return new UnpooledSlicedByteBuf(this, index, length);
    }
    
    @Override
    public ByteBuf retainedSlice(final int index, final int length) {
        return this.slice(index, length).retain();
    }
    
    @Override
    public ByteBuffer nioBuffer() {
        return this.nioBuffer(this.readerIndex, this.readableBytes());
    }
    
    @Override
    public ByteBuffer[] nioBuffers() {
        return this.nioBuffers(this.readerIndex, this.readableBytes());
    }
    
    @Override
    public String toString(final Charset charset) {
        return this.toString(this.readerIndex, this.readableBytes(), charset);
    }
    
    @Override
    public String toString(final int index, final int length, final Charset charset) {
        return ByteBufUtil.decodeString(this, index, length, charset);
    }
    
    @Override
    public int indexOf(final int fromIndex, final int toIndex, final byte value) {
        return ByteBufUtil.indexOf(this, fromIndex, toIndex, value);
    }
    
    @Override
    public int bytesBefore(final byte value) {
        return this.bytesBefore(this.readerIndex(), this.readableBytes(), value);
    }
    
    @Override
    public int bytesBefore(final int length, final byte value) {
        this.checkReadableBytes(length);
        return this.bytesBefore(this.readerIndex(), length, value);
    }
    
    @Override
    public int bytesBefore(final int index, final int length, final byte value) {
        final int endIndex = this.indexOf(index, index + length, value);
        if (endIndex < 0) {
            return -1;
        }
        return endIndex - index;
    }
    
    @Override
    public int forEachByte(final ByteProcessor processor) {
        this.ensureAccessible();
        try {
            return this.forEachByteAsc0(this.readerIndex, this.writerIndex, processor);
        }
        catch (final Exception e) {
            PlatformDependent.throwException(e);
            return -1;
        }
    }
    
    @Override
    public int forEachByte(final int index, final int length, final ByteProcessor processor) {
        this.checkIndex(index, length);
        try {
            return this.forEachByteAsc0(index, index + length, processor);
        }
        catch (final Exception e) {
            PlatformDependent.throwException(e);
            return -1;
        }
    }
    
    private int forEachByteAsc0(int start, final int end, final ByteProcessor processor) throws Exception {
        while (start < end) {
            if (!processor.process(this._getByte(start))) {
                return start;
            }
            ++start;
        }
        return -1;
    }
    
    @Override
    public int forEachByteDesc(final ByteProcessor processor) {
        this.ensureAccessible();
        try {
            return this.forEachByteDesc0(this.writerIndex - 1, this.readerIndex, processor);
        }
        catch (final Exception e) {
            PlatformDependent.throwException(e);
            return -1;
        }
    }
    
    @Override
    public int forEachByteDesc(final int index, final int length, final ByteProcessor processor) {
        this.checkIndex(index, length);
        try {
            return this.forEachByteDesc0(index + length - 1, index, processor);
        }
        catch (final Exception e) {
            PlatformDependent.throwException(e);
            return -1;
        }
    }
    
    private int forEachByteDesc0(int rStart, final int rEnd, final ByteProcessor processor) throws Exception {
        while (rStart >= rEnd) {
            if (!processor.process(this._getByte(rStart))) {
                return rStart;
            }
            --rStart;
        }
        return -1;
    }
    
    @Override
    public int hashCode() {
        return ByteBufUtil.hashCode(this);
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof ByteBuf && ByteBufUtil.equals(this, (ByteBuf)o));
    }
    
    @Override
    public int compareTo(final ByteBuf that) {
        return ByteBufUtil.compare(this, that);
    }
    
    @Override
    public String toString() {
        if (this.refCnt() == 0) {
            return StringUtil.simpleClassName(this) + "(freed)";
        }
        final StringBuilder buf = new StringBuilder().append(StringUtil.simpleClassName(this)).append("(ridx: ").append(this.readerIndex).append(", widx: ").append(this.writerIndex).append(", cap: ").append(this.capacity());
        if (this.maxCapacity != Integer.MAX_VALUE) {
            buf.append('/').append(this.maxCapacity);
        }
        final ByteBuf unwrapped = this.unwrap();
        if (unwrapped != null) {
            buf.append(", unwrapped: ").append(unwrapped);
        }
        buf.append(')');
        return buf.toString();
    }
    
    protected final void checkIndex(final int index) {
        this.checkIndex(index, 1);
    }
    
    protected final void checkIndex(final int index, final int fieldLength) {
        this.ensureAccessible();
        this.checkIndex0(index, fieldLength);
    }
    
    final void checkIndex0(final int index, final int fieldLength) {
        if (MathUtil.isOutOfBounds(index, fieldLength, this.capacity())) {
            throw new IndexOutOfBoundsException(String.format("index: %d, length: %d (expected: range(0, %d))", index, fieldLength, this.capacity()));
        }
    }
    
    protected final void checkSrcIndex(final int index, final int length, final int srcIndex, final int srcCapacity) {
        this.checkIndex(index, length);
        if (MathUtil.isOutOfBounds(srcIndex, length, srcCapacity)) {
            throw new IndexOutOfBoundsException(String.format("srcIndex: %d, length: %d (expected: range(0, %d))", srcIndex, length, srcCapacity));
        }
    }
    
    protected final void checkDstIndex(final int index, final int length, final int dstIndex, final int dstCapacity) {
        this.checkIndex(index, length);
        if (MathUtil.isOutOfBounds(dstIndex, length, dstCapacity)) {
            throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", dstIndex, length, dstCapacity));
        }
    }
    
    protected final void checkReadableBytes(final int minimumReadableBytes) {
        if (minimumReadableBytes < 0) {
            throw new IllegalArgumentException("minimumReadableBytes: " + minimumReadableBytes + " (expected: >= 0)");
        }
        this.checkReadableBytes0(minimumReadableBytes);
    }
    
    protected final void checkNewCapacity(final int newCapacity) {
        this.ensureAccessible();
        if (newCapacity < 0 || newCapacity > this.maxCapacity()) {
            throw new IllegalArgumentException("newCapacity: " + newCapacity + " (expected: 0-" + this.maxCapacity() + ')');
        }
    }
    
    private void checkReadableBytes0(final int minimumReadableBytes) {
        this.ensureAccessible();
        if (this.readerIndex > this.writerIndex - minimumReadableBytes) {
            throw new IndexOutOfBoundsException(String.format("readerIndex(%d) + length(%d) exceeds writerIndex(%d): %s", this.readerIndex, minimumReadableBytes, this.writerIndex, this));
        }
    }
    
    protected final void ensureAccessible() {
        if (AbstractByteBuf.checkAccessible && this.refCnt() == 0) {
            throw new IllegalReferenceCountException(0);
        }
    }
    
    final void setIndex0(final int readerIndex, final int writerIndex) {
        this.readerIndex = readerIndex;
        this.writerIndex = writerIndex;
    }
    
    final void discardMarks() {
        final int n = 0;
        this.markedWriterIndex = n;
        this.markedReaderIndex = n;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(AbstractByteBuf.class);
        checkAccessible = SystemPropertyUtil.getBoolean("io.netty.buffer.bytebuf.checkAccessible", true);
        if (AbstractByteBuf.logger.isDebugEnabled()) {
            AbstractByteBuf.logger.debug("-D{}: {}", "io.netty.buffer.bytebuf.checkAccessible", AbstractByteBuf.checkAccessible);
        }
        leakDetector = ResourceLeakDetectorFactory.instance().newResourceLeakDetector(ByteBuf.class);
    }
}
