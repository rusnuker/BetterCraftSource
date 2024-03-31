// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.sctp;

import io.netty.channel.sctp.SctpMessage;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToMessageEncoder;

public class SctpOutboundByteStreamHandler extends MessageToMessageEncoder<ByteBuf>
{
    private final int streamIdentifier;
    private final int protocolIdentifier;
    private final boolean unordered;
    
    public SctpOutboundByteStreamHandler(final int streamIdentifier, final int protocolIdentifier) {
        this(streamIdentifier, protocolIdentifier, false);
    }
    
    public SctpOutboundByteStreamHandler(final int streamIdentifier, final int protocolIdentifier, final boolean unordered) {
        this.streamIdentifier = streamIdentifier;
        this.protocolIdentifier = protocolIdentifier;
        this.unordered = unordered;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf msg, final List<Object> out) throws Exception {
        out.add(new SctpMessage(this.streamIdentifier, this.protocolIdentifier, this.unordered, msg.retain()));
    }
}
