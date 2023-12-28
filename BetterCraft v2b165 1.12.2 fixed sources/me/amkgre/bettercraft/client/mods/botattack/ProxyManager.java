// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.botattack;

import java.io.InputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.net.Proxy;
import java.util.List;

public class ProxyManager
{
    private final List<Proxy> proxys;
    private final Map<Proxy, SocksType> sockTypes;
    private volatile AtomicInteger currentPosition;
    
    public ProxyManager() {
        this.proxys = new ArrayList<Proxy>();
        this.sockTypes = new HashMap<Proxy, SocksType>();
        this.currentPosition = new AtomicInteger(0);
        final InputStream in = this.getClass().getResourceAsStream("/me/amkgre/bettercraft/client/mods/botattack/socks4_proxies.txt");
        final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        reader.lines().forEach(str -> {
            final String[] arr = str.split(":");
            final String host = arr[0];
            final int port = Integer.parseInt(arr[1]);
            new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, port));
            final Proxy proxy2;
            final Proxy proxy = proxy2;
            this.proxys.add(proxy);
            this.sockTypes.put(proxy, SocksType.SOCKS4);
            return;
        });
        try {
            reader.close();
        }
        catch (final IOException ex) {}
    }
    
    public List<Proxy> getProxys() {
        return this.proxys;
    }
    
    public synchronized int getCurrentPosition() {
        return this.currentPosition.get();
    }
    
    public synchronized Proxy nextProxy() {
        final Proxy proxy = this.proxys.get(this.currentPosition.getAndIncrement());
        if (this.currentPosition.get() >= this.proxys.size()) {
            this.currentPosition.set(0);
        }
        return proxy;
    }
    
    public SocksType getSocksType(final Proxy proxy) {
        return this.sockTypes.get(proxy);
    }
    
    public static String proxyToString(final Proxy proxy) {
        return "Proxy[host=" + ((InetSocketAddress)proxy.address()).getAddress().getHostAddress() + ";port=" + ((InetSocketAddress)proxy.address()).getPort() + "]";
    }
    
    public enum SocksType
    {
        SOCKS4("SOCKS4", 0), 
        SOCKS5("SOCKS5", 1);
        
        private SocksType(final String s, final int n) {
        }
    }
}
