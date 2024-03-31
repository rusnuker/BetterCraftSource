// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.ByteBufHolder;
import io.netty.handler.codec.memcache.MemcacheContent;
import io.netty.handler.codec.memcache.LastMemcacheContent;
import io.netty.handler.codec.memcache.FullMemcacheMessage;
import io.netty.util.ReferenceCounted;
import io.netty.handler.codec.memcache.MemcacheMessage;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;

public class DefaultFullBinaryMemcacheRequest extends DefaultBinaryMemcacheRequest implements FullBinaryMemcacheRequest
{
    private final ByteBuf content;
    
    public DefaultFullBinaryMemcacheRequest(final ByteBuf key, final ByteBuf extras) {
        this(key, extras, Unpooled.buffer(0));
    }
    
    public DefaultFullBinaryMemcacheRequest(final ByteBuf key, final ByteBuf extras, final ByteBuf content) {
        super(key, extras);
        if (content == null) {
            throw new NullPointerException("Supplied content is null.");
        }
        this.content = content;
        this.setTotalBodyLength(this.keyLength() + this.extrasLength() + content.readableBytes());
    }
    
    @Override
    public ByteBuf content() {
        return this.content;
    }
    
    @Override
    public FullBinaryMemcacheRequest retain() {
        super.retain();
        return this;
    }
    
    @Override
    public FullBinaryMemcacheRequest retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public FullBinaryMemcacheRequest touch() {
        super.touch();
        return this;
    }
    
    @Override
    public FullBinaryMemcacheRequest touch(final Object hint) {
        super.touch(hint);
        this.content.touch(hint);
        return this;
    }
    
    @Override
    protected void deallocate() {
        super.deallocate();
        this.content.release();
    }
    
    @Override
    public FullBinaryMemcacheRequest copy() {
        ByteBuf key = this.key();
        if (key != null) {
            key = key.copy();
        }
        ByteBuf extras = this.extras();
        if (extras != null) {
            extras = extras.copy();
        }
        return new DefaultFullBinaryMemcacheRequest(key, extras, this.content().copy());
    }
    
    @Override
    public FullBinaryMemcacheRequest duplicate() {
        ByteBuf key = this.key();
        if (key != null) {
            key = key.duplicate();
        }
        ByteBuf extras = this.extras();
        if (extras != null) {
            extras = extras.duplicate();
        }
        return new DefaultFullBinaryMemcacheRequest(key, extras, this.content().duplicate());
    }
    
    @Override
    public FullBinaryMemcacheRequest retainedDuplicate() {
        return this.replace(this.content().retainedDuplicate());
    }
    
    @Override
    public FullBinaryMemcacheRequest replace(final ByteBuf content) {
        ByteBuf key = this.key();
        if (key != null) {
            key = key.retainedDuplicate();
        }
        ByteBuf extras = this.extras();
        if (extras != null) {
            extras = extras.retainedDuplicate();
        }
        return new DefaultFullBinaryMemcacheRequest(key, extras, content);
    }
}
