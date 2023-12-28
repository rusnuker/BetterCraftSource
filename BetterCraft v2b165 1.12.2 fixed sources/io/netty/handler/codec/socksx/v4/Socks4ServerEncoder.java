// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v4;

import io.netty.util.NetUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.MessageToByteEncoder;

@ChannelHandler.Sharable
public final class Socks4ServerEncoder extends MessageToByteEncoder<Socks4CommandResponse>
{
    public static final Socks4ServerEncoder INSTANCE;
    private static final byte[] IPv4_HOSTNAME_ZEROED;
    
    private Socks4ServerEncoder() {
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final Socks4CommandResponse msg, final ByteBuf out) throws Exception {
        out.writeByte(0);
        out.writeByte(msg.status().byteValue());
        out.writeShort(msg.dstPort());
        out.writeBytes((msg.dstAddr() == null) ? Socks4ServerEncoder.IPv4_HOSTNAME_ZEROED : NetUtil.createByteArrayFromIpAddressString(msg.dstAddr()));
    }
    
    static {
        INSTANCE = new Socks4ServerEncoder();
        IPv4_HOSTNAME_ZEROED = new byte[] { 0, 0, 0, 0 };
    }
}
