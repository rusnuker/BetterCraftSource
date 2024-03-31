// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import io.netty.util.internal.StringUtil;
import java.net.IDN;
import io.netty.util.internal.ObjectUtil;

public abstract class AbstractDnsRecord implements DnsRecord
{
    private final String name;
    private final DnsRecordType type;
    private final short dnsClass;
    private final long timeToLive;
    private int hashCode;
    
    protected AbstractDnsRecord(final String name, final DnsRecordType type, final long timeToLive) {
        this(name, type, 1, timeToLive);
    }
    
    protected AbstractDnsRecord(final String name, final DnsRecordType type, final int dnsClass, final long timeToLive) {
        if (timeToLive < 0L) {
            throw new IllegalArgumentException("timeToLive: " + timeToLive + " (expected: >= 0)");
        }
        this.name = appendTrailingDot(IDN.toASCII(ObjectUtil.checkNotNull(name, "name")));
        this.type = ObjectUtil.checkNotNull(type, "type");
        this.dnsClass = (short)dnsClass;
        this.timeToLive = timeToLive;
    }
    
    private static String appendTrailingDot(final String name) {
        if (name.length() > 0 && name.charAt(name.length() - 1) != '.') {
            return name + '.';
        }
        return name;
    }
    
    @Override
    public String name() {
        return this.name;
    }
    
    @Override
    public DnsRecordType type() {
        return this.type;
    }
    
    @Override
    public int dnsClass() {
        return this.dnsClass & 0xFFFF;
    }
    
    @Override
    public long timeToLive() {
        return this.timeToLive;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DnsRecord)) {
            return false;
        }
        final DnsRecord that = (DnsRecord)obj;
        final int hashCode = this.hashCode;
        return (hashCode == 0 || hashCode == that.hashCode()) && this.type().intValue() == that.type().intValue() && this.dnsClass() == that.dnsClass() && this.name().equals(that.name());
    }
    
    @Override
    public int hashCode() {
        final int hashCode = this.hashCode;
        if (hashCode != 0) {
            return hashCode;
        }
        return this.hashCode = this.name.hashCode() * 31 + this.type().intValue() * 31 + this.dnsClass();
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder(64);
        buf.append(StringUtil.simpleClassName(this)).append('(').append(this.name()).append(' ').append(this.timeToLive()).append(' ');
        DnsMessageUtil.appendRecordClass(buf, this.dnsClass()).append(' ').append(this.type().name()).append(')');
        return buf.toString();
    }
}
