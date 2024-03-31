// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

public interface DnsOptPseudoRecord extends DnsRecord
{
    int extendedRcode();
    
    int version();
    
    int flags();
}
