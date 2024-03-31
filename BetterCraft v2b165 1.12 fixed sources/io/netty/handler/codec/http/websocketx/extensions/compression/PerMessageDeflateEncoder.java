// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.websocketx.extensions.compression;

import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

class PerMessageDeflateEncoder extends DeflateEncoder
{
    private boolean compressing;
    
    public PerMessageDeflateEncoder(final int compressionLevel, final int windowSize, final boolean noContext) {
        super(compressionLevel, windowSize, noContext);
    }
    
    @Override
    public boolean acceptOutboundMessage(final Object msg) throws Exception {
        return ((msg instanceof TextWebSocketFrame || msg instanceof BinaryWebSocketFrame) && (((WebSocketFrame)msg).rsv() & 0x4) == 0x0) || (msg instanceof ContinuationWebSocketFrame && this.compressing);
    }
    
    @Override
    protected int rsv(final WebSocketFrame msg) {
        return (msg instanceof TextWebSocketFrame || msg instanceof BinaryWebSocketFrame) ? (msg.rsv() | 0x4) : msg.rsv();
    }
    
    @Override
    protected boolean removeFrameTail(final WebSocketFrame msg) {
        return msg.isFinalFragment();
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final WebSocketFrame msg, final List<Object> out) throws Exception {
        super.encode(ctx, msg, out);
        if (msg.isFinalFragment()) {
            this.compressing = false;
        }
        else if (msg instanceof TextWebSocketFrame || msg instanceof BinaryWebSocketFrame) {
            this.compressing = true;
        }
    }
}
