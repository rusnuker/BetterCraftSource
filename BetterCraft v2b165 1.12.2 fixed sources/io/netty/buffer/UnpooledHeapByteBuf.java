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
import java.nio.ByteOrder;
import io.netty.util.internal.ObjectUtil;
import java.nio.ByteBuffer;

public class UnpooledHeapByteBuf extends AbstractReferenceCountedByteBuf
{
    private final ByteBufAllocator alloc;
    byte[] array;
    private ByteBuffer tmpNioBuf;
    
    protected UnpooledHeapByteBuf(final ByteBufAllocator alloc, final int initialCapacity, final int maxCapacity) {
        super(maxCapacity);
        ObjectUtil.checkNotNull(alloc, "alloc");
        if (initialCapacity > maxCapacity) {
            throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", initialCapacity, maxCapacity));
        }
        this.alloc = alloc;
        this.setArray(this.allocateArray(initialCapacity));
        this.setIndex(0, 0);
    }
    
    protected UnpooledHeapByteBuf(final ByteBufAllocator alloc, final byte[] initialArray, final int maxCapacity) {
        super(maxCapacity);
        ObjectUtil.checkNotNull(alloc, "alloc");
        ObjectUtil.checkNotNull(initialArray, "initialArray");
        if (initialArray.length > maxCapacity) {
            throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", initialArray.length, maxCapacity));
        }
        this.alloc = alloc;
        this.setArray(initialArray);
        this.setIndex(0, initialArray.length);
    }
    
    byte[] allocateArray(final int initialCapacity) {
        return new byte[initialCapacity];
    }
    
    void freeArray(final byte[] array) {
    }
    
    private void setArray(final byte[] initialArray) {
        this.array = initialArray;
        this.tmpNioBuf = null;
    }
    
    @Override
    public ByteBufAllocator alloc() {
        return this.alloc;
    }
    
    @Override
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }
    
    @Override
    public boolean isDirect() {
        return false;
    }
    
    @Override
    public int capacity() {
        this.ensureAccessible();
        return this.array.length;
    }
    
    @Override
    public ByteBuf capacity(final int newCapacity) {
        this.checkNewCapacity(newCapacity);
        final int oldCapacity = this.array.length;
        final byte[] oldArray = this.array;
        if (newCapacity > oldCapacity) {
            final byte[] newArray = this.allocateArray(newCapacity);
            System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
            this.setArray(newArray);
            this.freeArray(oldArray);
        }
        else if (newCapacity < oldCapacity) {
            final byte[] newArray = this.allocateArray(newCapacity);
            final int readerIndex = this.readerIndex();
            if (readerIndex < newCapacity) {
                int writerIndex = this.writerIndex();
                if (writerIndex > newCapacity) {
                    writerIndex = newCapacity;
                    this.writerIndex(newCapacity);
                }
                System.arraycopy(oldArray, readerIndex, newArray, readerIndex, writerIndex - readerIndex);
            }
            else {
                this.setIndex(newCapacity, newCapacity);
            }
            this.setArray(newArray);
            this.freeArray(oldArray);
        }
        return this;
    }
    
    @Override
    public boolean hasArray() {
        return true;
    }
    
    @Override
    public byte[] array() {
        this.ensureAccessible();
        return this.array;
    }
    
    @Override
    public int arrayOffset() {
        return 0;
    }
    
    @Override
    public boolean hasMemoryAddress() {
        return false;
    }
    
    @Override
    public long memoryAddress() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length) {
        this.checkDstIndex(index, length, dstIndex, dst.capacity());
        if (dst.hasMemoryAddress()) {
            PlatformDependent.copyMemory(this.array, index, dst.memoryAddress() + dstIndex, length);
        }
        else if (dst.hasArray()) {
            this.getBytes(index, dst.array(), dst.arrayOffset() + dstIndex, length);
        }
        else {
            dst.setBytes(dstIndex, this.array, index, length);
        }
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        this.checkDstIndex(index, length, dstIndex, dst.length);
        System.arraycopy(this.array, index, dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuffer dst) {
        this.checkIndex(index, dst.remaining());
        dst.put(this.array, index, dst.remaining());
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final OutputStream out, final int length) throws IOException {
        this.ensureAccessible();
        out.write(this.array, index, length);
        return this;
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        this.ensureAccessible();
        return this.getBytes(index, out, length, false);
    }
    
    @Override
    public int getBytes(final int index, final FileChannel out, final long position, final int length) throws IOException {
        this.ensureAccessible();
        return this.getBytes(index, out, position, length, false);
    }
    
    private int getBytes(final int index, final GatheringByteChannel out, final int length, final boolean internal) throws IOException {
        this.ensureAccessible();
        ByteBuffer tmpBuf;
        if (internal) {
            tmpBuf = this.internalNioBuffer();
        }
        else {
            tmpBuf = ByteBuffer.wrap(this.array);
        }
        return out.write((ByteBuffer)tmpBuf.clear().position(index).limit(index + length));
    }
    
    private int getBytes(final int index, final FileChannel out, final long position, final int length, final boolean internal) throws IOException {
        this.ensureAccessible();
        final ByteBuffer tmpBuf = internal ? this.internalNioBuffer() : ByteBuffer.wrap(this.array);
        return out.write((ByteBuffer)tmpBuf.clear().position(index).limit(index + length), position);
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
    public ByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
        this.checkSrcIndex(index, length, srcIndex, src.capacity());
        if (src.hasMemoryAddress()) {
            PlatformDependent.copyMemory(src.memoryAddress() + srcIndex, this.array, index, length);
        }
        else if (src.hasArray()) {
            this.setBytes(index, src.array(), src.arrayOffset() + srcIndex, length);
        }
        else {
            src.getBytes(srcIndex, this.array, index, length);
        }
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        this.checkSrcIndex(index, length, srcIndex, src.length);
        System.arraycopy(src, srcIndex, this.array, index, length);
        return this;
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuffer src) {
        this.ensureAccessible();
        src.get(this.array, index, src.remaining());
        return this;
    }
    
    @Override
    public int setBytes(final int index, final InputStream in, final int length) throws IOException {
        this.ensureAccessible();
        return in.read(this.array, index, length);
    }
    
    @Override
    public int setBytes(final int index, final ScatteringByteChannel in, final int length) throws IOException {
        this.ensureAccessible();
        try {
            return in.read((ByteBuffer)this.internalNioBuffer().clear().position(index).limit(index + length));
        }
        catch (final ClosedChannelException ignored) {
            return -1;
        }
    }
    
    @Override
    public int setBytes(final int index, final FileChannel in, final long position, final int length) throws IOException {
        this.ensureAccessible();
        try {
            return in.read((ByteBuffer)this.internalNioBuffer().clear().position(index).limit(index + length), position);
        }
        catch (final ClosedChannelException ignored) {
            return -1;
        }
    }
    
    @Override
    public int nioBufferCount() {
        return 1;
    }
    
    @Override
    public ByteBuffer nioBuffer(final int index, final int length) {
        this.ensureAccessible();
        return ByteBuffer.wrap(this.array, index, length).slice();
    }
    
    @Override
    public ByteBuffer[] nioBuffers(final int index, final int length) {
        return new ByteBuffer[] { this.nioBuffer(index, length) };
    }
    
    @Override
    public ByteBuffer internalNioBuffer(final int index, final int length) {
        this.checkIndex(index, length);
        return (ByteBuffer)this.internalNioBuffer().clear().position(index).limit(index + length);
    }
    
    @Override
    public byte getByte(final int index) {
        this.ensureAccessible();
        return this._getByte(index);
    }
    
    @Override
    protected byte _getByte(final int index) {
        return HeapByteBufUtil.getByte(this.array, index);
    }
    
    @Override
    public short getShort(final int index) {
        this.ensureAccessible();
        return this._getShort(index);
    }
    
    @Override
    protected short _getShort(final int index) {
        return HeapByteBufUtil.getShort(this.array, index);
    }
    
    @Override
    public short getShortLE(final int index) {
        this.ensureAccessible();
        return this._getShortLE(index);
    }
    
    @Override
    protected short _getShortLE(final int index) {
        return HeapByteBufUtil.getShortLE(this.array, index);
    }
    
    @Override
    public int getUnsignedMedium(final int index) {
        this.ensureAccessible();
        return this._getUnsignedMedium(index);
    }
    
    @Override
    protected int _getUnsignedMedium(final int index) {
        return HeapByteBufUtil.getUnsignedMedium(this.array, index);
    }
    
    @Override
    public int getUnsignedMediumLE(final int index) {
        this.ensureAccessible();
        return this._getUnsignedMediumLE(index);
    }
    
    @Override
    protected int _getUnsignedMediumLE(final int index) {
        return HeapByteBufUtil.getUnsignedMediumLE(this.array, index);
    }
    
    @Override
    public int getInt(final int index) {
        this.ensureAccessible();
        return this._getInt(index);
    }
    
    @Override
    protected int _getInt(final int index) {
        return HeapByteBufUtil.getInt(this.array, index);
    }
    
    @Override
    public int getIntLE(final int index) {
        this.ensureAccessible();
        return this._getIntLE(index);
    }
    
    @Override
    protected int _getIntLE(final int index) {
        return HeapByteBufUtil.getIntLE(this.array, index);
    }
    
    @Override
    public long getLong(final int index) {
        this.ensureAccessible();
        return this._getLong(index);
    }
    
    @Override
    protected long _getLong(final int index) {
        return HeapByteBufUtil.getLong(this.array, index);
    }
    
    @Override
    public long getLongLE(final int index) {
        this.ensureAccessible();
        return this._getLongLE(index);
    }
    
    @Override
    protected long _getLongLE(final int index) {
        return HeapByteBufUtil.getLongLE(this.array, index);
    }
    
    @Override
    public ByteBuf setByte(final int index, final int value) {
        this.ensureAccessible();
        this._setByte(index, value);
        return this;
    }
    
    @Override
    protected void _setByte(final int index, final int value) {
        HeapByteBufUtil.setByte(this.array, index, value);
    }
    
    @Override
    public ByteBuf setShort(final int index, final int value) {
        this.ensureAccessible();
        this._setShort(index, value);
        return this;
    }
    
    @Override
    protected void _setShort(final int index, final int value) {
        HeapByteBufUtil.setShort(this.array, index, value);
    }
    
    @Override
    public ByteBuf setShortLE(final int index, final int value) {
        this.ensureAccessible();
        this._setShortLE(index, value);
        return this;
    }
    
    @Override
    protected void _setShortLE(final int index, final int value) {
        HeapByteBufUtil.setShortLE(this.array, index, value);
    }
    
    @Override
    public ByteBuf setMedium(final int index, final int value) {
        this.ensureAccessible();
        this._setMedium(index, value);
        return this;
    }
    
    @Override
    protected void _setMedium(final int index, final int value) {
        HeapByteBufUtil.setMedium(this.array, index, value);
    }
    
    @Override
    public ByteBuf setMediumLE(final int index, final int value) {
        this.ensureAccessible();
        this._setMediumLE(index, value);
        return this;
    }
    
    @Override
    protected void _setMediumLE(final int index, final int value) {
        HeapByteBufUtil.setMediumLE(this.array, index, value);
    }
    
    @Override
    public ByteBuf setInt(final int index, final int value) {
        this.ensureAccessible();
        this._setInt(index, value);
        return this;
    }
    
    @Override
    protected void _setInt(final int index, final int value) {
        HeapByteBufUtil.setInt(this.array, index, value);
    }
    
    @Override
    public ByteBuf setIntLE(final int index, final int value) {
        this.ensureAccessible();
        this._setIntLE(index, value);
        return this;
    }
    
    @Override
    protected void _setIntLE(final int index, final int value) {
        HeapByteBufUtil.setIntLE(this.array, index, value);
    }
    
    @Override
    public ByteBuf setLong(final int index, final long value) {
        this.ensureAccessible();
        this._setLong(index, value);
        return this;
    }
    
    @Override
    protected void _setLong(final int index, final long value) {
        HeapByteBufUtil.setLong(this.array, index, value);
    }
    
    @Override
    public ByteBuf setLongLE(final int index, final long value) {
        this.ensureAccessible();
        this._setLongLE(index, value);
        return this;
    }
    
    @Override
    protected void _setLongLE(final int index, final long value) {
        HeapByteBufUtil.setLongLE(this.array, index, value);
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        this.checkIndex(index, length);
        final byte[] copiedArray = new byte[length];
        System.arraycopy(this.array, index, copiedArray, 0, length);
        return new UnpooledHeapByteBuf(this.alloc(), copiedArray, this.maxCapacity());
    }
    
    private ByteBuffer internalNioBuffer() {
        ByteBuffer tmpNioBuf = this.tmpNioBuf;
        if (tmpNioBuf == null) {
            tmpNioBuf = (this.tmpNioBuf = ByteBuffer.wrap(this.array));
        }
        return tmpNioBuf;
    }
    
    @Override
    protected void deallocate() {
        this.freeArray(this.array);
        this.array = null;
    }
    
    @Override
    public ByteBuf unwrap() {
        return null;
    }
}
