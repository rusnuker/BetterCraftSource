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

class PerMessageDeflateDecoder extends DeflateDecoder
{
    private boolean compressing;
    
    public PerMessageDeflateDecoder(final boolean noContext) {
        super(noContext);
    }
    
    @Override
    public boolean acceptInboundMessage(final Object msg) throws Exception {
        return ((msg instanceof TextWebSocketFrame || msg instanceof BinaryWebSocketFrame) && (((WebSocketFrame)msg).rsv() & 0x4) > 0) || (msg instanceof ContinuationWebSocketFrame && this.compressing);
    }
    
    @Override
    protected int newRsv(final WebSocketFrame msg) {
        return ((msg.rsv() & 0x4) > 0) ? (msg.rsv() ^ 0x4) : msg.rsv();
    }
    
    @Override
    protected boolean appendFrameTail(final WebSocketFrame msg) {
        return msg.isFinalFragment();
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final WebSocketFrame msg, final List<Object> out) throws Exception {
        super.decode(ctx, msg, out);
        if (msg.isFinalFragment()) {
            this.compressing = false;
        }
        else if (msg instanceof TextWebSocketFrame || msg instanceof BinaryWebSocketFrame) {
            this.compressing = true;
        }
    }
}
