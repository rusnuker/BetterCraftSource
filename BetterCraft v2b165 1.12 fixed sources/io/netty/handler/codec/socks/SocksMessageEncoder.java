// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.MessageToByteEncoder;

@ChannelHandler.Sharable
public class SocksMessageEncoder extends MessageToByteEncoder<SocksMessage>
{
    @Override
    protected void encode(final ChannelHandlerContext ctx, final SocksMessage msg, final ByteBuf out) throws Exception {
        msg.encodeAsByteBuf(out);
    }
}
