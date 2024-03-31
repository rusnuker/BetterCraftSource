// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.oio;

import io.netty.util.internal.StringUtil;
import io.netty.channel.FileRegion;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import java.io.IOException;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelFuture;
import io.netty.channel.Channel;
import io.netty.channel.ChannelMetadata;

public abstract class AbstractOioByteChannel extends AbstractOioChannel
{
    private static final ChannelMetadata METADATA;
    private static final String EXPECTED_TYPES;
    
    protected AbstractOioByteChannel(final Channel parent) {
        super(parent);
    }
    
    @Override
    public ChannelMetadata metadata() {
        return AbstractOioByteChannel.METADATA;
    }
    
    protected abstract boolean isInputShutdown();
    
    protected abstract ChannelFuture shutdownInput();
    
    private void closeOnRead(final ChannelPipeline pipeline) {
        if (this.isOpen()) {
            if (Boolean.TRUE.equals(this.config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
                this.shutdownInput();
                pipeline.fireUserEventTriggered((Object)ChannelInputShutdownEvent.INSTANCE);
            }
            else {
                this.unsafe().close(this.unsafe().voidPromise());
            }
        }
    }
    
    private void handleReadException(final ChannelPipeline pipeline, final ByteBuf byteBuf, final Throwable cause, final boolean close, final RecvByteBufAllocator.Handle allocHandle) {
        if (byteBuf != null) {
            if (byteBuf.isReadable()) {
                this.readPending = false;
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
    protected void doRead() {
        final ChannelConfig config = this.config();
        if (this.isInputShutdown() || !this.readPending) {
            return;
        }
        this.readPending = false;
        final ChannelPipeline pipeline = this.pipeline();
        final ByteBufAllocator allocator = config.getAllocator();
        final RecvByteBufAllocator.Handle allocHandle = this.unsafe().recvBufAllocHandle();
        allocHandle.reset(config);
        ByteBuf byteBuf = null;
        boolean close = false;
        boolean readData = false;
        try {
            byteBuf = allocHandle.allocate(allocator);
            do {
                allocHandle.lastBytesRead(this.doReadBytes(byteBuf));
                if (allocHandle.lastBytesRead() <= 0) {
                    if (!byteBuf.isReadable()) {
                        byteBuf.release();
                        byteBuf = null;
                        close = (allocHandle.lastBytesRead() < 0);
                        break;
                    }
                    break;
                }
                else {
                    readData = true;
                    final int available = this.available();
                    if (available <= 0) {
                        break;
                    }
                    if (byteBuf.isWritable()) {
                        continue;
                    }
                    final int capacity = byteBuf.capacity();
                    final int maxCapacity = byteBuf.maxCapacity();
                    if (capacity == maxCapacity) {
                        allocHandle.incMessagesRead(1);
                        this.readPending = false;
                        pipeline.fireChannelRead((Object)byteBuf);
                        byteBuf = allocHandle.allocate(allocator);
                    }
                    else {
                        final int writerIndex = byteBuf.writerIndex();
                        if (writerIndex + available > maxCapacity) {
                            byteBuf.capacity(maxCapacity);
                        }
                        else {
                            byteBuf.ensureWritable(available);
                        }
                    }
                }
            } while (allocHandle.continueReading());
            if (byteBuf != null) {
                if (byteBuf.isReadable()) {
                    this.readPending = false;
                    pipeline.fireChannelRead((Object)byteBuf);
                }
                else {
                    byteBuf.release();
                }
                byteBuf = null;
            }
            if (readData) {
                allocHandle.readComplete();
                pipeline.fireChannelReadComplete();
            }
            if (close) {
                this.closeOnRead(pipeline);
            }
        }
        catch (final Throwable t) {
            this.handleReadException(pipeline, byteBuf, t, close, allocHandle);
        }
        finally {
            if (this.readPending || config.isAutoRead() || (!readData && this.isActive())) {
                this.read();
            }
        }
    }
    
    @Override
    protected void doWrite(final ChannelOutboundBuffer in) throws Exception {
        while (true) {
            final Object msg = in.current();
            if (msg == null) {
                break;
            }
            if (msg instanceof ByteBuf) {
                final ByteBuf buf = (ByteBuf)msg;
                int newReadableBytes;
                for (int readableBytes = buf.readableBytes(); readableBytes > 0; readableBytes = newReadableBytes) {
                    this.doWriteBytes(buf);
                    newReadableBytes = buf.readableBytes();
                    in.progress(readableBytes - newReadableBytes);
                }
                in.remove();
            }
            else if (msg instanceof FileRegion) {
                final FileRegion region = (FileRegion)msg;
                final long transferred = region.transferred();
                this.doWriteFileRegion(region);
                in.progress(region.transferred() - transferred);
                in.remove();
            }
            else {
                in.remove(new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg)));
            }
        }
    }
    
    @Override
    protected final Object filterOutboundMessage(final Object msg) throws Exception {
        if (msg instanceof ByteBuf || msg instanceof FileRegion) {
            return msg;
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + AbstractOioByteChannel.EXPECTED_TYPES);
    }
    
    protected abstract int available();
    
    protected abstract int doReadBytes(final ByteBuf p0) throws Exception;
    
    protected abstract void doWriteBytes(final ByteBuf p0) throws Exception;
    
    protected abstract void doWriteFileRegion(final FileRegion p0) throws Exception;
    
    static {
        METADATA = new ChannelMetadata(false);
        EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(FileRegion.class) + ')';
    }
}
