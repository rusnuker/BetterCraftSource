// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import io.netty.util.internal.StringUtil;
import io.netty.util.internal.ObjectUtil;

public class DefaultDnsPtrRecord extends AbstractDnsRecord implements DnsPtrRecord
{
    private final String hostname;
    
    public DefaultDnsPtrRecord(final String name, final int dnsClass, final long timeToLive, final String hostname) {
        super(name, DnsRecordType.PTR, dnsClass, timeToLive);
        this.hostname = ObjectUtil.checkNotNull(hostname, "hostname");
    }
    
    @Override
    public String hostname() {
        return this.hostname;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder(64).append(StringUtil.simpleClassName(this)).append('(');
        final DnsRecordType type = this.type();
        buf.append(this.name().isEmpty() ? "<root>" : this.name()).append(' ').append(this.timeToLive()).append(' ');
        DnsMessageUtil.appendRecordClass(buf, this.dnsClass()).append(' ').append(type.name());
        buf.append(' ').append(this.hostname);
        return buf.toString();
    }
}
