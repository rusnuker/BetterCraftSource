// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver.dns;

import java.net.UnknownHostException;
import java.net.Inet6Address;
import java.net.InetAddress;
import io.netty.util.NetUtil;
import java.net.Inet4Address;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.internal.PlatformDependent;
import java.util.HashMap;
import io.netty.util.collection.IntObjectMap;
import java.net.InetSocketAddress;
import java.util.Map;

final class DnsQueryContextManager
{
    final Map<InetSocketAddress, IntObjectMap<DnsQueryContext>> map;
    
    DnsQueryContextManager() {
        this.map = new HashMap<InetSocketAddress, IntObjectMap<DnsQueryContext>>();
    }
    
    int add(final DnsQueryContext qCtx) {
        final IntObjectMap<DnsQueryContext> contexts = this.getOrCreateContextMap(qCtx.nameServerAddr());
        int id = PlatformDependent.threadLocalRandom().nextInt(65535) + 1;
        final int maxTries = 131070;
        int tries = 0;
        synchronized (contexts) {
            while (contexts.containsKey(id)) {
                id = (id + 1 & 0xFFFF);
                if (++tries >= 131070) {
                    throw new IllegalStateException("query ID space exhausted: " + qCtx.question());
                }
            }
            contexts.put(id, qCtx);
            return id;
        }
    }
    
    DnsQueryContext get(final InetSocketAddress nameServerAddr, final int id) {
        final IntObjectMap<DnsQueryContext> contexts = this.getContextMap(nameServerAddr);
        DnsQueryContext qCtx;
        if (contexts != null) {
            synchronized (contexts) {
                qCtx = contexts.get(id);
            }
        }
        else {
            qCtx = null;
        }
        return qCtx;
    }
    
    DnsQueryContext remove(final InetSocketAddress nameServerAddr, final int id) {
        final IntObjectMap<DnsQueryContext> contexts = this.getContextMap(nameServerAddr);
        if (contexts == null) {
            return null;
        }
        synchronized (contexts) {
            return contexts.remove(id);
        }
    }
    
    private IntObjectMap<DnsQueryContext> getContextMap(final InetSocketAddress nameServerAddr) {
        synchronized (this.map) {
            return this.map.get(nameServerAddr);
        }
    }
    
    private IntObjectMap<DnsQueryContext> getOrCreateContextMap(final InetSocketAddress nameServerAddr) {
        synchronized (this.map) {
            final IntObjectMap<DnsQueryContext> contexts = this.map.get(nameServerAddr);
            if (contexts != null) {
                return contexts;
            }
            final IntObjectMap<DnsQueryContext> newContexts = new IntObjectHashMap<DnsQueryContext>();
            final InetAddress a = nameServerAddr.getAddress();
            final int port = nameServerAddr.getPort();
            this.map.put(nameServerAddr, newContexts);
            if (a instanceof Inet4Address) {
                final Inet4Address a2 = (Inet4Address)a;
                if (a2.isLoopbackAddress()) {
                    this.map.put(new InetSocketAddress(NetUtil.LOCALHOST6, port), newContexts);
                }
                else {
                    this.map.put(new InetSocketAddress(toCompatAddress(a2), port), newContexts);
                }
            }
            else if (a instanceof Inet6Address) {
                final Inet6Address a3 = (Inet6Address)a;
                if (a3.isLoopbackAddress()) {
                    this.map.put(new InetSocketAddress(NetUtil.LOCALHOST4, port), newContexts);
                }
                else if (a3.isIPv4CompatibleAddress()) {
                    this.map.put(new InetSocketAddress(toIPv4Address(a3), port), newContexts);
                }
            }
            return newContexts;
        }
    }
    
    private static Inet6Address toCompatAddress(final Inet4Address a4) {
        final byte[] b4 = a4.getAddress();
        final byte[] b5 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, b4[0], b4[1], b4[2], b4[3] };
        try {
            return (Inet6Address)InetAddress.getByAddress(b5);
        }
        catch (final UnknownHostException e) {
            throw new Error(e);
        }
    }
    
    private static Inet4Address toIPv4Address(final Inet6Address a6) {
        final byte[] b6 = a6.getAddress();
        final byte[] b7 = { b6[12], b6[13], b6[14], b6[15] };
        try {
            return (Inet4Address)InetAddress.getByAddress(b7);
        }
        catch (final UnknownHostException e) {
            throw new Error(e);
        }
    }
}
