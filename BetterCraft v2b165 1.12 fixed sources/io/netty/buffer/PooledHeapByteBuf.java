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
import java.nio.ByteBuffer;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.Recycler;

class PooledHeapByteBuf extends PooledByteBuf<byte[]>
{
    private static final Recycler<PooledHeapByteBuf> RECYCLER;
    
    static PooledHeapByteBuf newInstance(final int maxCapacity) {
        final PooledHeapByteBuf buf = PooledHeapByteBuf.RECYCLER.get();
        buf.reuse(maxCapacity);
        return buf;
    }
    
    PooledHeapByteBuf(final Recycler.Handle<? extends PooledHeapByteBuf> recyclerHandle, final int maxCapacity) {
        super(recyclerHandle, maxCapacity);
    }
    
    @Override
    public final boolean isDirect() {
        return false;
    }
    
    @Override
    protected byte _getByte(final int index) {
        return HeapByteBufUtil.getByte((byte[])(Object)this.memory, this.idx(index));
    }
    
    @Override
    protected short _getShort(final int index) {
        return HeapByteBufUtil.getShort((byte[])(Object)this.memory, this.idx(index));
    }
    
    @Override
    protected short _getShortLE(final int index) {
        return HeapByteBufUtil.getShortLE((byte[])(Object)this.memory, this.idx(index));
    }
    
    @Override
    protected int _getUnsignedMedium(final int index) {
        return HeapByteBufUtil.getUnsignedMedium((byte[])(Object)this.memory, this.idx(index));
    }
    
    @Override
    protected int _getUnsignedMediumLE(final int index) {
        return HeapByteBufUtil.getUnsignedMediumLE((byte[])(Object)this.memory, this.idx(index));
    }
    
    @Override
    protected int _getInt(final int index) {
        return HeapByteBufUtil.getInt((byte[])(Object)this.memory, this.idx(index));
    }
    
    @Override
    protected int _getIntLE(final int index) {
        return HeapByteBufUtil.getIntLE((byte[])(Object)this.memory, this.idx(index));
    }
    
    @Override
    protected long _getLong(final int index) {
        return HeapByteBufUtil.getLong((byte[])(Object)this.memory, this.idx(index));
    }
    
    @Override
    protected long _getLongLE(final int index) {
        return HeapByteBufUtil.getLongLE((byte[])(Object)this.memory, this.idx(index));
    }
    
    @Override
    public final ByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length) {
        this.checkDstIndex(index, length, dstIndex, dst.capacity());
        if (dst.hasMemoryAddress()) {
            PlatformDependent.copyMemory((byte[])(Object)this.memory, this.idx(index), dst.memoryAddress() + dstIndex, length);
        }
        else if (dst.hasArray()) {
            this.getBytes(index, dst.array(), dst.arrayOffset() + dstIndex, length);
        }
        else {
            dst.setBytes(dstIndex, (byte[])(Object)this.memory, this.idx(index), length);
        }
        return this;
    }
    
    @Override
    public final ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        this.checkDstIndex(index, length, dstIndex, dst.length);
        System.arraycopy(this.memory, this.idx(index), dst, dstIndex, length);
        return this;
    }
    
    @Override
    public final ByteBuf getBytes(final int index, final ByteBuffer dst) {
        this.checkIndex(index, dst.remaining());
        dst.put((byte[])(Object)this.memory, this.idx(index), dst.remaining());
        return this;
    }
    
    @Override
    public final ByteBuf getBytes(final int index, final OutputStream out, final int length) throws IOException {
        this.checkIndex(index, length);
        out.write((byte[])(Object)this.memory, this.idx(index), length);
        return this;
    }
    
    @Override
    public final int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        return this.getBytes(index, out, length, false);
    }
    
    private int getBytes(int index, final GatheringByteChannel out, final int length, final boolean internal) throws IOException {
        this.checkIndex(index, length);
        index = this.idx(index);
        ByteBuffer tmpBuf;
        if (internal) {
            tmpBuf = this.internalNioBuffer();
        }
        else {
            tmpBuf = ByteBuffer.wrap((byte[])(Object)this.memory);
        }
        return out.write((ByteBuffer)tmpBuf.clear().position(index).limit(index + length));
    }
    
    @Override
    public final int getBytes(final int index, final FileChannel out, final long position, final int length) throws IOException {
        return this.getBytes(index, out, position, length, false);
    }
    
    private int getBytes(int index, final FileChannel out, final long position, final int length, final boolean internal) throws IOException {
        this.checkIndex(index, length);
        index = this.idx(index);
        final ByteBuffer tmpBuf = internal ? this.internalNioBuffer() : ByteBuffer.wrap((byte[])(Object)this.memory);
        return out.write((ByteBuffer)tmpBuf.clear().position(index).limit(index + length), position);
    }
    
    @Override
    public final int readBytes(final GatheringByteChannel out, final int length) throws IOException {
        this.checkReadableBytes(length);
        final int readBytes = this.getBytes(this.readerIndex, out, length, true);
        this.readerIndex += readBytes;
        return readBytes;
    }
    
    @Override
    public final int readBytes(final FileChannel out, final long position, final int length) throws IOException {
        this.checkReadableBytes(length);
        final int readBytes = this.getBytes(this.readerIndex, out, position, length, true);
        this.readerIndex += readBytes;
        return readBytes;
    }
    
    @Override
    protected void _setByte(final int index, final int value) {
        HeapByteBufUtil.setByte((byte[])(Object)this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setShort(final int index, final int value) {
        HeapByteBufUtil.setShort((byte[])(Object)this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setShortLE(final int index, final int value) {
        HeapByteBufUtil.setShortLE((byte[])(Object)this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setMedium(final int index, final int value) {
        HeapByteBufUtil.setMedium((byte[])(Object)this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setMediumLE(final int index, final int value) {
        HeapByteBufUtil.setMediumLE((byte[])(Object)this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setInt(final int index, final int value) {
        HeapByteBufUtil.setInt((byte[])(Object)this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setIntLE(final int index, final int value) {
        HeapByteBufUtil.setIntLE((byte[])(Object)this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setLong(final int index, final long value) {
        HeapByteBufUtil.setLong((byte[])(Object)this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setLongLE(final int index, final long value) {
        HeapByteBufUtil.setLongLE((byte[])(Object)this.memory, this.idx(index), value);
    }
    
    @Override
    public final ByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
        this.checkSrcIndex(index, length, srcIndex, src.capacity());
        if (src.hasMemoryAddress()) {
            PlatformDependent.copyMemory(src.memoryAddress() + srcIndex, (byte[])(Object)this.memory, this.idx(index), length);
        }
        else if (src.hasArray()) {
            this.setBytes(index, src.array(), src.arrayOffset() + srcIndex, length);
        }
        else {
            src.getBytes(srcIndex, (byte[])(Object)this.memory, this.idx(index), length);
        }
        return this;
    }
    
    @Override
    public final ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        this.checkSrcIndex(index, length, srcIndex, src.length);
        System.arraycopy(src, srcIndex, this.memory, this.idx(index), length);
        return this;
    }
    
    @Override
    public final ByteBuf setBytes(final int index, final ByteBuffer src) {
        final int length = src.remaining();
        this.checkIndex(index, length);
        src.get((byte[])(Object)this.memory, this.idx(index), length);
        return this;
    }
    
    @Override
    public final int setBytes(final int index, final InputStream in, final int length) throws IOException {
        this.checkIndex(index, length);
        return in.read((byte[])(Object)this.memory, this.idx(index), length);
    }
    
    @Override
    public final int setBytes(int index, final ScatteringByteChannel in, final int length) throws IOException {
        this.checkIndex(index, length);
        index = this.idx(index);
        try {
            return in.read((ByteBuffer)this.internalNioBuffer().clear().position(index).limit(index + length));
        }
        catch (final ClosedChannelException ignored) {
            return -1;
        }
    }
    
    @Override
    public final int setBytes(int index, final FileChannel in, final long position, final int length) throws IOException {
        this.checkIndex(index, length);
        index = this.idx(index);
        try {
            return in.read((ByteBuffer)this.internalNioBuffer().clear().position(index).limit(index + length), position);
        }
        catch (final ClosedChannelException ignored) {
            return -1;
        }
    }
    
    @Override
    public final ByteBuf copy(final int index, final int length) {
        this.checkIndex(index, length);
        final ByteBuf copy = this.alloc().heapBuffer(length, this.maxCapacity());
        copy.writeBytes((byte[])(Object)this.memory, this.idx(index), length);
        return copy;
    }
    
    @Override
    public final int nioBufferCount() {
        return 1;
    }
    
    @Override
    public final ByteBuffer[] nioBuffers(final int index, final int length) {
        return new ByteBuffer[] { this.nioBuffer(index, length) };
    }
    
    @Override
    public final ByteBuffer nioBuffer(int index, final int length) {
        this.checkIndex(index, length);
        index = this.idx(index);
        final ByteBuffer buf = ByteBuffer.wrap((byte[])(Object)this.memory, index, length);
        return buf.slice();
    }
    
    @Override
    public final ByteBuffer internalNioBuffer(int index, final int length) {
        this.checkIndex(index, length);
        index = this.idx(index);
        return (ByteBuffer)this.internalNioBuffer().clear().position(index).limit(index + length);
    }
    
    @Override
    public final boolean hasArray() {
        return true;
    }
    
    @Override
    public final byte[] array() {
        this.ensureAccessible();
        return (byte[])(Object)this.memory;
    }
    
    @Override
    public final int arrayOffset() {
        return this.offset;
    }
    
    @Override
    public final boolean hasMemoryAddress() {
        return false;
    }
    
    @Override
    public final long memoryAddress() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected final ByteBuffer newInternalNioBuffer(final byte[] memory) {
        return ByteBuffer.wrap(memory);
    }
    
    static {
        RECYCLER = new Recycler<PooledHeapByteBuf>() {
            @Override
            protected PooledHeapByteBuf newObject(final Handle<PooledHeapByteBuf> handle) {
                return new PooledHeapByteBuf(handle, 0);
            }
        };
    }
}
