// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver.dns;

import io.netty.channel.EventLoop;
import java.net.InetAddress;
import java.util.List;
import io.netty.handler.codec.dns.DnsRecord;

public interface DnsCache
{
    void clear();
    
    boolean clear(final String p0);
    
    List<DnsCacheEntry> get(final String p0, final DnsRecord[] p1);
    
    void cache(final String p0, final DnsRecord[] p1, final InetAddress p2, final long p3, final EventLoop p4);
    
    void cache(final String p0, final DnsRecord[] p1, final Throwable p2, final EventLoop p3);
}
