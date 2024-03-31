// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.websocketx;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.CharsetUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ContinuationWebSocketFrame extends WebSocketFrame
{
    public ContinuationWebSocketFrame() {
        this(Unpooled.buffer(0));
    }
    
    public ContinuationWebSocketFrame(final ByteBuf binaryData) {
        super(binaryData);
    }
    
    public ContinuationWebSocketFrame(final boolean finalFragment, final int rsv, final ByteBuf binaryData) {
        super(finalFragment, rsv, binaryData);
    }
    
    public ContinuationWebSocketFrame(final boolean finalFragment, final int rsv, final String text) {
        this(finalFragment, rsv, fromText(text));
    }
    
    public String text() {
        return this.content().toString(CharsetUtil.UTF_8);
    }
    
    private static ByteBuf fromText(final String text) {
        if (text == null || text.isEmpty()) {
            return Unpooled.EMPTY_BUFFER;
        }
        return Unpooled.copiedBuffer(text, CharsetUtil.UTF_8);
    }
    
    @Override
    public ContinuationWebSocketFrame copy() {
        return (ContinuationWebSocketFrame)super.copy();
    }
    
    @Override
    public ContinuationWebSocketFrame duplicate() {
        return (ContinuationWebSocketFrame)super.duplicate();
    }
    
    @Override
    public ContinuationWebSocketFrame retainedDuplicate() {
        return (ContinuationWebSocketFrame)super.retainedDuplicate();
    }
    
    @Override
    public ContinuationWebSocketFrame replace(final ByteBuf content) {
        return new ContinuationWebSocketFrame(this.isFinalFragment(), this.rsv(), content);
    }
    
    @Override
    public ContinuationWebSocketFrame retain() {
        super.retain();
        return this;
    }
    
    @Override
    public ContinuationWebSocketFrame retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public ContinuationWebSocketFrame touch() {
        super.touch();
        return this;
    }
    
    @Override
    public ContinuationWebSocketFrame touch(final Object hint) {
        super.touch(hint);
        return this;
    }
}
