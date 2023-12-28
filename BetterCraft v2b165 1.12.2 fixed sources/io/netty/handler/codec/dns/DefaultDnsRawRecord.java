// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.buffer.ByteBuf;

public class DefaultDnsRawRecord extends AbstractDnsRecord implements DnsRawRecord
{
    private final ByteBuf content;
    
    public DefaultDnsRawRecord(final String name, final DnsRecordType type, final long timeToLive, final ByteBuf content) {
        this(name, type, 1, timeToLive, content);
    }
    
    public DefaultDnsRawRecord(final String name, final DnsRecordType type, final int dnsClass, final long timeToLive, final ByteBuf content) {
        super(name, type, dnsClass, timeToLive);
        this.content = ObjectUtil.checkNotNull(content, "content");
    }
    
    @Override
    public ByteBuf content() {
        return this.content;
    }
    
    @Override
    public DnsRawRecord copy() {
        return this.replace(this.content().copy());
    }
    
    @Override
    public DnsRawRecord duplicate() {
        return this.replace(this.content().duplicate());
    }
    
    @Override
    public DnsRawRecord retainedDuplicate() {
        return this.replace(this.content().retainedDuplicate());
    }
    
    @Override
    public DnsRawRecord replace(final ByteBuf content) {
        return new DefaultDnsRawRecord(this.name(), this.type(), this.dnsClass(), this.timeToLive(), content);
    }
    
    @Override
    public int refCnt() {
        return this.content().refCnt();
    }
    
    @Override
    public DnsRawRecord retain() {
        this.content().retain();
        return this;
    }
    
    @Override
    public DnsRawRecord retain(final int increment) {
        this.content().retain(increment);
        return this;
    }
    
    @Override
    public boolean release() {
        return this.content().release();
    }
    
    @Override
    public boolean release(final int decrement) {
        return this.content().release(decrement);
    }
    
    @Override
    public DnsRawRecord touch() {
        this.content().touch();
        return this;
    }
    
    @Override
    public DnsRawRecord touch(final Object hint) {
        this.content().touch(hint);
        return this;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder(64).append(StringUtil.simpleClassName(this)).append('(');
        final DnsRecordType type = this.type();
        if (type != DnsRecordType.OPT) {
            buf.append(this.name().isEmpty() ? "<root>" : this.name()).append(' ').append(this.timeToLive()).append(' ');
            DnsMessageUtil.appendRecordClass(buf, this.dnsClass()).append(' ').append(type.name());
        }
        else {
            buf.append("OPT flags:").append(this.timeToLive()).append(" udp:").append(this.dnsClass());
        }
        buf.append(' ').append(this.content().readableBytes()).append("B)");
        return buf.toString();
    }
}
