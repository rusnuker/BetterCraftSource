// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.websocketx;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.internal.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;

public abstract class WebSocketFrame extends DefaultByteBufHolder
{
    private final boolean finalFragment;
    private final int rsv;
    
    protected WebSocketFrame(final ByteBuf binaryData) {
        this(true, 0, binaryData);
    }
    
    protected WebSocketFrame(final boolean finalFragment, final int rsv, final ByteBuf binaryData) {
        super(binaryData);
        this.finalFragment = finalFragment;
        this.rsv = rsv;
    }
    
    public boolean isFinalFragment() {
        return this.finalFragment;
    }
    
    public int rsv() {
        return this.rsv;
    }
    
    @Override
    public WebSocketFrame copy() {
        return (WebSocketFrame)super.copy();
    }
    
    @Override
    public WebSocketFrame duplicate() {
        return (WebSocketFrame)super.duplicate();
    }
    
    @Override
    public WebSocketFrame retainedDuplicate() {
        return (WebSocketFrame)super.retainedDuplicate();
    }
    
    @Override
    public abstract WebSocketFrame replace(final ByteBuf p0);
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + "(data: " + this.contentToString() + ')';
    }
    
    @Override
    public WebSocketFrame retain() {
        super.retain();
        return this;
    }
    
    @Override
    public WebSocketFrame retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public WebSocketFrame touch() {
        super.touch();
        return this;
    }
    
    @Override
    public WebSocketFrame touch(final Object hint) {
        super.touch(hint);
        return this;
    }
}
