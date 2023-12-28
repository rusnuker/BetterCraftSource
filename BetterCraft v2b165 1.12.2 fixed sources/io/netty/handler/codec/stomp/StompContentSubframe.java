// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.stomp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface StompContentSubframe extends ByteBufHolder, StompSubframe
{
    StompContentSubframe copy();
    
    StompContentSubframe duplicate();
    
    StompContentSubframe retainedDuplicate();
    
    StompContentSubframe replace(final ByteBuf p0);
    
    StompContentSubframe retain();
    
    StompContentSubframe retain(final int p0);
    
    StompContentSubframe touch();
    
    StompContentSubframe touch(final Object p0);
}
