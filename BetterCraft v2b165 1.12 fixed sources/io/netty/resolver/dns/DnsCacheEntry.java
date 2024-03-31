// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver.dns;

import java.util.concurrent.TimeUnit;
import io.netty.channel.EventLoop;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.concurrent.ScheduledFuture;
import java.net.InetAddress;

public final class DnsCacheEntry
{
    private final String hostname;
    private final InetAddress address;
    private final Throwable cause;
    private volatile ScheduledFuture<?> expirationFuture;
    
    public DnsCacheEntry(final String hostname, final InetAddress address) {
        this.hostname = ObjectUtil.checkNotNull(hostname, "hostname");
        this.address = ObjectUtil.checkNotNull(address, "address");
        this.cause = null;
    }
    
    public DnsCacheEntry(final String hostname, final Throwable cause) {
        this.hostname = ObjectUtil.checkNotNull(hostname, "hostname");
        this.cause = ObjectUtil.checkNotNull(cause, "cause");
        this.address = null;
    }
    
    public String hostname() {
        return this.hostname;
    }
    
    public InetAddress address() {
        return this.address;
    }
    
    public Throwable cause() {
        return this.cause;
    }
    
    void scheduleExpiration(final EventLoop loop, final Runnable task, final long delay, final TimeUnit unit) {
        assert this.expirationFuture == null : "expiration task scheduled already";
        this.expirationFuture = loop.schedule(task, delay, unit);
    }
    
    void cancelExpiration() {
        final ScheduledFuture<?> expirationFuture = this.expirationFuture;
        if (expirationFuture != null) {
            expirationFuture.cancel(false);
        }
    }
    
    @Override
    public String toString() {
        if (this.cause != null) {
            return this.hostname + '/' + this.cause;
        }
        return this.address.toString();
    }
}
