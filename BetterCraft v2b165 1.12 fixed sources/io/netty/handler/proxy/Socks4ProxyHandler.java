// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.proxy;

import io.netty.handler.codec.socksx.v4.Socks4CommandStatus;
import io.netty.handler.codec.socksx.v4.Socks4CommandResponse;
import io.netty.handler.codec.socksx.v4.DefaultSocks4CommandRequest;
import io.netty.handler.codec.socksx.v4.Socks4CommandType;
import java.net.InetSocketAddress;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.socksx.v4.Socks4ClientEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.socksx.v4.Socks4ClientDecoder;
import io.netty.channel.ChannelHandlerContext;
import java.net.SocketAddress;

public final class Socks4ProxyHandler extends ProxyHandler
{
    private static final String PROTOCOL = "socks4";
    private static final String AUTH_USERNAME = "username";
    private final String username;
    private String decoderName;
    private String encoderName;
    
    public Socks4ProxyHandler(final SocketAddress proxyAddress) {
        this(proxyAddress, null);
    }
    
    public Socks4ProxyHandler(final SocketAddress proxyAddress, String username) {
        super(proxyAddress);
        if (username != null && username.isEmpty()) {
            username = null;
        }
        this.username = username;
    }
    
    @Override
    public String protocol() {
        return "socks4";
    }
    
    @Override
    public String authScheme() {
        return (this.username != null) ? "username" : "none";
    }
    
    public String username() {
        return this.username;
    }
    
    @Override
    protected void addCodec(final ChannelHandlerContext ctx) throws Exception {
        final ChannelPipeline p = ctx.pipeline();
        final String name = ctx.name();
        final Socks4ClientDecoder decoder = new Socks4ClientDecoder();
        p.addBefore(name, null, decoder);
        this.decoderName = p.context(decoder).name();
        p.addBefore(name, this.encoderName = this.decoderName + ".encoder", Socks4ClientEncoder.INSTANCE);
    }
    
    @Override
    protected void removeEncoder(final ChannelHandlerContext ctx) throws Exception {
        final ChannelPipeline p = ctx.pipeline();
        p.remove(this.encoderName);
    }
    
    @Override
    protected void removeDecoder(final ChannelHandlerContext ctx) throws Exception {
        final ChannelPipeline p = ctx.pipeline();
        p.remove(this.decoderName);
    }
    
    @Override
    protected Object newInitialMessage(final ChannelHandlerContext ctx) throws Exception {
        final InetSocketAddress raddr = this.destinationAddress();
        String rhost;
        if (raddr.isUnresolved()) {
            rhost = raddr.getHostString();
        }
        else {
            rhost = raddr.getAddress().getHostAddress();
        }
        return new DefaultSocks4CommandRequest(Socks4CommandType.CONNECT, rhost, raddr.getPort(), (this.username != null) ? this.username : "");
    }
    
    @Override
    protected boolean handleResponse(final ChannelHandlerContext ctx, final Object response) throws Exception {
        final Socks4CommandResponse res = (Socks4CommandResponse)response;
        final Socks4CommandStatus status = res.status();
        if (status == Socks4CommandStatus.SUCCESS) {
            return true;
        }
        throw new ProxyConnectException(this.exceptionMessage("status: " + status));
    }
}
