// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.io.IOException;
import java.io.OutputStream;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.Recycler;
import java.nio.ByteBuffer;

final class PooledUnsafeDirectByteBuf extends PooledByteBuf<ByteBuffer>
{
    private static final Recycler<PooledUnsafeDirectByteBuf> RECYCLER;
    private long memoryAddress;
    
    static PooledUnsafeDirectByteBuf newInstance(final int maxCapacity) {
        final PooledUnsafeDirectByteBuf buf = PooledUnsafeDirectByteBuf.RECYCLER.get();
        buf.reuse(maxCapacity);
        return buf;
    }
    
    private PooledUnsafeDirectByteBuf(final Recycler.Handle<PooledUnsafeDirectByteBuf> recyclerHandle, final int maxCapacity) {
        super(recyclerHandle, maxCapacity);
    }
    
    @Override
    void init(final PoolChunk<ByteBuffer> chunk, final long handle, final int offset, final int length, final int maxLength, final PoolThreadCache cache) {
        super.init(chunk, handle, offset, length, maxLength, cache);
        this.initMemoryAddress();
    }
    
    @Override
    void initUnpooled(final PoolChunk<ByteBuffer> chunk, final int length) {
        super.initUnpooled(chunk, length);
        this.initMemoryAddress();
    }
    
    private void initMemoryAddress() {
        this.memoryAddress = PlatformDependent.directBufferAddress((ByteBuffer)this.memory) + this.offset;
    }
    
    @Override
    protected ByteBuffer newInternalNioBuffer(final ByteBuffer memory) {
        return memory.duplicate();
    }
    
    @Override
    public boolean isDirect() {
        return true;
    }
    
    @Override
    protected byte _getByte(final int index) {
        return UnsafeByteBufUtil.getByte(this.addr(index));
    }
    
    @Override
    protected short _getShort(final int index) {
        return UnsafeByteBufUtil.getShort(this.addr(index));
    }
    
    @Override
    protected short _getShortLE(final int index) {
        return UnsafeByteBufUtil.getShortLE(this.addr(index));
    }
    
    @Override
    protected int _getUnsignedMedium(final int index) {
        return UnsafeByteBufUtil.getUnsignedMedium(this.addr(index));
    }
    
    @Override
    protected int _getUnsignedMediumLE(final int index) {
        return UnsafeByteBufUtil.getUnsignedMediumLE(this.addr(index));
    }
    
    @Override
    protected int _getInt(final int index) {
        return UnsafeByteBufUtil.getInt(this.addr(index));
    }
    
    @Override
    protected int _getIntLE(final int index) {
        return UnsafeByteBufUtil.getIntLE(this.addr(index));
    }
    
    @Override
    protected long _getLong(final int index) {
        return UnsafeByteBufUtil.getLong(this.addr(index));
    }
    
    @Override
    protected long _getLongLE(final int index) {
        return UnsafeByteBufUtil.getLongLE(this.addr(index));
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length) {
        UnsafeByteBufUtil.getBytes(this, this.addr(index), index, dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        UnsafeByteBufUtil.getBytes(this, this.addr(index), index, dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuffer dst) {
        UnsafeByteBufUtil.getBytes(this, this.addr(index), index, dst);
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
    public ByteBuf getBytes(final int index, final OutputStream out, final int length) throws IOException {
        UnsafeByteBufUtil.getBytes(this, this.addr(index), index, out, length);
        return this;
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        return this.getBytes(index, out, length, false);
    }
    
    private int getBytes(int index, final GatheringByteChannel out, final int length, final boolean internal) throws IOException {
        this.checkIndex(index, length);
        if (length == 0) {
            return 0;
        }
        ByteBuffer tmpBuf;
        if (internal) {
            tmpBuf = this.internalNioBuffer();
        }
        else {
            tmpBuf = ((ByteBuffer)this.memory).duplicate();
        }
        index = this.idx(index);
        tmpBuf.clear().position(index).limit(index + length);
        return out.write(tmpBuf);
    }
    
    @Override
    public int getBytes(final int index, final FileChannel out, final long position, final int length) throws IOException {
        return this.getBytes(index, out, position, length, false);
    }
    
    private int getBytes(int index, final FileChannel out, final long position, final int length, final boolean internal) throws IOException {
        this.checkIndex(index, length);
        if (length == 0) {
            return 0;
        }
        final ByteBuffer tmpBuf = internal ? this.internalNioBuffer() : ((ByteBuffer)this.memory).duplicate();
        index = this.idx(index);
        tmpBuf.clear().position(index).limit(index + length);
        return out.write(tmpBuf, position);
    }
    
    @Override
    public int readBytes(final GatheringByteChannel out, final int length) throws IOException {
        this.checkReadableBytes(length);
        final int readBytes = this.getBytes(this.readerIndex, out, length, true);
        this.readerIndex += readBytes;
        return readBytes;
    }
    
    @Override
    public int readBytes(final FileChannel out, final long position, final int length) throws IOException {
        this.checkReadableBytes(length);
        final int readBytes = this.getBytes(this.readerIndex, out, position, length, true);
        this.readerIndex += readBytes;
        return readBytes;
    }
    
    @Override
    protected void _setByte(final int index, final int value) {
        UnsafeByteBufUtil.setByte(this.addr(index), (byte)value);
    }
    
    @Override
    protected void _setShort(final int index, final int value) {
        UnsafeByteBufUtil.setShort(this.addr(index), value);
    }
    
    @Override
    protected void _setShortLE(final int index, final int value) {
        UnsafeByteBufUtil.setShortLE(this.addr(index), value);
    }
    
    @Override
    protected void _setMedium(final int index, final int value) {
        UnsafeByteBufUtil.setMedium(this.addr(index), value);
    }
    
    @Override
    protected void _setMediumLE(final int index, final int value) {
        UnsafeByteBufUtil.setMediumLE(this.addr(index), value);
    }
    
    @Override
    protected void _setInt(final int index, final int value) {
        UnsafeByteBufUtil.setInt(this.addr(index), value);
    }
    
    @Override
    protected void _setIntLE(final int index, final int value) {
        UnsafeByteBufUtil.setIntLE(this.addr(index), value);
    }
    
    @Override
    protected void _setLong(final int index, final long value) {
        UnsafeByteBufUtil.setLong(this.addr(index), value);
    }
    
    @Override
    protected void _setLongLE(final int index, final long value) {
        UnsafeByteBufUtil.setLongLE(this.addr(index), value);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
        UnsafeByteBufUtil.setBytes(this, this.addr(index), index, src, srcIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        UnsafeByteBufUtil.setBytes(this, this.addr(index), index, src, srcIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuffer src) {
        UnsafeByteBufUtil.setBytes(this, this.addr(index), index, src);
        return this;
    }
    
    @Override
    public int setBytes(final int index, final InputStream in, final int length) throws IOException {
        return UnsafeByteBufUtil.setBytes(this, this.addr(index), index, in, length);
    }
    
    @Override
    public int setBytes(int index, final ScatteringByteChannel in, final int length) throws IOException {
        this.checkIndex(index, length);
        final ByteBuffer tmpBuf = this.internalNioBuffer();
        index = this.idx(index);
        tmpBuf.clear().position(index).limit(index + length);
        try {
            return in.read(tmpBuf);
        }
        catch (final ClosedChannelException ignored) {
            return -1;
        }
    }
    
    @Override
    public int setBytes(int index, final FileChannel in, final long position, final int length) throws IOException {
        this.checkIndex(index, length);
        final ByteBuffer tmpBuf = this.internalNioBuffer();
        index = this.idx(index);
        tmpBuf.clear().position(index).limit(index + length);
        try {
            return in.read(tmpBuf, position);
        }
        catch (final ClosedChannelException ignored) {
            return -1;
        }
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        return UnsafeByteBufUtil.copy(this, this.addr(index), index, length);
    }
    
    @Override
    public int nioBufferCount() {
        return 1;
    }
    
    @Override
    public ByteBuffer[] nioBuffers(final int index, final int length) {
        return new ByteBuffer[] { this.nioBuffer(index, length) };
    }
    
    @Override
    public ByteBuffer nioBuffer(int index, final int length) {
        this.checkIndex(index, length);
        index = this.idx(index);
        return ((ByteBuffer)((ByteBuffer)this.memory).duplicate().position(index).limit(index + length)).slice();
    }
    
    @Override
    public ByteBuffer internalNioBuffer(int index, final int length) {
        this.checkIndex(index, length);
        index = this.idx(index);
        return (ByteBuffer)this.internalNioBuffer().clear().position(index).limit(index + length);
    }
    
    @Override
    public boolean hasArray() {
        return false;
    }
    
    @Override
    public byte[] array() {
        throw new UnsupportedOperationException("direct buffer");
    }
    
    @Override
    public int arrayOffset() {
        throw new UnsupportedOperationException("direct buffer");
    }
    
    @Override
    public boolean hasMemoryAddress() {
        return true;
    }
    
    @Override
    public long memoryAddress() {
        this.ensureAccessible();
        return this.memoryAddress;
    }
    
    private long addr(final int index) {
        return this.memoryAddress + index;
    }
    
    @Override
    protected SwappedByteBuf newSwappedByteBuf() {
        if (PlatformDependent.isUnaligned()) {
            return new UnsafeDirectSwappedByteBuf(this);
        }
        return super.newSwappedByteBuf();
    }
    
    @Override
    public ByteBuf setZero(final int index, final int length) {
        UnsafeByteBufUtil.setZero(this, this.addr(index), index, length);
        return this;
    }
    
    @Override
    public ByteBuf writeZero(final int length) {
        this.ensureWritable(length);
        final int wIndex = this.writerIndex;
        this.setZero(wIndex, length);
        this.writerIndex = wIndex + length;
        return this;
    }
    
    static {
        RECYCLER = new Recycler<PooledUnsafeDirectByteBuf>() {
            @Override
            protected PooledUnsafeDirectByteBuf newObject(final Handle<PooledUnsafeDirectByteBuf> handle) {
                return new PooledUnsafeDirectByteBuf(handle, 0, null);
            }
        };
    }
}
