// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.websocketx;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class BinaryWebSocketFrame extends WebSocketFrame
{
    public BinaryWebSocketFrame() {
        super(Unpooled.buffer(0));
    }
    
    public BinaryWebSocketFrame(final ByteBuf binaryData) {
        super(binaryData);
    }
    
    public BinaryWebSocketFrame(final boolean finalFragment, final int rsv, final ByteBuf binaryData) {
        super(finalFragment, rsv, binaryData);
    }
    
    @Override
    public BinaryWebSocketFrame copy() {
        return (BinaryWebSocketFrame)super.copy();
    }
    
    @Override
    public BinaryWebSocketFrame duplicate() {
        return (BinaryWebSocketFrame)super.duplicate();
    }
    
    @Override
    public BinaryWebSocketFrame retainedDuplicate() {
        return (BinaryWebSocketFrame)super.retainedDuplicate();
    }
    
    @Override
    public BinaryWebSocketFrame replace(final ByteBuf content) {
        return new BinaryWebSocketFrame(this.isFinalFragment(), this.rsv(), content);
    }
    
    @Override
    public BinaryWebSocketFrame retain() {
        super.retain();
        return this;
    }
    
    @Override
    public BinaryWebSocketFrame retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public BinaryWebSocketFrame touch() {
        super.touch();
        return this;
    }
    
    @Override
    public BinaryWebSocketFrame touch(final Object hint) {
        super.touch(hint);
        return this;
    }
}
