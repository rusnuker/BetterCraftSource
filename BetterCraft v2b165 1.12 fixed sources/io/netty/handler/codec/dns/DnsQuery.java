// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

public interface DnsQuery extends DnsMessage
{
    DnsQuery setId(final int p0);
    
    DnsQuery setOpCode(final DnsOpCode p0);
    
    DnsQuery setRecursionDesired(final boolean p0);
    
    DnsQuery setZ(final int p0);
    
    DnsQuery setRecord(final DnsSection p0, final DnsRecord p1);
    
    DnsQuery addRecord(final DnsSection p0, final DnsRecord p1);
    
    DnsQuery addRecord(final DnsSection p0, final int p1, final DnsRecord p2);
    
    DnsQuery clear(final DnsSection p0);
    
    DnsQuery clear();
    
    DnsQuery touch();
    
    DnsQuery touch(final Object p0);
    
    DnsQuery retain();
    
    DnsQuery retain(final int p0);
}
