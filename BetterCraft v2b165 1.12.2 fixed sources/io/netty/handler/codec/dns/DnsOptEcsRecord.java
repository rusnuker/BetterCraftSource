// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

public interface DnsOptEcsRecord extends DnsOptPseudoRecord
{
    int sourcePrefixLength();
    
    int scopePrefixLength();
    
    byte[] address();
}
