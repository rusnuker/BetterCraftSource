// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver.dns;

import java.util.Random;
import io.netty.util.internal.PlatformDependent;
import java.net.InetSocketAddress;

final class ShuffledDnsServerAddressStream implements DnsServerAddressStream
{
    private final InetSocketAddress[] addresses;
    private int i;
    
    ShuffledDnsServerAddressStream(final InetSocketAddress[] addresses) {
        this.addresses = addresses.clone();
        this.shuffle();
    }
    
    private void shuffle() {
        final InetSocketAddress[] addresses = this.addresses;
        final Random r = PlatformDependent.threadLocalRandom();
        for (int i = addresses.length - 1; i >= 0; --i) {
            final InetSocketAddress tmp = addresses[i];
            final int j = r.nextInt(i + 1);
            addresses[i] = addresses[j];
            addresses[j] = tmp;
        }
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
            this.shuffle();
        }
        return next;
    }
    
    @Override
    public String toString() {
        return SequentialDnsServerAddressStream.toString("shuffled", this.i, this.addresses);
    }
}
