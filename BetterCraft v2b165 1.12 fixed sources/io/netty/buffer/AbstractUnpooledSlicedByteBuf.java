// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import io.netty.util.internal.MathUtil;
import io.netty.util.ByteProcessor;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.io.IOException;
import java.io.OutputStream;
import io.netty.util.CharsetUtil;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

abstract class AbstractUnpooledSlicedByteBuf extends AbstractDerivedByteBuf
{
    private final ByteBuf buffer;
    private final int adjustment;
    
    AbstractUnpooledSlicedByteBuf(final ByteBuf buffer, final int index, final int length) {
        super(length);
        checkSliceOutOfBounds(index, length, buffer);
        if (buffer instanceof AbstractUnpooledSlicedByteBuf) {
            this.buffer = ((AbstractUnpooledSlicedByteBuf)buffer).buffer;
            this.adjustment = ((AbstractUnpooledSlicedByteBuf)buffer).adjustment + index;
        }
        else if (buffer instanceof DuplicatedByteBuf) {
            this.buffer = buffer.unwrap();
            this.adjustment = index;
        }
        else {
            this.buffer = buffer;
            this.adjustment = index;
        }
        this.initLength(length);
        this.writerIndex(length);
    }
    
    void initLength(final int length) {
    }
    
    int length() {
        return this.capacity();
    }
    
    @Override
    public ByteBuf unwrap() {
        return this.buffer;
    }
    
    @Override
    public ByteBufAllocator alloc() {
        return this.unwrap().alloc();
    }
    
    @Deprecated
    @Override
    public ByteOrder order() {
        return this.unwrap().order();
    }
    
    @Override
    public boolean isDirect() {
        return this.unwrap().isDirect();
    }
    
    @Override
    public ByteBuf capacity(final int newCapacity) {
        throw new UnsupportedOperationException("sliced buffer");
    }
    
    @Override
    public boolean hasArray() {
        return this.unwrap().hasArray();
    }
    
    @Override
    public byte[] array() {
        return this.unwrap().array();
    }
    
    @Override
    public int arrayOffset() {
        return this.idx(this.unwrap().arrayOffset());
    }
    
    @Override
    public boolean hasMemoryAddress() {
        return this.unwrap().hasMemoryAddress();
    }
    
    @Override
    public long memoryAddress() {
        return this.unwrap().memoryAddress() + this.adjustment;
    }
    
    @Override
    public byte getByte(final int index) {
        this.checkIndex0(index, 1);
        return this.unwrap().getByte(this.idx(index));
    }
    
    @Override
    protected byte _getByte(final int index) {
        return this.unwrap().getByte(this.idx(index));
    }
    
    @Override
    public short getShort(final int index) {
        this.checkIndex0(index, 2);
        return this.unwrap().getShort(this.idx(index));
    }
    
    @Override
    protected short _getShort(final int index) {
        return this.unwrap().getShort(this.idx(index));
    }
    
    @Override
    public short getShortLE(final int index) {
        this.checkIndex0(index, 2);
        return this.unwrap().getShortLE(this.idx(index));
    }
    
    @Override
    protected short _getShortLE(final int index) {
        return this.unwrap().getShortLE(this.idx(index));
    }
    
    @Override
    public int getUnsignedMedium(final int index) {
        this.checkIndex0(index, 3);
        return this.unwrap().getUnsignedMedium(this.idx(index));
    }
    
    @Override
    protected int _getUnsignedMedium(final int index) {
        return this.unwrap().getUnsignedMedium(this.idx(index));
    }
    
    @Override
    public int getUnsignedMediumLE(final int index) {
        this.checkIndex0(index, 3);
        return this.unwrap().getUnsignedMediumLE(this.idx(index));
    }
    
    @Override
    protected int _getUnsignedMediumLE(final int index) {
        return this.unwrap().getUnsignedMediumLE(this.idx(index));
    }
    
    @Override
    public int getInt(final int index) {
        this.checkIndex0(index, 4);
        return this.unwrap().getInt(this.idx(index));
    }
    
    @Override
    protected int _getInt(final int index) {
        return this.unwrap().getInt(this.idx(index));
    }
    
    @Override
    public int getIntLE(final int index) {
        this.checkIndex0(index, 4);
        return this.unwrap().getIntLE(this.idx(index));
    }
    
    @Override
    protected int _getIntLE(final int index) {
        return this.unwrap().getIntLE(this.idx(index));
    }
    
    @Override
    public long getLong(final int index) {
        this.checkIndex0(index, 8);
        return this.unwrap().getLong(this.idx(index));
    }
    
    @Override
    protected long _getLong(final int index) {
        return this.unwrap().getLong(this.idx(index));
    }
    
    @Override
    public long getLongLE(final int index) {
        this.checkIndex0(index, 8);
        return this.unwrap().getLongLE(this.idx(index));
    }
    
    @Override
    protected long _getLongLE(final int index) {
        return this.unwrap().getLongLE(this.idx(index));
    }
    
    @Override
    public ByteBuf duplicate() {
        return this.unwrap().duplicate().setIndex(this.idx(this.readerIndex()), this.idx(this.writerIndex()));
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        this.checkIndex0(index, length);
        return this.unwrap().copy(this.idx(index), length);
    }
    
    @Override
    public ByteBuf slice(final int index, final int length) {
        this.checkIndex0(index, length);
        return this.unwrap().slice(this.idx(index), length);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length) {
        this.checkIndex0(index, length);
        this.unwrap().getBytes(this.idx(index), dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        this.checkIndex0(index, length);
        this.unwrap().getBytes(this.idx(index), dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuffer dst) {
        this.checkIndex0(index, dst.remaining());
        this.unwrap().getBytes(this.idx(index), dst);
        return this;
    }
    
    @Override
    public ByteBuf setByte(final int index, final int value) {
        this.checkIndex0(index, 1);
        this.unwrap().setByte(this.idx(index), value);
        return this;
    }
    
    @Override
    public CharSequence getCharSequence(final int index, final int length, final Charset charset) {
        this.checkIndex0(index, length);
        return this.buffer.getCharSequence(this.idx(index), length, charset);
    }
    
    @Override
    protected void _setByte(final int index, final int value) {
        this.unwrap().setByte(this.idx(index), value);
    }
    
    @Override
    public ByteBuf setShort(final int index, final int value) {
        this.checkIndex0(index, 2);
        this.unwrap().setShort(this.idx(index), value);
        return this;
    }
    
    @Override
    protected void _setShort(final int index, final int value) {
        this.unwrap().setShort(this.idx(index), value);
    }
    
    @Override
    public ByteBuf setShortLE(final int index, final int value) {
        this.checkIndex0(index, 2);
        this.unwrap().setShortLE(this.idx(index), value);
        return this;
    }
    
    @Override
    protected void _setShortLE(final int index, final int value) {
        this.unwrap().setShortLE(this.idx(index), value);
    }
    
    @Override
    public ByteBuf setMedium(final int index, final int value) {
        this.checkIndex0(index, 3);
        this.unwrap().setMedium(this.idx(index), value);
        return this;
    }
    
    @Override
    protected void _setMedium(final int index, final int value) {
        this.unwrap().setMedium(this.idx(index), value);
    }
    
    @Override
    public ByteBuf setMediumLE(final int index, final int value) {
        this.checkIndex0(index, 3);
        this.unwrap().setMediumLE(this.idx(index), value);
        return this;
    }
    
    @Override
    protected void _setMediumLE(final int index, final int value) {
        this.unwrap().setMediumLE(this.idx(index), value);
    }
    
    @Override
    public ByteBuf setInt(final int index, final int value) {
        this.checkIndex0(index, 4);
        this.unwrap().setInt(this.idx(index), value);
        return this;
    }
    
    @Override
    protected void _setInt(final int index, final int value) {
        this.unwrap().setInt(this.idx(index), value);
    }
    
    @Override
    public ByteBuf setIntLE(final int index, final int value) {
        this.checkIndex0(index, 4);
        this.unwrap().setIntLE(this.idx(index), value);
        return this;
    }
    
    @Override
    protected void _setIntLE(final int index, final int value) {
        this.unwrap().setIntLE(this.idx(index), value);
    }
    
    @Override
    public ByteBuf setLong(final int index, final long value) {
        this.checkIndex0(index, 8);
        this.unwrap().setLong(this.idx(index), value);
        return this;
    }
    
    @Override
    protected void _setLong(final int index, final long value) {
        this.unwrap().setLong(this.idx(index), value);
    }
    
    @Override
    public ByteBuf setLongLE(final int index, final long value) {
        this.checkIndex0(index, 8);
        this.unwrap().setLongLE(this.idx(index), value);
        return this;
    }
    
    @Override
    protected void _setLongLE(final int index, final long value) {
        this.unwrap().setLongLE(this.idx(index), value);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        this.checkIndex0(index, length);
        this.unwrap().setBytes(this.idx(index), src, srcIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
        this.checkIndex0(index, length);
        this.unwrap().setBytes(this.idx(index), src, srcIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuffer src) {
        this.checkIndex0(index, src.remaining());
        this.unwrap().setBytes(this.idx(index), src);
        return this;
    }
    
    @Override
    public int setCharSequence(final int index, final CharSequence sequence, final Charset charset) {
        if (charset.equals(CharsetUtil.UTF_8)) {
            this.checkIndex0(index, ByteBufUtil.utf8MaxBytes(sequence));
            return ByteBufUtil.writeUtf8(this, this.idx(index), sequence, sequence.length());
        }
        if (charset.equals(CharsetUtil.US_ASCII)) {
            final int len = sequence.length();
            this.checkIndex0(index, len);
            return ByteBufUtil.writeAscii(this, this.idx(index), sequence, len);
        }
        final byte[] bytes = sequence.toString().getBytes(charset);
        this.checkIndex0(index, bytes.length);
        this.buffer.setBytes(this.idx(index), bytes);
        return bytes.length;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final OutputStream out, final int length) throws IOException {
        this.checkIndex0(index, length);
        this.unwrap().getBytes(this.idx(index), out, length);
        return this;
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        this.checkIndex0(index, length);
        return this.unwrap().getBytes(this.idx(index), out, length);
    }
    
    @Override
    public int getBytes(final int index, final FileChannel out, final long position, final int length) throws IOException {
        this.checkIndex0(index, length);
        return this.unwrap().getBytes(this.idx(index), out, position, length);
    }
    
    @Override
    public int setBytes(final int index, final InputStream in, final int length) throws IOException {
        this.checkIndex0(index, length);
        return this.unwrap().setBytes(this.idx(index), in, length);
    }
    
    @Override
    public int setBytes(final int index, final ScatteringByteChannel in, final int length) throws IOException {
        this.checkIndex0(index, length);
        return this.unwrap().setBytes(this.idx(index), in, length);
    }
    
    @Override
    public int setBytes(final int index, final FileChannel in, final long position, final int length) throws IOException {
        this.checkIndex0(index, length);
        return this.unwrap().setBytes(this.idx(index), in, position, length);
    }
    
    @Override
    public int nioBufferCount() {
        return this.unwrap().nioBufferCount();
    }
    
    @Override
    public ByteBuffer nioBuffer(final int index, final int length) {
        this.checkIndex0(index, length);
        return this.unwrap().nioBuffer(this.idx(index), length);
    }
    
    @Override
    public ByteBuffer[] nioBuffers(final int index, final int length) {
        this.checkIndex0(index, length);
        return this.unwrap().nioBuffers(this.idx(index), length);
    }
    
    @Override
    public int forEachByte(final int index, final int length, final ByteProcessor processor) {
        this.checkIndex0(index, length);
        final int ret = this.unwrap().forEachByte(this.idx(index), length, processor);
        if (ret >= this.adjustment) {
            return ret - this.adjustment;
        }
        return -1;
    }
    
    @Override
    public int forEachByteDesc(final int index, final int length, final ByteProcessor processor) {
        this.checkIndex0(index, length);
        final int ret = this.unwrap().forEachByteDesc(this.idx(index), length, processor);
        if (ret >= this.adjustment) {
            return ret - this.adjustment;
        }
        return -1;
    }
    
    final int idx(final int index) {
        return index + this.adjustment;
    }
    
    static void checkSliceOutOfBounds(final int index, final int length, final ByteBuf buffer) {
        if (MathUtil.isOutOfBounds(index, length, buffer.capacity())) {
            throw new IndexOutOfBoundsException(buffer + ".slice(" + index + ", " + length + ')');
        }
    }
}
