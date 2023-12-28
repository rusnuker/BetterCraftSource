// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface DnsRawRecord extends DnsRecord, ByteBufHolder
{
    DnsRawRecord copy();
    
    DnsRawRecord duplicate();
    
    DnsRawRecord retainedDuplicate();
    
    DnsRawRecord replace(final ByteBuf p0);
    
    DnsRawRecord retain();
    
    DnsRawRecord retain(final int p0);
    
    DnsRawRecord touch();
    
    DnsRawRecord touch(final Object p0);
}
