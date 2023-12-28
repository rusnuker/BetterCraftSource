// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.memcache;

import io.netty.util.ReferenceCounted;

public interface MemcacheMessage extends MemcacheObject, ReferenceCounted
{
    MemcacheMessage retain();
    
    MemcacheMessage retain(final int p0);
    
    MemcacheMessage touch();
    
    MemcacheMessage touch(final Object p0);
}
