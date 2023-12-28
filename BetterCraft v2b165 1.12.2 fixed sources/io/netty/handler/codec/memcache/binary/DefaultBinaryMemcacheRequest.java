// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.memcache.binary;

import io.netty.util.ReferenceCounted;
import io.netty.handler.codec.memcache.MemcacheMessage;
import io.netty.buffer.ByteBuf;

public class DefaultBinaryMemcacheRequest extends AbstractBinaryMemcacheMessage implements BinaryMemcacheRequest
{
    public static final byte REQUEST_MAGIC_BYTE = Byte.MIN_VALUE;
    private short reserved;
    
    public DefaultBinaryMemcacheRequest() {
        this(null, null);
    }
    
    public DefaultBinaryMemcacheRequest(final ByteBuf key) {
        this(key, null);
    }
    
    public DefaultBinaryMemcacheRequest(final ByteBuf key, final ByteBuf extras) {
        super(key, extras);
        this.setMagic((byte)(-128));
    }
    
    @Override
    public short reserved() {
        return this.reserved;
    }
    
    @Override
    public BinaryMemcacheRequest setReserved(final short reserved) {
        this.reserved = reserved;
        return this;
    }
    
    @Override
    public BinaryMemcacheRequest retain() {
        super.retain();
        return this;
    }
    
    @Override
    public BinaryMemcacheRequest retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public BinaryMemcacheRequest touch() {
        super.touch();
        return this;
    }
    
    @Override
    public BinaryMemcacheRequest touch(final Object hint) {
        super.touch(hint);
        return this;
    }
}
