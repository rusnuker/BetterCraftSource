// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.smtp;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;

public interface LastSmtpContent extends SmtpContent
{
    public static final LastSmtpContent EMPTY_LAST_CONTENT = new LastSmtpContent() {
        @Override
        public LastSmtpContent copy() {
            return this;
        }
        
        @Override
        public LastSmtpContent duplicate() {
            return this;
        }
        
        @Override
        public LastSmtpContent retainedDuplicate() {
            return this;
        }
        
        @Override
        public LastSmtpContent replace(final ByteBuf content) {
            return new DefaultLastSmtpContent(content);
        }
        
        @Override
        public LastSmtpContent retain() {
            return this;
        }
        
        @Override
        public LastSmtpContent retain(final int increment) {
            return this;
        }
        
        @Override
        public LastSmtpContent touch() {
            return this;
        }
        
        @Override
        public LastSmtpContent touch(final Object hint) {
            return this;
        }
        
        @Override
        public ByteBuf content() {
            return Unpooled.EMPTY_BUFFER;
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
    };
    
    LastSmtpContent copy();
    
    LastSmtpContent duplicate();
    
    LastSmtpContent retainedDuplicate();
    
    LastSmtpContent replace(final ByteBuf p0);
    
    LastSmtpContent retain();
    
    LastSmtpContent retain(final int p0);
    
    LastSmtpContent touch();
    
    LastSmtpContent touch(final Object p0);
}
