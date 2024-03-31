// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.proxy;

import java.util.Arrays;
import io.netty.handler.codec.socksx.v5.DefaultSocks5InitialRequest;
import java.util.Collections;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandRequest;
import io.netty.handler.codec.socksx.v5.Socks5CommandType;
import io.netty.handler.codec.socksx.v5.Socks5CommandResponseDecoder;
import io.netty.util.internal.StringUtil;
import io.netty.util.NetUtil;
import io.netty.handler.codec.socksx.v5.Socks5AddressType;
import java.net.InetSocketAddress;
import io.netty.handler.codec.socksx.v5.Socks5CommandStatus;
import io.netty.handler.codec.socksx.v5.Socks5CommandResponse;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthStatus;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthResponse;
import io.netty.handler.codec.socksx.v5.DefaultSocks5PasswordAuthRequest;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthResponseDecoder;
import io.netty.handler.codec.socksx.v5.Socks5InitialResponse;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.socksx.v5.Socks5ClientEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.socksx.v5.Socks5InitialResponseDecoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.socksx.v5.Socks5AuthMethod;
import java.net.SocketAddress;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequest;

public final class Socks5ProxyHandler extends ProxyHandler
{
    private static final String PROTOCOL = "socks5";
    private static final String AUTH_PASSWORD = "password";
    private static final Socks5InitialRequest INIT_REQUEST_NO_AUTH;
    private static final Socks5InitialRequest INIT_REQUEST_PASSWORD;
    private final String username;
    private final String password;
    private String decoderName;
    private String encoderName;
    
    public Socks5ProxyHandler(final SocketAddress proxyAddress) {
        this(proxyAddress, null, null);
    }
    
    public Socks5ProxyHandler(final SocketAddress proxyAddress, String username, String password) {
        super(proxyAddress);
        if (username != null && username.isEmpty()) {
            username = null;
        }
        if (password != null && password.isEmpty()) {
            password = null;
        }
        this.username = username;
        this.password = password;
    }
    
    @Override
    public String protocol() {
        return "socks5";
    }
    
    @Override
    public String authScheme() {
        return (this.socksAuthMethod() == Socks5AuthMethod.PASSWORD) ? "password" : "none";
    }
    
    public String username() {
        return this.username;
    }
    
    public String password() {
        return this.password;
    }
    
    @Override
    protected void addCodec(final ChannelHandlerContext ctx) throws Exception {
        final ChannelPipeline p = ctx.pipeline();
        final String name = ctx.name();
        final Socks5InitialResponseDecoder decoder = new Socks5InitialResponseDecoder();
        p.addBefore(name, null, decoder);
        this.decoderName = p.context(decoder).name();
        p.addBefore(name, this.encoderName = this.decoderName + ".encoder", Socks5ClientEncoder.DEFAULT);
    }
    
    @Override
    protected void removeEncoder(final ChannelHandlerContext ctx) throws Exception {
        ctx.pipeline().remove(this.encoderName);
    }
    
    @Override
    protected void removeDecoder(final ChannelHandlerContext ctx) throws Exception {
        final ChannelPipeline p = ctx.pipeline();
        if (p.context(this.decoderName) != null) {
            p.remove(this.decoderName);
        }
    }
    
    @Override
    protected Object newInitialMessage(final ChannelHandlerContext ctx) throws Exception {
        return (this.socksAuthMethod() == Socks5AuthMethod.PASSWORD) ? Socks5ProxyHandler.INIT_REQUEST_PASSWORD : Socks5ProxyHandler.INIT_REQUEST_NO_AUTH;
    }
    
    @Override
    protected boolean handleResponse(final ChannelHandlerContext ctx, final Object response) throws Exception {
        if (response instanceof Socks5InitialResponse) {
            final Socks5InitialResponse res = (Socks5InitialResponse)response;
            final Socks5AuthMethod authMethod = this.socksAuthMethod();
            if (res.authMethod() != Socks5AuthMethod.NO_AUTH && res.authMethod() != authMethod) {
                throw new ProxyConnectException(this.exceptionMessage("unexpected authMethod: " + res.authMethod()));
            }
            if (authMethod == Socks5AuthMethod.NO_AUTH) {
                this.sendConnectCommand(ctx);
            }
            else {
                if (authMethod != Socks5AuthMethod.PASSWORD) {
                    throw new Error();
                }
                ctx.pipeline().replace(this.decoderName, this.decoderName, new Socks5PasswordAuthResponseDecoder());
                this.sendToProxyServer(new DefaultSocks5PasswordAuthRequest((this.username != null) ? this.username : "", (this.password != null) ? this.password : ""));
            }
            return false;
        }
        else if (response instanceof Socks5PasswordAuthResponse) {
            final Socks5PasswordAuthResponse res2 = (Socks5PasswordAuthResponse)response;
            if (res2.status() != Socks5PasswordAuthStatus.SUCCESS) {
                throw new ProxyConnectException(this.exceptionMessage("authStatus: " + res2.status()));
            }
            this.sendConnectCommand(ctx);
            return false;
        }
        else {
            final Socks5CommandResponse res3 = (Socks5CommandResponse)response;
            if (res3.status() != Socks5CommandStatus.SUCCESS) {
                throw new ProxyConnectException(this.exceptionMessage("status: " + res3.status()));
            }
            return true;
        }
    }
    
    private Socks5AuthMethod socksAuthMethod() {
        Socks5AuthMethod authMethod;
        if (this.username == null && this.password == null) {
            authMethod = Socks5AuthMethod.NO_AUTH;
        }
        else {
            authMethod = Socks5AuthMethod.PASSWORD;
        }
        return authMethod;
    }
    
    private void sendConnectCommand(final ChannelHandlerContext ctx) throws Exception {
        final InetSocketAddress raddr = this.destinationAddress();
        Socks5AddressType addrType;
        String rhost;
        if (raddr.isUnresolved()) {
            addrType = Socks5AddressType.DOMAIN;
            rhost = raddr.getHostString();
        }
        else {
            rhost = raddr.getAddress().getHostAddress();
            if (NetUtil.isValidIpV4Address(rhost)) {
                addrType = Socks5AddressType.IPv4;
            }
            else {
                if (!NetUtil.isValidIpV6Address(rhost)) {
                    throw new ProxyConnectException(this.exceptionMessage("unknown address type: " + StringUtil.simpleClassName(rhost)));
                }
                addrType = Socks5AddressType.IPv6;
            }
        }
        ctx.pipeline().replace(this.decoderName, this.decoderName, new Socks5CommandResponseDecoder());
        this.sendToProxyServer(new DefaultSocks5CommandRequest(Socks5CommandType.CONNECT, addrType, rhost, raddr.getPort()));
    }
    
    static {
        INIT_REQUEST_NO_AUTH = new DefaultSocks5InitialRequest(Collections.singletonList(Socks5AuthMethod.NO_AUTH));
        INIT_REQUEST_PASSWORD = new DefaultSocks5InitialRequest(Arrays.asList(Socks5AuthMethod.NO_AUTH, Socks5AuthMethod.PASSWORD));
    }
}
