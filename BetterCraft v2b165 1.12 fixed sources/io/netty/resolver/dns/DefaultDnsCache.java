// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver.dns;

import java.util.concurrent.TimeUnit;
import io.netty.channel.EventLoop;
import java.net.InetAddress;
import java.util.ArrayList;
import io.netty.handler.codec.dns.DnsRecord;
import java.util.Iterator;
import java.util.Map;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class DefaultDnsCache implements DnsCache
{
    private final ConcurrentMap<String, List<DnsCacheEntry>> resolveCache;
    private final int minTtl;
    private final int maxTtl;
    private final int negativeTtl;
    
    public DefaultDnsCache() {
        this(0, Integer.MAX_VALUE, 0);
    }
    
    public DefaultDnsCache(final int minTtl, final int maxTtl, final int negativeTtl) {
        this.resolveCache = PlatformDependent.newConcurrentHashMap();
        this.minTtl = ObjectUtil.checkPositiveOrZero(minTtl, "minTtl");
        this.maxTtl = ObjectUtil.checkPositiveOrZero(maxTtl, "maxTtl");
        if (minTtl > maxTtl) {
            throw new IllegalArgumentException("minTtl: " + minTtl + ", maxTtl: " + maxTtl + " (expected: 0 <= minTtl <= maxTtl)");
        }
        this.negativeTtl = ObjectUtil.checkPositiveOrZero(negativeTtl, "negativeTtl");
    }
    
    public int minTtl() {
        return this.minTtl;
    }
    
    public int maxTtl() {
        return this.maxTtl;
    }
    
    public int negativeTtl() {
        return this.negativeTtl;
    }
    
    @Override
    public void clear() {
        final Iterator<Map.Entry<String, List<DnsCacheEntry>>> i = (Iterator<Map.Entry<String, List<DnsCacheEntry>>>)this.resolveCache.entrySet().iterator();
        while (i.hasNext()) {
            final Map.Entry<String, List<DnsCacheEntry>> e = i.next();
            i.remove();
            cancelExpiration(e.getValue());
        }
    }
    
    @Override
    public boolean clear(final String hostname) {
        ObjectUtil.checkNotNull(hostname, "hostname");
        boolean removed = false;
        final Iterator<Map.Entry<String, List<DnsCacheEntry>>> i = (Iterator<Map.Entry<String, List<DnsCacheEntry>>>)this.resolveCache.entrySet().iterator();
        while (i.hasNext()) {
            final Map.Entry<String, List<DnsCacheEntry>> e = i.next();
            if (e.getKey().equals(hostname)) {
                i.remove();
                cancelExpiration(e.getValue());
                removed = true;
            }
        }
        return removed;
    }
    
    private static boolean emptyAdditionals(final DnsRecord[] additionals) {
        return additionals == null || additionals.length == 0;
    }
    
    @Override
    public List<DnsCacheEntry> get(final String hostname, final DnsRecord[] additionals) {
        ObjectUtil.checkNotNull(hostname, "hostname");
        if (!emptyAdditionals(additionals)) {
            return null;
        }
        return this.resolveCache.get(hostname);
    }
    
    private List<DnsCacheEntry> cachedEntries(final String hostname) {
        List<DnsCacheEntry> oldEntries = this.resolveCache.get(hostname);
        List<DnsCacheEntry> entries;
        if (oldEntries == null) {
            final List<DnsCacheEntry> newEntries = new ArrayList<DnsCacheEntry>(8);
            oldEntries = this.resolveCache.putIfAbsent(hostname, newEntries);
            entries = ((oldEntries != null) ? oldEntries : newEntries);
        }
        else {
            entries = oldEntries;
        }
        return entries;
    }
    
    @Override
    public void cache(final String hostname, final DnsRecord[] additionals, final InetAddress address, final long originalTtl, final EventLoop loop) {
        ObjectUtil.checkNotNull(hostname, "hostname");
        ObjectUtil.checkNotNull(address, "address");
        ObjectUtil.checkNotNull(loop, "loop");
        if (this.maxTtl == 0 || !emptyAdditionals(additionals)) {
            return;
        }
        final int ttl = Math.max(this.minTtl, (int)Math.min(this.maxTtl, originalTtl));
        final List<DnsCacheEntry> entries = this.cachedEntries(hostname);
        final DnsCacheEntry e = new DnsCacheEntry(hostname, address);
        synchronized (entries) {
            if (!entries.isEmpty()) {
                final DnsCacheEntry firstEntry = entries.get(0);
                if (firstEntry.cause() != null) {
                    assert entries.size() == 1;
                    firstEntry.cancelExpiration();
                    entries.clear();
                }
            }
            entries.add(e);
        }
        this.scheduleCacheExpiration(entries, e, ttl, loop);
    }
    
    @Override
    public void cache(final String hostname, final DnsRecord[] additionals, final Throwable cause, final EventLoop loop) {
        ObjectUtil.checkNotNull(hostname, "hostname");
        ObjectUtil.checkNotNull(cause, "cause");
        ObjectUtil.checkNotNull(loop, "loop");
        if (this.negativeTtl == 0 || !emptyAdditionals(additionals)) {
            return;
        }
        final List<DnsCacheEntry> entries = this.cachedEntries(hostname);
        final DnsCacheEntry e = new DnsCacheEntry(hostname, cause);
        synchronized (entries) {
            for (int numEntries = entries.size(), i = 0; i < numEntries; ++i) {
                entries.get(i).cancelExpiration();
            }
            entries.clear();
            entries.add(e);
        }
        this.scheduleCacheExpiration(entries, e, this.negativeTtl, loop);
    }
    
    private static void cancelExpiration(final List<DnsCacheEntry> entries) {
        for (int numEntries = entries.size(), i = 0; i < numEntries; ++i) {
            entries.get(i).cancelExpiration();
        }
    }
    
    private void scheduleCacheExpiration(final List<DnsCacheEntry> entries, final DnsCacheEntry e, final int ttl, final EventLoop loop) {
        e.scheduleExpiration(loop, new Runnable() {
            @Override
            public void run() {
                synchronized (entries) {
                    entries.remove(e);
                    if (entries.isEmpty()) {
                        DefaultDnsCache.this.resolveCache.remove(e.hostname());
                    }
                }
            }
        }, ttl, TimeUnit.SECONDS);
    }
    
    @Override
    public String toString() {
        return "DefaultDnsCache(minTtl=" + this.minTtl + ", maxTtl=" + this.maxTtl + ", negativeTtl=" + this.negativeTtl + ", cached resolved hostname=" + this.resolveCache.size() + ")";
    }
}
