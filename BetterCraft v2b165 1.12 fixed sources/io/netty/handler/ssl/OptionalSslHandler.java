// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import io.netty.util.ReferenceCountUtil;
import io.netty.channel.ChannelHandler;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import io.netty.handler.codec.ByteToMessageDecoder;

public class OptionalSslHandler extends ByteToMessageDecoder
{
    private final SslContext sslContext;
    
    public OptionalSslHandler(final SslContext sslContext) {
        this.sslContext = ObjectUtil.checkNotNull(sslContext, "sslContext");
    }
    
    @Override
    protected void decode(final ChannelHandlerContext context, final ByteBuf in, final List<Object> out) throws Exception {
        if (in.readableBytes() < 5) {
            return;
        }
        if (SslHandler.isEncrypted(in)) {
            this.handleSsl(context);
        }
        else {
            this.handleNonSsl(context);
        }
    }
    
    private void handleSsl(final ChannelHandlerContext context) {
        SslHandler sslHandler = null;
        try {
            sslHandler = this.newSslHandler(context, this.sslContext);
            context.pipeline().replace(this, this.newSslHandlerName(), sslHandler);
            sslHandler = null;
        }
        finally {
            if (sslHandler != null) {
                ReferenceCountUtil.safeRelease(sslHandler.engine());
            }
        }
    }
    
    private void handleNonSsl(final ChannelHandlerContext context) {
        final ChannelHandler handler = this.newNonSslHandler(context);
        if (handler != null) {
            context.pipeline().replace(this, this.newNonSslHandlerName(), handler);
        }
        else {
            context.pipeline().remove(this);
        }
    }
    
    protected String newSslHandlerName() {
        return null;
    }
    
    protected SslHandler newSslHandler(final ChannelHandlerContext context, final SslContext sslContext) {
        return sslContext.newHandler(context.alloc());
    }
    
    protected String newNonSslHandlerName() {
        return null;
    }
    
    protected ChannelHandler newNonSslHandler(final ChannelHandlerContext context) {
        return null;
    }
}
