// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface HttpContent extends HttpObject, ByteBufHolder
{
    HttpContent copy();
    
    HttpContent duplicate();
    
    HttpContent retainedDuplicate();
    
    HttpContent replace(final ByteBuf p0);
    
    HttpContent retain();
    
    HttpContent retain(final int p0);
    
    HttpContent touch();
    
    HttpContent touch(final Object p0);
}
