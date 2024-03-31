// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import io.netty.util.internal.StringUtil;

public class DefaultDnsQuestion extends AbstractDnsRecord implements DnsQuestion
{
    public DefaultDnsQuestion(final String name, final DnsRecordType type) {
        super(name, type, 0L);
    }
    
    public DefaultDnsQuestion(final String name, final DnsRecordType type, final int dnsClass) {
        super(name, type, dnsClass, 0L);
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder(64);
        buf.append(StringUtil.simpleClassName(this)).append('(').append(this.name()).append(' ');
        DnsMessageUtil.appendRecordClass(buf, this.dnsClass()).append(' ').append(this.type().name()).append(')');
        return buf.toString();
    }
}
