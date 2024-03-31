// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.websocketx.extensions.compression;

import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

class PerFrameDeflateEncoder extends DeflateEncoder
{
    public PerFrameDeflateEncoder(final int compressionLevel, final int windowSize, final boolean noContext) {
        super(compressionLevel, windowSize, noContext);
    }
    
    @Override
    public boolean acceptOutboundMessage(final Object msg) throws Exception {
        return (msg instanceof TextWebSocketFrame || msg instanceof BinaryWebSocketFrame || msg instanceof ContinuationWebSocketFrame) && ((WebSocketFrame)msg).content().readableBytes() > 0 && (((WebSocketFrame)msg).rsv() & 0x4) == 0x0;
    }
    
    @Override
    protected int rsv(final WebSocketFrame msg) {
        return msg.rsv() | 0x4;
    }
    
    @Override
    protected boolean removeFrameTail(final WebSocketFrame msg) {
        return true;
    }
}
