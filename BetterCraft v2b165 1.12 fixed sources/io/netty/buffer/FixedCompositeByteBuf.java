// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import java.util.Collection;
import java.util.Collections;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.EmptyArrays;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.nio.ByteOrder;

final class FixedCompositeByteBuf extends AbstractReferenceCountedByteBuf
{
    private static final ByteBuf[] EMPTY;
    private final int nioBufferCount;
    private final int capacity;
    private final ByteBufAllocator allocator;
    private final ByteOrder order;
    private final Object[] buffers;
    private final boolean direct;
    
    FixedCompositeByteBuf(final ByteBufAllocator allocator, final ByteBuf... buffers) {
        super(Integer.MAX_VALUE);
        if (buffers.length == 0) {
            this.buffers = FixedCompositeByteBuf.EMPTY;
            this.order = ByteOrder.BIG_ENDIAN;
            this.nioBufferCount = 1;
            this.capacity = 0;
            this.direct = false;
        }
        else {
            ByteBuf b = buffers[0];
            (this.buffers = new Object[buffers.length])[0] = b;
            boolean direct = true;
            int nioBufferCount = b.nioBufferCount();
            int capacity = b.readableBytes();
            this.order = b.order();
            for (int i = 1; i < buffers.length; ++i) {
                b = buffers[i];
                if (buffers[i].order() != this.order) {
                    throw new IllegalArgumentException("All ByteBufs need to have same ByteOrder");
                }
                nioBufferCount += b.nioBufferCount();
                capacity += b.readableBytes();
                if (!b.isDirect()) {
                    direct = false;
                }
                this.buffers[i] = b;
            }
            this.nioBufferCount = nioBufferCount;
            this.capacity = capacity;
            this.direct = direct;
        }
        this.setIndex(0, this.capacity());
        this.allocator = allocator;
    }
    
    @Override
    public boolean isWritable() {
        return false;
    }
    
    @Override
    public boolean isWritable(final int size) {
        return false;
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
    public int capacity() {
        return this.capacity;
    }
    
    @Override
    public int maxCapacity() {
        return this.capacity;
    }
    
    @Override
    public ByteBuf capacity(final int newCapacity) {
        throw new ReadOnlyBufferException();
    }
    
    @Override
    public ByteBufAllocator alloc() {
        return this.allocator;
    }
    
    @Override
    public ByteOrder order() {
        return this.order;
    }
    
    @Override
    public ByteBuf unwrap() {
        return null;
    }
    
    @Override
    public boolean isDirect() {
        return this.direct;
    }
    
    private Component findComponent(final int index) {
        int readable = 0;
        for (int i = 0; i < this.buffers.length; ++i) {
            Component comp = null;
            final Object obj = this.buffers[i];
            ByteBuf b;
            boolean isBuffer;
            if (obj instanceof ByteBuf) {
                b = (ByteBuf)obj;
                isBuffer = true;
            }
            else {
                comp = (Component)obj;
                b = comp.buf;
                isBuffer = false;
            }
            readable += b.readableBytes();
            if (index < readable) {
                if (isBuffer) {
                    comp = new Component(i, readable - b.readableBytes(), b);
                    this.buffers[i] = comp;
                }
                return comp;
            }
        }
        throw new IllegalStateException();
    }
    
    private ByteBuf buffer(final int i) {
        final Object obj = this.buffers[i];
        if (obj instanceof ByteBuf) {
            return (ByteBuf)obj;
        }
        return ((Component)obj).buf;
    }
    
    @Override
    public byte getByte(final int index) {
        return this._getByte(index);
    }
    
    @Override
    protected byte _getByte(final int index) {
        final Component c = this.findComponent(index);
        return c.buf.getByte(index - c.offset);
    }
    
    @Override
    protected short _getShort(final int index) {
        final Component c = this.findComponent(index);
        if (index + 2 <= c.endOffset) {
            return c.buf.getShort(index - c.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (short)((this._getByte(index) & 0xFF) << 8 | (this._getByte(index + 1) & 0xFF));
        }
        return (short)((this._getByte(index) & 0xFF) | (this._getByte(index + 1) & 0xFF) << 8);
    }
    
    @Override
    protected short _getShortLE(final int index) {
        final Component c = this.findComponent(index);
        if (index + 2 <= c.endOffset) {
            return c.buf.getShortLE(index - c.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (short)((this._getByte(index) & 0xFF) | (this._getByte(index + 1) & 0xFF) << 8);
        }
        return (short)((this._getByte(index) & 0xFF) << 8 | (this._getByte(index + 1) & 0xFF));
    }
    
    @Override
    protected int _getUnsignedMedium(final int index) {
        final Component c = this.findComponent(index);
        if (index + 3 <= c.endOffset) {
            return c.buf.getUnsignedMedium(index - c.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (this._getShort(index) & 0xFFFF) << 8 | (this._getByte(index + 2) & 0xFF);
        }
        return (this._getShort(index) & 0xFFFF) | (this._getByte(index + 2) & 0xFF) << 16;
    }
    
    @Override
    protected int _getUnsignedMediumLE(final int index) {
        final Component c = this.findComponent(index);
        if (index + 3 <= c.endOffset) {
            return c.buf.getUnsignedMediumLE(index - c.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (this._getShortLE(index) & 0xFFFF) | (this._getByte(index + 2) & 0xFF) << 16;
        }
        return (this._getShortLE(index) & 0xFFFF) << 8 | (this._getByte(index + 2) & 0xFF);
    }
    
    @Override
    protected int _getInt(final int index) {
        final Component c = this.findComponent(index);
        if (index + 4 <= c.endOffset) {
            return c.buf.getInt(index - c.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (this._getShort(index) & 0xFFFF) << 16 | (this._getShort(index + 2) & 0xFFFF);
        }
        return (this._getShort(index) & 0xFFFF) | (this._getShort(index + 2) & 0xFFFF) << 16;
    }
    
    @Override
    protected int _getIntLE(final int index) {
        final Component c = this.findComponent(index);
        if (index + 4 <= c.endOffset) {
            return c.buf.getIntLE(index - c.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (this._getShortLE(index) & 0xFFFF) | (this._getShortLE(index + 2) & 0xFFFF) << 16;
        }
        return (this._getShortLE(index) & 0xFFFF) << 16 | (this._getShortLE(index + 2) & 0xFFFF);
    }
    
    @Override
    protected long _getLong(final int index) {
        final Component c = this.findComponent(index);
        if (index + 8 <= c.endOffset) {
            return c.buf.getLong(index - c.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return ((long)this._getInt(index) & 0xFFFFFFFFL) << 32 | ((long)this._getInt(index + 4) & 0xFFFFFFFFL);
        }
        return ((long)this._getInt(index) & 0xFFFFFFFFL) | ((long)this._getInt(index + 4) & 0xFFFFFFFFL) << 32;
    }
    
    @Override
    protected long _getLongLE(final int index) {
        final Component c = this.findComponent(index);
        if (index + 8 <= c.endOffset) {
            return c.buf.getLongLE(index - c.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return ((long)this._getIntLE(index) & 0xFFFFFFFFL) | ((long)this._getIntLE(index + 4) & 0xFFFFFFFFL) << 32;
        }
        return ((long)this._getIntLE(index) & 0xFFFFFFFFL) << 32 | ((long)this._getIntLE(index + 4) & 0xFFFFFFFFL);
    }
    
    @Override
    public ByteBuf getBytes(int index, final byte[] dst, int dstIndex, int length) {
        this.checkDstIndex(index, length, dstIndex, dst.length);
        if (length == 0) {
            return this;
        }
        final Component c = this.findComponent(index);
        int i = c.index;
        int adjustment = c.offset;
        ByteBuf s = c.buf;
        while (true) {
            final int localLength = Math.min(length, s.readableBytes() - (index - adjustment));
            s.getBytes(index - adjustment, dst, dstIndex, localLength);
            index += localLength;
            dstIndex += localLength;
            length -= localLength;
            adjustment += s.readableBytes();
            if (length <= 0) {
                break;
            }
            s = this.buffer(++i);
        }
        return this;
    }
    
    @Override
    public ByteBuf getBytes(int index, final ByteBuffer dst) {
        final int limit = dst.limit();
        int length = dst.remaining();
        this.checkIndex(index, length);
        if (length == 0) {
            return this;
        }
        try {
            final Component c = this.findComponent(index);
            int i = c.index;
            int adjustment = c.offset;
            ByteBuf s = c.buf;
            while (true) {
                final int localLength = Math.min(length, s.readableBytes() - (index - adjustment));
                dst.limit(dst.position() + localLength);
                s.getBytes(index - adjustment, dst);
                index += localLength;
                length -= localLength;
                adjustment += s.readableBytes();
                if (length <= 0) {
                    break;
                }
                s = this.buffer(++i);
            }
        }
        finally {
            dst.limit(limit);
        }
        return this;
    }
    
    @Override
    public ByteBuf getBytes(int index, final ByteBuf dst, int dstIndex, int length) {
        this.checkDstIndex(index, length, dstIndex, dst.capacity());
        if (length == 0) {
            return this;
        }
        final Component c = this.findComponent(index);
        int i = c.index;
        int adjustment = c.offset;
        ByteBuf s = c.buf;
        while (true) {
            final int localLength = Math.min(length, s.readableBytes() - (index - adjustment));
            s.getBytes(index - adjustment, dst, dstIndex, localLength);
            index += localLength;
            dstIndex += localLength;
            length -= localLength;
            adjustment += s.readableBytes();
            if (length <= 0) {
                break;
            }
            s = this.buffer(++i);
        }
        return this;
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        final int count = this.nioBufferCount();
        if (count == 1) {
            return out.write(this.internalNioBuffer(index, length));
        }
        final long writtenBytes = out.write(this.nioBuffers(index, length));
        if (writtenBytes > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int)writtenBytes;
    }
    
    @Override
    public int getBytes(final int index, final FileChannel out, final long position, final int length) throws IOException {
        final int count = this.nioBufferCount();
        if (count == 1) {
            return out.write(this.internalNioBuffer(index, length), position);
        }
        long writtenBytes = 0L;
        for (final ByteBuffer buf : this.nioBuffers(index, length)) {
            writtenBytes += out.write(buf, position + writtenBytes);
        }
        if (writtenBytes > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int)writtenBytes;
    }
    
    @Override
    public ByteBuf getBytes(int index, final OutputStream out, int length) throws IOException {
        this.checkIndex(index, length);
        if (length == 0) {
            return this;
        }
        final Component c = this.findComponent(index);
        int i = c.index;
        int adjustment = c.offset;
        ByteBuf s = c.buf;
        while (true) {
            final int localLength = Math.min(length, s.readableBytes() - (index - adjustment));
            s.getBytes(index - adjustment, out, localLength);
            index += localLength;
            length -= localLength;
            adjustment += s.readableBytes();
            if (length <= 0) {
                break;
            }
            s = this.buffer(++i);
        }
        return this;
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        this.checkIndex(index, length);
        boolean release = true;
        final ByteBuf buf = this.alloc().buffer(length);
        try {
            buf.writeBytes(this, index, length);
            release = false;
            return buf;
        }
        finally {
            if (release) {
                buf.release();
            }
        }
    }
    
    @Override
    public int nioBufferCount() {
        return this.nioBufferCount;
    }
    
    @Override
    public ByteBuffer nioBuffer(final int index, final int length) {
        this.checkIndex(index, length);
        if (this.buffers.length == 1) {
            final ByteBuf buf = this.buffer(0);
            if (buf.nioBufferCount() == 1) {
                return buf.nioBuffer(index, length);
            }
        }
        final ByteBuffer merged = ByteBuffer.allocate(length).order(this.order());
        final ByteBuffer[] buffers = this.nioBuffers(index, length);
        for (int i = 0; i < buffers.length; ++i) {
            merged.put(buffers[i]);
        }
        merged.flip();
        return merged;
    }
    
    @Override
    public ByteBuffer internalNioBuffer(final int index, final int length) {
        if (this.buffers.length == 1) {
            return this.buffer(0).internalNioBuffer(index, length);
        }
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ByteBuffer[] nioBuffers(int index, int length) {
        this.checkIndex(index, length);
        if (length == 0) {
            return EmptyArrays.EMPTY_BYTE_BUFFERS;
        }
        final RecyclableArrayList array = RecyclableArrayList.newInstance(this.buffers.length);
        try {
            final Component c = this.findComponent(index);
            int i = c.index;
            int adjustment = c.offset;
            ByteBuf s = c.buf;
            while (true) {
                final int localLength = Math.min(length, s.readableBytes() - (index - adjustment));
                switch (s.nioBufferCount()) {
                    case 0: {
                        throw new UnsupportedOperationException();
                    }
                    case 1: {
                        array.add(s.nioBuffer(index - adjustment, localLength));
                        break;
                    }
                    default: {
                        Collections.addAll(array, s.nioBuffers(index - adjustment, localLength));
                        break;
                    }
                }
                index += localLength;
                length -= localLength;
                adjustment += s.readableBytes();
                if (length <= 0) {
                    return array.toArray(new ByteBuffer[array.size()]);
                }
                s = this.buffer(++i);
            }
        }
        finally {
            array.recycle();
        }
    }
    
    @Override
    public boolean hasArray() {
        return false;
    }
    
    @Override
    public byte[] array() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int arrayOffset() {
        throw new UnsupportedOperationException();
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
    protected void deallocate() {
        for (int i = 0; i < this.buffers.length; ++i) {
            this.buffer(i).release();
        }
    }
    
    @Override
    public String toString() {
        String result = super.toString();
        result = result.substring(0, result.length() - 1);
        return result + ", components=" + this.buffers.length + ')';
    }
    
    static {
        EMPTY = new ByteBuf[] { Unpooled.EMPTY_BUFFER };
    }
    
    private static final class Component
    {
        private final int index;
        private final int offset;
        private final ByteBuf buf;
        private final int endOffset;
        
        Component(final int index, final int offset, final ByteBuf buf) {
            this.index = index;
            this.offset = offset;
            this.endOffset = offset + buf.readableBytes();
            this.buf = buf;
        }
    }
}
