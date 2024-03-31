// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.memcache.FullMemcacheMessage;

public interface FullBinaryMemcacheResponse extends BinaryMemcacheResponse, FullMemcacheMessage
{
    FullBinaryMemcacheResponse copy();
    
    FullBinaryMemcacheResponse duplicate();
    
    FullBinaryMemcacheResponse retainedDuplicate();
    
    FullBinaryMemcacheResponse replace(final ByteBuf p0);
    
    FullBinaryMemcacheResponse retain(final int p0);
    
    FullBinaryMemcacheResponse retain();
    
    FullBinaryMemcacheResponse touch();
    
    FullBinaryMemcacheResponse touch(final Object p0);
}
