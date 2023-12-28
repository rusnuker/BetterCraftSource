// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.ConcurrentMap;
import io.netty.util.internal.logging.InternalLogger;

@ChannelHandler.Sharable
public abstract class ChannelInitializer<C extends Channel> extends ChannelInboundHandlerAdapter
{
    private static final InternalLogger logger;
    private final ConcurrentMap<ChannelHandlerContext, Boolean> initMap;
    
    public ChannelInitializer() {
        this.initMap = PlatformDependent.newConcurrentHashMap();
    }
    
    protected abstract void initChannel(final C p0) throws Exception;
    
    @Override
    public final void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
        if (this.initChannel(ctx)) {
            ctx.pipeline().fireChannelRegistered();
        }
        else {
            ctx.fireChannelRegistered();
        }
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        ChannelInitializer.logger.warn("Failed to initialize a channel. Closing: " + ctx.channel(), cause);
        ctx.close();
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().isRegistered()) {
            this.initChannel(ctx);
        }
    }
    
    private boolean initChannel(final ChannelHandlerContext ctx) throws Exception {
        if (this.initMap.putIfAbsent(ctx, Boolean.TRUE) == null) {
            try {
                this.initChannel(ctx.channel());
            }
            catch (final Throwable cause) {
                this.exceptionCaught(ctx, cause);
            }
            finally {
                this.remove(ctx);
            }
            return true;
        }
        return false;
    }
    
    private void remove(final ChannelHandlerContext ctx) {
        try {
            final ChannelPipeline pipeline = ctx.pipeline();
            if (pipeline.context(this) != null) {
                pipeline.remove(this);
            }
        }
        finally {
            this.initMap.remove(ctx);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(ChannelInitializer.class);
    }
}
