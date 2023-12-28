// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.websocketx;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class PingWebSocketFrame extends WebSocketFrame
{
    public PingWebSocketFrame() {
        super(true, 0, Unpooled.buffer(0));
    }
    
    public PingWebSocketFrame(final ByteBuf binaryData) {
        super(binaryData);
    }
    
    public PingWebSocketFrame(final boolean finalFragment, final int rsv, final ByteBuf binaryData) {
        super(finalFragment, rsv, binaryData);
    }
    
    @Override
    public PingWebSocketFrame copy() {
        return (PingWebSocketFrame)super.copy();
    }
    
    @Override
    public PingWebSocketFrame duplicate() {
        return (PingWebSocketFrame)super.duplicate();
    }
    
    @Override
    public PingWebSocketFrame retainedDuplicate() {
        return (PingWebSocketFrame)super.retainedDuplicate();
    }
    
    @Override
    public PingWebSocketFrame replace(final ByteBuf content) {
        return new PingWebSocketFrame(this.isFinalFragment(), this.rsv(), content);
    }
    
    @Override
    public PingWebSocketFrame retain() {
        super.retain();
        return this;
    }
    
    @Override
    public PingWebSocketFrame retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public PingWebSocketFrame touch() {
        super.touch();
        return this;
    }
    
    @Override
    public PingWebSocketFrame touch(final Object hint) {
        super.touch(hint);
        return this;
    }
}
