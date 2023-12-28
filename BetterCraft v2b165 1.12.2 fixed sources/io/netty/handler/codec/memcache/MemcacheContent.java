// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.memcache;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface MemcacheContent extends MemcacheObject, ByteBufHolder
{
    MemcacheContent copy();
    
    MemcacheContent duplicate();
    
    MemcacheContent retainedDuplicate();
    
    MemcacheContent replace(final ByteBuf p0);
    
    MemcacheContent retain();
    
    MemcacheContent retain(final int p0);
    
    MemcacheContent touch();
    
    MemcacheContent touch(final Object p0);
}
