// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver.dns;

import java.net.InetSocketAddress;

abstract class DefaultDnsServerAddresses extends DnsServerAddresses
{
    protected final InetSocketAddress[] addresses;
    private final String strVal;
    
    DefaultDnsServerAddresses(final String type, final InetSocketAddress[] addresses) {
        this.addresses = addresses;
        final StringBuilder buf = new StringBuilder(type.length() + 2 + addresses.length * 16);
        buf.append(type).append('(');
        for (final InetSocketAddress a : addresses) {
            buf.append(a).append(", ");
        }
        buf.setLength(buf.length() - 2);
        buf.append(')');
        this.strVal = buf.toString();
    }
    
    @Override
    public String toString() {
        return this.strVal;
    }
}
