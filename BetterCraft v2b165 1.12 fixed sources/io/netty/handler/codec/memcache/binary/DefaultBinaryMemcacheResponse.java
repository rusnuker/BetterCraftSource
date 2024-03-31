// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.memcache.binary;

import io.netty.util.ReferenceCounted;
import io.netty.handler.codec.memcache.MemcacheMessage;
import io.netty.buffer.ByteBuf;

public class DefaultBinaryMemcacheResponse extends AbstractBinaryMemcacheMessage implements BinaryMemcacheResponse
{
    public static final byte RESPONSE_MAGIC_BYTE = -127;
    private short status;
    
    public DefaultBinaryMemcacheResponse() {
        this(null, null);
    }
    
    public DefaultBinaryMemcacheResponse(final ByteBuf key) {
        this(key, null);
    }
    
    public DefaultBinaryMemcacheResponse(final ByteBuf key, final ByteBuf extras) {
        super(key, extras);
        this.setMagic((byte)(-127));
    }
    
    @Override
    public short status() {
        return this.status;
    }
    
    @Override
    public BinaryMemcacheResponse setStatus(final short status) {
        this.status = status;
        return this;
    }
    
    @Override
    public BinaryMemcacheResponse retain() {
        super.retain();
        return this;
    }
    
    @Override
    public BinaryMemcacheResponse retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public BinaryMemcacheResponse touch() {
        super.touch();
        return this;
    }
    
    @Override
    public BinaryMemcacheResponse touch(final Object hint) {
        super.touch(hint);
        return this;
    }
}
