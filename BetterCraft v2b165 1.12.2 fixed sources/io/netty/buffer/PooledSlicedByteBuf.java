// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import io.netty.util.ByteProcessor;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import io.netty.util.Recycler;

final class PooledSlicedByteBuf extends AbstractPooledDerivedByteBuf
{
    private static final Recycler<PooledSlicedByteBuf> RECYCLER;
    int adjustment;
    
    static PooledSlicedByteBuf newInstance(final AbstractByteBuf unwrapped, final ByteBuf wrapped, final int index, final int length) {
        AbstractUnpooledSlicedByteBuf.checkSliceOutOfBounds(index, length, unwrapped);
        return newInstance0(unwrapped, wrapped, index, length);
    }
    
    private static PooledSlicedByteBuf newInstance0(final AbstractByteBuf unwrapped, final ByteBuf wrapped, final int adjustment, final int length) {
        final PooledSlicedByteBuf slice = PooledSlicedByteBuf.RECYCLER.get();
        slice.init(unwrapped, wrapped, 0, length, length);
        slice.discardMarks();
        slice.adjustment = adjustment;
        return slice;
    }
    
    private PooledSlicedByteBuf(final Recycler.Handle<PooledSlicedByteBuf> handle) {
        super(handle);
    }
    
    @Override
    public int capacity() {
        return this.maxCapacity();
    }
    
    @Override
    public ByteBuf capacity(final int newCapacity) {
        throw new UnsupportedOperationException("sliced buffer");
    }
    
    @Override
    public int arrayOffset() {
        return this.idx(this.unwrap().arrayOffset());
    }
    
    @Override
    public long memoryAddress() {
        return this.unwrap().memoryAddress() + this.adjustment;
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
    public ByteBuf copy(final int index, final int length) {
        this.checkIndex0(index, length);
        return this.unwrap().copy(this.idx(index), length);
    }
    
    @Override
    public ByteBuf slice(final int index, final int length) {
        this.checkIndex0(index, length);
        return super.slice(this.idx(index), length);
    }
    
    @Override
    public ByteBuf retainedSlice(final int index, final int length) {
        this.checkIndex0(index, length);
        return newInstance0(this.unwrap(), this, this.idx(index), length);
    }
    
    @Override
    public ByteBuf duplicate() {
        return this.duplicate0().setIndex(this.idx(this.readerIndex()), this.idx(this.writerIndex()));
    }
    
    @Override
    public ByteBuf retainedDuplicate() {
        return PooledDuplicatedByteBuf.newInstance(this.unwrap(), this, this.idx(this.readerIndex()), this.idx(this.writerIndex()));
    }
    
    @Override
    public byte getByte(final int index) {
        this.checkIndex0(index, 1);
        return this.unwrap().getByte(this.idx(index));
    }
    
    @Override
    protected byte _getByte(final int index) {
        return this.unwrap()._getByte(this.idx(index));
    }
    
    @Override
    public short getShort(final int index) {
        this.checkIndex0(index, 2);
        return this.unwrap().getShort(this.idx(index));
    }
    
    @Override
    protected short _getShort(final int index) {
        return this.unwrap()._getShort(this.idx(index));
    }
    
    @Override
    public short getShortLE(final int index) {
        this.checkIndex0(index, 2);
        return this.unwrap().getShortLE(this.idx(index));
    }
    
    @Override
    protected short _getShortLE(final int index) {
        return this.unwrap()._getShortLE(this.idx(index));
    }
    
    @Override
    public int getUnsignedMedium(final int index) {
        this.checkIndex0(index, 3);
        return this.unwrap().getUnsignedMedium(this.idx(index));
    }
    
    @Override
    protected int _getUnsignedMedium(final int index) {
        return this.unwrap()._getUnsignedMedium(this.idx(index));
    }
    
    @Override
    public int getUnsignedMediumLE(final int index) {
        this.checkIndex0(index, 3);
        return this.unwrap().getUnsignedMediumLE(this.idx(index));
    }
    
    @Override
    protected int _getUnsignedMediumLE(final int index) {
        return this.unwrap()._getUnsignedMediumLE(this.idx(index));
    }
    
    @Override
    public int getInt(final int index) {
        this.checkIndex0(index, 4);
        return this.unwrap().getInt(this.idx(index));
    }
    
    @Override
    protected int _getInt(final int index) {
        return this.unwrap()._getInt(this.idx(index));
    }
    
    @Override
    public int getIntLE(final int index) {
        this.checkIndex0(index, 4);
        return this.unwrap().getIntLE(this.idx(index));
    }
    
    @Override
    protected int _getIntLE(final int index) {
        return this.unwrap()._getIntLE(this.idx(index));
    }
    
    @Override
    public long getLong(final int index) {
        this.checkIndex0(index, 8);
        return this.unwrap().getLong(this.idx(index));
    }
    
    @Override
    protected long _getLong(final int index) {
        return this.unwrap()._getLong(this.idx(index));
    }
    
    @Override
    public long getLongLE(final int index) {
        this.checkIndex0(index, 8);
        return this.unwrap().getLongLE(this.idx(index));
    }
    
    @Override
    protected long _getLongLE(final int index) {
        return this.unwrap()._getLongLE(this.idx(index));
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
    protected void _setByte(final int index, final int value) {
        this.unwrap()._setByte(this.idx(index), value);
    }
    
    @Override
    public ByteBuf setShort(final int index, final int value) {
        this.checkIndex0(index, 2);
        this.unwrap().setShort(this.idx(index), value);
        return this;
    }
    
    @Override
    protected void _setShort(final int index, final int value) {
        this.unwrap()._setShort(this.idx(index), value);
    }
    
    @Override
    public ByteBuf setShortLE(final int index, final int value) {
        this.checkIndex0(index, 2);
        this.unwrap().setShortLE(this.idx(index), value);
        return this;
    }
    
    @Override
    protected void _setShortLE(final int index, final int value) {
        this.unwrap()._setShortLE(this.idx(index), value);
    }
    
    @Override
    public ByteBuf setMedium(final int index, final int value) {
        this.checkIndex0(index, 3);
        this.unwrap().setMedium(this.idx(index), value);
        return this;
    }
    
    @Override
    protected void _setMedium(final int index, final int value) {
        this.unwrap()._setMedium(this.idx(index), value);
    }
    
    @Override
    public ByteBuf setMediumLE(final int index, final int value) {
        this.checkIndex0(index, 3);
        this.unwrap().setMediumLE(this.idx(index), value);
        return this;
    }
    
    @Override
    protected void _setMediumLE(final int index, final int value) {
        this.unwrap()._setMediumLE(this.idx(index), value);
    }
    
    @Override
    public ByteBuf setInt(final int index, final int value) {
        this.checkIndex0(index, 4);
        this.unwrap().setInt(this.idx(index), value);
        return this;
    }
    
    @Override
    protected void _setInt(final int index, final int value) {
        this.unwrap()._setInt(this.idx(index), value);
    }
    
    @Override
    public ByteBuf setIntLE(final int index, final int value) {
        this.checkIndex0(index, 4);
        this.unwrap().setIntLE(this.idx(index), value);
        return this;
    }
    
    @Override
    protected void _setIntLE(final int index, final int value) {
        this.unwrap()._setIntLE(this.idx(index), value);
    }
    
    @Override
    public ByteBuf setLong(final int index, final long value) {
        this.checkIndex0(index, 8);
        this.unwrap().setLong(this.idx(index), value);
        return this;
    }
    
    @Override
    protected void _setLong(final int index, final long value) {
        this.unwrap()._setLong(this.idx(index), value);
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
    public int forEachByte(final int index, final int length, final ByteProcessor processor) {
        this.checkIndex0(index, length);
        final int ret = this.unwrap().forEachByte(this.idx(index), length, processor);
        if (ret < this.adjustment) {
            return -1;
        }
        return ret - this.adjustment;
    }
    
    @Override
    public int forEachByteDesc(final int index, final int length, final ByteProcessor processor) {
        this.checkIndex0(index, length);
        final int ret = this.unwrap().forEachByteDesc(this.idx(index), length, processor);
        if (ret < this.adjustment) {
            return -1;
        }
        return ret - this.adjustment;
    }
    
    private int idx(final int index) {
        return index + this.adjustment;
    }
    
    static {
        RECYCLER = new Recycler<PooledSlicedByteBuf>() {
            @Override
            protected PooledSlicedByteBuf newObject(final Handle<PooledSlicedByteBuf> handle) {
                return new PooledSlicedByteBuf(handle, null);
            }
        };
    }
}
