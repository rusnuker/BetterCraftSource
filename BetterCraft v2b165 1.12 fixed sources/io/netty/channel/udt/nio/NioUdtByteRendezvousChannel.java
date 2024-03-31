// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.udt.nio;

import com.barchart.udt.nio.SocketChannelUDT;
import com.barchart.udt.TypeUDT;

@Deprecated
public class NioUdtByteRendezvousChannel extends NioUdtByteConnectorChannel
{
    public NioUdtByteRendezvousChannel() {
        super((SocketChannelUDT)NioUdtProvider.newRendezvousChannelUDT(TypeUDT.STREAM));
    }
}
