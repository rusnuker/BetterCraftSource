// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.smtp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface SmtpContent extends ByteBufHolder
{
    SmtpContent copy();
    
    SmtpContent duplicate();
    
    SmtpContent retainedDuplicate();
    
    SmtpContent replace(final ByteBuf p0);
    
    SmtpContent retain();
    
    SmtpContent retain(final int p0);
    
    SmtpContent touch();
    
    SmtpContent touch(final Object p0);
}
