// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v4;

import io.netty.buffer.ByteBufUtil;
import io.netty.util.NetUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.MessageToByteEncoder;

@ChannelHandler.Sharable
public final class Socks4ClientEncoder extends MessageToByteEncoder<Socks4CommandRequest>
{
    public static final Socks4ClientEncoder INSTANCE;
    private static final byte[] IPv4_DOMAIN_MARKER;
    
    private Socks4ClientEncoder() {
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final Socks4CommandRequest msg, final ByteBuf out) throws Exception {
        out.writeByte(msg.version().byteValue());
        out.writeByte(msg.type().byteValue());
        out.writeShort(msg.dstPort());
        if (NetUtil.isValidIpV4Address(msg.dstAddr())) {
            out.writeBytes(NetUtil.createByteArrayFromIpAddressString(msg.dstAddr()));
            ByteBufUtil.writeAscii(out, msg.userId());
            out.writeByte(0);
        }
        else {
            out.writeBytes(Socks4ClientEncoder.IPv4_DOMAIN_MARKER);
            ByteBufUtil.writeAscii(out, msg.userId());
            out.writeByte(0);
            ByteBufUtil.writeAscii(out, msg.dstAddr());
            out.writeByte(0);
        }
    }
    
    static {
        INSTANCE = new Socks4ClientEncoder();
        IPv4_DOMAIN_MARKER = new byte[] { 0, 0, 0, 1 };
    }
}
