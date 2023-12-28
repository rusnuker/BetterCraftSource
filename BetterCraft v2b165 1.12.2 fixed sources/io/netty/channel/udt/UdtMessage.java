// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.udt;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;

@Deprecated
public final class UdtMessage extends DefaultByteBufHolder
{
    public UdtMessage(final ByteBuf data) {
        super(data);
    }
    
    @Override
    public UdtMessage copy() {
        return (UdtMessage)super.copy();
    }
    
    @Override
    public UdtMessage duplicate() {
        return (UdtMessage)super.duplicate();
    }
    
    @Override
    public UdtMessage retainedDuplicate() {
        return (UdtMessage)super.retainedDuplicate();
    }
    
    @Override
    public UdtMessage replace(final ByteBuf content) {
        return new UdtMessage(content);
    }
    
    @Override
    public UdtMessage retain() {
        super.retain();
        return this;
    }
    
    @Override
    public UdtMessage retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public UdtMessage touch() {
        super.touch();
        return this;
    }
    
    @Override
    public UdtMessage touch(final Object hint) {
        super.touch(hint);
        return this;
    }
}
