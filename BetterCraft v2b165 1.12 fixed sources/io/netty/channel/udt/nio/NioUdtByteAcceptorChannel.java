// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.udt.nio;

import io.netty.channel.Channel;
import io.netty.channel.udt.UdtChannel;
import com.barchart.udt.nio.SocketChannelUDT;
import com.barchart.udt.TypeUDT;

@Deprecated
public class NioUdtByteAcceptorChannel extends NioUdtAcceptorChannel
{
    public NioUdtByteAcceptorChannel() {
        super(TypeUDT.STREAM);
    }
    
    @Override
    protected UdtChannel newConnectorChannel(final SocketChannelUDT channelUDT) {
        return new NioUdtByteConnectorChannel(this, channelUDT);
    }
}
