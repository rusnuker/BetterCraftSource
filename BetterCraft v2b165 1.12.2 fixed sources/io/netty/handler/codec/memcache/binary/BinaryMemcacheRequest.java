// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.memcache.binary;

public interface BinaryMemcacheRequest extends BinaryMemcacheMessage
{
    short reserved();
    
    BinaryMemcacheRequest setReserved(final short p0);
    
    BinaryMemcacheRequest retain();
    
    BinaryMemcacheRequest retain(final int p0);
    
    BinaryMemcacheRequest touch();
    
    BinaryMemcacheRequest touch(final Object p0);
}
