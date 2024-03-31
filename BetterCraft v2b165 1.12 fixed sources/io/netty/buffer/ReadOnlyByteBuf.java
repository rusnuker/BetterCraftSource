// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import io.netty.util.ByteProcessor;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.nio.ByteOrder;

@Deprecated
public class ReadOnlyByteBuf extends AbstractDerivedByteBuf
{
    private final ByteBuf buffer;
    
    public ReadOnlyByteBuf(final ByteBuf buffer) {
        super(buffer.maxCapacity());
        if (buffer instanceof ReadOnlyByteBuf || buffer instanceof DuplicatedByteBuf) {
            this.buffer = buffer.unwrap();
        }
        else {
            this.buffer = buffer;
        }
        this.setIndex(buffer.readerIndex(), buffer.writerIndex());
    }
    
    @Override
    public boolean isReadOnly() {
        return true;
    }
    
    @Override
    public boolean isWritable() {
        return false;
    }
    
    @Override
    public boolean isWritable(final int numBytes) {
        return false;
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
    public boolean hasArray() {
        return false;
    }
    
    @Override
    public byte[] array() {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public int arrayOffset() {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public boolean hasMemoryAddress() {
        return false;
    }
    
    @Override
    public long memoryAddress() {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public ByteBuf discardReadBytes() {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuffer src) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public ByteBuf setByte(final int index, final int value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    protected void _setByte(final int index, final int value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public ByteBuf setShort(final int index, final int value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    protected void _setShort(final int index, final int value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public ByteBuf setShortLE(final int index, final int value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    protected void _setShortLE(final int index, final int value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public ByteBuf setMedium(final int index, final int value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    protected void _setMedium(final int index, final int value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public ByteBuf setMediumLE(final int index, final int value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    protected void _setMediumLE(final int index, final int value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public ByteBuf setInt(final int index, final int value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    protected void _setInt(final int index, final int value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public ByteBuf setIntLE(final int index, final int value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    protected void _setIntLE(final int index, final int value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public ByteBuf setLong(final int index, final long value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    protected void _setLong(final int index, final long value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public ByteBuf setLongLE(final int index, final long value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    protected void _setLongLE(final int index, final long value) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public int setBytes(final int index, final InputStream in, final int length) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public int setBytes(final int index, final ScatteringByteChannel in, final int length) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public int setBytes(final int index, final FileChannel in, final long position, final int length) {
        throw new ReadOnlyBufferException();
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
    public ByteBuf getBytes(final int index, final OutputStream out, final int length) throws IOException {
        this.unwrap().getBytes(index, out, length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        this.unwrap().getBytes(index, dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length) {
        this.unwrap().getBytes(index, dst, dstIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuffer dst) {
        this.unwrap().getBytes(index, dst);
        return this;
    }
    
    @Override
    public ByteBuf duplicate() {
        return new ReadOnlyByteBuf(this);
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        return this.unwrap().copy(index, length);
    }
    
    @Override
    public ByteBuf slice(final int index, final int length) {
        return Unpooled.unmodifiableBuffer(this.unwrap().slice(index, length));
    }
    
    @Override
    public byte getByte(final int index) {
        return this.unwrap().getByte(index);
    }
    
    @Override
    protected byte _getByte(final int index) {
        return this.unwrap().getByte(index);
    }
    
    @Override
    public short getShort(final int index) {
        return this.unwrap().getShort(index);
    }
    
    @Override
    protected short _getShort(final int index) {
        return this.unwrap().getShort(index);
    }
    
    @Override
    public short getShortLE(final int index) {
        return this.unwrap().getShortLE(index);
    }
    
    @Override
    protected short _getShortLE(final int index) {
        return this.unwrap().getShortLE(index);
    }
    
    @Override
    public int getUnsignedMedium(final int index) {
        return this.unwrap().getUnsignedMedium(index);
    }
    
    @Override
    protected int _getUnsignedMedium(final int index) {
        return this.unwrap().getUnsignedMedium(index);
    }
    
    @Override
    public int getUnsignedMediumLE(final int index) {
        return this.unwrap().getUnsignedMediumLE(index);
    }
    
    @Override
    protected int _getUnsignedMediumLE(final int index) {
        return this.unwrap().getUnsignedMediumLE(index);
    }
    
    @Override
    public int getInt(final int index) {
        return this.unwrap().getInt(index);
    }
    
    @Override
    protected int _getInt(final int index) {
        return this.unwrap().getInt(index);
    }
    
    @Override
    public int getIntLE(final int index) {
        return this.unwrap().getIntLE(index);
    }
    
    @Override
    protected int _getIntLE(final int index) {
        return this.unwrap().getIntLE(index);
    }
    
    @Override
    public long getLong(final int index) {
        return this.unwrap().getLong(index);
    }
    
    @Override
    protected long _getLong(final int index) {
        return this.unwrap().getLong(index);
    }
    
    @Override
    public long getLongLE(final int index) {
        return this.unwrap().getLongLE(index);
    }
    
    @Override
    protected long _getLongLE(final int index) {
        return this.unwrap().getLongLE(index);
    }
    
    @Override
    public int nioBufferCount() {
        return this.unwrap().nioBufferCount();
    }
    
    @Override
    public ByteBuffer nioBuffer(final int index, final int length) {
        return this.unwrap().nioBuffer(index, length).asReadOnlyBuffer();
    }
    
    @Override
    public ByteBuffer[] nioBuffers(final int index, final int length) {
        return this.unwrap().nioBuffers(index, length);
    }
    
    @Override
    public int forEachByte(final int index, final int length, final ByteProcessor processor) {
        return this.unwrap().forEachByte(index, length, processor);
    }
    
    @Override
    public int forEachByteDesc(final int index, final int length, final ByteProcessor processor) {
        return this.unwrap().forEachByteDesc(index, length, processor);
    }
    
    @Override
    public int capacity() {
        return this.unwrap().capacity();
    }
    
    @Override
    public ByteBuf capacity(final int newCapacity) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public ByteBuf asReadOnly() {
        return this;
    }
}
