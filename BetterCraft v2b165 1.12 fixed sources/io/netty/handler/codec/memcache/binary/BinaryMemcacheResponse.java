// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.memcache.binary;

public interface BinaryMemcacheResponse extends BinaryMemcacheMessage
{
    short status();
    
    BinaryMemcacheResponse setStatus(final short p0);
    
    BinaryMemcacheResponse retain();
    
    BinaryMemcacheResponse retain(final int p0);
    
    BinaryMemcacheResponse touch();
    
    BinaryMemcacheResponse touch(final Object p0);
}
