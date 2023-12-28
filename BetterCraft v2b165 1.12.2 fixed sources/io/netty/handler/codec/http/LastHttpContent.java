// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.handler.codec.DecoderResult;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;

public interface LastHttpContent extends HttpContent
{
    public static final LastHttpContent EMPTY_LAST_CONTENT = new LastHttpContent() {
        @Override
        public ByteBuf content() {
            return Unpooled.EMPTY_BUFFER;
        }
        
        @Override
        public LastHttpContent copy() {
            return LastHttpContent$1.EMPTY_LAST_CONTENT;
        }
        
        @Override
        public LastHttpContent duplicate() {
            return this;
        }
        
        @Override
        public LastHttpContent replace(final ByteBuf content) {
            return new DefaultLastHttpContent(content);
        }
        
        @Override
        public LastHttpContent retainedDuplicate() {
            return this;
        }
        
        @Override
        public HttpHeaders trailingHeaders() {
            return EmptyHttpHeaders.INSTANCE;
        }
        
        @Override
        public DecoderResult decoderResult() {
            return DecoderResult.SUCCESS;
        }
        
        @Deprecated
        @Override
        public DecoderResult getDecoderResult() {
            return this.decoderResult();
        }
        
        @Override
        public void setDecoderResult(final DecoderResult result) {
            throw new UnsupportedOperationException("read only");
        }
        
        @Override
        public int refCnt() {
            return 1;
        }
        
        @Override
        public LastHttpContent retain() {
            return this;
        }
        
        @Override
        public LastHttpContent retain(final int increment) {
            return this;
        }
        
        @Override
        public LastHttpContent touch() {
            return this;
        }
        
        @Override
        public LastHttpContent touch(final Object hint) {
            return this;
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
        public String toString() {
            return "EmptyLastHttpContent";
        }
    };
    
    HttpHeaders trailingHeaders();
    
    LastHttpContent copy();
    
    LastHttpContent duplicate();
    
    LastHttpContent retainedDuplicate();
    
    LastHttpContent replace(final ByteBuf p0);
    
    LastHttpContent retain(final int p0);
    
    LastHttpContent retain();
    
    LastHttpContent touch();
    
    LastHttpContent touch(final Object p0);
}
