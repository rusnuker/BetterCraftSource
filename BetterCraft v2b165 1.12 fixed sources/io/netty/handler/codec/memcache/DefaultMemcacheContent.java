// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.memcache;

import io.netty.buffer.ByteBufHolder;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.StringUtil;
import io.netty.buffer.ByteBuf;

public class DefaultMemcacheContent extends AbstractMemcacheObject implements MemcacheContent
{
    private final ByteBuf content;
    
    public DefaultMemcacheContent(final ByteBuf content) {
        if (content == null) {
            throw new NullPointerException("Content cannot be null.");
        }
        this.content = content;
    }
    
    @Override
    public ByteBuf content() {
        return this.content;
    }
    
    @Override
    public MemcacheContent copy() {
        return this.replace(this.content.copy());
    }
    
    @Override
    public MemcacheContent duplicate() {
        return this.replace(this.content.duplicate());
    }
    
    @Override
    public MemcacheContent retainedDuplicate() {
        return this.replace(this.content.retainedDuplicate());
    }
    
    @Override
    public MemcacheContent replace(final ByteBuf content) {
        return new DefaultMemcacheContent(content);
    }
    
    @Override
    public MemcacheContent retain() {
        super.retain();
        return this;
    }
    
    @Override
    public MemcacheContent retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public MemcacheContent touch() {
        super.touch();
        return this;
    }
    
    @Override
    public MemcacheContent touch(final Object hint) {
        this.content.touch(hint);
        return this;
    }
    
    @Override
    protected void deallocate() {
        this.content.release();
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + "(data: " + this.content() + ", decoderResult: " + this.decoderResult() + ')';
    }
}
