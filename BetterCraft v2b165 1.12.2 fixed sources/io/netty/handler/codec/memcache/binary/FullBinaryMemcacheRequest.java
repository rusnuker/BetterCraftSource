// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.memcache.FullMemcacheMessage;

public interface FullBinaryMemcacheRequest extends BinaryMemcacheRequest, FullMemcacheMessage
{
    FullBinaryMemcacheRequest copy();
    
    FullBinaryMemcacheRequest duplicate();
    
    FullBinaryMemcacheRequest retainedDuplicate();
    
    FullBinaryMemcacheRequest replace(final ByteBuf p0);
    
    FullBinaryMemcacheRequest retain(final int p0);
    
    FullBinaryMemcacheRequest retain();
    
    FullBinaryMemcacheRequest touch();
    
    FullBinaryMemcacheRequest touch(final Object p0);
}
