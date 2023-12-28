// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.DefaultByteBufHolder;

public final class DefaultHttp2GoAwayFrame extends DefaultByteBufHolder implements Http2GoAwayFrame
{
    private final long errorCode;
    private final int lastStreamId;
    private int extraStreamIds;
    
    public DefaultHttp2GoAwayFrame(final Http2Error error) {
        this(error.code());
    }
    
    public DefaultHttp2GoAwayFrame(final long errorCode) {
        this(errorCode, Unpooled.EMPTY_BUFFER);
    }
    
    public DefaultHttp2GoAwayFrame(final Http2Error error, final ByteBuf content) {
        this(error.code(), content);
    }
    
    public DefaultHttp2GoAwayFrame(final long errorCode, final ByteBuf content) {
        this(-1, errorCode, content);
    }
    
    DefaultHttp2GoAwayFrame(final int lastStreamId, final long errorCode, final ByteBuf content) {
        super(content);
        this.errorCode = errorCode;
        this.lastStreamId = lastStreamId;
    }
    
    @Override
    public String name() {
        return "GOAWAY";
    }
    
    @Override
    public long errorCode() {
        return this.errorCode;
    }
    
    @Override
    public int extraStreamIds() {
        return this.extraStreamIds;
    }
    
    @Override
    public Http2GoAwayFrame setExtraStreamIds(final int extraStreamIds) {
        if (extraStreamIds < 0) {
            throw new IllegalArgumentException("extraStreamIds must be non-negative");
        }
        this.extraStreamIds = extraStreamIds;
        return this;
    }
    
    @Override
    public int lastStreamId() {
        return this.lastStreamId;
    }
    
    @Override
    public Http2GoAwayFrame copy() {
        return new DefaultHttp2GoAwayFrame(this.lastStreamId, this.errorCode, this.content().copy());
    }
    
    @Override
    public Http2GoAwayFrame duplicate() {
        return (Http2GoAwayFrame)super.duplicate();
    }
    
    @Override
    public Http2GoAwayFrame retainedDuplicate() {
        return (Http2GoAwayFrame)super.retainedDuplicate();
    }
    
    @Override
    public Http2GoAwayFrame replace(final ByteBuf content) {
        return new DefaultHttp2GoAwayFrame(this.errorCode, content).setExtraStreamIds(this.extraStreamIds);
    }
    
    @Override
    public Http2GoAwayFrame retain() {
        super.retain();
        return this;
    }
    
    @Override
    public Http2GoAwayFrame retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public Http2GoAwayFrame touch() {
        super.touch();
        return this;
    }
    
    @Override
    public Http2GoAwayFrame touch(final Object hint) {
        super.touch(hint);
        return this;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof DefaultHttp2GoAwayFrame)) {
            return false;
        }
        final DefaultHttp2GoAwayFrame other = (DefaultHttp2GoAwayFrame)o;
        return super.equals(o) && this.errorCode == other.errorCode && this.content().equals(other.content()) && this.extraStreamIds == other.extraStreamIds;
    }
    
    @Override
    public int hashCode() {
        int hash = 237395317;
        hash = hash * 31 + (int)(this.errorCode ^ this.errorCode >>> 32);
        hash = hash * 31 + this.content().hashCode();
        hash = hash * 31 + this.extraStreamIds;
        return hash;
    }
    
    @Override
    public String toString() {
        return "DefaultHttp2GoAwayFrame(errorCode=" + this.errorCode + ", content=" + this.content() + ", extraStreamIds=" + this.extraStreamIds + ", lastStreamId=" + this.lastStreamId + ")";
    }
}
