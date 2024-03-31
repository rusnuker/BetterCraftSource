// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver;

import java.util.Locale;
import java.net.InetAddress;
import java.net.Inet6Address;
import java.net.Inet4Address;
import java.util.Map;

public final class DefaultHostsFileEntriesResolver implements HostsFileEntriesResolver
{
    private final Map<String, Inet4Address> inet4Entries;
    private final Map<String, Inet6Address> inet6Entries;
    
    public DefaultHostsFileEntriesResolver() {
        this(HostsFileParser.parseSilently());
    }
    
    DefaultHostsFileEntriesResolver(final HostsFileEntries entries) {
        this.inet4Entries = entries.inet4Entries();
        this.inet6Entries = entries.inet6Entries();
    }
    
    @Override
    public InetAddress address(final String inetHost, final ResolvedAddressTypes resolvedAddressTypes) {
        final String normalized = this.normalize(inetHost);
        switch (resolvedAddressTypes) {
            case IPV4_ONLY: {
                return this.inet4Entries.get(normalized);
            }
            case IPV6_ONLY: {
                return this.inet6Entries.get(normalized);
            }
            case IPV4_PREFERRED: {
                final Inet4Address inet4Address = this.inet4Entries.get(normalized);
                return (inet4Address != null) ? inet4Address : ((InetAddress)this.inet6Entries.get(normalized));
            }
            case IPV6_PREFERRED: {
                final Inet6Address inet6Address = this.inet6Entries.get(normalized);
                return (inet6Address != null) ? inet6Address : ((InetAddress)this.inet4Entries.get(normalized));
            }
            default: {
                throw new IllegalArgumentException("Unknown ResolvedAddressTypes " + resolvedAddressTypes);
            }
        }
    }
    
    String normalize(final String inetHost) {
        return inetHost.toLowerCase(Locale.ENGLISH);
    }
}
