// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.websocketx.extensions.compression;

import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

class PerFrameDeflateDecoder extends DeflateDecoder
{
    public PerFrameDeflateDecoder(final boolean noContext) {
        super(noContext);
    }
    
    @Override
    public boolean acceptInboundMessage(final Object msg) throws Exception {
        return (msg instanceof TextWebSocketFrame || msg instanceof BinaryWebSocketFrame || msg instanceof ContinuationWebSocketFrame) && (((WebSocketFrame)msg).rsv() & 0x4) > 0;
    }
    
    @Override
    protected int newRsv(final WebSocketFrame msg) {
        return msg.rsv() ^ 0x4;
    }
    
    @Override
    protected boolean appendFrameTail(final WebSocketFrame msg) {
        return true;
    }
}
