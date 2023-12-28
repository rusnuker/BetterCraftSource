// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.oio;

import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelConfig;
import java.io.IOException;
import java.util.ArrayList;
import io.netty.channel.Channel;
import java.util.List;

public abstract class AbstractOioMessageChannel extends AbstractOioChannel
{
    private final List<Object> readBuf;
    
    protected AbstractOioMessageChannel(final Channel parent) {
        super(parent);
        this.readBuf = new ArrayList<Object>();
    }
    
    @Override
    protected void doRead() {
        if (!this.readPending) {
            return;
        }
        this.readPending = false;
        final ChannelConfig config = this.config();
        final ChannelPipeline pipeline = this.pipeline();
        final RecvByteBufAllocator.Handle allocHandle = this.unsafe().recvBufAllocHandle();
        allocHandle.reset(config);
        boolean closed = false;
        Throwable exception = null;
        try {
            do {
                final int localRead = this.doReadMessages(this.readBuf);
                if (localRead == 0) {
                    break;
                }
                if (localRead < 0) {
                    closed = true;
                    break;
                }
                allocHandle.incMessagesRead(localRead);
            } while (allocHandle.continueReading());
        }
        catch (final Throwable t) {
            exception = t;
        }
        boolean readData = false;
        final int size = this.readBuf.size();
        if (size > 0) {
            readData = true;
            for (int i = 0; i < size; ++i) {
                this.readPending = false;
                pipeline.fireChannelRead(this.readBuf.get(i));
            }
            this.readBuf.clear();
            allocHandle.readComplete();
            pipeline.fireChannelReadComplete();
        }
        if (exception != null) {
            if (exception instanceof IOException) {
                closed = true;
            }
            pipeline.fireExceptionCaught(exception);
        }
        if (closed) {
            if (this.isOpen()) {
                this.unsafe().close(this.unsafe().voidPromise());
            }
        }
        else if (this.readPending || config.isAutoRead() || (!readData && this.isActive())) {
            this.read();
        }
    }
    
    protected abstract int doReadMessages(final List<Object> p0) throws Exception;
}
