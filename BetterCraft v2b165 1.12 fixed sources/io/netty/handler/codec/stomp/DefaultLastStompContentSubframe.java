// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.stomp;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;

public class DefaultLastStompContentSubframe extends DefaultStompContentSubframe implements LastStompContentSubframe
{
    public DefaultLastStompContentSubframe(final ByteBuf content) {
        super(content);
    }
    
    @Override
    public LastStompContentSubframe copy() {
        return (LastStompContentSubframe)super.copy();
    }
    
    @Override
    public LastStompContentSubframe duplicate() {
        return (LastStompContentSubframe)super.duplicate();
    }
    
    @Override
    public LastStompContentSubframe retainedDuplicate() {
        return (LastStompContentSubframe)super.retainedDuplicate();
    }
    
    @Override
    public LastStompContentSubframe replace(final ByteBuf content) {
        return new DefaultLastStompContentSubframe(content);
    }
    
    @Override
    public DefaultLastStompContentSubframe retain() {
        super.retain();
        return this;
    }
    
    @Override
    public LastStompContentSubframe retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public LastStompContentSubframe touch() {
        super.touch();
        return this;
    }
    
    @Override
    public LastStompContentSubframe touch(final Object hint) {
        super.touch(hint);
        return this;
    }
    
    @Override
    public String toString() {
        return "DefaultLastStompContent{decoderResult=" + this.decoderResult() + '}';
    }
}
