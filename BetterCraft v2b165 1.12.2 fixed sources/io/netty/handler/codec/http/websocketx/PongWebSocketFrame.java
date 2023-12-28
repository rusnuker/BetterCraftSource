// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.websocketx;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class PongWebSocketFrame extends WebSocketFrame
{
    public PongWebSocketFrame() {
        super(Unpooled.buffer(0));
    }
    
    public PongWebSocketFrame(final ByteBuf binaryData) {
        super(binaryData);
    }
    
    public PongWebSocketFrame(final boolean finalFragment, final int rsv, final ByteBuf binaryData) {
        super(finalFragment, rsv, binaryData);
    }
    
    @Override
    public PongWebSocketFrame copy() {
        return (PongWebSocketFrame)super.copy();
    }
    
    @Override
    public PongWebSocketFrame duplicate() {
        return (PongWebSocketFrame)super.duplicate();
    }
    
    @Override
    public PongWebSocketFrame retainedDuplicate() {
        return (PongWebSocketFrame)super.retainedDuplicate();
    }
    
    @Override
    public PongWebSocketFrame replace(final ByteBuf content) {
        return new PongWebSocketFrame(this.isFinalFragment(), this.rsv(), content);
    }
    
    @Override
    public PongWebSocketFrame retain() {
        super.retain();
        return this;
    }
    
    @Override
    public PongWebSocketFrame retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public PongWebSocketFrame touch() {
        super.touch();
        return this;
    }
    
    @Override
    public PongWebSocketFrame touch(final Object hint) {
        super.touch(hint);
        return this;
    }
}
