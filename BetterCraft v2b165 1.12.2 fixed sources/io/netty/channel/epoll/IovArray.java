// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.epoll;

import java.nio.ByteBuffer;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.PlatformDependent;
import io.netty.channel.ChannelOutboundBuffer;

final class IovArray implements ChannelOutboundBuffer.MessageProcessor
{
    private static final int ADDRESS_SIZE;
    private static final int IOV_SIZE;
    private static final int CAPACITY;
    private final long memoryAddress;
    private int count;
    private long size;
    
    IovArray() {
        this.memoryAddress = PlatformDependent.allocateMemory(IovArray.CAPACITY);
    }
    
    void clear() {
        this.count = 0;
        this.size = 0L;
    }
    
    boolean add(final ByteBuf buf) {
        if (this.count == Native.IOV_MAX) {
            return false;
        }
        final int len = buf.readableBytes();
        if (len == 0) {
            return true;
        }
        final long addr = buf.memoryAddress();
        final int offset = buf.readerIndex();
        return this.add(addr, offset, len);
    }
    
    private boolean add(final long addr, final int offset, final int len) {
        if (len == 0) {
            return true;
        }
        final long baseOffset = this.memoryAddress(this.count++);
        final long lengthOffset = baseOffset + IovArray.ADDRESS_SIZE;
        if (Native.SSIZE_MAX - len < this.size) {
            return false;
        }
        this.size += len;
        if (IovArray.ADDRESS_SIZE == 8) {
            PlatformDependent.putLong(baseOffset, addr + offset);
            PlatformDependent.putLong(lengthOffset, len);
        }
        else {
            assert IovArray.ADDRESS_SIZE == 4;
            PlatformDependent.putInt(baseOffset, (int)addr + offset);
            PlatformDependent.putInt(lengthOffset, len);
        }
        return true;
    }
    
    boolean add(final CompositeByteBuf buf) {
        final ByteBuffer[] buffers = buf.nioBuffers();
        if (this.count + buffers.length >= Native.IOV_MAX) {
            return false;
        }
        for (final ByteBuffer nioBuffer : buffers) {
            final int offset = nioBuffer.position();
            final int len = nioBuffer.limit() - nioBuffer.position();
            if (len != 0) {
                final long addr = PlatformDependent.directBufferAddress(nioBuffer);
                if (!this.add(addr, offset, len)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    long processWritten(final int index, final long written) {
        final long baseOffset = this.memoryAddress(index);
        final long lengthOffset = baseOffset + IovArray.ADDRESS_SIZE;
        if (IovArray.ADDRESS_SIZE == 8) {
            final long len = PlatformDependent.getLong(lengthOffset);
            if (len > written) {
                final long offset = PlatformDependent.getLong(baseOffset);
                PlatformDependent.putLong(baseOffset, offset + written);
                PlatformDependent.putLong(lengthOffset, len - written);
                return -1L;
            }
            return len;
        }
        else {
            assert IovArray.ADDRESS_SIZE == 4;
            final long len = PlatformDependent.getInt(lengthOffset);
            if (len > written) {
                final int offset2 = PlatformDependent.getInt(baseOffset);
                PlatformDependent.putInt(baseOffset, (int)(offset2 + written));
                PlatformDependent.putInt(lengthOffset, (int)(len - written));
                return -1L;
            }
            return len;
        }
    }
    
    int count() {
        return this.count;
    }
    
    long size() {
        return this.size;
    }
    
    long memoryAddress(final int offset) {
        return this.memoryAddress + IovArray.IOV_SIZE * offset;
    }
    
    void release() {
        PlatformDependent.freeMemory(this.memoryAddress);
    }
    
    @Override
    public boolean processMessage(final Object msg) throws Exception {
        if (!(msg instanceof ByteBuf)) {
            return false;
        }
        if (msg instanceof CompositeByteBuf) {
            return this.add((CompositeByteBuf)msg);
        }
        return this.add((ByteBuf)msg);
    }
    
    static {
        ADDRESS_SIZE = PlatformDependent.addressSize();
        IOV_SIZE = 2 * IovArray.ADDRESS_SIZE;
        CAPACITY = Native.IOV_MAX * IovArray.IOV_SIZE;
    }
}
