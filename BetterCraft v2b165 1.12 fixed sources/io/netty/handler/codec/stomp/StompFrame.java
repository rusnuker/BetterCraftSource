// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.stomp;

import io.netty.buffer.ByteBuf;

public interface StompFrame extends StompHeadersSubframe, LastStompContentSubframe
{
    StompFrame copy();
    
    StompFrame duplicate();
    
    StompFrame retainedDuplicate();
    
    StompFrame replace(final ByteBuf p0);
    
    StompFrame retain();
    
    StompFrame retain(final int p0);
    
    StompFrame touch();
    
    StompFrame touch(final Object p0);
}
