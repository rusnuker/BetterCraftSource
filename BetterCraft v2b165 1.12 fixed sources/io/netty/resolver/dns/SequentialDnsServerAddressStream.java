// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver.dns;

import java.net.InetSocketAddress;

final class SequentialDnsServerAddressStream implements DnsServerAddressStream
{
    private final InetSocketAddress[] addresses;
    private int i;
    
    SequentialDnsServerAddressStream(final InetSocketAddress[] addresses, final int startIdx) {
        this.addresses = addresses;
        this.i = startIdx;
    }
    
    @Override
    public InetSocketAddress next() {
        int i = this.i;
        final InetSocketAddress next = this.addresses[i];
        if (++i < this.addresses.length) {
            this.i = i;
        }
        else {
            this.i = 0;
        }
        return next;
    }
    
    @Override
    public String toString() {
        return toString("sequential", this.i, this.addresses);
    }
    
    static String toString(final String type, final int index, final InetSocketAddress[] addresses) {
        final StringBuilder buf = new StringBuilder(type.length() + 2 + addresses.length * 16);
        buf.append(type).append("(index: ").append(index);
        buf.append(", addrs: (");
        for (final InetSocketAddress a : addresses) {
            buf.append(a).append(", ");
        }
        buf.setLength(buf.length() - 2);
        buf.append("))");
        return buf.toString();
    }
}
