// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.memcache;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class DefaultLastMemcacheContent extends DefaultMemcacheContent implements LastMemcacheContent
{
    public DefaultLastMemcacheContent() {
        super(Unpooled.buffer());
    }
    
    public DefaultLastMemcacheContent(final ByteBuf content) {
        super(content);
    }
    
    @Override
    public LastMemcacheContent retain() {
        super.retain();
        return this;
    }
    
    @Override
    public LastMemcacheContent retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public LastMemcacheContent touch() {
        super.touch();
        return this;
    }
    
    @Override
    public LastMemcacheContent touch(final Object hint) {
        super.touch(hint);
        return this;
    }
    
    @Override
    public LastMemcacheContent copy() {
        return this.replace(this.content().copy());
    }
    
    @Override
    public LastMemcacheContent duplicate() {
        return this.replace(this.content().duplicate());
    }
    
    @Override
    public LastMemcacheContent retainedDuplicate() {
        return this.replace(this.content().retainedDuplicate());
    }
    
    @Override
    public LastMemcacheContent replace(final ByteBuf content) {
        return new DefaultLastMemcacheContent(content);
    }
}
