// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.udt.nio;

import com.barchart.udt.nio.SocketChannelUDT;
import com.barchart.udt.TypeUDT;

public class NioUdtMessageRendezvousChannel extends NioUdtMessageConnectorChannel
{
    public NioUdtMessageRendezvousChannel() {
        super((SocketChannelUDT)NioUdtProvider.newRendezvousChannelUDT(TypeUDT.DATAGRAM));
    }
}
