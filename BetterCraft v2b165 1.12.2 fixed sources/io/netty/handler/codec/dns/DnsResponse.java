// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

public interface DnsResponse extends DnsMessage
{
    boolean isAuthoritativeAnswer();
    
    DnsResponse setAuthoritativeAnswer(final boolean p0);
    
    boolean isTruncated();
    
    DnsResponse setTruncated(final boolean p0);
    
    boolean isRecursionAvailable();
    
    DnsResponse setRecursionAvailable(final boolean p0);
    
    DnsResponseCode code();
    
    DnsResponse setCode(final DnsResponseCode p0);
    
    DnsResponse setId(final int p0);
    
    DnsResponse setOpCode(final DnsOpCode p0);
    
    DnsResponse setRecursionDesired(final boolean p0);
    
    DnsResponse setZ(final int p0);
    
    DnsResponse setRecord(final DnsSection p0, final DnsRecord p1);
    
    DnsResponse addRecord(final DnsSection p0, final DnsRecord p1);
    
    DnsResponse addRecord(final DnsSection p0, final int p1, final DnsRecord p2);
    
    DnsResponse clear(final DnsSection p0);
    
    DnsResponse clear();
    
    DnsResponse touch();
    
    DnsResponse touch(final Object p0);
    
    DnsResponse retain();
    
    DnsResponse retain(final int p0);
}
