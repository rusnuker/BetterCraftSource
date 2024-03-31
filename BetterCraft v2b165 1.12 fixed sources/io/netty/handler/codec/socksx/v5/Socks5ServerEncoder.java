// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v5;

import io.netty.handler.codec.EncoderException;
import io.netty.util.internal.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.MessageToByteEncoder;

@ChannelHandler.Sharable
public class Socks5ServerEncoder extends MessageToByteEncoder<Socks5Message>
{
    public static final Socks5ServerEncoder DEFAULT;
    private final Socks5AddressEncoder addressEncoder;
    
    protected Socks5ServerEncoder() {
        this(Socks5AddressEncoder.DEFAULT);
    }
    
    public Socks5ServerEncoder(final Socks5AddressEncoder addressEncoder) {
        if (addressEncoder == null) {
            throw new NullPointerException("addressEncoder");
        }
        this.addressEncoder = addressEncoder;
    }
    
    protected final Socks5AddressEncoder addressEncoder() {
        return this.addressEncoder;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final Socks5Message msg, final ByteBuf out) throws Exception {
        if (msg instanceof Socks5InitialResponse) {
            encodeAuthMethodResponse((Socks5InitialResponse)msg, out);
        }
        else if (msg instanceof Socks5PasswordAuthResponse) {
            encodePasswordAuthResponse((Socks5PasswordAuthResponse)msg, out);
        }
        else {
            if (!(msg instanceof Socks5CommandResponse)) {
                throw new EncoderException("unsupported message type: " + StringUtil.simpleClassName(msg));
            }
            this.encodeCommandResponse((Socks5CommandResponse)msg, out);
        }
    }
    
    private static void encodeAuthMethodResponse(final Socks5InitialResponse msg, final ByteBuf out) {
        out.writeByte(msg.version().byteValue());
        out.writeByte(msg.authMethod().byteValue());
    }
    
    private static void encodePasswordAuthResponse(final Socks5PasswordAuthResponse msg, final ByteBuf out) {
        out.writeByte(1);
        out.writeByte(msg.status().byteValue());
    }
    
    private void encodeCommandResponse(final Socks5CommandResponse msg, final ByteBuf out) throws Exception {
        out.writeByte(msg.version().byteValue());
        out.writeByte(msg.status().byteValue());
        out.writeByte(0);
        final Socks5AddressType bndAddrType = msg.bndAddrType();
        out.writeByte(bndAddrType.byteValue());
        this.addressEncoder.encodeAddress(bndAddrType, msg.bndAddr(), out);
        out.writeShort(msg.bndPort());
    }
    
    static {
        DEFAULT = new Socks5ServerEncoder(Socks5AddressEncoder.DEFAULT);
    }
}
