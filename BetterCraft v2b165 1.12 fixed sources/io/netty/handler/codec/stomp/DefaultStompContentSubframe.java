// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.stomp;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderResult;
import io.netty.buffer.DefaultByteBufHolder;

public class DefaultStompContentSubframe extends DefaultByteBufHolder implements StompContentSubframe
{
    private DecoderResult decoderResult;
    
    public DefaultStompContentSubframe(final ByteBuf content) {
        super(content);
        this.decoderResult = DecoderResult.SUCCESS;
    }
    
    @Override
    public StompContentSubframe copy() {
        return (StompContentSubframe)super.copy();
    }
    
    @Override
    public StompContentSubframe duplicate() {
        return (StompContentSubframe)super.duplicate();
    }
    
    @Override
    public StompContentSubframe retainedDuplicate() {
        return (StompContentSubframe)super.retainedDuplicate();
    }
    
    @Override
    public StompContentSubframe replace(final ByteBuf content) {
        return new DefaultStompContentSubframe(content);
    }
    
    @Override
    public StompContentSubframe retain() {
        super.retain();
        return this;
    }
    
    @Override
    public StompContentSubframe retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public StompContentSubframe touch() {
        super.touch();
        return this;
    }
    
    @Override
    public StompContentSubframe touch(final Object hint) {
        super.touch(hint);
        return this;
    }
    
    @Override
    public DecoderResult decoderResult() {
        return this.decoderResult;
    }
    
    @Override
    public void setDecoderResult(final DecoderResult decoderResult) {
        this.decoderResult = decoderResult;
    }
    
    @Override
    public String toString() {
        return "DefaultStompContent{decoderResult=" + this.decoderResult + '}';
    }
}
