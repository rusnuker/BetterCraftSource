// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Iterator;
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

final class AdvancedLeakAwareCompositeByteBuf extends SimpleLeakAwareCompositeByteBuf
{
    AdvancedLeakAwareCompositeByteBuf(final CompositeByteBuf wrapped, final ResourceLeakTracker<ByteBuf> leak) {
        super(wrapped, leak);
    }
    
    @Override
    public ByteBuf order(final ByteOrder endianness) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.order(endianness);
    }
    
    @Override
    public ByteBuf slice() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.slice();
    }
    
    @Override
    public ByteBuf retainedSlice() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.retainedSlice();
    }
    
    @Override
    public ByteBuf slice(final int index, final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.slice(index, length);
    }
    
    @Override
    public ByteBuf retainedSlice(final int index, final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.retainedSlice(index, length);
    }
    
    @Override
    public ByteBuf duplicate() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.duplicate();
    }
    
    @Override
    public ByteBuf retainedDuplicate() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.retainedDuplicate();
    }
    
    @Override
    public ByteBuf readSlice(final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readSlice(length);
    }
    
    @Override
    public ByteBuf readRetainedSlice(final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readRetainedSlice(length);
    }
    
    @Override
    public ByteBuf asReadOnly() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.asReadOnly();
    }
    
    @Override
    public boolean isReadOnly() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.isReadOnly();
    }
    
    @Override
    public CompositeByteBuf discardReadBytes() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.discardReadBytes();
    }
    
    @Override
    public CompositeByteBuf discardSomeReadBytes() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.discardSomeReadBytes();
    }
    
    @Override
    public CompositeByteBuf ensureWritable(final int minWritableBytes) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.ensureWritable(minWritableBytes);
    }
    
    @Override
    public int ensureWritable(final int minWritableBytes, final boolean force) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.ensureWritable(minWritableBytes, force);
    }
    
    @Override
    public boolean getBoolean(final int index) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getBoolean(index);
    }
    
    @Override
    public byte getByte(final int index) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getByte(index);
    }
    
    @Override
    public short getUnsignedByte(final int index) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedByte(index);
    }
    
    @Override
    public short getShort(final int index) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getShort(index);
    }
    
    @Override
    public int getUnsignedShort(final int index) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedShort(index);
    }
    
    @Override
    public int getMedium(final int index) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getMedium(index);
    }
    
    @Override
    public int getUnsignedMedium(final int index) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedMedium(index);
    }
    
    @Override
    public int getInt(final int index) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getInt(index);
    }
    
    @Override
    public long getUnsignedInt(final int index) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedInt(index);
    }
    
    @Override
    public long getLong(final int index) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getLong(index);
    }
    
    @Override
    public char getChar(final int index) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getChar(index);
    }
    
    @Override
    public float getFloat(final int index) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getFloat(index);
    }
    
    @Override
    public double getDouble(final int index) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getDouble(index);
    }
    
    @Override
    public CompositeByteBuf getBytes(final int index, final ByteBuf dst) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, dst);
    }
    
    @Override
    public CompositeByteBuf getBytes(final int index, final ByteBuf dst, final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, dst, length);
    }
    
    @Override
    public CompositeByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, dst, dstIndex, length);
    }
    
    @Override
    public CompositeByteBuf getBytes(final int index, final byte[] dst) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, dst);
    }
    
    @Override
    public CompositeByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, dst, dstIndex, length);
    }
    
    @Override
    public CompositeByteBuf getBytes(final int index, final ByteBuffer dst) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, dst);
    }
    
    @Override
    public CompositeByteBuf getBytes(final int index, final OutputStream out, final int length) throws IOException {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, out, length);
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, out, length);
    }
    
    @Override
    public CharSequence getCharSequence(final int index, final int length, final Charset charset) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getCharSequence(index, length, charset);
    }
    
    @Override
    public CompositeByteBuf setBoolean(final int index, final boolean value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setBoolean(index, value);
    }
    
    @Override
    public CompositeByteBuf setByte(final int index, final int value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setByte(index, value);
    }
    
    @Override
    public CompositeByteBuf setShort(final int index, final int value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setShort(index, value);
    }
    
    @Override
    public CompositeByteBuf setMedium(final int index, final int value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setMedium(index, value);
    }
    
    @Override
    public CompositeByteBuf setInt(final int index, final int value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setInt(index, value);
    }
    
    @Override
    public CompositeByteBuf setLong(final int index, final long value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setLong(index, value);
    }
    
    @Override
    public CompositeByteBuf setChar(final int index, final int value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setChar(index, value);
    }
    
    @Override
    public CompositeByteBuf setFloat(final int index, final float value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setFloat(index, value);
    }
    
    @Override
    public CompositeByteBuf setDouble(final int index, final double value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setDouble(index, value);
    }
    
    @Override
    public CompositeByteBuf setBytes(final int index, final ByteBuf src) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, src);
    }
    
    @Override
    public CompositeByteBuf setBytes(final int index, final ByteBuf src, final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, src, length);
    }
    
    @Override
    public CompositeByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, src, srcIndex, length);
    }
    
    @Override
    public CompositeByteBuf setBytes(final int index, final byte[] src) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, src);
    }
    
    @Override
    public CompositeByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, src, srcIndex, length);
    }
    
    @Override
    public CompositeByteBuf setBytes(final int index, final ByteBuffer src) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, src);
    }
    
    @Override
    public int setBytes(final int index, final InputStream in, final int length) throws IOException {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, in, length);
    }
    
    @Override
    public int setBytes(final int index, final ScatteringByteChannel in, final int length) throws IOException {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, in, length);
    }
    
    @Override
    public CompositeByteBuf setZero(final int index, final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setZero(index, length);
    }
    
    @Override
    public boolean readBoolean() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readBoolean();
    }
    
    @Override
    public byte readByte() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readByte();
    }
    
    @Override
    public short readUnsignedByte() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedByte();
    }
    
    @Override
    public short readShort() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readShort();
    }
    
    @Override
    public int readUnsignedShort() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedShort();
    }
    
    @Override
    public int readMedium() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readMedium();
    }
    
    @Override
    public int readUnsignedMedium() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedMedium();
    }
    
    @Override
    public int readInt() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readInt();
    }
    
    @Override
    public long readUnsignedInt() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedInt();
    }
    
    @Override
    public long readLong() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readLong();
    }
    
    @Override
    public char readChar() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readChar();
    }
    
    @Override
    public float readFloat() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readFloat();
    }
    
    @Override
    public double readDouble() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readDouble();
    }
    
    @Override
    public ByteBuf readBytes(final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(length);
    }
    
    @Override
    public CompositeByteBuf readBytes(final ByteBuf dst) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(dst);
    }
    
    @Override
    public CompositeByteBuf readBytes(final ByteBuf dst, final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(dst, length);
    }
    
    @Override
    public CompositeByteBuf readBytes(final ByteBuf dst, final int dstIndex, final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(dst, dstIndex, length);
    }
    
    @Override
    public CompositeByteBuf readBytes(final byte[] dst) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(dst);
    }
    
    @Override
    public CompositeByteBuf readBytes(final byte[] dst, final int dstIndex, final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(dst, dstIndex, length);
    }
    
    @Override
    public CompositeByteBuf readBytes(final ByteBuffer dst) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(dst);
    }
    
    @Override
    public CompositeByteBuf readBytes(final OutputStream out, final int length) throws IOException {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(out, length);
    }
    
    @Override
    public int readBytes(final GatheringByteChannel out, final int length) throws IOException {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(out, length);
    }
    
    @Override
    public CharSequence readCharSequence(final int length, final Charset charset) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readCharSequence(length, charset);
    }
    
    @Override
    public CompositeByteBuf skipBytes(final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.skipBytes(length);
    }
    
    @Override
    public CompositeByteBuf writeBoolean(final boolean value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeBoolean(value);
    }
    
    @Override
    public CompositeByteBuf writeByte(final int value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeByte(value);
    }
    
    @Override
    public CompositeByteBuf writeShort(final int value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeShort(value);
    }
    
    @Override
    public CompositeByteBuf writeMedium(final int value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeMedium(value);
    }
    
    @Override
    public CompositeByteBuf writeInt(final int value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeInt(value);
    }
    
    @Override
    public CompositeByteBuf writeLong(final long value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeLong(value);
    }
    
    @Override
    public CompositeByteBuf writeChar(final int value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeChar(value);
    }
    
    @Override
    public CompositeByteBuf writeFloat(final float value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeFloat(value);
    }
    
    @Override
    public CompositeByteBuf writeDouble(final double value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeDouble(value);
    }
    
    @Override
    public CompositeByteBuf writeBytes(final ByteBuf src) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(src);
    }
    
    @Override
    public CompositeByteBuf writeBytes(final ByteBuf src, final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(src, length);
    }
    
    @Override
    public CompositeByteBuf writeBytes(final ByteBuf src, final int srcIndex, final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(src, srcIndex, length);
    }
    
    @Override
    public CompositeByteBuf writeBytes(final byte[] src) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(src);
    }
    
    @Override
    public CompositeByteBuf writeBytes(final byte[] src, final int srcIndex, final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(src, srcIndex, length);
    }
    
    @Override
    public CompositeByteBuf writeBytes(final ByteBuffer src) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(src);
    }
    
    @Override
    public int writeBytes(final InputStream in, final int length) throws IOException {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(in, length);
    }
    
    @Override
    public int writeBytes(final ScatteringByteChannel in, final int length) throws IOException {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(in, length);
    }
    
    @Override
    public CompositeByteBuf writeZero(final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeZero(length);
    }
    
    @Override
    public int writeCharSequence(final CharSequence sequence, final Charset charset) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeCharSequence(sequence, charset);
    }
    
    @Override
    public int indexOf(final int fromIndex, final int toIndex, final byte value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.indexOf(fromIndex, toIndex, value);
    }
    
    @Override
    public int bytesBefore(final byte value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.bytesBefore(value);
    }
    
    @Override
    public int bytesBefore(final int length, final byte value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.bytesBefore(length, value);
    }
    
    @Override
    public int bytesBefore(final int index, final int length, final byte value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.bytesBefore(index, length, value);
    }
    
    @Override
    public int forEachByte(final ByteProcessor processor) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.forEachByte(processor);
    }
    
    @Override
    public int forEachByte(final int index, final int length, final ByteProcessor processor) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.forEachByte(index, length, processor);
    }
    
    @Override
    public int forEachByteDesc(final ByteProcessor processor) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.forEachByteDesc(processor);
    }
    
    @Override
    public int forEachByteDesc(final int index, final int length, final ByteProcessor processor) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.forEachByteDesc(index, length, processor);
    }
    
    @Override
    public ByteBuf copy() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.copy();
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.copy(index, length);
    }
    
    @Override
    public int nioBufferCount() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.nioBufferCount();
    }
    
    @Override
    public ByteBuffer nioBuffer() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.nioBuffer();
    }
    
    @Override
    public ByteBuffer nioBuffer(final int index, final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.nioBuffer(index, length);
    }
    
    @Override
    public ByteBuffer[] nioBuffers() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.nioBuffers();
    }
    
    @Override
    public ByteBuffer[] nioBuffers(final int index, final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.nioBuffers(index, length);
    }
    
    @Override
    public ByteBuffer internalNioBuffer(final int index, final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.internalNioBuffer(index, length);
    }
    
    @Override
    public String toString(final Charset charset) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.toString(charset);
    }
    
    @Override
    public String toString(final int index, final int length, final Charset charset) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.toString(index, length, charset);
    }
    
    @Override
    public CompositeByteBuf capacity(final int newCapacity) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.capacity(newCapacity);
    }
    
    @Override
    public short getShortLE(final int index) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getShortLE(index);
    }
    
    @Override
    public int getUnsignedShortLE(final int index) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedShortLE(index);
    }
    
    @Override
    public int getUnsignedMediumLE(final int index) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedMediumLE(index);
    }
    
    @Override
    public int getMediumLE(final int index) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getMediumLE(index);
    }
    
    @Override
    public int getIntLE(final int index) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getIntLE(index);
    }
    
    @Override
    public long getUnsignedIntLE(final int index) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedIntLE(index);
    }
    
    @Override
    public long getLongLE(final int index) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getLongLE(index);
    }
    
    @Override
    public ByteBuf setShortLE(final int index, final int value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setShortLE(index, value);
    }
    
    @Override
    public ByteBuf setMediumLE(final int index, final int value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setMediumLE(index, value);
    }
    
    @Override
    public ByteBuf setIntLE(final int index, final int value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setIntLE(index, value);
    }
    
    @Override
    public ByteBuf setLongLE(final int index, final long value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setLongLE(index, value);
    }
    
    @Override
    public int setCharSequence(final int index, final CharSequence sequence, final Charset charset) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setCharSequence(index, sequence, charset);
    }
    
    @Override
    public short readShortLE() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readShortLE();
    }
    
    @Override
    public int readUnsignedShortLE() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedShortLE();
    }
    
    @Override
    public int readMediumLE() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readMediumLE();
    }
    
    @Override
    public int readUnsignedMediumLE() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedMediumLE();
    }
    
    @Override
    public int readIntLE() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readIntLE();
    }
    
    @Override
    public long readUnsignedIntLE() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedIntLE();
    }
    
    @Override
    public long readLongLE() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readLongLE();
    }
    
    @Override
    public ByteBuf writeShortLE(final int value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeShortLE(value);
    }
    
    @Override
    public ByteBuf writeMediumLE(final int value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeMediumLE(value);
    }
    
    @Override
    public ByteBuf writeIntLE(final int value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeIntLE(value);
    }
    
    @Override
    public ByteBuf writeLongLE(final long value) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeLongLE(value);
    }
    
    @Override
    public CompositeByteBuf addComponent(final ByteBuf buffer) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.addComponent(buffer);
    }
    
    @Override
    public CompositeByteBuf addComponents(final ByteBuf... buffers) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.addComponents(buffers);
    }
    
    @Override
    public CompositeByteBuf addComponents(final Iterable<ByteBuf> buffers) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.addComponents(buffers);
    }
    
    @Override
    public CompositeByteBuf addComponent(final int cIndex, final ByteBuf buffer) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.addComponent(cIndex, buffer);
    }
    
    @Override
    public CompositeByteBuf addComponents(final int cIndex, final ByteBuf... buffers) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.addComponents(cIndex, buffers);
    }
    
    @Override
    public CompositeByteBuf addComponents(final int cIndex, final Iterable<ByteBuf> buffers) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.addComponents(cIndex, buffers);
    }
    
    @Override
    public CompositeByteBuf addComponent(final boolean increaseWriterIndex, final ByteBuf buffer) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.addComponent(increaseWriterIndex, buffer);
    }
    
    @Override
    public CompositeByteBuf addComponents(final boolean increaseWriterIndex, final ByteBuf... buffers) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.addComponents(increaseWriterIndex, buffers);
    }
    
    @Override
    public CompositeByteBuf addComponents(final boolean increaseWriterIndex, final Iterable<ByteBuf> buffers) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.addComponents(increaseWriterIndex, buffers);
    }
    
    @Override
    public CompositeByteBuf addComponent(final boolean increaseWriterIndex, final int cIndex, final ByteBuf buffer) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.addComponent(increaseWriterIndex, cIndex, buffer);
    }
    
    @Override
    public CompositeByteBuf removeComponent(final int cIndex) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.removeComponent(cIndex);
    }
    
    @Override
    public CompositeByteBuf removeComponents(final int cIndex, final int numComponents) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.removeComponents(cIndex, numComponents);
    }
    
    @Override
    public Iterator<ByteBuf> iterator() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.iterator();
    }
    
    @Override
    public List<ByteBuf> decompose(final int offset, final int length) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.decompose(offset, length);
    }
    
    @Override
    public CompositeByteBuf consolidate() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.consolidate();
    }
    
    @Override
    public CompositeByteBuf discardReadComponents() {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.discardReadComponents();
    }
    
    @Override
    public CompositeByteBuf consolidate(final int cIndex, final int numComponents) {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.consolidate(cIndex, numComponents);
    }
    
    @Override
    public int getBytes(final int index, final FileChannel out, final long position, final int length) throws IOException {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, out, position, length);
    }
    
    @Override
    public int setBytes(final int index, final FileChannel in, final long position, final int length) throws IOException {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, in, position, length);
    }
    
    @Override
    public int readBytes(final FileChannel out, final long position, final int length) throws IOException {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(out, position, length);
    }
    
    @Override
    public int writeBytes(final FileChannel in, final long position, final int length) throws IOException {
        AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(in, position, length);
    }
    
    @Override
    public CompositeByteBuf retain() {
        this.leak.record();
        return super.retain();
    }
    
    @Override
    public CompositeByteBuf retain(final int increment) {
        this.leak.record();
        return super.retain(increment);
    }
    
    @Override
    public CompositeByteBuf touch() {
        this.leak.record();
        return this;
    }
    
    @Override
    public CompositeByteBuf touch(final Object hint) {
        this.leak.record(hint);
        return this;
    }
    
    @Override
    protected AdvancedLeakAwareByteBuf newLeakAwareByteBuf(final ByteBuf wrapped, final ByteBuf trackedByteBuf, final ResourceLeakTracker<ByteBuf> leakTracker) {
        return new AdvancedLeakAwareByteBuf(wrapped, trackedByteBuf, leakTracker);
    }
}
