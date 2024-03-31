// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBuf;

public interface DnsRecordEncoder
{
    public static final DnsRecordEncoder DEFAULT = new DefaultDnsRecordEncoder();
    
    void encodeQuestion(final DnsQuestion p0, final ByteBuf p1) throws Exception;
    
    void encodeRecord(final DnsRecord p0, final ByteBuf p1) throws Exception;
}
