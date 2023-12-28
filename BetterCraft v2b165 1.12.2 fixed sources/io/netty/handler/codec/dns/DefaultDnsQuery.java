// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import io.netty.util.ReferenceCounted;

public class DefaultDnsQuery extends AbstractDnsMessage implements DnsQuery
{
    public DefaultDnsQuery(final int id) {
        super(id);
    }
    
    public DefaultDnsQuery(final int id, final DnsOpCode opCode) {
        super(id, opCode);
    }
    
    @Override
    public DnsQuery setId(final int id) {
        return (DnsQuery)super.setId(id);
    }
    
    @Override
    public DnsQuery setOpCode(final DnsOpCode opCode) {
        return (DnsQuery)super.setOpCode(opCode);
    }
    
    @Override
    public DnsQuery setRecursionDesired(final boolean recursionDesired) {
        return (DnsQuery)super.setRecursionDesired(recursionDesired);
    }
    
    @Override
    public DnsQuery setZ(final int z) {
        return (DnsQuery)super.setZ(z);
    }
    
    @Override
    public DnsQuery setRecord(final DnsSection section, final DnsRecord record) {
        return (DnsQuery)super.setRecord(section, record);
    }
    
    @Override
    public DnsQuery addRecord(final DnsSection section, final DnsRecord record) {
        return (DnsQuery)super.addRecord(section, record);
    }
    
    @Override
    public DnsQuery addRecord(final DnsSection section, final int index, final DnsRecord record) {
        return (DnsQuery)super.addRecord(section, index, record);
    }
    
    @Override
    public DnsQuery clear(final DnsSection section) {
        return (DnsQuery)super.clear(section);
    }
    
    @Override
    public DnsQuery clear() {
        return (DnsQuery)super.clear();
    }
    
    @Override
    public DnsQuery touch() {
        return (DnsQuery)super.touch();
    }
    
    @Override
    public DnsQuery touch(final Object hint) {
        return (DnsQuery)super.touch(hint);
    }
    
    @Override
    public DnsQuery retain() {
        return (DnsQuery)super.retain();
    }
    
    @Override
    public DnsQuery retain(final int increment) {
        return (DnsQuery)super.retain(increment);
    }
    
    @Override
    public String toString() {
        return DnsMessageUtil.appendQuery(new StringBuilder(128), this).toString();
    }
}
