// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.unix;

import java.net.InetSocketAddress;

public final class DatagramSocketAddress extends InetSocketAddress
{
    private static final long serialVersionUID = 3094819287843178401L;
    private final int receivedAmount;
    
    DatagramSocketAddress(final String addr, final int port, final int receivedAmount) {
        super(addr, port);
        this.receivedAmount = receivedAmount;
    }
    
    public int receivedAmount() {
        return this.receivedAmount;
    }
}
