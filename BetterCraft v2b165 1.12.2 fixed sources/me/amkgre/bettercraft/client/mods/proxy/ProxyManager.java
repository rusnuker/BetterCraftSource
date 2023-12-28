// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.proxy;

import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class ProxyManager
{
    public static volatile Proxy proxy;
    
    public static Proxy getProxyFromString(final String proxy) {
        return new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxy.split(":")[0], Integer.valueOf(proxy.split(":")[1])));
    }
    
    public static void setProxy(final Proxy proxy) {
        ProxyManager.proxy = proxy;
    }
    
    public static Proxy getProxy() {
        return ProxyManager.proxy;
    }
}
