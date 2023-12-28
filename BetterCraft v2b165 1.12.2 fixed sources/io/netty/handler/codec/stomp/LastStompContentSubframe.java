// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.stomp;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.handler.codec.DecoderResult;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;

public interface LastStompContentSubframe extends StompContentSubframe
{
    public static final LastStompContentSubframe EMPTY_LAST_CONTENT = new LastStompContentSubframe() {
        @Override
        public ByteBuf content() {
            return Unpooled.EMPTY_BUFFER;
        }
        
        @Override
        public LastStompContentSubframe copy() {
            return LastStompContentSubframe$1.EMPTY_LAST_CONTENT;
        }
        
        @Override
        public LastStompContentSubframe duplicate() {
            return this;
        }
        
        @Override
        public LastStompContentSubframe retainedDuplicate() {
            return this;
        }
        
        @Override
        public LastStompContentSubframe replace(final ByteBuf content) {
            return new DefaultLastStompContentSubframe(content);
        }
        
        @Override
        public LastStompContentSubframe retain() {
            return this;
        }
        
        @Override
        public LastStompContentSubframe retain(final int increment) {
            return this;
        }
        
        @Override
        public LastStompContentSubframe touch() {
            return this;
        }
        
        @Override
        public LastStompContentSubframe touch(final Object hint) {
            return this;
        }
        
        @Override
        public int refCnt() {
            return 1;
        }
        
        @Override
        public boolean release() {
            return false;
        }
        
        @Override
        public boolean release(final int decrement) {
            return false;
        }
        
        @Override
        public DecoderResult decoderResult() {
            return DecoderResult.SUCCESS;
        }
        
        @Override
        public void setDecoderResult(final DecoderResult result) {
            throw new UnsupportedOperationException("read only");
        }
    };
    
    LastStompContentSubframe copy();
    
    LastStompContentSubframe duplicate();
    
    LastStompContentSubframe retainedDuplicate();
    
    LastStompContentSubframe replace(final ByteBuf p0);
    
    LastStompContentSubframe retain();
    
    LastStompContentSubframe retain(final int p0);
    
    LastStompContentSubframe touch();
    
    LastStompContentSubframe touch(final Object p0);
}
