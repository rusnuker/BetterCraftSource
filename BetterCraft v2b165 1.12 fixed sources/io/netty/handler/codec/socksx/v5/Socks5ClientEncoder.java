// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v5;

import io.netty.buffer.ByteBufUtil;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import io.netty.handler.codec.EncoderException;
import io.netty.util.internal.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.MessageToByteEncoder;

@ChannelHandler.Sharable
public class Socks5ClientEncoder extends MessageToByteEncoder<Socks5Message>
{
    public static final Socks5ClientEncoder DEFAULT;
    private final Socks5AddressEncoder addressEncoder;
    
    protected Socks5ClientEncoder() {
        this(Socks5AddressEncoder.DEFAULT);
    }
    
    public Socks5ClientEncoder(final Socks5AddressEncoder addressEncoder) {
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
        if (msg instanceof Socks5InitialRequest) {
            encodeAuthMethodRequest((Socks5InitialRequest)msg, out);
        }
        else if (msg instanceof Socks5PasswordAuthRequest) {
            encodePasswordAuthRequest((Socks5PasswordAuthRequest)msg, out);
        }
        else {
            if (!(msg instanceof Socks5CommandRequest)) {
                throw new EncoderException("unsupported message type: " + StringUtil.simpleClassName(msg));
            }
            this.encodeCommandRequest((Socks5CommandRequest)msg, out);
        }
    }
    
    private static void encodeAuthMethodRequest(final Socks5InitialRequest msg, final ByteBuf out) {
        out.writeByte(msg.version().byteValue());
        final List<Socks5AuthMethod> authMethods = msg.authMethods();
        final int numAuthMethods = authMethods.size();
        out.writeByte(numAuthMethods);
        if (authMethods instanceof RandomAccess) {
            for (int i = 0; i < numAuthMethods; ++i) {
                out.writeByte(authMethods.get(i).byteValue());
            }
        }
        else {
            for (final Socks5AuthMethod a : authMethods) {
                out.writeByte(a.byteValue());
            }
        }
    }
    
    private static void encodePasswordAuthRequest(final Socks5PasswordAuthRequest msg, final ByteBuf out) {
        out.writeByte(1);
        final String username = msg.username();
        out.writeByte(username.length());
        ByteBufUtil.writeAscii(out, username);
        final String password = msg.password();
        out.writeByte(password.length());
        ByteBufUtil.writeAscii(out, password);
    }
    
    private void encodeCommandRequest(final Socks5CommandRequest msg, final ByteBuf out) throws Exception {
        out.writeByte(msg.version().byteValue());
        out.writeByte(msg.type().byteValue());
        out.writeByte(0);
        final Socks5AddressType dstAddrType = msg.dstAddrType();
        out.writeByte(dstAddrType.byteValue());
        this.addressEncoder.encodeAddress(dstAddrType, msg.dstAddr(), out);
        out.writeShort(msg.dstPort());
    }
    
    static {
        DEFAULT = new Socks5ClientEncoder();
    }
}
