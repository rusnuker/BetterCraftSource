// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBuf;

public interface DnsRecordDecoder
{
    public static final DnsRecordDecoder DEFAULT = new DefaultDnsRecordDecoder();
    
    DnsQuestion decodeQuestion(final ByteBuf p0) throws Exception;
    
     <T extends DnsRecord> T decodeRecord(final ByteBuf p0) throws Exception;
}
