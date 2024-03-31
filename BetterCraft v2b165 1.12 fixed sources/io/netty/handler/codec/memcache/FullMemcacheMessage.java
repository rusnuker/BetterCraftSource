// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.memcache;

import io.netty.buffer.ByteBuf;

public interface FullMemcacheMessage extends MemcacheMessage, LastMemcacheContent
{
    FullMemcacheMessage copy();
    
    FullMemcacheMessage duplicate();
    
    FullMemcacheMessage retainedDuplicate();
    
    FullMemcacheMessage replace(final ByteBuf p0);
    
    FullMemcacheMessage retain(final int p0);
    
    FullMemcacheMessage retain();
    
    FullMemcacheMessage touch();
    
    FullMemcacheMessage touch(final Object p0);
}
