// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.timeout;

import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.TimeUnit;

public class ReadTimeoutHandler extends IdleStateHandler
{
    private boolean closed;
    
    public ReadTimeoutHandler(final int timeoutSeconds) {
        this(timeoutSeconds, TimeUnit.SECONDS);
    }
    
    public ReadTimeoutHandler(final long timeout, final TimeUnit unit) {
        super(timeout, 0L, 0L, unit);
    }
    
    @Override
    protected final void channelIdle(final ChannelHandlerContext ctx, final IdleStateEvent evt) throws Exception {
        assert evt.state() == IdleState.READER_IDLE;
        this.readTimedOut(ctx);
    }
    
    protected void readTimedOut(final ChannelHandlerContext ctx) throws Exception {
        if (!this.closed) {
            ctx.fireExceptionCaught((Throwable)ReadTimeoutException.INSTANCE);
            ctx.close();
            this.closed = true;
        }
    }
}
