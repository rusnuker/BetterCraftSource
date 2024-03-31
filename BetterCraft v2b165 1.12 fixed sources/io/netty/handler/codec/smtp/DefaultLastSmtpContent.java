// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.smtp;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;

public final class DefaultLastSmtpContent extends DefaultSmtpContent implements LastSmtpContent
{
    public DefaultLastSmtpContent(final ByteBuf data) {
        super(data);
    }
    
    @Override
    public LastSmtpContent copy() {
        return (LastSmtpContent)super.copy();
    }
    
    @Override
    public LastSmtpContent duplicate() {
        return (LastSmtpContent)super.duplicate();
    }
    
    @Override
    public LastSmtpContent retainedDuplicate() {
        return (LastSmtpContent)super.retainedDuplicate();
    }
    
    @Override
    public LastSmtpContent replace(final ByteBuf content) {
        return new DefaultLastSmtpContent(content);
    }
    
    @Override
    public DefaultLastSmtpContent retain() {
        super.retain();
        return this;
    }
    
    @Override
    public DefaultLastSmtpContent retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public DefaultLastSmtpContent touch() {
        super.touch();
        return this;
    }
    
    @Override
    public DefaultLastSmtpContent touch(final Object hint) {
        super.touch(hint);
        return this;
    }
}
