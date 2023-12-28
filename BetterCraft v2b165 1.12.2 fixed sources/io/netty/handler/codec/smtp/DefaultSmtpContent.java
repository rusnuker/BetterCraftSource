// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.smtp;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;

public class DefaultSmtpContent extends DefaultByteBufHolder implements SmtpContent
{
    public DefaultSmtpContent(final ByteBuf data) {
        super(data);
    }
    
    @Override
    public SmtpContent copy() {
        return (SmtpContent)super.copy();
    }
    
    @Override
    public SmtpContent duplicate() {
        return (SmtpContent)super.duplicate();
    }
    
    @Override
    public SmtpContent retainedDuplicate() {
        return (SmtpContent)super.retainedDuplicate();
    }
    
    @Override
    public SmtpContent replace(final ByteBuf content) {
        return new DefaultSmtpContent(content);
    }
    
    @Override
    public SmtpContent retain() {
        super.retain();
        return this;
    }
    
    @Override
    public SmtpContent retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public SmtpContent touch() {
        super.touch();
        return this;
    }
    
    @Override
    public SmtpContent touch(final Object hint) {
        super.touch(hint);
        return this;
    }
}
