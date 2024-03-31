// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class ApplicationProtocolNegotiationHandler extends ChannelInboundHandlerAdapter
{
    private static final InternalLogger logger;
    private final String fallbackProtocol;
    
    protected ApplicationProtocolNegotiationHandler(final String fallbackProtocol) {
        this.fallbackProtocol = ObjectUtil.checkNotNull(fallbackProtocol, "fallbackProtocol");
    }
    
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
        if (evt instanceof SslHandshakeCompletionEvent) {
            ctx.pipeline().remove(this);
            final SslHandshakeCompletionEvent handshakeEvent = (SslHandshakeCompletionEvent)evt;
            if (handshakeEvent.isSuccess()) {
                final SslHandler sslHandler = ctx.pipeline().get(SslHandler.class);
                if (sslHandler == null) {
                    throw new IllegalStateException("cannot find a SslHandler in the pipeline (required for application-level protocol negotiation)");
                }
                final String protocol = sslHandler.applicationProtocol();
                this.configurePipeline(ctx, (protocol != null) ? protocol : this.fallbackProtocol);
            }
            else {
                this.handshakeFailure(ctx, handshakeEvent.cause());
            }
        }
        ctx.fireUserEventTriggered(evt);
    }
    
    protected abstract void configurePipeline(final ChannelHandlerContext p0, final String p1) throws Exception;
    
    protected void handshakeFailure(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        ApplicationProtocolNegotiationHandler.logger.warn("{} TLS handshake failed:", ctx.channel(), cause);
        ctx.close();
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        ApplicationProtocolNegotiationHandler.logger.warn("{} Failed to select the application-level protocol:", ctx.channel(), cause);
        ctx.close();
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(ApplicationProtocolNegotiationHandler.class);
    }
}
