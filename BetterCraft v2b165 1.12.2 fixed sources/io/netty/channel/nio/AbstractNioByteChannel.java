// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.nio;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import java.io.IOException;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.ChannelInputShutdownReadComplete;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.AbstractChannel;
import java.nio.channels.SelectionKey;
import io.netty.util.internal.StringUtil;
import io.netty.channel.FileRegion;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelFuture;
import java.nio.channels.SelectableChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelMetadata;

public abstract class AbstractNioByteChannel extends AbstractNioChannel
{
    private static final ChannelMetadata METADATA;
    private static final String EXPECTED_TYPES;
    private Runnable flushTask;
    
    protected AbstractNioByteChannel(final Channel parent, final SelectableChannel ch) {
        super(parent, ch, 1);
    }
    
    protected abstract ChannelFuture shutdownInput();
    
    protected boolean isInputShutdown0() {
        return false;
    }
    
    @Override
    protected AbstractNioUnsafe newUnsafe() {
        return new NioByteUnsafe();
    }
    
    @Override
    public ChannelMetadata metadata() {
        return AbstractNioByteChannel.METADATA;
    }
    
    @Override
    protected void doWrite(final ChannelOutboundBuffer in) throws Exception {
        int writeSpinCount = -1;
        boolean setOpWrite = false;
        while (true) {
            final Object msg = in.current();
            if (msg == null) {
                this.clearOpWrite();
                return;
            }
            if (msg instanceof ByteBuf) {
                final ByteBuf buf = (ByteBuf)msg;
                final int readableBytes = buf.readableBytes();
                if (readableBytes == 0) {
                    in.remove();
                }
                else {
                    boolean done = false;
                    long flushedAmount = 0L;
                    if (writeSpinCount == -1) {
                        writeSpinCount = this.config().getWriteSpinCount();
                    }
                    for (int i = writeSpinCount - 1; i >= 0; --i) {
                        final int localFlushedAmount = this.doWriteBytes(buf);
                        if (localFlushedAmount == 0) {
                            setOpWrite = true;
                            break;
                        }
                        flushedAmount += localFlushedAmount;
                        if (!buf.isReadable()) {
                            done = true;
                            break;
                        }
                    }
                    in.progress(flushedAmount);
                    if (!done) {
                        break;
                    }
                    in.remove();
                }
            }
            else {
                if (!(msg instanceof FileRegion)) {
                    throw new Error();
                }
                final FileRegion region = (FileRegion)msg;
                boolean done2 = region.transferred() >= region.count();
                if (!done2) {
                    long flushedAmount2 = 0L;
                    if (writeSpinCount == -1) {
                        writeSpinCount = this.config().getWriteSpinCount();
                    }
                    for (int j = writeSpinCount - 1; j >= 0; --j) {
                        final long localFlushedAmount2 = this.doWriteFileRegion(region);
                        if (localFlushedAmount2 == 0L) {
                            setOpWrite = true;
                            break;
                        }
                        flushedAmount2 += localFlushedAmount2;
                        if (region.transferred() >= region.count()) {
                            done2 = true;
                            break;
                        }
                    }
                    in.progress(flushedAmount2);
                }
                if (!done2) {
                    break;
                }
                in.remove();
            }
        }
        this.incompleteWrite(setOpWrite);
    }
    
    @Override
    protected final Object filterOutboundMessage(final Object msg) {
        if (msg instanceof ByteBuf) {
            final ByteBuf buf = (ByteBuf)msg;
            if (buf.isDirect()) {
                return msg;
            }
            return this.newDirectBuffer(buf);
        }
        else {
            if (msg instanceof FileRegion) {
                return msg;
            }
            throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + AbstractNioByteChannel.EXPECTED_TYPES);
        }
    }
    
    protected final void incompleteWrite(final boolean setOpWrite) {
        if (setOpWrite) {
            this.setOpWrite();
        }
        else {
            Runnable flushTask = this.flushTask;
            if (flushTask == null) {
                final Runnable flushTask2 = new Runnable() {
                    @Override
                    public void run() {
                        AbstractNioByteChannel.this.flush();
                    }
                };
                this.flushTask = flushTask2;
                flushTask = flushTask2;
            }
            this.eventLoop().execute(flushTask);
        }
    }
    
    protected abstract long doWriteFileRegion(final FileRegion p0) throws Exception;
    
    protected abstract int doReadBytes(final ByteBuf p0) throws Exception;
    
    protected abstract int doWriteBytes(final ByteBuf p0) throws Exception;
    
    protected final void setOpWrite() {
        final SelectionKey key = this.selectionKey();
        if (!key.isValid()) {
            return;
        }
        final int interestOps = key.interestOps();
        if ((interestOps & 0x4) == 0x0) {
            key.interestOps(interestOps | 0x4);
        }
    }
    
    protected final void clearOpWrite() {
        final SelectionKey key = this.selectionKey();
        if (!key.isValid()) {
            return;
        }
        final int interestOps = key.interestOps();
        if ((interestOps & 0x4) != 0x0) {
            key.interestOps(interestOps & 0xFFFFFFFB);
        }
    }
    
    static {
        METADATA = new ChannelMetadata(false, 16);
        EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(FileRegion.class) + ')';
    }
    
    protected class NioByteUnsafe extends AbstractNioUnsafe
    {
        private void closeOnRead(final ChannelPipeline pipeline) {
            if (!AbstractNioByteChannel.this.isInputShutdown0()) {
                if (Boolean.TRUE.equals(AbstractNioByteChannel.this.config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
                    AbstractNioByteChannel.this.shutdownInput();
                    pipeline.fireUserEventTriggered((Object)ChannelInputShutdownEvent.INSTANCE);
                }
                else {
                    this.close(this.voidPromise());
                }
            }
            else {
                pipeline.fireUserEventTriggered((Object)ChannelInputShutdownReadComplete.INSTANCE);
            }
        }
        
        private void handleReadException(final ChannelPipeline pipeline, final ByteBuf byteBuf, final Throwable cause, final boolean close, final RecvByteBufAllocator.Handle allocHandle) {
            if (byteBuf != null) {
                if (byteBuf.isReadable()) {
                    AbstractNioByteChannel.this.readPending = false;
                    pipeline.fireChannelRead((Object)byteBuf);
                }
                else {
                    byteBuf.release();
                }
            }
            allocHandle.readComplete();
            pipeline.fireChannelReadComplete();
            pipeline.fireExceptionCaught(cause);
            if (close || cause instanceof IOException) {
                this.closeOnRead(pipeline);
            }
        }
        
        @Override
        public final void read() {
            final ChannelConfig config = AbstractNioByteChannel.this.config();
            final ChannelPipeline pipeline = AbstractNioByteChannel.this.pipeline();
            final ByteBufAllocator allocator = config.getAllocator();
            final RecvByteBufAllocator.Handle allocHandle = this.recvBufAllocHandle();
            allocHandle.reset(config);
            ByteBuf byteBuf = null;
            boolean close = false;
            try {
                do {
                    byteBuf = allocHandle.allocate(allocator);
                    allocHandle.lastBytesRead(AbstractNioByteChannel.this.doReadBytes(byteBuf));
                    if (allocHandle.lastBytesRead() <= 0) {
                        byteBuf.release();
                        byteBuf = null;
                        close = (allocHandle.lastBytesRead() < 0);
                        break;
                    }
                    allocHandle.incMessagesRead(1);
                    AbstractNioByteChannel.this.readPending = false;
                    pipeline.fireChannelRead((Object)byteBuf);
                    byteBuf = null;
                } while (allocHandle.continueReading());
                allocHandle.readComplete();
                pipeline.fireChannelReadComplete();
                if (close) {
                    this.closeOnRead(pipeline);
                }
            }
            catch (final Throwable t) {
                this.handleReadException(pipeline, byteBuf, t, close, allocHandle);
            }
            finally {
                if (!AbstractNioByteChannel.this.readPending && !config.isAutoRead()) {
                    this.removeReadOp();
                }
            }
        }
    }
}
