// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import io.netty.util.ReferenceCounted;

public interface DnsMessage extends ReferenceCounted
{
    int id();
    
    DnsMessage setId(final int p0);
    
    DnsOpCode opCode();
    
    DnsMessage setOpCode(final DnsOpCode p0);
    
    boolean isRecursionDesired();
    
    DnsMessage setRecursionDesired(final boolean p0);
    
    int z();
    
    DnsMessage setZ(final int p0);
    
    int count(final DnsSection p0);
    
    int count();
    
     <T extends DnsRecord> T recordAt(final DnsSection p0);
    
     <T extends DnsRecord> T recordAt(final DnsSection p0, final int p1);
    
    DnsMessage setRecord(final DnsSection p0, final DnsRecord p1);
    
     <T extends DnsRecord> T setRecord(final DnsSection p0, final int p1, final DnsRecord p2);
    
    DnsMessage addRecord(final DnsSection p0, final DnsRecord p1);
    
    DnsMessage addRecord(final DnsSection p0, final int p1, final DnsRecord p2);
    
     <T extends DnsRecord> T removeRecord(final DnsSection p0, final int p1);
    
    DnsMessage clear(final DnsSection p0);
    
    DnsMessage clear();
    
    DnsMessage touch();
    
    DnsMessage touch(final Object p0);
    
    DnsMessage retain();
    
    DnsMessage retain(final int p0);
}
