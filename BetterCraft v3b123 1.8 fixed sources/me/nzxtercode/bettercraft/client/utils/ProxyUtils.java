// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.utils;

import io.netty.channel.Channel;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import io.netty.channel.socket.oio.OioSocketChannel;
import io.netty.bootstrap.ChannelFactory;

public class ProxyUtils implements ChannelFactory<OioSocketChannel>
{
    public static Proxy proxy;
    
    public ProxyUtils(final Proxy proxy) {
        setProxy(proxy);
    }
    
    public static void setProxy(final Proxy proxy) {
        ProxyUtils.proxy = proxy;
    }
    
    public static Proxy getProxy() {
        return ProxyUtils.proxy;
    }
    
    public static Proxy getProxyFromString(final String proxy) {
        return new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxy.split(":")[0], Integer.valueOf(proxy.split(":")[1])));
    }
    
    @Override
    public OioSocketChannel newChannel() {
        if (ProxyUtils.proxy == null || ProxyUtils.proxy == Proxy.NO_PROXY) {
            return new OioSocketChannel(new Socket(Proxy.NO_PROXY));
        }
        final Socket sock = new Socket(ProxyUtils.proxy);
        try {
            Method m = sock.getClass().getDeclaredMethod("getImpl", (Class<?>[])new Class[0]);
            m.setAccessible(true);
            final Object sd = m.invoke(sock, new Object[0]);
            m = sd.getClass().getDeclaredMethod("setV4", (Class<?>[])new Class[0]);
            m.setAccessible(true);
            m.invoke(sd, new Object[0]);
            return new OioSocketChannel(sock);
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
