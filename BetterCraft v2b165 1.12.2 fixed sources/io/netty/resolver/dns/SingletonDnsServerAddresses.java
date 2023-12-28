// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver.dns;

import java.net.InetSocketAddress;

final class SingletonDnsServerAddresses extends DnsServerAddresses
{
    private final InetSocketAddress address;
    private final String strVal;
    private final DnsServerAddressStream stream;
    
    SingletonDnsServerAddresses(final InetSocketAddress address) {
        this.stream = new DnsServerAddressStream() {
            @Override
            public InetSocketAddress next() {
                return SingletonDnsServerAddresses.this.address;
            }
            
            @Override
            public String toString() {
                return SingletonDnsServerAddresses.this.toString();
            }
        };
        this.address = address;
        this.strVal = new StringBuilder(32).append("singleton(").append(address).append(')').toString();
    }
    
    @Override
    public DnsServerAddressStream stream() {
        return this.stream;
    }
    
    @Override
    public String toString() {
        return this.strVal;
    }
}
