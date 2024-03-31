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

final class PooledDuplicatedByteBuf extends AbstractPooledDerivedByteBuf
{
    private static final Recycler<PooledDuplicatedByteBuf> RECYCLER;
    
    static PooledDuplicatedByteBuf newInstance(final AbstractByteBuf unwrapped, final ByteBuf wrapped, final int readerIndex, final int writerIndex) {
        final PooledDuplicatedByteBuf duplicate = PooledDuplicatedByteBuf.RECYCLER.get();
        duplicate.init(unwrapped, wrapped, readerIndex, writerIndex, unwrapped.maxCapacity());
        duplicate.markReaderIndex();
        duplicate.markWriterIndex();
        return duplicate;
    }
    
    private PooledDuplicatedByteBuf(final Recycler.Handle<PooledDuplicatedByteBuf> handle) {
        super(handle);
    }
    
    @Override
    public int capacity() {
        return this.unwrap().capacity();
    }
    
    @Override
    public ByteBuf capacity(final int newCapacity) {
        this.unwrap().capacity(newCapacity);
        return this;
    }
    
    @Override
    public int arrayOffset() {
        return this.unwrap().arrayOffset();
    }
    
    @Override
    public long memoryAddress() {
        return this.unwrap().memoryAddress();
    }
    
    @Override
    public ByteBuffer nioBuffer(final int index, final int length) {
        return this.unwrap().nioBuffer(index, length);
    }
    
    @Override
    public ByteBuffer[] nioBuffers(final int index, final int length) {
        return this.unwrap().nioBuffers(index, length);
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        return this.unwrap().copy(index, length);
    }
    
    @Override
    public ByteBuf retainedSlice(final int index, final int length) {
        return PooledSlicedByteBuf.newInstance(this.unwrap(), this, index, length);
    }
    
    @Override
    public ByteBuf duplicate() {
        return this.duplicate0().setIndex(this.readerIndex(), this.writerIndex());
    }
    
    @Override
    public ByteBuf retainedDuplicate() {
        return newInstance(this.unwrap(), this, this.readerIndex(), this.writerIndex());
    }
    
    @Override
    public byte getByte(final int index) {
        return this.unwrap().getByte(index);
    }
    
    @Override
    protected byte _getByte(final int index) {
        return this.unwrap()._getByte(index);
    }
    
    @Override
    public short getShort(final int index) {
        return this.unwrap().getShort(index);
    }
    
    @Override
    protected short _getShort(final int index) {
        return this.unwrap()._getShort(index);
    }
    
    @Override
    public short getShortLE(final int index) {
        return this.unwrap().getShortLE(index);
    }
    
    @Override
    protected short _getShortLE(final int index) {
        return this.unwrap()._getShortLE(index);
    }
    
    @Override
    public int getUnsignedMedium(final int index) {
        return this.unwrap().getUnsignedMedium(index);
    }
    
    @Override
    protected int _getUnsignedMedium(final int index) {
        return this.unwrap()._getUnsignedMedium(index);
    }
    
    @Override
    public int getUnsignedMediumLE(final int index) {
        return this.unwrap().getUnsignedMediumLE(index);
    }
    
    @Override
    protected int _getUnsignedMediumLE(final int index) {
        return this.unwrap()._getUnsignedMediumLE(index);
    }
    
    @Override
    public int getInt(final int index) {
        return this.unwrap().getInt(index);
    }
    
    @Override
    protected int _getInt(final int index) {
        return this.unwrap()._getInt(index);
    }
    
    @Override
    public int getIntLE(final int index) {
        return this.unwrap().getIntLE(index);
    }
    
    @Override
    protected int _getIntLE(final int index) {
        return this.unwrap()._getIntLE(index);
    }
    
    @Override
    public long getLong(final int index) {
        return this.unwrap().getLong(index);
    }
    
    @Override
    protected long _getLong(final int index) {
        return this.unwrap()._getLong(index);
    }
    
    @Override
    public long getLongLE(final int index) {
        return this.unwrap().getLongLE(index);
    }
    
    @Override
    protected long _getLongLE(final int index) {
        return this.unwrap()._getLongLE(index);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length) {
        this.unwrap().getBytes(index, dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        this.unwrap().getBytes(index, dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuffer dst) {
        this.unwrap().getBytes(index, dst);
        return this;
    }
    
    @Override
    public ByteBuf setByte(final int index, final int value) {
        this.unwrap().setByte(index, value);
        return this;
    }
    
    @Override
    protected void _setByte(final int index, final int value) {
        this.unwrap()._setByte(index, value);
    }
    
    @Override
    public ByteBuf setShort(final int index, final int value) {
        this.unwrap().setShort(index, value);
        return this;
    }
    
    @Override
    protected void _setShort(final int index, final int value) {
        this.unwrap()._setShort(index, value);
    }
    
    @Override
    public ByteBuf setShortLE(final int index, final int value) {
        this.unwrap().setShortLE(index, value);
        return this;
    }
    
    @Override
    protected void _setShortLE(final int index, final int value) {
        this.unwrap()._setShortLE(index, value);
    }
    
    @Override
    public ByteBuf setMedium(final int index, final int value) {
        this.unwrap().setMedium(index, value);
        return this;
    }
    
    @Override
    protected void _setMedium(final int index, final int value) {
        this.unwrap()._setMedium(index, value);
    }
    
    @Override
    public ByteBuf setMediumLE(final int index, final int value) {
        this.unwrap().setMediumLE(index, value);
        return this;
    }
    
    @Override
    protected void _setMediumLE(final int index, final int value) {
        this.unwrap()._setMediumLE(index, value);
    }
    
    @Override
    public ByteBuf setInt(final int index, final int value) {
        this.unwrap().setInt(index, value);
        return this;
    }
    
    @Override
    protected void _setInt(final int index, final int value) {
        this.unwrap()._setInt(index, value);
    }
    
    @Override
    public ByteBuf setIntLE(final int index, final int value) {
        this.unwrap().setIntLE(index, value);
        return this;
    }
    
    @Override
    protected void _setIntLE(final int index, final int value) {
        this.unwrap()._setIntLE(index, value);
    }
    
    @Override
    public ByteBuf setLong(final int index, final long value) {
        this.unwrap().setLong(index, value);
        return this;
    }
    
    @Override
    protected void _setLong(final int index, final long value) {
        this.unwrap()._setLong(index, value);
    }
    
    @Override
    public ByteBuf setLongLE(final int index, final long value) {
        this.unwrap().setLongLE(index, value);
        return this;
    }
    
    @Override
    protected void _setLongLE(final int index, final long value) {
        this.unwrap().setLongLE(index, value);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        this.unwrap().setBytes(index, src, srcIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
        this.unwrap().setBytes(index, src, srcIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuffer src) {
        this.unwrap().setBytes(index, src);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final OutputStream out, final int length) throws IOException {
        this.unwrap().getBytes(index, out, length);
        return this;
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        return this.unwrap().getBytes(index, out, length);
    }
    
    @Override
    public int getBytes(final int index, final FileChannel out, final long position, final int length) throws IOException {
        return this.unwrap().getBytes(index, out, position, length);
    }
    
    @Override
    public int setBytes(final int index, final InputStream in, final int length) throws IOException {
        return this.unwrap().setBytes(index, in, length);
    }
    
    @Override
    public int setBytes(final int index, final ScatteringByteChannel in, final int length) throws IOException {
        return this.unwrap().setBytes(index, in, length);
    }
    
    @Override
    public int setBytes(final int index, final FileChannel in, final long position, final int length) throws IOException {
        return this.unwrap().setBytes(index, in, position, length);
    }
    
    @Override
    public int forEachByte(final int index, final int length, final ByteProcessor processor) {
        return this.unwrap().forEachByte(index, length, processor);
    }
    
    @Override
    public int forEachByteDesc(final int index, final int length, final ByteProcessor processor) {
        return this.unwrap().forEachByteDesc(index, length, processor);
    }
    
    static {
        RECYCLER = new Recycler<PooledDuplicatedByteBuf>() {
            @Override
            protected PooledDuplicatedByteBuf newObject(final Handle<PooledDuplicatedByteBuf> handle) {
                return new PooledDuplicatedByteBuf(handle, null);
            }
        };
    }
}
