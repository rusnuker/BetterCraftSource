// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver.dns;

public final class NoopDnsServerAddressStreamProvider implements DnsServerAddressStreamProvider
{
    public static final NoopDnsServerAddressStreamProvider INSTANCE;
    
    private NoopDnsServerAddressStreamProvider() {
    }
    
    @Override
    public DnsServerAddressStream nameServerAddressStream(final String hostname) {
        return null;
    }
    
    static {
        INSTANCE = new NoopDnsServerAddressStreamProvider();
    }
}
