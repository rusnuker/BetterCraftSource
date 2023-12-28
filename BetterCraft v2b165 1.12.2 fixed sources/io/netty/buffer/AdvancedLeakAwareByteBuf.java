// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.ReferenceCounted;
import java.nio.channels.FileChannel;
import io.netty.util.ByteProcessor;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.channels.GatheringByteChannel;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import io.netty.util.ResourceLeakTracker;
import io.netty.util.internal.logging.InternalLogger;

final class AdvancedLeakAwareByteBuf extends SimpleLeakAwareByteBuf
{
    private static final String PROP_ACQUIRE_AND_RELEASE_ONLY = "io.netty.leakDetection.acquireAndReleaseOnly";
    private static final boolean ACQUIRE_AND_RELEASE_ONLY;
    private static final InternalLogger logger;
    
    AdvancedLeakAwareByteBuf(final ByteBuf buf, final ResourceLeakTracker<ByteBuf> leak) {
        super(buf, leak);
    }
    
    AdvancedLeakAwareByteBuf(final ByteBuf wrapped, final ByteBuf trackedByteBuf, final ResourceLeakTracker<ByteBuf> leak) {
        super(wrapped, trackedByteBuf, leak);
    }
    
    static void recordLeakNonRefCountingOperation(final ResourceLeakTracker<ByteBuf> leak) {
        if (!AdvancedLeakAwareByteBuf.ACQUIRE_AND_RELEASE_ONLY) {
            leak.record();
        }
    }
    
    @Override
    public ByteBuf order(final ByteOrder endianness) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.order(endianness);
    }
    
    @Override
    public ByteBuf slice() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.slice();
    }
    
    @Override
    public ByteBuf slice(final int index, final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.slice(index, length);
    }
    
    @Override
    public ByteBuf retainedSlice() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.retainedSlice();
    }
    
    @Override
    public ByteBuf retainedSlice(final int index, final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.retainedSlice(index, length);
    }
    
    @Override
    public ByteBuf retainedDuplicate() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.retainedDuplicate();
    }
    
    @Override
    public ByteBuf readRetainedSlice(final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readRetainedSlice(length);
    }
    
    @Override
    public ByteBuf duplicate() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.duplicate();
    }
    
    @Override
    public ByteBuf readSlice(final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readSlice(length);
    }
    
    @Override
    public ByteBuf discardReadBytes() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.discardReadBytes();
    }
    
    @Override
    public ByteBuf discardSomeReadBytes() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.discardSomeReadBytes();
    }
    
    @Override
    public ByteBuf ensureWritable(final int minWritableBytes) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.ensureWritable(minWritableBytes);
    }
    
    @Override
    public int ensureWritable(final int minWritableBytes, final boolean force) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.ensureWritable(minWritableBytes, force);
    }
    
    @Override
    public boolean getBoolean(final int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getBoolean(index);
    }
    
    @Override
    public byte getByte(final int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getByte(index);
    }
    
    @Override
    public short getUnsignedByte(final int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedByte(index);
    }
    
    @Override
    public short getShort(final int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getShort(index);
    }
    
    @Override
    public int getUnsignedShort(final int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedShort(index);
    }
    
    @Override
    public int getMedium(final int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getMedium(index);
    }
    
    @Override
    public int getUnsignedMedium(final int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedMedium(index);
    }
    
    @Override
    public int getInt(final int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getInt(index);
    }
    
    @Override
    public long getUnsignedInt(final int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedInt(index);
    }
    
    @Override
    public long getLong(final int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getLong(index);
    }
    
    @Override
    public char getChar(final int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getChar(index);
    }
    
    @Override
    public float getFloat(final int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getFloat(index);
    }
    
    @Override
    public double getDouble(final int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getDouble(index);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, dst);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, dst, length);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, dst, dstIndex, length);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, dst);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, dst, dstIndex, length);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuffer dst) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, dst);
    }
    
    @Override
    public ByteBuf getBytes(final int index, final OutputStream out, final int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, out, length);
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, out, length);
    }
    
    @Override
    public CharSequence getCharSequence(final int index, final int length, final Charset charset) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getCharSequence(index, length, charset);
    }
    
    @Override
    public ByteBuf setBoolean(final int index, final boolean value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setBoolean(index, value);
    }
    
    @Override
    public ByteBuf setByte(final int index, final int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setByte(index, value);
    }
    
    @Override
    public ByteBuf setShort(final int index, final int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setShort(index, value);
    }
    
    @Override
    public ByteBuf setMedium(final int index, final int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setMedium(index, value);
    }
    
    @Override
    public ByteBuf setInt(final int index, final int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setInt(index, value);
    }
    
    @Override
    public ByteBuf setLong(final int index, final long value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setLong(index, value);
    }
    
    @Override
    public ByteBuf setChar(final int index, final int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setChar(index, value);
    }
    
    @Override
    public ByteBuf setFloat(final int index, final float value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setFloat(index, value);
    }
    
    @Override
    public ByteBuf setDouble(final int index, final double value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setDouble(index, value);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, src);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, src, length);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, src, srcIndex, length);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, src);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, src, srcIndex, length);
    }
    
    @Override
    public ByteBuf setBytes(final int index, final ByteBuffer src) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, src);
    }
    
    @Override
    public int setBytes(final int index, final InputStream in, final int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, in, length);
    }
    
    @Override
    public int setBytes(final int index, final ScatteringByteChannel in, final int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, in, length);
    }
    
    @Override
    public ByteBuf setZero(final int index, final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setZero(index, length);
    }
    
    @Override
    public int setCharSequence(final int index, final CharSequence sequence, final Charset charset) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setCharSequence(index, sequence, charset);
    }
    
    @Override
    public boolean readBoolean() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBoolean();
    }
    
    @Override
    public byte readByte() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readByte();
    }
    
    @Override
    public short readUnsignedByte() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedByte();
    }
    
    @Override
    public short readShort() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readShort();
    }
    
    @Override
    public int readUnsignedShort() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedShort();
    }
    
    @Override
    public int readMedium() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readMedium();
    }
    
    @Override
    public int readUnsignedMedium() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedMedium();
    }
    
    @Override
    public int readInt() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readInt();
    }
    
    @Override
    public long readUnsignedInt() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedInt();
    }
    
    @Override
    public long readLong() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readLong();
    }
    
    @Override
    public char readChar() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readChar();
    }
    
    @Override
    public float readFloat() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readFloat();
    }
    
    @Override
    public double readDouble() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readDouble();
    }
    
    @Override
    public ByteBuf readBytes(final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(length);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(dst);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst, final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(dst, length);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuf dst, final int dstIndex, final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(dst, dstIndex, length);
    }
    
    @Override
    public ByteBuf readBytes(final byte[] dst) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(dst);
    }
    
    @Override
    public ByteBuf readBytes(final byte[] dst, final int dstIndex, final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(dst, dstIndex, length);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuffer dst) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(dst);
    }
    
    @Override
    public ByteBuf readBytes(final OutputStream out, final int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(out, length);
    }
    
    @Override
    public int readBytes(final GatheringByteChannel out, final int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(out, length);
    }
    
    @Override
    public CharSequence readCharSequence(final int length, final Charset charset) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readCharSequence(length, charset);
    }
    
    @Override
    public ByteBuf skipBytes(final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.skipBytes(length);
    }
    
    @Override
    public ByteBuf writeBoolean(final boolean value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeBoolean(value);
    }
    
    @Override
    public ByteBuf writeByte(final int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeByte(value);
    }
    
    @Override
    public ByteBuf writeShort(final int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeShort(value);
    }
    
    @Override
    public ByteBuf writeMedium(final int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeMedium(value);
    }
    
    @Override
    public ByteBuf writeInt(final int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeInt(value);
    }
    
    @Override
    public ByteBuf writeLong(final long value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeLong(value);
    }
    
    @Override
    public ByteBuf writeChar(final int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeChar(value);
    }
    
    @Override
    public ByteBuf writeFloat(final float value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeFloat(value);
    }
    
    @Override
    public ByteBuf writeDouble(final double value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeDouble(value);
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(src);
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src, final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(src, length);
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuf src, final int srcIndex, final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(src, srcIndex, length);
    }
    
    @Override
    public ByteBuf writeBytes(final byte[] src) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(src);
    }
    
    @Override
    public ByteBuf writeBytes(final byte[] src, final int srcIndex, final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(src, srcIndex, length);
    }
    
    @Override
    public ByteBuf writeBytes(final ByteBuffer src) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(src);
    }
    
    @Override
    public int writeBytes(final InputStream in, final int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(in, length);
    }
    
    @Override
    public int writeBytes(final ScatteringByteChannel in, final int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(in, length);
    }
    
    @Override
    public ByteBuf writeZero(final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeZero(length);
    }
    
    @Override
    public int indexOf(final int fromIndex, final int toIndex, final byte value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.indexOf(fromIndex, toIndex, value);
    }
    
    @Override
    public int bytesBefore(final byte value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.bytesBefore(value);
    }
    
    @Override
    public int bytesBefore(final int length, final byte value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.bytesBefore(length, value);
    }
    
    @Override
    public int bytesBefore(final int index, final int length, final byte value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.bytesBefore(index, length, value);
    }
    
    @Override
    public int forEachByte(final ByteProcessor processor) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.forEachByte(processor);
    }
    
    @Override
    public int forEachByte(final int index, final int length, final ByteProcessor processor) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.forEachByte(index, length, processor);
    }
    
    @Override
    public int forEachByteDesc(final ByteProcessor processor) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.forEachByteDesc(processor);
    }
    
    @Override
    public int forEachByteDesc(final int index, final int length, final ByteProcessor processor) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.forEachByteDesc(index, length, processor);
    }
    
    @Override
    public ByteBuf copy() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.copy();
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.copy(index, length);
    }
    
    @Override
    public int nioBufferCount() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.nioBufferCount();
    }
    
    @Override
    public ByteBuffer nioBuffer() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.nioBuffer();
    }
    
    @Override
    public ByteBuffer nioBuffer(final int index, final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.nioBuffer(index, length);
    }
    
    @Override
    public ByteBuffer[] nioBuffers() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.nioBuffers();
    }
    
    @Override
    public ByteBuffer[] nioBuffers(final int index, final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.nioBuffers(index, length);
    }
    
    @Override
    public ByteBuffer internalNioBuffer(final int index, final int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.internalNioBuffer(index, length);
    }
    
    @Override
    public String toString(final Charset charset) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.toString(charset);
    }
    
    @Override
    public String toString(final int index, final int length, final Charset charset) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.toString(index, length, charset);
    }
    
    @Override
    public ByteBuf capacity(final int newCapacity) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.capacity(newCapacity);
    }
    
    @Override
    public short getShortLE(final int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getShortLE(index);
    }
    
    @Override
    public int getUnsignedShortLE(final int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedShortLE(index);
    }
    
    @Override
    public int getMediumLE(final int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getMediumLE(index);
    }
    
    @Override
    public int getUnsignedMediumLE(final int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedMediumLE(index);
    }
    
    @Override
    public int getIntLE(final int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getIntLE(index);
    }
    
    @Override
    public long getUnsignedIntLE(final int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedIntLE(index);
    }
    
    @Override
    public long getLongLE(final int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getLongLE(index);
    }
    
    @Override
    public ByteBuf setShortLE(final int index, final int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setShortLE(index, value);
    }
    
    @Override
    public ByteBuf setIntLE(final int index, final int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setIntLE(index, value);
    }
    
    @Override
    public ByteBuf setMediumLE(final int index, final int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setMediumLE(index, value);
    }
    
    @Override
    public ByteBuf setLongLE(final int index, final long value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setLongLE(index, value);
    }
    
    @Override
    public short readShortLE() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readShortLE();
    }
    
    @Override
    public int readUnsignedShortLE() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedShortLE();
    }
    
    @Override
    public int readMediumLE() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readMediumLE();
    }
    
    @Override
    public int readUnsignedMediumLE() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedMediumLE();
    }
    
    @Override
    public int readIntLE() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readIntLE();
    }
    
    @Override
    public long readUnsignedIntLE() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedIntLE();
    }
    
    @Override
    public long readLongLE() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readLongLE();
    }
    
    @Override
    public ByteBuf writeShortLE(final int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeShortLE(value);
    }
    
    @Override
    public ByteBuf writeMediumLE(final int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeMediumLE(value);
    }
    
    @Override
    public ByteBuf writeIntLE(final int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeIntLE(value);
    }
    
    @Override
    public ByteBuf writeLongLE(final long value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeLongLE(value);
    }
    
    @Override
    public int writeCharSequence(final CharSequence sequence, final Charset charset) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeCharSequence(sequence, charset);
    }
    
    @Override
    public int getBytes(final int index, final FileChannel out, final long position, final int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, out, position, length);
    }
    
    @Override
    public int setBytes(final int index, final FileChannel in, final long position, final int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, in, position, length);
    }
    
    @Override
    public int readBytes(final FileChannel out, final long position, final int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(out, position, length);
    }
    
    @Override
    public int writeBytes(final FileChannel in, final long position, final int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(in, position, length);
    }
    
    @Override
    public ByteBuf asReadOnly() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.asReadOnly();
    }
    
    @Override
    public ByteBuf retain() {
        this.leak.record();
        return super.retain();
    }
    
    @Override
    public ByteBuf retain(final int increment) {
        this.leak.record();
        return super.retain(increment);
    }
    
    @Override
    public ByteBuf touch() {
        this.leak.record();
        return this;
    }
    
    @Override
    public ByteBuf touch(final Object hint) {
        this.leak.record(hint);
        return this;
    }
    
    @Override
    protected AdvancedLeakAwareByteBuf newLeakAwareByteBuf(final ByteBuf buf, final ByteBuf trackedByteBuf, final ResourceLeakTracker<ByteBuf> leakTracker) {
        return new AdvancedLeakAwareByteBuf(buf, trackedByteBuf, leakTracker);
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(AdvancedLeakAwareByteBuf.class);
        ACQUIRE_AND_RELEASE_ONLY = SystemPropertyUtil.getBoolean("io.netty.leakDetection.acquireAndReleaseOnly", false);
        if (AdvancedLeakAwareByteBuf.logger.isDebugEnabled()) {
            AdvancedLeakAwareByteBuf.logger.debug("-D{}: {}", "io.netty.leakDetection.acquireAndReleaseOnly", AdvancedLeakAwareByteBuf.ACQUIRE_AND_RELEASE_ONLY);
        }
    }
}
